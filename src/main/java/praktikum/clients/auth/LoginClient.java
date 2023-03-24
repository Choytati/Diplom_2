package praktikum.clients.auth;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.clients.BaseClient;
import praktikum.model.UserModel;

import static org.hamcrest.Matchers.*;

public class LoginClient extends BaseClient{
    private final String LOGIN_URI = "/api/auth/login";

    @Step("Авторизация пользователя")
    public Response loginUser(String email, String password) {
        UserModel model = new UserModel(email, password);
        return doPostRequest(LOGIN_URI, model);
    }

    @Step("Проверка успешной авторизации пользователя")
    public void checkPositiveUserAuthorization(Response response, String email, String name) {
        response.then().assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("user.email", equalTo(email), "user.name", equalTo(name))
                .and()
                .body("accessToken", startsWith("Bearer "))
                .and()
                .body("refreshToken", notNullValue(String.class));
    }

    @Step("Проверка негативной авторизации пользователя")
    public void checkNegativeUserAuthorization(Response response) {
        response.then().assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
}
