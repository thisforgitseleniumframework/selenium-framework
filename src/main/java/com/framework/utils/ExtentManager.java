package com.framework.utils;
//package com.company.framework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getReport() {
        if (extent == null) {
            ExtentSparkReporter reporter =
                    new ExtentSparkReporter("target/ExtentReport.html");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
        }
        return extent;
    }
}
