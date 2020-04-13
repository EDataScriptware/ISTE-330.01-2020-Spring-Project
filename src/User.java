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
   Connection connection;
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
<<<<<<< HEAD
   String resetPasswordToken;

   public User(Connection conn)
   {
=======
   
   
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
   public User (Connection conn) {
   
      // All attributes are created 
>>>>>>> c6ce21451addfdfe38a83f120c7da46a839e01d0
      connection = conn;
      userID = null;
      lastName = null;
      firstName = null;
      email = null;
      password = null;
      verifyPassword = null;
      canReview = null; // '1' for yes / NULL value for no
      dateOfExpiration = null;
      affiliationId = null; // '1' for yes / '0' value for no
<<<<<<< HEAD
      resetPasswordToken = null;
   }
   
   public void register()
{
   boolean repeatFlag = true;

   while (repeatFlag == true)
   {
      System.out.println("---------REGISTER---------");
      System.out.print("Enter your first name: ");
      Scanner scnUser = new Scanner(System.in);
      firstName = scnUser.next();

      System.out.print("Enter your last name: ");
      Scanner scnNamel = new Scanner(System.in);
      lastName = scnNamel.next();

      System.out.print("Enter a new password: ");
      Scanner scnPassword = new Scanner(System.in);
      password = scnPassword.next();
      password = toHexString(getSHA(password)); // HASHED

      System.out.print("Verify your new password: ");
      Scanner scnVerifyPassword = new Scanner(System.in); // Note: Hash the password at a later time.
      verifyPassword = scnVerifyPassword.next();
      verifyPassword = toHexString(getSHA(verifyPassword)); // HASHED

      System.out.print("Enter your email: ");
      Scanner scnEmail = new Scanner(System.in);
      email = scnEmail.next();

      System.out.print("Enter your Affiliation: ");
      Scanner scnAfId = new Scanner(System.in);
      affiliationId = getAffilatiionID(scnAfId.nextLine());





      if (verifyPassword.equals(password))
      {
         System.out.println("Name: " + firstName + "\nlastName: " + lastName +   "\nPassword: " + password +"\nEmail: " + email + "\nAffilation: " + affilationName);
         repeatFlag = false;
         insertUser();
      }

      else
      {
         System.out.println("ERROR: Password not verified! Please try again.");
         System.out.println("Password: " + password);
         System.out.println("Verfied Password: " + verifyPassword);
      }

   }

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
         PreparedStatement preparedStmt = connection.prepareStatement("SELECT email FROM  users WHERE email= ?");
         preparedStmt.setString(1, email);
         ResultSet resultSet = preparedStmt.executeQuery();

         if (resultSet.next()) {
            isEmailFound = true;
            System.out.println("Row with email found: " +resultSet.getString("email"));
         }

      }
      catch(Exception ex){
         ex.printStackTrace();
         System.out.println("SQLException: " + ex.getMessage());
         System.out.println("SQLException: " + ex);
      }

      return isEmailFound;
   }

=======
   }
   
   
   /**
    *
    * A While Loop is running while the user is doing with 
    * the registration of their account
    *
    * No parameters are used
    *
    */
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
         affiliationId = getAffilatiionID(scnAfId.nextLine());
      
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
   } 
