/*
 * Created on 07-Jul-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
//package uk.co.firstchoice.stella;

 package uk.co.firstchoice.stella.frontend;
 
 import java.util.*;
 import javax.naming.*;
 import javax.naming.Context;
 import javax.naming.directory.*;
 

 

public class TraditionalPersonDaoImpl  {
	
	public TraditionalPersonDaoImpl() {
	}
	
   public List getAllPersonNames() {
      Hashtable env = new Hashtable();
      env.put(Context.INITIAL_CONTEXT_FACTORY,
         "com.sun.jndi.ldap.LdapCtxFactory");
      env.put(Context.PROVIDER_URL,"" + "ldap://vsvr-dc01.ad.tuispecialist.com:389");
      	//	"ldap://cra-dom-hub01.firstchoice.co.uk/dc=jayway,dc=se");
       //  "ldap://localhost:389/dc=jayway,dc=se");
      
      /////
      String dn = "cn=DWH Tomcat Service Account,ou=Service Accounts,dc=ad,dc=tuispecialist,dc=com";
      env.put(Context.SECURITY_AUTHENTICATION, "simple");
	  env.put(Context.SECURITY_PRINCIPAL, dn);
	  env.put(Context.SECURITY_CREDENTIALS,"M4ng0M0nk3y");
	  
     
      
          
      /////

      DirContext ctx;
      try {
         ctx = new InitialDirContext(env);
      } catch (NamingException e) {
         throw new RuntimeException(e);
      }

      LinkedList list = new LinkedList();
      NamingEnumeration results = null;
     
      try {
         SearchControls controls =
            new SearchControls();
         controls.setSearchScope(
            SearchControls.SUBTREE_SCOPE);
         String searchBase = "ou=UKISD,dc=ad,dc=tuispecialist,dc=com";
         results = ctx.search(
        		 searchBase, "(company=*)", controls);
     
         System.out.println("out Side While ::::::: " );
     
         while (results.hasMore()) {
            SearchResult searchResult =
               (SearchResult) results.next();
            Attributes attributes =
               searchResult.getAttributes();
            Attribute attr = attributes.get("cn");
            String cn = (String) attr.get();
            
          //  System.out.println("CN  is ::::::::::::: " + cn);
            
            
            list.add(cn);
         }
      } catch (NameNotFoundException e) {
         // The base context was not found.
         // Just clean up and exit.
      } catch (NamingException e) {
         throw new RuntimeException(e);
      } finally {
         if (results != null) {
            try {
              ctx.close();
            } catch (Exception e) {
               // Never mind this.
            }
         }
         if (ctx != null) {
            try {
               ctx.close();
            } catch (Exception e) {
               // Never mind this.
            }
         }
      }
      return list;
   }
}