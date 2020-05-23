package acs.data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class UserEntity {
	
	/*	UserBoundary
	private String email;
	private UserRole role;
	private String username;
	private String avatar;
	 */
	
	private String email;
	private UserRole role;
	private String username;
	private String avatar;
	 
	public UserEntity() {
		super();
	}
	
	@Id
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
	
	@Enumerated(EnumType.STRING)
	public UserRole getRole() {
		return role;
	}
	
	public void setRole(UserRole role) {
		this.role = role;
	}	
}