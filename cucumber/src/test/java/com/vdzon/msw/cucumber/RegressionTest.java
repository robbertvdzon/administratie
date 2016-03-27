package com.vdzon.msw.cucumber;

import org.junit.runner.RunWith;
import cucumber.junit.Cucumber;

@RunWith(Cucumber.class)
@Cucumber.Options(
        features = "MockApp",//path to the features
        format = {"json:target/integration_cucumber.json"},//what formatters to use
        tags = {"@all"})//what tags to incluse(@)/exclude(@~)
public class RegressionTest {
}
