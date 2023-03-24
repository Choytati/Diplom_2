package praktikum.clients;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import praktikum.model.Model;

import static io.restassured.RestAssured.given;

public class BaseClient {
    private final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    private RequestSpecification baseSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .build();
    }

    protected final Response doGetRequest(String uri, String accessToken) {
        return given()
                .spec(baseSpec())
                .header("Authorization", accessToken)
                .get(uri);
    }

    protected final Response doPostRequest(String uri, Model body) {
        return given()
                .spec(baseSpec())
                .body(body)
                .post(uri);
    }

    protected final Response doPostRequest(String uri, String accessToken, Model body) {
        return given()
                .spec(baseSpec())
                .header("Authorization", accessToken)
                .body(body)
                .post(uri);
    }

    protected final Response doPatchRequest(String uri, String accessToken, Model body) {
        return given()
                .header("Authorization", accessToken)
                .spec(baseSpec())
                .body(body)
                .patch(uri);
    }

    protected final Response doDeleteRequest(String uri, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(baseSpec())
                .delete(uri);
    }
}
