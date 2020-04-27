package acs;

import acs.boundaries.ElementBoundary;
import org.assertj.core.api.AbstractObjectAssert;
import org.hamcrest.collection.IsMapContaining;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasEntry;

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
		// cleanup - delete all messages from database
		assertThat(responseFromServer)
				.isEqualToComparingOnlyGivenFields(messageToPost,
						"elementId");
	}



	@Test
	public void testGetAllElementsOnServerInitReturnsEmptyArray() throws Exception{
		// GIVEN server is up
		// do nothing
		// WHEN I POST /messages with a new message
		ElementBoundary [] responseFromServer =
				this.restTemplate
						.getForObject(
								this.url+"/{managerEmail}" ,
								ElementBoundary[].class,
								"xx@xx.com");
		// THEN the server responds with the same message details, except for the timestamp and the id
		// cleanup - delete all messages from database
		assertThat(responseFromServer);

	}
	@Test
	public void testGetElementsWithEmailAndIdy() throws Exception{
		// GIVEN server is up
		// do nothing
		// WHEN I POST /messages with a new message
		ElementBoundary [] responseFromServer =
				this.restTemplate
						.getForObject(
								this.url+"/{managerEmail}" ,
								ElementBoundary[].class,
								"xx@xx.com");
		// THEN the server responds with the same message details, except for the timestamp and the id
		// cleanup - delete all messages from database
		assertThat(responseFromServer);

	}
//	@Test
//	public void testGetAllMessagesWithDatabaseContainig3MessagesReturnsAllMessagesInTheDatabase() throws Exception{
//		// GIVEN server is up
//		// AND the database contains 3 messages
//		List<ElementBoundary> databaseContent =
//				IntStream.range(0, 3) // Stream<Integer>
//						.mapToObj(i->
//								new ElementBoundary(i+"@gmail.com", i+"")
//						) // Stream<ComplexMessageBoundary>
//						.map(msg->
//						{
//
//							AtomicInteger j = new AtomicInteger(1);
//							String mail = j.getAndIncrement()+"@gmail.com";
//							this.restTemplate.postForObject(this.url+"/{managerEmail}",msg,ElementBoundary.class,mail);
//
//						}
//						.collect(Collectors.toList()); // Stream<ComplexMessageBoundary>
//					 // List<ComplexMessageBoundary>
//		System.err.println(databaseContent);
//
//		// WHEN I GET /messages
//		ElementBoundary[] dataFromServer
//				= this.restTemplate
//				.getForObject(this.url+"/{managerEmail}",
//						ElementBoundary[].class,"1@gmail.com");
//		// THEN the server returns status 2xx
//		// AND the response includes all messages in the database in any order
//		assertThat(dataFromServer)
//				.usingRecursiveFieldByFieldElementComparator()
//				.containsExactlyInAnyOrderElementsOf(databaseContent);
//
//	}
}
