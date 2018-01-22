CREATE OR REPLACE PACKAGE p_bsp_reconciliation IS

/* ***************************************************************************** *

Purpose
-------
reconciliation processes for bsp stella system

History
-------

Date      By          Description                                    Version
----      ----------- ---------------------------------------------- -------
21/01/03  L Ashton     Initial version.                                 001
16/10/03  L Ashton     use debug not dbms_output.put_line(); output     001/006
10/11/03  L Ashton     allow update of stella ticket with bsp cost      001/007
25/01/05  Jyoti        Added new transaction type MD5
* ***************************************************************************** */

  -- Public declarations
  g_version                   VARCHAR2(20) := '001/007';
  g_package_name     CONSTANT VARCHAR2(100) := 'STLBSPR';
  g_statement NUMBER := 0;
  g_sqlerrm VARCHAR2(500);
  g_sqlcode CHAR(20);
  g_debug BOOLEAN := FALSE;

  g_log_sequence jutil.application_log.log_sequence%TYPE;     

PROCEDURE bsp_reconcile
/* some or all of these params will be passed */
/* allows flexibilty in design so any level of reconciliation can be started */
/* can be run at airline  levelor for just one transaction (useful for testing*/
           (
           p_airline_num IN NUMBER,
           p_bsp_trans_id IN NUMBER,
           p_bsp_yymm IN CHAR,  -- month and year to process if we want to process just one
           p_sqlerrm OUT CHAR,
           p_no_of_rows OUT CHAR,
           p_valid OUT BOOLEAN);

FUNCTION update_stella_ticket_amts (p_ticket_no           NUMBER,
                                    p_pnr_id              NUMBER,
                                    p_commissionable_amt  NUMBER,                                    
                                    p_net_fare_amt        NUMBER,
                                    p_commission_amt      NUMBER,
                                    p_supp_commission_amt NUMBER,
                                    p_ub_tax_amt          NUMBER, 
                                    p_gb_tax_amt          NUMBER,
                                    p_remaining_tax_amt   NUMBER
) RETURN NUMBER;



  
FUNCTION Version
  RETURN VARCHAR2
  
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
  SELECT p_bsp_reconciliation.version
  FROM dual;

  * ***************************************************************************** */
  ;

 ------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------  
 ------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------  
  
END p_bsp_reconciliation;  --end of package
/
CREATE OR REPLACE PACKAGE BODY p_bsp_reconciliation IS
------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------  


  FUNCTION version
  RETURN VARCHAR2
  IS
  BEGIN
    RETURN 'Package: '||g_package_name||'; Version: '||g_version;
  END Version;

------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------  
------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------  
PROCEDURE bsp_reconcile
/* some or all of these params will be passed */
/* allows flexibilty in design so any level of reconciliation can be started */
/* can be run at airline  levelor for just one transaction (useful for testing*/
           (
           p_airline_num IN NUMBER,
           p_bsp_trans_id IN NUMBER,
           p_bsp_yymm IN CHAR,  -- month and year to process if we want to process just one
           p_sqlerrm OUT CHAR,
           p_no_of_rows OUT CHAR,
           p_valid OUT BOOLEAN)
IS
  
  v_app_key CHAR(8)  := 'STLBSPR';
  v_parameters          VARCHAR2(200) := substr('Params, airline: '
                                                ||p_airline_num||' Tranid:'
                                                ||p_bsp_trans_id||'yymm:'
                                                ||p_bsp_yymm
                                                 ,1
                                                 ,200
                                                 );

v_mainloop_count NUMBER:=0;                                                 
v_full_match_count NUMBER :=0;
v_tol_match_count NUMBER :=0;
v_count_failed NUMBER:=0;
v_count_tkt_trans NUMBER:=0;
v_count_tkt_reconciled NUMBER:=0;
v_count_ref_reconciled NUMBER:=0;
v_count_refund_trans NUMBER:=0;
v_count_dup_trans NUMBER :=0;
v_error_message VARCHAR2(400);
v_run_date DATE;
        
v_reason_code  reason.reason_code%TYPE := NULL;
v_valid BOOLEAN:= TRUE;
v_return_valid BOOLEAN :=TRUE;
-- exceptions
ex_early_exit EXCEPTION;
ex_major_error EXCEPTION;
ex_reconciled EXCEPTION;
ex_iata_transaction EXCEPTION;
v_msg VARCHAR2(200);
v_tran_group CHAR(1):= NULL; 



