CREATE OR REPLACE PROCEDURE sp_update_pnr_reason_Codes (p_reason_code_from IN CHAR,
                                                        p_reason_Code_to IN CHAR) 
                                                        IS
           
/* script to change stella discrepancies enmass from TV reason code to TN
  so that they disappear from users exception screens
  
  this is run regularly because users don't care about TV exceptions


Leigh mar 2004  */

v_count NUMBER(9):=0;
res VARCHAR2(2000);

CURSOR c1 IS 
SELECT * FROM stella.pnr_reconciliation_history p
 WHERE p.reason_code = p_reason_code_from
 AND   p.process_date = (SELECT MAX(process_date) FROM stella.pnr_reconciliation_history p2
WHERE p2.pnr_id = p.pnr_id)
AND p.unmatched_amt < 0
 ;
  
 BEGIN
  dbms_output.put_line ('start:'||to_char(SYSDATE,'yy-mon-dd hh24:mi:ss'));
  dbms_output.put_line ('reas from:'||p_reason_code_from||' reas to'|| p_reason_code_To);
  
 FOR x IN c1 LOOP
 
    res:= stella.p_stella_get_data.update_exception_reason_code('D',x.pnr_id, p_reason_code_to,'REASFIX',x.reason_code);
    dbms_output.put_line('res:'||res);
    v_count := v_count +1;
     
 END LOOP;
 
 dbms_output.put_line('Count of fixed:'||v_count);
 dbms_output.put_line ('end:'||to_char(SYSDATE,'yy-mon-dd hh24:mi:ss')); 
 END; 
 
 
/
