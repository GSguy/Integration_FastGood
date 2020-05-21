package acs.data;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import acs.boundaries.UserBoundary;

@Component
public class UserEntityConverter {
	
	private ObjectMapper jackson;
	
	@PostConstruct
	public void setup() {
		this.jackson = new ObjectMapper();
	}
	
	
	public UserEntity toEntity(UserBoundary user) {
		
		UserEntity entity = new UserEntity();
		
		if(user.getEmail() != null) 
			entity.setEmail(user.getEmail());
		else
			entity.setEmail(null);
		
		UserRole role = null;
		
		entity.setRole(UserRole.valueOf(user.getRole().name().toUpperCase()));
		entity.setAvatar(user.getAvatar());
		entity.setUsername(user.getUsername());

		
		return entity;
		
	}
	
	
	public UserBoundary convertFromEntity(UserEntity user) {
	UserBoundary boundary=new UserBoundary();

	    if (user.getEmail() != null)
	    	boundary.setEmail(user.getEmail());
	    else
	    	boundary.setEmail(null);
	    
	    
	    boundary.setAvatar(user.getAvatar());
	    boundary.setUsername(user.getUsername());

	    if(user.getRole()!=null) {
	    	boundary.setRole(UserRole.valueOf(user.getRole().name().toUpperCase()));
	    }
	    return boundary;
	}
	
}
