create or replace package P_stella_maint is

  -- Author  : LSVENSSON
  -- Created : 23/05/03 10:47:41
  -- Purpose : Stella maintenance
  
  -- Public type declarations
  TYPE return_refcursor IS REF CURSOR;

  
----------------------------------------------------------------
----------------------------------------------------------------                      
-- Generic Maintenance GetAll function for Stella_Branch table
-- Returns a result set
FUNCTION branch_getall RETURN p_stella_maint.return_refcursor;


----------------------------------------------------------------
----------------------------------------------------------------                      
-- Generic Maintenance Save function for Stella_Branch table
-- Returns a result set
FUNCTION branch_save(
                      p_season_type CHAR,
                      p_season_year CHAR,
                      p_branch_code CHAR,
                      p_description CHAR,
                      p_data_warehouse_match_ind CHAR,
                      p_company_code CHAR,
                      p_action CHAR,      -- (A)dd (U)pdate (D)elete
                      p_user_name CHAR
                     ) RETURN CHAR;

                     
----------------------------------------------------------------
----------------------------------------------------------------                      
-- Generic Maintenance GetAll function for Ticketing_Agent table
-- Returns a result set
FUNCTION ticketing_agent_getall RETURN p_stella_maint.return_refcursor;


----------------------------------------------------------------
----------------------------------------------------------------                      
-- Generic Maintenance Save function for Ticketing_Agent table
-- Returns a result set
FUNCTION ticketing_agent_save(
                      p_ticketing_agent_initials CHAR,
                      p_agent_name CHAR,
                      p_action CHAR,      -- (A)dd (U)pdate (D)elete
                      p_user_name CHAR
                     ) RETURN CHAR;

                     
----------------------------------------------------------------
----------------------------------------------------------------                      
-- Generic Maintenance GetAll function for Ticket_Range table
-- Returns a result set
FUNCTION ticket_range_getall RETURN p_stella_maint.return_refcursor;


----------------------------------------------------------------
----------------------------------------------------------------                      
-- Generic Maintenance Save function for Ticket_Range table
-- Returns a result set
FUNCTION ticket_range_save(
                      p_iata_no NUMBER,
                      p_ticket_no_start NUMBER,
                      p_ticket_no_end NUMBER,
                      p_search_enabled_ind CHAR,
                      p_range_complete_ind CHAR,
                      p_action CHAR,      -- (A)dd (U)pdate (D)elete
                      p_user_name CHAR
                     ) RETURN CHAR;


----------------------------------------------------------------
----------------------------------------------------------------                      
-- Generic Maintenance GetAll function for Branch_Group table
-- Returns a result set
FUNCTION branch_group_getall RETURN p_stella_maint.return_refcursor;


----------------------------------------------------------------
----------------------------------------------------------------                      
-- Generic Maintenance GetAll function for Ticketing_Agent table
-- Returns a result set
FUNCTION branch_group_save(
                      p_group_code CHAR,
                      p_description CHAR,
                      p_action CHAR,      -- (A)dd (U)pdate (D)elete
                      p_user_name CHAR
                     ) RETURN CHAR;
                     

----------------------------------------------------------------
----------------------------------------------------------------                      
-- Generic Maintenance GetAll function for Branch_Group_Allocation table
-- Returns a result set
FUNCTION branch_group_allocation_getall RETURN p_stella_maint.return_refcursor;


----------------------------------------------------------------
----------------------------------------------------------------                      
-- Generic Maintenance GetAll function for branch_group_allocation table
-- Returns a result set
FUNCTION branch_group_allocation_save(
                      p_group_code CHAR,
                      p_season_type CHAR,
                      p_season_year CHAR,
                      p_branch_code CHAR,
                      p_action CHAR,      -- (A)dd (U)pdate (D)elete
                      p_user_name CHAR
                     ) RETURN CHAR;



end P_stella_maint;
/
create or replace package body P_stella_maint is

----------------------------------------------------------------
----------------------------------------------------------------                      
-- Generic Maintenance GetAll function for Stella_Branch table
-- Returns a result set
FUNCTION branch_getall
                      RETURN p_stella_maint.return_refcursor

IS

  v_result p_stella_maint.return_refcursor;
  
BEGIN

  OPEN v_result FOR
       SELECT
             b.season_type,
             b.season_year,
             b.branch_code,
             b.description,
             b.data_warehouse_match_ind,
             b.company_code
       FROM  stella.branch b
       ORDER BY 2, 1, 3;
      
  RETURN(v_result);
