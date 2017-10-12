import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by Administrator on 10/10/2017.
 */
public class WebDriverFactory {

    public static WebDriver getWebDriver(String browser) {
        if (browser.equals("CHROME")) {
            return new ChromeDriver();
        } else {
            return new ChromeDriver();
        }
    }
}
           // case "FIREFOX":
             //   return null;
//                return new FirefoxDriver();
            //else {
            //return new ChromeDriver();
       // }
           // }


