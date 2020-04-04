import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.io.*;
import java.util.Date;

public class User
{
   String userID;
   String lastName;
   String firstName;
   String email;
   String password;
   String verifyPassword;
   String canReview; // '1' for yes / NULL value for no
   Date dateOfExpiration;
   String affiliationId; // '1' for yes / '0' value for no

   public User()
   {
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
         System.out.print("Enter a new username: ");
         Scanner scnUser = new Scanner(System.in);
         firstName = scnUser.next();
       
         System.out.print("Enter a new password: ");
         Scanner scnPassword = new Scanner(System.in); 
         password = scnPassword.next();
       
         System.out.print("Verify your new password: ");
         Scanner scnVerifyPassword = new Scanner(System.in); // Note: Hash the password at a later time. 
         verifyPassword = scnVerifyPassword.next();
       
         if (verifyPassword == password)
         {
            System.out.println("Username: " + firstName + "\nPassword: " + password); 
            repeatFlag = false;
         }
         
         else 
         {
            System.out.println("ERROR: Password not verified! Please try again.");
         }
      
      }
       
   } 
   
   public void login()
   {
      System.out.println("---------LOGIN---------");
      System.out.print("Enter username: ");
      Scanner scnUser = new Scanner(System.in);
      firstName = scnUser.next();
       
      System.out.print("Enter password: ");
      Scanner scnPassword = new Scanner(System.in); // Note: Hash the password at a later time. 
      password = scnPassword.nextLine();
       
      System.out.println("Username: " + firstName + "\nPassword: " + password); 
   } 


}