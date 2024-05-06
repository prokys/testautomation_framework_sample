package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class MainPage extends BasePage{
    public MainPage(WebDriver driver){
        super(driver);
    }
    @FindBy(how = How.XPATH, using = "(//a[@class='dropdown-toggle'])[1]")
    private WebElement navOwners;

    @FindBy(how = How.XPATH, using = "//a[@routerlink='/owners']")
    private WebElement navOwnersSearch;

    public OwnersSearchPage navigateToOwnersSearchPage(){
        waitThenClick(navOwners);
        waitThenClick(navOwnersSearch);
        return new OwnersSearchPage(driver);
    }
}
