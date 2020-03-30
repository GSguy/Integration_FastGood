package demo;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
		public UserController createNewUser()



	} 

