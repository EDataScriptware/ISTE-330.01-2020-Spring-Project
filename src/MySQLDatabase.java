import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.io.*;

public class MySQLDatabase  
{

   // Connecting Classes Together
   static main m = new main();
   static Papers papers = new Papers();

   // Initialized Variables
   static String hostMySQL = "jdbc:mysql://localhost/CSM?";
   static String driver = "com.mysql.jdbc.Driver";
   static String user = "root";
   static String password = "students";
   static String databaseType = "MySQL";  
   static private Connection connection = null; 
   
      // Changed Connection to return connection instead of boolean
   public static Connection connect() throws DLException
   {
      try 
      {
         
         // Loads driver
         Class.forName(driver);     
         
         Connection connection = DriverManager.getConnection(hostMySQL, user, password);

         return connection;      
      }
      catch (SQLException e)
      {         
         // It's likely be incorrect user/password.
         System.out.println("Failed to connect: " + e.getMessage());
         throw new DLException(e, e.toString());
      
      }
      catch (Exception e)
      {
      
         System.out.println(e.getMessage());
         throw new DLException(e, e.toString());

      }
   } // end connect method
   
      
   public static boolean close(Connection connection) throws DLException
   {
      try 
      {
       //  System.out.print("\nAttempting to close connection...");

       // Receive ANY connection and attempt to close. 
         connection.close();
         return true;
      
      
       //  System.out.print("Success!");
      }
      catch (Exception e)
      {
         System.out.println("Error! Failed to close! Error Message: " + e.toString());
         throw new DLException(e, e.toString());
      }
   }

}

   
   

