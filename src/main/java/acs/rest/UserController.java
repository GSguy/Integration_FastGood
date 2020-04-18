package acs.rest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.boundaries.UserBoundary;
@RestController
public class UserController {
	
	
	@RequestMapping(path = "/acs/users/login/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary getUsers (@PathVariable("userEmail") String userEmail) {
		return new UserBoundary (userEmail);
	}
	
	@RequestMapping(path = "/acs/users",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary createNewUser(@RequestBody UserBoundary input) {
		UserBoundary user=input;
		return user;
	}


	// PUT - update content (SQL: update)
	@RequestMapping(path = "/acs/users/{userEmail}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void update (
			@PathVariable("userEmail") String userEmail, 
			@RequestBody UserBoundary update) {
		// TODO update message with id: messageId with details within update
	}
	
} 
