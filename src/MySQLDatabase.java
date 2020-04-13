import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.io.*;

/**
 *  MySQL Database Connections
 *
 *  @author Liam Bewley
 *  @version 2020.04.09
 */
public class MySQLDatabase {

   //constant
   private static String EXCEPTIONMESSAGE = "Failure to perform operation";
   private static String SQLEXCEPTIONMESSAGE = "SQL Engine Failure";

   // attributes
   private Connection connection; //Database Connection Object
   private boolean inTrans; //whether or not we are currently in a transaction

   private String driver = "com.mysql.cj.jdbc.Driver";
   private String username = "root";
   private String password = "Bella1422"; //need to be changed to your password
   private String dbServer = "localhost";
   private String dbName = "CSM";

   public MySQLDatabase() {

   }

   public MySQLDatabase(String username, String password, String dbServer, String dbName) {
      this.username = username;
      this.password = password;
      this.dbServer = dbServer;
      this.dbName = dbName;

      inTrans = false;
   }

   /**
    *
    * @return Connection
    */
   public Connection getConnection() {
      return this.connection;
   }

   /**
    * Open connection to database
    *
    * @return boolean If connection was successful or not
    */
   public boolean connect() throws DLException {

      //if in transaction, CAN NOT connect
      if ( inTrans ) {
         return false;
      }

      //jdbc:mysql://<server>/<database>?user=<user>&password=<password>"
      String connectionURI =
              "jdbc:mysql://"
                      + dbServer + ":3306"
                      + "/" + dbName
                      + "?user=" + username
                      + "&password=" + password
                      + "&serverTimezone=UTC";

      try {
         Class.forName(driver);
         connection = DriverManager.getConnection(connectionURI);
      } catch(Exception e) {
         System.out.println(e);
         throw new DLException(e, e.toString());
      }

      return true; // connection successful
   }

   /**
    * Close database connection
    *
    * @return boolean If connection was closed successfully
    */
   public boolean close() throws DLException {

      //if in transaction, CAN NOT close
      if ( inTrans ) {
         return false;
      }

      try {
         connection.close();
      } catch (Exception e) {
         throw new DLException(e, e.toString());
      }
      return true; //closed successfully
   }

   /**
    * Perform a SELECT operation on the database
    *
    * @param query SQL string containing a query
    * @return ArrayList<ArrayList<String>> 2D ArrayList containing the query results
    */
   public ArrayList<ArrayList<String>> getData(String query) throws DLException {

      ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
      ResultSet rs;


      try {
         Statement statement = connection.createStatement();
         rs = statement.executeQuery(query);
         ResultSetMetaData rsmd = rs.getMetaData();
         int numCols = rsmd.getColumnCount();

         //while there are more rows, add row to result
         while(rs.next()) {
            ArrayList<String> row = new ArrayList<String>();

            //process row
            for(int i = 1; i <= numCols; i++) {
               String res = rs.getString(i);
               row.add(res);
            }

            result.add(row);
         }

      } catch (Exception e) {
         throw new DLException(e, EXCEPTIONMESSAGE);
      }

      return result;

   }

   /**
    * Perform a SELECT operation on the database
    *
    * @param query SQL string containing a query
    * @param includeHeader Whether or not to include metadata
    * @return ArrayList<ArrayList<String>> 2D ArrayList containing the query results
    */
   public ArrayList<ArrayList<String>> getData(String query, boolean includeHeader) throws DLException{
      //use default method for no header
      if ( !includeHeader ) {
         return getData(query);
      }

      ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
      ResultSet rs;

      try {
         Statement statement = connection.createStatement();
         rs = statement.executeQuery(query);
         ResultSetMetaData rsmd = rs.getMetaData();
         int numCols = rsmd.getColumnCount();

         //first row is field names
         ArrayList<String> names = new ArrayList<String>();
         for(int i = 1; i <= numCols; i++) {
            names.add(rsmd.getColumnLabel(i));
         }
         result.add(names);

         //second row is field widths
         ArrayList<String> widths = new ArrayList<String>();
         for(int i = 1; i <= numCols; i++) {
            widths.add(Integer.toString(rsmd.getPrecision(i)));
         }
         result.add(widths);

         //while there are more rows, add row to result
         while(rs.next()) {
            ArrayList<String> row = new ArrayList<String>();

            //process row
            for(int i = 1; i <= numCols; i++) {
               String res = rs.getString(i);
               row.add(res);
            }

            result.add(row);
         }

      } catch (Exception e) {
         throw new DLException(e, EXCEPTIONMESSAGE);
      }

      return result;

   }

