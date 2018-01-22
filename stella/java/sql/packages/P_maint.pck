CREATE OR REPLACE PACKAGE P_maint IS
  -- Author  : Lars Svensson
  -- Created : 08/05/03
  -- Purpose : Defines the procedures and functions for the Stella system
  --         : data extracts. These stored procedures / functions are used by java programs to 
  --         : read data from Oracle


  -- Public type declarations
  TYPE return_refcursor IS REF CURSOR;


  g_version NUMBER:= 1.00;
  g_statement NUMBER := 0;
  g_sqlerrm VARCHAR2(500);
  g_sqlcode CHAR(20);

----------------------------------------------------------------
----------------------------------------------------------------                      
-- Gets a single airline's full details from the AIRLINE table, and returns
-- it as result set
FUNCTION get_airline_details( p_airline_num NUMBER )
                      RETURN p_maint.return_refcursor;
  
----------------------------------------------------------------
----------------------------------------------------------------
-- Either insert or update a single airlines details in the AIRLINE table, depending
-- on whether p_airline_nm already exist.
-- Returns NULL if successfull or "Error, "+error if not 
FUNCTION save_airline_details( 
                               p_airline_num NUMBER,
                               p_airline_code CHAR,
                               p_airline_name CHAR,
                               p_contact_name CHAR,
                               p_address_line_1 CHAR,
                               p_address_line_2 CHAR,
                               p_city_town CHAR,
                               p_county CHAR,
                               p_country CHAR,
                               p_postcode CHAR,
                               p_telephone_no CHAR,
                               p_addressee CHAR,
                               p_tax_amt_tolerance_level NUMBER,
                               p_seat_amt_tolerance_level NUMBER,
                               p_sector_payment_ind CHAR,
                               p_bkg_payment_commission_amt NUMBER,
                               p_amended_user_id CHAR
                             ) RETURN CHAR;

----------------------------------------------------------------
----------------------------------------------------------------
-- Delete a single airline from the AIRLINE table
-- Returns NULL if successfull or "Error, "+error if not
FUNCTION delete_airline_details( p_airline_num NUMBER ) RETURN CHAR;
                          

end P_maint;   -- end of package HEADER
  
  
  
  
/
CREATE OR REPLACE PACKAGE BODY p_maint IS

  -- Author  : Lars Svensson
  -- Created : 07/05/03
  -- Purpose : Defines the procedures and functions for the Stella system
  --         : data extracts. These stored procedures / functions are used by java programs to 
  --         : read data from Oracle



  -- Define the application key, used to retrieve properties from the registry
  -- and as the top level key for logging.
  --v_app_key CHAR(8)  := 'STELLADT';

  
----------------------------------------------------------------
----------------------------------------------------------------                      
-- Gets a single airline's full details from the AIRLINE table, and returns
-- it as result set
FUNCTION get_airline_details( p_airline_num NUMBER )
                      RETURN p_maint.return_refcursor

IS

  v_result p_maint.return_refcursor;
  
BEGIN

  OPEN v_result FOR
       SELECT *
       FROM  airline
       WHERE airline_num = p_airline_num;
      
  RETURN(v_result);
END; -- end function get_airline_details


----------------------------------------------------------------
----------------------------------------------------------------
-- Either insert or update a single airlines details in the AIRLINE table, depending
-- on whether p_airline_nm already exist.
-- Returns NULL if successfull or "Error, "+error if not 
FUNCTION save_airline_details( 
                               p_airline_num NUMBER,
                               p_airline_code CHAR,
                               p_airline_name CHAR,
                               p_contact_name CHAR,
                               p_address_line_1 CHAR,
                               p_address_line_2 CHAR,
                               p_city_town CHAR,
                               p_county CHAR,
                               p_country CHAR,
                               p_postcode CHAR,
                               p_telephone_no CHAR,
                               p_addressee CHAR,
                               p_tax_amt_tolerance_level NUMBER,
                               p_seat_amt_tolerance_level NUMBER,
                               p_sector_payment_ind CHAR,
                               p_bkg_payment_commission_amt NUMBER,
                               p_amended_user_id CHAR
                             ) RETURN CHAR
                             
IS

  v_error_statement VARCHAR2(150) := '';
  ex_data_invalid EXCEPTION;

