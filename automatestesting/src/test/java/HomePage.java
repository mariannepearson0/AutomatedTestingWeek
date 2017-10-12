import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 * Created by Administrator on 12/10/2017.
 */
public class HomePage {

    @FindBy(css="#search_query_top")
    private WebElement searchBar;

    @FindBy(css="#searchbox > button")
    private WebElement searchButton;

    @FindBy(xpath = "//*[@id=\"homefeatured\"]/li[4]/div/div[1]/div/a[1]/img")
    private WebElement dressPic;

    public void searchForItem(){
        searchBar.sendKeys("Dress");
    }

    public void clickSubmit(){
        searchButton.click();
    }

    public WebElement getDressPic(){
        return dressPic;
    }

    public void clickDress(){
        dressPic.click();
    }
}
