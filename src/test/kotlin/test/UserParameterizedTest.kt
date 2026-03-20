import TestConfig.BASE_URL
import models.UserPayload
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import java.util.*

class UserParameterizedTest : BaseTest() {

    lateinit var userId: String

    @ParameterizedTest(name = "{0}")
    @MethodSource("userData")
    fun test(
        testName: String,
        payload: UserPayload,
    ) {
        val createResponse = apiClient.createUser(payload)
        assert(createResponse.statusCode == 201)
        userId = createResponse.jsonPath().getString("id")

        usersPage.open()

        val name = payload.name

        usersPage.waitUserVisible(name)

        apiClient.deleteUser(userId).then().statusCode(204)

        driver.navigate().refresh()

        usersPage.waitUserInvisible(name)
    }

    @AfterEach
    fun cleanup() {
        apiClient.deleteUser(userId)
    }

    companion object {
        @JvmStatic
        fun userData(): List<Array<Any>> {
            val name = "Valid_${UUID.randomUUID()}"
            return listOf(
                arrayOf("Валидный пользователь", UserPayload(name, "$name@test.com")),
                arrayOf("Пользователь с невалидным Email", UserPayload("InvalidEmail", "invalid")),
                arrayOf("Пользователь с дублирующимся username", UserPayload("DuplicateUsername", "test@test.com"))
            )
        }
    }
}