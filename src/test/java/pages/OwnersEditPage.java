package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class OwnersEditPage extends MainPage{
    public OwnersEditPage(WebDriver driver){
        super(driver);
    }
    @FindBy(how = How.XPATH, using = "//input[@id='firstName']")
    private WebElement ownersEditFirstNameInput;

    @FindBy(how = How.XPATH, using = "//input[@id='lastName']")
    private WebElement ownersEditLastNameInput;

    @FindBy(how = How.XPATH, using = "//input[@id='address']")
    private WebElement ownersEditAddressInput;

    @FindBy(how = How.XPATH, using = "//input[@id='city']")
    private WebElement ownersEditCityInput;

    @FindBy(how = How.XPATH, using = "//input[@id='telephone']")
    private WebElement ownersEditTelephoneInput;

    @FindBy(how = How.XPATH, using = "//button[@type='submit']")
    private WebElement ownersEditUpdateOwnerButton;

    public void editOwner (String firstName, String lastName, String address, String city, int telephone){
        waitThenSendKeys(ownersEditFirstNameInput, firstName);
        waitThenSendKeys(ownersEditLastNameInput, lastName);
        waitThenSendKeys(ownersEditAddressInput, address);
        waitThenSendKeys(ownersEditCityInput, city);
        waitThenSendKeys(ownersEditTelephoneInput, String.valueOf(telephone));
        waitThenClick(ownersEditUpdateOwnerButton);
    }
}
