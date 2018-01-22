CREATE OR REPLACE PACKAGE p_stella_reconciliation IS

/* ***************************************************************************** *

Purpose
-------
reconciliation processes for bsp stella system

History
-------

Date      By          Description                                    Version
----      ----------- ---------------------------------------------- -------
21/01/03  L Ashton     Initial version.                                 001
24/9/03   L Ashton     added new function calc_gemini_flight_tax_amt    006
27/10/03  L Ashton     reduced dbms_output.put_line(); output to prevent buffer overflow
20/11/03  L Ashton     changed refund calcs to use selling fare
11/11/04  Jyoti        Bug Fix -  showing reason code TE even when Seat Variance exists
* ***************************************************************************** */

  -- Public declarations
  g_version                   VARCHAR2(20) := '001/007';
  g_package_name     CONSTANT VARCHAR2(100) := 'STLDWHSP';
  g_statement NUMBER := 0;
  g_sqlerrm VARCHAR2(500);
  g_sqlcode CHAR(20);
  g_debug BOOLEAN := FALSE;

  g_log_sequence jutil.application_log.log_sequence%TYPE;


/* calculate the total scheduled flight tax of a gemini booking
all params mandatory */

FUNCTION calc_gemini_flight_tax_amt(p_season_type CHAR, p_season_year CHAR, p_booking_reference_no NUMBER)
RETURN NUMBER;


/* calculate the seat cost only of a ticket , does not take into account refunds*/
FUNCTION calc_seat_amt (
                        p_ticket_no IN NUMBER
                        )
                           RETURN NUMBER;



/* calculate the total cost of ticket NOT taking into account the
refunds linked to that ticket */
/* param is mandatory */
FUNCTION calc_stella_ticket_cost
          (p_ticket_no IN NUMBER
           ) RETURN NUMBER;


/* calculate the total tax of ticket NOT taking into account the
refunds linked to that ticket */
/* param is mandatory */
FUNCTION calc_ticket_tax_total (p_ticket_no IN NUMBER) RETURN NUMBER;



/* loop through each row of refund_ticket table and work out the
   total refund amt for each ticket in a batch*/
PROCEDURE calc_stella_refund_doc_amts
          (p_refund_document_no IN NUMBER,   -- mandatory
           p_refund_ticket_no IN NUMBER,      -- optional
           p_seat_amt OUT NUMBER,
           p_tax_amt  OUT NUMBER,
           p_valid OUT BOOLEAN
)       ;

/* function wrapper to allow calc_stella_refund_doc_amts to be used from sql
-- returns seat PLUS tax cost added together */
FUNCTION calc_stella_refund_doc_total
          (p_refund_document_no IN NUMBER,   -- mandatory
           p_refund_ticket_no IN NUMBER  -- optional

) RETURN NUMBER;


/* calculate the total cost of a ticket AFTER taking into account the
refunds linked to that ticket */
PROCEDURE calc_stella_ticket_accrual
          (p_ticket_no IN NUMBER,
           p_ub_tax_amt IN NUMBER,
           p_gb_tax_amt IN NUMBER,
           p_remain_tax_amt IN NUMBER,
           p_seat_total IN OUT NUMBER,
           p_tax_total IN OUT NUMBER,
           p_valid OUT BOOLEAN);


/* calculate the seat amt net of refunds for a particular ticket
param mandatory */
FUNCTION calc_seat_amt_net_of_refunds
          (p_ticket_no IN NUMBER
          ) RETURN NUMBER;


/* calculate the tax amt net of refunds for a particular ticket
param mandatory */
FUNCTION calc_tax_amt_net_of_refunds
          (p_ticket_no IN NUMBER
          ) RETURN NUMBER;


PROCEDURE dwhse_stella_reconcile
           (
           p_branch_code IN CHAR,       -- optional
           p_pnr_no IN CHAR,            -- optional
           p_season_type IN CHAR,       -- optional
           p_season_year IN CHAR,       -- optional
           p_booking_reference_no IN NUMBER,      -- optional
           p_airline_num IN NUMBER,               -- optional
           p_no_of_days_previous IN NUMBER,  -- mandatory
           p_sqlerrm OUT CHAR,
           p_no_of_rows OUT CHAR,
           p_valid OUT BOOLEAN);
  /* this procedure is the data warehouse to stella reconciliation process
  - compares costs of tickets/bookings in stella data with that
  in data warehouse tables that are sourced from selling systems
  e,g, gemini . atop gold etc.
  */

------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------
PROCEDURE update_recon_status(
          p_pnr_id IN NUMBER,
          p_reconciled IN BOOLEAN,
          p_reason_code IN CHAR,
          p_run_date IN DATE
          );
------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------
PROCEDURE update_recon_history (p_pnr_id IN NUMBER,
                          p_process_code IN CHAR,
                          p_process_date IN DATE,
                          p_reason_code IN CHAR,
                          p_amended_user_id IN CHAR,
                          p_data_warehouse_seat_cost IN NUMBER,
                          p_data_warehouse_tax_cost IN NUMBER,
                          p_stella_seat_cost IN NUMBER,
                          p_stella_tax_cost IN NUMBER,
                          p_unmatched_amt IN NUMBER
                           )
;
------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------
/* batch routine to produce a list of ticket missing from database
within certain ranges
if no parameters are passed then it just uses the stella.ticket_range table
to determine the particular ticket range to check */

PROCEDURE missing_ticket_report (p_iata_no IN NUMBER,
                                 p_ticket_no_start IN NUMBER,
                                 p_ticket_no_end IN NUMBER,
/* some or all of these params will be passed */
/* allows flexibilty in design so any level of reconciliation can be started */
/* -- will do this range of ticket numbers only (inclusive range) */
           p_sqlerrm OUT CHAR,
           p_no_of_rows OUT CHAR,
           p_valid OUT BOOLEAN);
------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------



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
  SELECT p_stella.reconciliation.version
  FROM dual;

  * ***************************************************************************** */
  ;

------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------

END p_stella_reconciliation;  --end of package
/
CREATE OR REPLACE PACKAGE BODY p_stella_reconciliation IS
------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------


FUNCTION calc_gemini_flight_tax_amt(p_season_type CHAR, p_season_year CHAR, p_booking_reference_no NUMBER)
RETURN NUMBER IS


v_dwhse_tax_amt supplement_sale.supplement_value%TYPE;

BEGIN


          -- nasty idea to hard-code in the supp codes, so store in a seasonal table instead
