import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

@ExtendWith(SeleniumExtension.class)
public class BaseTest {

    protected WebDriver driver;
    protected UserApiClient apiClient;

    @BeforeEach
    void setUp() {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(10));
        apiClient = UserApiClient.getInstance("http://localhost:8080");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}