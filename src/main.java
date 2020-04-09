import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.io.*;

public class main
{

   public static void main(String[] args) throws DLException
   {  
     try 
     {
      MySQLDatabase msd = new MySQLDatabase();
      Papers papers = new Papers();
     
      
     msd.connect();
      
        
      msd.close();
    }
    catch (Exception e)
    {
    
    throw new DLException(e, e.getMessage());
    }
   }
   
   
}