CREATE OR REPLACE FUNCTION f_get_search_results(p_in_ticket_no         NUMBER,
                                                p_in_pnr               CHAR,
                                                p_in_booking_ref       NUMBER,
                                                p_in_season            CHAR, -- in s03 format, not s2003
                                                p_in_airline_num       NUMBER,
                                                p_in_pax_name          CHAR,
                                                p_in_departure_date    DATE,
                                                p_in_ticket_issue_date DATE,
                                                p_in_refund_doc        NUMBER,
                                                p_in_refund_letter_id  NUMBER,
                                                p_in_iata_num          NUMBER)
  RETURN p_stella_get_data.return_refcursor IS

  /* return list of all tickets and refund documents for given criteria
  all arguments are optional - where clause is built dynamically */

  v_result       p_stella_get_data.return_refcursor;
  v_season_year  season.season_year%TYPE := NULL;
  v_season_type  season.season_type%TYPE := NULL;
  v_sqlstatement VARCHAR2(32000);
  --  v_output_file  utl_file.file_type;

BEGIN

  IF p_in_season IS NOT NULL THEN
    v_season_year := '20' || substr(p_in_season, 2, 3);
    v_season_type := substr(p_in_season, 1, 1);
  END IF;
  v_sqlstatement := NULL;

  IF p_in_refund_doc IS NULL THEN
    IF p_in_refund_letter_id IS NULL THEN
      v_sqlstatement := 'SELECT ' || chr(13);
      v_sqlstatement := v_sqlstatement || '''TKT'' rectype, ' || chr(13);
      v_sqlstatement := v_sqlstatement || 'p.pnr_no, ' || chr(13);
      v_sqlstatement := v_sqlstatement || 't.ticket_no, ' || chr(13);
      v_sqlstatement := v_sqlstatement || 't.airline_num, ' || chr(13);
      v_sqlstatement := v_sqlstatement || 't.iata_no, ' || chr(13);
      v_sqlstatement := v_sqlstatement || 't.e_ticket_ind ,' || chr(13);
      v_sqlstatement := v_sqlstatement ||
                        'to_char(t.ticket_issue_date, ''dd/mm/yyyy'') ticket_issue_date, ' ||
                        chr(13);
      v_sqlstatement := v_sqlstatement || 't.passenger_name, ' || chr(13);
      v_sqlstatement := v_sqlstatement || 'p.booking_reference_no, ' ||
                        chr(13);
      v_sqlstatement := v_sqlstatement || 'p.branch_code, ' || chr(13);
      v_sqlstatement := v_sqlstatement ||
                        'p.season_type || substr(p.season_year, 3, 2) season, ' ||
                        chr(13);
      v_sqlstatement := v_sqlstatement ||
                        'to_char(t.departure_date, ''dd/mm/yyyy'') departure_date, ' ||
                        chr(13);
      v_sqlstatement := v_sqlstatement || 'airline.airline_name, ' ||
                        chr(13);
      /* next call returns the season_type that users want to see e.g. C for summer HandJ */
      v_sqlstatement := v_sqlstatement ||
                        'p_stella_get_data.convert_season_type(p.season_type, p.season_year, p.branch_code)|| substr(p.season_year, 3, 2) derived_season, ' ||
                        chr(13);
      v_sqlstatement := v_sqlstatement || '0 refund_document_no, ' ||
                        chr(13);
      v_sqlstatement := v_sqlstatement || 'NULL refund_doc_type_code, ' ||
                        chr(13);
      v_sqlstatement := v_sqlstatement || 'NULL issue_date, ' || chr(13);
      v_sqlstatement := v_sqlstatement ||
                        'p_stella_reconciliation.calc_stella_ticket_cost(t.ticket_no) total_doc_cost ' ||
                        chr(13);
      v_sqlstatement := v_sqlstatement || 'FROM ticket t, ' || chr(13);
      v_sqlstatement := v_sqlstatement || 'pnr p, ' || chr(13);
      v_sqlstatement := v_sqlstatement || 'airline, ' || chr(13);
      v_sqlstatement := v_sqlstatement || 'ticketing_agent ta, ' || chr(13);
      v_sqlstatement := v_sqlstatement || 'doc_type doc ' || chr(13);
      v_sqlstatement := v_sqlstatement || 'WHERE t.pnr_id = p.pnr_id ' ||
                        chr(13);
      v_sqlstatement := v_sqlstatement ||
                        'and ta.ticketing_agent_initials = t.ticketing_agent_initials ' ||
                        chr(13);
      v_sqlstatement := v_sqlstatement ||
                        'and airline.airline_num = t.airline_num ' ||
                        chr(13);
      v_sqlstatement := v_sqlstatement ||
                        'and t.doc_type_code = doc.doc_type_code ' ||
                        chr(13);
    
      /* now build rest of where clause dynamically according to what user has entered in search screen */
      /* rules about which columns are mandatory are held in GUI */
      IF p_in_ticket_no IS NOT NULL THEN
        v_sqlstatement := v_sqlstatement || 'AND  (t.ticket_no = ' ||
                          p_in_ticket_no || ' ' || chr(13);
        v_sqlstatement := v_sqlstatement ||
                          'OR t.ticket_no = (SELECT linked_ticket_no ' ||
                          chr(13);
        v_sqlstatement := v_sqlstatement ||
                          'FROM ticket t3 WHERE t3.ticket_no = ' ||
                          p_in_ticket_no || ') ' || chr(13);
        v_sqlstatement := v_sqlstatement ||
                          'OR t.linked_ticket_no = (SELECT ticket_no ' ||
                          chr(13);
        v_sqlstatement := v_sqlstatement ||
                          'FROM ticket t3 WHERE t3.ticket_no = ' ||
                          p_in_ticket_no || ')) ' || chr(13);
        /* ticket numbers recycle so don't show anything older than 3 years */
        v_sqlstatement := v_sqlstatement ||
                          'AND nvl(t.departure_date,SYSDATE) > (SYSDATE - (365*3)) ' ||
                          chr(13);
      END IF;
    
      IF p_in_pnr IS NOT NULL THEN
        v_sqlstatement := v_sqlstatement || 'AND p.pnr_no = ''' || p_in_pnr ||
                          ''' ' || chr(13);
      END IF;
    
      IF p_in_booking_ref IS NOT NULL THEN
        v_sqlstatement := v_sqlstatement || 'AND p.booking_reference_no = ' ||
                          p_in_booking_ref || ' ' || chr(13);
      END IF;
    
      IF v_season_type IS NOT NULL THEN
        v_sqlstatement := v_sqlstatement || 'AND p.season_type = ''' ||
                          v_season_type || ''' ' || chr(13);
      END IF;
    
      IF v_season_year IS NOT NULL THEN
        v_sqlstatement := v_sqlstatement || 'AND p.season_year = ''' ||
                          v_season_year || ''' ' || chr(13);
      END IF;
    
      IF p_in_airline_num IS NOT NULL THEN
        v_sqlstatement := v_sqlstatement || 'AND t.airline_num = ' ||
                          p_in_airline_num || ' ' || chr(13);
      END IF;
    
      IF p_in_iata_num IS NOT NULL THEN
        v_sqlstatement := v_sqlstatement || 'AND t.iata_no = ' ||
                          p_in_iata_num || ' ' || chr(13);
      END IF;
    
      IF p_in_pax_name IS NOT NULL THEN
        v_sqlstatement := v_sqlstatement || 'AND t.passenger_name like ''' ||
                          p_in_pax_name || '%'' ' || chr(13);
      END IF;
    
      IF p_in_departure_date IS NOT NULL THEN
        v_sqlstatement := v_sqlstatement || 'AND t.departure_date = ''' ||
                          p_in_departure_date || ''' ' || chr(13);
      END IF;
    
      IF p_in_ticket_issue_date IS NOT NULL THEN
        v_sqlstatement := v_sqlstatement || 'AND t.ticket_issue_date = ''' ||
                          p_in_ticket_issue_date || ''' ' || chr(13);
      END IF;
    
    END IF;
  
  END IF;

  /* refund doc section -- cannot show a lot for same columns 
  e.g. booking ref, because a refund document can cover many pnrs/bookings 
  */
  IF p_in_refund_letter_id IS NULL THEN
    IF v_sqlstatement IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'UNION ' || chr(13);
      v_sqlstatement := v_sqlstatement || 'SELECT ' || chr(13);
    END IF;
    IF v_sqlstatement IS NULL THEN
      v_sqlstatement := 'SELECT ' || chr(13);
    END IF;
    v_sqlstatement := v_sqlstatement || '''REF'' rectype, ' || chr(13);
    v_sqlstatement := v_sqlstatement || ''' '' pnr_no, ' || chr(13); -- pnr
    v_sqlstatement := v_sqlstatement || '0 ticket_no, ' || chr(13); -- ticket no
    v_sqlstatement := v_sqlstatement || '0 airline_num, ' || chr(13); -- airline num
    v_sqlstatement := v_sqlstatement || '0 iata_no, ' || chr(13); -- iata num
    v_sqlstatement := v_sqlstatement || 'NULL e_ticket_ind, ' || chr(13); -- eticket ind
    v_sqlstatement := v_sqlstatement || 'NULL ticket_issue_date, ' ||
                      chr(13); -- ticket issue date
    v_sqlstatement := v_sqlstatement || 'NULL passenger_name,' || chr(13); -- passenger name
    v_sqlstatement := v_sqlstatement || '0 booking_reference_no, ' ||
                      chr(13); -- booking ref number
    v_sqlstatement := v_sqlstatement || 'NULL branch_code, ' || chr(13); -- branch code
    v_sqlstatement := v_sqlstatement || 'NULL season, ' || chr(13); -- season
    v_sqlstatement := v_sqlstatement || 'NULL departure_date, ' || chr(13); -- departure date
    v_sqlstatement := v_sqlstatement || 'NULL airline_name, ' || chr(13); -- airline NAME
    v_sqlstatement := v_sqlstatement || 'NULL derived_season, ' || chr(13); -- converted season
    v_sqlstatement := v_sqlstatement || 'refb.refund_document_no, ' ||
                      chr(13);
    v_sqlstatement := v_sqlstatement ||
                      'refb.doc_type_code refund_doc_type_code, ' ||
                      chr(13);
    v_sqlstatement := v_sqlstatement ||
                      'to_char(refb.issue_date,''dd/mm/yy'') issue_date, ' ||
                      chr(13);
    v_sqlstatement := v_sqlstatement ||
                      'sum(p_stella_reconciliation.calc_stella_refund_doc_total(refb.refund_document_no, NULL))/count(*) total_doc_cost ' ||
                      chr(13);
    v_sqlstatement := v_sqlstatement || 'FROM ticket t, ' || chr(13);
    v_sqlstatement := v_sqlstatement || 'pnr p, ' || chr(13);
    v_sqlstatement := v_sqlstatement || 'airline, ' || chr(13);
    v_sqlstatement := v_sqlstatement || 'ticketing_agent ta, ' || chr(13);
    v_sqlstatement := v_sqlstatement || 'doc_type doc, ' || chr(13);
    v_sqlstatement := v_sqlstatement || 'refund_batch refb, ' || chr(13);
    v_sqlstatement := v_sqlstatement || 'refund_ticket reft ' || chr(13);
    v_sqlstatement := v_sqlstatement || 'WHERE t.pnr_id = p.pnr_id ' ||
                      chr(13);
    v_sqlstatement := v_sqlstatement ||
                      'and ta.ticketing_agent_initials = t.ticketing_agent_initials ' ||
                      chr(13);
    v_sqlstatement := v_sqlstatement ||
                      'and airline.airline_num = t.airline_num ' || chr(13);
    v_sqlstatement := v_sqlstatement ||
                      'and t.doc_type_code = doc.doc_type_code ' || chr(13);
    v_sqlstatement := v_sqlstatement ||
                      'AND reft.refund_document_no = refb.refund_document_no ' ||
                      chr(13);
    v_sqlstatement := v_sqlstatement || 'AND reft.ticket_no = t.ticket_no ' ||
                      chr(13);
  
    /* now build rest of where clause dynamically according to what user has entered in search screen */
    /* rules about which columns are mandatory are held in GUI */
    IF p_in_ticket_no IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and t.ticket_no = ' ||
                        p_in_ticket_no || ' ' || chr(13);
      v_sqlstatement := v_sqlstatement ||
                        'and nvl(t.departure_date,SYSDATE) > (SYSDATE - (365*3)) ' ||
                        chr(13);
    END IF;
  
    IF p_in_pnr IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and p.pnr_no = ''' || p_in_pnr ||
                        ''' ' || chr(13);
    END IF;
  
    IF p_in_booking_ref IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and p.booking_reference_no = ' ||
                        p_in_booking_ref || ' ' || chr(13);
    END IF;
  
    IF v_season_type IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and p.season_type = ''' ||
                        v_season_type || ''' ' || chr(13);
    END IF;
  
    IF v_season_year IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and p.season_year = ''' ||
                        v_season_year || ''' ' || chr(13);
    END IF;
  
    IF p_in_airline_num IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and t.airline_num = ' ||
                        p_in_airline_num || ' ' || chr(13);
    END IF;
  
    IF p_in_iata_num IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and t.iata_no = ' ||
                        p_in_iata_num || ' ' || chr(13);
    END IF;
  
    IF p_in_pax_name IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and t.passenger_name like ''' ||
                        p_in_pax_name || '%'' ' || chr(13);
    END IF;
  
    IF p_in_departure_date IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and t.departure_date = ''' ||
                        p_in_departure_date || ''' ' || chr(13);
    END IF;
  
    IF p_in_ticket_issue_date IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and t.ticket_issue_date = ''' ||
                        p_in_ticket_issue_date || ''' ' || chr(13);
    END IF;
  
    IF p_in_refund_doc IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and refb.refund_document_no = ' ||
                        p_in_refund_doc || ' ' || chr(13);
    END IF;
  
    v_sqlstatement := v_sqlstatement || 'GROUP BY ''REF'', ' || chr(13);
    v_sqlstatement := v_sqlstatement || 'refb.refund_document_no, ' ||
                      chr(13);
    v_sqlstatement := v_sqlstatement || 'refb.doc_type_code, ' || chr(13);
    v_sqlstatement := v_sqlstatement ||
                      'to_char(refb.issue_date,''dd/mm/yy'') ' || chr(13);
  END IF;

  IF p_in_refund_doc IS NULL THEN
    IF v_sqlstatement IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'UNION ' || chr(13);
      v_sqlstatement := v_sqlstatement || 'SELECT ' || chr(13);
    END IF;
    IF v_sqlstatement IS NULL THEN
      v_sqlstatement := 'SELECT ' || chr(13);
    END IF;
    v_sqlstatement := v_sqlstatement || '''RFL'' rectype, ' || chr(13);
    v_sqlstatement := v_sqlstatement || 'p.pnr_no pnr_no,' || chr(13);
    v_sqlstatement := v_sqlstatement || '0 ticket_no, ' || chr(13); -- ticket no
    v_sqlstatement := v_sqlstatement || 'rl.airline_num airline_num, ' ||
                      chr(13);
    v_sqlstatement := v_sqlstatement || '0 iata_no, ' || chr(13); -- iatanum
    v_sqlstatement := v_sqlstatement || 'NULL e_ticket_ind, ' || chr(13); -- eticket ind
    v_sqlstatement := v_sqlstatement || 'NULL ticket_issue_date, ' ||
                      chr(13); -- ticket issue date
    v_sqlstatement := v_sqlstatement || 'NULL passenger_name, ' || chr(13); -- pax name
    v_sqlstatement := v_sqlstatement ||
                      'p.booking_reference_no booking_reference_no, ' ||
                      chr(13);
    v_sqlstatement := v_sqlstatement || 'p.branch_code branch_code, ' ||
                      chr(13);
    v_sqlstatement := v_sqlstatement ||
                      'p.season_type || substr(p.season_year, 3, 2) season, ' ||
                      chr(13);
    v_sqlstatement := v_sqlstatement ||
                      'to_char(p_stella_get_data.get_first_departure_date(p.pnr_id), ''dd/mm/yyyy'') departure_date, ' ||
                      chr(13); -- departure date   
    v_sqlstatement := v_sqlstatement ||
                      'airline.airline_name airline_name, ' || chr(13); -- airline name
    /* next call returns the season_type that users want to see e.g. C for summer HandJ */
    v_sqlstatement := v_sqlstatement ||
                      'p_stella_get_data.convert_season_type(p.season_type, p.season_year, p.branch_code)|| substr(p.season_year, 3, 2) derived_season, ' ||
                      chr(13);
    v_sqlstatement := v_sqlstatement ||
                      'rl.refund_letter_id refund_document_no, ' || chr(13);
    v_sqlstatement := v_sqlstatement || 'NULL refund_doc_type_code, ' ||
                      chr(13); -- refund doc type code
    v_sqlstatement := v_sqlstatement ||
                      'to_char(rl.entry_date,''dd/mm/yy'') issue_date, ' ||
                      chr(13); -- refund issue date 
    v_sqlstatement := v_sqlstatement || 'rl.requested_amt total_doc_cost ' ||
                      chr(13);
    v_sqlstatement := v_sqlstatement || 'FROM  ticket t, ' || chr(13);
    v_sqlstatement := v_sqlstatement || 'pnr p, ' || chr(13);
    v_sqlstatement := v_sqlstatement || 'airline, ' || chr(13);
    v_sqlstatement := v_sqlstatement || 'refund_letter rl, ' || chr(13);
    v_sqlstatement := v_sqlstatement || 'refund_letter_ticket rt ' ||
                      chr(13);
    v_sqlstatement := v_sqlstatement || 'WHERE t.pnr_id = p.pnr_id ' ||
                      chr(13);
    v_sqlstatement := v_sqlstatement ||
                      'and airline.airline_num = rl.airline_num ' ||
                      chr(13);
    v_sqlstatement := v_sqlstatement ||
                      'AND rl.refund_letter_id = rt.refund_letter_id ' ||
                      chr(13);
    v_sqlstatement := v_sqlstatement || 'AND t.ticket_no = rt.ticket_no ' ||
                      chr(13);
  
    /* now build rest of where clause dynamically according to what user has entered in search screen */
    /* rules about which columns are mandatory are held in GUI */
    IF p_in_ticket_no IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and rt.ticket_no = ' ||
                        p_in_ticket_no || ' ' || chr(13);
      /* ticket numbers recycle so don't show anything older than 3 years */
      v_sqlstatement := v_sqlstatement ||
                        'and nvl(t.departure_date,SYSDATE) > (SYSDATE - (365*3)) ' ||
                        chr(13);
    END IF;
  
    IF p_in_pnr IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and p.pnr_no = ''' || p_in_pnr ||
                        ''' ' || chr(13);
    END IF;
  
    IF p_in_booking_ref IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and p.booking_reference_no = ' ||
                        p_in_booking_ref || ' ' || chr(13);
    END IF;
  
    IF v_season_type IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and p.season_type = ''' ||
                        v_season_type || ''' ' || chr(13);
    END IF;
  
    IF v_season_year IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and p.season_year = ''' ||
                        v_season_year || ''' ' || chr(13);
    END IF;
  
    IF p_in_airline_num IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and rl.airline_num = ' ||
                        p_in_airline_num || ' ' || chr(13);
    END IF;
  
    IF p_in_iata_num IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and t.iata_no = ' ||
                        p_in_iata_num || ' ' || chr(13);
    END IF;
  
    IF p_in_pax_name IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and t.passenger_name like ''' ||
                        p_in_pax_name || '%'' ' || chr(13);
    END IF;
  
    IF p_in_departure_date IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and t.departure_date = ''' ||
                        p_in_departure_date || ''' ' || chr(13);
    END IF;
  
    IF p_in_ticket_issue_date IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and t.ticket_issue_date = ''' ||
                        p_in_ticket_issue_date || ''' ' || chr(13);
    END IF;
  
    IF p_in_refund_letter_id IS NOT NULL THEN
      v_sqlstatement := v_sqlstatement || 'and rl.refund_letter_id = ' ||
                        p_in_refund_letter_id || ' ' || chr(13);
    END IF;
  
  END IF;
  v_sqlstatement := v_sqlstatement ||
                    'order by pnr_no,ticket_no,rectype DESC';

  --v_output_file := utl_file.fopen('/home/dw/SQL_Output','johntest.txt','W',32000);
  --utl_file.put_line(v_output_file,v_sqlstatement);
  --utl_file.fflush(v_output_file);
  --utl_file.fclose(v_output_file);
  p_common.debug_message(v_sqlstatement);

  OPEN v_result FOR v_sqlstatement;
  RETURN(v_result);

END; -- end function
/
