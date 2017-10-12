import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.io.File;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by Administrator on 12/10/2017.
 */
public class Selectable {

    private WebDriver webDriver;
    private static ExtentReports report;

    @BeforeClass
    public static void init(){
        report = new ExtentReports();
        report.attachReporter(new ExtentHtmlReporter(System.getProperty("user.dir") + File.separatorChar + "MySelectableReport" + ".html"));
    }

    @Before
    public void setUp(){
        webDriver = new ChromeDriver();
        webDriver.navigate().to("http://demoqa.com");
        webDriver.manage().window().maximize();
        webDriver.findElement(By.id("menu-item-142")).click();
    }

    @After
    public void tearDown(){webDriver.quit();}

    @AfterClass
    public static void cleanUp(){report.flush();}

    @Test
    public void defaultFunctionality(){
        webDriver.findElement(By.id("ui-id-1")).click();
        WebElement item1 = webDriver.findElement(By.cssSelector("#selectable > li:nth-child(1)"));
        WebElement item6 = webDriver.findElement(By.cssSelector("#selectable > li:nth-child(6)"));
        WebElement item4 = webDriver.findElement(By.cssSelector("#selectable > li:nth-child(4)"));
        String beforeColor1 = item1.getCssValue("color");
        String beforeColor4 = item4.getCssValue("color");
        item1.click();
        String afterColor1 = item1.getCssValue("color");
        Actions builder = new Actions(webDriver);
        builder.clickAndHold(item1).release(item6).perform();
        String afterColor4 = item4.getCssValue("color");
        ExtentTest test = report.createTest("DefaultFunctionalityTest");
        try{
            assertTrue(beforeColor1 != afterColor1);
            assertTrue(beforeColor4 != afterColor4);
            test.log(Status.PASS, "The test has passed, the items can be selected when dragged.");
        } catch(AssertionError e){
            Assert.fail();
            test.log(Status.FAIL, "The test has failed because of an assertion error");
        }
    }

    @Test
    public void displayAsGrid(){
        webDriver.findElement(By.id("ui-id-2")).click();
        WebElement box1 = webDriver.findElement(By.cssSelector("#selectable_grid > li:nth-child(1)"));
        WebElement box11 = webDriver.findElement(By.cssSelector("#selectable_grid > li:nth-child(11)"));
        WebElement box6 = webDriver.findElement(By.cssSelector("#selectable_grid > li:nth-child(6)"));
        WebElement box8 = webDriver.findElement(By.cssSelector("#selectable_grid > li:nth-child(8)"));
        String beforeColor1 = box1.getCssValue("color");
        String beforeColor6 = box6.getCssValue("color");
        String beforeColor8 = box8.getCssValue("color");
        box1.click();
        String afterColor1 = box1.getCssValue("color");
        Actions builder = new Actions(webDriver);
        builder.clickAndHold(box1).release(box11).perform();
        String afterColor6 = box6.getCssValue("color");
        String afterColor8 = box8.getCssValue("color");
        ExtentTest test = report.createTest("DisplayAsGridTest");
        try{
            assertNotEquals(beforeColor1,afterColor1);
            assertNotEquals(beforeColor6,afterColor6);
            assertTrue(beforeColor8.equals(afterColor8));
            test.log(Status.PASS, "The test has passed, the items can be selected from the grid when dragged.");
        }catch(AssertionError e){
            Assert.fail();
            test.log(Status.FAIL, "The test has failed because of an assertion error");
        }
    }

    @Test
    public void serialize(){
        webDriver.findElement(By.id("ui-id-3")).click();
        WebElement item1 = webDriver.findElement(By.cssSelector("#selectable-serialize > li:nth-child(1)"));
        WebElement item3 = webDriver.findElement(By.cssSelector("#selectable-serialize > li:nth-child(3)"));
        WebElement item5 = webDriver.findElement(By.cssSelector("#selectable-serialize > li:nth-child(5)"));
        WebElement howMany = webDriver.findElement(By.cssSelector("#select-result"));
        item1.click();
        String howMany_1 = howMany.getText();
        Actions builder = new Actions(webDriver);
        builder.keyDown(Keys.CONTROL).perform();
        item3.click();
        item5.click();
        builder.keyUp(Keys.CONTROL).perform();
        String howMany_2 = howMany.getText();
        ExtentTest test = report.createTest("SerializeTest");
        try{
            assertTrue(howMany_1.equals("#1"));
            assertTrue(howMany_2.equals("#1#3#5"));
            test.log(Status.PASS, "The test has passed, the items can be selected from the list also using control");
        } catch(AssertionError e){
            Assert.fail();
            test.log(Status.FAIL, "The test has failed because of an assertion error");
        }
    }
}
