package acs;

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
        user.setEmail("omerlewitz@gmail.com");
        user.setAvatar("someAvatar");
        user.setRole(UserRole.PLAYER);
        user.setUsername("omer");
        return user;
    }
    
    
    public UserBoundary createPostMessageAndReturningTheMessage(String uri, UserBoundary messageToPost)
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
        // AND I created user with email-->omer@gmail.com
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

        UserBoundary  createUser = createUserMessageForTesting();
        UserBoundary  messageToPost =createPostMessageAndReturningTheMessage("users",createUser);

        // THEN the server responds with the same message details
        assertThat(this.restTemplate
                .getForObject(
                        this.url + "users/login/omerlewitz@gmail.com", UserBoundary.class,messageToPost.getEmail(),messageToPost.getRole(),messageToPost.getUsername(), messageToPost.getAvatar()))
                .extracting(
                        "email",
                        "role",
                        "username",
                        "avatar"
                )
                .containsExactly(
                        "omerlewitz@gmail.com",
                        UserRole.valueOf(UserRole.PLAYER.name().toUpperCase()),
                        "omer",
                        "someAvatar");
    }
    
    
    @Test()
    public void testCreateNewUserWithInvalidEmail() throws  Exception{
        // GIVEN server is running properly
        // AND I create user with invalid Email 'xxx'
        // WHEN I POST /users with a new message
        // THEN the server responds with expected excecption

        Exception exception = assertThrows(RuntimeException.class, () -> {
            UserBoundary user = new UserBoundary();
            user.setEmail("xxx");
            user.setAvatar("someAvatar");
            user.setRole(UserRole.PLAYER);
            user.setUsername("omer");

            UserBoundary  messageToPost =createPostMessageAndReturningTheMessage("users",user);


            UserBoundary messageToServer =
                    this.restTemplate
                            .postForObject(
                                    this.url + "users",
                                    messageToPost,
                                    UserBoundary.class);


        });

        String expectedMessage = "User Email is not valid";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    
    @Test()
    public void testCreateNewUserWithNullUserRole() throws  Exception {
        // GIVEN server is running properly
        // AND I create user with invalid UserRole as 'null'
        // WHEN I POST /users with a new message
        // THEN the server responds with expected excecption

        Exception exception = assertThrows(RuntimeException.class, () -> {
            UserBoundary user = new UserBoundary();
            user.setEmail("omerlewitz@gmail.com");
            user.setAvatar("someAvatar");
            user.setUsername("omer");

            UserBoundary  messageToPost = createPostMessageAndReturningTheMessage("users", user);

            UserBoundary messageToServer =
                    this.restTemplate
                            .postForObject(
                                    this.url + "users",
                                    messageToPost,
                                    UserBoundary.class);
        });

        String expectedMessage = "User Role cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    
    @Test
    public void testCreateNewUserWithNullAvatar() throws  Exception{
        // GIVEN server is running properly
        // AND I create user with invalid avatar as 'null'
        // WHEN I POST /users with a new message
        // THEN the server responds with expected excecption

        Exception exception = assertThrows(RuntimeException.class, () -> {
            UserBoundary user = new UserBoundary();
            user.setEmail("omerlewitz@gmail.com");
            user.setRole(UserRole.PLAYER);
            user.setUsername("omer");

            UserBoundary  messageToPost =createPostMessageAndReturningTheMessage("users",user);

            UserBoundary messageToServer =
                    this.restTemplate
                            .postForObject(
                                    this.url + "users",
                                    messageToPost,
                                    UserBoundary.class);


        });

        String expectedMessage = "Avatar Cannot be null or empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    
    @Test
    public void testCreateNewUserWithEmptyAvatar() throws  Exception{
        // GIVEN server is running properly
        // AND I create user with invalid avatar as 'null'
        // WHEN I POST /users with a new message
        // THEN the server responds with expected excecption

        Exception exception = assertThrows(RuntimeException.class, () -> {
            UserBoundary user = new UserBoundary();
            user.setEmail("omerlewitz@gmail.com");
            user.setRole(UserRole.PLAYER);
            user.setUsername("omer");
            user.setAvatar("");

            UserBoundary  messageToPost =createPostMessageAndReturningTheMessage("users",user);

            UserBoundary messageToServer =
                    this.restTemplate
                            .postForObject(
                                    this.url + "users",
                                    messageToPost,
                                    UserBoundary.class);
        });

        String expectedMessage = "Avatar Cannot be null or empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    
    @Test
    public void testCreateNewUserWithNullUserName() throws  Exception{
        // GIVEN server is running properly
        // AND I create user with invalid UserName as 'null'
        // WHEN I POST /users with a new message
        // THEN the server responds with expected excecption

        Exception exception = assertThrows(RuntimeException.class, () -> {
            UserBoundary user = new UserBoundary();
            user.setEmail("omerlewitz@gmail.com");
            user.setRole(UserRole.PLAYER);
            user.setAvatar("address");

            UserBoundary  messageToPost =createPostMessageAndReturningTheMessage("users",user);

            UserBoundary messageToServer =
                    this.restTemplate
                            .postForObject(
                                    this.url + "users",
                                    messageToPost,
                                    UserBoundary.class);


        });

        String expectedMessage = "User Name Cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    
}