END; -- stella_branch_getall

                    
----------------------------------------------------------------
----------------------------------------------------------------                      
-- Generic Maintenance Save function for Stella_Branch table
-- 
FUNCTION branch_save(
                      p_season_type CHAR,
                      p_season_year CHAR,
                      p_branch_code CHAR,
                      p_description CHAR,
                      p_data_warehouse_match_ind CHAR,
                      p_company_code CHAR,
                      p_action CHAR,      -- (A)dd (U)pdate (D)elete
                      p_user_name CHAR
                     ) RETURN CHAR

IS

  dependencies_exist EXCEPTION;
  PRAGMA EXCEPTION_INIT(dependencies_exist, -2292);

BEGIN
-- 1. Validate input
  IF p_season_type NOT IN ('S','W') THEN
    RETURN 'Error, season type must be either S or W';
  END IF;
  IF p_season_year IS NULL THEN
    RETURN 'Error, season year is blank';
  ELSIF NOT app_util.is_number(p_season_year) THEN
    RETURN 'Error, season year is not numeric. ('||p_season_year||')';
  END IF;
  IF p_branch_code IS NULL THEN
    RETURN 'Error, branch code is blank';
  END IF;
  IF p_description IS NULL THEN
    RETURN 'Error, description is blank';
  END IF;
  IF p_data_warehouse_match_ind NOT IN ('Y','N') THEN
    RETURN 'Error, match indicator must be Y or N';
  END IF;
  IF p_company_code IS NULL THEN
    RETURN 'Error, company code is blank';
  END IF;
  IF p_action IS NULL THEN
    RETURN 'Error, internal error - action is blank';
  END IF;
  IF p_user_name IS NULL THEN
    RETURN 'Error, internal error - user name is blank';
  END IF;

  IF p_action = 'A' THEN
  
    BEGIN
      INSERT INTO stella.branch (
      season_type,
      season_year,
      branch_code,
      description,
      data_warehouse_match_ind,
      company_code,
      amended_user_id,
      amended_date)
      VALUES (
          p_season_type,
          p_season_year,
          p_branch_code,
          p_description,
          p_data_warehouse_match_ind,
          p_company_code,
          p_user_name,
          sysdate);
    EXCEPTION
      WHEN DUP_VAL_ON_INDEX THEN         -- already exists - update instead
        RETURN 'Error, data already exist';
    END;
    
  ELSIF p_action = 'U' THEN
  
    BEGIN
      UPDATE branch
      SET season_type = p_season_type,
          season_year = p_season_year,
          branch_code = p_branch_code,
          description = p_description,
          data_warehouse_match_ind = p_data_warehouse_match_ind,
          company_code = p_company_code,
          amended_user_id = p_user_name,
          amended_date = sysdate
      WHERE season_type = p_season_type AND
            season_year = p_season_year AND
            branch_code = p_branch_code;
    EXCEPTION
      WHEN OTHERS THEN
        ROLLBACK;
        RETURN 'Error, unable to update branch. '||substr(sqlerrm,1,150);
    END;
  
  ELSIF p_action = 'D' THEN
  
    BEGIN
      DELETE FROM branch
      WHERE season_type = p_season_type AND
            season_year = p_season_year AND
            branch_code = p_branch_code;
  
    EXCEPTION
      WHEN no_data_found then
        RETURN 'Error, could not delete branch - not found';

      WHEN dependencies_exist THEN
        ROLLBACK;
        RETURN 'Error, could not delete branch - dependencies exist';

      WHEN OTHERS THEN
        ROLLBACK;
        RETURN 'Error, '||substr(sqlerrm,1,150);
    END;

  ELSE
    RETURN 'Error, unknown action code ('+p_action+')';
    
  END IF;
  COMMIT;
  RETURN null;
END;


----------------------------------------------------------------
----------------------------------------------------------------                      
-- Generic Maintenance GetAll function for Ticketing_Agent table
-- Returns a result set
FUNCTION ticketing_agent_getall RETURN p_stella_maint.return_refcursor

IS

  v_result p_stella_maint.return_refcursor;
  
BEGIN

  OPEN v_result FOR
       SELECT ticketing_agent_initials, agent_name
       FROM  stella.ticketing_agent
       ORDER BY 1;
      
  RETURN(v_result);
END; -- ticketing_agent_getall


----------------------------------------------------------------
----------------------------------------------------------------                      
-- Generic Maintenance GetAll function for Ticketing_Agent table
-- Returns a result set
FUNCTION ticketing_agent_save(
                      p_ticketing_agent_initials CHAR,
                      p_agent_name CHAR,
                      p_action CHAR,      -- (A)dd (U)pdate (D)elete
                      p_user_name CHAR
                     ) RETURN CHAR

