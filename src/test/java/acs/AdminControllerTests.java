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
        ElementBoundary messageToPost
                = new ElementBoundary();

        messageToPost.setName("test1");
        messageToPost.setType("typeTest");


        return messageToPost;
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
        newActionBoundary.setActionID("1");
        newActionBoundary.setActionAttributes(testMap);
        newActionBoundary.setElement(testMap);
        newActionBoundary.setInvokedBy(testMap);
        newActionBoundary.setType("someType");
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

        // GIVEN server is run properly
        // WHEN I POST /elements/xx@xx.com with a new message
        // AND i insert values to ElementBoundary object with name-"test1" and type "typeTest"
        // THEN the server responds with the same message details, except for the timestamp,elementId


        ElementBoundary messageToPost
                = new ElementBoundary();

        messageToPost.setName("test1");
        messageToPost.setType("typeTest");

        ElementBoundary responseFromServer =
                this.restTemplate
                        .postForObject(
                                this.url + "/elements/{managerEmail}",
                                messageToPost,
                                ElementBoundary.class,"xx@xx.com");


        assertThat(responseFromServer)
                .isEqualToComparingOnlyGivenFields(messageToPost,
                        "name","type");
    }
    @Test
    // DELETE - delete all elements content (SQL: delete)
    public void testDeleteAllElementWithOneElementCreationForTesting() throws  Exception{

        // GIVEN server is run properly
        // do nothing
        // WHEN I DELETE /admin/elements/xx@xx.com
        // THEN Database is empty

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
        // GIVEN server is run properly
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
          // GIVEN server is run properly
          // do nothing
          // WHEN I DELETE /actions
          // THEN Database is empty
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
