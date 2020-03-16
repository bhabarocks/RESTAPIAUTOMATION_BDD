package runnerClasses;

import java.io.File;

import org.junit.AfterClass;
import org.junit.runner.RunWith;

import com.cucumber.listener.Reporter;
import com.github.mkolisnyk.cucumber.runner.ExtendedCucumber;
import com.github.mkolisnyk.cucumber.runner.ExtendedCucumberOptions;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(ExtendedCucumber.class)
@ExtendedCucumberOptions(
        jsonReport = "target/cucumber-report/cucumber-report.json",
        retryCount = 0, // Number of times retry should happen in case of failure
        detailedReport = true,
        detailedAggregatedReport = true,
        //jsonUsageReport = "target/cucumber-report/cucumber-report.json",
        overviewReport = true,
        //usageReport = true,
        toPDF = true,
        outputFolder = "target")

@CucumberOptions(
		//plugin = {"pretty"},
    /*plugin = {
       "html:target/cucumberHtmlReport",
       "json:target/cucumber-report.json"
   }, // Plugin to generate HTML report and json report*/
        plugin = {"com.cucumber.listener.ExtentCucumberFormatter:target/cucumber-reports/cucumber-report.html","json:target/cucumber-report/cucumber-report.json"},
        monochrome = true,
    features = {
        "src/test/resources/Features"
    },
    glue = {
        "glueCode"
    },
    tags = {
        "@API_Test"
    })
public class CukeRunner {
    @AfterClass
    public static void writeExtentReport() {
        Reporter.loadXMLConfig(new File("src/test/resources/extent-config.xml"));
        Reporter.setSystemInfo("User Name", System.getProperty("user.name"));
        Reporter.setSystemInfo("Time Zone", System.getProperty("user.timezone"));
        Reporter.setSystemInfo("Machine", 	System.getProperty("os.name") + " - "+ System.getProperty("os.arch"));
        Reporter.setSystemInfo("Rest Assured", "4.2.0");
        Reporter.setSystemInfo("Maven", "3.5.2");
        Reporter.setSystemInfo("Java Version", System.getProperty("java.version"));
    }
}