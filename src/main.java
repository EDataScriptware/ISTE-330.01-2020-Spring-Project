import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.io.*;

public class main
{
 
   
   public static void main(String[] args) throws DLException
   {  
      MySQLDatabase msd = new MySQLDatabase();
   
      char choice;
      try 
      {
         
         msd.connect(); 
         Papers papers = new Papers();
         User user = new User();
      
         System.out.println("[R]egister or [L]ogin?");
         Scanner scnLoginOrRegister = new Scanner(System.in);
         choice = scnLoginOrRegister.next().charAt(0);
         System.out.println(choice);
         
         if (choice == 'R')
         {
            user.register();
         }
         else if (choice == 'L')
         {
            user.login();
         }
         else 
         {
            System.out.println("Error.");
            System.exit(0);
         }
        
         msd.close();
      }
      catch (Exception e)
      {
      
         msd.connect();
         msd.close();

         throw new DLException(e, e.getMessage());

      }
    
    
   
   }
   
   
}