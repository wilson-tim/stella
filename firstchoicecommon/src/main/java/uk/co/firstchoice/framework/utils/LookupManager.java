/*
 * LookupManager.java
 *
 * Created on 01 September 2003, 11:25
 */

package uk.co.firstchoice.framework.utils;

import java.io.*;
import java.net.URL;
import java.util.Vector;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.StringTokenizer;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import uk.co.firstchoice.framework.utils.Printer;

/**
 *
 * @author  mkonda
 */
public class LookupManager {
    
    private static Context ctx = null;
    
	private static Object obj = null;
	
	private static Properties _props = null;
    
    public LookupManager() {
    }
    
    public static Object lookup(String jndiName){
        
        try{
            ctx = getInitialContext();
            obj = ctx.lookup(jndiName);
        }catch(NamingException nex){
            Printer.print(" Lookup Exception "+nex.getMessage());
            nex.printStackTrace();
        }
       
       return obj;
    }
    
     public static Context getInitialContext(){
	
	String ctxFactory = "jrun.naming.JRunContextFactory";//ps.getProperty("Context.INITIAL_CONTEXT_FACTORY");
        String ctxUrl  = "cra-devweb2:2003";//ps.getProperty("Context.PROVIDER_URL");
        
        Context ctx = null;
        Properties props = new Properties();
        
        props.put ("Context.INITIAL_CONTEXT_FACTORY", ctxFactory);
        props.put ("Context.PROVIDER_URL", ctxUrl);

        try{
            ctx = new InitialContext(props);
        }catch(NamingException ex){
           Printer.print("Context Exception "+ex.getMessage());      
           ex.printStackTrace();
        }
        return ctx;
      }
    
     public static void loadProperties() {
        _props = new Properties();
        URL url = null;
        String delim = "=";
        String line = null;
        InputStream in = null;
        Vector v = new Vector();
        StringTokenizer tok = null;
        BufferedReader buffIn = null;
        InputStreamReader inputReader = null;
        
        try{
            
            url = ClassLoader.getSystemResource("data.properties");
            _props.load(url.openStream()) ;       

        }catch(NullPointerException ex){
            Printer.print(" Properties file not found !!"+ex.getMessage ());
            ex.printStackTrace ();
        }catch(Exception ex){
            Printer.print(" Exception occured while loading properties file: "+ex.getMessage ());
        }
    }    
}
