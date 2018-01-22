/*
 * Created on 19-Jul-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


/**
 * @author Jrenganathan
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package uk.co.firstchoice.stella.frontend;

import java.util.*;

import javax.naming.*;
import javax.naming.Context;
import javax.naming.directory.*;


public class LdapData {


  DirContext ctx;
  String searchBase = "ou=Security Groups,ou=Groups,ou=UKISD,dc=ad,dc=tuispecialist,dc=com";

  public LdapData () {
	      Hashtable env = new Hashtable();
	      //String searchBase = "ou=Security Groups,ou=Groups,ou=UKISD,dc=ad,dc=tuispecialist,dc=com";
	      String dn = "cn=DWH Tomcat Service Account,ou=Service Accounts,dc=ad,dc=tuispecialist,dc=com";
	    	  //"cn=wpsadmin,o=FCG" ;
	      String url = "ldap://vsvr-dc01.ad.tuispecialist.com:389";



	      env.put(Context.INITIAL_CONTEXT_FACTORY,
	         "com.sun.jndi.ldap.LdapCtxFactory");
	      env.put(Context.PROVIDER_URL,url);

System.out.println(" Context.PROVIDER_URL>  " + url );

   		// "ldap://cra-dom-hub01.firstchoice.co.uk:389");

	      env.put(Context.SECURITY_AUTHENTICATION, "simple");
		  env.put(Context.SECURITY_PRINCIPAL, dn);
		  env.put(Context.SECURITY_CREDENTIALS,"M4ng0M0nk3y");
				  //"wpsadmin");


	      try {
	         ctx = new InitialDirContext(env);
	      } catch (NamingException e) {

			 System.out.println(" System ....." + e.toString());
	         throw new RuntimeException(e);

	      }

  }

	public String getUserGroups(String p_userName,String p_applicationNm) {
	      //LinkedList list = new LinkedList();
	       String searchFilter = "" ;
	       Attributes attributes ;
		   		  Attribute attr ;
		   		  Attribute attr_cn ;
	      String out,outStr = "",full_out;
	      NamingEnumeration results = null;
	      //String searchBase = "ou=UKISD,dc=ad,dc=tuispecialist,dc=com";
    	  int pos1,pos2;

	      try {
	         SearchControls controls = new SearchControls();

	        controls.setSearchScope( SearchControls.SUBTREE_SCOPE);
	       //  controls.setSearchScope( SearchControls.ONELEVEL_SCOPE);

System.out.println(" at 80  in Ldapdata.getUserGroups ");

	  //  searchFilter =  "(member=" +  p_userName + "*)" ;
	   searchFilter =  "(CN=*"+p_applicationNm+"*)" ; // Get all the groups with application name STELLA in it

	   System.out.println(" Search filter " + searchFilter);

	   results = ctx.search(searchBase, searchFilter, controls);

		System.out.println(" at 87 in Ldapdata.getUserGroups ");

	   // Go through each Stella Group
	   while (results.hasMore()) {
		System.out.println(" at 91 in Ldapdata.getUserGroups ");

		    attributes =    ((SearchResult) results.next()).getAttributes();
	   		attr_cn = attributes.get("cn"); //group name
	   		attr = attributes.get("member");  // member names

/*	   		out = (String) attr.get();
	   		if(out.indexOf(p_applicationNm,0) > 0 ) {
				  if (outStr != "") { outStr += ',' ;}
	   		      outStr += out ;

	   		}
*/

       if (attr.size() > 0 ){

		System.out.println(" at 104 in Ldapdata.getUserGroups ");

	   		for (int i = 0 ; i < attr.size(); i++) {

			 System.out.println(" Inside Ldap getusergroups FOR loop at 103");


				full_out = (String) attr.get(i) ;

			   		 // get only first name and last name
	   		   		  pos1 = full_out.indexOf("CN=")+3;
	   		   		  pos2 = full_out.indexOf(",OU");

	   		   		  if (pos1 >= 0 && pos2 >= 0) {
	   					  out = full_out.substring(pos1,pos2);

							System.out.println(" Group : " + attr_cn.get() +  " Member :  "+ out);

//out = (String) attr.get();

if(full_out.indexOf(p_userName,0) > 0 ) {
if (outStr != "") { outStr += ',' ;}
outStr += attr_cn.get() ;
}
				} // if  loop

 }

} // for loop

} // while loop

       System.out.println("Group for  " + p_userName  + " : " +  outStr);