   /**
    * Perform a SELECT operation on the database using a prepared statement
    *
    * @param query SQL string containing a query
    * @param vals List of values to query with
    * @return ArrayList<ArrayList<String>> 2D ArrayList containing the query results
    */
   public ArrayList<ArrayList<String>> getData(String query, List<String> vals) throws DLException {

      ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
      ResultSet rs;
      ResultSetMetaData rsmd;
      int numCols;

      try {
         //prepare statement
         PreparedStatement statement = prepare(query);

         //insert each string value
         for (int i = 0; i < vals.size(); i++) {
            statement.setString(i+1, vals.get(i));
         }

         //perform query
         rs = statement.executeQuery();
         rsmd = rs.getMetaData();
         numCols = rsmd.getColumnCount();

         //add results to ArrayList
         while (rs.next()) {
            ArrayList<String> row = new ArrayList<String>();

            //process row
            for (int i = 1; i <= numCols; i++) {
               row.add(rs.getString(i));
            }

            result.add(row);
         }

      } catch (SQLException sqle) {
         throw new DLException(sqle, SQLEXCEPTIONMESSAGE);
      } catch (Exception e) {
         throw new DLException(e, EXCEPTIONMESSAGE);
      }

      return result;
   }

   /**
    *
    * @param updateString SQL String with the DB operation to be performed
    * @return Number of rows of data affected
    */
   public int setData(String updateString) throws DLException {

      int numAffected = -1;

      try {
         Statement statement = connection.createStatement();
         numAffected = statement.executeUpdate(updateString);
      } catch (Exception e) {
         throw new DLException(e, EXCEPTIONMESSAGE);
      }

      return numAffected;

   }

   /**
    *
    * @param updateString SQL String with the DB operation to be performed
    * @return Number of rows of data affected
    */
   public int setData(String updateString, List<String> vals) throws DLException {

      return executeStatement(updateString, vals);

   }

   /**
    * @param SQLString SQL String to turn into a prepared statement
    * @return PreparedStatement of passed SQL String
    */
   private PreparedStatement prepare(String SQLString) throws DLException {

      try {
         return connection.prepareStatement(SQLString);
      } catch (SQLException sqle) {
         throw new DLException(sqle, EXCEPTIONMESSAGE);
      }

   }

   /**
    *
    */
   private int executeStatement(String SQLString, List<String> vals) throws DLException {
      int numAffected = -1;

      try {
         PreparedStatement statement = prepare(SQLString);

         //insert each string value
         for(int i = 0; i < vals.size(); i++) {
            statement.setString(i+1, vals.get(i));
         }

         numAffected = statement.executeUpdate();

      } catch (SQLException sqle) {
         throw new DLException(sqle, SQLEXCEPTIONMESSAGE);
      }catch (Exception e) {
         throw new DLException(e, EXCEPTIONMESSAGE);
      }

      return numAffected;
   }

   /**
    * Start a transaction
    */
   public void startTrans() throws DLException {
      inTrans = true;

      try {

         connection.setAutoCommit(false);

      } catch (SQLException sqle) {
         throw new DLException(sqle, SQLEXCEPTIONMESSAGE);
      }
   }

   /**
    * End a transaction by committing
    */
   public void endTrans() throws DLException {

      try {
         connection.commit();
         connection.setAutoCommit(true);
      } catch (SQLException sqle) {
         throw new DLException(sqle, SQLEXCEPTIONMESSAGE);
      }

      inTrans = false;
   }

   /**
    * End a transaction by rolling back
    */
   public void rollbackTrans() throws DLException {

      try {
         connection.rollback();
         connection.setAutoCommit(true);
      } catch (SQLException sqle) {
         throw new DLException(sqle, SQLEXCEPTIONMESSAGE);
      }

      inTrans = false;

   }


}
   
   