/*
          SELECT
            sum(nvl(SUPPLEMENT_SALE.QUANTITY,0) *
            nvl(SUPPLEMENT_SALE.SUPPLEMENT_VALUE,0))
          INTO v_dwhse_tax_amt
          FROM
            dataw.SUPPLEMENT_SALE,
            dataw.BOOKING
          WHERE

          SUPPLEMENT_SALE.BOOKING_REFERENCE_NO=
          BOOKING.BOOKING_REFERENCE_NO and
          SUPPLEMENT_SALE.SEASON_TYPE=
          BOOKING.SEASON_TYPE and
          SUPPLEMENT_SALE.SEASON_YEAR=
          BOOKING.SEASON_YEAR and
          SUPPLEMENT_SALE.DETAILS_UPDATED_DATE=
          BOOKING.AMENDED_DATE  )
            AND  (
              SUPPLEMENT_SALE.SUPPLEMENT_CODE  IN  ('AT', '@S')
            )
          AND booking.season_year = cmain.season_year
          AND booking.season_type = cmain.season_type
          AND booking.booking_Reference_no = cmain.booking_reference_no;
  */

          SELECT
            nvl(sum(nvl(SUPPLEMENT_SALE.QUANTITY,0) *
            nvl(SUPPLEMENT_SALE.SUPPLEMENT_VALUE,0)),0)
          INTO v_dwhse_tax_amt
          FROM
            dataw.SUPPLEMENT_SALE,
            dataw.BOOKING,
            stella.scheduled_flight_supplement s
          WHERE

            SUPPLEMENT_SALE.BOOKING_REFERENCE_NO=
            BOOKING.BOOKING_REFERENCE_NO and
            SUPPLEMENT_SALE.SEASON_TYPE=
            BOOKING.SEASON_TYPE and
            SUPPLEMENT_SALE.SEASON_YEAR=
            BOOKING.SEASON_YEAR and
            SUPPLEMENT_SALE.DETAILS_UPDATED_DATE=  BOOKING.AMENDED_DATE  AND

            SUPPLEMENT_SALE.SUPPLEMENT_CODE  = s.supplement_code AND
            supplement_sale.season_year  = s.season_year AND
            supplement_sale.season_type  = s.season_type AND

            booking.season_year = p_season_year AND
            booking.season_type = p_season_type AND
            booking.booking_Reference_no = p_booking_reference_no;


          RETURN v_dwhse_tax_amt;




END;


------------------------------------------------------------------------------
------------------------------------------------------------------------------


FUNCTION calc_seat_amt (
                        p_ticket_no IN NUMBER
                        )
                           RETURN NUMBER IS


v_selling_fare_amt  ticket.selling_fare_amt%TYPE :=0;
v_published_fare_amt  ticket.selling_fare_amt%TYPE :=0;
v_commission_amt  ticket.selling_fare_amt%TYPE :=0;
v_commission_pct  ticket.commission_pct%TYPE :=0;
v_ub_tax_amt  ticket.selling_fare_amt%TYPE :=0;
v_seat_amt ticket.selling_fare_amt%TYPE :=0;

v_sector_payment_ind airline.sector_payment_ind%TYPE;
v_tax_commission_bit ticket.selling_fare_amt%TYPE;

BEGIN
 g_statement :=10;

      SELECT  nvl(t.selling_fare_amt,0),
              nvl( t.published_fare_amt,0),
              nvl( t.commission_pct,0),
              nvl(t.commission_amt,0),
              nvl(t.ub_tax_amt,0),
              nvl(a.sector_payment_ind,'N')
      INTO v_selling_fare_amt,
           v_published_fare_amt,
           v_commission_pct,
           v_commission_amt,
           v_ub_tax_amt,
           v_sector_payment_ind
      FROM ticket t, airline a
      WHERE t.ticket_no = p_ticket_no
      and   t.airline_num = a.airline_num;





  /* For a published fare. (published fare amt is not zero)
    Seat Cost =  Published Fare - Commission Amount - (UB tax * commission percentage)
    otherwise, seat cost is selling fare amt
  */

  /* this calculation depends on whether the appropriate airline is "sector payment"
   if it is sector payment, then don't subtract this ub tax commission bit
  */

p_common.debug_message(' v_selling_fare_amt  135  ' || v_published_fare_amt);
p_common.debug_message('v_published_fare_amt at 135  ' || v_selling_fare_amt );

  IF v_published_fare_amt <> v_selling_fare_amt THEN
    -- use selling amount
    v_seat_amt := v_selling_fare_amt;

  ELSE
  p_common.debug_message('v_sector_payment_ind'||v_sector_payment_ind);
      -- use published fare amt
      IF v_sector_payment_ind <> 'Y' THEN
         v_tax_commission_bit := (nvl(v_ub_tax_amt,0) * v_commission_pct/100);
      ELSE
         -- sector payment is set, don't subtract the ub tax commission
         v_tax_commission_bit := 0;
      END IF;

      v_seat_amt := (nvl(v_published_fare_amt,0) - nvl(v_commission_amt,0)) - v_tax_commission_bit;



  END IF;

RETURN v_seat_amt;

EXCEPTION
WHEN OTHERS THEN
         g_sqlcode := SQLCODE;
         g_sqlerrm := substr(SQLERRM, 1,100);

         dbms_output.put_line('Calc_seat_amt:'||g_statement||' General error'||g_sqlerrm);

         app_util.log(g_package_name, NULL,g_log_sequence,'ALL','GENERROR','FATAL',
              'No data', substr('General error calc_seat_amt at:'||g_statement||','||g_sqlerrm,1,200));

     RETURN -1;



END calc_seat_amt;

--------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------
FUNCTION calc_ticket_tax_total (p_ticket_no IN NUMBER) RETURN NUMBER IS
/* calculate the total tax of ticket NOT taking into account the
refunds linked to that ticket */

           v_ub_tax_amt         ticket.selling_fare_amt%TYPE;
           v_gb_tax_amt         ticket.selling_fare_amt%TYPE;
           v_remain_tax_amt     ticket.selling_fare_amt%TYPE;
           v_tax_amt            ticket.selling_fare_amt%TYPE;

BEGIN

  p_common.debug_message('in tkt:'||p_ticket_no);

-- Excluding ub_tax_amt for infants , part of air reconciliation
SELECT
           decode(passenger_type,'IN',0,nvl(ub_tax_amt,0)),
           nvl(gb_tax_amt,0),
           nvl(remaining_tax_amt,0)
           INTO
           v_ub_tax_amt,
           v_gb_tax_amt,
           v_remain_tax_amt
FROM ticket t
WHERE t.ticket_no = p_ticket_no;


  v_tax_amt :=  nvl(v_ub_tax_amt,0) + nvl(v_gb_tax_amt,0) + nvl(v_remain_tax_amt,0);

  p_common.debug_message('out tot:'||v_tax_amt);


  RETURN v_tax_amt;



EXCEPTION
WHEN OTHERS THEN
         g_sqlcode := SQLCODE;
         g_sqlerrm := substr(SQLERRM, 1,100);
         dbms_output.put_line('Calc_stella_ticket_cost:'||g_statement||' General error'||g_sqlerrm);
         app_util.log(g_package_name, NULL,g_log_sequence,'ALL','GENERROR','FATAL',
              'Others excp', substr('General error calc_refund_amt at:'||g_statement||','||g_sqlerrm,1,200));

         RETURN -1;

END calc_ticket_tax_total;


--------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------



FUNCTION calc_stella_ticket_cost
          (p_ticket_no IN NUMBER
           ) RETURN NUMBER IS
/* calculate the total cost of ticket NOT taking into account the
refunds linked to that ticket */


           v_tax_amt     ticket.selling_fare_amt%TYPE :=0;
           v_seat_amt     ticket.selling_fare_amt%TYPE :=0;
           v_tot      ticket.selling_fare_amt%TYPE :=0;
BEGIN


  p_common.debug_message('in tkt:'||p_ticket_no);





  v_tax_amt :=  nvl(calc_ticket_tax_total(p_ticket_no),0);
  v_seat_amt := nvl(calc_seat_amt (p_ticket_no),0);

  v_tot := nvl(v_tax_amt,0) + nvl(v_seat_amt,0);

  p_common.debug_message('out tot:'||v_tot);


  RETURN v_tot;



EXCEPTION
WHEN OTHERS THEN
         g_sqlcode := SQLCODE;
         g_sqlerrm := substr(SQLERRM, 1,100);
         dbms_output.put_line('Calc_stella_ticket_cost:'||g_statement||' General error'||g_sqlerrm);
         app_util.log(g_package_name, NULL,g_log_sequence,'ALL','GENERROR','FATAL',
              'Others excp', substr('General error calc_refund_amt at:'||g_statement||','||g_sqlerrm,1,200));

         RETURN -1;

