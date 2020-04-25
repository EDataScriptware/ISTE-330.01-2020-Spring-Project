import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.io.*;
import java.util.Date;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.net.*;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.math.BigInteger;  
import java.nio.charset.StandardCharsets; 
import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException; 

/**
*
* The User programs allows the user to register or login with 
* their account
*
* @author Matthew Oelbaum, Edward Riley, Trent Jacobson, Liam Bewley, and Sayed Mobin 
* @version 1.0
* @since   2020-04-08 
*/


   /**
    *
    * To set up the program to register or login for the user
    *
    * No parameters are used
    *
    */
public class User {

   // All attributes are created 
   String userID;
   String lastName;
   String firstName;
   String email;
   String password;
   String verifyPassword;
   String canReview; // '1' for yes / NULL value for no
   Date dateOfExpiration;
   String isAdmin;// '1' for yes / '0' value for no
   String affiliationId;
   String affilationName;
   String resetPasswordToken;
   
   MySQLDatabase db = new MySQLDatabase();
  
   //getters and setters
   public String getUserID() {
      return userID;
   }

   public void setUserID(String userID) {
      this.userID = userID;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getVerifyPassword() {
      return verifyPassword;
   }

   public void setVerifyPassword(String verifyPassword) {
      this.verifyPassword = verifyPassword;
   }

   public String getCanReview() {
      return canReview;
   }

   public void setCanReview(String canReview) {
      this.canReview = canReview;
   }

   public Date getDateOfExpiration() {
      return dateOfExpiration;
   }

   public void setDateOfExpiration(Date dateOfExpiration) {
      this.dateOfExpiration = dateOfExpiration;
   }

   public String getIsAdmin() {
      return isAdmin;
   }

   public void setIsAdmin(String isAdmin) {
      this.isAdmin = isAdmin;
   }

   public String getAffiliationId() {
      return affiliationId;
   }

   public void setAffiliationId(String affiliationId) {
      this.affiliationId = affiliationId;
   }

   public String getAffilationName() {
      return affilationName;
   }

   public void setAffilationName(String affilationName) {
      this.affilationName = affilationName;
   }

   public String getResetPasswordToken() {
      return resetPasswordToken;
   }

   public void setResetPasswordToken(String resetPasswordToken) {
      this.resetPasswordToken = resetPasswordToken;
   }



   public User()
   {
   
    /**
    * 
    * Setting up the attributes to prepare for the connection and user's data information
    *
    * @param conn is used to connect to the database
    * @see #connection
    * @see #userID
    * @see #lastName
    * @see #firstName
    * @see #email
    * @see #password
    * @see #verifyPassword
    * @see #canReview
    * @see #dateofExpiration
    * @see #affiliationId
    * 
    */   
      // All attributes are created 

      userID = null;
      lastName = null;
      firstName = null;
      email = null;
      password = null;
      verifyPassword = null;
      canReview = null; // '1' for yes / NULL value for no
      dateOfExpiration = null;
      affiliationId = null; // '1' for yes / '0' value for no
      resetPasswordToken = null;


   }

   public User(String userID) {
      this.userID = userID;

      connection = null;
      lastName = null;
      firstName = null;
      email = null;
      password = null;
      verifyPassword = null;
      canReview = null; // '1' for yes / NULL value for no
      dateOfExpiration = null;
      affiliationId = null; // '1' for yes / '0' value for no
      resetPasswordToken = null;
}

private MySQLDatabase  connect() throws DLException {
   MySQLDatabase db = new MySQLDatabase();
   db.connect();
   connection = db.getConnection();
   return db;
}
/**
    *
    * This is used to set up the affilation for each user
    *
    * @para Affilation is the user's unique ID number
    *
    */
    public String convertAffilatiionID(String Affilation) throws DLException{
   
      // Set up the string named idTemp
      String idTemp = 0 + "";
       // Set up the array named tempList
       ArrayList<String> tempList = new ArrayList<String>();
       // Added Affilation to the tempList
       tempList.add(Affilation);
       // Set up the second array named results to get the data
       ArrayList<Object> results  = getData("SELECT affiliationID FROM _affiliations WHERE affiliationName =?;", 1, tempList);
       // To find the ID number
       if(results.size() > 0){
          idTemp = results.get(0) + "";
          affilationName = Affilation;
       }
       // If the affilation cannot be founded, it will display the message in the output to let the user knows
       else{
          affilationName = "Unknown";
       }

      // return
      return idTemp;
   }



      
      /**
       *
       * @param userID Id for this user object
       */


      /**
       *
       * Update class data to database using userID
       *
       * @throws DLException is to find any errors
       *
       */
      public void fetch(User loggedInUser) throws DLException {

         //check credentials
         if(Integer.parseInt(loggedInUser.getIsAdmin()) == 0) {

            //NOT admin, check if this is them
            if(!getUserID().equals(loggedInUser.getUserID())) {
               //access denied

               return;
            }

          }

         try {
            // Instantiates this database
            db.connect();


            // Set up the array named values and get data
            ArrayList<String> values = new ArrayList<>();
            ArrayList<String> list = db.getData("SELECT * FROM users WHERE userID = " + getUserID()).get(0);

            // To set data to match
            if (list.size() > 2) {

               setLastName(list.get(1));
               setFirstName(list.get(2));
               setEmail(list.get(3));

               setDateOfExpiration(new Date(list.get(5)));
               setIsAdmin(list.get(6));
               setAffiliationId(list.get(7));
               setAffilationName(db.getData("SELECT affiliationName FROM _affiliations WHERE affiliationId = " + getAffiliationId()).get(0).get(0));

            }
            // When there is no data return, display the error
            else {
               throw new DLException("No data returned");
            }

            // Database is closed
            db.close();
         }
         // To catch any errors and show the message
         catch (Exception e) {
            throw new DLException(e, "Requested operation failed");
         }
   }

      /**
       * Non logged in fetch, only able to get names
       *
       * @throws DLException is to find any errors
       */
   public void fetch() throws DLException {
      try {

          // Instantiates this database and then connect it
         MySQLDatabase db = connect();

         // Set up the array named values and get data
         ArrayList<String> values = new ArrayList<>();
         ArrayList<String> list = db.getData("SELECT * FROM users WHERE userID = " + getUserID()).get(0);

         // To set data to match
         if (list.size() > 2) {

            setLastName(list.get(1));
            setFirstName(list.get(2));


         }
         // When there is no data return, display the error
         else {
            throw new DLException("No data returned");
         }

         // Database is closed
         db.close();
      }
      // To catch any errors and show the message
      catch (Exception e) {
         throw new DLException(e, "Requested operation failed");
      }
   }
   
   public ArrayList<Object> getPapers(int uID){
      ArrayList<String> tempList = new ArrayList<String>();
      // Added both email and password to the tempList
      tempList.add(uID + "");
      return getData("Select Papers.title FROM Papers INNER JOIN PaperAuthors ON Papers.paperId = PaperAuthors.paperId WHERE PaperAuthors.userId =? ;", 1, tempList);
  }

   public ArrayList<String> getUser(){
      ArrayList<String> tempList = new ArrayList<String>();
      // Added both email and password to the tempList
      tempList.add(userID);
      tempList.add(firstName);
      tempList.add(lastName);
      tempList.add(email);
      tempList.add(affiliationId);
      tempList.add(isAdmin);
      return tempList;
   }



   public void forgotPassword () {
      System.out.println("---------FORGOT PASSWORD---------");
      System.out.print("Enter your email: ");
      Scanner scnUser = new Scanner(System.in);
      email = scnUser.next();
   
      if (checkEmail(email)) {
         newResetPassword(email, true);
      } else {
         newResetPassword(email, false);
         System.out.println(""+email+ " does not exist: ");
      }
   
   }

   public boolean checkEmail (String email) {
      boolean isEmailFound = false;
      try{
         db.setData("SELECT email FROM users WHERE email= ?");
         // preparedStmt.setString(1, email);
         // ResultSet resultSet = preparedStmt.executeQuery();
      
         //if (resultSet.next()) {
         //   isEmailFound = true;
         //   System.out.println("Row with email found: " +resultSet.getString("email"));
         //}
      
      }
      catch(Exception ex){
         ex.printStackTrace();
         System.out.println("SQLException: " + ex.getMessage());
         System.out.println("SQLException: " + ex);
      }
   
      return isEmailFound;
   }

   public void setUser( String lN,String fN, String em, String pass,  String aff) throws DLException{

      setLastName(lN);
      setFirstName(fN);
      setPassword(pass);
      password = toHexString(getSHA(password)); // HASHED
      setEmail(em);
      // Enter the user's affiliation
      setAffilationName(aff);
      setAffiliationId(convertAffilatiionID(aff));
      
    if(userID == null){

      post();
    }
      else
      put();

      
   } 
   
   /**
    * 
    OUTDATED METHOD - We can remove this
    *
    * A While Loop is running while the user is doing with 
    * the registration of their account
    *
    * No parameters are used
    *
   
   public void register() {
      
      // Set it to be true
      boolean repeatFlag = true;
   
      // A While Loop
      while (repeatFlag == true) {
         System.out.println("---------REGISTER---------");
         
         // Enter the user's first name
         System.out.print("Enter your first name: ");
         Scanner scnUser = new Scanner(System.in);
         firstName = scnUser.next();
       
         // Enter the user's last name
         System.out.print("Enter your last name: ");
         Scanner scnNamel = new Scanner(System.in);
         lastName = scnNamel.next();
       
         // Enter the user's new password
         System.out.print("Enter a new password: ");
         Scanner scnPassword = new Scanner(System.in); 
         password = scnPassword.next();
         password = toHexString(getSHA(password)); // HASHED
         
         // Enter the user's password again to verify
         System.out.print("Verify your new password: ");
         Scanner scnVerifyPassword = new Scanner(System.in); // Note: Hash the password at a later time. 
         verifyPassword = scnVerifyPassword.next();
         verifyPassword = toHexString(getSHA(verifyPassword)); // HASHED
      
         // Enter the user's email
         System.out.print("Enter your email: ");
         Scanner scnEmail = new Scanner(System.in);
         email = scnEmail.next();
      
         // Enter the user's affiliation
         System.out.print("Enter your Affiliation: ");
         Scanner scnAfId = new Scanner(System.in);
        
         affiliationId = convertAffilatiionID(scnAfId.nextLine());
      
         // When everything is finished, it will display the summary of the user's information (Name, Password, Email, and Affilication)
         if (verifyPassword.equals(password)) {
            System.out.println("Name: " + firstName + "\nlastName: " + lastName +   "\nPassword: " + password +"\nEmail: " + email + "\nAffilation: " + affilationName); 
            repeatFlag = false;
            insertUser();
         }
         
         // When password is not right, must enter it again. 
         else {
            System.out.println("ERROR: Password not verified! Please try again.");
            System.out.println("Password: " + password);
            System.out.println("Verfied Password: " + verifyPassword);
         }
      }   
   }  */

   /**
    *
    * This is used to set up the affilation for each user
    *
    * @para Affilation is the user's unique ID number
    *
    */
   
   
  
   /**
    *
    * To insert the User ID
    *
    * No parameters are used
    *
    */
    public int post() throws DLException{
      // Instantiates this database and then connect it
   MySQLDatabase db = connect();

    int newID = Integer.parseInt(db.getData("select MAX(userID) FROM USERS").get(0).get(0) ) + 1;
     setUserID(newID + ""); 
 
   // Set up the array named tempList
   ArrayList<String> tempList = new ArrayList<String>();
   // Added both email and password to the tempList
   tempList.add(userID);
   tempList.add(firstName);
   tempList.add(lastName);
   tempList.add(password);
   tempList.add(email);
   tempList.add(affiliationId);
   System.out.println("adding");
   int r = db.setData(" INSERT INTO USERS (userID , firstName, lastName, pswd, email, affiliationId) VALUES (?,?,?,?,?,?);", tempList);
    // Database is closed

     db.close();
     // Return
      return r;
 
    }
 
    public int put() throws DLException {
      // Instantiates this database and then connect it
      MySQLDatabase db = connect();
         
      // Set up the array named tempList
      ArrayList<String> tempList = new ArrayList<String>();
      // Added both email and password to the tempList
      tempList.add(firstName);
      tempList.add(lastName);
      tempList.add(password);
      tempList.add(email);
      tempList.add(affiliationId);
      tempList.add(userID);
 
      int r = db.setData("UPDATE USERS SET firstName = ? , lastName = ?, pswd = ?, email = ?, affiliationId = ? WHERE userID = ? ", tempList);
 
     // Database is closed
     db.close();
     
     // Return
     return r;
 
 
    }
       
   /**
    * 
    * A While Loop is to find the total fields and list for the query statement
    *
    * @param query is a SQL statement
    * @para totalFeilds is to find the total of all fields
    * @para list to find the data list
    * 
    */

    public ArrayList<Object> getData(String query, int totalFeilds, ArrayList<String> list) {
      
      // Set up the array named reSet
      ArrayList<Object> reSet = new ArrayList<Object>();
      
      try {
         MySQLDatabase db = connect();
         Connection connection = db.getConnection();
         // Set up the prepared statement named stmt for the query
         // PreparedStatement stmt = connection.prepareStatement(query);
         
         // A For Statements starts here
         for(int i = 0; i < list.size(); i++){
            // stmt.setString(i + 1, list.get(i));
         }
         // To execute the query and put it in result
         // ResultSet result = stmt.executeQuery();
         
         // row starts with zero
         int row = 0;
         
         // A While Loop starts here
         
         /*while(result.next()){
         
            for(int i = 0; i < totalFeilds; i++) {
               reSet.add(result.getObject(i + 1));
            }
            row++; // Add row
         }*/
         
         // Database is closed
         db.close();
      }
      // To find any error and display the error message
      catch(Exception ex){
         ex.printStackTrace();
         System.out.println("error " + ex);
      }
      
      // Return
      return reSet;
   }
   
   
      
   /**
    *
    * A While Loop is running while the user is doing with 
    * the login in of their account
    *
    * No parameters are used
    *
    */
    public String login(String em, String pass) {
 
      // Setting email and password strings
       email = em;   
       password = pass;
       password = toHexString(getSHA(password)); // HASHED
       
       // Set up the array named tempList
       ArrayList<String> tempList = new ArrayList<String>();
       // Added both email and password to the tempList
       tempList.add(email);
       tempList.add(password);
       // Set up another array named results 
       ArrayList<Object> results = getData("Select userID, firstName, lastName, affiliationId, canReview, isAdmin FROM USERS WHERE email =?  AND pswd =? ;", 6, tempList);
      
       // A For Statement starts here
       if(results.size() > 0) {
          userID = results.get(0) + "";
          firstName = results.get(1) + "";
          lastName = results.get(2) + "";
          affiliationId = results.get(3) + "";
          canReview = results.get(4) + "";
          isAdmin = results.get(5) + "";     
       }
       
   
    
    // To display the summary of the user's name, lastname, password, email, and affilation
 // System.out.println("Name: " + firstName + "\nlastName: " + lastName +   "\nPassword: " + password +"\nEmail: " + email + "\nAffilation: " + affiliationId); 
 return password;
 }

   public void newResetPassword(String email, boolean exists) {
      String emailMessage;
      String subject = "Password Reset";
      String toEmail = email;
      String fromEmail = "javahelpprogram330@gmail.com";
      String host = "smtp.gmail.com";
      String mailPassword = "student0808";
      final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
   
      Properties properties = System.getProperties();
      properties.setProperty("mail.smtp.host", host);
      properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
      properties.setProperty("mail.smtp.socketFactory.fallback", "false");
      properties.setProperty("mail.smtp.port", "465");
      properties.setProperty("mail.smtp.socketFactory.port", "465");
      properties.put("mail.smtp.auth", "true");
      properties.put("mail.smtp.ssl.enable", "true");
      properties.put("mail.debug", "true");
      properties.put("mail.transport.protocol", "smtp");
      properties.put("mail.smtp.starttls.enable", "smtp");
   
      if (exists) {
         String randomPassword = randomAlphaNumeric(10);
         long passwordExpiryDate = System.currentTimeMillis()+(1900000);
         System.out.println("time" +passwordExpiryDate);
         try
         {
            // create our java preparedstatement using a sql update query
            db.setData(
                    "UPDATE users SET resetPasswordToken = ?, resetPasswordExpires = ? WHERE email = ?");
         
            // set the prepared statement parameters
            // ps.setString(1,randomPassword);
            // ps.setLong(2,passwordExpiryDate);
            // ps.setString(3,email);
         
            // call executeUpdate to execute our sql update statement
            // ps.executeUpdate();
            // ps.close();
         }
         catch (Exception se) //--------CHANGE TO SQLEXCEPTION LATER MAYBE - EDWARD
         {
            // log the exception
            se.printStackTrace();
         }
         emailMessage = "You are receiving this because you or someone else requested the reset of the password for your account. Please copy this token " +randomPassword+ " and use to create a new password for yout account";
      } else {
         emailMessage = "You are receiving this message because you or someone else requested the reset of the password of an account that does not exist. Please register to get an account";
      }
   
   
      Session session = Session.getInstance(properties,
              new javax.mail.Authenticator() {
              
                 protected PasswordAuthentication getPasswordAuthentication() {
                 
                    return new PasswordAuthentication(fromEmail, mailPassword);
                 
                 }
              
              });
      //compose the message
      try {
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);
      
         // Set From: header field of the header.
         message.setFrom(new InternetAddress(fromEmail));
      
         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
      
         // Set Subject: header field
         message.setSubject(subject);
      
         // Now set the actual message
         message.setText(emailMessage);
      
         // Send message
         Transport transport = session.getTransport("smtp");
         transport.connect(host, fromEmail, mailPassword);
         transport.sendMessage(message, message.getAllRecipients());
         transport.close();
         System.out.println("message sent successfully....");
      
      } catch (MessagingException mex) {
         mex.printStackTrace();
      }
   
      if (exists) {
         System.out.println("---------Update Password---------");
         System.out.print("Enter the Token sent to your email: ");
         Scanner scnToken = new Scanner(System.in);
         resetPasswordToken = scnToken.next();
      
         System.out.print("Enter new password: ");
         Scanner scnPassword = new Scanner(System.in);
         password = scnPassword.next();
         password = toHexString(getSHA(password)); // HASHED
      
         try {
            db.setData("SELECT resetPasswordExpires FROM users WHERE email = ? and resetPasswordToken = ?");
            //preparedStmt.setString(1, email);
            //preparedStmt.setString(2, resetPasswordToken);
            //ResultSet resultSet = preparedStmt.executeQuery();
         
           /* if (resultSet.next()) {
               long passwordExpiresTime = resultSet.getLong("resetPasswordExpires");
               long currentTime = System.currentTimeMillis();
               if (passwordExpiresTime > currentTime) {
                  try {
                     // create our java preparedstatement using a sql update query
                     PreparedStatement ps = connection.prepareStatement(
                             "UPDATE users SET pswd = ? WHERE email = ? and resetPasswordToken = ?");
                  
                     // set the prepared statement parameters
                     ps.setString(1,password);
                     ps.setString(2, email);
                     ps.setString(3,resetPasswordToken);
                  
                     // call executeUpdate to execute our sql update statement
                     ps.executeUpdate();
                     ps.close();
                     System.out.println("New Password Created, Please use new password to login");
                  } catch (SQLException se) {
                     // log the exception
                     se.printStackTrace();
                     System.out.println("update error: " +se);
                  }
               } else {
                  System.out.println("Reset Password Token Has Expired! Please reset password again: ");
               }
            }
         
            preparedStmt.close();
         */
         }
         catch (Exception se) //--------CHANGE TO SQLEXCEPTION LATER MAYBE - EDWARD
         {            // log the exception
            se.printStackTrace();
         }
      }
   }