//               System.out.println("Group :   " + outStr);

	      } catch (NameNotFoundException e) {
			  System.out.println(" name not found exeption" + e);
	         // The base context was not found.
	         // Just clean up and exit.
	      } catch (NamingException e) {
			  System.out.println(" naming exeption" + e);
	         throw new RuntimeException(e);
	      } finally {
	         if (results != null) {
	            try {
	               results.close();
	            } catch (Exception e) {
	               // Never mind this.
	            }
	         }
	         if (ctx != null) {
	            try {

	               ctx.close();
	            } catch (Exception e) {
	            	System.out.println("in exception at context close in ldapdata");
	               // Never mind this.
	            }
	         }
	      }
	      System.out.println("return from getusergroups");
	      return outStr;
	   }


public Vector getAppUsers(String p_ApplicationNm) {

    NamingEnumeration res = null;
      //LinkedList list = new LinkedList();
	       String searchFilter = "" ;
	       Attributes attributes ;
		   		  Attribute attr ;
	      String out,outStr = "" ,full_out;
	      Vector allUsers = new Vector();
	      NamingEnumeration results = null;
	      int pos1,pos2;
	      try {

	         SearchControls controls =       new SearchControls();
			 //System.out.println(" after control");

	        controls.setSearchScope( SearchControls.SUBTREE_SCOPE);
	       //  controls.setSearchScope( SearchControls.ONELEVEL_SCOPE);


	     searchFilter = "(&(objectClass=group)(cn=" + p_ApplicationNm + "*))"; // all groups with STELLA in it
	  //searchFilter =  "(cn=" + p_ApplicationNm + "*)";
	 //  searchFilter =  "(objectClass=Person)"; // All users

	   results = ctx.search(searchBase, searchFilter, controls);

	   while (results.hasMore()) {

	   		attributes =    ((SearchResult) results.next()).getAttributes();
	   		//attr = attributes.get("member");
	   		attr = attributes.get("member");

	   		for (int i = 0 ; i < attr.size(); i++) {
	   		 full_out = (String) attr.get(i) ;

	   		full_out = (String) attr.get(i) ;

	   		   		 // get only first name and last name
	   		   		  pos1 = full_out.indexOf("CN=")+3;
	   		   		  pos2 = full_out.indexOf(",OU");

	   		   		  if (pos1 >= 0 && pos2 >= 0) {
	   					  out = full_out.substring(pos1,pos2);

	if(outStr.indexOf(out,0) < 0  ) { // new user name
				  if (outStr != "") { outStr += ',' ;}
				 allUsers.add(out);  // store in vector
				outStr += out;
			}
	   		  	 } // end of if (pos1
		} // end of for loop

	   		      		//outStr += out ;

	   		//}
	    }

          //     System.out.println("Group :   " + outStr);

	      } catch (NameNotFoundException e) {
			  System.out.println(" name not found exeption" + e);
	         // The base context was not found.
	         // Just clean up and exit.
	      } catch (NamingException e) {
			  System.out.println(" naming exeption" + e);
	         throw new RuntimeException(e);
	      } finally {
	         if (results != null) {
	            try {
	               results.close();
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
          // sort i1 before returning
	      java.util.Collections.sort(allUsers);
	      return allUsers ;
	   }

	public static void main(String[] args) {

		LdapData lt =  new LdapData();

//		System.out.println(lt.getAppUsers("STELLA"));
		System.out.println(lt.getUserGroups("Stella Test","STELLA").toString());
//		System.out.println(lt.isUserInRole("Jyoti Renganathan","STELLA_SUPERVISOR"));
	}
}
