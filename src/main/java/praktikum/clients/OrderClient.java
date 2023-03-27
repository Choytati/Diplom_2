package praktikum.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.model.IngredientsArrayModel;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class OrderClient extends BaseClient{
    private final String ORDERS_URI = "/api/orders";

    @Step("Создание заказа")
    public Response createOrder(String accessToken, String... ingredients) {
        IngredientsArrayModel ingredientsArrayModel = new IngredientsArrayModel(ingredients);
        return doPostRequest(ORDERS_URI, accessToken, ingredientsArrayModel);
    }

    @Step("Проверка успешного создания заказа")
    public void checkPositiveOrderCreatingWithoutAuth(Response response) {
        response.then().assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("name", endsWith(" бургер"))
                .and()
                .body("order.number", notNullValue(Integer.class));
    }

    @Step("Проверка успешного создания заказа")
    public void checkPositiveOrderCreatingWithAuth(Response response) {
        response.then().assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body(matchesJsonSchemaInClasspath("orderCreatingResponseShema.json"));
    }

    @Step("Проверка негативного создания заказа без ингредиентов")
    public void checkNegativeOrderCreatingWithoutIngredients(Response response) {
        response.then().assertThat()
                .statusCode(400)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Проверка негативного создания заказа с неверным хэшем ингредиентов")
    public void checkNegativeOrderCreatingWithWrongIngredients(Response response) {
        response.then().assertThat()
                .statusCode(500);
    }

    @Step("Получение заказов конкретного пользователя")
    public Response getUserOrders(String accessToken) {
        return doGetRequest(ORDERS_URI, accessToken);
    }

    @Step("Проверка успешоного получения заказов")
    public void checkPositiveUserOrdersGetting(Response response) {
        response.then().assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body(matchesJsonSchemaInClasspath("userOrdersGettingResponseSchema.json"));
    }

    @Step("Проверка негативного сценария получения заказов пользователя без авторизации")
    public void checkNegativeUserOrdersGettingWithoutAuth(Response response) {
        response.then().assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}
