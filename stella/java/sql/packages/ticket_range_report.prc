create or replace procedure ticket_range_report ( p_iata_no IN NUMBER,
                                  p_ticket_no_start IN NUMBER, 
                                  p_ticket_no_end IN NUMBER,
/* some or all of these params will be passed */
/* allows flexibilty in design so any level of reconciliation can be started */
           p_sqlerrm OUT CHAR,
           p_no_of_rows OUT CHAR,
           p_valid OUT BOOLEAN)
IS


  g_version                   VARCHAR2(20) := '001/005';
  g_package_name     CONSTANT VARCHAR2(100) := 'STLDWHSP';
  g_statement NUMBER := 0;
  g_sqlerrm VARCHAR2(500);
  g_sqlcode CHAR(20);
  g_debug BOOLEAN := FALSE;

  g_log_sequence jutil.application_log.log_sequence%TYPE;     

  
  v_app_key CHAR(8)  := 'STLTKTR';
  v_parameters          VARCHAR2(200) := substr('Params:'
                                                ||'iata:'||p_iata_no
                                                ||'tkt:'||p_ticket_no_start
                                                ||'tkt:'||p_ticket_no_end
                                                 ,1,200);
v_error_message VARCHAR2(150);
v_run_date DATE;
v_num_ranges NUMBER :=0;

v_msg VARCHAR2(200);

v_lowest NUMBER :=0;
v_highest NUMBER :=0;
v_total_tkts NUMBER :=0;
v_prev_tkt_no NUMBER:=0;
v_num_tkts_this_range NUMBER:= 0;            

CURSOR cursor_ranges IS
   SELECT DISTINCT iata_no
   FROM ticket;
   
CURSOR cursor_ticket_range (vl_iata_no char) IS
SELECT ticket_no, iata_no
FROM ticket t
WHERE t.iata_no = vl_iata_no
ORDER BY iata_no, ticket_no -- this order by ticket_no is crucial
;



BEGIN
      g_statement := 5;
      
--      app_util.setup_logging(v_app_key,'LIVE','ALL');
      
      g_statement := 10;      
      g_log_sequence := 1;

      

      v_msg := v_app_key||' started:'||to_char(SYSDATE,'yy-mon-dd hh24:mi:ss');
      p_common.debug_message(v_msg);      
      app_util.file_log(v_msg);      
      p_common.debug_message('Params:'||v_parameters);
      IF p_ticket_no_end IS NOT NULL THEN
        app_util.file_log('RUNNING USING PARAMETERS, NOT CONTROL TABLE!!!!!!!');
      END IF;


      IF (p_ticket_no_end IS NULL
            AND p_ticket_no_start IS NOT NULL)
      OR (p_ticket_no_end IS NOT NULL
            AND p_ticket_no_start IS NULL) 
      OR  (p_iata_no IS NULL 
            AND p_ticket_no_start IS NOT NULL) 
      OR  (p_iata_no IS NOT NULL 
            AND p_ticket_no_start IS NULL)             
      THEN
            g_statement := 20;      
            p_sqlerrm := 'ERROR, parameters must all be null or not null';
            p_valid:= FALSE;
            app_util.file_log(p_sqlerrm);
            app_util.end_logging;
            RETURN;
      END IF;

      
      v_run_date := SYSDATE;
      v_num_ranges := 0;
      v_msg := 'List of Ticket Ranges Follows';
      p_common.debug_message(v_msg); 
      app_util.file_log(v_msg);           
      v_msg := '-------------------------------';
      p_common.debug_message(v_msg);      
      app_util.file_log(v_msg);            
      
      FOR c_range IN cursor_ranges LOOP
          BEGIN
            g_statement := 40;      
            v_num_ranges := v_num_ranges + 1;
            -- need to store highest/lowest figures so can output the ranges missing
            -- without outputting every single missing ticket



            -- now loop through that entire range looking for each ticket number
            -- log if it does not exist
            g_statement := 50;  
            v_prev_tkt_no := 0;
            v_lowest :=9999999999999;
            v_highest := 0;
            v_num_tkts_this_range := 0;            
            
            
            FOR c_tkt IN cursor_ticket_range(c_range.iata_no) LOOP

                IF v_prev_tkt_no = 0 THEN
                  -- first time through each iata range
                  v_prev_tkt_no := c_tkt.ticket_no -1;
                END IF;
                
                v_total_tkts := v_total_tkts + 1;                
                IF c_tkt.ticket_no < v_lowest THEN
                  v_lowest := c_tkt.ticket_no;
                END IF;
                
                IF c_tkt.ticket_no = v_prev_tkt_no + 1 THEN
                  -- do nothing, we have a consecutive ticket                  
                  v_num_tkts_this_range := v_num_tkts_this_range + 1;
                ELSE
                  -- break on sequence found
                  --output message, reset low/high vars
                   v_msg     := 'Range:iata,'||c_range.iata_no||', from,'||v_lowest||', up to,'||v_prev_tkt_no||',numtkts,'||v_num_tkts_this_range;
                   p_common.debug_message(v_msg);      
                   app_util.file_log(v_msg);                         
                   v_lowest := c_tkt.ticket_no;
                   v_num_tkts_this_range := 1;
                    
                END IF;
                
                v_prev_tkt_no := c_tkt.ticket_no;
                
            END LOOP; -- end c_tkt loop
            
            -- end of the iata range, need to display last complete range
            v_msg     := 'Range:iata,'||c_range.iata_no||', from,'||v_lowest||', up to,'||v_prev_tkt_no||',numtkts,'||v_num_tkts_this_range;
            p_common.debug_message(v_msg);      
            app_util.file_log(v_msg);                         
            
            END;           

            

           
           
      END LOOP;    -- ranges loop

      

       g_statement := 999;
       v_msg := 'End of run';
       app_util.file_log( v_msg);      
       app_util.file_log(null);       
       v_msg := 'iata numbers processed                   :'||v_num_ranges;
       p_common.debug_message(v_msg);
       app_util.file_log( v_msg);      
       v_msg := 'Number of tickets in total :'||v_total_tkts;
       p_common.debug_message(v_msg);
       app_util.file_log( v_msg);      
       v_msg :=      v_app_key||' ended:'||to_char(SYSDATE,'yy-mon-dd hh24:mi:ss');
       p_common.debug_message(v_msg);
       dbms_output.put_line(v_msg);
       app_util.file_log( v_msg);             
         
       p_valid := TRUE;
       p_sqlerrm:= null;
       p_no_of_rows := v_num_ranges;
     
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
          p_valid := FALSE;
          p_no_of_rows := 0;
          p_sqlerrm :='Gen Error Stmt:'||g_statement||', '||substr(g_sqlerrm,1,100);
          app_util.log(g_package_name,NULL,g_log_sequence,'ALL','GENERROR','FATAL',
              'Others excp', substr('General error at:'||g_statement||','||g_sqlerrm||' '||v_parameters,1,200));
              


END ticket_range_report;
/
