import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.io.*;

 
/**
* The MySQLDatabase program is used to connect or close the database
* and receive the data from the database by using methods.
*
* @author  Liam Bewley, Edward Riley, Trent Jacobson, Matthew Oelbaum, and Sayed Mobin 
* @version 1.0
* @since   2020-04-09 
*/

public class MySQLDatabase {

   // Two attributes are created but they are constant
   private static String EXCEPTIONMESSAGE = "Failure to perform operation";
   private static String SQLEXCEPTIONMESSAGE = "SQL Engine Failure";

   // All attributes are created
   private Connection connection; // Database Connection Object
   private boolean inTrans; // Whether or not we are currently in a transaction

   public String driver = "com.mysql.jdbc.Driver";
   public String username = "root";
   public String password = "students"; // Needs to be changed to your password 
   public String dbServer = "localhost";
   public String dbName = "CSM";

   public MySQLDatabase() {}

  /**
    * 
    * @para username is the user's username
    * @para password is the user's password
    * @para dbServer is a database's server
    * @para dbName is a database's name
    *
    * @see #username
    * @see #password
    * @see #dbServer
    * @see #dbName
    *
    */
    
   public MySQLDatabase(String username, String password, String dbServer, String dbName) {
      this.username = username;
      this.password = password;
      this.dbServer = dbServer;
      this.dbName = dbName;
   
      inTrans = false;
   }

   /**
    *
    * Para are not used
    *
    * @return Connection
    *
    */
    
   public Connection getConnection() {
      return this.connection;
   }

   /**
    * Open connection to database
    *
    * @return boolean If connection was successful or not
    * @throws DLException to find any error
    *
    */
    
   public boolean connect() throws DLException {
   
      //if in transaction, CAN NOT connect
      if ( inTrans ) {
         return false;
      }
   
      // jdbc:mysql://<server>/<database>?user=<user>&password=<password>"
      String connectionURI = "jdbc:mysql://" 
                                + dbServer + ":3306" 
                                + "/" + dbName + "?autoReconnect=true&useSSL=false" 
                                + "&user=" + username 
                                + "&password=" + password;
   
      try {
         // To set the driver
         Class.forName(driver);
         // To get it connected
         connection = DriverManager.getConnection(connectionURI);
      } 
      // To catch the error and show message
      catch(Exception e) {
         throw new DLException(e, e.toString());
      }
      // Show that connection is successful 
      return true; 
   }

   /**
    * 
    * Close database connection
    *
    * @return boolean If connection was closed successfully
    * @throws DLException is to find any errors
    *
    */
   public boolean close() throws DLException {
   
      //if in transaction, CAN NOT close
      if ( inTrans ) {
         return false;
      }
   
      try {
         // The connection is closed
         connection.close();
      } 
      // To catch the error and show message
      catch (Exception e) {
         throw new DLException(e, e.toString());
      }
      // Shows that connection is closed successfully
      return true; 
      }
   
   
   
   /**
   *
   *
   * Database Connection Status
   * @throws DLException is to find any errors
   *
   */
   public static boolean main() throws DLException {
      
      // Instantiates this database
      MySQLDatabase MySQL = new MySQLDatabase(); 
      
      // Opens both databases below but prints some information about drivers and others.
      System.out.println( "\n");
      System.out.println("MySQLDatabase Connection: " + MySQL.connect() + " \nDriver Loaded: " + MySQL.driver + "\nConnecting to the database: " + MySQL.dbName);  
      System.out.println( "\n");
      
      // return false
      return false;
   } 
   

