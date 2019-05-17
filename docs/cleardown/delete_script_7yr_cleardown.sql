
----------------------TLINK_VIEW------------------
-- No indexes
            
-- Get records to keep from tlink_view (exists is faster) 362,475 records

-- create table of only 280 records? Check this!! All ok,
CREATE TABLE tlink_view_new
TABLESPACE ts_stella
AS 
SELECT * FROM tlink_view tv
WHERE EXISTS
            (
            SELECT  1
            FROM    ticket  t,
                    pnr     p
            WHERE   t.pnr_id = p.pnr_id 
            AND     t.ticket_issue_date >= TO_DATE('1-oct-'||TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')-7))
            AND     p.pnr_no = tv.pnr_no 
            );            
            
-- backup old tlink view table          
CREATE TABLE tlink_view_old    
TABLESPACE ts_stella      
AS SELECT * FROM tlink_view;

-- Drop the original table
DROP TABLE tlink_view CASCADE CONSTRAINTS PURGE;

-- Create replacement table from the new records we are keeping
CREATE TABLE tlink_view 
TABLESPACE ts_stella
AS SELECT * FROM tlink_view_new;

--Drop the no longer needed keep set of records.
DROP TABLE tlink_view_new CASCADE CONSTRAINTS PURGE; 


-- Grants and synonyms
CREATE OR REPLACE SYNONYM BOXIUSER.TLINK_VIEW FOR STELLA.TLINK_VIEW;


CREATE OR REPLACE PUBLIC SYNONYM TLINK_VIEW FOR STELLA.TLINK_VIEW;


GRANT SELECT ON STELLA.TLINK_VIEW TO BO_ROLE;

GRANT SELECT ON STELLA.TLINK_VIEW TO PUBLIC;


COMMIT;
----------------------TLINK_VIEW------------------






---------------REFUND_LETTER_TICKET-----------
--  IS EMPTY  ---
--

/* DELETE
FROM REFUND_LETTER_TICKET
WHERE TICKET_NO >= 2572265323 AND TICKET_NO <= 2572265422
;
COMMIT; */
---------------REFUND_LETTER_TICKET-----------





---------------REFUND_TICKET ---------------- 174,378 rows small so we can run delete across it??
--backup data
CREATE TABLE refund_ticket_old
TABLESPACE ts_stella
AS SELECT * FROM refund_ticket;


            
-- count records for deletion less than 01-OCT-12 (7 years ago finance year)
SELECT COUNT(*) FROM  REFUND_TICKET rt
WHERE EXISTS 
            (
            SELECT  1
            FROM    TICKET t
            WHERE   t.ticket_issue_date < TO_DATE('1-oct-'||TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')-7))
            AND     t.ticket_no = rt.ticket_no
            );

            
-- deletion of records directly as under 200,000 records
DELETE FROM  REFUND_TICKET rt
WHERE EXISTS 
            (
            SELECT  1
            FROM    TICKET t
            WHERE   t.ticket_issue_date < TO_DATE('1-oct-'||TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')-7))
            AND     t.ticket_no = rt.ticket_no
            );  

COMMIT;

--Indexes for reference
/* CREATE INDEX STELLA.IDX_REFTKT_TKTNO ON STELLA.REFUND_TICKET
(TICKET_NO)
LOGGING
TABLESPACE TS_STELLA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          3M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


CREATE UNIQUE INDEX STELLA.REFTKT_PK ON STELLA.REFUND_TICKET
(REFUND_DOCUMENT_NO, TICKET_NO)
LOGGING
TABLESPACE TS_STELLA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          4M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


CREATE INDEX STELLA.XIF2REFUND_TICKET ON STELLA.REFUND_TICKET
(AIRLINE_NUM)
LOGGING
TABLESPACE TS_STELLA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          3M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


CREATE INDEX STELLA.XIF1REFUND_TICKET ON STELLA.REFUND_TICKET
(REFUND_DOCUMENT_NO)
LOGGING
TABLESPACE TS_STELLA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          3M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


CREATE OR REPLACE SYNONYM BOXIUSER.REFUND_TICKET FOR STELLA.REFUND_TICKET;


CREATE OR REPLACE PUBLIC SYNONYM REFUND_TICKET FOR STELLA.REFUND_TICKET;


ALTER TABLE STELLA.REFUND_TICKET ADD (
  CONSTRAINT BRGRPALL_POSITIVE_NUMBER185
  CHECK (ticket_no >= 0)
  ENABLE VALIDATE,
  CONSTRAINT REFBTCH_POSITIVE_NUMBER22
  CHECK (refund_document_no >= 1)
  ENABLE VALIDATE,
  CONSTRAINT REFTKT_PK
  PRIMARY KEY
  (REFUND_DOCUMENT_NO, TICKET_NO)
  USING INDEX STELLA.REFTKT_PK
  ENABLE VALIDATE);

ALTER TABLE STELLA.REFUND_TICKET ADD (
  CONSTRAINT FK_TKT_REFTKT 
  FOREIGN KEY (TICKET_NO) 
  REFERENCES STELLA.TICKET (TICKET_NO)
  ENABLE VALIDATE,
  CONSTRAINT REFBTCH_REFTKT_FK 
  FOREIGN KEY (REFUND_DOCUMENT_NO) 
  REFERENCES STELLA.REFUND_BATCH (REFUND_DOCUMENT_NO)
  ENABLE VALIDATE,
  CONSTRAINT REFTKT_AIRL_FK 
  FOREIGN KEY (AIRLINE_NUM) 
  REFERENCES STELLA.AIRLINE (AIRLINE_NUM)
  ENABLE VALIDATE);

GRANT SELECT ON STELLA.REFUND_TICKET TO BO_ROLE;

GRANT SELECT ON STELLA.REFUND_TICKET TO PUBLIC;

GRANT DELETE, INSERT, SELECT, UPDATE ON STELLA.REFUND_TICKET TO STELLAINTRANET;
 */
---------------REFUND_TICKET ----------------







------------- PNR_RECONCILIATION_HISTORY ----------  1,429,182 rows

CREATE TABLE pnr_reconciliation_history_new
TABLESPACE ts_stella
AS 
SELECT * FROM pnr_reconciliation_history ph
WHERE EXISTS 
            (
            SELECT  1
            FROM    ticket  t
            WHERE   t.pnr_id = ph.pnr_id 
            AND     t.ticket_issue_date >= TO_DATE('1-oct-'||TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')-7)) 
            );            
            
-- backup old bsp_transaction table          
CREATE TABLE pnr_reconciliation_history_old
TABLESPACE ts_stella          
AS SELECT * FROM pnr_reconciliation_history;

-- Drop the original table
DROP TABLE pnr_reconciliation_history CASCADE CONSTRAINTS PURGE;

-- Create replacement table from the new records we are keeping
CREATE TABLE pnr_reconciliation_history 
TABLESPACE ts_stella
AS SELECT * FROM pnr_reconciliation_history_new;

--Drop the no longer needed keep set of records.
DROP TABLE pnr_reconciliation_history_new CASCADE CONSTRAINTS PURGE; 

----Indexes
CREATE UNIQUE INDEX STELLA.PNR_RECHIST_PK ON STELLA.PNR_RECONCILIATION_HISTORY
(PNR_ID, PROCESS_DATE, PROCESS_CODE, REASON_CODE)
LOGGING
TABLESPACE TS_STELLA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          43M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


CREATE INDEX STELLA.XIF1PNR_RECONCILIATION_HISTORY ON STELLA.PNR_RECONCILIATION_HISTORY
(PNR_ID)
LOGGING
TABLESPACE TS_STELLA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          16M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;

--synonym
CREATE OR REPLACE SYNONYM BOXIUSER.PNR_RECONCILIATION_HISTORY FOR STELLA.PNR_RECONCILIATION_HISTORY;


--constraints
ALTER TABLE STELLA.PNR_RECONCILIATION_HISTORY ADD (
  CONSTRAINT PNR_RECHIST_PK
  PRIMARY KEY
  (PNR_ID, PROCESS_DATE, PROCESS_CODE, REASON_CODE)
  USING INDEX STELLA.PNR_RECHIST_PK
  ENABLE VALIDATE);

ALTER TABLE STELLA.PNR_RECONCILIATION_HISTORY ADD (
  CONSTRAINT PNR_PNRHIST_FK 
  FOREIGN KEY (PNR_ID) 
  REFERENCES STELLA.PNR (PNR_ID)
  ENABLE VALIDATE);


-- grants
GRANT SELECT ON STELLA.PNR_RECONCILIATION_HISTORY TO BO_ROLE;

GRANT SELECT ON STELLA.PNR_RECONCILIATION_HISTORY TO PUBLIC;



COMMIT;


------------- PNR_RECONCILIATION_HISTORY ----------



---------- BSP_TRANSACTION --------------
--3,330,818 records

CREATE TABLE bsp_transaction_new
TABLESPACE ts_stella
AS 
SELECT * FROM bsp_transaction bt
WHERE EXISTS 
            (
            SELECT  1
            FROM    TICKET t
            WHERE   t.ticket_issue_date >= TO_DATE('1-oct-'||TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')-7))
            AND     t.ticket_no = bt.ticket_no
            )     
or          (entry_date_time >= TO_DATE('1-oct-'||TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')-7))
            and refund_document_no is not null);  
            


            
-- backup old bsp_transaction table          
CREATE TABLE bsp_transaction_old   
TABLESPACE ts_stella       
AS SELECT * FROM bsp_transaction;

-- Drop the original table
DROP TABLE bsp_transaction CASCADE CONSTRAINTS PURGE;

-- Create replacement table from the new records we are keeping
CREATE TABLE bsp_transaction 
TABLESPACE ts_stella
AS SELECT * FROM bsp_transaction_new;

--Drop the no longer needed keep set of records.
DROP TABLE bsp_transaction_new CASCADE CONSTRAINTS PURGE; 

-- indexes
CREATE INDEX STELLA.BSPTRN_IDX_TKT ON STELLA.BSP_TRANSACTION
(TICKET_NO)
LOGGING
TABLESPACE TS_STELLA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          24M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


CREATE UNIQUE INDEX STELLA.XPKBSP_TRANSACTION ON STELLA.BSP_TRANSACTION
(BSP_TRANS_ID)
LOGGING
TABLESPACE TS_STELLA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          19M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;

-- synonyms
CREATE OR REPLACE SYNONYM BOXIUSER.BSP_TRANSACTION FOR STELLA.BSP_TRANSACTION;


CREATE OR REPLACE PUBLIC SYNONYM BSP_TRANSACTION FOR STELLA.BSP_TRANSACTION;

-- constraints
ALTER TABLE STELLA.BSP_TRANSACTION ADD (
  CONSTRAINT REFBTCH_Y_OR_N_IND48
  CHECK (reconciled_ind IN ('Y', 'N'))
  ENABLE VALIDATE,
  CONSTRAINT XPKBSP_TRANSACTION
  PRIMARY KEY
  (BSP_TRANS_ID)
  USING INDEX STELLA.XPKBSP_TRANSACTION
  ENABLE VALIDATE);


-- grants
GRANT SELECT ON STELLA.BSP_TRANSACTION TO BO_ROLE;

GRANT SELECT ON STELLA.BSP_TRANSACTION TO PUBLIC;



---------- BSP_TRANSACTION --------------



------------PNR ------------------- 1,097,990 Rows
CREATE TABLE pnr_new
TABLESPACE ts_stella
AS 
SELECT * FROM pnr p
WHERE EXISTS
            (
            SELECT  1
            FROM    ticket  t
            WHERE   t.ticket_issue_date >= TO_DATE('1-oct-'||TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')-7))
            AND     t.pnr_id = p.pnr_id 
            );    

COMMIT;


-- backup old pnr table          
CREATE TABLE pnr_old 
TABLESPACE ts_stella         
AS SELECT * FROM pnr;

-- Drop the original table
DROP TABLE pnr CASCADE CONSTRAINTS PURGE;

-- Create replacement table from the new records we are keeping
CREATE TABLE pnr 
TABLESPACE ts_stella
AS SELECT * FROM pnr_new;

--Drop the no longer needed keep set of records.
DROP TABLE pnr_new CASCADE CONSTRAINTS PURGE; 

-- indexes
CREATE INDEX STELLA.XIF11PNR ON STELLA.PNR
(SEASON_TYPE, SEASON_YEAR, BRANCH_CODE)
LOGGING
TABLESPACE TS_STELLA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          14M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


CREATE UNIQUE INDEX STELLA.IDX_PNR_UNQ_1 ON STELLA.PNR
(PNR_NO, PNR_CREATION_DATE, CRS_CODE)
LOGGING
TABLESPACE TS_STELLA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          17M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


CREATE UNIQUE INDEX STELLA.PNR_PK ON STELLA.PNR
(PNR_ID)
LOGGING
TABLESPACE TS_STELLA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          8M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


CREATE INDEX STELLA.XIF2PNR ON STELLA.PNR
(CRS_CODE)
LOGGING
TABLESPACE TS_STELLA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          9M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;

-- synonyms
CREATE OR REPLACE SYNONYM BOXIUSER.PNR FOR STELLA.PNR;


CREATE OR REPLACE PUBLIC SYNONYM PNR FOR STELLA.PNR;

-- constraints
ALTER TABLE STELLA.PNR ADD (
  CONSTRAINT PNR_PK
  PRIMARY KEY
  (PNR_ID)
  USING INDEX STELLA.PNR_PK
  ENABLE VALIDATE);

ALTER TABLE STELLA.PNR ADD (
  CONSTRAINT CRS_PNR_FK 
  FOREIGN KEY (CRS_CODE) 
  REFERENCES STELLA.CRS (CRS_CODE)
  ENABLE VALIDATE,
  CONSTRAINT FK_REASON_PNR 
  FOREIGN KEY (BOOKING_REASON_CODE) 
  REFERENCES STELLA.REASON (REASON_CODE)
  ENABLE VALIDATE,
  CONSTRAINT PNR_BRNCH_FK 
  FOREIGN KEY (SEASON_TYPE, SEASON_YEAR, BRANCH_CODE) 
  REFERENCES STELLA.BRANCH (SEASON_TYPE,SEASON_YEAR,BRANCH_CODE)
  ENABLE VALIDATE);


-- grants
GRANT SELECT ON STELLA.PNR TO BO_ROLE;

GRANT SELECT ON STELLA.PNR TO PUBLIC;

GRANT DELETE, INSERT, SELECT, UPDATE ON STELLA.PNR TO STELLAINTRANET;


------------PNR ------------------- 1,097,990 Rows




------- Lastly driving table Ticket -------------- 2,872,226
CREATE TABLE ticket_new
TABLESPACE ts_stella
AS 
SELECT * FROM ticket t
WHERE   t.ticket_issue_date >= TO_DATE('1-oct-'||TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')-7))
;    

COMMIT;


-- backup old ticket table          
CREATE TABLE ticket_old    
TABLESPACE ts_stella      
AS SELECT * FROM ticket;

-- Drop the original table
DROP TABLE ticket CASCADE CONSTRAINTS PURGE;

-- Create replacement table from the new records we are keeping
CREATE TABLE ticket 
TABLESPACE ts_stella
AS SELECT * FROM ticket_new;

--Drop the no longer needed keep set of records.
DROP TABLE ticket_new CASCADE CONSTRAINTS PURGE; 

COMMIT;


-- indexes
CREATE INDEX STELLA.XIF12TICKET ON STELLA.TICKET
(AIRLINE_NUM)
LOGGING
TABLESPACE TS_STELLA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          24M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


CREATE INDEX STELLA.XIF2TICKET ON STELLA.TICKET
(PNR_ID)
LOGGING
TABLESPACE TS_STELLA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          25M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


CREATE INDEX STELLA.XIF5TICKET ON STELLA.TICKET
(BSP_TRANS_ID)
LOGGING
TABLESPACE TS_STELLA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          15M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


CREATE UNIQUE INDEX STELLA.TKT_PK ON STELLA.TICKET
(TICKET_NO)
LOGGING
TABLESPACE TS_STELLA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          25M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


CREATE INDEX STELLA.XIF7TICKET ON STELLA.TICKET
(IATA_NO)
LOGGING
TABLESPACE TS_STELLA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          25M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;

-- synonyms
CREATE OR REPLACE SYNONYM BOXIUSER.TICKET FOR STELLA.TICKET;


CREATE OR REPLACE PUBLIC SYNONYM TICKET FOR STELLA.TICKET;

-- constraints
ALTER TABLE STELLA.TICKET ADD (
  CONSTRAINT TKT_PK
  PRIMARY KEY
  (TICKET_NO)
  USING INDEX STELLA.TKT_PK
  ENABLE VALIDATE);

ALTER TABLE STELLA.TICKET ADD (
  CONSTRAINT FK_BSPTRN_TKT 
  FOREIGN KEY (BSP_TRANS_ID) 
  REFERENCES STELLA.BSP_TRANSACTION (BSP_TRANS_ID)
  ENABLE VALIDATE,
  CONSTRAINT IATA_TKT_FK 
  FOREIGN KEY (IATA_NO) 
  REFERENCES STELLA.IATA_DETAILS (IATA_NO)
  ENABLE VALIDATE,
  CONSTRAINT PNR_TKT_FK 
  FOREIGN KEY (PNR_ID) 
  REFERENCES STELLA.PNR (PNR_ID)
  ENABLE VALIDATE,
  CONSTRAINT TKT_AIRL_FK 
  FOREIGN KEY (AIRLINE_NUM) 
  REFERENCES STELLA.AIRLINE (AIRLINE_NUM)
  ENABLE VALIDATE,
  CONSTRAINT TKT_DOCTYP_FK 
  FOREIGN KEY (DOC_TYPE_CODE) 
  REFERENCES STELLA.DOC_TYPE (DOC_TYPE_CODE)
  ENABLE VALIDATE,
  CONSTRAINT TKT_SRC_FK 
  FOREIGN KEY (SOURCE_IND) 
  REFERENCES STELLA.SOURCE (SOURCE_IND)
  ENABLE VALIDATE,
  CONSTRAINT TKT_TKTAGT_FK 
  FOREIGN KEY (TICKETING_AGENT_INITIALS) 
  REFERENCES STELLA.TICKETING_AGENT (TICKETING_AGENT_INITIALS)
  ENABLE VALIDATE);
  
  
--grants
GRANT SELECT ON STELLA.TICKET TO BO_ROLE;

GRANT SELECT ON STELLA.TICKET TO PUBLIC;

GRANT DELETE, INSERT, SELECT, UPDATE ON STELLA.TICKET TO STELLAINTRANET;

------- Ticket --------------







