package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utils.PropertiesUtils;

import java.time.Duration;

public abstract class BasePage {
    protected final WebDriver driver;
    private final int explicitWait = Integer.parseInt(PropertiesUtils.getExplicitWait());

    public BasePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, explicitWait), this);
    }
    public void waitWrapper() {

        // wait for javascript to finish (this will also provide enough time for jquery and angular to be recognized in browser)
        waitForJavaScript();

        // in case jquery is present, wait for it to finish
        waitForJquery();

        // in case angular is present, wait for it to finish
        waitForAngular();
    }
    public void waitForJquery() {

        try {

            // create a new condition
            ExpectedCondition<Boolean> condition = driver -> {
                assert driver != null;
                return (Boolean) (
                        (JavascriptExecutor) driver).executeScript("return jQuery.active==0");
            };

            // wait until condition is true
            new WebDriverWait(driver, Duration.ofSeconds(explicitWait)).until(condition);

        } catch (WebDriverException webDriverException) {
            Assert.fail("Test failed when waiting for JQuery with exception: " + webDriverException);
        }
    }

    private void waitForJavaScript() {

        try {

            // create a new condition
            ExpectedCondition<Boolean> condition = driver -> {
                assert driver != null;
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
            };

            // wait until condition is true
            new WebDriverWait(driver, Duration.ofSeconds(explicitWait)).until(condition);

        } catch (WebDriverException webDriverException) {
            Assert.fail("Test failed when waiting for Javascript with exception: " + webDriverException);
        }
    }

    public void waitForAngular() {

        String angularWait = """
            var isStable = false;
            var el = document.querySelector('app-root');
            if (window.getAngularTestability) {
                isStable = window.getAngularTestability(el).isStable();
            } else {
                console.log("Angular not present on the page...")
                isStable = true;
            }
            if (!isStable) {
                console.log("Waiting for Angular...")
            } else {
                console.log("Angular is ready for testing...")
            }
            return isStable;
            """;

        try {

            // create a new condition
            ExpectedCondition<Boolean> condition = driver -> {
                assert driver != null;
                return (Boolean)((JavascriptExecutor) driver).executeScript(angularWait);
            };

            // wait until condition is true
            new WebDriverWait(driver, Duration.ofSeconds(explicitWait)).until(condition);

        } catch (WebDriverException webDriverException) {
            Assert.fail("Test failed when waiting for Javascript with exception: " + webDriverException);
        }
    }
    public WebElement waitForElementToBeClickable(WebElement elementToWaitFor) {

        // wait for all scripts on page to finish (javascript, jquery, angular)
        waitWrapper();

        // wait until element is clickable (visible and enabled)
        return new WebDriverWait(driver, Duration.ofSeconds(explicitWait)).until(ExpectedConditions.elementToBeClickable(elementToWaitFor));
    }

    public void waitThenClick(WebElement elementToClick) {

        waitForElementToBeClickable(elementToClick).click();
    }

    public void waitThenSendKeys(WebElement elementToSendKeys, String keysToSend) {

        waitForElementToBeClickable(elementToSendKeys).clear();
        elementToSendKeys.sendKeys(keysToSend);
    }
}
