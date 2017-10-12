import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by Administrator on 12/10/2017.
 */
public class DressPage {

    @FindBy(css = "#add_to_cart > button")
    private WebElement addToCart;

    @FindBy(xpath="//*[@id=\"layer_cart\"]/div[1]/div[1]/h2")
    private WebElement success;

    public void addToCart(){
        addToCart.click();
    }

    public String getWords(){
        return success.getAttribute("innerHTML");
    }
}
