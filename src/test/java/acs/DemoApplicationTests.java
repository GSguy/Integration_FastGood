package acs;

import acs.boundaries.ElementBoundary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {
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
	public void testPostMessageReturnsMessageDetailsInResponse() throws Exception{
		// GIVEN server is up
		// do nothing
		// WHEN I POST /messages with a new message
		ElementBoundary messageToPost
				= new ElementBoundary("xx@xx.com","1");

		ElementBoundary responseFromServer =
				this.restTemplate
						.postForObject(
								this.url + "/{managerEmail}",
								messageToPost,
								ElementBoundary.class,"xx@xx.com");

		// THEN the server responds with the same message details, except for the timestamp and the id
		System.out.println(responseFromServer.toString());
		System.out.println("");
		System.out.println(messageToPost.toString());
		// cleanup - delete all messages from database
		assertThat(responseFromServer)
				.isEqualToComparingOnlyGivenFields(messageToPost,
						"elementId");
		assertThat(responseFromServer.getElementId()).isEqualTo(messageToPost.getElementId());
		// do nothing
	}

}
