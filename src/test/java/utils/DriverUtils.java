package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;

public class DriverUtils {
    private static WebDriver driver;

    public static WebDriver initDriver(String browserName) {



        switch (browserName) {
            case "edge" -> driver = initEdgeDriver();
            case "chrome" -> driver = initChromeDriver();
            case "firefox" -> driver = initFirefoxDriver();
            default -> {
                driver = initChromeDriver();
            }
        }
        return driver;
    }

    private static WebDriver initChromeDriver(){

        // chrome options if needed
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("start-maximized");

        try {
            driver = new ChromeDriver(chromeOptions);

        } catch (Exception e ) {
            Assert.fail("[ERROR] An error occurred while trying to initialize new driver instance: " + e);
        }

        return driver;
    }
    private static WebDriver initEdgeDriver(){

        // edge options if needed
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments("start-maximized");

        try {
            driver = new EdgeDriver(edgeOptions);

        } catch (Exception e ) {
            Assert.fail("[ERROR] An error occurred while trying to initialize new driver instance: " + e);
        }

        return driver;
    }

    private static WebDriver initFirefoxDriver(){

        // firefox options if needed
        FirefoxOptions firefoxOptions = new FirefoxOptions();

        try {
            driver = new FirefoxDriver(firefoxOptions);
            driver.manage().window().maximize();

        } catch (Exception e ) {
            Assert.fail("[ERROR] An error occurred while trying to initialize new driver instance: " + e);
        }

        return driver;
    }

    public static void quitDriver() {

        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                Assert.fail("[ERROR] An error occurred while trying to close the driver: " + e);
            }
        }
    }
}
