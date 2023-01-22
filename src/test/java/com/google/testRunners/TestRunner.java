package com.google.testRunners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.*;
import util.Log;
import util.WriteAJsonFile;

import static com.google.steps.CommonHooks.failedScenariosCount;
import static com.google.steps.CommonHooks.passedScenariosCount;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com/google/steps"},
        plugin = {"json:target/cucumber-report/cucumber_googlesearchtests.json", "rerun:target/failed_scenarios_googlesearchtests.txt"}
)

public class TestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    @AfterSuite
    public void tearDown() throws Exception {
        Log.info("##Test suite end!");
        String[][] resultValues = {{"Tests executed", (passedScenariosCount + failedScenariosCount) + ""},
                {"Tests passed", passedScenariosCount + ""}, {"Tests failed", failedScenariosCount + ""}};
        WriteAJsonFile writeResultIntoJson = new WriteAJsonFile();
        writeResultIntoJson.createAndWriteInFile(resultValues, "testResults", "TestResults.json");
    }
}