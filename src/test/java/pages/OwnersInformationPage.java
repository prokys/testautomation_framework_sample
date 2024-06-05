package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

public class OwnersInformationPage extends MainPage{
    public OwnersInformationPage(WebDriver driver){
        super(driver);
    }

    @FindBy(how = How.XPATH, using = "//button[text()='Edit Owner']")
    private WebElement ownersInformationEditOwnerButton;

    @FindBy(how = How.XPATH, using = "//b[@class=\"ownerFullName\"]")
    private WebElement ownersInformationFullName;

    public OwnersEditPage navigateToOwnersEditPage(){
        waitThenClick(ownersInformationEditOwnerButton);
        return new OwnersEditPage(driver);
    }
    public void checkOwnersInformation(String firstName, String lastName){
        String fullName = ownersInformationFullName.getText();
        Assert.assertEquals(fullName, firstName+" "+lastName);
    }
}
