CREATE OR REPLACE PROCEDURE missing_bookings_report
(parm_season_Type IN CHAR,
 parm_season_year IN CHAR,
 parm_no_days_pre_Dept IN NUMBER,
 parm_sqlerrm OUT CHAR,
 parm_valid OUT BOOLEAN,
 parm_no_of_rows OUT NUMBER
 )

IS


-- Created on 12/11/2003 by LASHTON 
-- amended may 2004 Leigh , added dept date to report output

g_version                   VARCHAR2(20) := '001/001';
g_package_name     CONSTANT VARCHAR2(100) := 'STLBKGMS';
g_statement NUMBER := 0;
g_sqlerrm VARCHAR2(500);
g_sqlcode CHAR(20);
g_debug BOOLEAN := FALSE;

g_log_sequence jutil.application_log.log_sequence%TYPE;


/* List of Bookings In Data Warehouse (from Gemini) But Not In Stella */


v_app_key CHAR(8)  := 'STLBKGMS';
v_parameters          VARCHAR2(200) := substr('Params:'
                                                ||'seas:'||parm_season_type||parm_season_year||'days:'||parm_no_days_pre_dept
                                                 ,1,200);
v_error_message VARCHAR2(150);
v_run_date DATE;
v_num_bkgs NUMBER :=0;
v_num_missing_bkg NUMBER :=0;
v_pnr_mismatch_count NUMBER :=0;
v_season_mismatch_count NUMBER :=0;
v_ok_count NUMBER :=0;
v_pnr_found BOOLEAN := TRUE;
v_bkg_found BOOLEAN := TRUE;
v_msg VARCHAR2(200);
v_pnr_season_type season.season_type%TYPE;
v_pnr_crs_code pnr.crs_code%TYPE;
v_pnr_no pnr.pnr_no%TYPE;
v_crs_code crs.crs_code%TYPE;

CURSOR cursor_season  IS
/* looks only at last season and x future years */
SELECT season_type, season_year
FROM season s
WHERE
   (parm_season_type IS NULL AND
    s.season_start_date >= SYSDATE -365
    AND s.season_end_date < SYSDATE + (365*3))
OR (parm_season_type IS NOT NULL AND
    s.season_type = parm_season_type AND s.season_year = parm_season_year);




-- list of all scheduled flight bookings in the warehouse
CURSOR cursor_dwhse_gemini_bookings (vl_season_type CHAR, vl_season_year CHAR) IS
   SELECT b.booking_reference_no, b.season_type, b.season_year,
   b.pnr_no, b.crs_type_code, ts.supplier_code,
   bkg.holiday_departure_date
   FROM dataw.transport_scheduled_cost b, dataw.booking bkg, dataw.transport_sale ts
   WHERE b.season_type = vl_season_type
   AND   b.season_year = vl_season_year
   AND b.crs_type_code <> 9 -- exclude eurostar bookings
   AND bkg.season_year = b.season_year
   AND bkg.season_type = b.season_type
   AND bkg.booking_reference_no = b.booking_reference_no
   -- only do bookings due to depart within next x days
   -- ignore things that have already departed
   AND bkg.holiday_departure_date > SYSDATE 
   AND bkg.holiday_departure_date < SYSDATE + parm_no_days_pre_dept
   AND bkg.booking_date < SYSDATE -2 -- may take a while for pnr to get into stella - only when it is ticketed
   AND bkg.booking_status_code = 'BKG'
   AND bkg.season_year = ts.season_year
   AND bkg.season_type = ts.season_type
   AND bkg.booking_reference_no = ts.booking_reference_no
   AND ts.details_updated_date = bkg.amended_date
   AND ts.sequence = (SELECT MIN(ts2.sequence) FROM 
                     transport_sale ts2
                     WHERE ts2.season_year = ts.season_year
                     AND   ts2.season_type = ts.season_type
                     AND   ts2.booking_reference_no = ts.booking_reference_no
                     AND   ts2.details_updated_date = ts.details_updated_date)
   ;



