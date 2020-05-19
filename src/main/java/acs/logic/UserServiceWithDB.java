package acs.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acs.boundaries.UserBoundary;
import acs.dal.UserDao;
import acs.data.*;

@Service
public class UserServiceWithDB implements UserService {
	
	private UserDao userDao; // DAO = Data Access Object 
	private UserEntityConverter userConverter;
	
	
	@Autowired
	public UserServiceWithDB(UserDao userDao) {
		this.userDao = userDao;
	}
	
	
	@Autowired
	public void setUserEntityConverter(UserEntityConverter userConverter) {
		this.userConverter = userConverter;
	}
	
	
	@Override
	@Transactional //(readOnly = false)
	public UserBoundary createUser(UserBoundary user) {
		UserEntity entity = this.userConverter.toEntity(user);
		
		// Check if User Email exist
		if(entity.getEmail()==null) {
	    	throw new EntityNotFoundException("could not create new user without email" );
		}
		// ValidEmailAddress
		if (!(isValidEmailAddress(entity.getEmail()))) {
			throw new RuntimeException("User Email is not valid");
		}
		
		// Guy: i'm not sure that we need this "if" check, for now.
		//if(entity.getAvatar()!=null && entity.getEmail()!=null && entity.getRole()!=null && entity.getUsername()!=null) { //Check if all fields are valid
			entity = this.userDao.save(entity); // UPSERT:  SELECT  -> UPDATE / INSERT
			return this.userConverter.convertFromEntity(entity);	
    	//}
		//else
		//	return null;
	}
	
	
	@Transactional(readOnly = true)
	public  UserBoundary getUser(String userEmail) {
		// SELECT * FROM MESSAGES WHERE ID=? 
		Optional<UserEntity> entity = this.userDao.findById(userEmail);
		
		if (entity.isPresent()) {
			return this.userConverter.convertFromEntity(entity.get());
		}
	    else {
	    	throw new EntityNotFoundException("could not find user for email:" + userEmail);
	    }	
		
	}
	
	
	@Override
	public UserBoundary login(String userEmail) {
		return this.getUser(userEmail);
		//TODO - Guy: Here we need to implement the login operation
	}
	
	
	@Override
	@Transactional //(readOnly = false)
	public UserBoundary updateUser(String userEmail, UserBoundary update) {
		UserBoundary existing = this.getUser(userEmail);
		
		if(update.getAvatar()!=null) {
			existing.setAvatar(update.getAvatar());
		}
		if(update.getUsername()!=null) {
			existing.setUsername(update.getUsername());
		}
		if(update.getRole()!=null) {
			existing.setRole(UserRole.valueOf(update.getRole().name().toUpperCase()));
		}
		
		this.userDao.save(this.userConverter.toEntity(existing));
		
		return existing;
	}
	
	
	@Override
	@Transactional (readOnly = true)
	public List<UserBoundary> getAllUsers(String adminEmail) {
		
		// ON INIT - create new Transaction	
		List<UserBoundary> rv = new ArrayList<>(); 
				
		// SELECT * FROM MESSAGES
		Iterable<UserEntity> content = this.userDao.findAll();
		for (UserEntity msg : content) {
			rv.add(this.userConverter.convertFromEntity(msg));
		}
			
		// On SUCCESS - commit transaction
		// On Error - rollback transaction
		return rv;
	}
	
	
	@Override
	@Transactional // (readOnly = false)
	public void deleteAllUsers(String adminEmail) {
		this.userDao.deleteAll();
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