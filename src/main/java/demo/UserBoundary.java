package demo;

public class UserBoundary {
	
	private String email;
	private RoleEnum role;
	private String username;
	private String avatar;
	 
	public UserBoundary() {
		 
	 }
	
	public UserBoundary(String email) {
		 super();
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
	
	public RoleEnum getRole() {
		return role;
	}
	
	public void setRole(RoleEnum role) {
		this.role = role;
	}	
  
}
