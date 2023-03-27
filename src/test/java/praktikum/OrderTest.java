package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.clients.OrderClient;
import praktikum.clients.auth.RegisterClient;
import praktikum.clients.auth.UserClient;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@DisplayName("Заказы")
public class OrderTest {
    private OrderClient ordersAPIClient;
    private UserClient userAPIClient;
    private String accessToken;

    @Before
    public void setUp() {
        RegisterClient registerAPIClient = new RegisterClient();
        ordersAPIClient = new OrderClient();
        userAPIClient = new UserClient();
        String password = randomAlphanumeric(8, 10);
        String name = randomAlphabetic(5, 10);
        String email = name.toLowerCase() + "test@mail.ru";
        Response response = registerAPIClient.registerUser(email, password, name);
        accessToken = registerAPIClient.getAccessTokenFromResponse(response);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void positiveOrderCreatingWithAuth() {
        String[] ingredients = new String[]{
                "61c0c5a71d1f82001bdaaa77",
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f82001bdaaa75",
                "61c0c5a71d1f82001bdaaa78"
        };
        Response response = ordersAPIClient.createOrder(accessToken, ingredients);
        ordersAPIClient.checkPositiveOrderCreatingWithAuth(response);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void positiveOrderCreatingWithoutAuth() {
        String[] ingredients = new String[]{
                "61c0c5a71d1f82001bdaaa77",
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f82001bdaaa75",
                "61c0c5a71d1f82001bdaaa78"
        };
        Response response = ordersAPIClient.createOrder("", ingredients);
        ordersAPIClient.checkPositiveOrderCreatingWithoutAuth(response);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void negativeOrderCreatingWithoutIngredients() {
        Response response = ordersAPIClient.createOrder(accessToken);
        ordersAPIClient.checkNegativeOrderCreatingWithoutIngredients(response);
    }

    @Test
    @DisplayName("Создание заказа с неверным хэшем ингредиентов")
    public void negativeOrderCreatingWithWrongIngredients() {
        String[] ingredients = new String[]{
                "aaaa",
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f82001bdaaa75",
                "61c0c5a71d1f82001bdaaa78"
        };
        Response response = ordersAPIClient.createOrder(accessToken, ingredients);
        ordersAPIClient.checkNegativeOrderCreatingWithWrongIngredients(response);
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя с авторизацией")
    public void positiveUserOrdersGetting() {
        Response response = ordersAPIClient.getUserOrders(accessToken);
        ordersAPIClient.checkPositiveUserOrdersGetting(response);
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя без авторизации")
    public void negativeUserOrdersGettingWithoutAuth() {
        Response response = ordersAPIClient.getUserOrders("");
        ordersAPIClient.checkNegativeUserOrdersGettingWithoutAuth(response);
    }

    @After
    public void tearDown() {
        userAPIClient.deleteUser(accessToken);
    }
}
