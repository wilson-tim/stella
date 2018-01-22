CREATE OR REPLACE PACKAGE p_stella_tlink_reconciliation IS

  /* ***************************************************************************** *
  
  Purpose
  -------
  Processing to reconcile stella bookings against Travelink
  
  History
  -------
  
  Date      By          Description                                    Version
  ----      ----------- ---------------------------------------------- -------
  04/11/05  Paul Butler  Initial version cloned from itour version        001
  * ***************************************************************************** */

  -- Public declarations
  g_version   VARCHAR2(20) := '001';
  g_app_key   jutil.application_log.application_key%TYPE := 'STLTLR';
  g_statement NUMBER := 0;
  g_sqlerrm   VARCHAR2(500);
  g_sqlcode   CHAR(20);
  g_debug     BOOLEAN := FALSE;

  g_log_sequence jutil.application_log.log_sequence%TYPE;

  ------------------------------------------------------------------------------
  ------------------------------------------------------------------------------

  PROCEDURE tlink_stella_reconcile(p_branch_code         IN CHAR, -- optional
                                   p_pnr_no              IN CHAR, -- optional
                                   p_season_type         IN CHAR, -- optional
                                   p_season_year         IN CHAR, -- optional
                                   p_airline_num         IN NUMBER, -- optional
                                   p_no_of_days_previous IN NUMBER, -- mandatory
                                   p_sqlerrm             OUT CHAR,
                                   p_no_of_rows          OUT CHAR,
                                   p_valid               OUT BOOLEAN);

  /*   this procedure is the Itour to stella reconciliation process
  - compares costs of tickets in stella data with that
  in Itour View
  */

  ------------------------------------------------------------------------------
  ------------------------------------------------------------------------------


  FUNCTION version RETURN VARCHAR2;

  /* ***************************************************************************** *
  
  Purpose
  -------
  Returns current version of the package.
  
  History
  -------
  
  Date      By          Description                                    Version
  --------  ----------- ---------------------------------------------- -------
  02/09/02  L Ashton     Initial version.                                 001
  
  Parameters
  ----------
  Name             Description                                         Default In/Out
  ---------------- --------------------------------------------------- ------- ------
  
  Notes:
  
  Example Call
  ------------
  SELECT p_stella.reconciliation.version
  FROM dual;
  
  * ***************************************************************************** */


  ------------------------------------------------------------------------------
  ------------------------------------------------------------------------------
  PROCEDURE tlink_missingbkg_report(
                                    /*
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        Purpose
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        -------
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        This procedure creates missing bookings  from stella  which exist in Travelink
                                                                                                                                                                                                                                                                                                
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          Date      By          Description                                    Version
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          --------  ----------- ---------------------------------------------- -------
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          05/12/04  Jyoti R.     Initial version.                                 001
                                                                                                                                                                                                                                                                                                
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        -- Created on 05/12/2004 by Jyoti
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        */
                                    
                                    parm_no_days_pre_dept IN NUMBER,
                                    p_sqlerrm             OUT CHAR,
                                    p_valid               OUT BOOLEAN,
                                    p_no_of_rows          OUT NUMBER);
  ------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------

