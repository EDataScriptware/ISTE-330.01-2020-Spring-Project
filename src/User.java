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
         password = toHexString(getSHA(password)); // HASHED
      
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
         }
      
      }
       
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
            //    System.out.println("result: " + result.getObject(i + 1));
               reSet.add(result.getObject(i + 1));
            }
            row++;
         }
      
         stmt.close();
      }
      catch(Exception ex){
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
   
   


}