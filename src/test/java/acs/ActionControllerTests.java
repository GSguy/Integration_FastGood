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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ActionControllerTests {

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
        this.url = "http://localhost:" + port + "/acs/";
    }


    @Test
    public void testCreateActionWithEmptyInvokedBy() throws Exception {
        // GIVEN server is running properly
        // AND I create user with invalid Action Type  as 'null'
        // WHEN I POST /actions with a new message
        // THEN the server responds with expected exception
        Exception exception = assertThrows(RuntimeException.class, () -> {
            ActionBoundary messageToPost = new ActionBoundary();
            
            HashMap<String, Object> testMap=new HashMap<String,Object>();
            testMap.put("elementId","1");
            messageToPost.setActionAttributes(testMap);
            messageToPost.setType("type1");
            
            HashMap<String, String> testElement=new HashMap<String,String>();
            testElement.put("elementId","1");
            messageToPost.setElement(testElement);

            ElementBoundary responseFromServer =
                    this.restTemplate
                            .postForObject(
                                    this.url + "/actions",
                                    messageToPost,
                                    ElementBoundary.class);
        });

        String expectedMessage = "Cannot invoke action without element id or inovkedby";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    
    @Test
    public void testCreateNullActionElement() throws Exception{
        // GIVEN server is running properly
        // AND I create user with invalid ActionElement Type  as 'null'
        // WHEN I POST /actions with a new message
        // THEN the server responds with expected exception

        Exception exception = assertThrows(RuntimeException.class, () -> {
            ActionBoundary messageToPost = new ActionBoundary();
            
            HashMap<String, Object> testMap = new HashMap<String,Object>();
            testMap.put("someKey","someValue");
            messageToPost.setActionAttributes(testMap);
            messageToPost.setType("type1");
            

            ElementBoundary responseFromServer =
                    this.restTemplate
                            .postForObject(
                                    this.url + "/actions",
                                    messageToPost,
                                    ElementBoundary.class);
        });

        String expectedMessage = "Cannot invoke action without element id or inovkedby";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    
    @Test
    public void testCreateActionProperly() throws Exception {
            // GIVEN server is running properly
            // AND I create ActionBoundary object and assign attributes
            // WHEN I POST /actions with a new message 'messageToPost'
            // THEN the server responds with expected 'type' and 'actionAttributes'

            ActionBoundary messageToPost = new ActionBoundary();
            
            HashMap<String, Object> AttributestMap=new HashMap<String,Object>();
            AttributestMap.put("1","1");
            HashMap<String, String> elementMap=new HashMap<String,String>();
            elementMap.put("elementId","12");
            HashMap<String, String> invokeBy=new HashMap<String,String>();
            invokeBy.put("email","guy@gmail.com");

            messageToPost.setActionAttributes(AttributestMap);
            messageToPost.setElement(elementMap);
            messageToPost.setInvokedBy(invokeBy);
            messageToPost.setType("someType");

            ActionBoundary responseFromServer =
                        this.restTemplate
                                .postForObject(
                                        this.url + "/actions",
                                        messageToPost,
                                        ActionBoundary.class);
            
        assertThat(responseFromServer)
                .isEqualToComparingOnlyGivenFields(messageToPost,
                        "type","actionAttributes");
    }
    
}