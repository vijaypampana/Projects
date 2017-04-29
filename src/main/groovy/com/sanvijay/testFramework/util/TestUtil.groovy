package com.sanvijay.testFramework.util

import com.relevantcodes.extentreports.ExtentReports
import com.relevantcodes.extentreports.ExtentTest
import com.relevantcodes.extentreports.LogStatus
import com.sanvijay.testFramework.config.testConfig
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.openqa.selenium.By
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
import java.sql.Timestamp
import org.slf4j.*

import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * Created by vijaypampana on 4/16/2017.
 * Test Framework
 */
class TestUtil {

    final static Logger loggerS4lj = LoggerFactory.getLogger(TestUtil.class)
    def driver, jsonObject, filePath, fos, fis, df = new DataFormatter(), waitTime = 30
    Workbook WB
    Sheet sh
    Row row
    static ExtentReports extentReports
    static ExtentTest extentTest
    def con = new testConfig()

    //This method will return the Timestamp YYYY-MM-DD HH:MM:SS
    def Timestamp tstamp() {
        Date date = new Date()
        return (new Timestamp(date.getTime()))
    }

    //This method will return date in YYY-MM-DD
    def String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd")
        Date date = new Date()
        dateFormat.format(date)
    }

    //This method will return time in HH-MM-SS
    def String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH-mm-ss")
        Date date = new Date()
        dateFormat.format(date)
    }

    //This method will return the List of files in a folder path
    def File[] getListofFiles(String filePath) {
        File folder = new File(this.getClass().getResource(filePath).getFile())
        folder.listFiles()
    }

    //This method is to pass environment parameters
    def List<String> getEnvParams(String env) {
        List<String> urls = new ArrayList<>()
        if(env.equalsIgnoreCase("dev")) {
            urls.add(0,"dev")
            urls.add(1,"devMongo")
        } else if(env.equalsIgnoreCase("system")) {
            urls.add(0,"system")
            urls.add(1,"sysMongo")
        }

        urls
    }

    //************Error Handling Methods
    //Method to exit and add the error code and msg to the Logs
    def eHandler(int i, String msg) {

        loggerS4lj.debug("Encounter a Error, hence Quitting!")
        loggerS4lj.debug("Error codes is $i")
        loggerS4lj.debug(msg)
        exit()

    }

    //Method to exit and add the error code and msg to the Logs and close the WB
    def eHandler(int i, String msg, Workbook WB) {

        loggerS4lj.debug("Encounter a Error, hence Quitting!")
        loggerS4lj.debug("Error codes is $i")
        loggerS4lj.debug(msg)
        exit(WB)

    }


    //***********EXIT Methods
    //This method will close the browser and WB if any
    def exit(Workbook WB) {
        if(driver) {
            driver.close()
            driver.quit()
        }

        if(fis) {
            fis.close()
            fos = new FileOutputStream(new File(filePath))
            WB.write(fos)
            fos.close()
        }
    }

    //This method will close the browser if any
    def exit() {
        if (driver) {
            driver.close()
            driver.quit()
        }
    }

    //This method will generate ssn
    def String genSSN() {
        ((new SimpleDateFormat("MMddHHmmss")).format(tstamp())).substring(1)
    }

    //This method will return driver file
    //Chrome Driver when pass CH
    //Firefox Driver when pass FF
    //IE Driver when pass IE
    def WebDriver loadDriver(String d) {
        DesiredCapabilities caps

        if(d.equalsIgnoreCase("CH")) {
            caps = DesiredCapabilities.chrome()
            caps.setCapability("chrome.switches", Arrays.asList("--incognito"))
            System.setProperty("webdriver.chrome.driver", con.chromeDriverPath)
            driver = new ChromeDriver(caps)
            loggerS4lj.debug("Opening the chrome driver")
        } else if(d.equalsIgnoreCase("FF")) {
            System.setProperty("webdriver.gecko.driver", con.firefoxDriverPath)
            /*ProfilesIni profile = new ProfilesIni()
            FirefoxProfile myprofile = profile.getProfile("default")
            driver = new FirefoxDriver(myprofile)*/
            driver = new FirefoxDriver()
            loggerS4lj.debug("Opening the FireFox driver")
        } else if(d.equalsIgnoreCase("IE")) {
            System.setProperty("webdriver.ie.driver", con.IEDriverPath)
            driver = new InternetExplorerDriver()
            loggerS4lj.debug("Opening the IE Browser")
        }


        driver.manage().window().maximize()
        driver
    }

    //This code is used to choose a file by user and return the filePath
    def String fileChoose() {

        JFileChooser fileChooser = new JFileChooser(dialogTitle: "Choose an Excel File")
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")))
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Excel Files", "xls", "xlsx"
        )
        fileChooser.setFileFilter(filter)

        int result = fileChooser.showOpenDialog()
        if(result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile()
            loggerS4lj.debug("Selected file is : " + selectedFile.getAbsolutePath())
            return selectedFile.getAbsolutePath()
        } else {
            return ""
        }

    }

    //This method will get the filename if not availabel will choose the fileChoose
    def void getFileName(String s) {
        String fileName
        if((s?.trim().size()>0)) {
            fileName = s
        } else {
            fileName = fileChoose()
        }

        fileName
    }

    //This method will send the first element find
    def WebElement findElement(String searchType, String searchString) {
        //findElements(searchType,searchString).get(0)
        //The above line is sufficient but to improve performance below code is written
        WebDriverWait wait = new WebDriverWait(driver, waitTime)
        WebElement element
        switch (searchType) {
            case "id":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(searchString)))
                element = driver.findElement(By.id(searchString))
                break

            case "name":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(searchString)))
                element = driver.findElement(By.name(searchString))
                break

            case "class":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(searchString)))
                element = driver.findElement(By.className(searchString))
                break

            case "css":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(searchString)))
                element = driver.findElement(By.cssSelector(searchString))
                break

            case "linkText":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(searchString)))
                element = driver.findElement(By.linkText(searchString))
                break

            case "partialLinkText":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(searchString)))
                element = driver.findElement(By.partialLinkText(searchString))
                break

            case "xpath":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(searchString)))
                element = driver.findElement(By.xpath(searchString))
                break

            default:
                loggerS4lj.debug("Incorrect searchType is provided, hence skipped : $searchType" )
        }

        element

    }

    //This method will find all the WebElement which match the search string for a searchType
    def List<WebElement> findElements(String searchType, String searchString) {

        WebDriverWait wait = new WebDriverWait(driver, waitTime)
        List<WebElement> elements = new ArrayList<WebElement>()
        switch (searchType) {
            case "id":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(searchString)))
                elements = driver.findElements(By.id(searchString))
                break

            case "name":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(searchString)))
                elements = driver.findElements(By.name(searchString))
                break

            case "class":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(searchString)))
                elements = driver.findElements(By.className(searchString))
                break

            case "css":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(searchString)))
                elements = driver.findElements(By.cssSelector(searchString))
                break

            case "linkText":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(searchString)))
                elements = driver.findElements(By.linkText(searchString))
                break

            case "partialLinkText":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(searchString)))
                elements = driver.findElements(By.partialLinkText(searchString))
                break

            case "xpath":
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(searchString)))
                elements = driver.findElements(By.xpath(searchString))
                break
        }

        elements

    }

    //This method will sleep the application
    def sleep(int sec) throws Exception {
        Thread.sleep(sec)
    }

    //***********Excel Utilities****************************
    //This method will read a value from Excel, input values are Sheet, Row number, column Number
    def String readCell (Sheet sh, int i, int j) {

        try {
            return df.formatCellValue(sh.getRow(i).getCell(j).toString().trim())
        } catch (Exception e) {
            loggerS4lj.debug("Encounter a Exception while reading the Row $i and Column $j in the sheet $sh.sheetName : $e")
        }

    }

    //This method will read a value from Excel, input values are Row, column Number
    def String readCell (Row row, int j) {

        try {
            return df.formatCellValue(row.getCell(j).toString().trim())
        } catch (Exception e) {
            loggerS4lj.debug("Encounter a Exception while reading the Row $row.rowNum and Column $j : $e")
        }

    }

    //This method will read a value from Excel, input values are Row number, column Number, Sheet value is taken from static variable
    def String readCell (int i, int j) {

        try {
            return df.formatCellValue(sh.getRow(i).getCell(j).toString().trim())
        } catch (Exception e) {
            loggerS4lj.debug("Encounter a Exception while reading the Row $i and Column $j in the sheet $sh.sheetName : $e")
        }

    }

    //This method will return the Workbook based on FilePath
    def Workbook openExcel(String filePath) {
        WB = null
        try {
            fis = new FileInputStream(new File(filePath))
            String fileExt = FilenameUtils.getExtension(filePath)

            if(fileExt.equalsIgnoreCase("xls")) {
                WB = new HSSFWorkbook(fis)
            } else if (fileExt.equalsIgnoreCase("xlsx")) {
                WB = new XSSFWorkbook(fis)
            } else {
                loggerS4lj.debug("The file at the path $filePath is not a excel file")
            }

        } catch (Exception e) {
            loggerS4lj.debug("Encounter a Exception while reading a workbook at the filePath $filePath : $e")
        }
        WB
    }

    //This method will give the FileExtension
    def String getFileExtension(String filePath) {
        FilenameUtils.getExtension(filePath)
    }

    //This method will give the sheet using the sheetName
    def Sheet getSheet(String sheetName) {
        try {
            sh = WB.getSheet(sheetName)
        } catch (Exception e) {
            loggerS4lj.debug("Encounter a Exception while reading a sheet with Name $sheetName : $e")
        }
        sh
    }

    //This method will give the sheet using the sheetNumber
    def Sheet getSheet(int i) {
        try {
            sh = WB.getSheetAt(i)
        } catch (Exception e) {
            loggerS4lj.debug("Encounter a Exception while reading a sheet at $i : $e")
        }
        sh
    }

    //This method will give Row, sheet is taken from earlier
    def Row getRow(int i) {
        row = null
        row = sh.getRow(i)
        row
    }

    //This method will setValue
    def void setValue(String str, int i, int j) {
        row = sh.getRow(i)
        Cell cell = row.getCell(j)
        if(cell.cellType != cell.CELL_TYPE_STRING) {
            cell.setCellType(Cell.CELL_TYPE_STRING)
        }

        cell.setCellValue(str)

    }


    //*******ExtentReport Utilities**********
    //This method will taken the screen and save the file as Jpg
    def String takeScreenShot(WebDriver driver) {
        File screenShotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE)
        String temp = con.screenshotPath + getDate() + "/ScreenShot/" + getTime() + ".jpg"
        File fileTemp = new File(temp)
        FileUtils.copyFile(screenShotFile, fileTemp)
        fileTemp.getAbsolutePath()
    }

    //This mthod will get report HTML Path
    def String getReportHTMLPath() {
        con.reportHtmlPath + con.reportHtmlFileName
    }

    //This method will get Report replaceSw
    def boolean getReportReplaceHTMLSW() {
        con.reportReplaceHtmlSw
    }

    //This method will get the Report config path
    def String getReportConfigPath() {
        con.reportConfigPath + con.reportConfigFileName
    }

    //This method will initiate the extent Report
    def void logSetup() {
        extentReports = new ExtentReports(getReportHTMLPath(),getReportReplaceHTMLSW())
        extentReports.loadConfig(new File(getReportConfigPath()))
    }

    //This method will add System parameters
    def void reportAddSystemInfo(String str1, String str2) {
        extentReports.addSystemInfo(str1, str2)
    }

    //This method will Log Pass step
    def void logPass(String passInfo) {
        loggerS4lj.info(passInfo)
        extentTest.log(LogStatus.PASS, passInfo)
    }

    //This method will Log Fail step
    def void logFail(String failInfo) {
        loggerS4lj.info(failInfo)
        extentTest.log(LogStatus.FAIL, failInfo)
    }

    //This method will Log Fail step
    def void logFail(String failInfo, AssertionError e) {
        loggerS4lj.info(failInfo,e)
        extentTest.log(LogStatus.FAIL, failInfo)
    }

    //This method will Log Fail step
    def void logFail(String failInfo, AssertionError e, WebDriver driver) {
        loggerS4lj.info(failInfo,e)
        takeScreenShot(driver)
        extentTest.log(LogStatus.FAIL, failInfo)
    }

    //This method will Log Fail step
    def void logFail(String failInfo, WebDriver driver) {
        loggerS4lj.info(failInfo)
        takeScreenShot(driver)
        extentTest.log(LogStatus.FAIL, failInfo)
    }

    //This method will Log Pass step
    def void logInfo(String Info) {
        loggerS4lj.info(Info)
        extentTest.log(LogStatus.INFO, Info)
    }

    //This method will start the extentTest
    def ExtentTest logStartTest(String testName) {
        this.extentTest = extentReports.startTest(testName)
        extentTest
    }

    //This method will end the extentTest
    def void logEndTest(ExtentTest test) {
        extentReports.endTest(test)
    }

    //This method will flush the report
    def void endTestReport() {
        extentReports.flush()
        extentReports.close()
    }

    //This method will captureScreenshot and add to the extentReport
    def void captureScreenShot(WebDriver driver) {
        extentTest.log(LogStatus.INFO, "Snapshot below: " + extentTest.addScreenCapture(takeScreenShot(driver)))
    }






































}