   /**
    *
    * Perform a SELECT operation on the database
    *
    * @param query SQL string containing a query
    * @return ArrayList<ArrayList<String>> 2D ArrayList containing the query results
    * @throws DLException is to find any errors
    *
    */
   public ArrayList<ArrayList<String>> getData(String query) throws DLException {
   
      // To set up the array named result
      ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
      // To set up the ResultSet named rs
      ResultSet rs;
   
   
      try {
         // To set up the statement with connection for query
         Statement statement = connection.createStatement();
         rs = statement.executeQuery(query);
         ResultSetMetaData rsmd = rs.getMetaData();
         int numCols = rsmd.getColumnCount();
      
         // While there are more rows, add row to result
         while(rs.next()) {
            ArrayList<String> row = new ArrayList<String>();
         
            // Process row
            for(int i = 1; i <= numCols; i++) {
               String res = rs.getString(i);
               row.add(res);
            }
            // Added row to the result
            result.add(row);
         }
      
      } 
      // To catch the error and show message
      catch (Exception e) {
         throw new DLException(e, EXCEPTIONMESSAGE);
      }
      // return
      return result;
   
   }

   /**
    *
    * Perform a SELECT operation on the database
    *
    * @param query SQL string containing a query
    * @param includeHeader Whether or not to include metadata
    * @return ArrayList<ArrayList<String>> 2D ArrayList containing the query results
    * @throws DLException is to find any errors
    *
    */
   public ArrayList<ArrayList<String>> getData(String query, boolean includeHeader) throws DLException{
      //use default method for no header
      if ( !includeHeader ) {
         return getData(query);
      }
      
      // Set up the array named result
      ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
      // To set up the ResultSet named rs
      ResultSet rs;
   
      try {
         // Set up the statement with connection 
         Statement statement = connection.createStatement();
         rs = statement.executeQuery(query);
         ResultSetMetaData rsmd = rs.getMetaData();
         int numCols = rsmd.getColumnCount();
      
         // First row is field names
         ArrayList<String> names = new ArrayList<String>();
         for(int i = 1; i <= numCols; i++) {
            names.add(rsmd.getColumnLabel(i));
         }
         // Added names to the result
         result.add(names);
      
         // Second row is field widths
         ArrayList<String> widths = new ArrayList<String>();
         for(int i = 1; i <= numCols; i++) {
            widths.add(Integer.toString(rsmd.getPrecision(i)));
         }
         // Added widths to the result
         result.add(widths);
      
         // While there are more rows, add row to result
         while(rs.next()) {
            ArrayList<String> row = new ArrayList<String>();
         
            // Process row
            for(int i = 1; i <= numCols; i++) {
               String res = rs.getString(i);
               row.add(res);
            }
            // Added row to the result
            result.add(row);
         }
      
      } 
      // To catch the error and show message
      catch (Exception e) {
         throw new DLException(e, EXCEPTIONMESSAGE);
      }
      // return
      return result;
   
   }

   /**
    *
    * Perform a SELECT operation on the database using a prepared statement
    *
    * @param query SQL string containing a query
    * @param vals List of values to query with
    * @return ArrayList<ArrayList<String>> 2D ArrayList containing the query results
    * @throws DLException to find any errors
    *
    */
   public ArrayList<ArrayList<String>> getData(String query, List<String> vals) throws DLException {
   
      // To set the attributes and array
      ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
      ResultSet rs;
      ResultSetMetaData rsmd;
      int numCols;
   
      try {
         //To set up the prepare statement and insert query to it
         PreparedStatement statement = prepare(query);
      
         // Insert each string value
         for (int i = 0; i < vals.size(); i++) {
            statement.setString(i+1, vals.get(i));
         }
      
         // Perform query
         rs = statement.executeQuery();
         rsmd = rs.getMetaData();
         numCols = rsmd.getColumnCount();
      
         // Add results to ArrayList
         while (rs.next()) {
            ArrayList<String> row = new ArrayList<String>();
         
            // Process row
            for (int i = 1; i <= numCols; i++) {
               row.add(rs.getString(i));
            }
            // Added row to the result
            result.add(row);
         }
      
      } 
      // To catch the error and show message
      catch (SQLException sqle) {
         throw new DLException(sqle, SQLEXCEPTIONMESSAGE);
      } 
      // To catch the error and show message
      catch (Exception e) {
         throw new DLException(e, EXCEPTIONMESSAGE);
      }
      // return
      return result;
   }

