package acs.boundaries;

import acs.data.UserRole;

public class UserBoundary {
	
	private String email;
	private UserRole role;
	private String username;
	private String avatar;
	 
	
	public UserBoundary() {
		 super();


	 }
	
	public UserBoundary(String email) {
		this();
		setEmail(email);
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getAvatar() {
		return avatar;
	}
	
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}


	public UserRole getRole() {
		return role;
	}
	
	public void setRole(UserRole role) {
		this.role = role;
	}	
  
}
