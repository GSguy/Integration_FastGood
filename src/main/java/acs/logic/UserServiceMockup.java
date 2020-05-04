package acs.logic;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acs.boundaries.UserBoundary;
import acs.data.*;

//@Service
public class UserServiceMockup implements UserService {
	
	private Map<String, UserEntity> database; 
	private UserEntityConverter userConverter;
	private AtomicReference<String> userId;
	
	public UserServiceMockup() {
	}
	
	@Autowired
	public void setUserEntityConverter(UserEntityConverter userConverter) {
		this.userConverter = userConverter;
	}
	
	@PostConstruct
	public void init() {
		// create thread safe list
		this.database = Collections.synchronizedMap(new TreeMap<>());
		this.userId = new  AtomicReference<String>();
	}
	
	@Override
	public UserBoundary createUser(UserBoundary user) {
      UserEntity entity=this.userConverter.toEntity(user);

    	if(entity.getAvatar()!=null && entity.getEmail()!=null && entity.getRole()!=null && entity.getUsername()!=null) {
    		//if(this.userId.get().equals(entity.getEmail())==false  && this.database.get(entity.getEmail())==null) //check if user exist
    	     this.userId.set(entity.getEmail());
    			this.database
 			.put(userId.get(), entity);
    	     return this.userConverter.convertFromEntity(entity);
    	//}
    	//else {
		//return null;
    	}
    	else
    		return null;
	}
	
	
	public  UserBoundary getUser(String userEmail) {
		  UserEntity user=this.database.get(userEmail);
	      if(user!=null) {
	    	  return this.userConverter.convertFromEntity(user);
	      }
	      else {
	    	  throw new EntityNotFoundException("user not found:"+userEmail);
	      }
			
	}
	
	@Override
	public UserBoundary login(String userEmail) {
     return this.getUser(userEmail);
	}
	
	@Override
	public UserBoundary updateUser(String userEmail, UserBoundary update) {
		UserBoundary existing=this.getUser(userEmail);
		boolean dirty=false;
		if(update.getAvatar()!=null) {
			existing.setAvatar(update.getAvatar());
			dirty=true;
		}
		if(update.getUsername()!=null) {
			existing.setUsername(update.getUsername());
			dirty=true;
		}
		if(update.getRole()!=null) {
			existing.setRole(UserRole.valueOf(update.getRole().name().toUpperCase()));
			dirty=true;
		}
		
		if(dirty) {
			this.database.put(existing.getEmail(),this.userConverter.toEntity(existing));
		}
		return existing;
	}
	@Override
	public List<UserBoundary> getAllUsers(String adminEmail) {
     return this.database.values().stream().map(entity->this.userConverter.convertFromEntity(entity)) 
				.collect(Collectors.toList());
	}
	
	@Override
	public void deleteAllUsers(String adminEmail) {
		this.database.clear();		
		
	}
}
