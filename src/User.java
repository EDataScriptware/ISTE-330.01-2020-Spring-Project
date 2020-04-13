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


///will comment and clean alot of this stuff up - 4/8/20 matt
public class User
{
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
   String resetPasswordToken;

   public User(Connection conn)
   {
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


   public String getAffilatiionID(String Affilation){
      String idTemp = 0 + "";
      try{
      
         ArrayList<String> tempList = new ArrayList<String>();
         tempList.add(Affilation);
         ArrayList<Object> results  = getData("SELECT affiliationID FROM _affiliations WHERE affiliationName =?;", 1, tempList);
      
         if(results.size() > 0){
            idTemp = results.get(0) + "";
            System.out.println("aID Number: " + idTemp);
            affilationName = Affilation;
         }
         else{
            System.out.println("Couldn't find Affilation of " + Affilation +  "\nMarking as unknown group");
            affilationName = "Unknown";
         }
      }
      catch(Exception ex){
         ex.printStackTrace();
         System.out.println("SQLException: " + ex.getMessage());
         System.out.println("SQLException: " + ex);
      }
    
      return idTemp;
   }
   public void insertUser(){
   
      try{
         int newID = Integer.parseInt((getData("select MAX(userID) FROM USERS", 1, new ArrayList<String>()).get(0) + "").trim()) + 1;
         userID = newID + "";
         PreparedStatement preparedStmt = connection.prepareStatement("INSERT INTO USERS (userID , firstName, lastName, pswd, email, affiliationId) VALUES (" + userID + " ,'" + firstName + "','" + lastName + "','" + password +"','" + email +"','" + affiliationId + "');");
         preparedStmt.executeUpdate();
      
      }
      catch(Exception ex){
         ex.printStackTrace();
         System.out.println("SQLException: " + ex.getMessage());
         System.out.println("SQLException: " + ex);
      }
   }

   public ArrayList<Object> getData(String query, int totalFeilds, ArrayList<String> list){
      ArrayList<Object> reSet = new ArrayList<Object>();
      try{
         PreparedStatement stmt = connection.prepareStatement(query);
         for(int i = 0; i < list.size(); i++){
            stmt.setString(i + 1, list.get(i));
         }
         ResultSet result = stmt.executeQuery();
         int row = 0;
         while(result.next()){
         
            for(int i = 0; i < totalFeilds; i++){
                //System.out.println("result: " + result.getObject(i + 1));
               reSet.add(result.getObject(i + 1));
            }
            row++;
         }
      
         stmt.close();
      }
      catch(Exception ex){
         ex.printStackTrace();
         System.out.println("error " + ex);
      }
      return reSet;
   }
   public void login()
   {
      boolean repeatFlag = true;
   
      while (repeatFlag == true)
      {
         System.out.println("---------LOGIN---------");
         System.out.print("Enter your email: ");
         Scanner scnEmail = new Scanner(System.in);
         email = scnEmail.next();
      
         System.out.print("Enter a new password: ");
         Scanner scnPassword = new Scanner(System.in); 
         password = scnPassword.next();
         password = toHexString(getSHA(password)); // HASHED
         
         
         ArrayList<String> tempList = new ArrayList<String>();
         tempList.add(email);
         tempList.add(password);
         ArrayList<Object> results = getData("Select userID, firstName, lastName, affiliationId, canReview, isAdmin FROM USERS WHERE email =?  AND pswd =? ;", 6, tempList);
         if(results.size() > 0){
            userID = results.get(0) + "";
            firstName = results.get(1) + "";
            lastName = results.get(2) + "";
            affiliationId = results.get(3) + "";
            canReview = results.get(4) + "";
            isAdmin = results.get(5) + "";
            repeatFlag = false;
         }
         else{
            System.out.println("Incorrect Email or Password");
         }
      
      }
      System.out.println("Name: " + firstName + "\nlastName: " + lastName +   "\nPassword: " + password +"\nEmail: " + email + "\nAffilation: " + affiliationId); 
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



   public void resetPassword(){
   
   
      String fromEmail = "Javahelpprogram330@gmail.com";
   
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
      
      
         Transport.send(message);
         System.out.println("Sent message successfully....");
      } catch (MessagingException e) {
         e.printStackTrace();
      }
   }
   
   public static byte[] getSHA(String input)
   {
        // Static getInstance method is called with hashing SHA  
      try
      {
         MessageDigest md = MessageDigest.getInstance("SHA-256");  
         return md.digest(input.getBytes(StandardCharsets.UTF_8));
      
      }
      catch (Exception e)
      {
         System.out.println("Unexpected Error at Hashing... " + e.toString() + " \nContact the system administrator immediately!");
         return null;
      }
   }
   
   public static String toHexString(byte[] hash) 
   { 
      BigInteger number = new BigInteger(1, hash);  
      StringBuilder hexString = new StringBuilder(number.toString(16));  
   
      while (hexString.length() < 32)  
      {  
         hexString.insert(0, '0');  
      }  
   
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