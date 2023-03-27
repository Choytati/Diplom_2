package praktikum.clients.auth;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.clients.BaseClient;
import praktikum.model.UserModel;

import static org.hamcrest.Matchers.*;

public class RegisterClient extends BaseClient {
    private final String REGISTER_URI = "/api/auth/register";

    @Step("Регистрация пользователя по всем полям")
    public Response registerUser(String email, String password, String name) {
        UserModel model = new UserModel(email, password, name);
        return doPostRequest(REGISTER_URI, model);
    }

    @Step("Получение accessToken из тела ответа")
    public String getAccessTokenFromResponse(Response response) {
        return response.then().extract().path("accessToken");
    }

    @Step("Проверка успешной регистрации пользователя")
    public void checkPositiveUserRegistration(Response response, String email, String name) {
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

    @Step("Проверка негативной регистрации существующего пользователя")
    public void checkNegativeUserDuplicateRegistration(Response response) {
        response.then().assertThat()
                .statusCode(403)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Step("Проверка негативной регистрации пользователя без обязательного поля")
    public void checkNegativeUserRegistrationWithoutParam(Response response) {
        response.then().assertThat()
                .statusCode(403)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
