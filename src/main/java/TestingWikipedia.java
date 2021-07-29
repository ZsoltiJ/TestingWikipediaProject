import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class TestingWikipedia {


    WebDriver webdriver;

    @BeforeEach
    public void InitialTest() {

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");
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
        WebElement titleDiv = webdriver.findElement(By.xpath("//*[@id='mp-welcome']/a"));

        Assertions.assertEquals(expectedTitle, titleDiv.getText());

    }

    @Test
    public void testLoginToAccountSuccesful() {

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
    public void testLoginToAccountUnsuccesful() {

        webdriver.get("https://en.wikipedia.org/wiki/Main_Page");
        webdriver.findElement(By.xpath("//*[@id='pt-login']/a")).click();
        webdriver.findElement(By.xpath("//*[@id='wpName1']")).sendKeys("Szuperteszter");
        webdriver.findElement(By.xpath("//*[@id='wpPassword1']")).sendKeys("acbgF-");
        webdriver.findElement(By.xpath("//*[@id='wpLoginAttempt']")).click();

        String expected = "Incorrect username or password entered.";
        String actual = webdriver.findElement(By.xpath("//*[@id='userloginForm']/form/div[1]")).getText();

        Assertions.assertTrue(actual.contains(expected));

    }

    @Test
    public void testPrivacyAndPolicy() {

        testLoginToAccountSuccesful();

        webdriver.findElement(By.xpath("//*[@id='footer-places-privacy']/a")).click();
        JavascriptExecutor js = (JavascriptExecutor) webdriver;
        js.executeScript("window.scrollBy(0, 15000)");
        Assertions.assertEquals("Privacy policy", webdriver.findElement(By.xpath("//*[@id='mw-normal-catlinks']/ul/li[1]/a")).getText());


    }

    @Test
    public void testPrivacyAndPolicyLink() {

        testLoginToAccountSuccesful();

        webdriver.findElement(By.xpath("//*[@id='footer-places-privacy']/a")).click();
        Assertions.assertEquals("https://foundation.wikimedia.org/wiki/Privacy_policy", webdriver.getCurrentUrl());


    }

    @Test
    public void testDataList() {

        boolean isTestDataList = false;
        int number = 0;

        testLoginToAccountSuccesful();

        webdriver.findElement(By.xpath("//*[@id='searchInput']")).sendKeys("Ferrari");
        webdriver.findElement(By.xpath("//*[@id='searchButton']")).click();

        List<WebElement> words = webdriver.findElements(By.xpath("//*"));
        for (WebElement elements : words) {

            try {
                if (elements.getText().contains("Ferrari")) {
                    number += 1;
                    isTestDataList = true;

                }
            } catch (Exception ignored) {

            }


        }
        System.out.println("'Ferrari' is founded: " + number + " pieces.");

        Assertions.assertTrue(isTestDataList);


    }

    @Test
    public void morePagesList() {

        testLoginToAccountSuccesful();
        webdriver.findElement(By.xpath("//*[@id='searchInput']")).sendKeys("letter types");
        webdriver.findElement(By.xpath("//*[@id='searchButton']")).click();

        List<WebElement> links; //=webdriver.findElements(By.xpath("//*[@id='mw-content-text']/div[3]/ul/li"));
        webdriver.findElement(By.xpath("//*[@id='mw-content-text']/div[3]/p[2]/a[6]")).click();
        webdriver.findElement(By.xpath("//*[@id='mw-content-text']/div[3]/p[2]/a[1]")).click();
        WebDriverWait wait = new WebDriverWait(webdriver, 5);

        for (int l = 1; l <= 5; l++) {
            links = webdriver.findElements(By.xpath("//*[@id='mw-content-text']/div[3]/ul/li"));
            for (int j = 0; j < links.size(); j++) {
                WebElement link = links.get(j).findElement(By.xpath(".//a"));
                System.out.println(link.getText());
            }
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='mw-content-text']/div[3]/p[2]/a[2]")));
                webdriver.findElement(By.xpath("//*[@id='mw-content-text']/div[3]/p[2]/a[2]")).click();
            } catch (StaleElementReferenceException e) {
                e.printStackTrace();

            }

        }
        Assertions.assertEquals("Letter types", webdriver.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div[3]/p[1]/i/a[1]")).getText());
        Assertions.assertEquals("next 500", webdriver.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div[3]/p[2]/a[2]")).getText());
    }

    @Test
    public void newDataTyping() {
        testLoginToAccountSuccesful();
        webdriver.findElement(By.xpath("//*[@id='pt-sandbox']/a")).click();
        webdriver.findElement(By.xpath("//*[@id='wpTextbox1']")).sendKeys("What is Lorem Ipsum?\n" +
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. \n Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. \n It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. \n It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        Assertions.assertEquals("https://en.wikipedia.org/w/index.php?title=User:Szuperteszter/sandbox&action=edit&redlink=1&preload=Template%3AUser+sandbox%2Fpreload", webdriver.getCurrentUrl());

    }

    @Test
    public void repeatedDataTyping() throws InterruptedException {
        testLoginToAccountSuccesful();
        webdriver.findElement(By.xpath("//*[@id='pt-sandbox']/a")).click();

        for ( int i = 0; i <= 5; i++) {
            try {
                File myFile = new File("PoetAndAuthors.txt");
                Scanner scanner = new Scanner(myFile);
                while (scanner.hasNextLine()) {
                    String data = scanner.nextLine();
                    webdriver.findElement(By.xpath("//*[@id='wpTextbox1']")).sendKeys(data);

                    }

            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        try{ Thread.sleep(4000); }
        catch (InterruptedException ex){ Thread.currentThread().interrupt();}
        webdriver.findElement(By.xpath("//*[@id='wpPreview']")).click();
        String actual = webdriver.findElement(By.xpath("//*[@id='wpTextbox1']")).getText();
        Assertions.assertTrue(actual.contains("Arany János"));
    }

    @Test
    public void dataModifing() throws InterruptedException {
        testLoginToAccountSuccesful();

        String[] array = {"Ferrari", "Mercedes", "BMW", "Audi", "Renault"};
        for (int i = 0; i < 5; i++){

        webdriver.findElement(By.xpath("//*[@id='searchInput']")).sendKeys(array[i]);
        webdriver.findElement(By.xpath("//*[@id='searchButton']")).click();
        webdriver.findElement(By.xpath("//*[@id='searchInput']")).clear();

        }
        String expected = webdriver.findElement(By.xpath("//*[@id=\"firstHeading\"]")).getText();
        String actual ="Renault";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testLogout() {

        testLoginToAccountSuccesful();
        webdriver.findElement(By.cssSelector("#pt-logout > a")).click();

        Assertions.assertTrue(webdriver.findElement(By.xpath("//*[@id='pt-login']/a")).getText().contains("Log in"));

    }

    @Test
    public void deleteData() throws InterruptedException {
        testLoginToAccountSuccesful();
        webdriver.findElement(By.xpath("//*[@id='pt-sandbox']/a")).click();
        for ( int i = 0; i <= 5; i++) {
            try {
                File myFile = new File("PoetAndAuthors.txt");
                Scanner scanner = new Scanner(myFile);
                while (scanner.hasNextLine()) {
                    String data = scanner.nextLine();
                    webdriver.findElement(By.xpath("//*[@id='wpTextbox1']")).sendKeys(data);

                }

            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        Thread.sleep(3000);
        webdriver.findElement(By.xpath("//*[@id='wpTextbox1']")).clear();
        Assertions.assertEquals(webdriver.findElement(By.xpath("//*[@id='wpTextbox1']")).getText(),
                "{{User sandbox}}\n" + "<!-- EDIT BELOW THIS LINE -->");

    }



    @Test
    public void searchAndSaveToFile() {
          testLoginToAccountSuccesful();
         try {
            FileWriter myWriter = new FileWriter("MainPage.txt");
            myWriter.append(webdriver.findElement(By.id("On_this_day")).getText()+"\n");
            myWriter.append(webdriver.findElement(By.xpath("//*[@id='mp-otd']")).getText()+"\n");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
       Assertions.assertEquals("On this day", webdriver.findElement(By.id("On_this_day")).getText());

    }




    @AfterEach
    public void tearDown() {

        webdriver.quit();
    }

}


