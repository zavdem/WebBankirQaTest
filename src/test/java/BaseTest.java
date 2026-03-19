import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;


@ExtendWith(SeleniumExtension.class)
public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected UserApiClient apiClient;

    @BeforeEach
    void setUp() {
        String driverPath = System.getenv("CHROME_DRIVER_PATH");
        if (driverPath != null) {
            System.setProperty("webdriver.chrome.driver", driverPath);
        }
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        apiClient = new UserApiClient(TestConfig.BASE_URL);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }
}