IS

  dependencies_exist EXCEPTION;
  PRAGMA EXCEPTION_INIT(dependencies_exist, -2292);

BEGIN
-- 1. Validate input
  IF p_ticketing_agent_initials IS NULL THEN
    RETURN 'Error, ticketing agent initials is blank';
  END IF;
  IF p_agent_name IS NULL THEN
    RETURN 'Error, agent name is blank';
  END IF;

  IF p_action = 'A' THEN
  
    BEGIN
      INSERT INTO stella.ticketing_agent (
        ticketing_agent_initials,
        agent_name,
        amended_user_id,
        amended_date)
      VALUES (
        p_ticketing_agent_initials,
        p_agent_name,
        p_user_name,
        sysdate);
    EXCEPTION
      WHEN DUP_VAL_ON_INDEX THEN         -- already exists - update instead
        RETURN 'Error, data already exist';
    END;
    
  ELSIF p_action = 'U' THEN
  
    BEGIN
      UPDATE stella.ticketing_agent
      SET agent_name = p_agent_name,
          amended_user_id = p_user_name,
          amended_date = sysdate
      WHERE ticketing_agent_initials = p_ticketing_agent_initials;
    EXCEPTION
      WHEN OTHERS THEN
        ROLLBACK;
        RETURN 'Error, unable to agent. '||substr(sqlerrm,1,150);
    END;
  
  ELSIF p_action = 'D' THEN
  
    BEGIN
      DELETE FROM stella.ticketing_agent
      WHERE ticketing_agent_initials = p_ticketing_agent_initials;
  
    EXCEPTION
      WHEN no_data_found then
        RETURN 'Error, could not delete agent - not found';

      WHEN dependencies_exist THEN
        ROLLBACK;
        RETURN 'Error, could not delete agent - dependencies exist';

      WHEN OTHERS THEN
        ROLLBACK;
        RETURN 'Error, '||substr(sqlerrm,1,150);
    END;

  ELSE
    RETURN 'Error, unknown action code ('+p_action+')';
    
  END IF;
  COMMIT;
  RETURN null;
END;


----------------------------------------------------------------
----------------------------------------------------------------                      
-- Generic Maintenance GetAll function for Ticket_Range table
-- Returns a result set
FUNCTION ticket_range_getall RETURN p_stella_maint.return_refcursor

IS

  v_result p_stella_maint.return_refcursor;
  
BEGIN

  OPEN v_result FOR
       SELECT t.iata_no,
              t.ticket_no_start,
              t.ticket_no_end,
              t.search_enabled_ind,
              t.range_complete_ind
       FROM  stella.ticket_range t
       ORDER BY 1,2;
      
  RETURN(v_result);
END; -- ticketing_agent_getall


----------------------------------------------------------------
----------------------------------------------------------------                      
-- Generic Maintenance GetAll function for Ticketing_Agent table
-- Returns a result set
FUNCTION ticket_range_save(
                      p_iata_no NUMBER,
                      p_ticket_no_start NUMBER,
                      p_ticket_no_end NUMBER,
                      p_search_enabled_ind CHAR,
                      p_range_complete_ind CHAR,
                      p_action CHAR,      -- (A)dd (U)pdate (D)elete
                      p_user_name CHAR
                     ) RETURN CHAR

IS

  dependencies_exist EXCEPTION;
  PRAGMA EXCEPTION_INIT(dependencies_exist, -2292);

