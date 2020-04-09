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
     
      
     Connection connect = msd.connect();    
      
        
      msd.close(connect);      
    }
    catch (Exception e)
    {
    
    throw new DLException(e, e.getMessage());
    }
   }
   
   
}