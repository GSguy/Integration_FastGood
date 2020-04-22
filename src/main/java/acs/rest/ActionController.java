package acs.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import acs.boundaries.ActionBoundary;

@RestController
public class ActionController {
	
	@RequestMapping(path = "/acs/actions",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public Object InvokeAction(@RequestBody ActionBoundary input) {
		ActionBoundary user=input;
		return user;
	}
		
}