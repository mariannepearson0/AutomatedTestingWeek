
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class WebDriverExample {

    private WebDriver webDriver;
    private ExtentHtmlReporter extentHtmlReporter;
    private static ExtentReports report;
    private Workbook workbook;

    @BeforeClass
    public static void init(){
        report = new ExtentReports();
        report.attachReporter(new ExtentHtmlReporter(System.getProperty("user.dir") + File.separatorChar + "MyReport" + ".html"));
    }

    @Before
    public void setUp(){
        spreadsheetReader("C:\\Users\\Administrator\\Documents\\AutomatedTestingWeek\\InputData.xlsx");
        List<String> reading = readWorksheetRow(1,"Sheet1");
        WebDriverFactory chooseWebDrive = new WebDriverFactory();
        chooseWebDrive.getWebDriver(reading.get(2));
        webDriver.manage().window().maximize();
    }

    @After
    public void tearDown(){
        webDriver.quit();
    }

    @AfterClass
    public static void cleanUp(){
        report.flush();
    }

    @Test
    public void myFirstTestReportTest(){
        ExtentTest test = report.createTest("MyFirstTest");
        test.log(Status.INFO, "My First Test is starting");
    }

    public String takeScreenshot(WebDriver webDriver, String fileName) throws IOException {
        File scrFile = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);
        String pathname = System.getProperty("user.dir") + File.separatorChar + "MyScreenshot" + ".jpg";
        FileUtils.copyFile(scrFile, new File(pathname));
        return pathname;
    }

    //method to take an input spreadsheet and read it.
    public void spreadsheetReader(String fileName){
        try {
            FileInputStream input = new FileInputStream(new File(fileName));
            workbook = new XSSFWorkbook(input);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    //Method to read row from the worksheet one at a time.
    public List<String> readWorksheetRow(int rowNo, String sheetname){
        List<String> row = new ArrayList<String>();
        Sheet dataTypeSheet = workbook.getSheet(sheetname);
        Row currentRow = dataTypeSheet.getRow(rowNo);
        for (Cell currentCell : currentRow){
            switch(currentCell.getCellTypeEnum()){
                case STRING:
                    row.add(currentCell.getStringCellValue());
                    break;
                case NUMERIC:
                    row.add(String.valueOf(currentCell.getNumericCellValue()));
                    break;
                case BOOLEAN:
                    row.add(String.valueOf(currentCell.getBooleanCellValue()));
                    break;
                case BLANK:
                    row.add(currentCell.getStringCellValue());
                    break;
                case _NONE:
                    System.out.println("No value in cell");
                    break;
                case ERROR:
                    System.out.println("Error in cell");
                    break;
                case FORMULA:
                    row.add(currentCell.getCellFormula());
                    break;
            }
        }
        return row;
    }

    @Test
    public void qaTest(){
        webDriver.navigate().to("http://thedemosite.co.uk");
        webDriver.findElement(By.cssSelector("body > div:nth-child(1) > center:nth-child(1) > table:nth-child(6) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(1) > div:nth-child(2) > center:nth-child(1) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2) > p:nth-child(1) > small:nth-child(1) > a:nth-child(6)")).click();
        //create a spreadsheet reader to read the excel spreadsheet containing log in info
        spreadsheetReader("C:\\Users\\Administrator\\Documents\\AutomatedTestingWeek\\InputData.xlsx");
        //List of values from the row that was read
        List<String> reading = readWorksheetRow(1,"Sheet1");
        //Getting the first value from that row
        webDriver.findElement(By.name("username")).sendKeys(reading.get(0));
        webDriver.findElement(By.name("password")).sendKeys(reading.get(1));
        WebElement saveButton = webDriver.findElement(By.name("FormsButton2"));
        saveButton.click();
        WebElement loginButton = webDriver.findElement(By.cssSelector("body > div > center > table > tbody > tr:nth-child(2) > td > div > center > table > tbody > tr > td:nth-child(2) > p > small > a:nth-child(7)"));
        loginButton.click();
        webDriver.findElement(By.name("username")).sendKeys(reading.get(0));
        webDriver.findElement(By.name("password")).sendKeys(reading.get(1));
        webDriver.findElement(By.name("FormsButton2")).click();
        String success = webDriver.findElement(By.cssSelector("body > table > tbody > tr > td.auto-style1 > big > blockquote > blockquote > font > center > b")).getText();
        ExtentTest test = report.createTest("qaTest");
        test.log(Status.INFO, "Test to go onto qa website, provide a username and password and assert whether this has worked");
        try {
            String myScreenshot = takeScreenshot(webDriver, "MyScreenshot");
            test.addScreenCaptureFromPath(myScreenshot);
            List<String> reading2 = readWorksheetRow(1,"Sheet2");
            assertTrue(success.equalsIgnoreCase(reading2.get(0)));
            test.log(Status.PASS, "The test has passed");
        } catch (AssertionError e){
            test.log(Status.FAIL, "The test has failed because of assertion error");
        } catch (IOException e) {
            e.printStackTrace();
            test.log(Status.FAIL, "The test has failed because of IOException");
        }
    }
}
