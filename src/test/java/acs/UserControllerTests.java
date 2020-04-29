package acs;

import acs.boundaries.ActionBoundary;
import acs.boundaries.ElementBoundary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import static org.assertj.core.api.Assertions.assertThat;

public class UserControllerTests {

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
    public void testGetAllusers() throws  Exception{


        ActionBoundary[] responseFromServer =
                this.restTemplate
                        .getForObject(
                                this.url+"/users/{adminEmail}",
                                ActionBoundary[].class,"xx");
        assertThat(responseFromServer).isEmpty();


    }
}
