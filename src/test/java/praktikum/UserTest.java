package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.clients.auth.RegisterClient;
import praktikum.clients.auth.UserClient;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class UserTest {
    private UserClient userAPIClient;
    private String email;
    private String name;
    private String accessToken;

    @Before
    public void setUp() {
        RegisterClient registerAPIClient = new RegisterClient();
        userAPIClient = new UserClient();
        String password = randomAlphanumeric(8, 10);
        name = randomAlphabetic(5, 10);
        email = name.toLowerCase() + "test@mail.ru";
        Response response = registerAPIClient.registerUser(email, password, name);
        accessToken = registerAPIClient.getAccessTokenFromResponse(response);
    }

    @Test
    @DisplayName("Изменение email пользователя с авторизацией")
    public void positiveEmailChanging() {
        String newEmail = randomAlphabetic(5, 10).toLowerCase() + "test@mail.ru";
        Response response = userAPIClient.changeUserData(accessToken, newEmail, name);
        userAPIClient.checkPositiveUserChanging(response, newEmail, name);
    }

    @Test
    @DisplayName("Изменение имени пользователя с авторизацией")
    public void positiveNameChanging() {
        String newName = randomAlphabetic(5, 10);
        Response response = userAPIClient.changeUserData(accessToken, email, newName);
        userAPIClient.checkPositiveUserChanging(response, email, newName);
    }

    @Test
    @DisplayName("Изменение email пользователя без авторизации")
    public void negativeEmailChangingWithoutAuth() {
        String newEmail = randomAlphabetic(5, 10).toLowerCase() + "test@mail.ru";
        Response response = userAPIClient.changeUserData("", newEmail, name);
        userAPIClient.checkNegativeUserChangingWithoutAuth(response);
    }

    @Test
    @DisplayName("Изменение имени пользователя без авторизации")
    public void negativeNameChangingWithoutAuth() {
        String newName = randomAlphabetic(5, 10);
        Response response = userAPIClient.changeUserData("", email, newName);
        userAPIClient.checkNegativeUserChangingWithoutAuth(response);
    }

    @After
    public void tearDown() {
        userAPIClient.deleteUser(accessToken);
    }
}
