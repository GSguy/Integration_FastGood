package acs.rest;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import acs.boundaries.ElementBoundary;
import acs.boundaries.ElementIdWrapper;
import acs.logic.ElementServiceRelational;


@RestController
public class ElementController {
	private ElementServiceRelational elementService;
	
	// injection
	@Autowired
	public ElementController(ElementServiceRelational elementService) {
		super();
		this.elementService =  elementService;
	}
	
	
	// POST -- > store instance in system (SQL: INSERT)
	@RequestMapping(path = "/acs/elements/{managerEmail}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary createNewElement(@RequestBody ElementBoundary input,
			@PathVariable("managerEmail") String managerEmail) {
//		// STUB implementation
//		ElementBoundary user = input;
//		return user;
		return this.elementService.create(managerEmail, input);
	}
	
	
	// PUT - update content (SQL: update)
	@RequestMapping(path = "/acs/elements/{managerEmail}/{elementId}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary updateElement (
			@PathVariable("managerEmail") String managerEmail,
			@PathVariable("elementId") String elementId,
			@RequestBody ElementBoundary update) {
		
		return this.elementService.update(managerEmail, elementId, update);
	}
	
	
	// GET -- > retrieve all instances from system (SQL: SELECT)
	@RequestMapping(path = "/acs/elements/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ElementBoundary> getAllElements (
			@RequestParam(name = "page", required = false, defaultValue = "0") int page, 
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@PathVariable("userEmail") String userEmail) {
//		// STUB implementation
//		ElementBoundary [] allElements=new ElementBoundary[2];
//		return allElements;
		return this.elementService.getAll(userEmail, page, size);
	}
	
	
	// GET -- > retrieve instance from system (SQL: SELECT)
	@RequestMapping(path = "/acs/elements/{managerEmail}/{elementId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary getElement (
			@PathVariable("managerEmail") String managerEmail,
			@PathVariable("elementId") String elementId) {
//		// STUB implementation
//		return new ElementBoundary(managerEmail,elementId);
		return this.elementService.getSpecificElement(managerEmail, elementId);
	}
	

	// PUT - update content (SQL: update)
	@RequestMapping(path = "/acs/elements/{managerEmail}/{parentElementId}/children",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addElementToElement (
			@PathVariable("managerEmail") String managerEmail,
			@PathVariable("parentElementId") String parentElementId,
			@RequestBody ElementIdWrapper id) { 
		this.elementService
			.addElementToParent(parentElementId, id.getId(),managerEmail);
	}
	
	
	// GET -- > retrieve instance from system (SQL: SELECT)
	@RequestMapping(path = "/acs/elements/{userEmail}/{parentElementId}/children",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getChildren (
			@RequestParam(name = "page", required = false, defaultValue = "0") int page, 
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("parentElementId") String parentElementId) { 
		return this.elementService.getChildrens(parentElementId, userEmail, page, size).toArray(new ElementBoundary[0]); // Java Reflection
	}
	
	
	// GET -- > retrieve instance from system (SQL: SELECT)
	@RequestMapping(path =  "/acs/elements/{userEmail}/{ChildElementId}/parents",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getParents (
			@RequestParam(name = "page", required = false, defaultValue = "0") int page, 
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("ChildElementId") String ChildElementId)  { 
		return this.elementService.getParents(ChildElementId, userEmail, page, size).toArray(new ElementBoundary[0]); // Java Reflection
	}
	
	// GET -- > retrieve instance from system (SQL: SELECT)
	@RequestMapping(path =  "/acs/elements/{userEmail}/search/byName/{name}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getElementsByName (
			@RequestParam(name = "page", required = false, defaultValue = "0") int page, 
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("name") String name)  { 
		return this.elementService.getElementsByName(name, userEmail, page, size).toArray(new ElementBoundary[0]); // Java Reflection
	}
	
	// GET -- > retrieve instance from system (SQL: SELECT)
	@RequestMapping(path =  "/acs/elements/{userEmail}/search/byType/{type}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getElementsByType (
			@RequestParam(name = "page", required = false, defaultValue = "0") int page, 
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@PathVariable("userEmail") String userEmail,
			@PathVariable("type") String type)  { 
		return this.elementService.getElementsByType(type, userEmail, page, size).toArray(new ElementBoundary[0]); // Java Reflection
	}

	
}
