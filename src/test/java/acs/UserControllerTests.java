package acs;

import acs.boundaries.ElementBoundary;
import acs.boundaries.UserBoundary;
import acs.data.UserRole;
import acs.logic.EntityNotFoundException;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerTests {

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


    public UserBoundary createUserMessageForTesting()
    {
        UserBoundary user = new UserBoundary();
        user.setEmail("xxx");
        user.setAvatar("someAvatar");
        user.setRole(UserRole.PLAYER);
        user.setUsername("omer");
        return user;
    }
    public UserBoundary createPostMessageAndReturningTheMessage(String uri,UserBoundary messageToPost)
    {

        UserBoundary messageToServer =
                this.restTemplate
                        .postForObject(
                                this.url + uri,
                                messageToPost,
                                UserBoundary.class);

        return messageToServer;
    }
    @Test
    public void testGetSpecificUserWhenDBisEmpty() throws  Exception{
        // GIVEN server is up
        // do nothing

        // WHEN I GET /users/login/omer@gmail.com
        //THEN server throws EntityNotFoundException(RuntimeException)
        try {
            UserBoundary responseFromServer =
                    this.restTemplate
                            .getForObject(
                                    this.url + "users/login/{userEmail}",
                                    UserBoundary.class,"omer@gmail.com");
        }
       catch (Throwable  exception){
           assertTrue(exception instanceof RuntimeException);
        }
    }
    @Test
    public void testCreateNewUser() throws  Exception{
        // GIVEN server is up
        // do nothing
        // WHEN I POST /users with a new message

        UserBoundary  createUser= createUserMessageForTesting();
        UserBoundary  messageToPost =createPostMessageAndReturningTheMessage("users",createUser);


        // THEN the server responds with the same message details
        assertThat(this.restTemplate
                .getForObject(
                        this.url + "users/login/xxx", UserBoundary.class,messageToPost.getEmail(),messageToPost.getRole(),messageToPost.getUsername(), messageToPost.getAvatar()))
                .extracting(
                        "email",
                        "role",
                        "username",
                        "avatar"
                )
                .containsExactly(
                        "xxx",
                        UserRole.PLAYER,
                        "omer",
                        "someAvatar");


    }
}
