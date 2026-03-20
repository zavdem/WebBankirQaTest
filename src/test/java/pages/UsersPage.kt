package pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

class UsersPage(private val driver: WebDriver, private val wait: WebDriverWait) {

    private val url = "${TestConfig.BASE_URL}/admin/users"

    fun open() {
        driver.get(url)
    }

    private fun userRow(name: String): By = By.xpath("//td[text()='$name']")

    fun waitUserVisible(name: String) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(userRow(name)))
    }

    fun waitUserInvisible(name: String) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(userRow(name)))
    }
}