BEGIN
-- 1. Validate input
  IF p_iata_no IS NULL THEN
    RETURN 'Error, iata number is blank';
  ELSIF NOT app_util.is_number(p_iata_no) THEN
    RETURN 'Error, iata no is not numeric. ('||p_iata_no||')';
  END IF;
  IF p_ticket_no_start IS NULL THEN
    RETURN 'Error, ticket start range is blank';
  ELSIF NOT app_util.is_number(p_ticket_no_start) THEN
    RETURN 'Error, ticket start range is not numeric. ('||p_ticket_no_start||')';
  END IF;
  IF p_ticket_no_end IS NULL THEN
    RETURN 'Error, ticket end range is blank';
  ELSIF NOT app_util.is_number(p_ticket_no_end) THEN
    RETURN 'Error, ticket end range is not numeric. ('||p_ticket_no_end||')';
  END IF;
  IF p_ticket_no_end < p_ticket_no_start THEN
    RETURN 'Error, end of range is smaller than start of range';
  END IF;
  IF p_search_enabled_ind NOT IN ('Y','N') THEN
    RETURN 'Error, search enabled indicator must be Y or N';
  END IF;
  IF p_range_complete_ind NOT IN ('Y','N') THEN
    RETURN 'Error, range complete indicator must be Y or N';
  END IF;

  IF p_action = 'A' THEN
  
    BEGIN
      INSERT INTO stella.ticket_range (
        iata_no,
        ticket_no_start,
        ticket_no_end,
        search_enabled_ind,
        range_complete_ind,
        amended_user_id,
        amended_date)
      VALUES (
        p_iata_no,
        p_ticket_no_start,
        p_ticket_no_end,
        p_search_enabled_ind,
        p_range_complete_ind,
        p_user_name,
        sysdate);
    EXCEPTION
      WHEN DUP_VAL_ON_INDEX THEN         -- already exists - update instead
        RETURN 'Error, data already exist';
    END;
    
  ELSIF p_action = 'U' THEN
  
    BEGIN
      UPDATE stella.ticket_range
      SET 
        ticket_no_end = p_ticket_no_end,
        search_enabled_ind = p_search_enabled_ind,
        range_complete_ind = p_range_complete_ind,
        amended_user_id = p_user_name,
        amended_date = sysdate
      WHERE iata_no = p_iata_no AND ticket_no_start = p_ticket_no_start;

    EXCEPTION
      WHEN OTHERS THEN
        ROLLBACK;
        RETURN 'Error, unable to update range. '||substr(sqlerrm,1,150);
    END;
  
  ELSIF p_action = 'D' THEN
  
    BEGIN
      DELETE FROM stella.ticket_range
      WHERE iata_no = p_iata_no AND ticket_no_start = p_ticket_no_start;
  
    EXCEPTION
      WHEN no_data_found then
        RETURN 'Error, could not delete range - not found';

      WHEN dependencies_exist THEN
        ROLLBACK;
        RETURN 'Error, could not delete range - dependencies exist';

      WHEN OTHERS THEN
        ROLLBACK;
        RETURN 'Error, '||substr(sqlerrm,1,150);
    END;

  ELSE
    RETURN 'Error, unknown action code ('+p_action+')';
    
  END IF;
  COMMIT;
  RETURN null;
END;


----------------------------------------------------------------
----------------------------------------------------------------                      
-- Generic Maintenance GetAll function for Branch_Group table
-- Returns a result set
FUNCTION branch_group_getall RETURN p_stella_maint.return_refcursor

IS

  v_result p_stella_maint.return_refcursor;
  
BEGIN

  OPEN v_result FOR
       SELECT t.group_code,
              t.description
       FROM  stella.branch_group t
       ORDER BY 1;
      
  RETURN(v_result);
END; -- branch_group_getall


----------------------------------------------------------------
----------------------------------------------------------------                      
-- Generic Maintenance GetAll function for Ticketing_Agent table
-- Returns a result set
FUNCTION branch_group_save(
                      p_group_code CHAR,
                      p_description CHAR,
                      p_action CHAR,      -- (A)dd (U)pdate (D)elete
                      p_user_name CHAR
                     ) RETURN CHAR

IS

  dependencies_exist EXCEPTION;
  PRAGMA EXCEPTION_INIT(dependencies_exist, -2292);

BEGIN
-- 1. Validate input
  IF p_group_code IS NULL THEN
    RETURN 'Error, group code is blank';
  END IF;
  IF p_description IS NULL THEN
    RETURN 'Error, description is blank';
  end if;
  
  IF p_action = 'A' THEN
  
    BEGIN
      INSERT INTO stella.branch_group (
        group_code,
        description
--        amended_user_id,
--        amended_date
        )
      VALUES (
        p_group_code,
        p_description
--        p_user_name,
--        sysdate
        );
    EXCEPTION
      WHEN DUP_VAL_ON_INDEX THEN         -- already exists - update instead
        RETURN 'Error, data already exist';
    END;
    
  ELSIF p_action = 'U' THEN
  
    BEGIN
      UPDATE stella.branch_group
      SET 
        description = p_description
