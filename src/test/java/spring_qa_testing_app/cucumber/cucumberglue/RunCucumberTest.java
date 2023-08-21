package spring_qa_testing_app.cucumber.cucumberglue;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

// see https://palashray.com/example-of-creating-cucumber-based-bdd-tests-using-junit5-and-spring-dependency-injection/
// The Test Runner
// cucumber glue is another word for step definitions
@Suite
@SelectClasspathResource("features")
@IncludeEngines({"cucumber"})
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "spring_qa_testing_app.cucumber.cucumberglue")
@ConfigurationParameter(key = Constants.PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, json:target/cucumber-report/cucumber.json")
public class RunCucumberTest {
}