END p_stella_tlink_reconciliation; --end of package
/
CREATE OR REPLACE PACKAGE BODY p_stella_tlink_reconciliation IS

  ------------------------------------------------------------------------------
  ------------------------------------------------------------------------------

  PROCEDURE tlink_stella_reconcile(p_branch_code         IN CHAR,
                                   p_pnr_no              IN CHAR,
                                   p_season_type         IN CHAR,
                                   p_season_year         IN CHAR,
                                   p_airline_num         IN NUMBER,
                                   p_no_of_days_previous IN NUMBER,
                                   p_sqlerrm             OUT CHAR,
                                   p_no_of_rows          OUT CHAR,
                                   p_valid               OUT BOOLEAN)
  
   IS
    /* some or all of these params will be passed */
    /* allows flexibilty in design so any level of reconciliation can be started */
  
    v_parameters VARCHAR2(200) := substr('Params, branch: ' ||
                                         p_branch_code || ' pnr:' ||
                                         p_pnr_no || ' seas:' ||
                                         p_season_type || p_season_year ||
                                         ' airl:' || p_airline_num ||
                                         ' days:' || p_no_of_days_previous,
                                         1,
                                         200);
  
    --v_tlink_count    NUMBER := 0;
    v_tlink_seat_amt dataw.transport_scheduled_cost_det.seat_cost%TYPE := 0;
    v_tlink_tax_amt  dataw.transport_scheduled_cost_det.tax%TYPE := 0;
    v_tlink_pnr_no   dataw.transport_scheduled_cost.pnr_no%TYPE := '';
    -- v_cancelled_ind  CHAR(1) := 'N';
    v_mainloop_count NUMBER := 0;
    v_error_message  VARCHAR2(150);
    v_run_date       DATE := SYSDATE;
  
    v_general_tax_tolerance_amt  NUMBER(20, 2);
    v_general_seat_tolerance_amt NUMBER(20, 2);
  
    v_stella_seat_amt NUMBER := 0;
    v_stella_tax_amt  NUMBER := 0;
  
    v_tkt_seat_amt       NUMBER := 0;
    v_tkt_tax_amt        NUMBER := 0;
    v_tax_tolerance_amt  NUMBER := 0;
    v_seat_tolerance_amt NUMBER := 0;
  
  
    --v_refund_seat_amt NUMBER(20,2) :=0;
    --v_refund_tax_amt NUMBER(20,2) :=0;
  
  
    v_discrepancy_amt NUMBER := 0;
  
    v_no_pnr_count     NUMBER := 0;
    v_tax_diff_count   NUMBER := 0;
    v_seat_diff_count  NUMBER := 0;
    v_full_match_count NUMBER := 0;
    v_part_match_count NUMBER := 0;
  
    v_seat_amt_ok BOOLEAN := TRUE;
    v_tax_amt_ok  BOOLEAN := TRUE;
  
    v_reason_code reason.reason_code%TYPE := NULL;
    v_valid       BOOLEAN := TRUE;
  
    -- exceptions
    ex_early_exit EXCEPTION;
    ex_major_error EXCEPTION;
    ex_reconciled EXCEPTION;
    v_msg VARCHAR2(200);
  
    --v_pnr_no pnr.pnr_no%TYPE;
  
    v_tlink_booking_ref VARCHAR2(15);
  
  
  
    CURSOR main_cursor(vl_branch_code CHAR, vl_pnr_no CHAR, vl_season_type CHAR, vl_season_year CHAR, vl_airline_num NUMBER, vl_num_days NUMBER)
    
    IS
    -- build where clause dynamically according to passed params
    -- all stella data that has not been previously reconciled or that has changed in some way since then
    
      SELECT p.pnr_no,
             p.pnr_creation_date,
             p.branch_code,
             p.crs_code,
             p.pnr_id,
             p.booking_reason_code,
             p.last_reconcile_attempt_date,
             p_stella_get_data.convert_season_type(p.season_type,
                                                   p.season_year,
                                                   p.branch_code) ||
             substr(p.season_year, 3, 2) derived_season
      
        FROM pnr p, branch b
       WHERE
      
       b.branch_code = p.branch_code
       AND b.season_type = p.season_type
       AND b.season_year = p.season_year
       AND b.data_warehouse_match_ind = 'N' -- do not need any datawarehouse match
       AND (p.booking_reconciled_ind = 'N' OR p.booking_reconciled_ind IS NULL) -- this gets reset to null when a change is made to a pnr/ticket/refund
       AND (p.booking_reason_code IS NULL) -- has not been through reconciliation before, this gets reset to null if a change is made in stella
      /* ticket numbers recycle so don't show anything older than 3 years */
       AND p.season_year > to_char(SYSDATE - (365 * 3), 'yyyy')
      /* user-defined params determining level of reconciliation */
       AND (vl_pnr_no IS NULL OR
       (vl_pnr_no IS NOT NULL AND p.pnr_no = vl_pnr_no))
       AND (vl_season_type IS NULL OR
       (vl_season_type IS NOT NULL AND p.season_type = vl_season_type))
       AND (vl_season_year IS NULL OR
       (vl_season_year IS NOT NULL AND p.season_year = vl_season_year))
       AND (vl_airline_num IS NULL OR
       (vl_airline_num IS NOT NULL AND EXISTS
        (SELECT NULL
            FROM ticket t
           WHERE t.airline_num = vl_airline_num
             AND t.pnr_id = p.pnr_id)))
       AND (vl_branch_code IS NULL OR
       (vl_branch_code IS NOT NULL AND p.branch_code = vl_branch_code))
       AND p.entry_date < SYSDATE - vl_num_days -- only check data entered a few days ago
       AND p.crs_code = 'AMA' -- Only Reconcile Amadeus bookings
      -- 04/11/05 Include only Travelink bookings
       AND p.branch_code IN
       (SELECT DISTINCT branch_code
          FROM branch_group_allocation
         WHERE group_code IN ('A', 'L', 'S', 'M', 'C'))
       GROUP BY p.pnr_no,
                p.pnr_creation_date,
                p.branch_code,
                p.crs_code,
                p.pnr_id,
                p.booking_reason_code,
                p.last_reconcile_attempt_date,
                p_stella_get_data.convert_season_type(p.season_type,
                                                      p.season_year,
                                                      p.branch_code) ||
                substr(p.season_year, 3, 2);
  
    CURSOR c_ticket_details(vl_pnr_id NUMBER) IS
      SELECT t.ticket_no,
             nvl(t.gb_tax_amt, 0) gb_tax_amt,
             nvl(t.remaining_tax_amt, 0) remaining_tax_amt,
             nvl(t.ub_tax_amt, 0) total_tax_amt,
             nvl(t.ub_tax_amt, 0) ub_tax_amt,
             -- work out tax variance allowed
             nvl(al.tax_amt_tolerance_level, 0) tax_tolerance_amt,
             nvl(al.seat_amt_tolerance_level, 0) seat_tolerance_amt,
             al.airline_num
      
        FROM ticket t, airline al
       WHERE t.pnr_id = vl_pnr_id
         AND t.airline_num = al.airline_num;
  
  BEGIN
    -- main block
    g_statement := 5;
  
    app_util.setup_logging(g_app_key, 'LIVE', 'ALL');
  
    g_statement    := 10;
    g_log_sequence := 1;
    app_util.file_log(g_statement, NULL);
  
    p_common.debug_message(g_app_key || ' started:' ||
                           to_char(SYSDATE, 'yy-mon-dd hh24:mi:ss'));
    p_common.debug_message('Params:' || v_parameters);
  
  
    --  v_run_date := SYSDATE;
  
  
    v_mainloop_count   := 0;
    v_no_pnr_count     := 0;
    v_tax_diff_count   := 0;
    v_seat_diff_count  := 0;
    v_full_match_count := 0;
    v_part_match_count := 0;
  
    g_statement := 15;
    -- get tolerance amounts from app registry
    IF app_util.is_number(app_util.get_registry_property(g_app_key,
                                                         'LIVE',
                                                         'ALL',
                                                         'GenTaxTol')) THEN
      v_general_tax_tolerance_amt := app_util.get_registry_property(g_app_key,
                                                                    'LIVE',
                                                                    'ALL',
                                                                    'GenTaxTol');
    ELSE
      v_msg := 'invalid registry setting for tax tolerance:' ||
               app_util.get_registry_property(g_app_key,
                                              'LIVE',
                                              'ALL',
                                              'GenTaxTol');
      dbms_output.put_line(v_msg);
      p_common.debug_message(v_msg);
      RAISE ex_major_error;
    END IF;
    IF app_util.is_number(app_util.get_registry_property(g_app_key,
                                                         'LIVE',
                                                         'ALL',
                                                         'GenSeatTol')) THEN
      v_general_seat_tolerance_amt := app_util.get_registry_property(g_app_key,
                                                                     'LIVE',
                                                                     'ALL',
                                                                     'GenSeatTol');
    ELSE
      v_msg := 'invalid registry setting for seat tolerance:' ||
               app_util.get_registry_property(g_app_key,
                                              'LIVE',
                                              'ALL',
                                              'GenseatTol');
      p_common.debug_message(v_msg);
      RAISE ex_major_error;
    END IF;
  
    g_statement := 18;
    --?? what IF NOT FOUND
    p_common.debug_message('Tax tol:' || v_general_tax_tolerance_amt ||
                           ' seat tol:' || v_general_seat_tolerance_amt);
  
    g_statement := 20;
  
    IF p_no_of_days_previous IS NULL THEN
      v_msg := 'Parameter for no. days cannot be null';
      p_common.debug_message(v_msg);
      RAISE ex_major_error;
    END IF;
  
  
    FOR cmain IN main_cursor(p_branch_code,
                             p_pnr_no,
                             p_season_type,
                             p_season_year,
                             p_airline_num,
                             p_no_of_days_previous) LOOP
      BEGIN
        v_mainloop_count := v_mainloop_count + 1;
      
        v_reason_code := NULL;
      
        g_statement := 30;
        p_common.debug_message(v_mainloop_count || 'pnr:' || cmain.pnr_id || ' ' ||
                               cmain.pnr_no || ' pnr creation dt:' ||
                               cmain.pnr_creation_date ||
                               cmain.derived_season || ' br:' ||
                               cmain.branch_code || ' reas:' ||
                               cmain.booking_reason_code ||
                               cmain.last_reconcile_attempt_date
                               
                               );
      
        app_util.file_log(g_statement, NULL);
      
        v_stella_seat_amt    := 0;
        v_stella_tax_amt     := 0;
        v_tax_tolerance_amt  := 0;
        v_seat_tolerance_amt := 0;
        v_tlink_seat_amt     := 0;
        v_tlink_tax_amt      := 0;
        v_discrepancy_amt    := 0;
      
        -- loop through all tickets for this pnr and calculate the stella accrual for each
        -- adding them up so can be compared to tlink accrual at pnr level
        g_statement := 32;
      
        FOR c_tkt IN c_ticket_details(cmain.pnr_id) LOOP
        
          g_statement := 132;
        
          -- add in the refund amts
          -- this procedure returns the new accrual after taking off any refunds
          p_stella_reconciliation.calc_stella_ticket_accrual(c_tkt.ticket_no,
                                                             c_tkt.ub_tax_amt,
                                                             c_tkt.gb_tax_amt,
                                                             c_tkt.remaining_tax_amt,
                                                             v_tkt_seat_amt,
                                                             v_tkt_tax_amt,
                                                             v_valid);
        
          p_common.debug_message('Ticket No ' || c_tkt.ticket_no);
          p_common.debug_message('UB Tax ' || c_tkt.ub_tax_amt);
          p_common.debug_message('GB Tax ' || c_tkt.gb_tax_amt);
          p_common.debug_message('Remianing Tax ' ||
                                 c_tkt.remaining_tax_amt);
          p_common.debug_message('Tkt Seat Amt ' || v_tkt_seat_amt);
          p_common.debug_message('Tkt Tax Amt ' || v_tkt_tax_amt);
        
        
          g_statement := 134;
          p_common.debug_message('tkt:' || c_tkt.ticket_no || ' seat:' ||
                                 v_tkt_seat_amt || ' tx:' || v_tkt_tax_amt ||
                                 ' airl:' || c_tkt.airline_num);
        
          IF NOT v_valid THEN
            v_msg := 'error in calc_stella_ticket_accrual, pnr:' ||
                     cmain.pnr_no;
            p_common.debug_message(v_msg);
            RAISE ex_major_error;
          END IF;
        
          -- add these ticket amounts to the total for the pnr so far
          v_stella_seat_amt    := v_stella_seat_amt + v_tkt_seat_amt;
          v_stella_tax_amt     := v_stella_tax_amt + v_tkt_tax_amt;
          v_tax_tolerance_amt  := v_tax_tolerance_amt +
                                  c_tkt.tax_tolerance_amt;
          v_seat_tolerance_amt := v_seat_tolerance_amt +
                                  c_tkt.seat_tolerance_amt;
        
          g_statement := 144;
        
        END LOOP; -- c_tkt loop
      
        g_statement := 146;
      
        v_discrepancy_amt := (v_stella_seat_amt + v_stella_tax_amt);
      
        p_common.debug_message('-->stella pnr costs, seat:' ||
                               v_stella_seat_amt || ' tx:' ||
                               v_stella_tax_amt || 'tol st:' ||
                               v_seat_tolerance_amt || 'tol tx:' ||
                               v_tax_tolerance_amt);
      
        -- get tlink figures for this pnr
      
        BEGIN
        
          g_statement := 31;
        
          p_common.debug_message('cmain.pnr_no ' || cmain.pnr_no);
        
          p_common.debug_message('cmain.pnr_creation_date' ||
                                 cmain.pnr_creation_date);
        
        
          -- Create Travelink Ref from Stella
          SELECT DISTINCT TRIM(bga.group_code) ||
                          lpad(p.booking_reference_no, 6, '0')
            INTO v_tlink_booking_ref
            FROM pnr p, branch_group_allocation bga
           WHERE p.pnr_no = cmain.pnr_no
             AND p.pnr_creation_date = cmain.pnr_creation_date
             AND p.branch_code = bga.branch_code;
        
        
          -- will raise no data found exception if booking does not exist in tlink
          /*          SELECT DISTINCT pnr_no
           INTO v_pnr_no
           FROM tlink_view t
          WHERE t.booking_ref =  tlink_booking_ref ; */
          -- pnr_no = cmain.pnr_no 
          -- AND trunc(booking_date) = cmain.pnr_creation_date; */
        
          --        p_common.debug_message('v_pnr_no ' || v_pnr_no); 
        
        
          g_statement := 32;
        
          -- amended to take only latest amendment date from tlink view
          SELECT nvl(t1.total_cost, 0), nvl(t1.total_tax, 0)
            INTO v_tlink_seat_amt, v_tlink_tax_amt
            FROM tlink_view t1
           WHERE t1.booking_ref = v_tlink_booking_ref;
          /* t1.pnr_no = cmain.pnr_no
          AND trunc(t1.booking_date) = cmain.pnr_creation_date; */
        
          g_statement := 34;
          --v_dwhse_tax_amt := nvl(calc_gemini_flight_tax_amt(cmain.season_type, cmain.season_year, cmain.booking_reference_no),0);
        
        EXCEPTION
          WHEN too_many_rows THEN
            v_error_message := 'Stella pnr / booking ref (' || cmain.pnr_no || '/' ||
                               cmain.pnr_creation_date || '/' ||
                               v_tlink_booking_ref ||
                               ' )relates to multiple PNRs in Travelink ';
            v_reason_code   := 'MP';
            RAISE ex_early_exit;
          
          WHEN no_data_found THEN
            /* v_error_message := 'Stella pnr (' || cmain.pnr_no || '/' ||
            cmain.pnr_creation_date ||
            ') has no scheduled match in Travelink ';  */
            v_error_message := 'Stella Booking  (' || v_tlink_booking_ref ||
                               ') has no scheduled match in Travelink ';
          
            -- mark as reason code MO and don't mark as reconciled
            v_reason_code  := 'MO';
            v_no_pnr_count := v_no_pnr_count + 1;
            RAISE ex_early_exit;
          
        
        END;
      
        p_common.debug_message('-->Travelink accrual:seat:' ||
                               v_tlink_seat_amt || ' tax:' ||
                               v_tlink_tax_amt || ' pnr:' ||
                               v_tlink_pnr_no);
        app_util.file_log(g_statement, NULL);
        g_statement := 40;
      
        g_statement := 90;
        app_util.file_log(g_statement, NULL);
        -- to get here count must be 1
        -- now start to compare amounts
        -- get the PNR details from tlink View
      
        g_statement := 130;
      
        -- now actually start to do the comparisons of amounts
        v_discrepancy_amt := 0;
      
        IF (v_stella_seat_amt + v_stella_tax_amt) =
           v_tlink_seat_amt + v_tlink_tax_amt THEN
          -- fully reconciled!!
          g_statement   := 150;
          v_reason_code := NULL; -- fully reconciled
          RAISE ex_reconciled;
        END IF;
      
        -- to get here, must be a variance, so see if it is due to tax variance or seat cost variance
        g_statement := 170;
        app_util.file_log(g_statement, NULL);
      
        -- now do reconcilation of seat as per appendix B of functional spec
        -- assume ok until proven otherwise
        v_seat_amt_ok     := TRUE;
        v_tax_amt_ok      := TRUE;
        v_discrepancy_amt := 0;
      
        IF v_tlink_seat_amt = v_stella_seat_amt THEN
          g_statement   := 190;
          v_reason_code := 'SE';
        
        ELSIF v_tlink_seat_amt BETWEEN
              (v_stella_seat_amt - v_general_seat_tolerance_amt) AND
              (v_stella_seat_amt + v_general_seat_tolerance_amt) THEN
          -- seat is within general tolerance
        
          v_reason_code := 'SE';
        
        ELSIF v_tlink_seat_amt BETWEEN
              (v_stella_seat_amt - v_seat_tolerance_amt) AND
              (v_stella_seat_amt + v_seat_tolerance_amt) THEN
          -- seat is within airline tolerance
        
          v_reason_code := 'SO';
        
        ELSE
          -- give up on this record, the seat cost is wrong
          p_common.debug_message('seatamt outside tols');
          v_seat_amt_ok     := FALSE;
          v_reason_code     := 'SV'; -- seat variance
          v_seat_diff_count := v_seat_diff_count + 1;
        
        END IF;
      
        -- now start of appendix c from func spec
      
        IF v_tlink_tax_amt = v_stella_tax_amt THEN
          g_statement   := 190;
          v_reason_code := 'TE';
        
        ELSIF v_tlink_tax_amt BETWEEN
              (v_stella_tax_amt - v_general_tax_tolerance_amt) AND
              (v_stella_tax_amt + v_general_tax_tolerance_amt) THEN
          -- tax is within tolerance
        
          v_reason_code := 'TE';
        
        ELSIF v_tlink_tax_amt BETWEEN
              (v_stella_tax_amt - v_tax_tolerance_amt) AND
              (v_stella_tax_amt + v_tax_tolerance_amt) THEN
          -- tax is within tolerance
        
          v_reason_code := 'TO';
        
        ELSE
          -- give up on this record, the tax cost is wrong
        
          p_common.debug_message('tax outside tols');
          -- give up on this record, the tax is wrong
          v_tax_amt_ok     := FALSE;
          v_reason_code    := 'TV';
          v_tax_diff_count := v_tax_diff_count + 1;
        END IF;
      
        IF NOT v_seat_amt_ok THEN
          v_discrepancy_amt := v_discrepancy_amt +
                               (v_stella_seat_amt - v_tlink_seat_amt);
        END IF;
        IF NOT v_tax_amt_ok THEN
          v_discrepancy_amt := v_discrepancy_amt +
                               (v_stella_tax_amt - v_tlink_tax_amt);
        END IF;
      
      
        IF v_tax_amt_ok AND v_seat_amt_ok THEN
          p_common.debug_message('both in tol');
          -- to get here, both tax and seat cost must be within variance
          -- so can be stamped as matched
          g_statement := 360;
          app_util.file_log(g_statement, NULL);
          -- This reason code WT is never get stampted into pnr table ?? (same as MIR)
          v_reason_code := 'WT'; -- within tolerance, but there was a variance
          RAISE ex_reconciled;
        
          /* else one or the other is outside variance so cannot reconcile */
        ELSE
          IF NOT v_tax_amt_ok AND NOT v_seat_amt_ok THEN
            v_reason_code := 'OV'; -- BOTH outside variance
          ELSIF (v_tax_amt_ok AND NOT v_seat_amt_ok) THEN
            v_reason_code := 'SV'; -- seat variance
          ELSIF (NOT v_tax_amt_ok AND v_seat_amt_ok) THEN
            v_reason_code := 'TV'; -- tax variance
          END IF;
          -- else use the reason code already determined above
          RAISE ex_early_exit;
        END IF;
      
        -- should never get here, if so there is an error
        v_msg := 'At statement:' || g_statement ||
                 ', system error, wrong path in code';
        p_common.debug_message(v_msg);
      
        RAISE ex_major_error;
      
      
      EXCEPTION
        WHEN ex_reconciled THEN
          -- fully reconciled, log and move on to next row
          g_statement := 370;
          app_util.file_log(g_statement, NULL);
          IF v_reason_code = NULL THEN
            v_full_match_count := v_full_match_count + 1;
          ELSE
            v_part_match_count := v_part_match_count + 1;
          END IF;
          v_error_message := 'PNR:' || cmain.pnr_id || ' ' || cmain.pnr_no ||
                             ' bkg:' || ' reconciled, level:' ||
                             v_reason_code;
        
          p_stella_reconciliation.update_recon_status(cmain.pnr_id,
                                                      TRUE,
                                                      NULL,
                                                      v_run_date);
        
          p_stella_reconciliation.update_recon_history(cmain.pnr_id,
                                                       g_app_key,
                                                       v_run_date,
                                                       nvl(v_reason_code,
                                                           'OK'),
                                                       NULL,
                                                       v_tlink_seat_amt,
                                                       v_tlink_tax_amt,
                                                       v_stella_seat_amt,
                                                       v_stella_tax_amt,
                                                       v_discrepancy_amt);
        
          p_common.debug_message(v_error_message);
        
        WHEN ex_early_exit THEN
          -- business reason why this pnr has failed reconciliation
          g_statement := 390;
          p_stella_reconciliation.update_recon_status(cmain.pnr_id,
                                                      FALSE,
                                                      v_reason_code,
                                                      v_run_date);
          p_common.debug_message(v_error_message);
          IF NOT v_tax_amt_ok OR NOT v_seat_amt_ok THEN
            -- slightly more serious error, log to exception table
            app_util.log(g_app_key,
                         NULL,
                         g_log_sequence,
                         'ALL',
                         'BUSRULE',
                         'WARNING',
                         cmain.pnr_no,
                         substr(v_error_message, 1, 150));
          END IF;
        
          p_stella_reconciliation.update_recon_history(cmain.pnr_id,
                                                       g_app_key,
                                                       v_run_date,
                                                       v_reason_code,
                                                       NULL,
                                                       v_tlink_seat_amt,
                                                       v_tlink_tax_amt,
                                                       v_stella_seat_amt,
                                                       v_stella_tax_amt,
                                                       v_discrepancy_amt);
        
      END; -- main loop block
    
    END LOOP; -- end main_cursor cmain loop
  
    g_statement := 999;
    app_util.file_log(g_statement, NULL);
  
    p_common.debug_message('Rows processed                   :' ||
                           v_mainloop_count);
    p_common.debug_message('Rows no pnr match                :' ||
                           v_no_pnr_count);
    p_common.debug_message('Rows tax outside tolerances      :' ||
                           v_tax_diff_count);
    p_common.debug_message('Rows seat cost outside tolerances:' ||
                           v_seat_diff_count);
    p_common.debug_message('Rows part-reconciled             :' ||
                           v_part_match_count);
    p_common.debug_message('Rows fully matched 100%          :' ||
                           v_full_match_count);
  
    p_common.debug_message(g_app_key || ' ended:' ||
                           to_char(SYSDATE, 'yy-mon-dd hh24:mi:ss'));
    dbms_output.put_line(g_app_key || ' ended:' ||
                         to_char(SYSDATE, 'yy-mon-dd hh24:mi:ss'));
  
    p_valid      := TRUE;
    p_sqlerrm    := NULL;
    p_no_of_rows := v_mainloop_count;
  
    app_util.end_logging;
  
    COMMIT;
  
  
  EXCEPTION
    -- main exception block
  
    WHEN ex_major_error THEN
      -- deliberate exit from loop due to logic failure
      g_sqlcode := SQLCODE;
      g_sqlerrm := substr(SQLERRM, 1, 100);
      app_util.file_log(g_statement, v_msg || ' Gen error' || g_sqlerrm);
      dbms_output.put_line(g_statement || ' major error' || g_sqlerrm ||
                           g_sqlcode);
    
      app_util.end_logging;
      p_valid      := FALSE;
      p_no_of_rows := 0;
      p_sqlerrm    := 'Major exception raised Stmt:' || g_statement || ', ' ||
                      substr(g_sqlerrm, 1, 100);
      app_util.log(g_app_key,
                   NULL,
                   g_log_sequence,
                   'ALL',
                   'GENERROR',
                   'FATAL',
                   'Major excp',
                   substr('Major exception at:' || g_statement || ',' ||
                          g_sqlerrm || ' ' || v_parameters,
                          1,
                          200));
    
    WHEN OTHERS THEN
      g_sqlcode := SQLCODE;
      g_sqlerrm := substr(SQLERRM, 1, 100);
      p_common.handle_error(g_app_key || '.tlink_stella_recon',
                            g_statement -- Code location.
                           ,
                            '900' -- First Choice error number.
                           ,
                            SQLERRM,
                            v_parameters);
      app_util.file_log(g_statement, 'Gen error' || g_sqlerrm);
      dbms_output.put_line(g_statement || ' General error' || g_sqlerrm);
    
      app_util.end_logging;
      p_valid      := FALSE;
      p_no_of_rows := 0;
      p_sqlerrm    := 'Gen Error Stmt:' || g_statement || ', ' ||
                      substr(g_sqlerrm, 1, 100);
      app_util.log(g_app_key,
                   NULL,
                   g_log_sequence,
                   'ALL',
                   'GENERROR',
                   'FATAL',
                   'Others excp',
                   substr('General error at:' || g_statement || ',' ||
                          g_sqlerrm || ' ' || v_parameters,
                          1,
                          200));
    
  
  END tlink_stella_reconcile; -- end procedure

  ------------------------------------------------------------------------------
  ------------------------------------------------------------------------------
  ------------------------------------------------------------------------------

  FUNCTION version RETURN VARCHAR2
  /* ***************************************************************************** *
    
    Date      By          Description                                         Version
    ----      ----------- --------------------------------------------------- -------
    10/01/03  A.James     Initial version.                                      001
    
    * ***************************************************************************** */
   IS
  BEGIN
    RETURN 'Package: ' || g_app_key || '; Version: ' || g_version;
  END version;

  ------------------------------------------------------------------------------
  ------------------------------------------------------------------------------
  ------------------------------------------------------------------------------
  PROCEDURE tlink_missingbkg_report(parm_no_days_pre_dept IN NUMBER,
                                    p_sqlerrm             OUT CHAR,
                                    p_valid               OUT BOOLEAN,
                                    p_no_of_rows          OUT NUMBER)
  
   IS
  
    g_log_sequence jutil.application_log.log_sequence%TYPE;
  
    /* List of Bookings/PNRs  in tlink But Not In Stella */
  
  
    v_parameters VARCHAR2(200) := substr('Params:' || 'days:' ||
                                         parm_no_days_pre_dept,
                                         1,
                                         200);
    --v_error_message      VARCHAR2(150);
    v_run_date           DATE;
    v_num_bkgs           NUMBER := 0;
    v_missing_count      NUMBER := 0;
    v_ok_count           NUMBER := 0;
    v_pnr_mismatch_count NUMBER := 0;
    v_pnr_found          BOOLEAN := TRUE;
    v_bkg_found          BOOLEAN := TRUE;
    v_msg                VARCHAR2(200);
    v_pnr_no             pnr.pnr_no%TYPE;
    v_crs_code           crs.crs_code%TYPE;
    v_pnr_crs_code       crs.crs_code%TYPE;
    v_pnr_creation_date  pnr.pnr_creation_date%TYPE;
  
  
  
    -- List of all the booking in Travelink ,departs in last x weeks ,
    CURSOR cursor_tlink_bookings IS
      SELECT DISTINCT pnr_no, booking_date, booking_source, departure_date
        FROM tlink_view
       WHERE departure_date > SYSDATE
            -- only do bookings due to depart within next x  weeks
         AND departure_date < SYSDATE + parm_no_days_pre_dept
         AND booking_status = 'C'; -- Only Confirmed bookings in Travelink
  
  
  BEGIN
    g_statement := 5;
  
    app_util.setup_logging(g_app_key, 'LIVE', 'ALL');
  
    g_statement    := 10;
    g_log_sequence := 1;
  
    v_msg := g_app_key || ' started:' ||
             to_char(SYSDATE, 'yy-mon-dd hh24:mi:ss');
    p_common.debug_message(v_msg);
    app_util.file_log(v_msg);
    p_common.debug_message('Params:' || v_parameters);
  
    --v_run_date      := SYSDATE;
    v_num_bkgs      := 0;
    v_missing_count := 0;
    v_msg           := 'List of Bookings In Travelink But Not In Stella Follows';
    p_common.debug_message(v_msg);
    app_util.file_log(v_msg);
    v_msg := '--------------------------------------------------------------------------';
    p_common.debug_message(v_msg);
    app_util.file_log(v_msg);
  
    g_statement := 35;
  
    FOR c_range IN cursor_tlink_bookings LOOP
      BEGIN
        g_statement := 40;
        v_num_bkgs  := v_num_bkgs + 1;
        v_pnr_found := TRUE;
        v_bkg_found := TRUE;
      
        -- value of source code will be (GAL,WOR,AMA)  -- Will we recive GAL bookings in future ???
        v_crs_code := c_range.booking_source;
      
        -- now look in stella tables to see if exists
        -- log if it does not exist
        SELECT pnr_no, p.crs_code, p.pnr_creation_date
          INTO v_pnr_no, v_pnr_crs_code, v_pnr_creation_date
          FROM pnr p
         WHERE p.pnr_no = c_range.pnr_no
           AND p.crs_code = v_crs_code
           AND p.pnr_creation_date = c_range.booking_date
              -- 04/11/05 Include only Travelink bookings
           AND p.branch_code IN
               (SELECT DISTINCT branch_code
                  FROM branch_group_allocation
                 WHERE group_code IN ('A', 'L', 'S', 'M', 'C'));
        -- AND   rownum = 1;   -- there is a unique key on (pnr_no,crs_code,pnr_creation_date)
      
        g_statement := 40;
      
      
      EXCEPTION
        WHEN no_data_found THEN
          v_pnr_found := FALSE;
          g_statement := 40;
        
          BEGIN
            -- Try finding without checking booking date
          
            SELECT pnr_no, p.crs_code, p.pnr_creation_date
              INTO v_pnr_no, v_pnr_crs_code, v_pnr_creation_date
              FROM pnr p
             WHERE p.pnr_no = c_range.pnr_no
               AND p.crs_code = v_crs_code
               AND rownum = 1 -- don't care if more than 1
            ;
          
          EXCEPTION
            WHEN no_data_found THEN
              -- both methods of joining have failed
              v_bkg_found := FALSE;
              g_statement := 55;
          END;
        
      
      END;
    
      IF v_pnr_found = FALSE THEN
        IF v_bkg_found = TRUE THEN
          -- missing from stella, so log
          g_statement          := 90;
          v_msg                := 'PNR match but booking date/pnr_creation date mismatch,' ||
                                  c_range.pnr_no || ',' ||
                                  c_range.booking_date || '/' ||
                                  v_pnr_creation_date || ',' ||
                                  c_range.booking_source;
          v_pnr_mismatch_count := v_pnr_mismatch_count + 1;
          p_common.debug_message(v_msg);
          app_util.file_log(v_msg);
        ELSE
          -- missing from stella, so log
          g_statement := 90;
          v_msg       := 'Travelink pnr not found in Stella,' ||
                         c_range.pnr_no || ',' || c_range.booking_date || ',' ||
                         c_range.booking_source;
          p_common.debug_message(v_msg);
          app_util.file_log(v_msg);
          v_missing_count := v_missing_count + 1;
        
        END IF;
      ELSE
        -- pnr found
        g_statement := 100;
        v_msg       := 'PNR found in stella,' || c_range.pnr_no || ',' ||
                       c_range.booking_date || ',' ||
                       c_range.booking_source;
        p_common.debug_message(v_msg);
        v_ok_count := v_ok_count + 1;
      
      END IF;
    
    END LOOP; -- main bkg loop
  
    g_statement := 999;
    v_msg       := 'End of run';
    app_util.file_log(v_msg);
    app_util.file_log(NULL);
    v_msg := 'No. of days pre-dept bookings were looked at:' ||
             parm_no_days_pre_dept;
    p_common.debug_message(v_msg);
    app_util.file_log(v_msg);
    v_msg := 'Bookings processed                   :' || v_num_bkgs;
    p_common.debug_message(v_msg);
    app_util.file_log(v_msg);
    v_msg := 'Number of missing bookings in total :' || v_missing_count;
    p_common.debug_message(v_msg);
    app_util.file_log(v_msg);
    v_msg := 'Number of pnr creation date mismatches       :' ||
             v_pnr_mismatch_count;
    p_common.debug_message(v_msg);
    app_util.file_log(v_msg);
    v_msg := 'Number of matches                  :' || v_ok_count;
    p_common.debug_message(v_msg);
    app_util.file_log(v_msg);
    v_msg := g_app_key || ' ended:' ||
             to_char(SYSDATE, 'yy-mon-dd hh24:mi:ss');
    p_common.debug_message(v_msg);
    dbms_output.put_line(v_msg);
    app_util.file_log(v_msg);
  
    p_valid      := TRUE;
    p_sqlerrm    := NULL;
    p_no_of_rows := v_num_bkgs;
  
    app_util.end_logging;
  
    COMMIT;
  
  
  EXCEPTION
    -- main exception block
  
    WHEN OTHERS THEN
      g_sqlcode := SQLCODE;
      g_sqlerrm := substr(SQLERRM, 1, 100);
      ROLLBACK;
      p_common.handle_error(g_app_key || '',
                            g_statement -- Code location.
                           ,
                            '900' -- First Choice error number.
                           ,
                            SQLERRM,
                            v_parameters);
      app_util.file_log(g_statement, 'Gen error' || g_sqlerrm);
      dbms_output.put_line(g_statement || ' General error' || g_sqlerrm);
    
      app_util.end_logging;
      p_valid      := FALSE;
      p_no_of_rows := 0;
      p_sqlerrm    := 'Gen Error Stmt:' || g_statement || ', ' ||
                      substr(g_sqlerrm, 1, 100);
      app_util.log(g_app_key,
                   NULL,
                   g_log_sequence,
                   'ALL',
                   'GENERROR',
                   'FATAL',
                   'Others excp',
                   substr('General error at:' || g_statement || ',' ||
                          g_sqlerrm || ' ' || v_parameters,
                          1,
                          200));
    
  END tlink_missingbkg_report;
  ------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------


END p_stella_tlink_reconciliation;

