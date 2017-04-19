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
 */
public class loginPageStepDef extends TestUtil {

	final static Logger loggerS4lj = LoggerFactory.getLogger(TestUtil.class);

	WebDriver driver;

	@Given("^the user navigates to http://localhost:(\\d+)/March(\\d+)/web/login page$")
	public void the_user_navigates_to_http_localhost_March_web_login_page(int arg1, int arg2) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		this.driver = loadDriver("FF");
		this.driver.get("http://localhost:8080/March09/web/login");
	}

	@When("^the tester type vijay in username field$")
	public void the_tester_type_vijay_in_username_field() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		findElement("id", "username").sendKeys("vijay");

	}

	@When("^the tester type pampana in password field$")
	public void the_tester_type_pampana_in_password_field() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		findElement("id", "password").sendKeys("pampana");
	}

	@When("^the tester click on login button$")
	public void the_tester_click_on_login_button() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		findElement("id", "Submit").click();

	}

	@Then("^the tester navigates to application page$")
	public void the_tester_navigates_to_application_page() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		try {
			Assert.assertTrue(findElement("name", "applfirstname").isDisplayed());
			System.out.println("\nScenario Pass");
			this.driver.quit();
		} catch (AssertionError e) {
			System.out.println("Assertion Fails");
		}
	}

		@Then("^the password error message is thrown on the login page$")
		public void the_password_error_message_is_thrown_on_the_login_page() throws Throwable {
			// Write code here that turns the phrase above into concrete actions
			try {
				Assert.assertTrue(findElement("id", "password.errors").isDisplayed());
				System.out.println("\nScenario Pass");
				this.driver.quit();
			} catch (AssertionError e) {
				System.out.println("Assertion Fails");
			}



	}

}
