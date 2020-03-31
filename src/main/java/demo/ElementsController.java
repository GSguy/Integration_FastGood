package demo;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class ElementsController {
	@RequestMapping(path = "/acs/elements{managerEmail}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary createNewUser(@RequestBody ElementBoundary input,
			@PathVariable("managerEmail") String managerEmail) {
		ElementBoundary user=input;
		return user;
	}

	// PUT - update content (SQL: update)
	@RequestMapping(path = "/acs/elements{managerEmail}/{elementId}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateElement (
			@PathVariable("managerEmail") String managerEmail,
			@PathVariable("elementId") String elementId,
			@RequestBody ElementBoundary update) {
	}
	
	
	@RequestMapping(path = "/acs/elements/{managerEmail}/{elementId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary getElement (
			@PathVariable("managerEmail") String managerEmail,
			@PathVariable("elementId") String elementId) {
		return new ElementBoundary (managerEmail,elementId);
	}
	
	@RequestMapping(path = "/acs/elements/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllElements (@PathVariable("userEmail") String userEmail) {
		ElementBoundary [] allElements=new ElementBoundary[2];
		return allElements;
	}
}
