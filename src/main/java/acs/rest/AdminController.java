package acs.rest;

import java.util.List;

import acs.logic.ActionService;
import acs.logic.ActionServiceUpgraded;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import acs.boundaries.ActionBoundary;
import acs.boundaries.UserBoundary;
import acs.logic.ElementService;
import acs.logic.ElementServiceRelational;
import acs.logic.UserService;
import acs.logic.UserServiceUpgraded;

@RestController
public class AdminController {
	private ElementServiceRelational elementService;
	private UserServiceUpgraded userService;
	private ActionServiceUpgraded actionService;
	
	// injection
	@Autowired
	public AdminController(ElementServiceRelational elementService, UserServiceUpgraded userService, ActionServiceUpgraded actionService) {
		super();
		this.elementService =  elementService;
		this.userService = userService;
		this.actionService = actionService;
	}
		
	
	// DELETE - delete content (SQL: delete)
	@RequestMapping(path = "/acs/admin/users/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllUsers (@PathVariable("adminEmail") String adminEmail) {
		userService.deleteAllUsers(adminEmail);

	}
		
		
	// DELETE - delete content (SQL: delete)
	@RequestMapping(path = "/acs/admin/elements/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllElements (@PathVariable("adminEmail") String adminEmail) {
		elementService.deleteAllElements(adminEmail);
	}
	
	
	// DELETE - delete content (SQL: delete)
	@RequestMapping(path = "/acs/admin/actions/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllActions (@PathVariable("adminEmail") String adminEmail) {
		actionService.deleteAllActions(adminEmail);
	}
		
	
	// GET -- > retrieve all instances from system (SQL: SELECT)
	@RequestMapping(path ="/acs/admin/users/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserBoundary> getAllUsers (
			@RequestParam(name = "page", required = false, defaultValue = "0") int page, 
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@PathVariable("adminEmail") String adminEmail) {
		return userService.getAllUsers(adminEmail, page, size);
	}

  
	// GET -- > retrieve all instances from system (SQL: SELECT)
	@RequestMapping(path = "/acs/admin/actions/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ActionBoundary> exportAllActions (
			@RequestParam(name = "page", required = false, defaultValue = "0") int page, 
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@PathVariable("adminEmail") String adminEmail) {
		return actionService.getAllActions(adminEmail, page, size);
	}
	
}