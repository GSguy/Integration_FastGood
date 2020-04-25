package acs.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import acs.boundaries.ActionBoundary;
import acs.logic.ActionService;

@RestController
public class ActionController {
	
	private ActionService actionService;
	
	@Autowired
	public ActionController(ActionService actionService) {
		super();
		this.actionService = actionService;
	}
	
	@RequestMapping(path = "/acs/actions",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object InvokeAction(@RequestBody ActionBoundary input) {	
		return actionService.invokeAction(input);
//		STUB implementation:
//		ActionBoundary user = input;
//		return user;
	}
		
}