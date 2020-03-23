/* 
   Name: Edward Riley
   Professor: Stephen Zilora
   Course: Database Connectivity and Access
   Date: March 18, 2020
*/

import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.io.*;

public class DLException extends Exception 
{
   Exception exception = null;
   String message = null;
   String output = null;
   File file = new File("ErrorLog.txt");
   BufferedWriter bw = null;
   PrintWriter pw = null;

   
   public DLException(Exception _exception)
   {
      exception = _exception;
      writeLog();
   }
   
   public DLException(Exception _exception, String _message)
   {
      exception = _exception;
      message = _message;
      writeLog();
   }
   
 // writeLog method
   public void writeLog()
   {  
      // Try and catch statment   
      try
      {
         if(!file.exists())
         {
            file.createNewFile();
         }
         
         FileWriter fw = new FileWriter(file, true);
         bw = new BufferedWriter(fw);
         pw = new PrintWriter(bw);
         if (exception.getCause() == null)
         {
            pw.println("Reason: Unknown - please refer to Cause below." + "\nCause: " + message + "\n");
         }
         else 
         {
            pw.println("Reason: " + exception.getCause() + "\nCause: " + message + "\n");
         }
         pw.flush();
         pw.close();
      }
      catch(Exception e)
      {
         System.out.println("ERROR: " + e.toString());
         System.exit(-1);
      }
   }

   
   
   
   
   
}