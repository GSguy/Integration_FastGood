package acs.logic;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import acs.dal.UserDao;
import acs.data.UserEntity;
import acs.data.UserRole;

public class GlobalUtilites {
	
	
	 public static Boolean checkIfAdminEmailExist(String adminEmail, UserDao userDao) {
		GlobalUtilites.checkIfUserEmailExistWithError(adminEmail, userDao);
		UserEntity user = userDao.findOneByEmail(adminEmail);
		if(user != null && user.getRole() == UserRole.ADMIN) {
		return true;
		}
		else{
			return false;
		}
		
	}
	
	public static Boolean checkIfUserEmailExistWithError(String userEmail, UserDao userDao) {
		UserEntity user = userDao.findOneByEmail(userEmail);
		if(user == null) {
			throw new  EntityNotFoundException ("The email " + userEmail + " is not exist.");
		}
		else return true;
		
	}
	
	
	public static Boolean checkIfUserEmailExist(String userEmail, UserDao userDao) {
		UserEntity user = userDao.findOneByEmail(userEmail);
		if(user == null) {
			return false;
		}
		else return true;
		
	}
	
	 	// Using the official java email package - https://javaee.github.io/javamail/
	public static boolean isValidEmailAddress(String email) {
		   boolean result = true;
		   try {
		      InternetAddress emailAddr = new InternetAddress(email);
		      emailAddr.validate();
		   } catch (AddressException ex) {
		      result = false;
		   }
		   return result;
		}
}
