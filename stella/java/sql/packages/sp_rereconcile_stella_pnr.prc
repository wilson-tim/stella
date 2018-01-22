CREATE OR REPLACE PROCEDURE sp_rereconcile_stella_pnr(parm_season_type IN  CHAR,
                                                      parm_season_year IN CHAR,
                                                      parm_no_of_days IN NUMBER,
                                                      parm_log_mode IN CHAR,
                                                      parm_sqlerrm OUT VARCHAR2,
                                                      parm_valid OUT BOOLEAN,
                                                      parm_no_of_rows OUT NUMBER
                                                      ) IS

                                                      
/* leigh ashton nov 03 
stella job to reset stella pnr's so that they are re-reconciled against the data warehouse/Gemini data
when the Gemini booking is first made or every time it is modified
We are specifically trying to capture when a chaneg is made to the select page in Gemini --- scheduled flight details,
but we have no way in the data warehouse of isolating that change, so just have to detect any change in a scheduled booking
Run daily
-- has to be run daily, 7 days a week.
Inputs
parm_season_year and parm_Season_type - optional - if want to run for one particular season then populate these,
        otherwise will loop through a few seasons' bookings
parm_no_of_days       
Usually pass param of 1 to parm_no_of_days so that changes made in last 24 hours are picked up
parm_log_mode - Y if to be run without updates
             -  N if to run and update data (normal occurence)
Outputs
parm_sqlerrm  - text of sqlerror if any
parm_valid - true if has run successfully, false if has failed
parm_no_of_rows - number of bookings read

*/


  g_version                   VARCHAR2(20) := '001/001 ';
  g_package_name     CONSTANT VARCHAR2(100) := 'STLRRECS';
  g_statement NUMBER := 0; 
  g_sqlerrm   VARCHAR2(500);
  g_sqlcode   CHAR(20);
  g_debug     BOOLEAN := FALSE;

  g_log_sequence jutil.application_log.log_sequence%TYPE;

  
  
  
  




 v_app_key CHAR(8)  := 'STLGEMRT';
  v_parameters          VARCHAR2(200) := substr('Params:'
                                                ||'seas:'||parm_season_type||parm_season_year
                                                 ,1,200);
v_error_message VARCHAR2(150);
v_run_date DATE;
v_msg VARCHAR2(200);
v_change BOOLEAN;
v_previous_amt financial_Detail.Revenue_Or_Cost%TYPE;
v_prev_details_updated_date financial_Detail.Details_Updated_Date%TYPE;
v_dwhse_pnr_no transport_scheduled_cost.pnr_no%TYPE;
v_num_bkgs NUMBER :=0;
v_num_reset NUMBER :=0;
v_find_count NUMBER:=0;


/* loop through recent seasons */
CURSOR cursor_season  IS
SELECT season_type, season_year
FROM season s
WHERE
   (parm_season_type IS NULL AND
    s.season_start_date >= SYSDATE -365
    AND s.season_end_date < SYSDATE + (365*3))
OR (parm_season_type IS NOT NULL AND
    s.season_type = parm_season_type AND s.season_year = parm_season_year);






CURSOR c_changed_bookings (vl_season_type CHAR, vl_season_year CHAR) IS
/* those scheduled flight bookings that have had changes made recently, or are new bookings made recently
-- could be any element of the booking which has chnaged though, no just scheduled details on "S"-page
-- will usually be bookings changed in last 24 hours, so this job needs to be run daily
*/


          SELECT t.pnr_no, b.booking_Reference_no, b.season_type, b.season_year
            FROM dataw.transport_scheduled_cost t, booking b
            WHERE t.season_year = b.season_year
            AND   t.season_type = b.season_type
            AND   b.season_type = vl_season_type
            AND   b.season_year = vl_season_year
            AND   t.booking_reference_no = b.booking_reference_no
            AND   t.crs_type_code <> 9 -- exclude eurostar scheduled bookings -- these are not covered by stella
            AND   b.amended_date > SYSDATE - parm_no_of_days -- the booking has recently been changed/made
            AND EXISTS
            (
            /* it has recently been calculated in financial calculations.
            If the financial_detail row has same details_updated_date as the booking.amended_date 
            then the financial calculation must be up-to-date*/

              SELECT  NULL
              FROM dataw.financial_detail fd
              WHERE season_year = b.season_year
              AND season_type = b.season_type
              AND details_updated_date = b.amended_date
  
              AND revenue_line_code in  (51,151) -- seat/tax scheduled booking cost
              AND fd.amended_date > SYSDATE - parm_no_of_days -- another check that has been calculated recently
            )
  ;


