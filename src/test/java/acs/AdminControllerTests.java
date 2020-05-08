package acs;

import acs.boundaries.ActionBoundary;
import acs.boundaries.ElementBoundary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;


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
        HashMap testMap=new HashMap<String,Object>();
        testMap.put("1","1");
        newActionBoundary.setActionId("1");
        newActionBoundary.setActionAttributes(testMap);
        newActionBoundary.setElement(testMap);
        newActionBoundary.setInvokedBy(testMap);
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

        System.err.println(messageToPost);

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
                                this.url + "/elements/{managerEmail}",
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
        // GIVEN server is up
        // do nothing
        // WHEN I DELETE /admin/elements/xx@xx.com
        //THEN Database is empty
        ElementBoundary elementForTesting=createElementMessageForTesting();

        ElementBoundary messageToServer=createPostMessageAndReturningTheMessage(elementForTesting);

        System.err.println(elementForTesting);

        this.restTemplate.delete(this.url + "admin/elements/{adminEmail}", String.class, "xx@xx.com");

        ElementBoundary[] responseFromServer =
                this.restTemplate
                        .getForObject(
                                this.url+"elements/{adminEmail}",
                                ElementBoundary[].class,"xx@xx.com");

        assertThat(responseFromServer).isEmpty();

    }
    @Test
    // DELETE - delete all users content (SQL: delete)
    public void testDeleteAllUsersWithOneElementCreationForTesting() throws  Exception{
        // GIVEN server is up
        // do nothing
        // WHEN I DELETE admin/users/{managerEmail}
        //THEN Database is empty
        ElementBoundary elementForTesting=createElementMessageForTesting();

        ElementBoundary messageToServer=createPostMessageAndReturningTheMessage(elementForTesting);


        this.restTemplate.delete(this.url + "admin/users/{managerEmail}", ElementBoundary.class, "xx@xx.com");

        ElementBoundary[] responseFromServer =
                this.restTemplate
                        .getForObject(
                                this.url + "admin/users/{managerEmail}",
                                ElementBoundary[].class,
                                "xx@xx.com");
        
        assertThat(responseFromServer).isEmpty();
    }

      @Test
    public void testDeleteAllActionsWithOneElementCreationForTesting() throws  Exception{
          // GIVEN server is up
          // do nothing
          // WHEN I DELETE /actions
          //THEN Database is empty
        ActionBoundary elementForTesting=createActionMessageForTesting();

        ActionBoundary messageToServer=createPostMessageAndReturningTheMessage(elementForTesting);

        System.err.println(elementForTesting);

        this.restTemplate.delete(this.url + "/admin/actions/{adminEmail}", ActionBoundary.class,"xx@xx.com");

        ActionBoundary[] responseFromServer =
                this.restTemplate
                        .getForObject(
                                this.url + "/admin/actions/{adminEmail}",
                                ActionBoundary[].class,
                                "xx@xx.com");


        assertThat(responseFromServer).isEmpty();


    }







}
