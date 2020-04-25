import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.io.*;

/**
* The main program is used to request the user to choose Register or Login 
*
* @author  Edward Riley, Trent Jacobson, Liam Bewley, Matthew Oelbaum, and Sayed Mobin 
* @version 1.0
* @since   2020-03-18 
*/

public class main {
 
   
   
   /**
    * 
    * The Program is set that will request the user to 
    * choose [R] Register or [L] Login to sign in their
    * account.
    * 
    * @param are not used
    * @throw DLException to find errors
    * 
    */
    
   public static void main(String[] args) throws DLException {  
   

     
      
      try {
         
        
         // Connect to the papers java file 
         // Connect user java file to the MySQLDatabase
         User user = new User();
         User user2 = new User();
         User user3 = new User("7641"); //Change to any id you want to overied
       
      user.setUser("Mark", "Blackz", "jacko1234@gmail.com", "sigma1234", "none"); // if user has an id it will update its info, otherwise it will register a new user

      user2.login("jacko1234@gmail.com", "sigma1234"); //Login with email or password, returns password token

      user.getPapers(700); //returns arraylist of all papers the specfied user authored
      
      user3.setUser("Jimbo", "Hobbs", "jimjim343@gmail.com", "cooldude34", "none"); // Updating new info in SetUser

      }
      
      // When program is not working, it will display error and then close the connection
      catch (Exception e) {


         throw new DLException(e, e.getMessage());

      }
   }  
}