BEGIN

g_statement := 5;
  p_common.set_debug_mode('ON');
--  app_util.setup_logging(v_app_key,'LIVE','ALL');
  v_msg := v_app_key||' started:'||to_char(SYSDATE,'yy-mon-dd hh24:mi:ss');
  p_common.debug_message(v_msg);
  p_common.debug_message('Params:'||v_parameters);


  IF (parm_season_type IS NULL AND parm_season_year IS NOT NULL)
  THEN
        g_statement := 20;
        parm_sqlerrm := 'ERROR, parameters must be both null, or neither null';
        parm_valid:= FALSE;

        RETURN;
  END IF;


  v_run_date := SYSDATE;
  p_common.debug_message('Total Number of days changes read:'||parm_no_of_days);
  

  FOR c_season IN CURSOR_season LOOP 

    g_statement := 10;
    p_common.debug_message(c_season.season_type||c_season.season_year);
    
    FOR cmain IN c_changed_bookings(c_season.season_type, c_season.season_year) LOOP
    
      BEGIN
        g_statement :=20;
        v_num_bkgs := v_num_bkgs + 1;
        

         -- force corresponding row in Stella to be re-reconciled by changing it's flags. It will then get
         -- picked up in the nect run of the stella/gemini reconciliation
         
         -- Update pnr table, but may be more than one pnr affected because a booking can potentially cover more than one pnr.
         -- If no rows updated, no problem, must be a discrepancy between stella and gemini/warehouse : this will be 
         -- picked up by data warehouse / stella reconciliation process anyway
         -- 
         
         IF parm_log_mode <> 'Y' THEN
           
           UPDATE pnr 
           SET booking_reason_code = NULL,
               booking_reconciled_ind = 'N',
               amended_user_id = v_app_key
           -- booking ref not always populated on pnr row in stella, so try two ways to find rows 
           -- note that neither way is the primary key of pnr table
           -- but are the best ways of joining stella pnr's to Gemini bookings in warehouse
           WHERE  (pnr_no = cmain.pnr_no 
                   AND season_year = cmain.season_year
                   AND Season_type = cmain.season_type)
           OR (booking_reference_no = cmain.booking_reference_no 
              AND season_type = cmain.season_type
              AND season_year = cmain.season_year)
              ;
         ELSE


           SELECT count(*) INTO v_find_count 
           FROM pnr
           WHERE  (pnr_no = cmain.pnr_no 
                   AND season_year = cmain.season_year
                   AND Season_type = cmain.season_type)
           OR (booking_reference_no = cmain.booking_reference_no 
              AND season_type = cmain.season_type
              AND season_year = cmain.season_year)
              ;
                       
         END IF; -- if log mode flag set -- otherwise set to log mode only       
              
              
           g_statement :=90;    



           
           IF (parm_log_mode = 'N' AND SQL%ROWCOUNT > 0) OR (parm_log_mode = 'Y' AND v_find_Count > 0)THEN 
             v_msg := 'PNR reset:'||v_dwhse_pnr_no||',bkg:'||cmain.season_type||cmain.season_year||'/'||cmain.booking_reference_no||', pnr:'||cmain.pnr_no;
             p_common.debug_message(v_msg);
             app_util.file_log(v_msg);
             v_num_reset := v_num_reset + 1;
           ELSE
             v_msg := 'PNR not reset (not found):'||v_dwhse_pnr_no||',bkg:'||cmain.season_type||cmain.season_year||'/'||cmain.booking_reference_no||', pnr:'||cmain.pnr_no;
             p_common.debug_message(v_msg);
             app_util.file_log(v_msg);
           
           END IF;
  
           g_statement :=95;    
       
                      
             
      END; -- end of block within for loop
      g_statement :=100;
      
      
    END LOOP; -- end main booking loop

    g_statement :=110;           
        
  END LOOP ; -- end season loop

  g_statement :=120;
       g_statement := 999;


       v_msg := 'Total Bookings read because changed        :'||v_num_bkgs;
       p_common.debug_message(v_msg);

       v_msg := 'Total Number of stella pnrs reset          :'||v_num_reset;
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



              
              
              

END sp_rereconcile_stella_pnr;            
            
/
