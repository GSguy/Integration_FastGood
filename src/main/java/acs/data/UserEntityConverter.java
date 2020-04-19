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
		UserEntity entity=new UserEntity();
		if(user.getEmail()!=null) {
			entity.setEmail(user.getEmail());
		}
		
		RoleEnum type=RoleEnum.valueOf(user.getRole().name().toUpperCase());
		if(type!=null && type==RoleEnum.ADMIN||type==RoleEnum.MANAGER||type==RoleEnum.PLAYER) {
			entity.setRole(RoleEnum.valueOf(user.getRole().name().toUpperCase()));
		}
		if(user.getAvatar()!=null) {
			entity.setAvatar(user.getAvatar());
		}
		if(user.getUsername()!=null) {
			entity.setUsername(user.getUsername());
		}
		return entity;
		
	}
	public UserBoundary convertFromEntity(UserEntity user) {
	UserBoundary boundary=new UserBoundary();

	    boundary.setEmail(user.getEmail());
	    boundary.setAvatar(user.getAvatar());
	    boundary.setUsername(user.getUsername());
	    if(user.getRole()!=null) {
	    	boundary.setRole(RoleEnum.valueOf(user.getRole().name().toUpperCase()));
	    }
	    return boundary;
	}
	
}
