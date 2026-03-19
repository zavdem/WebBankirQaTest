import TestConfig.BASE_URL
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
        driver.get("$BASE_URL/admin/users")

        val userLocator = By.xpath("//td[text()='$name']")
        wait.until(ExpectedConditions.visibilityOfElementLocated(userLocator))

        val deleteResponse = apiClient.deleteUser(userId)
        assert(deleteResponse.statusCode == 204) { "Пользователь не удален" }

        driver.navigate().refresh()
        wait.until(ExpectedConditions.invisibilityOfElementLocated(userLocator))
    }

    @AfterEach
    fun cleanup() {
        apiClient.deleteUser(userId)
    }
}