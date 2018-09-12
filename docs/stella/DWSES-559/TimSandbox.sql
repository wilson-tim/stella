CREATE OR REPLACE AND COMPILE JAVA SOURCE NAMED "uk.co.firstchoice.stella/TimSandbox" AS

package uk.co.firstchoice.stella;

import java.sql.*;
import java.io.*;
import oracle.jdbc.*;
import uk.co.firstchoice.util.StringUtils;
import uk.co.firstchoice.util.businessrules.NumberProcessor;

public class TimSandbox
{
  /** Creates a new instance of TimSandbox */
  public TimSandbox() {
  }

  /** Hello world! */    
  public static String world()
  {
    return "Hello world!";
  }

  public static String fpRecord(String recordText)
  {
    String group = "";
    String recBookingRef = "";

    int refPos = recordText.indexOf("NONREFAGT");
    
    if ((refPos > 0) && (recordText.length() > refPos + 9)) {
        if ((recordText.charAt(refPos + 9) >= 'A') &&
                (recordText.charAt(refPos + 9) <= 'Z')) {
    
            // locate ;S or if ;S not found than total length
            int delimPos = recordText.indexOf("/GBP"); // returns -1, if not found

            if (delimPos < 0) {
                delimPos = recordText.indexOf(";S"); // returns -1, if not found
            }

            if (delimPos < 0) {
                delimPos = recordText.length();
            }
    
            String fprefs = recordText.substring(refPos + 9, delimPos).trim();
            group         = fprefs.replaceAll("[0-9]", "");
            recBookingRef = fprefs.replaceAll("[A-Z]", "");
    
        }
    }

    return "Group: " + group + "\n" + "Booking ref: " + recBookingRef;
  }

  public static String rmRecord(String recordText)
  {
    String group = "";
    String recBookingRef = "";
    
    if ((recordText.substring(0, 6).equals("RM ##D")) || (recordText.substring(0, 5).equals("RM #D"))) {
    
        String rmrefs = recordText.replaceAll("RM ##D","").replaceAll("RM #D","").trim();
        if (group.equals("")) {
            group         = rmrefs.replaceAll("[0-9]", "");
        }
        if (recBookingRef.equals("")) {
            recBookingRef = rmrefs.replaceAll("[A-Z]", "");
        }
        
    }
        
    return "Group: " + group + "\n" + "Booking ref: " + recBookingRef;
  }
  
  public static boolean taxAmt(String taxAmt)
  {
        taxAmt = taxAmt.replaceAll("EXEMPT","0.00");
        return NumberProcessor.validateStringAsNumber(taxAmt);
  }  
  
  public static String recComm(String recordText)
  {
    
		String recCommAmt = "0.0";
        String recCommPct = "0.0";
            
        int startPos = StringUtils.searchStringOccur(
                recordText, "*", 2); // start position
                                             // for amount / pct

        if (startPos < 0) {
            // Assuming recordText starts with "FM" so not startPos = 0
            startPos = 1;
        }
        
        int endPos = recordText.indexOf(";"); // returns -1
                                                   // if not
                                                   // found
        if (endPos < 0) {
            endPos = recordText.length();
        } // Here if no semicolon means it is manual commission
          // eg FM*M*8

        if (recordText.substring(endPos - 1, endPos)
                .equalsIgnoreCase("P")) { // "P" commission pct
            recCommPct = recordText.substring(
                    startPos + 1, endPos - 1);
        } else if (recordText.substring(endPos - 1, endPos)
                .equalsIgnoreCase("A")) { // "A" commission amt
            recCommAmt = recordText.substring(
                    startPos + 1, endPos - 1);
        } else { // other
            recCommAmt = recordText.substring(
                    startPos + 1, endPos);
        }
        
        return ("recCommAmt: " + recCommAmt + " recCommPct: " + recCommPct);
        
  }
  
