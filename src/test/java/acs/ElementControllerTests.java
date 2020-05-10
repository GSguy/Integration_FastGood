package acs;

import acs.boundaries.ElementBoundary;
import acs.boundaries.UserBoundary;
import acs.data.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ElementControllerTests {
	private RestTemplate restTemplate;
	private String url;
	private int port;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		//this.url = "http://localhost:" + port + "/acs/elements";
		this.restTemplate = new RestTemplate();
	}

	@BeforeEach
	public void setup() {
		this.url = "http://localhost:" + port + "/acs/elements";
	}

//	@AfterEach
//	public void teardown() {
//		this.restTemplate
//				.delete(this.url);
//	}


	@Test
	public void testPostMessageReturnsMessageDetailsInResponse() throws Exception {
		// GIVEN server is run properly
		// WHEN I POST /elements/omer@gmail.com with a new message
		// AND i insert values to ElementBoundary object with name-"test1" and type "typeTest"
		// THEN the server responds with the same message details, except for the timestamp

		ElementBoundary messageToPost
				= new ElementBoundary();
		messageToPost.setName("test1");
		messageToPost.setType("typeTest");
		ElementBoundary responseFromServer =
				this.restTemplate
						.postForObject(
								this.url + "/{managerEmail}",
								messageToPost,
								ElementBoundary.class, "omer@gmail.com");

		assertThat(responseFromServer)
				.isEqualToComparingOnlyGivenFields(messageToPost,
						"name","type");
	}

	@Test
	public void testGetAllElementsOnServerInitReturnsEmptyArray() throws Exception {

		// GIVEN server is run properly
		// do nothing
		// WHEN I GET elements/omer@gmail.com with a new message
		// THEN the server responds with empty array

		ElementBoundary[] responseFromServer =
				this.restTemplate
						.getForObject(
								this.url + "/{managerEmail}",
								ElementBoundary[].class,
								"omer@gmail.com");

		assertThat(responseFromServer).isEmpty();

	}

	@Test
	public void testGetElementsWithEmailAndIdy() throws Exception {

		// GIVEN server is run properly
		// WHEN I POST /elements/omer@gmail.com with a new message
		// AND I insert values to ElementBoundary object with name "test1",type "typeTest",elementID "1"
		// THEN the server responds with the same message details, except for the timestamp

		ElementBoundary messageToPost
				= new ElementBoundary();

		messageToPost.setName("test1");
		messageToPost.setType("typeTest");

		ElementBoundary responseFromServer =
				this.restTemplate
						.postForObject(
								this.url + "/{managerEmail}",
								messageToPost,
								ElementBoundary.class, "omer@gmail.com");

		assertThat(responseFromServer)
				.isEqualToComparingOnlyGivenFields(messageToPost,
						"name","type");

	}

	@Test
	public void testUpdatingElementsWithEmailAndIdy() throws Exception {

		// GIVEN server is up
		// do nothing
		// WHEN I PUT /{managerEmail}/{elementId} with a new message
		// WITH responseFromServer.setName("test4");

		ElementBoundary messageToPost = new ElementBoundary();
		Map testMap = new HashMap<String,Object>();
		testMap.put("sdf","sdasdf");
		messageToPost.setName("test4");
		messageToPost.setType("someType");
		messageToPost.setElementAttributes(testMap);

		ElementBoundary responseFromServer =
				this.restTemplate
						.postForObject(
								this.url + "/{managerEmail}",
								messageToPost,
								ElementBoundary.class, "omer@gmail.com");


		responseFromServer.setName("test4");
		responseFromServer.setType("someType");


		this.restTemplate
				.put(this.url + "/{managerEmail}/{elementId}",
						responseFromServer,
						"omer@gmail.com", "1");

		// THEN the PUT operation is responded with status 2xx
		// AND the database is updated
		assertThat(this.restTemplate
				.getForObject(
						this.url + "/{managerEmail}/{elementId}", ElementBoundary.class, responseFromServer.getName(), "1"))
				.extracting(
						"elementId",
						"name"
				)
				.containsExactly(
						"1",
						"test4");
	}
	@Test
	public void testCreateElementWithNullName() throws  Exception{
		// GIVEN server is running properly
		// AND I create user with invalid Element name  as 'null'
		// WHEN I POST /acs/elements/{managerEmail} with a new message
		// THEN the server responds with expected exception

		Exception exception = assertThrows(RuntimeException.class, () -> {
			ElementBoundary messageToPost
					= new ElementBoundary();
			messageToPost.setActive(true);

			ElementBoundary responseFromServer =
					this.restTemplate
							.postForObject(
									this.url + "/{managerEmail}",
									messageToPost,
									ElementBoundary.class, "xx@xx.com");


		});

		String expectedMessage = "Element Name Cannot be null";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}
	@Test
	public void testCreateElementWithNullElementType() throws  Exception{

		// GIVEN server is running properly
		// AND I create user with invalid Element Type  as 'null'
		// WHEN I POST /acs/elements/{managerEmail} with a new message
		// THEN the server responds with expected exception

		Exception exception = assertThrows(RuntimeException.class, () -> {
			ElementBoundary messageToPost
					= new ElementBoundary();
			messageToPost.setActive(true);
			messageToPost.setName("test");

			ElementBoundary responseFromServer =
					this.restTemplate
							.postForObject(
									this.url + "/{managerEmail}",
									messageToPost,
									ElementBoundary.class, "xx@xx.com");


		});

		String expectedMessage = "Element Type Cannot be null";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}
}

