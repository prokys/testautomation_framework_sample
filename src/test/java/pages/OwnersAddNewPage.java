package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class OwnersAddNewPage extends MainPage{
    public OwnersAddNewPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(how = How.XPATH, using = "//input[@id='firstName']")
    private WebElement ownersAddNewFirstNameInput;

    @FindBy(how = How.XPATH, using = "//input[@id='lastName']")
    private WebElement ownersAddNewLastNameInput;

    @FindBy(how = How.XPATH, using = "//input[@id='address']")
    private WebElement ownersAddNewAddressInput;

    @FindBy(how = How.XPATH, using = "//input[@id='city']")
    private WebElement ownersAddNewCityInput;

    @FindBy(how = How.XPATH, using = "//input[@id='telephone']")
    private WebElement ownersAddNewTelephoneInput;

    @FindBy(how = How.XPATH, using = "//button[@type='submit']")
    private WebElement ownersAddNewAddOwnerButton;

    public OwnersSearchPage addNewOwner (String firstName, String lastName, String address, String city, int telephone){
        waitThenSendKeys(ownersAddNewFirstNameInput, firstName);
        waitThenSendKeys(ownersAddNewLastNameInput, lastName);
        waitThenSendKeys(ownersAddNewAddressInput, address);
        waitThenSendKeys(ownersAddNewCityInput, city);
        waitThenSendKeys(ownersAddNewTelephoneInput, String.valueOf(telephone));
        waitThenClick(ownersAddNewAddOwnerButton);
        return new OwnersSearchPage(driver);
    }
}
