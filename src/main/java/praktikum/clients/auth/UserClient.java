package praktikum.clients.auth;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.clients.BaseClient;
import praktikum.model.UserModel;

import static org.hamcrest.Matchers.equalTo;

public class UserClient extends BaseClient {
    private final String USER_URI = "/api/auth/user";

    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken) {
        return doDeleteRequest(USER_URI, accessToken);
    }

    @Step("Изменение данных пользователя")
    public Response changeUserData(String accessToken, String email, String name) {
        UserModel userModel = new UserModel();
        userModel.setEmail(email);
        userModel.setName(name);
        return doPatchRequest(USER_URI, accessToken, userModel);
    }

    @Step("Проверка успешного изменения данных пользователя")
    public void checkPositiveUserChanging(Response response, String email, String name) {
        response.then().assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("user.email", equalTo(email), "user.name", equalTo(name));
    }

    @Step("Проверка негативного изменения данных пользователя")
    public void checkNegativeUserChangingWithoutAuth(Response response) {
        response.then().assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}
