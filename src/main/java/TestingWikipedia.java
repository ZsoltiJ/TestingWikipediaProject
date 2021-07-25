import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

public class TestingWikipedia {


        WebDriver webdriver;

        @BeforeEach
        public void InitialTest() {

            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            //options.addArguments("--headless");
            webdriver = new ChromeDriver(options);
            webdriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            webdriver.manage().window().maximize();

        }

        @Test
        public void testEnglishButton() {

            webdriver.get("https://www.wikipedia.org/");
            WebElement englishButton = webdriver.findElement(By.id("js-link-box-en"));
            englishButton.click();
            String expectedTitle = "Wikipedia";
            WebElement titleDiv = webdriver.findElement(By.xpath("//*[@id=\"mp-welcome\"]/a"));
            Assertions.assertEquals(expectedTitle, titleDiv.getText());

        }

        @Test
        public void loginToAccountSuccesful() {

            webdriver.get("https://en.wikipedia.org/wiki/Main_Page");
            webdriver.findElement(By.xpath("//*[@id='pt-login']/a")).click();
            webdriver.findElement(By.xpath("//*[@id='wpName1']")).sendKeys("Szuperteszter");
            webdriver.findElement(By.xpath("//*[@id='wpPassword1']")).sendKeys("acbgF9y-");
            webdriver.findElement(By.xpath("//*[@id='wpLoginAttempt']")).click();
            String expected = "Wikipedia";
            String actual = webdriver.findElement(By.xpath("//*[@id='mp-welcome']/a")).getText();
            Assertions.assertEquals(expected, actual);
        }

    @Test
    public void loginToAccountUnsuccesful() {

        webdriver.get("https://en.wikipedia.org/wiki/Main_Page");
        webdriver.findElement(By.xpath("//*[@id='pt-login']/a")).click();
        webdriver.findElement(By.xpath("//*[@id='wpName1']")).sendKeys("Szuperteszter");
        webdriver.findElement(By.xpath("//*[@id='wpPassword1']")).sendKeys("acbgF-");
        webdriver.findElement(By.xpath("//*[@id='wpLoginAttempt']")).click();
        String expected = "Incorrect username or password entered.";
        String actual = webdriver.findElement(By.xpath("//*[@id='userloginForm']/form/div[1]")).getText();
        Assertions.assertTrue(actual.contains(expected));

    }

        @AfterEach
        public void tearDown() {
            webdriver.quit();
        }

    }

