package uk.co.firstchoice.common.base.datetime;
import java.sql.*;
import java.util.*;

public class GetJavaTimeZone {
  public static int GetTZOffset (String tzName, String eraName, int year, int month, int dayNum, String dayName, int timeInMsecs)
                    throws SQLException                             
   {
    /* ***************************************************************************** *

       Returns the offset to GMT in milliseconds for the time zone for the date. 

       History
       -------

                                                                            
       Date      By          Description                                         Version
       --------  ----------- --------------------------------------------------- -------
       29/11/05  A.James     Initial version.                                      001
       14/02/06  A.James     Moved to the datetime package instead of a class on   002
                             its own.
                             
    * ***************************************************************************** */
    System.out.println("Parameters: ");
    System.out.println("Time zone name: " + tzName);
    System.out.println("Era name: " + eraName);
    System.out.println("Year: " + year);
    System.out.println("Month: " + month);
    System.out.println("Day number: " + dayNum);
    System.out.println("Day name: " + dayName);
    System.out.println("Time in milliseconds: " + timeInMsecs);

    int dayNameInt = -9999;
    int era = -9999;
    String trimDayName = dayName.trim().toUpperCase();
    String trimEraName = eraName.trim();

    TimeZone tz = TimeZone.getTimeZone(tzName);
    /*
      See if time zone id returned is GMT. This indicates the time zone isn't recognised and so
      the default of GMT has been used.
      
      N.B. cater for the time zone being passed actually being GMT which is valid.
    */
    String tzID = tz.getID();
    System.out.println("Time zone Id.: " + tzID);
    if (tzID.equals("GMT")&!tzName.equals("GMT")) {
            System.out.println("Throwing Time Zone cannot be found exception...");
            throw new SQLException("Time Zone " + tzName + " cannot be found...");
    }

    System.out.println("Time Zone has been found...");
    // int offset = tz.getOffset(java.util.GregorianCalendar.AD,2005,11,22,java.util.Calendar.WEDNESDAY,1000);
        
    if (trimEraName.equals("AD")) {
        era = GregorianCalendar.AD;
    } else if (trimEraName.equals("BC")) {
        era = GregorianCalendar.BC;
    }
    
    if (trimDayName.equals("MONDAY")) {
        dayNameInt = Calendar.MONDAY;
    } else if (trimDayName.equals("TUESDAY")) {
        dayNameInt = java.util.Calendar.TUESDAY;
    } else if (trimDayName.equals("WEDNESDAY")) {
        dayNameInt = java.util.Calendar.WEDNESDAY;
    } else if (trimDayName.equals("THURSDAY")) {
        dayNameInt = java.util.Calendar.THURSDAY;
    } else if (trimDayName.equals("FRIDAY")) {
        dayNameInt = java.util.Calendar.FRIDAY;
    } else if (trimDayName.equals("SATURDAY")) {
        dayNameInt = java.util.Calendar.SATURDAY;
    } else if (trimDayName.equals("SUNDAY")) {
        dayNameInt = java.util.Calendar.SUNDAY;
    }

    // month is 0 - based, e.g. January = 0, december = 11; so subtract 1.
    //int offset = tz.getOffset(era,year,month -1,dayNum,dayNameInt,timeInMsecs);
    int offset = tz.getOffset(era,year,month - 1,dayNum,dayNameInt,timeInMsecs);

    System.out.println("Time zone offset.: " + offset);

    return offset;
  }
}
