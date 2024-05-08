package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class OwnersInformationPage extends MainPage{
    public OwnersInformationPage(WebDriver driver){
        super(driver);
    }
    @FindBy(how = How.XPATH, using = "//button[text()='Edit Owner']")
    private WebElement ownersInformationEditOwnerButton;

    public OwnersEditPage navigateToOwnersEditPage(){
        waitThenClick(ownersInformationEditOwnerButton);
        return new OwnersEditPage(driver);
    }
}
