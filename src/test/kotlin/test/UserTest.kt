import TestConfig.BASE_URL
import models.UserPayload
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import java.util.*

class UserTest : BaseTest() {

    val name = "AutoTest_${UUID.randomUUID()}"
    lateinit var userId: String

    @BeforeEach
    fun createUser() {
        val payload = UserPayload(name, "$name@example.com")
        val createResponse = apiClient.createUser(payload)
        assert(createResponse.statusCode == 201) { "Пользователь не создан" }
        userId = createResponse.jsonPath().getString("id")
    }

    @Test
    @DisplayName("Создание и удаление пользователя")
    fun test() {
        usersPage.open()

        usersPage.waitUserVisible(name)

        apiClient.deleteUser(userId).then().statusCode(204)

        driver.navigate().refresh()

        usersPage.waitUserInvisible(name)
    }

    @AfterEach
    fun cleanup() {
        apiClient.deleteUser(userId)
    }
}