BEGIN
-- 1. validate data
  IF p_airline_num IS NULL THEN
    RETURN 'Error, airline number is blank';
  END IF;
  IF p_airline_code IS NULL THEN
    RETURN 'Error, airline code is blank';
  END IF;
  IF p_airline_name IS NULL THEN
    RETURN 'Error, airline name is blank';
  END IF;
  IF p_address_line_1 IS NULL THEN
    RETURN 'Error, first line of airline address is blank';
  END IF;
  IF p_city_town IS NULL THEN
    RETURN 'Error, airline city/town is blank';
  END IF;
  IF p_postcode IS NULL THEN
    RETURN 'Error, postcode is blank';
  END IF;
  IF p_addressee IS NULL THEN
    RETURN 'Error, addressee is blank';
  END IF;
  IF p_tax_amt_tolerance_level IS NULL THEN
    RETURN 'Error, tax cost tolerance level is blank';
  END IF;
  IF p_seat_amt_tolerance_level IS NULL THEN
    RETURN 'Error, seat cost tolerance level is blank';
  END IF;
  IF p_sector_payment_ind IS NULL THEN
    RETURN 'Error, Sector Payment Airline indicator is blank';
  END IF;
  IF p_sector_payment_ind = 'Y' THEN
    IF p_bkg_payment_commission_amt IS NULL THEN
      RETURN 'Error, Booking Payment Commission is blank';
    END IF;
  END IF;
  IF p_amended_user_id IS NULL THEN
    RETURN 'Error, amended user id is blank';
  END IF;

-- 2. attempt to insert - if exception "dup_val_on_index" then attempt to update
  BEGIN
    INSERT INTO airline (
      airline_num,
      airline_code,
      airline_name,
      contact_name,
      address_line_1,
      address_line_2,
      city_town,
      county,
      country,
      postcode,
      telephone_no,
      addressee,
      tax_amt_tolerance_level,
      seat_amt_tolerance_level,
      sector_payment_ind,
      bkg_payment_commission_amt,
      amended_user_id,
      amended_date)
      VALUES (
          p_airline_num,
          p_airline_code,
          p_airline_name,
          p_contact_name,
          p_address_line_1,
          p_address_line_2,
          p_city_town,
          p_county,
          p_country,
          p_postcode,
          p_telephone_no,
          p_addressee,
          p_tax_amt_tolerance_level,
          p_seat_amt_tolerance_level,
          p_sector_payment_ind,
          p_bkg_payment_commission_amt,
          p_amended_user_id,
          sysdate);
  EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN         -- already exists - update instead
      BEGIN
        UPDATE airline 
        SET airline_code = p_airline_code,
            airline_name = p_airline_name,
            contact_name = p_contact_name,
            address_line_1 = p_address_line_1,
            address_line_2 = p_address_line_2,
            city_town = p_city_town,
            county = p_county,
            country = p_country,
            postcode = p_postcode,
            telephone_no = p_telephone_no,
            addressee = p_addressee,
            tax_amt_tolerance_level = p_tax_amt_tolerance_level,
            seat_amt_tolerance_level = p_seat_amt_tolerance_level,
            sector_payment_ind = p_sector_payment_ind,
            bkg_payment_commission_amt = p_bkg_payment_commission_amt,
            amended_user_id = p_amended_user_id,
            amended_date = sysdate
        WHERE airline_num = p_airline_num;
      EXCEPTION
        WHEN OTHERS THEN
          ROLLBACK;
          RETURN 'Error, unable to update airline. '||substr(sqlerrm,1,150);
      END;
    WHEN OTHERS THEN
      ROLLBACK;
      RETURN 'Error, unable to insert airline. '||substr(sqlerrm,1,150);
  END;
  COMMIT;
  RETURN null;
END;


----------------------------------------------------------------
----------------------------------------------------------------
-- Delete a single airline from the AIRLINE table
-- Returns NULL if successfull or "Error, "+error if not
FUNCTION delete_airline_details( p_airline_num NUMBER ) RETURN CHAR
IS

  dependencies_exist EXCEPTION;
  PRAGMA EXCEPTION_INIT(dependencies_exist, -2292);
  
BEGIN
-- attempt to delete row where airline_num = p_airline_num
-- catch exceptions "not found" and "dependencies exist" and return propper error
  DELETE FROM airline
  WHERE airline_num = p_airline_num;
  COMMIT;
  RETURN null;
  
EXCEPTION
  WHEN no_data_found then
    RETURN 'Error, could not delete airline# '||p_airline_num||' - not found';

  WHEN dependencies_exist THEN
    ROLLBACK;
    RETURN 'Error, tickets or mappings for this airline must be deleted first';

  WHEN OTHERS THEN
    ROLLBACK;
    RETURN 'Error, '||substr(sqlerrm,1,150);
 
END; --FUNCTION delete_airline_details

                      

END p_maint; -- end of package body
/
