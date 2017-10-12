import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.google.common.base.Function;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.io.File;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.*;

/**
 * Created by Administrator on 12/10/2017.
 */
public class Shopping {

        private WebDriver webDriver;
        private static ExtentReports report;
        private Wait<WebDriver> wait;

        @BeforeClass
        public static void init(){
            report = new ExtentReports();
            report.attachReporter(new ExtentHtmlReporter(System.getProperty("user.dir") + File.separatorChar + "MyShoppingReport" + ".html"));
        }

        @Before
        public void setUp(){
            webDriver = new ChromeDriver();
            webDriver.navigate().to("http://automationpractice.com/index.php");
            webDriver.manage().window().maximize();
        }

        @After
        public void tearDown(){webDriver.quit();}

        @AfterClass
        public static void cleanUp(){report.flush();}

        @Test
        public void searchForItem(){
            HomePage hp = PageFactory.initElements(webDriver, HomePage.class);
            hp.searchForItem();
            hp.clickSubmit();
            SearchPage sp = PageFactory.initElements(webDriver, SearchPage.class);
            List<String> yehno = sp.iterateThrough();
            ExtentTest test = report.createTest("AcceptTest");
            try{
                assertFalse(yehno.contains("no"));
            }catch(AssertionError e){
                Assert.fail();
                test.log(Status.FAIL, "The test has failed, some items in the search do not contain the search term");
                test.log(Status.INFO, "Searching with Dress, may work for other search terms");
            }
        }

        @Test
        public void addToBasket(){
            HomePage hp = PageFactory.initElements(webDriver,HomePage.class);
            WebElement dressPic = hp.getDressPic();
            Actions builder = new Actions(webDriver);
            builder.moveToElement(dressPic).moveByOffset(0,-50).perform();
            builder.click().perform();
            DressPage dp = PageFactory.initElements(webDriver,DressPage.class);
            dp.addToCart();
            String success = dp.getWords();
            try{
                assertTrue(success.contains("successfully added"));
            }catch(AssertionError e){
                Assert.fail();
            }
        }

        @Test
        public void checkOut(){
            HomePage hp = PageFactory.initElements(webDriver,HomePage.class);
            hp.clickDress();
            webDriver.findElement(By.cssSelector("#add_to_cart > button")).click();
            //DressPopUp dpop = PageFactory.initElements(webDriver,DressPopUp.class);
            //dpop.clickcart();
            webDriver.findElement(By.cssSelector("#layer_cart > div.clearfix > div.layer_cart_cart.col-xs-12.col-md-6 > div.button-container > a")).click();
        }
    }

