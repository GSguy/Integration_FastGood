package acs;

import acs.boundaries.ActionBoundary;
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

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasEntry;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdminControllerTests {

    private RestTemplate restTemplate;
    private String url;
    private int port;

    public ElementBoundary createElementMessageForTesting()
    {
        ElementBoundary newElement
                = new ElementBoundary("xx@xx.com", "1");

      return newElement;
    }

    public ElementBoundary createPostMessageAndReturningTheMessage(ElementBoundary messageToPost)
    {
        ElementBoundary messageToServer =
                this.restTemplate
                        .postForObject(
                                this.url + "elements/{managerEmail}",
                                messageToPost,
                                ElementBoundary.class, "xx@xx.com");
        return  messageToServer;
    }
    public ActionBoundary createActionMessageForTesting()
    {
        ActionBoundary newActionBoundary = new ActionBoundary();
        return newActionBoundary;
    }
    public ActionBoundary createPostMessageAndReturningTheMessage(ActionBoundary messageToPost)
    {
        ActionBoundary messageToServer =
                this.restTemplate
                        .postForObject(
                                this.url + "actions",
                                messageToPost,
                                ActionBoundary.class);
        return  messageToServer;
    }
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
        this.url = "http://localhost:" + port + "/acs/";
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
    // DELETE - delete all elements content (SQL: delete)
    public void testDeleteAllElementWithOneElementCreationForTesting() throws  Exception{

        ElementBoundary elementForTesting=createElementMessageForTesting();

        ElementBoundary messageToServer=createPostMessageAndReturningTheMessage(elementForTesting);

        System.err.println(elementForTesting);

        this.restTemplate.delete(this.url + "admin/elements/{managerEmail}", ElementBoundary.class, "xx@xx.com");
    }
    @Test
    // DELETE - delete all users content (SQL: delete)
    public void testDeleteAllUsersWithOneElementCreationForTesting() throws  Exception{

        ElementBoundary elementForTesting=createElementMessageForTesting();

        ElementBoundary messageToServer=createPostMessageAndReturningTheMessage(elementForTesting);

        System.err.println(elementForTesting);

        this.restTemplate.delete(this.url + "admin/users/{managerEmail}", ElementBoundary.class, "xx@xx.com");

        System.err.println(elementForTesting);


    }

    @Test
    // DELETE - delete all actions content (SQL: delete)
    public void testDeleteAllActionsWithOneElementCreationForTesting() throws  Exception{

        ActionBoundary elementForTesting=createActionMessageForTesting();

        ActionBoundary messageToServer=createPostMessageAndReturningTheMessage(elementForTesting);

        System.err.println(elementForTesting);

        this.restTemplate.delete(this.url + "actions", ElementBoundary.class);
    }







}
