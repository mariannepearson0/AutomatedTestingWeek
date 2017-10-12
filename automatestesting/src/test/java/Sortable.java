import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 12/10/2017.
 */
public class Sortable {

    private WebDriver webDriver;
    private static ExtentReports report;

    @BeforeClass
    public static void init(){
        report = new ExtentReports();
        report.attachReporter(new ExtentHtmlReporter(System.getProperty("user.dir") + File.separatorChar + "MySortableReport" + ".html"));
    }

    @Before
    public void setUp(){
        webDriver = new ChromeDriver();
        webDriver.navigate().to("http://demoqa.com");
        webDriver.manage().window().maximize();
        webDriver.findElement(By.id("menu-item-151")).click();
    }

    @After
    public void tearDown(){webDriver.quit();}

    @AfterClass
    public static void cleanUp(){report.flush();}

    @Test
    public void connectLists() {
        webDriver.findElement(By.id("ui-id-2")).click();
        Actions builder = new Actions(webDriver);
        WebElement list1 = webDriver.findElement(By.id("sortable1"));
        WebElement list2 = webDriver.findElement(By.id("sortable2"));
        WebElement item2 = webDriver.findElement(By.cssSelector("#sortable1 > li:nth-child(2)"));
        builder.dragAndDrop(item2,list2).perform();
        List<WebElement> listA = list1.findElements(By.tagName("li"));
        List<WebElement> listB = list2.findElements(By.tagName("li"));
        try{
            assertTrue(listB.contains(item2));

        }catch(AssertionError e){
            Assert.fail();
        }
    }
    @Test
    public void displayAsGrid() {
        webDriver.findElement(By.id("ui-id-3")).click();
        WebElement item1 = webDriver.findElement(By.cssSelector("#sortable_grid > li:nth-child(1)"));
        WebElement item7 = webDriver.findElement(By.cssSelector("#sortable_grid > li:nth-child(7)"));
        WebElement item6 = webDriver.findElement(By.cssSelector("#sortable_grid > li:nth-child(6)"));
        System.out.println(item1.getLocation());
        Point before6 = item6.getLocation();
        Actions builder = new Actions(webDriver);
        builder.dragAndDropBy(item1, before6.getX(), before6.getY()).build().perform();
        System.out.println(item1.getLocation());
        try{
            //System.out.println(item6.getLocation());
            assertTrue(before6 != item6.getLocation());
        }catch(AssertionError e){
            Assert.fail();
        }
    }
}
