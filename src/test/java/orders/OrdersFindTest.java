package orders;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import setup.SetUpTests;

import static client.OrdersClient.getFindMessage;
import static org.hamcrest.Matchers.is;

@DisplayName("Поиск заказа")
public class OrdersFindTest extends SetUpTests {

    @Before
    @Step("Создание тестового заказа")
    public void setUp() {
        setUpOrder();
    }

    @Test
    @DisplayName("Можно найти нужный заказ по трекинговому номеру")
    @Description("Ожидаемый код ответа: 200")
    public void shouldFindOrderWithId() {

        expectedStatusCode = 200;

        ordersClient.getOrder(track)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("order.track", is(track));
    }

    @Test
    @DisplayName("Запрос без номера заказа возвращает ошибку")
    @Description("Ожидаемый код ответа: 400")
    public void shouldNotFindOrderWithoutId() {

        expectedStatusCode = 400;

        ordersClient.getOrder()
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(getFindMessage(expectedStatusCode)));

    }

    @Test
    @DisplayName("Запрос с несуществующим заказом возвращает ошибку")
    @Description("Ожидаемый код ответа: 404")
    public void shouldNotFindNotExistingOrder() {

        expectedStatusCode = 404;

        ordersClient.cancelOrder(track);

        ordersClient.getOrder(track)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(getFindMessage(expectedStatusCode)));

    }

    @After
    @DisplayName("Удаление тестовых заказов")
    public void cleanUp() {
        if (expectedStatusCode != 404)
            cleanUpOrder();
    }
}