   /**
    * 
    * This is used to set up the email formats and reset the password
    *
    * 
    */
   public void resetPassword(){
   
      // Set the String named fromEmail
      String fromEmail = "Javahelpprogram330@gmail.com";
   
      // Set the String name toEmail within the email 
      String toEmail = email;
   
      // Assuming you are sending email from through gmails smtp
      String host = "smtp.gmail.com";
   
      // Get system properties
      Properties properties = System.getProperties();
   
      // Setup mail server
      properties.put("mail.smtp.host", host);
      properties.put("mail.smtp.port", "465");
      properties.put("mail.smtp.ssl.enable", "true");
      properties.put("mail.smtp.auth", "true");
   
      // Get the Session object.// and pass username and password
      Session session = Session.getInstance(properties, 
         new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
               // Return the email and password
               return new PasswordAuthentication("javahelpprogram330@gmail.com", "student0808");
            }
         });
   
      // Used to debug SMTP issues
      // session.setDebug(true);
      //Start our mail message
      MimeMessage msg = new MimeMessage(session);
      try {
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);
      
         // Set From: header field of the header.
         message.setFrom(new InternetAddress(fromEmail));
      
         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
      
         // Set Subject: header field
         message.setSubject("Your Account Information ");
      
         // Now set the actual message
         message.setText("You are logged in now.\n\n Email: " + email + "\n\n If you forgot the password, please follow this instruction below \n\n Instruction:\n\n 1. When program file runs \n\n 2. Type [F] or Forgot Password \n\n 3. You may reset your password with token");
         
         
      
         // To add the message and display it
         Transport.send(message);
         System.out.println("Sent message successfully....");
      } 
      // To catch any errors and display the message
      catch (MessagingException e) {
         e.printStackTrace();
      }
   }
   
   /**
    * This is used to set up the StringBuilder and insert the bytes
    *
    * @param input is to get the user input
    * 
    */
   public static byte[] getSHA(String input) {
      // Static getInstance method is called with hashing SHA  
      try {
         MessageDigest md = MessageDigest.getInstance("SHA-256");  
         return md.digest(input.getBytes(StandardCharsets.UTF_8));
      }
      // To find the error and display the message
      catch (Exception e) {
         System.out.println("Unexpected Error at Hashing... " + e.toString() + " \nContact the system administrator immediately!");
         return null;
      }
   }
   
   
   /**
    * 
    * This is used to set up the StringBuilder and insert the bytes
    *
    * @param hash is a byte
    * 
    */
   public static String toHexString(byte[] hash) { 
      
      // Set the Biginterger named number 
      BigInteger number = new BigInteger(1, hash);  
      // Set the StringBuilder named hexString 
      StringBuilder hexString = new StringBuilder(number.toString(16));  
   
      // A While Loop 
      while (hexString.length() < 32) {  
         hexString.insert(0, '0');  
      }  
      // return
      return hexString.toString();  
   }


   private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
   public static String randomAlphaNumeric(int count) {
      StringBuilder builder = new StringBuilder();
      while (count-- != 0) {
         int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
         builder.append(ALPHA_NUMERIC_STRING.charAt(character));
      }
      return builder.toString();
   }
}
