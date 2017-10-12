import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Color;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 11/10/2017.
 */
public class Droppable {

    private WebDriver webDriver;
    private static ExtentReports report;

    @BeforeClass
    public static void init(){
        report = new ExtentReports();
        report.attachReporter(new ExtentHtmlReporter(System.getProperty("user.dir") + File.separatorChar + "MyDroppableReport" + ".html"));
    }

    @Before
    public void setUp(){
        webDriver = new ChromeDriver();
        webDriver.navigate().to("http://demoqa.com");
        webDriver.manage().window().maximize();
        webDriver.findElement(By.id("menu-item-141")).click();
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
    public void defaultFunctionality(){
        webDriver.findElement(By.id("ui-id-1")).click();
        WebElement dragBox = webDriver.findElement(By.id("draggableview"));
        Actions builder = new Actions(webDriver);
        builder.dragAndDropBy(dragBox, 100,0).perform();
        String droppedText = webDriver.findElement(By.cssSelector("#droppableview > p")).getText();
        ExtentTest test = report.createTest("defaultFunctionalityTest");
        try{
            assertTrue(droppedText.equals("Dropped!"));
            test.log(Status.PASS, "The test has passed, the box was successfully dropped into the larger box!");
        } catch(AssertionError e){
            Assert.fail();
            test.log(Status.FAIL, "The test has failed because of an assertion error");
        }
    }

    @Test
    public void accept(){
        webDriver.findElement(By.id("ui-id-2")).click();
        WebElement nondropBox = webDriver.findElement(By.id("draggable-nonvalid"));
        WebElement dropBox = webDriver.findElement(By.id("draggableaccept"));
        WebElement dropText = webDriver.findElement(By.cssSelector("#droppableaccept > p"));
        Actions builder = new Actions(webDriver);
        builder.dragAndDropBy(nondropBox,250,0).perform();
        ExtentTest test = report.createTest("AcceptTest");
        try{
            assertTrue(dropText.getText().equals("accept: ‘#draggableaccept’"));
        } catch(AssertionError e){
            Assert.fail();
        }
        Actions builder2 = new Actions(webDriver);
        builder2.dragAndDropBy(dropBox,130,10).perform();
        try{
            assertTrue(dropText.getText().equals("Dropped!"));
        } catch(AssertionError e){
            Assert.fail();
        }
    }

    @Test
    public void preventPropagation(){
        webDriver.findElement(By.id("ui-id-3")).click();
        WebElement dragProp = webDriver.findElement(By.id("draggableprop"));
        WebElement greedyInDrop = webDriver.findElement(By.id("droppable-inner"));
        WebElement greedyOutDrop = webDriver.findElement(By.id("droppableprop"));
        JavascriptExecutor js = null;
//        if (webDriver instanceof JavascriptExecutor) {
//            js = (JavascriptExecutor)webDriver;
//        }
//        String beforeColor = String.valueOf(js.executeScript("document.getElementById(droppableprop).style.backgroundColor"));

        String beforeColor = greedyOutDrop.getCssValue("color");
        Actions builder = new Actions(webDriver);
        builder.dragAndDrop(dragProp, greedyInDrop).perform();
        String afterColor = greedyOutDrop.getCssValue("color");
        try {
            assertFalse(beforeColor.equalsIgnoreCase(afterColor));
        }catch(AssertionError e){
            Assert.fail();
        }
        WebElement nongreedyInDrop = webDriver.findElement(By.id("droppable2-inner"));
        WebElement nongreedyOutDrop = webDriver.findElement(By.id("droppableprop2"));
        String beforeColor2 = nongreedyOutDrop.getCssValue("color");
        //Actions builder = new Actions(webDriver);
        builder.dragAndDrop(dragProp, nongreedyInDrop).perform();
        String afterColor2 = nongreedyOutDrop.getCssValue("color");
        try{
            assertTrue(beforeColor2.equalsIgnoreCase(afterColor2));
        }catch(AssertionError e){
            Assert.fail();
        }
    }

    @Test
    public void revertDraggablePosition(){
        webDriver.findElement(By.id("ui-id-4")).click();
        WebElement dropBox = webDriver.findElement(By.id("droppablerevert"));
        WebElement revertDrop = webDriver.findElement(By.id("draggablerevert"));
        Point locationbeforemove = revertDrop.getLocation();
        //WebElement revertNotDrop = webDriver.findElement(By.id("draggablerevert2"));
        Actions builder = new Actions(webDriver);
        builder.dragAndDropBy(revertDrop, 0,200).perform();
        Point locationaftermove = revertDrop.getLocation();
        builder.dragAndDrop(revertDrop, dropBox).perform();
        Point locationafter2move = revertDrop.getLocation();
        try{
            assertTrue(locationbeforemove != locationaftermove);
            assertTrue(locationaftermove == locationafter2move);
        }catch(AssertionError e){
            Assert.fail();
        }


    }
}