   /**
    *
    * To set the data for updatedString 
    *
    * @param updateString SQL String with the DB operation to be performed
    * @return Number of rows of data affected
    * @throws DLException is to find any errors
    *
    */
   public int setData(String updateString) throws DLException {
   
      int numAffected = -1;
   
      try {
         // Set up the statement with connection
         Statement statement = connection.createStatement();
         
         // statement is executed with updatedString and put it in numAffected
         numAffected = statement.executeUpdate(updateString);
      } 
      // To catch the error and show message
      catch (Exception e) {
         throw new DLException(e, EXCEPTIONMESSAGE);
      }
      // return
      return numAffected;
   
   }

   /**
    *
    * To set the data when updated 
    *
    * @param updateString SQL String with the DB operation to be performed
    * @return Number of rows of data affected
    * @throws DLException is to find any errors
    *
    */
   public int setData(String updateString, List<String> vals) throws DLException {
   
      // return the execute statement with updated SQLString and vals (list)
      return executeStatement(updateString, vals);
   
   }

   /**
    *
    * To prepare the statement for SQLString
    *
    * @param SQLString SQL String to turn into a prepared statement
    * @return PreparedStatement of passed SQL String
    * @throws DLException to find any errors
    *
    */
   private PreparedStatement prepare(String SQLString) throws DLException {
   
      try {
         // return the connection with SQLString (Prepare Statement)
         return connection.prepareStatement(SQLString);
      } 
      // To catch the error and show message
      catch (SQLException sqle) {
         throw new DLException(sqle, EXCEPTIONMESSAGE);
      }
   
   }

    /**
    * 
    * To execute the statement with prepared in a for statement loop
    *
    * @param SQLString is a statement
    * @para vals is to list the data
    * @throws DLException to find any errors
    * 
    */
    
   private int executeStatement(String SQLString, List<String> vals) throws DLException {
     
      // Set attribute 
      int numAffected = -1;
   
      try {
         // Set up the Prepared Statement for SQLString
         PreparedStatement statement = prepare(SQLString);
      
         // Insert each string value
         for(int i = 0; i < vals.size(); i++) {
            statement.setString(i+1, vals.get(i));
         }
      
         // To get it updated
         numAffected = statement.executeUpdate();
      
      } 
      // To catch the error and show message
      catch (SQLException sqle) {
         throw new DLException(sqle, SQLEXCEPTIONMESSAGE);
      }
      // To catch the error and show message
      catch (Exception e) {
         throw new DLException(e, EXCEPTIONMESSAGE);
      }
      // return 
      return numAffected;
   }

   /**
    *
    * Start a transaction
    * @throws DLException to find any errors
    *
    */
   public void startTrans() throws DLException {
      
      // set it to be true
      inTrans = true;
   
      try {
         // The connection is set to false when commits
         connection.setAutoCommit(false);
      
      } 
      // To catch the error and show message
      catch (SQLException sqle) {
         throw new DLException(sqle, SQLEXCEPTIONMESSAGE);
      }
   }

   /**
    *
    * End a transaction by committing
    * @throws DLException to find any errors
    *
    */
   public void endTrans() throws DLException {
   
      try {
         // The connection is commited
         connection.commit();
         // The connection is set to true when commits
         connection.setAutoCommit(true);
      } 
      
      // To catch the error and show message
      catch (SQLException sqle) {
         throw new DLException(sqle, SQLEXCEPTIONMESSAGE);
      }
      // Returns false
      inTrans = false;
   }

   /**
    *
    * End a transaction by rolling back
    * @throws DLException to find any errors
    *
    */
   public void rollbackTrans() throws DLException {
   
      try {
         // connection is rollback
         connection.rollback();
         // connection is set to true when commited 
         connection.setAutoCommit(true);
      } 
      // To catch the error and show message
      catch (SQLException sqle) {
         throw new DLException(sqle, SQLEXCEPTIONMESSAGE);
      }
      // Returns false
      inTrans = false;
   }
}
   
   

