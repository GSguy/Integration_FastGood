package acs.logic;

import java.util.List;

import acs.boundaries.UserBoundary;

public interface UserServiceUpgraded extends UserService{
	public List<UserBoundary> getAllUsers(String adminEmail, int size, int page);


}