--        amended_user_id = p_user_name,
--        amended_date = sysdate
      WHERE group_code = p_group_code;

    EXCEPTION
      WHEN OTHERS THEN
        ROLLBACK;
        RETURN 'Error, unable to update. '||substr(sqlerrm,1,150);
    END;
  
  ELSIF p_action = 'D' THEN
  
    BEGIN
      DELETE FROM stella.branch_group
      WHERE group_code = p_group_code;
  
    EXCEPTION
      WHEN no_data_found then
        RETURN 'Error, could not delete - not found';

      WHEN dependencies_exist THEN
        ROLLBACK;
        RETURN 'Error, could not delete - dependencies exist';

      WHEN OTHERS THEN
        ROLLBACK;
        RETURN 'Error, '||substr(sqlerrm,1,150);
    END;

  ELSE
    RETURN 'Error, unknown action code ('+p_action+')';
    
  END IF;
  COMMIT;
  RETURN null;
END;


----------------------------------------------------------------
----------------------------------------------------------------                      
-- Generic Maintenance GetAll function for Branch_Group_Allocation table
-- Returns a result set
FUNCTION branch_group_allocation_getall RETURN p_stella_maint.return_refcursor

IS

  v_result p_stella_maint.return_refcursor;
  
BEGIN

  OPEN v_result FOR
       SELECT t.group_code,
              t.season_type,
              t.season_year,
              t.branch_code
       FROM  stella.branch_group_allocation t
       ORDER BY 1,2,3,4;
      
  RETURN(v_result);
END; -- branch_group_allocation_getall


----------------------------------------------------------------
----------------------------------------------------------------                      
-- Generic Maintenance GetAll function for branch_group_allocation table
-- Returns a result set
FUNCTION branch_group_allocation_save(
                      p_group_code CHAR,
                      p_season_type CHAR,
                      p_season_year CHAR,
                      p_branch_code CHAR,
                      p_action CHAR,      -- (A)dd (U)pdate (D)elete
                      p_user_name CHAR
                     ) RETURN CHAR

IS

  dependencies_exist EXCEPTION;
  parent_doesnot_exist EXCEPTION;
  PRAGMA EXCEPTION_INIT(dependencies_exist, -2292);
  PRAGMA EXCEPTION_INIT(parent_doesnot_exist, -2291);

BEGIN
-- 1. Validate input
  IF p_group_code IS NULL THEN
    RETURN 'Error, group code is blank';
  END IF;
  IF p_season_type NOT IN ('S','W') THEN
    RETURN 'Error, season type must be either S or W';
  END IF;
  IF p_season_year IS NULL THEN
    RETURN 'Error, season year is blank';
  ELSIF NOT app_util.is_number(p_season_year) THEN
    RETURN 'Error, season year is not numeric. ('||p_season_year||')';
  END IF;
  IF p_branch_code IS NULL THEN
    RETURN 'Error, branch code is blank';
  END IF;
  
  IF p_action = 'A' THEN
  
    BEGIN
      INSERT INTO stella.branch_group_allocation (
        group_code,
        season_type,
        season_year,
        branch_code,
        amended_user_id,
        amended_date
        )
      VALUES (
        p_group_code,
        p_season_type,
        p_season_year,
        p_branch_code,
        p_user_name,
        sysdate
        );
    EXCEPTION
      WHEN DUP_VAL_ON_INDEX THEN         -- already exists - update instead
        RETURN 'Error, data already exist';
        
      WHEN parent_doesnot_exist THEN
        RETURN 'Error, dependent parent record missing';
        
      WHEN OTHERS THEN
        RETURN 'Error, '||substr(sqlerrm,1,150);
       
    END;
    
  ELSIF p_action = 'U' THEN
  
    BEGIN
      UPDATE stella.branch_group_allocation
      SET 
        branch_code = p_branch_code,
        amended_user_id = p_user_name,
        amended_date = sysdate
      WHERE group_code = p_group_code AND 
            season_type = p_season_type AND
            season_year = p_season_year;

    EXCEPTION
      WHEN OTHERS THEN
        ROLLBACK;
        RETURN 'Error, unable to update. '||substr(sqlerrm,1,150);
    END;
  
  ELSIF p_action = 'D' THEN
  
    BEGIN
      DELETE FROM stella.branch_group_allocation
      WHERE group_code = p_group_code AND 
            season_type = p_season_type AND
            season_year = p_season_year AND
            branch_code = p_branch_code;
  
    EXCEPTION
      WHEN no_data_found then
        RETURN 'Error, could not delete - not found';

      WHEN dependencies_exist THEN
        ROLLBACK;
        RETURN 'Error, could not delete - dependencies exist';

      WHEN OTHERS THEN
        ROLLBACK;
        RETURN 'Error, '||substr(sqlerrm,1,150);
    END;

  ELSE
    RETURN 'Error, unknown action code ('+p_action+')';
    
  END IF;
  COMMIT;
  RETURN null;
END;


END P_stella_maint;
/
