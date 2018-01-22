CREATE OR REPLACE PROCEDURE sp_update_stella_booking_ref
         ( parm_log_mode IN CHAR,
           parm_sqlerrm OUT CHAR,
           parm_valid  OUT BOOLEAN,
           parm_no_of_rows OUT NUMBER
           ) IS
           


/* leigh ashton nov 03 
v1.1 30/3/04 LA , fixed if null booking ref
set correct stella booking reference from Gemini information in data warehouse
where there is currently no match between stella and gemini data

Inputs
Outputs
parm_sqlerrm  - text of sqlerror if any
parm_valid - true if has run successfully, false if has failed
parm_no_of_rows - number of bookings read
parm_log_mode - Y if to be run in log mode only -- i.e. no database updates performed


*/


  g_version                   VARCHAR2(20) := '001/001 ';
  g_statement NUMBER := 0; 
  g_sqlerrm VARCHAR2(500);
  g_sqlcode CHAR(20);
  g_debug BOOLEAN := FALSE;
  g_package_name     CONSTANT VARCHAR2(100) := 'STLUPBKR';

  g_log_sequence jutil.application_log.log_sequence%TYPE;
  
  

 v_app_key CHAR(8)  := 'STLBKGRF';
  v_parameters          VARCHAR2(200) := substr('Params:'

                                                 ,1,200);
v_error_message VARCHAR2(150);
v_run_date DATE;
v_msg VARCHAR2(200);
v_change BOOLEAN;
v_gemini_booking_Ref booking.booking_reference_no%TYPE;
v_gemini_season_type booking.season_type%TYPE;
v_gemini_season_year booking.season_year%TYPE;

v_num_bkgs NUMBER :=0;
v_num_reset NUMBER :=0;



CURSOR c_stella_discrepancies IS
SELECT p.branch_code, p.pnr_no, p.pnr_id, nvl(p.booking_Reference_no,0) booking_reference_no , p.season_type, p.season_year, 
  (SELECT MIN(Departure_date) FROM ticket t
   WHERE t.pnr_id = p.pnr_id) departure_date -- can be multiple tickets on a pnr, so just get one of the departure dates
FROM pnr p, branch b
WHERE
p.booking_reason_code = 'MO' -- Record exists in Stella and not the Data Warehouse      
 AND b.data_warehouse_match_ind= 'Y'
 AND b.season_type = p.season_type
 AND b.season_year = p.season_year
 AND b.branch_code = p.branch_code
;



BEGIN

g_statement := 5;

--  app_util.setup_logging(v_app_key,'LIVE','ALL');
  p_common.set_debug_mode('ON');
  v_msg := v_app_key||' started:'||to_char(SYSDATE,'yy-mon-dd hh24:mi:ss');
  p_common.debug_message(v_msg);
  p_common.debug_message('Params:'||v_parameters);



  v_run_date := SYSDATE;




  g_statement := 10;

    
  FOR cmain IN c_stella_discrepancies LOOP
    
      BEGIN
        g_statement :=20;
        v_num_bkgs := v_num_bkgs + 1;
        


        -- find the correct gemini booking reference number
        -- and update the pnr if necessary
        -- bit of a lack of indexed columns here! could be a performance issue
        
        SELECT b.booking_Reference_no, b.season_year, b.season_type
        INTO v_gemini_booking_ref, v_gemini_season_year, v_gemini_season_type
        FROM booking b, transport_scheduled_cost t
        WHERE 
            b.season_year = t.season_year 
        AND b.season_type = t.season_type
        AND b.booking_reference_no = t.booking_reference_no
        AND b.holiday_departure_date = cmain.departure_date
        AND t.pnr_no = cmain.pnr_no;
        
        
        
        IF v_gemini_booking_ref <> cmain.booking_reference_no 
        OR v_gemini_season_type <> cmain.season_type
        OR v_gemini_season_year <> cmain.season_year
        THEN
            -- stella is wrong

           -- if booking ref now different, update booking details in stella and set pnr flags so that it gets re-reconciled
           
           IF parm_log_mode <> 'Y' THEN
             p_common.debug_message('changed');           
             UPDATE pnr 
             SET booking_reason_code = NULL,
               booking_reconciled_ind = 'N',
               amended_user_id = v_app_key,
               booking_reference_no = v_gemini_booking_ref,
               season_type = v_gemini_season_type,
               season_year = v_gemini_season_year
             WHERE  pnr_id = cmain.pnr_id
              ;
           END IF;            
            
                       
           g_statement :=90;    
         
         
           v_msg := 'Log:PNR booking ref changed:'||cmain.pnr_no||'/'||v_gemini_season_type||
               v_gemini_season_year||'/'||v_gemini_booking_ref||' Old bkg was:'||cmain.season_type||cmain.season_year||'/'||cmain.booking_reference_no;
           p_common.debug_message(v_msg);
           app_util.file_log(v_msg);
           v_num_reset := v_num_reset + 1;
         
         
        END IF; -- if booking details different
       
        g_statement :=95;    
      EXCEPTION
      WHEN no_data_found THEN
         -- doesn't matter, to be MO reason code , we already know there is a discrepancy between stella and dwhse                    
         NULL;             
      WHEN too_many_rows THEN
           v_msg := 'Log:Too many rows (2 bookings in gemini/dwhse)'||cmain.pnr_no||'/'||cmain.departure_date||'/'||cmain.season_type||cmain.season_year||'/'||cmain.booking_reference_no;
           p_common.debug_message(v_msg);
           app_util.file_log(v_msg);
      WHEN dup_val_on_index THEN
           v_msg := 'Problem: Duplicate PNR found, cannot have same pnr/season/crs:'||cmain.pnr_no||'/'||cmain.departure_date||'/'||cmain.season_type||cmain.season_year||'/'||cmain.booking_reference_no;
           p_common.debug_message(v_msg);
           app_util.file_log(v_msg);
      
      
      
      END; -- end of block within for loop
      g_statement :=100;
      
      
  END LOOP; -- end main booking loop


  g_statement :=120;



       v_msg := 'Log: TOTAL PNRs WITH MO reason code read        :'||v_num_bkgs;
       p_common.debug_message(v_msg);

       v_msg := 'Log: TOTAL Number of stella pnrs changed        :'||v_num_reset;
       p_common.debug_message(v_msg);

       v_msg :=      v_app_key||' ended:'||to_char(SYSDATE,'yy-mon-dd hh24:mi:ss');
       p_common.debug_message(v_msg);
       dbms_output.put_line(v_msg);


       parm_valid := TRUE;
       parm_sqlerrm:= null;
       parm_no_of_rows := v_num_bkgs;



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

          dbms_output.put_line(g_statement||' General error'||g_sqlerrm);


          parm_valid := FALSE;
          parm_no_of_rows := 0;
          parm_sqlerrm :='Gen Error Stmt:'||g_statement||', '||substr(g_sqlerrm,1,100);
          app_util.log(g_package_name,NULL,g_log_sequence,'ALL','GENERROR','FATAL',
              'Others excp', substr('General error at:'||g_statement||','||g_sqlerrm||' '||v_parameters,1,200));



              
              
              


END sp_update_stella_booking_ref;            
            
/