>>>>>>> c6ce21451addfdfe38a83f120c7da46a839e01d0

   /**
    *
    * This is used to set up the affilation for each user
    *
    * @para Affilation is the user's unique ID number
    *
    */
   public String getAffilatiionID(String Affilation) {
   
      // Set up the string named idTemp
      String idTemp = 0 + "";
      
      try {
         
         // Set up the array named tempList
         ArrayList<String> tempList = new ArrayList<String>();
         // Added Affilation to the tempList
         tempList.add(Affilation);
         // Set up the second array named results to get the data
         ArrayList<Object> results  = getData("SELECT affiliationID FROM _affiliations WHERE affiliationName =?;", 1, tempList);
         // To find the ID number
         if(results.size() > 0){
            idTemp = results.get(0) + "";
            System.out.println("aID Number: " + idTemp);
            affilationName = Affilation;
         }
         // If the affilation cannot be founded, it will display the message in the output to let the user knows
         else{
            System.out.println("Couldn't find Affilation of " + Affilation +  "\nMarking as unknown group");
            affilationName = "Unknown";
         }
      }
      // To catch any errors and display the error messages
      catch(Exception ex){
         ex.printStackTrace();
         System.out.println("SQLException: " + ex.getMessage());
         System.out.println("SQLException: " + ex);
      }
      // return
      return idTemp;
   }
   
   
   /**
    *
    * To insert the User ID
    *
    * No parameters are used
    *
    */
   public void insertUser(){
   
<<<<<<< HEAD
      try{
=======
      try {
         // Selects the user id from USERS 
>>>>>>> c6ce21451addfdfe38a83f120c7da46a839e01d0
         int newID = Integer.parseInt((getData("select MAX(userID) FROM USERS", 1, new ArrayList<String>()).get(0) + "").trim()) + 1;
         userID = newID + "";
         // Prepare the statement for inserting into USERS
         PreparedStatement preparedStmt = connection.prepareStatement("INSERT INTO USERS (userID , firstName, lastName, pswd, email, affiliationId) VALUES (" + userID + " ,'" + firstName + "','" + lastName + "','" + password +"','" + email +"','" + affiliationId + "');");
         
         // To get the prepared statement executed 
         preparedStmt.executeUpdate();
      
      }
      // To catch any errors and display the error messages
      catch(Exception ex){
         ex.printStackTrace();
         System.out.println("SQLException: " + ex.getMessage());
         System.out.println("SQLException: " + ex);
      }
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
      
         // Set up the prepared statement named stmt for the query
         PreparedStatement stmt = connection.prepareStatement(query);
         
         // A For Statements starts here
         for(int i = 0; i < list.size(); i++){
            stmt.setString(i + 1, list.get(i));
         }
         // To execute the query and put it in result
         ResultSet result = stmt.executeQuery();
         
         // row starts with zero
         int row = 0;
         
         // A While Loop starts here
         while(result.next()){
         
            for(int i = 0; i < totalFeilds; i++) {
               reSet.add(result.getObject(i + 1));
            }
            row++; // Add row
         }
         // Statement is closed
         stmt.close();
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
   public void login() {
   
      // Set it to be true
      boolean repeatFlag = true;
   
      // A While Loop starts here
      while (repeatFlag == true) {
         System.out.println("---------LOGIN---------");
         
         // Enter the user's email
         System.out.print("Enter your email: ");
         Scanner scnEmail = new Scanner(System.in);
         email = scnEmail.next();
         
         // Enter the user's new password
         System.out.print("Enter a new password: ");
         Scanner scnPassword = new Scanner(System.in); 
         password = scnPassword.next();
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
            repeatFlag = false;
         }
         
         // To display the error message of email or password
         else {
            System.out.println("Incorrect Email or Password");
         }
      
      }
      // To display the summary of the user's name, lastname, password, email, and affilation
      System.out.println("Name: " + firstName + "\nlastName: " + lastName +   "\nPassword: " + password +"\nEmail: " + email + "\nAffilation: " + affiliationId); 
      // To reset the password
      resetPassword();
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
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE users SET resetPasswordToken = ?, resetPasswordExpires = ? WHERE email = ?");

            // set the prepared statement parameters
            ps.setString(1,randomPassword);
            ps.setLong(2,passwordExpiryDate);
            ps.setString(3,email);

            // call executeUpdate to execute our sql update statement
            ps.executeUpdate();
            ps.close();
         }
         catch (SQLException se)
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
            PreparedStatement preparedStmt = connection.prepareStatement("SELECT resetPasswordExpires FROM  users WHERE email= ? and resetPasswordToken = ?");
            preparedStmt.setString(1, email);
            preparedStmt.setString(2, resetPasswordToken);
            ResultSet resultSet = preparedStmt.executeQuery();

            if (resultSet.next()) {
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

         } catch (SQLException se) {
            // log the exception
            se.printStackTrace();
         }
      }
   }


   /**
    * 
    * This is used to set up the email formats and reset the password
    *
    * @param are not used
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
      Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
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
         message.setSubject("Hello!!!!!!!!!!!!!!!!!!!");
      
         // Now set the actual message
         message.setText("Just logged in :D");
      
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
    * 
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
<<<<<<< HEAD
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
=======
   } 
}
>>>>>>> c6ce21451addfdfe38a83f120c7da46a839e01d0
