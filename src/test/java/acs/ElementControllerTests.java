package acs;

import acs.boundaries.ElementBoundary;
import acs.boundaries.UserBoundary;
import acs.data.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

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
		this.restTemplate = new RestTemplate();
	}

	@BeforeEach
	public void setup() {
		this.url = "http://localhost:" + port + "/acs/elements";
	}


	@Test
	public void testPostMessageReturnsMessageDetailsInResponse() throws Exception {
		// GIVEN server is up
		// do nothing
		// WHEN I POST /elements/xx@xx.com with a new message
		ElementBoundary messageToPost
				= new ElementBoundary("xx@xx.com", "2");

		ElementBoundary responseFromServer =
				this.restTemplate
						.postForObject(
								this.url + "/{managerEmail}",
								messageToPost,
								ElementBoundary.class, "xx@xx.com");

		// THEN the server responds with the same message details, except for the timestamp
		assertThat(responseFromServer)
				.isEqualToComparingOnlyGivenFields(messageToPost,
						"elementId");
	}

	@Test
	public void testGetAllElementsOnServerInitReturnsEmptyArray() throws Exception {
		// GIVEN server is up
		// do nothing
		// WHEN I GET elements/xx@xx.com with a new message
		ElementBoundary[] responseFromServer =
				this.restTemplate
						.getForObject(
								this.url + "/{managerEmail}",
								ElementBoundary[].class,
								"xx@xx.com");
		// THEN the server responds with empty array
		assertThat(responseFromServer).isEmpty();

	}

	@Test
	public void testGetElementsWithEmailAndIdy() throws Exception {
		// GIVEN server is up
		// do nothing
		// WHEN I GET elements/xx@xx.com with a new message
		ElementBoundary messageToPost
				= new ElementBoundary("xx@xx.com", "1");

		ElementBoundary responseFromServer =
				this.restTemplate
						.postForObject(
								this.url + "/{managerEmail}",
								messageToPost,
								ElementBoundary.class, "xx@xx.com");

		// THEN the server responds with the same message details, except for the timestamp
		assertThat(responseFromServer)
				.isEqualToComparingOnlyGivenFields(messageToPost,
						"elementId");

	}

	@Test
	public void testUpdatingElementsWithEmailAndIdy() throws Exception {

		ElementBoundary messageToPost
				= new ElementBoundary("xx@xx.com", "1");

		ElementBoundary responseFromServer =
				this.restTemplate
						.postForObject(
								this.url + "/{managerEmail}",
								messageToPost,
								ElementBoundary.class, "xx@xx.com");

		// GIVEN server is up
		// do nothing
		// WHEN I PUT /{managerEmail}/{elementId} with a new message
		// WITH responseFromServer.setName("test4");
		responseFromServer.setName("test4");

		this.restTemplate
				.put(this.url + "/{managerEmail}/{elementId}",
						responseFromServer,
						"xx@xx.com", "1");

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

