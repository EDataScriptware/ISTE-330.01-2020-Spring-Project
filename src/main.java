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
   
   
      // Instantiates this database
      MySQLDatabase msd = new MySQLDatabase();
      
      
      // Set it to compare that choice is made
      char choice;
      
      try {
         
         // To get data initialization connected
         msd.connect(); 
         // Connect to the papers java file 
         Papers papers = new Papers();
         // Connect user java file to the MySQLDatabase
         User user = new User(msd.getConnection());
         
         // Display the message to see if database is able to connect (true or false), what driver is connected, and what database is connected
         System.out.println("\n");  
         System.out.println("MySQLDatabase Connection: " + msd.connect() + " \nDriver Loaded: " + msd.driver + "\nConnecting to the database: " + msd.dbName);
         System.out.println("\n");  

      
         // Requests the user to pick one choice (Register or Login) 
         System.out.println("[R]egister or [L]ogin?");
         Scanner scnLoginOrRegister = new Scanner(System.in);
         choice = scnLoginOrRegister.next().charAt(0);
         System.out.println(choice);
         
         // If R is being selected, it will be processed with next steps
         if (choice == 'R') {
            user.register();
         }
         
         // if L is being selected, it will be processed with next steps
         else if (choice == 'L') {
            user.login();
         }
         
         // If program is not working, it will display error and then exit
         else {
            System.out.println("Error.");
            System.exit(0);
         }
         
         // Data initialization is closed
         msd.close();
      }
      
      // When program is not working, it will display error and then close the connection
      catch (Exception e) {

         msd.connect();
         msd.close();

         throw new DLException(e, e.getMessage());

      }
   }  
}
