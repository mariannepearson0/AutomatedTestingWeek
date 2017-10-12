import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.awt.*;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by Administrator on 11/10/2017.
 */
public class Draggable {

    private WebDriver webDriver;
    private static ExtentReports report;

    @BeforeClass
    public static void init(){
        report = new ExtentReports();
        report.attachReporter(new ExtentHtmlReporter(System.getProperty("user.dir") + File.separatorChar + "MyDraggableReport" + ".html"));
    }

    @Before
    public void setUp(){
        webDriver = new ChromeDriver();
        webDriver.navigate().to("http://demoqa.com");
        webDriver.manage().window().maximize();
        webDriver.findElement(By.id("menu-item-140")).click();
    }

    @After
    public void tearDown(){webDriver.quit();}

    @AfterClass
    public static void cleanUp(){report.flush();}

    public String takeScreenshot(WebDriver webDriver, String fileName) throws IOException {
        File scrFile = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);
        String pathname = System.getProperty("user.dir") + File.separatorChar + "MyScreenshot" + ".jpg";
        FileUtils.copyFile(scrFile, new File(pathname));
        return pathname;
    }

    @Test
    public void defaultFunctionality() {
        WebElement dragBox = webDriver.findElement(By.id("draggable"));
        int beforeX = dragBox.getLocation().getX();
        int beforeY = dragBox.getLocation().getY();
        Actions builder = new Actions(webDriver);
        builder.dragAndDropBy(dragBox, 300,200).perform();
        ExtentTest test = report.createTest("defaultFunctionalityTest");
        try {
            assertTrue(dragBox.getLocation().getX() == (beforeX + 300));
            assertTrue(dragBox.getLocation().getY() == (beforeY + 200));
            test.log(Status.PASS, "The test has passed, box has been dragged to expected area");
        }
        catch(AssertionError e) {
            Assert.fail();
            test.log(Status.FAIL, "The test has failed because of assertion error");
            try {
                String myScreenshot = takeScreenshot(webDriver, "MyScreenshot");
                test.addScreenCaptureFromPath(myScreenshot);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Test
    public void constraintAxis(){
        webDriver.findElement(By.id("ui-id-2")).click();
        WebElement verticalDrag = webDriver.findElement(By.id("draggabl"));
        int beforevertX = verticalDrag.getLocation().getX();
        int beforevertY = verticalDrag.getLocation().getY();
        WebElement horizDrag = webDriver.findElement(By.id("draggabl2"));
        int beforehorizX = horizDrag.getLocation().getX();
        int beforehorizY = horizDrag.getLocation().getY();
        Actions builder = new Actions(webDriver);
        builder.dragAndDropBy(verticalDrag, 100,100).perform();
        builder.dragAndDropBy(horizDrag, 100,100).perform();
        ExtentTest test = report.createTest("constraintAxisTest");
        try{
            assertTrue(verticalDrag.getLocation().getX() == beforevertX);
            assertTrue(verticalDrag.getLocation().getY() == (beforevertY + 100));
            assertTrue(horizDrag.getLocation().getX() == (beforehorizX + 100));
            assertTrue(horizDrag.getLocation().getY() == beforehorizY);
            test.log(Status.PASS, "The test has passed, the horizontal is only moving horizontally and the vertically is only moving vertically");
        }
        catch (AssertionError e){
            Assert.fail();
            try {
                String myScreenshot = takeScreenshot(webDriver, "MyScreenshot");
                test.addScreenCaptureFromPath(myScreenshot);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    @Test
    public void constrainBox(){
        webDriver.findElement(By.id("ui-id-2")).click();
        Actions builder = new Actions(webDriver);
        WebElement bigBox = webDriver.findElement(By.id("containment-wrapper"));
        int bigboxHeight = bigBox.getSize().getHeight();
        int bigboxWidth = bigBox.getSize().getWidth();
        builder.moveByOffset((bigBox.getLocation().getX()), bigBox.getLocation().getY());
        WebElement whiteBox = webDriver.findElement(By.id("draggabl3"));
        builder.dragAndDropBy(whiteBox, bigboxWidth, bigboxHeight).perform();
        ExtentTest test = report.createTest("constraintBoxTest");
        try{
            assertFalse(whiteBox.getLocation().getX() == bigboxWidth);
            assertFalse(whiteBox.getLocation().getY() == bigboxHeight);
            test.log(Status.PASS, "The test has passed the boxes cannot leave the big box");

        } catch(AssertionError e){
            Assert.fail();
        }
    }

    @Test
    public void events(){
        webDriver.findElement(By.id("ui-id-4")).click();
        WebElement MainBox = webDriver.findElement(By.id("dragevent"));
        WebElement startCount = webDriver.findElement(By.cssSelector("#event-start > span.count"));
        String dragCount = webDriver.findElement(By.cssSelector("#event-drag > span.count")).getText();
        WebElement stopCount = webDriver.findElement(By.cssSelector("#event-stop > span.count"));
        Actions builder = new Actions(webDriver);
        builder.dragAndDropBy(MainBox,3,3).perform();
        String dragAfter = webDriver.findElement(By.cssSelector("#event-drag > span.count")).getText();
        ExtentTest test = report.createTest("EventsTest");
        try{
            assertTrue(startCount.getText().equals("1"));
            assertTrue(Integer.parseInt(dragCount) < Integer.parseInt(dragAfter));
            assertTrue(stopCount.getText().equals("1"));
            test.log(Status.PASS, "The test has passed, the events are occuring as expected and counts are increasing");
        }
        catch(AssertionError e){
            Assert.fail();
            test.log(Status.FAIL, "The test has failed because of an assertion error");
        }
    }

    @Test
    public void draggableAndSortable(){
        webDriver.findElement(By.id("ui-id-5")).click();
        WebElement listBefore = webDriver.findElement(By.id("sortablebox"));
        List<WebElement> listA = listBefore.findElements(By.tagName("li"));
        WebElement item1 = webDriver.findElement(By.cssSelector("#sortablebox > li:nth-child(1)"));
        Actions builder = new Actions(webDriver);
        builder.dragAndDropBy(item1, 0,200).perform();
        WebElement listAfter = webDriver.findElement(By.id("sortablebox"));
        List<WebElement> listB = listAfter.findElements(By.tagName("li"));
        ExtentTest test = report.createTest("DragAndSortTest");
        try{
            assertTrue(listA.get(1) != listB.get(1));
            test.log(Status.PASS, "The test has passed, the list is being resorted when dragged");
        } catch(AssertionError e){
            Assert.fail();
            test.log(Status.FAIL, "The test has failed because of an assertion error");
        }
    }
}
