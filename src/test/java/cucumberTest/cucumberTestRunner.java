package cucumberTest;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created by vijaypampana on 4/16/2017.
 */

//You have to type these lines manually
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/feature",
        format = {"pretty","json:target/cucumber.json"},
        monochrome=true,
        //tags = { "@1" },
        glue={"src/main/java/cucumber/stepDef"}
)
public class cucumberTestRunner {

}
