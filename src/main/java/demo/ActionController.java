package demo;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class ActionController {
	
	@RequestMapping(path = "/acs/actions",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public Object InvokeAction(@RequestBody ActionBoundary input) {
		ActionBoundary user=input;
		return user;
	}
	
	// DELETE - delete content (SQL: delete)
	@RequestMapping(path = "/acs/admin/users/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllUsers (@PathVariable("adminEmail") String adminEmail) {
		// TODO delete all deleteAllUsers from database
	}
	
	
	// DELETE - delete content (SQL: delete)
		@RequestMapping(path = "/acs/admin/elements/{adminEmail}",
				method = RequestMethod.DELETE)
		public void deleteAllElementss (@PathVariable("adminEmail") String adminEmail) {
			// TODO delete all deleteAllUsers from database
		}
		
		
		// DELETE - delete content (SQL: delete)
				@RequestMapping(path = "/acs/admin/actions/{adminEmail}",
						method = RequestMethod.DELETE)
				public void deleteAllElements (@PathVariable("adminEmail") String adminEmail) {
					// TODO delete all deleteAllUsers from database
				}
				
				@RequestMapping(path = "/acs/admin/users/{adminEmail}",
						method = RequestMethod.GET,
						produces = MediaType.APPLICATION_JSON_VALUE)
				public UserBoundary[] exportAllUsers (@PathVariable("adminEmail") String userEmail) {
					UserBoundary [] allUsers=new UserBoundary[2];
					return allUsers;
				}
				
				@RequestMapping(path = "/acs/admin/actions/{adminEmail}",
						method = RequestMethod.GET,
						produces = MediaType.APPLICATION_JSON_VALUE)
				public ActionBoundary[] exportAllActions (@PathVariable("adminEmail") String userEmail) {
					ActionBoundary [] allUsers=new ActionBoundary[2];
					return allUsers;
				}
}
