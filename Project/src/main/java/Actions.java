import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static junit.framework.TestCase.*;
import static org.junit.Assert.*;

public class Actions {

    WebDriver driver;
    By byTaxYearDropDown = By.id("yearsel");
    By bySalaryBeforeDeductionField = By.id("gross");
    By bySalaryFrequencyDropDown = By.id("period");
    By byAgeDropDown = By.id("age");
    By byCalculateButton = By.xpath("//a[contains(text(), \"Calculate\")]");
    By byClosePopupBtn = By.xpath("//a[contains(text(), 'CLOSE')]");
    By getByTakeHome(String takeHome){
        return By.xpath("//div[@id='display']//span[contains(text(), '" +takeHome+ "')]");
    }


    @Before
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        //the sandbox removes unnecessary privileges from the processes that don't need them in Chrome, for security purposes. Disabling the sandbox makes your PC more vulnerable to exploits via webpages, so Google don't recommend it.
        options.addArguments("--no-sandbox");
        //"--disable-dev-shm-usage" Only added when CI system environment variable is set or when inside a docker instance. The /dev/shm partition is too small in certain VM environments, causing Chrome to fail or crash.
        options.addArguments("--disable-dev-shm-usage");
        if(!System.getProperty("os.name").contains("Windows")){
            options.addArguments("--headless");
        }
        driver = new ChromeDriver(options);
    }

    @Given("the user is on the landing page")
    public void theUserIsonTheLandingPage(){
        driver.get("https://www.taxtim.com/za/calculators/income-tax");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement element;
        element = wait.until(ExpectedConditions.elementToBeClickable(byClosePopupBtn));
        driver.findElement(byClosePopupBtn).click();
    }

    @Given("the tax year is set to the previous year")
    public void theTaxYearIsSetToThePreviousYear() {
        WebElement dropdown = driver.findElement(byTaxYearDropDown);
        Select select = new Select(dropdown);
        select.selectByIndex(3);
    }

    @After
    public void tearDown(){
        driver.quit();
    }


    @Given("the salary before deduction is {string}")
    public void theSalaryBeforeDeductionIs(String grossSalary) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement element;
        element = wait.until(ExpectedConditions.elementToBeClickable(bySalaryBeforeDeductionField));
        element.clear();
        element.sendKeys(grossSalary);
    }

    @Given("the salary period is set to monthly")
    public void theSalaryPeriodIsSetToMonthly() {
        WebElement dropdown = driver.findElement(bySalaryFrequencyDropDown);
        Select select = new Select(dropdown);
        select.selectByIndex(3);
    }

    @Given("the user is under the age {string}")
    public void theUserIsUnderTheAgeSixtyFive(String age) {
        WebElement dropdown = driver.findElement(byAgeDropDown);
        dropdown.sendKeys(age);

    }

    @When("the user calculates their tax")
    public void theUserCalculatesTheirTax() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement element;
        element = wait.until(ExpectedConditions.elementToBeClickable(byCalculateButton));
        element.click();
        element.click();
    }

    @Then("the take home must be {string}")
    public void theTakeHomeMustBe(String takeHome) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        boolean isVisible =  wait.until(ExpectedConditions.visibilityOfElementLocated(getByTakeHome(takeHome));
        assertTrue(isVisible);
    }
}