v_num_iata_recs NUMBER :=0;
v_tran_details VARCHAR2(200);
v_existing_bsp_trans_id ticket.bsp_trans_id%TYPE;

v_seat_amt ticket.selling_fare_amt%TYPE :=0;
v_tax_amt  ticket.selling_fare_amt%TYPE :=0;
v_bsp_seat_cost ticket.selling_fare_amt%TYPE :=0;
v_sector_payment_ind airline.sector_payment_ind%TYPE;

v_bkging_commission_amt ticket.selling_fare_amt%TYPE;
v_bkg_payment_commission_amt airline.bkg_payment_commission_amt%TYPE;

v_airline_penalty_amt bsp_transaction.airline_penalty_amt%TYPE;


v_date DATE;
v_allow_update CHAR(1);
v_count_stella_updated NUMBER :=0;
v_upd NUMBER :=0;
v_tkt_pnr_id pnr.pnr_id%TYPE;


                                                 
CURSOR main_cursor  (vl_airline_num NUMBER,vl_bsp_trans_id NUMBER, vl_bsp_yymm CHAR)

IS 
  -- build where clause dynamically according to passed params
  -- all stella bsp transaction data that has not been previously reconciled or that has changed in some way since then

  SELECT b.ticket_no, b.tax_amt, b.supp_commission_amt,
    b.commissionable_amt, b.commission_amt, b.net_fare_amt, b.ccy_code,
    b.bsp_trans_id, b.transaction_code,  b.bsp_crs_code,
    b.airline_num, b.iata_num, b.refund_document_no,
    b.conjunction_ind, nvl(a.bsp_tolerance_amt,0) tolerance_amt,
    b.bsp_period_ending_date,
    b.airline_penalty_amt,
    b.balance_payable_amt,
    b.ub_tax_amt bsp_ub_tax_amt,
    b.gb_tax_amt bsp_gb_tax_amt,
    b.remaining_tax_amt bsp_remaining_tax_amt
        
  FROM bsp_transaction b, airline a
  WHERE b.reconciled_ind <> 'Y'
  AND nvl(vl_airline_num,b.airline_num) = b.airline_num
  AND nvl(vl_bsp_trans_id, b.bsp_trans_id) = b.bsp_trans_id
  AND nvl(vl_bsp_yymm,to_char(b.bsp_period_ending_date,'yymm')) = to_char(b.bsp_period_ending_date,'yymm')
  AND a.airline_num (+)= b.airline_num
  ORDER BY b.bsp_trans_id
  -- don't just process one month's bsp data, but also process any left over from last time
  -- in case corresponding tickets have been entered/amended since last run
  -- outer join to airline in case the airline number does not exist in stella
  
  ;


 
  BEGIN   -- main block
      g_statement := 5;
      
      app_util.setup_logging(v_app_key,'LIVE','ALL');
      
      g_statement := 10;      
      
      -- v007 Leigh, new functionality to allow update of stella seat/tax amount with that taken from bsp file
      v_allow_update := app_util.get_Registry_Property(v_app_key,'LIVE','ALL', 'AllowStellaUpdate'); -- either Y or N
      IF v_allow_update NOT IN ('Y','N') THEN
         v_msg := 'invalid registry setting (not Y/N) for allow stella update:'||v_allow_update;
         dbms_output.put_line(v_msg);
         p_common.debug_message(v_msg);
         RAISE ex_major_error;
      END IF;

    
      
      
      
      g_log_sequence :=1;
      app_util.file_log(g_statement,null);      
      
      p_common.debug_message(v_app_key||' started:'||to_char(SYSDATE,'yy-mon-dd hh24:mi:ss'));
      
      p_common.debug_message('Params:'||v_parameters||' allow updates:'||v_allow_update);

      
      v_run_date := SYSDATE;
      
      
      v_mainloop_count  :=0;                         
      v_count_failed    :=0;      
      v_count_refund_trans :=0;
      v_count_tkt_trans :=0;
      v_count_dup_trans :=0;

      g_statement := 12;      
      IF p_bsp_yymm IS NOT NULL THEN
        BEGIN      
           v_date := to_date('01'||p_bsp_yymm,'ddyymm');
        EXCEPTION
        WHEN OTHERS THEN
             v_msg := 'Invalid month/yy specified in run:'||p_bsp_yymm;
             RAISE ex_major_error;
        END;
      END IF;


      
      
      FOR cmain IN main_cursor(p_airline_num, p_bsp_trans_id, p_bsp_yymm)
      LOOP
        BEGIN
          v_mainloop_count := v_mainloop_count + 1;
          v_tran_group := NULL;
          v_reason_code := NULL;
          v_seat_amt :=0;
          v_tax_amt  :=0;         

          v_tran_details := cmain.transaction_code||': tkt:'||cmain.ticket_no||'/'||cmain.airline_num||'/'||cmain.iata_num||' id:'||cmain.bsp_trans_id;

          g_statement := 30;
          v_error_message:= 'tkt:,'||cmain.ticket_no||
            ',tax:,'||cmain.tax_amt||', suppcomm:,'||
            cmain.supp_commission_amt||',commable:,'||
            cmain.commissionable_amt||',comm:,'||cmain.commission_amt||',net:,'||
            cmain.net_fare_amt||','||cmain.ccy_code||
            ', trn:,'||cmain.bsp_trans_id||
            ',trn:,'||cmain.transaction_code||           
            ',air:,'||cmain.airline_num||','||cmain.iata_num||',rfnd:'||cmain.refund_document_no||
            ' cnj:'||cmain.conjunction_ind||
            ' airpen:'||cmain.airline_penalty_amt||
            ' balpay:'||cmain.balance_payable_amt;
 
          p_common.debug_message(v_mainloop_count||v_error_message);  
          

          g_statement := 32;                  
          app_util.file_log(g_statement,null);                                      

          BEGIN
            g_statement := 34;                  
            SELECT nvl(a.sector_payment_ind,'N'), a.bkg_payment_commission_amt
            INTO v_sector_payment_ind, v_bkg_payment_commission_amt
            FROM airline a
            WHERE a.airline_num = cmain.airline_num;
                      
                      
            g_statement := 180;                  
            EXCEPTION
            WHEN no_data_found THEN
              p_common.debug_message('Warning, Invalid airline: BSP tran ('||v_tran_details||') has no matching airline in stella');
  
              -- mark as reason code IQ and don't mark as reconciled
              UPDATE bsp_transaction b
              SET b.reconciled_ind = 'N',              
                b.last_reconciled_date = SYSDATE,
                b.reason_code = 'IQ'
              WHERE b.bsp_trans_id = cmain.bsp_trans_id;
  
              RAISE ex_early_exit;             
            
          END; -- airline select block

          
          v_bsp_seat_cost := cmain.commissionable_amt - cmain.supp_commission_amt - cmain.commission_amt;  

          
          IF cmain.transaction_code = 'SPD'
          OR cmain.airline_num IN (992,993) THEN
            -- special airlines used to denote special bsp transactions for IATA costs
            -- don't reconcile these to stella 
            -- but write out to log so users can report on them
            g_statement := 40;
            v_num_iata_recs := v_num_iata_recs + 1;
            UPDATE bsp_transaction b
            SET b.reconciled_ind = 'Y',              
              b.last_reconciled_date = SYSDATE,
              b.reason_code = 'II'
            WHERE b.bsp_trans_id = cmain.bsp_trans_id;
            g_statement := 50;
            p_common.debug_message('IATA (992/993) trans:,'|| v_error_message);
            
            RAISE ex_iata_transaction;
          END IF;
          
          g_statement := 60;
          --find corresponding transaction in stella data                                                                                  
          
          IF cmain.transaction_code IN ('TKT','MCO','CAN','MPD','MD5') THEN
            -- look in tickets
            BEGIN
              -- find stella ticket and check if it has already been reconciled before
              g_statement := 70;
              v_count_tkt_trans := v_count_tkt_trans + 1;
              v_tran_group := 'T';
              
              
              SELECT t.bsp_trans_id, t.pnr_id
              INTO v_existing_bsp_trans_id, v_tkt_pnr_id
              FROM ticket t
              WHERE t.ticket_no = cmain.ticket_no;
              
              g_statement := 80;
              
              IF v_existing_bsp_trans_id IS NOT NULL THEN
                v_error_message := 'BSP tran ('||v_tran_details||') has already been matched in stella';
                -- mark as reason code I2 and don't mark as reconciled
                v_reason_code := 'I2';
                v_count_dup_trans := v_count_dup_trans + 1;
                RAISE ex_early_exit;             
              END IF;
              
              -- now compare amounts
              g_statement := 90;              
              v_seat_amt := p_stella_reconciliation.calc_seat_amt(cmain.ticket_no) /* does not take into account refunds */;
              v_tax_amt  := p_stella_reconciliation.calc_ticket_tax_total(cmain.ticket_no); /* does not take into account refunds */
              
              
              g_statement := 100;
              
              p_common.debug_message('stl tkt:'||cmain.ticket_no||', seat:'||v_seat_amt||' tax:'||v_tax_amt||' bspseat:'||v_bsp_seat_cost||' bsptax:'||cmain.tax_amt||' tol:'||cmain.tolerance_amt||' ub:'||cmain.bsp_ub_tax_amt||' gb:'||cmain.bsp_gb_tax_amt||' rem:'||cmain.bsp_remaining_tax_amt  );

              -- first check if exact match
              
              IF v_seat_amt = v_bsp_seat_cost AND v_tax_amt = cmain.tax_amt THEN
                -- mark as reconciled
                v_reason_code := 'IS';
                RAISE ex_reconciled;             
              END IF;

              
              
              
              
              -- if total is within tolerance then mark reconciled as well, 
              -- but with different reason code              
              -- remember tax has no tolerance
              IF v_seat_amt  BETWEEN (v_bsp_seat_cost  - cmain.tolerance_amt) AND (v_bsp_seat_cost + cmain.tolerance_amt) 
              AND (v_tax_amt = cmain.tax_amt) THEN
                -- mark as reconciled
                v_reason_code := 'IO';
                RAISE ex_reconciled;             
              END IF;

              
              -- to get here, must be outside tolerance
              g_statement := 105;              

                                            
              IF v_allow_update = 'Y' AND cmain.transaction_code <>'CAN' 
              --AND cmain.bsp_period_ending_date > '01-aug-2003' -- previously only wanted to new stuff 
              AND cmain.bsp_crs_code <> 'MANU' 
              THEN
                -- new functionality added in v007 - update the stella ticket with the bsp figures
                -- because bsp figures assumed to be accurate. 
                -- exclude MANUally entered BSP transactions - we need to reconcile these properly
                -- exclude cancellations - these need to be manually voided in stella           
                v_upd:= update_stella_ticket_amts(cmain.ticket_no,
                                                  v_tkt_pnr_id, 
                                                  cmain.commissionable_amt,
                                                  cmain.net_fare_amt, 
                                                  cmain.commission_amt,
                                                  cmain.supp_commission_amt, 
                                                  cmain.bsp_ub_tax_amt,
                                                  cmain.bsp_gb_tax_amt,
                                                  cmain.bsp_remaining_tax_amt);

                p_common.debug_message('upd stella tkt:');
                                                  
                IF v_upd < 1 OR v_upd > 1 THEN
                      v_msg := 'Update stella ticket amts error, wrong no of rows:'||v_upd||':'||v_tran_details;
                      RAISE ex_major_error;
                END IF;                
                g_statement := 107;                                          
                v_count_stella_updated := v_count_stella_updated + 1;
                v_reason_code := 'IZ'; -- autooverride of amounts, assume matched
                RAISE ex_reconciled;  -- not really reconciled properly, but assume so by using IZ           
                
              END IF; -- if allow_update = 'Y'
              
                            
              IF v_seat_amt <> v_bsp_seat_cost AND (v_tax_amt <> cmain.tax_amt) THEN
                v_error_message := 'Seat AND tax discrepancy on bsp tran:('||v_tran_details||') ';
                -- don't mark as reconciled
                v_reason_code := 'IG';
                RAISE ex_early_exit;             
              END IF;

              
              IF v_seat_amt <> v_bsp_seat_cost THEN
                v_error_message := 'Seat/fare discrepancy on bsp tran:('||v_tran_details||') ';
                -- don't mark as reconciled
                v_reason_code := 'IF';
                RAISE ex_early_exit;             
              END IF;
              
              
              IF v_tax_amt <> cmain.tax_amt THEN
                v_error_message := 'Tax discrepancy on bsp tran:('||v_tran_details||') seat cost was OK';
                -- don't mark as reconciled
                v_reason_code := 'IT';
                RAISE ex_early_exit;             
              END IF;

              
              g_statement := 110;
              -- should never get here, if so there is an error
              v_msg := 'At statement:'||g_statement||', system error, wrong path in code';
              p_common.debug_message(v_msg||' '||v_error_message);                  
       
              RAISE ex_major_error;                                      
          
            EXCEPTION
            WHEN no_data_found THEN
              v_error_message := 'BSP tran ('||v_tran_details||') no matching tkt in stella';
              -- mark as reason code ID and don't mark as reconciled
              v_reason_code := 'ID';
              RAISE ex_early_exit;             
                 
            END; -- select from ticket block
            
          ELSIF cmain.transaction_code IN ('ACM','ADM','MAN','RFN','SPD','SSA','SPC') THEN
                -- refund type transaction
               g_statement := 150;
               v_tran_group := 'R';
               v_count_refund_trans := v_count_refund_trans + 1;
               BEGIN
               
               
                    SELECT bsp_trans_id
                    INTO v_existing_bsp_trans_id
                    FROM refund_batch t
                    WHERE t.refund_document_no = cmain.refund_document_no; 
                    
                    g_statement := 155;
              
                    IF v_existing_bsp_trans_id IS NOT NULL THEN
                         v_error_message := 'BSP tran ('||v_tran_details||') has already been matched in stella';
                         -- mark as reason code I2 and don't mark as reconciled
                         v_reason_code := 'I2';
                         RAISE ex_early_exit;             
                    END IF; 
                                       
                    g_statement := 160;
                    
               EXCEPTION
               WHEN no_data_found THEN
               
                   g_statement := 170;

                  
                   IF v_sector_payment_ind = 'Y' AND cmain.transaction_code = 'ACM' THEN
                      -- may be a special ACM for an airline for booking payment commission i.e. an airline where sector payment ind is set
                      -- this is how airlines such as BA pay us our commission - through one bsp transaction per month
                      BEGIN 
                        g_statement := 175;
                        
                        SELECT SUM(nvl(t.published_fare_amt,0) ) * (v_bkg_payment_commission_amt/100)
                        INTO v_bkging_commission_amt                      
                        FROM ticket t
                        WHERE t.airline_num = cmain.airline_num
                        AND   to_char(t.ticket_issue_date,'mon-yy') = to_char(cmain.bsp_period_ending_date,'mon-yy')
                        ; 
                        p_common.debug_message('SEC PAY ACM, total :'|| v_bkging_commission_amt||' bsp amt:'||v_bsp_seat_cost);

                        

                        
                      END;                      
                      
                      IF v_bkging_commission_amt = v_bsp_seat_cost THEN
                          -- mark as reconciled
                          p_common.debug_message('Booking payment commission matches');
                          v_reason_code := 'IA';
                          RAISE ex_reconciled;             
                      ELSE
                        v_error_message := 'BSP tran ('||v_tran_details||') bkg comm unmatched';                      
                        v_reason_code := 'IB'; -- unreconciled                      
                        RAISE ex_early_exit;                      
                      END IF;

                  END IF; -- end of ACM/sector paymnetcheck
               
                  g_statement := 190;
                  v_error_message := 'BSP tran ('||v_tran_details||') has no matching refund doc in stella';
                  -- mark as reason code ID and don't mark as reconciled
                  v_reason_code := 'ID';
                  RAISE ex_early_exit;             
                 
               END; -- select from refund_ticket block
               
               g_statement := 200;               
               -- to be here assumes that it has found a matching refund in stella.. now to check amounts               
               -- check amounts match as well here
                             
               p_stella_reconciliation.calc_stella_refund_doc_amts(cmain.refund_document_no,NULL,v_seat_amt, v_tax_amt, v_return_valid); -- calc amount of refund doc in stella
               
               --v_seat_amt := v_seat_amt * -1;  -- need to reverse signs so that amounts match because adms are debits, acms are credits
               --v_tax_amt := v_tax_amt * -1;    -- need to reverse signs so that amounts match
               

               IF NOT v_return_valid THEN 
                 v_msg := 'Failure in calc of refund totals';
                 RAISE ex_major_error;
               END IF;
                
                
              p_common.debug_message('stl ref:'||cmain.refund_document_no||', sseat:'||v_seat_amt||' stax:'||v_tax_amt||' bspseat:'||v_bsp_seat_cost||' bsptax:'||cmain.tax_amt||' tol:'||cmain.tolerance_amt  );
              
              IF cmain.transaction_code = 'RFN' THEN
                 -- deduct the airline penalty amount from the bsp cost 
                  v_airline_penalty_amt := cmain.airline_penalty_amt;
              ELSE
                  v_airline_penalty_amt := 0;
              END IF;

              IF v_seat_amt = (v_bsp_seat_cost + v_airline_penalty_amt) AND v_tax_amt = cmain.tax_amt THEN
                -- mark as reconciled
                v_reason_code := 'IS';
                RAISE ex_reconciled;             
              END IF;

              -- if seat total is within tolerance then mark reconciled as well, 
              -- but with different reason code              
              -- tax must be exactly the same
              
              IF v_seat_amt  BETWEEN (v_bsp_seat_cost  + v_airline_penalty_amt - cmain.tolerance_amt) AND (v_bsp_seat_cost + v_airline_penalty_amt + cmain.tolerance_amt) 
              AND (v_tax_amt = cmain.tax_amt) THEN
                -- mark as reconciled
                v_reason_code := 'IO';
                RAISE ex_reconciled;             
              END IF;

              IF v_seat_amt <> (v_bsp_seat_cost + v_airline_penalty_amt) AND (v_tax_amt <> cmain.tax_amt) THEN
                v_error_message := 'Seat AND tax discrepancy on bsp tran:('||v_tran_details||') ';
                -- don't mark as reconciled
                v_reason_code := 'IG';
                RAISE ex_early_exit;             
              END IF;

              
              IF v_seat_amt <> (v_bsp_seat_cost + v_airline_penalty_amt) THEN
                v_error_message := 'Seat/fare discrepancy on bsp tran:('||v_tran_details||') ';
                -- don't mark as reconciled
                v_reason_code := 'IF';
                RAISE ex_early_exit;             
              END IF;
              
              
              IF v_tax_amt <> cmain.tax_amt THEN
                v_error_message := 'Tax discrepancy on bsp tran:('||v_tran_details||') seat cost was OK';
                -- don't mark as reconciled
                v_reason_code := 'IT';
                RAISE ex_early_exit;             
              END IF;

                            
              g_statement := 232;
              -- should never get here, if so there is an error
              v_msg := 'At statement:'||g_statement||', system error, wrong path in code';
              p_common.debug_message(v_msg||' '||v_error_message);                  
       
              RAISE ex_major_error;                                                    
                         
                    
          ELSE
              g_statement := 235;
              v_msg := 'Error, invalid trans code encountered, system error: '||v_tran_details;
              p_common.debug_message(v_msg||' '||v_error_message);                  
              RAISE ex_major_error;

            
          END IF; -- end of transaction code ifs          
          

          
          g_statement := 240;
          
            
          
      EXCEPTION 
      WHEN ex_reconciled THEN
         -- fully reconciled, log and move on to next row
          g_statement := 370;             
          app_util.file_log(g_statement,null);  

          
          IF v_tran_group = 'T' THEN
            -- why do we want to tie up trans_id?? so that we can find out which month a transaction
            -- was reconciled in
            g_statement := 380;
            UPDATE ticket t
            SET t.bsp_trans_id = cmain.bsp_trans_id
            WHERE t.ticket_no = cmain.ticket_no;
            
            v_count_tkt_reconciled := v_count_tkt_reconciled + 1;
            
          ELSE
            g_statement := 390;
            UPDATE refund_batch rt
            SET rt.bsp_trans_id = cmain.bsp_trans_id
            WHERE 
             rt.refund_document_no = cmain.refund_document_no
            ;
            
            v_count_ref_reconciled := v_count_ref_reconciled + 1;
            
          END IF;  -- end if tran group = 'T'    
                
          g_statement := 400;                      
          
          UPDATE bsp_transaction b
            SET b.reconciled_ind = 'Y',              
              b.last_reconciled_date = SYSDATE,
              b.reason_code = v_reason_code,
              b.stella_seat_amt = v_seat_amt,
              b.stella_tax_amt  = v_tax_amt
            WHERE b.bsp_trans_id = cmain.bsp_trans_id;
          
          IF v_reason_code = 'IS' THEN                                        
            v_full_match_count := v_full_match_count +1;
          ELSE
            v_tol_match_count := v_tol_match_count +1;
          END IF;
                                                                        
          v_error_message := 'Success:'||v_tran_details||' reconciled, level:'||v_reason_code;          
          p_common.debug_message(v_error_message);
          
      WHEN ex_early_exit THEN
           -- there is a business reason why this pnr has failed reconciliation
           g_statement := 410;             
            UPDATE bsp_transaction b
            SET b.reconciled_ind = 'N',              
              b.last_reconciled_date = SYSDATE,
              b.stella_seat_amt = v_seat_amt,
              b.stella_tax_amt  = v_tax_amt,
              b.reason_code = v_reason_code
            WHERE b.bsp_trans_id = cmain.bsp_trans_id;
            
            g_statement := 420;
            p_common.debug_message('Fail:'||v_error_message||' code:'||v_reason_code);  
            
            v_count_failed := v_count_failed + 1;
           
      WHEN ex_iata_transaction THEN
           -- processing already taken care of above, special case for certain airlines
           g_statement := 450;
           NULL; -- skip to next loop iteration

      END; -- main loop block
      
      g_statement := 460;
    END LOOP; -- end main_cursor cmain loop

    g_statement := 999;
    app_util.file_log(g_statement,null);                                      

    p_common.debug_message('Rows processed                   :'||v_mainloop_count);
    p_common.debug_message('Refund type trans read           :'||v_count_refund_trans);
    p_common.debug_message('Ticket type trans read           :'||v_count_tkt_trans);

    p_common.debug_message('IATA trans                       :'||v_num_iata_recs);    
    p_common.debug_message('Count duplicate transactions     :'||v_count_dup_trans);    
    p_common.debug_message('Rows fully matched 100%          :'||v_full_match_count);
    p_common.debug_message('Rows not 100%, but within tol.   :'||v_tol_match_count);    
    p_common.debug_message('Rows failed to reconcile         :'||v_count_failed);
    
    p_common.debug_message('Rows tkts reconciled             :'||v_count_tkt_reconciled);
    p_common.debug_message('Rows refs reconciled             :'||v_count_ref_reconciled);

    p_common.debug_message('Rows tkts not reconciled         :'||(v_count_tkt_trans - v_count_tkt_reconciled));
    p_common.debug_message('Rows tkts updated costs          :'||v_count_stella_updated);
    p_common.debug_message('Rows refs not reconciled         :'||(v_count_refund_trans - v_count_ref_reconciled));

    
    p_common.debug_message(v_app_key||' ended:'||to_char(SYSDATE,'yy-mon-dd hh24:mi:ss'));      
    dbms_output.put_line (v_app_key||' ended:'||to_char(SYSDATE,'yy-mon-dd hh24:mi:ss'));
      
    p_valid := TRUE;
    p_sqlerrm:= null;
    p_no_of_rows := v_mainloop_count;
  
    app_util.end_logging;

    COMMIT;
    
    
  EXCEPTION  -- main exception block
    
    WHEN ex_major_error THEN
         -- deliberate exit from loop due to logic failure
          g_sqlcode := SQLCODE;
          g_sqlerrm := substr(SQLERRM, 1,100);
          
          ROLLBACK;
          
          app_util.file_log(g_statement,'Error,'||v_msg||' Gen error'||nvl(g_sqlerrm,' '));                                      
          dbms_output.put_line('Error, Statement:'||g_statement||v_msg||',major error'||nvl(g_sqlerrm,' ')||g_sqlcode);
          dbms_output.put_line('Failed in :'||v_tran_details);
          app_util.end_logging;
          p_valid := FALSE;
          p_no_of_rows := 0;
          p_sqlerrm :='Major exception raised Stmt:'||g_statement||', '||v_msg||' '||substr(nvl(g_sqlerrm,' '),1,100);
          app_util.log(g_package_name,NULL,g_log_sequence,'ALL','GENERROR','FATAL',
              'Major excp', substr('Major exception at:'||g_statement||','||g_sqlerrm||' '||v_parameters,1,200));
             
    WHEN OTHERS THEN
         g_sqlcode := SQLCODE;
         g_sqlerrm := substr(SQLERRM, 1,100);
         p_common.handle_error(g_package_name||'.bsp_reconcile'
                              ,g_statement       -- Code location.
                              ,'900'       -- First Choice error number.
                              ,g_Sqlerrm
                              ,v_parameters
                              );
          app_util.file_log(g_statement,'Error, Gen error'||g_sqlerrm);                                      
          dbms_output.put_line(g_statement||' Error, General error'||g_sqlerrm);
          dbms_output.put_line('Failed in :'||v_tran_details);          
          app_util.end_logging;
          p_valid := FALSE;
          p_no_of_rows := 0;
          p_sqlerrm :='Gen Error Stmt:'||g_statement||', '||substr(g_sqlerrm,1,100);
          app_util.log(g_package_name,NULL,g_log_sequence,'ALL','GENERROR','FATAL',
              'Others excp', substr('General error at:'||g_statement||','||g_sqlerrm||' '||v_parameters,1,200));
              
          ROLLBACK;    
              
                        
  END bsp_reconcile; -- end procedure

  
  
  
  