END calc_stella_ticket_cost;


------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------

PROCEDURE calc_stella_refund_doc_amts
          (p_refund_document_no IN NUMBER,   -- mandatory
           p_refund_ticket_no IN NUMBER,      -- optional
           p_seat_amt OUT NUMBER,
           p_tax_amt  OUT NUMBER,
           p_valid OUT BOOLEAN
)
 IS



CURSOR c_refunds IS
SELECT

  nvl(rt.seat_amt,0) refund_seat_amt,
  nvl(rt.tax_amt,0) refund_tax_amt,
  nvl(rt.fare_used_amt,0) refund_fare_used_amt,
  nvl(rt.airline_penalty_amt,0) refund_airline_penalty_amt,
  nvl(rt.tax_used_amt,0) refund_tax_used_amt,
  nvl(rt.commission_amt,0) refund_commission_amt
  ,
   rt.ticket_no, rb.doc_type_code

 FROM refund_ticket rt, refund_batch rb
 WHERE rt.refund_document_no = rb.refund_document_no
 -- use refund ticket number  to get total for one ticket only
 AND (( nvl(p_refund_document_no,rt.refund_document_no) = rt.refund_document_no)
 AND  ( nvl(p_refund_ticket_no,rt.ticket_no)            = rt.ticket_no))
     ;

v_tax_total  refund_ticket.seat_amt%TYPE :=0;
v_seat_total refund_ticket.seat_amt%TYPE :=0;
v_msg VARCHAR2(200) := '';
ex_validation_error EXCEPTION;
v_orig_ticket_amt refund_ticket.seat_amt%TYPE :=0;
v_tkt_tax ticket.ub_tax_amt%TYPE :=0;

BEGIN
  g_statement := 10;
  p_common.debug_message('in doc:'||p_refund_document_no );

  v_tax_total     := 0;
  v_seat_total    := 0;

  IF p_refund_document_no IS NULL THEN
     v_msg := 'Error, refund document number must be provided';
     RAISE ex_validation_error;

  END IF;



  FOR c1 IN c_refunds LOOP

      -- certain doc types cause reduction in overall picture even though
      -- amt is stored as positive number
      -- see spec document "cost examples3.xls" for detailed spec

      -- users (Sue Stempt) decided that airline penalty should be shown in tax column, not seat
      -- in order to match bsp figures




      IF c1.doc_type_code = 'ADM' THEN
          v_seat_total := v_seat_total + c1.refund_seat_amt;
          --v_tax_total  := v_tax_total  + (v_tkt_tax - c1.refund_tax_amt)  + c1.refund_airline_penalty_amt;
          v_tax_total  := v_tax_total  + c1.refund_tax_amt  + c1.refund_airline_penalty_amt;
      ELSIF c1.doc_type_code IN ('ACM','MAN') THEN
          v_seat_total := v_seat_total - c1.refund_seat_amt;
          --v_tax_total  := v_tax_total  - ((v_tkt_tax - c1.refund_tax_amt) + c1.refund_airline_penalty_amt);
          v_tax_total  := v_tax_total  - c1.refund_tax_amt + c1.refund_airline_penalty_amt;
      ELSIF c1.doc_type_code = 'MRN' THEN

            v_tkt_tax := calc_ticket_tax_total(c1.ticket_no);
            -- REMOVED Nov 2003 v_orig_ticket_amt := nvl(calc_seat_amt(c1.ticket_no),0);
            -- for refunds we have to use the selling fare, not the seat amt

            SELECT nvl(selling_fare_amt,0)
              INTO v_orig_ticket_amt
            FROM ticket t
            WHERE t.ticket_no = c1.ticket_no;



            v_seat_total := v_seat_total - (v_orig_ticket_amt - c1.refund_fare_used_amt  - c1.refund_commission_amt - c1.refund_airline_penalty_amt);

            v_tax_total  := v_tax_total - (v_tkt_tax - c1.refund_tax_used_amt) ;
            -- now need to find the seat/tax amounts from the source ticket



            p_common.debug_message('mrn b4 tkt tot, seat'||v_seat_total||' tax:'||v_tkt_tax);
            p_common.debug_message('tkt:'||c1.ticket_no||', tkt amt:'||v_orig_ticket_amt);





      END IF; -- doc type
      p_common.debug_message('so far stot:'||v_seat_total);
      p_common.debug_message('so far taxtot:'||v_tax_total);

  END LOOP;


   p_common.debug_message('out seat tot:'||v_seat_total);
   p_common.debug_message('out tax tot:'|| v_tax_total);

   p_seat_amt := v_seat_total;
   p_tax_amt  := v_tax_total;
   p_valid := TRUE;

   RETURN;

EXCEPTION

WHEN ex_validation_error THEN
          dbms_output.put_line('Calc_refund:'||v_msg);
          app_util.log(g_package_name, NULL,g_log_sequence,'ALL','GENERROR','FATAL',
              'Others excp', substr('General error calc_refund_cost at:'||g_statement||','||v_msg,1,200));

          p_valid := FALSE;

WHEN OTHERS THEN
         g_sqlcode := SQLCODE;
         g_sqlerrm := substr(SQLERRM, 1,100);

          dbms_output.put_line('Calc_refund:'||g_statement||' General error'||g_sqlerrm);


          app_util.log(g_package_name, NULL,g_log_sequence,'ALL','GENERROR','FATAL',
              'Others excp', substr('General error calc_refund_cost at:'||g_statement||','||g_sqlerrm,1,200));


          p_valid := FALSE;


END  calc_stella_refund_doc_amts;

------------------------------------------------------------------------------
------------------------------------------------------------------------------
/* wrapper around calc_stella_refund_doc_seat to return the total of tax and seat */
FUNCTION calc_stella_refund_doc_total
          (p_refund_document_no IN NUMBER,   -- mandatory
           p_refund_ticket_no IN NUMBER  -- optional
)
 RETURN NUMBER
 IS

v_seat_amt refund_ticket.seat_amt%TYPE;
v_tax_amt refund_ticket.seat_amt%TYPE;
v_valid BOOLEAN;
BEGIN
  calc_stella_refund_doc_amts(p_refund_document_no,  --in
                             p_refund_ticket_no,    --in
                             v_seat_amt, --out
                             v_tax_amt,   --out
                             v_valid -- out
                             );


   IF v_valid THEN
      RETURN v_seat_amt + v_tax_amt;
   ELSE
      RETURN NULL;
   END IF;

END calc_stella_refund_doc_total;




------------------------------------------------------------------------------


PROCEDURE calc_stella_ticket_accrual
          (p_ticket_no IN NUMBER,
           p_ub_tax_amt IN NUMBER,
           p_gb_tax_amt IN NUMBER,
           p_remain_tax_amt IN NUMBER,
           p_seat_total IN OUT NUMBER,
           p_tax_total IN OUT NUMBER,
           p_valid OUT BOOLEAN)
 IS

/* calculate ACCRUAL amount of a ticket --
need to take into account the ticket details
PLUS find total refund amounts linked to that ticket */


/* loop through each row of refund_ticket table and work out the
   total refund amt and the effect on seat/tax cost for accrual */

CURSOR c_refunds IS
SELECT

