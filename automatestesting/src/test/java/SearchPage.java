import com.aventstack.extentreports.Status;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Administrator on 12/10/2017.
 */
public class SearchPage {

    @FindBy(css = "#center_column > ul")
    private WebElement searchResults;

    public List<String> iterateThrough(){
        List<WebElement> searchList = searchResults.findElements(By.cssSelector("#center_column > ul > li"));
        List<String> yesno = new ArrayList<String>();
        for (WebElement item : searchList) {
            String itemTitle = item.findElement(By.className("product-name")).getText();
            if (itemTitle.contains("Dress")) {
                yesno.add("yes");
                System.out.println("contains Dress!");
            } else {
                System.out.println("no dress");
                yesno.add("no");
            }
        }
        return yesno;
    }
}