-----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------
/* update the stella ticket amounts with those taken from the bsp file figures
-- need to derive the various elements of the seat cost using business rules

*/  

FUNCTION update_stella_ticket_amts (p_ticket_no           NUMBER,
                                    p_pnr_id              NUMBER,
                                    p_commissionable_amt  NUMBER,                                    
                                    p_net_fare_amt        NUMBER,
                                    p_commission_amt      NUMBER,
                                    p_supp_commission_amt NUMBER,
                                    p_ub_tax_amt          NUMBER, 
                                    p_gb_tax_amt          NUMBER,
                                    p_remaining_tax_amt   NUMBER
) RETURN NUMBER IS

v_stella_published_fare_amt         NUMBER :=0;
v_stella_selling_fare_amt           NUMBER :=0;
v_commission_pct                    NUMBER :=0;
v_statement NUMBER:=0;



BEGIN

  v_statement := 10;   
/*
RULE 1   IF net fare 0, pub = selling = commissionable amt - known as a published fare
RULE 2   IF net fare amt <> 0 , selling = net fare amt, pub = commissionable amt - known as a net fare
RULE 3   IF supp comm 0 AND commission amt 0 THEN pub =0, selling = commissionable amt
*/


  IF nvl(p_supp_commission_amt,0) = 0 AND nvl(p_commission_amt,0) = 0 THEN
        -- net fare
        v_stella_published_fare_amt := 0;
        v_stella_selling_fare_amt   := nvl(p_commissionable_amt,0);
  ELSE     
      v_statement := 20;   
      IF nvl(p_net_fare_amt,0) = 0 THEN
          -- published fare
          v_stella_published_fare_amt := nvl(p_commissionable_amt,0);
          v_stella_selling_fare_amt   := nvl(p_commissionable_amt,0);
      ELSE
          v_stella_published_fare_amt := nvl(p_commissionable_amt,0);
          v_stella_selling_fare_amt   := nvl(p_net_fare_amt,0);
      END IF;
  END IF;
      
  v_statement := 30;   

  IF nvl(p_commission_amt,0) = 0 THEN
    v_commission_pct := 0;
  ELSE
    IF v_stella_published_fare_amt = 0 THEN
       v_commission_pct := 0;
    ELSE
       IF v_stella_published_fare_amt = v_stella_selling_fare_amt THEN
         v_commission_pct := round((nvl(p_commission_amt,0) / nvl(p_commissionable_amt + p_ub_tax_amt,0)) * 100,2);    
       ELSE
         v_commission_pct := round((nvl(p_commission_amt,0) / nvl(p_commissionable_amt,0)) * 100,2);    
       END IF;
    END IF;
  END IF;
  
    v_statement := 40;   

  UPDATE ticket t
  SET t.published_fare_amt = nvl(v_stella_published_fare_amt,0),
      t.selling_fare_amt   = nvl(v_stella_selling_fare_amt,0),
      t.commission_pct     = v_commission_pct,
      t.commission_amt     = nvl(p_commission_amt,0),
      t.gb_tax_amt         = nvl(p_gb_tax_amt,0),
      t.ub_tax_amt         = nvl(p_ub_tax_amt,0),
      t.remaining_tax_amt  = nvl(p_remaining_tax_amt,0),
      t.amended_date       = SYSDATE,
      t.amended_user_id    = 'UPDBSPAMT'
   WHERE t.ticket_no = p_ticket_no;   

   
     v_statement := 50;   
   
   UPDATE pnr 
     SET booking_reason_code = NULL,
         booking_reconciled_ind = 'N'
   WHERE pnr_id = p_pnr_id;
   
   
   
   v_statement := 60;   
   RETURN SQL%ROWCOUNT;

   EXCEPTION
   WHEN OTHERS THEN
         g_sqlcode := SQLCODE;
         g_sqlerrm := substr(SQLERRM, 1,100);
         app_util.file_log('upd tkt:'||p_ticket_no||' failed:'||v_statement,'Error, Gen error'||g_sqlerrm);                                      
         dbms_output.put_line('upd tkt:'||p_ticket_no||' failed:'||v_statement||' Error, Gen error'||g_sqlerrm);
         RAISE;      
   
   
END update_stella_ticket_amts;







  
END p_bsp_reconciliation;
/