nvl(rt.seat_amt,0) refund_seat_amt,
nvl(rt.tax_amt,0) refund_tax_amt,
nvl(rt.fare_used_amt,0) refund_fare_used_amt,
nvl(rt.airline_penalty_amt,0) refund_airline_penalty_amt,
nvl(rt.tax_used_amt,0) refund_tax_used_amt,
nvl(rt.commission_amt,0) refund_commission_amt
,
 rt.ticket_no, rb.doc_type_code

 FROM refund_ticket rt, refund_batch rb
 WHERE rt.refund_document_no = rb.refund_document_no
 AND rt.ticket_no = p_ticket_no
     ;

v_seat_amt refund_ticket.seat_amt%TYPE :=0;
v_tax_amt  refund_ticket.tax_amt%TYPE :=0;


BEGIN
  g_statement := 10;
  p_common.debug_message('in gbtx:'||p_gb_tax_amt||' ubtx:'||p_ub_tax_amt||' remtx:'||p_remain_tax_amt);

  v_tax_amt :=  nvl(p_ub_tax_amt,0) + nvl(p_gb_tax_amt,0) + nvl(p_remain_tax_amt,0);

  v_seat_amt := nvl(calc_seat_amt (p_ticket_no),0);

  -- now adjust those amounts according to any refunds issued against this ticket

  FOR c1 IN c_refunds LOOP

      --ignore airline penalty for any doc except MRN
      -- certain doc types cause reduction in overall picture even though
      -- amt is stored as positive number
      -- see spec document "cost examples3.xls" for detailed spec
      IF c1.doc_type_code = 'ADM' THEN
          v_seat_amt := v_seat_amt + c1.refund_seat_amt;
          v_tax_amt  := v_tax_amt + c1.refund_tax_amt;
      ELSIF c1.doc_type_code IN ('ACM','MAN') THEN
          v_seat_amt := v_seat_amt - c1.refund_seat_amt;
          v_tax_amt  := v_tax_amt - c1.refund_tax_amt;
      ELSIF c1.doc_type_code = 'MRN' THEN
          -- completely overrides any other refund transaction
          -- completely replaces the amounts entered for a ticket (but doesn't replace the whole pnr amount)
          v_seat_amt := c1.refund_fare_used_amt + c1.refund_airline_penalty_amt - c1.refund_commission_amt;
          v_tax_amt  := c1.refund_tax_used_amt;

      END IF; -- doc type


  END LOOP;


  p_seat_total := v_seat_amt;
  p_tax_total  := v_tax_amt;


  -- dbms_output.put_line('out seat:'||p_seat_total||' tax:'||p_tax_total);

  p_valid := TRUE;

EXCEPTION
WHEN OTHERS THEN
         g_sqlcode := SQLCODE;
         g_sqlerrm := substr(SQLERRM, 1,100);

          dbms_output.put_line('Calc_accrual:'||g_statement||' General error'||g_sqlerrm);


          app_util.log(g_package_name, NULL,g_log_sequence,'ALL','GENERROR','FATAL',
              'Others excp', substr('General error calc_accrual at:'||g_statement||','||g_sqlerrm,1,200));


          p_valid := FALSE;


END  calc_stella_ticket_accrual;

------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------
/* wrapper around accrual calc to allow us to return only one value -- seat accrual
so can be called from sql
-- will be net of refunds*/
FUNCTION calc_seat_amt_net_of_refunds
          (p_ticket_no IN NUMBER
          ) RETURN NUMBER IS


v_seat_total NUMBER :=0;
v_tax_total NUMBER :=0;
v_valid BOOLEAN;

BEGIN
     calc_stella_ticket_accrual
          (p_ticket_no,
           0,  -- we don't need this tax amt returned , so pass 0
           0,
           0,
           v_seat_total, --out
           v_tax_total,  --out
           v_valid       --out
           );

     RETURN v_seat_total;   -- ONLY RETURN THE SEAT TOTAL



END calc_seat_amt_net_of_refunds;

------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------
/* wrapper around accrual calc to allow us to return only one value -- tax accrual
so can be called from sql
-- will be net of refunds*/
FUNCTION calc_tax_amt_net_of_refunds
          (p_ticket_no IN NUMBER
          ) RETURN NUMBER IS


v_seat_total NUMBER :=0;
v_tax_total NUMBER :=0;
v_valid BOOLEAN;
v_gb_tax_amt NUMBER;
v_ub_tax_amt NUMBER;
v_remaining_tax_amt NUMBER;


BEGIN

  SELECT nvl(t.gb_tax_amt,0), nvl(t.ub_tax_amt,0), nvl(t.remaining_tax_amt,0)
  INTO v_gb_tax_amt, v_ub_tax_amt, v_remaining_tax_amt
  FROM ticket t
  WHERE t.ticket_no = p_ticket_no;


     calc_stella_ticket_accrual
          (p_ticket_no,
           v_ub_tax_amt,  -- we don't need this tax amt returned , so pass 0
           v_gb_tax_amt,
           v_remaining_tax_amt,
           v_seat_total, --out
           v_tax_total,  --out
           v_valid       --out
           );

     RETURN v_tax_total;   -- ONLY RETURN THE TAX TOTAL



END calc_tax_amt_net_of_refunds;

------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------


PROCEDURE dwhse_stella_reconcile
/* some or all of these params will be passed */
/* allows flexibilty in design so any level of reconciliation can be started */
           (
           p_branch_code IN CHAR,
           p_pnr_no IN CHAR,
           p_season_type IN CHAR,
           p_season_year IN CHAR,
           p_booking_reference_no IN NUMBER,
           p_airline_num IN NUMBER,
           p_no_of_days_previous IN NUMBER,
           p_sqlerrm OUT CHAR,
           p_no_of_rows OUT CHAR,
           p_valid OUT BOOLEAN)
IS

  v_app_key CHAR(8)  := 'STLDWHSR';
  v_parameters          VARCHAR2(200) := substr('Params, branch: '
                                                ||p_branch_code
                                                ||' pnr:'||p_pnr_no
                                                ||' seas:'||p_season_type||p_season_year
                                                ||' bkg:'||p_booking_reference_no
                                                ||' airl:'||p_airline_num
                                                ||' days:'||p_no_of_days_previous
                                                 ,1
                                                 ,200
                                                 );

v_dwhse_count NUMBER:=0;
v_dwhse_seat_amt dataw.transport_scheduled_cost_det.seat_cost%TYPE :=0;
v_dwhse_tax_amt  dataw.transport_scheduled_cost_det.tax%TYPE :=0;
v_dwhse_pnr_no   dataw.transport_scheduled_cost.pnr_no%TYPE :='';
v_cancelled_ind CHAR(1) := 'N';
v_mainloop_count NUMBER:=0;
v_error_message VARCHAR2(150);
v_run_date DATE;

v_general_tax_tolerance_amt NUMBER(20,2);
v_general_seat_tolerance_amt NUMBER(20,2);

v_stella_seat_amt NUMBER :=0;
v_stella_tax_amt  NUMBER :=0;

v_tkt_seat_amt NUMBER :=0;
v_tkt_tax_amt  NUMBER :=0;
v_tax_tolerance_amt NUMBER := 0;
v_seat_tolerance_amt NUMBER := 0;


v_refund_seat_amt NUMBER(20,2) :=0;
v_refund_tax_amt NUMBER(20,2) :=0;


v_discrepancy_amt NUMBER :=0;

v_no_booking_count  NUMBER :=0;
v_tax_diff_count    NUMBER :=0;
v_seat_diff_count   NUMBER :=0;
v_full_match_count  NUMBER :=0;
v_part_match_count  NUMBER :=0;

v_seat_amt_ok BOOLEAN := TRUE;
v_tax_amt_ok  BOOLEAN := TRUE;

v_reason_code  reason.reason_code%TYPE := NULL;
v_valid BOOLEAN:= TRUE;

-- exceptions
ex_early_exit EXCEPTION;
ex_major_error EXCEPTION;
ex_reconciled EXCEPTION;
v_msg VARCHAR2(200);


CURSOR main_cursor (
           vl_branch_code CHAR,
           vl_pnr_no CHAR,
           vl_season_type CHAR,
           vl_season_year CHAR,
           vl_booking_ref NUMBER,
           vl_airline_num NUMBER,
           vl_num_days NUMBER)

  IS
  -- build where clause dynamically according to passed params
  -- all stella data that has not been previously reconciled or that has changed in some way since then

  SELECT p.pnr_no, p.booking_reference_no, p.season_type, p.season_year,
         p.branch_code, p.crs_code, p.pnr_id, p.booking_reason_code, p.last_reconcile_attempt_date,
         p_stella_get_data.convert_season_type(p.season_type, p.season_year, p.branch_code)|| substr(p.season_year, 3, 2) derived_season

  FROM
    pnr p, branch b
  WHERE

   b.branch_code = p.branch_code
  AND b.season_type = p.season_type
  AND b.season_year = p.season_year
  AND b.data_warehouse_match_ind= 'Y'
  AND (p.booking_reconciled_ind = 'N' OR p.booking_reconciled_ind IS NULL)   -- this gets reset to null when a change is made to a pnr/ticket/refund
  AND (p.booking_reason_code IS NULL) -- has not been through reconciliation before, this gets reset to null if a change is made in stella
           /* ticket numbers recycle so don't show anything older than 3 years */
           AND p.season_year > to_char(SYSDATE - (365*3),'yyyy')
           /* user-defined params determining level of reconciliation */
           and (vl_pnr_no IS NULL OR (vl_pnr_no IS NOT NULL AND  p.pnr_no = vl_pnr_no))
           and (vl_booking_ref IS NULL OR (vl_booking_ref IS NOT NULL AND  p.booking_reference_no = vl_booking_ref))
           and (vl_season_type IS NULL OR (vl_season_type IS NOT NULL AND  p.season_type = vl_season_type))
           and (vl_season_year IS NULL OR (vl_season_year IS NOT NULL AND  p.season_year = vl_season_year))
           and (vl_airline_num IS NULL OR (vl_airline_num IS NOT NULL AND EXISTS
                                ( SELECT NULL FROM ticket t
                                  WHERE t.airline_num = vl_airline_num
                                  AND t.pnr_id = p.pnr_id)))
           and (vl_branch_code IS NULL OR (vl_branch_code IS NOT NULL AND  p.branch_code= vl_branch_code))
           AND p.entry_date < SYSDATE - vl_num_days     -- only check data entered a few days ago
                                                        -- this is to allow enough time for gemini to feed to dwhse
           AND p.crs_code <> 'AMA'                                                        
GROUP BY p.pnr_no, p.booking_reference_no, p.season_type, p.season_year,
         p.branch_code, p.crs_code, p.pnr_id,  p.booking_reason_code, p.last_reconcile_attempt_date,
         p_stella_get_data.convert_season_type(p.season_type, p.season_year, p.branch_code)|| substr(p.season_year, 3, 2)
   ;



CURSOR c_ticket_details (vl_pnr_id NUMBER)
IS
SELECT
         t.ticket_no,
         nvl(t.gb_tax_amt,0) gb_tax_amt,
         nvl(t.remaining_tax_amt,0) remaining_tax_amt,
         nvl(t.ub_tax_amt,0)  total_tax_amt,
         nvl(t.ub_tax_amt,0)  ub_tax_amt,
         -- work out tax variance allowed
         nvl(al.tax_amt_tolerance_level,0) tax_tolerance_amt,
         nvl(al.seat_amt_tolerance_level,0) seat_tolerance_amt,
         al.airline_num

FROM ticket t, airline al
WHERE t.pnr_id = vl_pnr_id
AND t.airline_num = al.airline_num
;





  BEGIN   -- main block
      g_statement := 5;

      app_util.setup_logging(v_app_key,'LIVE','ALL');

      g_statement := 10;
      g_log_sequence :=1;
      app_util.file_log(g_statement,null);

      p_common.debug_message(v_app_key||' started:'||to_char(SYSDATE,'yy-mon-dd hh24:mi:ss'));
      p_common.debug_message('Params:'||v_parameters);


      v_run_date := SYSDATE;


      v_mainloop_count  :=0;
      v_no_booking_count:=0;
      v_tax_diff_count  :=0;
      v_seat_diff_count :=0;
      v_full_match_count:=0;
      v_part_match_count:=0;

      g_statement := 15;
      -- get tolerance amounts from app registry
      IF app_util.is_number(app_util.get_Registry_Property(v_app_key,'LIVE','ALL', 'GenTaxTol')) THEN
         v_general_tax_tolerance_amt  := app_util.get_Registry_Property(v_app_key,'LIVE','ALL', 'GenTaxTol');
      ELSE
         v_msg := 'invalid registry setting for tax tolerance:'||app_util.get_Registry_Property(v_app_key,'LIVE','ALL', 'GenTaxTol');
         dbms_output.put_line(v_msg);
         p_common.debug_message(v_msg);
         RAISE ex_major_error;
      END IF;
      IF app_util.is_number(app_util.get_Registry_Property(v_app_key,'LIVE','ALL', 'GenSeatTol')) THEN
         v_general_seat_tolerance_amt  := app_util.get_Registry_Property(v_app_key,'LIVE','ALL', 'GenSeatTol');
      ELSE
         v_msg := 'invalid registry setting for seat tolerance:'||app_util.get_Registry_Property(v_app_key,'LIVE','ALL', 'GenseatTol');
         p_common.debug_message(v_msg);
         RAISE ex_major_error;
      END IF;

      g_statement := 18;
      --?? what IF NOT FOUND
      p_common.debug_message( 'Tax tol:'||v_general_tax_tolerance_amt||' seat tol:'||v_general_seat_tolerance_amt);

      g_statement := 20;

      IF p_no_of_days_previous IS NULL THEN
         v_msg := 'Parameter for no. days cannot be null';
         p_common.debug_message(v_msg);
         RAISE ex_major_error;
      END IF;


      FOR cmain IN main_cursor (p_branch_code,  p_pnr_no,  p_season_type, p_season_year,
                               p_booking_reference_no, p_airline_num, p_no_of_days_previous)
      LOOP
        BEGIN
          v_mainloop_count := v_mainloop_count + 1;

          v_reason_code := NULL;

          g_statement := 30;
          p_common.debug_message(v_mainloop_count||'pnr:'||cmain.pnr_id||' '||cmain.pnr_no||' bkg:'||cmain.booking_reference_no
                                  ||cmain.derived_season||' br:'||cmain.branch_code||' reas:'||cmain.booking_reason_code
                                  ||cmain.last_reconcile_attempt_date

                                  );

          app_util.file_log(g_statement,null);

          v_stella_seat_amt := 0;
          v_stella_tax_amt  := 0;
          v_tax_tolerance_amt := 0;
          v_seat_tolerance_amt := 0;
          v_dwhse_seat_amt := 0;
          v_dwhse_tax_amt := 0;
          v_discrepancy_amt := 0;






          -- loop through all tickets for this pnr and calculate the stella accrual for each
          -- adding them up so can be compared to dwhse accrual at pnr level
          g_statement := 32;

          FOR c_tkt IN c_ticket_details (cmain.pnr_id) LOOP

            g_statement := 132;

            -- add in the refund amts
            -- this procedure returns the new accrual after taking off any refunds
            calc_stella_ticket_accrual(c_tkt.ticket_no,
                                       c_tkt.ub_tax_amt,
                                       c_tkt.gb_tax_amt,
                                       c_tkt.remaining_tax_amt,
                                       v_tkt_seat_amt,
                                       v_tkt_tax_amt,
                                       v_valid);

            g_statement := 134;
            p_common.debug_message('tkt:'||c_tkt.ticket_no||' seat:'||v_tkt_seat_amt||' tx:'||v_tkt_tax_amt||' airl:'||c_tkt.airline_num);

            IF NOT v_valid THEN
                 v_msg := 'error in calc_stella_ticket_accrual, pnr:'||cmain.pnr_no;
                 p_common.debug_message(v_msg);
                 RAISE ex_major_error;
            END IF;

            -- add these ticket amounts to the total for the pnr so far
            v_stella_seat_amt    := v_stella_seat_amt + v_tkt_seat_amt;
            v_stella_tax_amt     := v_stella_tax_amt + v_tkt_tax_amt;
            v_tax_tolerance_amt  := v_tax_tolerance_amt + c_tkt.tax_tolerance_amt;
            v_seat_tolerance_amt := v_seat_tolerance_amt + c_tkt.seat_tolerance_amt;

            g_statement := 144;




          END LOOP; -- c_tkt loop

          g_statement := 146;

          v_discrepancy_amt := (v_stella_seat_amt + v_stella_tax_amt);


          p_common.debug_message('-->stella pnr costs, seat:'||v_stella_seat_amt||' tx:'||v_stella_tax_amt||
                                                'tol st:'||v_seat_tolerance_amt||'tol tx:'||v_tax_tolerance_amt);


          IF cmain.booking_reference_no = 9999999999 THEN
            -- dummy booking ref, will not exist in warehouse so don't bother trying to reconcile
            v_error_message := 'Stella bkg ('||cmain.season_type||cmain.season_year||'/'||cmain.booking_reference_no||') has no scheduled match in data warehouse';
            -- mark as reason code MO and don't mark as reconciled
            v_reason_code := 'MO';
            v_no_booking_count:= v_no_booking_count +1;
            RAISE ex_early_exit;

          END IF;
          -- get data warehouse figures for this booking
          g_statement := 31;
          BEGIN
            SELECT t.pnr_no
              INTO v_dwhse_pnr_no
            FROM dataw.transport_scheduled_cost t
            WHERE t.season_year = cmain.season_year
            AND   t.season_type = cmain.season_type
            AND   t.booking_reference_no = cmain.booking_reference_no
            ;


            g_statement := 32;

            SELECT count(*), SUM(nvl(fd.revenue_or_cost,0) * -1)
            INTO v_dwhse_count, v_dwhse_seat_amt
            from dataw.financial_detail fd
            WHERE season_year = cmain.season_year
            AND season_type = cmain.season_type
            AND details_updated_date = (select max(details_updated_date)
                            from dataw.financial_detail fd2
                                             where fd2.season_year = fd.season_year
                                             and fd2.season_type = fd.season_type
                                             and fd2.booking_reference_no = fd.booking_reference_no)

            AND revenue_line_code in  (51,151) -- seat cost
            AND fd.booking_reference_no =cmain.booking_reference_no ;



            g_statement := 34;

            v_dwhse_tax_amt := nvl(calc_gemini_flight_tax_amt(cmain.season_type, cmain.season_year, cmain.booking_reference_no),0);


          EXCEPTION
          WHEN too_many_rows THEN
             v_error_message := 'Stella bkg ('||cmain.season_type||cmain.season_year||'/'||cmain.booking_reference_no||') relates to multiple PNRs in data warehouse';
             v_reason_code := 'MP';
             RAISE ex_early_exit;
          WHEN no_data_found THEN
            v_error_message := 'Stella bkg ('||cmain.season_type||cmain.season_year||'/'||cmain.booking_reference_no||') has no scheduled match in data warehouse';
            -- mark as reason code MO and don't mark as reconciled
            v_reason_code := 'MO';
            v_no_booking_count:= v_no_booking_count +1;
            RAISE ex_early_exit;

          END;

          p_common.debug_message('-->dwhse accrual:seat:'||v_dwhse_seat_amt||
                                  ' tax:'||v_dwhse_tax_amt||' pnr:'||v_dwhse_pnr_no);
          app_util.file_log(g_statement,null);
          g_statement := 40;

          IF v_dwhse_count = 0 THEN
            -- no match
            g_statement := 50;
            v_error_message := 'Stella bkg ('||cmain.season_type||cmain.season_year||'/'||cmain.booking_reference_no||') has no scheduled cost match in data warehouse';
            -- mark as reason code MO and don't mark as reconciled
            v_reason_code := 'MO';
            v_no_booking_count:= v_no_booking_count +1;
            RAISE ex_early_exit;

          END IF;

          IF v_dwhse_pnr_no <> cmain.pnr_no THEN
             g_statement := 70;
             v_error_message := 'Dwhse booking pnr ('||v_dwhse_pnr_no||') different to that in stella ('||cmain.pnr_no||')';
             v_reason_code := 'BP';
             RAISE ex_early_exit;
          END IF;


          g_statement := 90;
          app_util.file_log(g_statement,null);
          -- to get here count must be 1
          -- now start to compare amounts
          -- get the booking details from data warehouse selling data
          BEGIN

            SELECT decode(substr(b.booking_status_code,1,3),'CNX','Y','N')
             INTO   v_cancelled_ind
            FROM dataw.booking b
            WHERE b.season_type = cmain.season_type
            AND   b.season_year= cmain.season_year
            AND   b.booking_reference_no = cmain.booking_reference_no
            ;

          EXCEPTION
          WHEN no_data_found THEN
            g_statement := 110;
            v_error_message := 'Stella bkg ('||cmain.season_type||cmain.season_year||'/'||cmain.booking_reference_no||') has no bkg match in data warehouse';
            -- mark as reason code MO and don't mark as reconciled
            v_reason_code := 'MO';
            v_no_booking_count:= v_no_booking_count +1;

            RAISE ex_early_exit;
          END;
          g_statement := 130;



          -- now actually start to do the comparisons of amounts
          v_discrepancy_amt := 0;


          IF (v_stella_seat_amt + v_stella_tax_amt) = v_dwhse_seat_amt + v_dwhse_tax_amt THEN
             -- fully reconciled!!
             g_statement := 150;
             v_reason_code := NULL; -- fully reconciled
             RAISE ex_reconciled;
          END IF;



          -- to get here, must be a variance, so see if it is due to tax variance or seat cost variance
          g_statement := 170;
          app_util.file_log(g_statement,null);

          -- now do reconcilation of seat as per appendix B of functional spec
          -- assume ok until proven otherwise
          v_seat_amt_ok := TRUE;
          v_tax_amt_ok  := TRUE;
          v_discrepancy_amt :=0;

          IF v_dwhse_seat_amt = v_stella_seat_amt THEN
             g_statement := 190;
             v_reason_code := 'SE';


          ELSIF v_dwhse_seat_amt BETWEEN  (v_stella_seat_amt - v_general_seat_tolerance_amt )
                                AND (v_stella_seat_amt + v_general_seat_tolerance_amt) THEN
             -- seat is within general tolerance

             v_reason_code := 'SE';

          ELSIF v_dwhse_seat_amt BETWEEN  (v_stella_seat_amt - v_seat_tolerance_amt )
                                AND (v_stella_seat_amt + v_seat_tolerance_amt) THEN
             -- seat is within airline tolerance

             v_reason_code := 'SO';

          ELSE
             -- give up on this record, the seat cost is wrong
             p_common.debug_message( 'seatamt outside tols');
             v_seat_amt_ok := FALSE;
             v_reason_code := 'SV';-- seat variance
             v_seat_diff_count := v_seat_diff_count + 1;

          END IF;


          -- now start of appendix c from func spec

          IF v_dwhse_tax_amt = v_stella_tax_amt THEN
             g_statement := 190;
             v_reason_code := 'TE';

          ELSIF v_dwhse_tax_amt BETWEEN  (v_stella_tax_amt - v_general_tax_tolerance_amt )
                                AND (v_stella_tax_amt + v_general_tax_tolerance_amt) THEN
             -- tax is within tolerance

             v_reason_code := 'TE';

          ELSIF v_dwhse_tax_amt BETWEEN  (v_stella_tax_amt - v_tax_tolerance_amt )
                                AND (v_stella_tax_amt + v_tax_tolerance_amt) THEN
               -- tax is within tolerance

               v_reason_code := 'TO';

          ELSE
             -- give up on this record, the tax cost is wrong

             p_common.debug_message( 'tax outside tols');
             -- give up on this record, the tax is wrong
             v_tax_amt_ok := FALSE;
             v_reason_code := 'TV';
             v_tax_diff_count := v_tax_diff_count + 1;
          END IF;

          IF NOT v_seat_amt_ok THEN
              v_discrepancy_amt := v_discrepancy_amt + (v_stella_seat_amt - v_dwhse_seat_amt);
          END IF;
          IF NOT v_tax_amt_ok THEN
              v_discrepancy_amt := v_discrepancy_amt + (v_stella_tax_amt - v_dwhse_tax_amt);
          END IF;


          IF v_tax_amt_ok AND v_seat_amt_ok
            THEN
              p_common.debug_message( 'both in tol');

              -- to get here, both tax and seat cost must be within variance
              -- so can be stamped as matched

              g_statement := 360;
              app_util.file_log(g_statement,null);
              v_reason_code := 'WT'; -- within tolerance, but there was a variance

              RAISE ex_reconciled;

         /* else one or the other is outside variance so cannot reconcile */
         /* Bug fix on seat variance */
          ELSE
              IF NOT v_tax_amt_ok AND NOT v_seat_amt_ok THEN
                v_reason_code := 'OV'; -- BOTH outside variance
              ELSIF (v_tax_amt_ok AND NOT v_seat_amt_ok) THEN
                 v_reason_code := 'SV';-- seat variance
              ELSIF (NOT v_tax_amt_ok AND  v_seat_amt_ok) THEN
                 v_reason_code := 'TV';-- tax variance
              END IF;
              -- else use the reason code already determined above
              RAISE ex_early_exit;
          END IF;



          -- should never get here, if so there is an error
          v_msg := 'At statement:'||g_statement||', system error, wrong path in code';
          p_common.debug_message(v_msg);

          RAISE ex_major_error;




      EXCEPTION
      WHEN ex_reconciled THEN
         -- fully reconciled, log and move on to next row
          g_statement := 370;
          app_util.file_log(g_statement,null);
          IF v_reason_code = NULL THEN
                  v_full_match_count := v_full_match_count +1;
          ELSE
                  v_part_match_count := v_part_match_count +1;
          END IF;
          v_error_message := 'PNR:'||cmain.pnr_id||' '||cmain.pnr_no||' bkg:'||cmain.season_type||cmain.season_year||'/'||cmain.booking_reference_no||' reconciled, level:'||v_reason_code;

          update_recon_status(cmain.pnr_id, TRUE,NULL, v_run_date);

          update_recon_history(cmain.pnr_id, v_app_key, v_run_date, nvl(v_reason_code,'OK'), NULL,
                                                v_dwhse_seat_amt, v_dwhse_tax_amt, v_stella_seat_amt, v_stella_tax_amt,
                                                v_discrepancy_amt);


          p_common.debug_message(v_error_message);

      WHEN ex_early_exit THEN
           -- business reason why this pnr has failed reconciliation
           g_statement := 390;
           update_recon_status(cmain.pnr_id, FALSE,v_reason_code, v_run_date);
           p_common.debug_message(v_error_message);
           IF NOT v_tax_amt_ok OR NOT v_seat_amt_ok THEN
             -- slightly more serious error, log to exception table
             app_util.log(g_package_name,NULL,g_log_sequence,'ALL','BUSRULE','WARNING',
                           cmain.pnr_no, substr(v_error_message,1,150));
           END IF;

           update_recon_history(cmain.pnr_id, v_app_key, v_run_date, v_reason_code, NULL,
                                                v_dwhse_seat_amt, v_dwhse_tax_amt, v_stella_seat_amt, v_stella_tax_amt,
                                                v_discrepancy_amt);



      END; -- main loop block

    END LOOP; -- end main_cursor cmain loop

    g_statement := 999;
    app_util.file_log(g_statement,null);

    p_common.debug_message('Rows processed                   :'||v_mainloop_count);
    p_common.debug_message('Rows no bkg match                :'||v_no_booking_count);
    p_common.debug_message('Rows tax outside tolerances      :'||v_tax_diff_count);
    p_common.debug_message('Rows seat cost outside tolerances:'||v_seat_diff_count);
    p_common.debug_message('Rows part-reconciled             :'||v_part_match_count);
    p_common.debug_message('Rows fully matched 100%          :'||v_full_match_count);

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
          app_util.file_log(g_statement,v_msg||' Gen error'||g_sqlerrm);
          dbms_output.put_line(g_statement||' major error'||g_sqlerrm||g_sqlcode);

          app_util.end_logging;
          p_valid := FALSE;
          p_no_of_rows := 0;
          p_sqlerrm :='Major exception raised Stmt:'||g_statement||', '||substr(g_sqlerrm,1,100);
          app_util.log(g_package_name,NULL,g_log_sequence,'ALL','GENERROR','FATAL',
              'Major excp', substr('Major exception at:'||g_statement||','||g_sqlerrm||' '||v_parameters,1,200));

    WHEN OTHERS THEN
         g_sqlcode := SQLCODE;
         g_sqlerrm := substr(SQLERRM, 1,100);
         p_common.handle_error(g_package_name||'.dwhse_stella_recon'
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




  END dwhse_stella_reconcile; -- end procedure


------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------
PROCEDURE update_recon_status(
          p_pnr_id IN NUMBER,
          p_reconciled IN BOOLEAN,
          p_reason_code IN CHAR,
          p_run_date IN DATE
          ) AS

v_reconcile_char CHAR(1);

BEGIN

-- stamp new reconciled status on the pnr
   IF p_reconciled THEN
      v_reconcile_char := 'Y';
   ELSE
      v_reconcile_char := 'N';
   END IF;


   UPDATE pnr
   SET booking_reason_code = p_reason_code,
       pnr.booking_reconciled_ind = v_reconcile_char,
       pnr.last_reconcile_attempt_date = p_run_date
    WHERE pnr.pnr_id = p_pnr_id
   ;


END update_recon_status;

------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------
PROCEDURE update_recon_history (p_pnr_id IN NUMBER,
                          p_process_code IN CHAR,
                          p_process_date IN DATE,
                          p_reason_code IN CHAR,
                          p_amended_user_id IN CHAR,
                          p_data_warehouse_seat_cost IN NUMBER,
                          p_data_warehouse_tax_cost IN NUMBER,
                          p_stella_seat_cost IN NUMBER,
                          p_stella_tax_cost IN NUMBER,
                          p_unmatched_amt IN NUMBER
                           )
IS
/* insert pnr_reconciliation_history table
 there is no primary key*/
BEGIN

INSERT INTO pnr_reconciliation_history
(pnr_id,
 process_code,
 process_date,
 reason_code,
 amended_user_id,
 data_warehouse_seat_cost,
 data_warehouse_tax_cost,
 stella_seat_cost,
 stella_tax_cost,
 unmatched_amt
)
 VALUES
 (p_pnr_id,
 p_process_code,
 p_process_date,
 p_reason_code,
 nvl(p_amended_user_id,USER),
 p_data_warehouse_seat_cost,
 p_data_warehouse_tax_cost,
 p_stella_seat_cost,
 p_stella_tax_cost,
 p_unmatched_amt
 );


END update_recon_history;
------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------

  FUNCTION version
  RETURN VARCHAR2
  /* ***************************************************************************** *

  Date      By          Description                                         Version
  ----      ----------- --------------------------------------------------- -------
  10/01/03  A.James     Initial version.                                      001

  * ***************************************************************************** */
  IS
  BEGIN
    RETURN 'Package: '||g_package_name||'; Version: '||g_version;
  END Version;

------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------



PROCEDURE missing_ticket_report ( p_iata_no IN NUMBER,
                                  p_ticket_no_start IN NUMBER,
                                  p_ticket_no_end IN NUMBER,
/* some or all of these params will be passed */
/* allows flexibilty in design so any level of reconciliation can be started */
           p_sqlerrm OUT CHAR,
           p_no_of_rows OUT CHAR,
           p_valid OUT BOOLEAN)
IS

  v_app_key CHAR(8)  := 'STLMTKTR';
  v_parameters          VARCHAR2(200) := substr('Params:'
                                                ||'iata:'||p_iata_no
                                                ||'tkt:'||p_ticket_no_start
                                                ||'tkt:'||p_ticket_no_end
                                                 ,1,200);
v_error_message VARCHAR2(150);
v_run_date DATE;
v_num_ranges NUMBER :=0;
v_num_missing_tickets NUMBER :=0;
v_ticket_no ticket.ticket_no%TYPE;
v_check_tkt NUMBER(30);
v_end NUMBER(30);
v_msg VARCHAR2(200);
v_num_missing_this_range NUMBER := 0;
v_range_complete_ind CHAR(1) := 'N';
v_lowest NUMBER :=0;
v_highest NUMBER :=0;

CURSOR cursor_ranges IS
   SELECT iata_no, ticket_no_start, ticket_no_end  FROM ticket_range t
   WHERE t.search_enabled_ind= 'Y'
    AND  t.range_complete_ind = 'N'
    AND p_ticket_no_end IS NULL
    AND p_ticket_no_start IS NULL
    AND p_iata_no IS NULL
    UNION  -- if parameters passed then use these
    SELECT p_iata_no, p_ticket_no_start, p_ticket_no_end FROM dual
    WHERE p_ticket_no_end IS NOT NULL
    AND p_ticket_no_start IS NOT NULL
    AND p_iata_no IS NOT NULL;



BEGIN
      g_statement := 5;

      app_util.setup_logging(v_app_key,'LIVE','ALL');

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
      v_msg := 'List of Missing Ticket Ranges Follows';
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
            v_lowest := c_range.ticket_no_start;
            v_highest := c_range.ticket_no_start;
            v_msg := 'Range to check:'||v_num_ranges||' iata:'||c_range.iata_no||' tkt start:'||c_range.ticket_no_start||' tkt end:'||c_range.ticket_no_end||' '||to_char(sysdate,'yy-mon-dd hh24:mi:ss');
            p_common.debug_message(v_msg);
            app_util.file_log(v_msg);

            -- now loop through that entire range looking for each ticket number
            -- log if it does not exist
            g_statement := 50;
            v_num_missing_this_range := 0;


            v_end :=   c_range.ticket_no_end - c_range.ticket_no_start + 1;

            -- cannot use ticket number range directly in from/to limits of this for loop because the numbers are too high for pl/sql

            FOR looper IN 1..v_end LOOP
              BEGIN
               g_statement := 60;
               v_check_tkt := (c_range.ticket_no_start -1 )+ looper;

               SELECT ticket_no INTO v_ticket_no
                 FROM ticket
                 WHERE ticket.ticket_no = v_check_tkt
                 AND   ticket.iata_no = c_range.iata_no;

               --dbms_output.put_line ('exists :'||v_check_tkt);
               g_statement := 70;
               -- one has been found, so output the last range of missing ones
               IF v_num_missing_this_range > 0 THEN

                   v_msg     := 'Missing:iata,'||c_range.iata_no||', from,'||v_lowest||', up to,'||v_highest||',numtkts,'||v_num_missing_this_range;
                   v_lowest  := v_check_tkt + 1;
                   v_highest := v_check_tkt + 1;
                   p_common.debug_message(v_msg);
                   app_util.file_log(v_msg);
                   v_num_missing_this_range := 0;
               ELSE
                  v_lowest := v_check_tkt + 1;
               END IF;


               EXCEPTION
               WHEN no_data_found THEN
                -- missing ticket, so log
                 g_statement := 90;
                 IF v_check_tkt > v_highest THEN
                    v_highest := v_check_tkt;
                 END IF;
                 v_num_missing_tickets := v_num_missing_tickets + 1;
                 v_num_missing_this_range := v_num_missing_this_range + 1;
                 v_msg := 'ind. ticket,'||c_range.iata_no||','||v_check_tkt;
                 p_common.debug_message(v_msg);


              END;
            END LOOP; -- for next within ticket range loop

            g_statement := 110;
            IF v_num_missing_this_range > 0 THEN
              v_range_complete_ind := 'N';
              v_msg := 'Missing:iata,'||c_range.iata_no||', from,'||v_lowest||', up to,'||v_highest||',numtkts,'||v_num_missing_this_range;
              v_lowest := v_check_tkt + 1;
              v_highest := v_check_tkt + 1;
              p_common.debug_message(v_msg);
              app_util.file_log(v_msg);
            ELSE
              v_range_complete_ind := 'Y';
            END IF;


            UPDATE ticket_range t
            SET t.last_run_date = sysdate,
                range_complete_ind = v_range_complete_ind
            WHERE t.iata_no = c_range.iata_no
            AND   t.ticket_no_start = c_range.ticket_no_start;


           END ; --main loop block

           g_statement := 130;

           --v_msg := v_num_missing_this_range || ' missing tickets in above range';
           --p_common.debug_message(v_msg);
           --app_util.file_log(v_msg);


      END LOOP;    -- ranges loop



       g_statement := 999;
       v_msg := 'End of run';
       app_util.file_log( v_msg);
       app_util.file_log(null);
       v_msg := 'Ranges processed                   :'||v_num_ranges;
       p_common.debug_message(v_msg);
       app_util.file_log( v_msg);
       v_msg := 'Number of missing tickets in total :'||v_num_missing_tickets;
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



END missing_ticket_report;

------------------------------------------------------------------------------
------------------------------------------------------------------------------
------------------------------------------------------------------------------

END p_stella_reconciliation;
/
