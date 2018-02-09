/*
 * Printer.java
 *
 * Created on 03 September 2003, 14:33
 */

package uk.co.firstchoice.framework.utils;

import java.sql.ResultSet;
/**
 *
 * @author  mkonda
 */
public class Printer {
    
    /** Creates a new instance of Printer */
    public Printer() {
    }
    
    public static void print(String s) {
        System.out.println(s);
    }
	public static void print(Object s) {
        System.out.println(s.toString());
    }
    
    public static void print(Exception ex) {
        System.out.println(ex.getMessage ());
    }
    public static void print(ResultSet rs) {
        try{
            while(rs.next()){
            Printer.print(rs.getString(1)); 
            Printer.print(rs.getInt(2)+""); 
            }
        }catch(Exception ex){
                ex.printStackTrace();
            }
    }
}
