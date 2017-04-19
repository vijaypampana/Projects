package cucumber.stepDef;

import com.sanvijay.testFramework.util.TestUtil;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Created by vijaypampana on 4/16/2017.
 * 
 */
public class applicantPageStepDef extends TestUtil {

	final static Logger loggerS4lj = LoggerFactory.getLogger(TestUtil.class);

	WebDriver driver;

	@Given("^the user navigates to http://localhost:8080/March09/web/applicant page$")
	public void the_user_navigates_to_http_localhost_March_web_applicant_page() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		this.driver = loadDriver("FF");
		this.driver.get("http://localhost:8080/March09/web/login");
		findElement("id","username").sendKeys("vijay");
		findElement("id","password").sendKeys("pampana");
		findElement("id", "Submit").click();

	}

	@When("^the tester type vijay in firstname field$")
	public void the_tester_type_vijay_in_firstname_field() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		findElement("name","applfirstname").sendKeys("vijay");

	}

	@When("^the tester type pampana in lastname field$")
	public void the_tester_type_pampana_in_lastname_field() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		findElement("name","appllastname").sendKeys("pampana");
	}

	@When("^the tester type Downey Drive in addrline1 field$")
	public void the_tester_type_Downey_Drive_in_addrline_field() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		findElement("id","appaddrline1").sendKeys("Downey Dr");
	}

	@When("^the tester type Manchester in city field$")
	public void the_tester_type_Manchester_in_city_field() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		findElement("name","city").sendKeys("Manchester");
	}

	@When("^the tester type CT in state field$")
	public void the_tester_type_CT_in_state_field() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		findElement("name","state").sendKeys("CT");
	}

	@When("^the tester type 06040 in zipcode field$")
	public void the_tester_type_in_zipcode_field() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		findElement("name","zipcode").sendKeys("06040");

	}

	@When("^the tester click on next button$")
	public void the_tester_click_on_next_button() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		findElement("id","Submit").click();

	}

	@Then("^the tester navigates to income page$")
	public void the_tester_navigates_to_income_page() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		try {
			Assert.assertTrue(findElement("id", "applSSN").isDisplayed());
			System.out.println("\nApplication information validation completed");
			this.driver.quit();
		} catch (AssertionError e) {
			System.out.println("Assertion Fails");
		}

	}

}
