import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by Administrator on 12/10/2017.
 */
public class DressPopUp {

    @FindBy(css="#add_to_cart > button")
    private WebElement add2cart;

    public void clickcart(){
        add2cart.click();
    }
}
