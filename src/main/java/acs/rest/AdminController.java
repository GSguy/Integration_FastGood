package acs.rest;

import java.util.List;

import acs.logic.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.boundaries.ActionBoundary;
import acs.boundaries.UserBoundary;
import acs.logic.ActionService;
import acs.logic.ElementService;
import acs.logic.UserService;

@RestController
public class AdminController {
	private ElementService elementService;
	private UserService userService;
	private ActionService actionService;
	
	// injection
	@Autowired
	public AdminController(ElementService elementService, UserService userService, ActionService actionService) {
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
	public void deleteAllElementss (@PathVariable("adminEmail") String adminEmail) {
		elementService.deleteAllElements(adminEmail);
	}
	// DELETE - delete content (SQL: delete)
	@RequestMapping(path = "/acs/admin/actions/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllElements (@PathVariable("adminEmail") String adminEmail) {
		actionService.deleteAllActions(adminEmail);
	}
		
	@RequestMapping(path ="/acs/admin/users/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserBoundary> getAllUsers (@PathVariable("adminEmail") String adminEmail) {
		return userService.getAllUsers(adminEmail);
		
	}
		
	@RequestMapping(path = "/acs/admin/actions/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ActionBoundary> exportAllActions (@PathVariable("adminEmail") String adminEmail) {
		return actionService.getAllActions(adminEmail);
	}
	
}