  public static String collectionAmt(String recordText)
  {
    String lineData = recordText;
    String oneLine  = recordText;
    String collectionAmt = "0.00";
   

    if ((lineData.length() > 3) 
            && (oneLine.substring(0, 3).equalsIgnoreCase("FPO"))) {

        int slashPos = oneLine.indexOf("/GBP");  // pos 26
        int endPos   = oneLine.indexOf(";"); // either -1 for not fo

        // The end position of Amount is either up to first
        // semicolon or up to end of line length
        if (slashPos > 0) {
            if (endPos > 0) {
                collectionAmt = oneLine.substring(slashPos + 4, endPos);
            } else {
                collectionAmt = oneLine.substring(slashPos + 4,	oneLine.length()); 
            }
        }
    }
    
    return ("collectionAmt: " + collectionAmt);
    
  }
  
  public static String recExchangeTicketNo(String recordText)
  {
    String recExchangeTicketNo = "";
    String foRecord = recordText.substring(5);
    
    int foLen = foRecord.length();
    if (foLen > 10) {
        int foEndPos = 0;
        while ((foEndPos < foLen) && (recExchangeTicketNo.equals(""))) {
            if ((foRecord.charAt(foEndPos) >= 'A') && (foRecord.charAt(foEndPos) <= 'Z')) {
                recExchangeTicketNo = foRecord.substring(0, foEndPos);
            } else {
                foEndPos = foEndPos + 1;
            }
        }
    }    
    
    return ("recExchangeTicketNo: " + recExchangeTicketNo);
  }
}
;
/


CREATE OR REPLACE FUNCTION TIMHELLOWORLD RETURN VARCHAR2 AS
LANGUAGE JAVA NAME 'uk.co.firstchoice.stella.TimSandbox.world() return java.lang.String'
;
/
CREATE OR REPLACE FUNCTION TIMFPRECORD (recordText VARCHAR2)
RETURN VARCHAR2 AS
LANGUAGE JAVA NAME 'uk.co.firstchoice.stella.TimSandbox.fpRecord(java.lang.String) return java.lang.String'
;
/
CREATE OR REPLACE FUNCTION TIMRMRECORD (recordText VARCHAR2)
RETURN VARCHAR2 AS
LANGUAGE JAVA NAME 'uk.co.firstchoice.stella.TimSandbox.rmRecord(java.lang.String) return java.lang.String'
;
/
CREATE OR REPLACE FUNCTION TIMTAXRECORD (taxAmt VARCHAR2)
RETURN BOOLEAN AS
LANGUAGE JAVA NAME 'uk.co.firstchoice.stella.TimSandbox.taxAmt(java.lang.String) return java.lang.boolean'
;
/
CREATE OR REPLACE FUNCTION TIMRECCOMMRECORD (recordText VARCHAR2)
RETURN VARCHAR2 AS
LANGUAGE JAVA NAME 'uk.co.firstchoice.stella.TimSandbox.recComm(java.lang.String) return java.lang.String'
;
/
CREATE OR REPLACE FUNCTION TIMCOLLAMTRECORD (recordText VARCHAR2)
RETURN VARCHAR2 AS
LANGUAGE JAVA NAME 'uk.co.firstchoice.stella.TimSandbox.collectionAmt(java.lang.String) return java.lang.String'
;
/
CREATE OR REPLACE FUNCTION TIMEXCHTKTRECORD (recordText VARCHAR2)
RETURN VARCHAR2 AS
LANGUAGE JAVA NAME 'uk.co.firstchoice.stella.TimSandbox.recExchangeTicketNo(java.lang.String) return java.lang.String'
;
/

SELECT TIMHELLOWORLD() FROM DUAL;

SELECT TIMFPRECORD('FPNONREFAGTA1234567890;S') FROM DUAL;
SELECT TIMFPRECORD('FPNONREFAGTAB1234567890;S') FROM DUAL;
SELECT TIMFPRECORD('FPNONREFAGTA1234567890') FROM DUAL;
SELECT TIMFPRECORD('FPNONREFAGTAB1234567890') FROM DUAL;
SELECT TIMFPRECORD('FPNONREFAGTA') FROM DUAL;
SELECT TIMFPRECORD('FPNONREFAGTAC') FROM DUAL;
SELECT TIMFPRECORD('FPNONREFAGTA;S') FROM DUAL;
SELECT TIMFPRECORD('FPNONREFAGTAC;S') FROM DUAL;
SELECT TIMFPRECORD('FPNONREFAGT') FROM DUAL;
SELECT TIMFPRECORD('FPNONREFAGT;S') FROM DUAL;
SELECT TIMFPRECORD('FPO/NONREFAGT;S') FROM DUAL;
SELECT TIMFPRECORD('FPO/NONREFAGTA1162404;S5-8;P1-4') FROM DUAL;
SELECT TIMFPRECORD('FPO/NONREFAGTAD1162404;S5-8;P1-4') FROM DUAL;
SELECT TIMFPRECORD('FPNONREFAGTL1189738') FROM DUAL;
SELECT TIMFPRECORD('FPNONREFAGTA1159137/GBP119.91') FROM DUAL;
SELECT TIMFPRECORD('FPNONREF/GBP42.27') FROM DUAL;
SELECT TIMFPRECORD('FPNONREFAGTZ1174620') FROM DUAL;