BEGIN
      g_statement := 5;

      app_util.setup_logging(v_app_key,'LIVE','ALL');

      g_statement := 10;
      g_log_sequence := 1;



      v_msg := v_app_key||' started:'||to_char(SYSDATE,'yy-mon-dd hh24:mi:ss');
      p_common.debug_message(v_msg);
      app_util.file_log(v_msg);
      p_common.debug_message('Params:'||v_parameters);


      IF (parm_season_type IS NULL AND parm_season_year IS NOT NULL)
      THEN
            g_statement := 20;
            parm_sqlerrm := 'ERROR, parameters must be both null, or neither null';
            parm_valid:= FALSE;
            app_util.file_log(parm_sqlerrm);
            app_util.end_logging;
            RETURN;
      END IF;


      v_run_date := SYSDATE;
      v_num_bkgs := 0;
      v_num_missing_bkg := 0;
      v_msg := 'List of Scheduled Bookings In Data Warehouse (from Gemini) But Not In Stella Follows';
      p_common.debug_message(v_msg);
      app_util.file_log(v_msg);
      v_msg := '--------------------------------------------------------------------------';
      p_common.debug_message(v_msg);
      app_util.file_log(v_msg);

      FOR c_Season IN cursor_Season LOOP

        p_common.debug_message(c_season.season_type||c_season.season_year);
        g_statement  := 35;

        FOR c_range IN cursor_dwhse_gemini_bookings(c_season.season_type, c_season.season_year) LOOP
            BEGIN
               g_statement  := 40;
               v_num_bkgs  := v_num_bkgs + 1;
               v_pnr_found := TRUE;
               v_bkg_found := TRUE;


               IF c_range.crs_type_code = 0 THEN
                 v_crs_code := 'GRP'; -- groups
               ELSIF c_range.crs_type_code = 2 THEN
                 v_crs_code := 'WOR'; -- worldspan
               ELSIF c_range.crs_type_code IN (5,7) THEN
                 v_crs_code := 'GAL'; -- galileo
               ELSIF c_range.crs_type_code = 9 THEN
                 v_crs_code := 'EUR'; -- eurostar
               ELSE
                 v_crs_code := '???' ;
               END IF;

              -- now look in stella tables to see if exists
              -- log if it does not exist
              
              -- first try to find using pnr number and pnr primary keys
              
               SELECT pnr_no, season_type INTO v_pnr_no, v_pnr_season_type
               FROM pnr p
               WHERE p.pnr_no      = c_range.pnr_no
               AND   p.crs_code    = v_crs_code
               AND   p.season_year = c_range.season_year
               AND rownum = 1;

               g_statement := 45;


             EXCEPTION
                 WHEN no_data_found THEN
                    -- now try joining using booking reference instead
                   v_pnr_found := FALSE;
                   g_statement := 50;


                   BEGIN
                     SELECT pnr_no, p.crs_code  INTO v_pnr_no, v_pnr_crs_code
                     FROM pnr p
                     WHERE p.booking_reference_no = c_range.booking_reference_no
                     AND   p.season_type = c_range.season_type
                     AND   p.season_year = c_range.season_year
                     AND   ROWNUM = 1 -- don't care if more than 1
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
                   g_statement := 90;
                   v_msg := 'Bkg ref match but pnr/crs code mismatch,'||c_range.season_type||c_range.season_year||','||c_range.booking_reference_no||','||c_range.pnr_no||','||v_crs_code||' airline:'||c_range.supplier_code||',dept:'||c_range.holiday_departure_date||', pnr/crs in stella is :'||v_pnr_no||'/'||v_pnr_crs_code;
                   v_pnr_mismatch_count := v_pnr_mismatch_count + 1;
                   p_common.debug_message(v_msg);
                   app_util.file_log(v_msg);
              ELSE
                   g_statement := 110;
                   v_msg := 'dwhse pnr not found in stella,'||c_range.season_type||c_range.season_year||','||c_range.booking_reference_no||','||c_range.pnr_no||','||v_crs_code||',airline:'||c_range.supplier_code||',dept:'||c_range.holiday_departure_date;
                   p_common.debug_message(v_msg);
                   app_util.file_log(v_msg);
                   v_num_missing_bkg := v_num_missing_bkg + 1;                   
              END IF;
            ELSE -- pnr was found
              IF v_pnr_season_type <> c_range.season_type THEN
                g_statement := 112;
                v_msg := 'pnr found in stella but season different,'||c_range.season_type||c_range.season_year||','||c_range.booking_reference_no||','||c_range.pnr_no||','||v_crs_code||', airline:'||c_range.supplier_code||',dept:'||c_range.holiday_departure_date||', season in stella is :'||v_pnr_season_type;
                p_common.debug_message(v_msg);
                app_util.file_log(v_msg);
                v_season_mismatch_count := v_season_mismatch_count +1;
              ELSE
                g_statement := 115;
                v_msg := 'pnr found in stella,'||c_range.season_type||c_range.season_year||','||c_range.booking_reference_no||','||c_range.pnr_no||','||v_crs_code||',airline:'||c_range.supplier_code||',dept:'||c_range.holiday_departure_date;
                p_common.debug_message(v_msg);
                v_ok_count := v_ok_count + 1;
              END IF;
            
            END IF;

            g_statement := 120;

        END LOOP;    -- main bkg loop
        g_statement := 125;
      END LOOP;  -- end season loop



       g_statement := 999;
       v_msg := 'End of run';
       app_util.file_log( v_msg);
       app_util.file_log(null);
       v_msg := 'No. of days pre-dept bookings were looked at:'||parm_no_days_pre_dept;
       p_common.debug_message(v_msg);
       app_util.file_log( v_msg);
       v_msg := 'Bookings processed                   :'||v_num_bkgs;
       p_common.debug_message(v_msg);
       app_util.file_log( v_msg);
       v_msg := 'Number of missing bookings in total :'||v_num_missing_bkg;
       p_common.debug_message(v_msg);
       app_util.file_log( v_msg);
       v_msg := 'Number of pnr mismatches            :'||v_pnr_mismatch_count;
       p_common.debug_message(v_msg);
       app_util.file_log( v_msg);
       v_msg := 'Number of season mismatches        :'||v_season_mismatch_count;
       p_common.debug_message(v_msg);
       app_util.file_log( v_msg);
       v_msg := 'Number of matches                  :'||v_ok_count;
       p_common.debug_message(v_msg);
       app_util.file_log( v_msg);
       
       v_msg :=      v_app_key||' ended:'||to_char(SYSDATE,'yy-mon-dd hh24:mi:ss');
       p_common.debug_message(v_msg);
       dbms_output.put_line(v_msg);
       app_util.file_log( v_msg);

       parm_valid := TRUE;
       parm_sqlerrm:= null;
       parm_no_of_rows := v_num_bkgs;

       app_util.end_logging;

       COMMIT;
      

  EXCEPTION  -- main exception block

    WHEN OTHERS THEN
         g_sqlcode := SQLCODE;
         g_sqlerrm := substr(SQLERRM, 1,100);
         ROLLBACK;
         p_common.handle_error(v_app_key||''
                              ,g_statement       -- Code location.
                              ,'900'       -- First Choice error number.
                              ,SQLERRM
                              ,v_parameters
                              );
          app_util.file_log(g_statement,'Gen error'||g_sqlerrm);
          dbms_output.put_line(g_statement||' General error'||g_sqlerrm);

          app_util.end_logging;
          parm_valid := FALSE;
          parm_no_of_rows := 0;
          parm_sqlerrm :='Gen Error Stmt:'||g_statement||', '||substr(g_sqlerrm,1,100);
          app_util.log(g_package_name,NULL,g_log_sequence,'ALL','GENERROR','FATAL',
              'Others excp', substr('General error at:'||g_statement||','||g_sqlerrm||' '||v_parameters,1,200));



              
              
              
END missing_bookings_report;

------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------
/
