package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

public class OwnersSearchPage extends MainPage {
    public OwnersSearchPage(WebDriver driver){
        super(driver);
    }
    @FindBy (how = How.XPATH, using = "//input[@id='lastName']")
    private WebElement ownersSearchInput;

    @FindBy(how = How.XPATH, using = "//button[@type='submit']")
    private WebElement ownersSearchFindOwnerButton;

    @FindAll(
            @FindBy (how = How.XPATH, using = "//td//a[@routerlinkactive='active']"))
    private List<WebElement> ownersSearchVisibleElementsInTable;

    public String searchForOwnerAndCheckIfExists(String firstName, String lastName){
        String fullName = "";
        waitThenSendKeys(ownersSearchInput, lastName);
        waitThenClick(ownersSearchFindOwnerButton);
        waitForAngular();

        for (WebElement element : ownersSearchVisibleElementsInTable){
            if (element.getText().equals(firstName+" "+lastName) ){
                fullName = firstName+" "+lastName;
            }
        }
        return fullName;
    }
    public OwnersInformationPage navigateToOwnersInformationPage(String firstName, String lastName){
        for (WebElement element : ownersSearchVisibleElementsInTable){
            if (element.getText().equals(firstName+" "+lastName) ){
                waitThenClick(element);
            }
        }
        return new OwnersInformationPage(driver);
    }
}