SELECT TIMRMRECORD('RM ##A1162404') FROM DUAL;
SELECT TIMRMRECORD('RM ##DA1162404') FROM DUAL;
SELECT TIMRMRECORD('RM ##D A1162404') FROM DUAL;
SELECT TIMRMRECORD('RM ##AB1162404') FROM DUAL;
SELECT TIMRMRECORD('RM ##DAB1162404') FROM DUAL;
SELECT TIMRMRECORD('RM ##D AB1162404') FROM DUAL;
SELECT TIMRMRECORD('RM ##D') FROM DUAL;
SELECT TIMRMRECORD('RM ##D ') FROM DUAL;
SELECT TIMRMRECORD('RM #DL1189738') FROM DUAL;
SELECT TIMRMRECORD('RM #DAD1189738') FROM DUAL;

SELECT TIMTAXRECORD('123.456') FROM DUAL;
SELECT TIMTAXRECORD('EXEMPT') FROM DUAL;
SELECT TIMTAXRECORD('NUMBER') FROM DUAL;

SELECT TIMRECCOMMRECORD('FM*F*3.14;S5-8;P1-4') FROM DUAL;
SELECT TIMRECCOMMRECORD('FM*F*3.14X;S5-8;P1-4') FROM DUAL;
SELECT TIMRECCOMMRECORD('FM*F*3.14A;S5-8;P1-4') FROM DUAL;
SELECT TIMRECCOMMRECORD('FM*F*3.14P;S5-8;P1-4') FROM DUAL;
SELECT TIMRECCOMMRECORD('FM12.34X') FROM DUAL;
SELECT TIMRECCOMMRECORD('FM12.34A') FROM DUAL;
SELECT TIMRECCOMMRECORD('FM12.34P') FROM DUAL;
SELECT TIMRECCOMMRECORD('FM12.34') FROM DUAL;

SELECT TIMCOLLAMTRECORD('FPO/NONREFAGTX1151490+/NONREF/GBP6.50;S4-5;P2') FROM DUAL;
SELECT TIMCOLLAMTRECORD('FPO/NONREFAGTX1161145/+/NONREFAGT/GBP228.30') FROM DUAL;
SELECT TIMCOLLAMTRECORD('FPO/NONREFAGT;S') FROM DUAL;
SELECT TIMCOLLAMTRECORD('FPO/NONREFAGTA1162404;S5-8;P1-4') FROM DUAL;
SELECT TIMCOLLAMTRECORD('FPO/NONREFAGTAD1162404;S5-8;P1-4') FROM DUAL;

SELECT TIMEXCHTKTRECORD('FO1252570513764LON15MAY1891278401') FROM DUAL;
SELECT TIMEXCHTKTRECORD('FO125123LON15MAY1891278401') FROM DUAL;
SELECT TIMEXCHTKTRECORD('FO125012345678901234567890LON15MAY1891278401') FROM DUAL;
SELECT TIMEXCHTKTRECORD('FO125012345678901234567890') FROM DUAL;
SELECT TIMEXCHTKTRECORD('125012345678901234567890') FROM DUAL;


--DROP JAVA SOURCE "TimSandbox";
--DROP FUNCTION "STELLA"."TIMHELLOWORLD";
--DROP FUNCTION "STELLA"."TIMFPRECORD";
--DROP FUNCTION "STELLA"."TIMRMRECORD";



