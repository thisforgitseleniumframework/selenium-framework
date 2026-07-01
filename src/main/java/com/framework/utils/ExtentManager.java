package com.framework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentManager {

    private static ExtentReports extent;

    public static synchronized ExtentReports getReport() {
        if (extent == null) {
            String runTime  = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd MMM yyyy  hh:mm:ss a"));
            String reportPath = System.getProperty("user.dir") + "/target/ExtentReport.html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setDocumentTitle("Selenium Automation Report");
            spark.config().setReportName(
                    "Login Module — Test Execution Report" +
                    "<br><small style='font-size:13px;font-weight:400;color:#aaa;'>" +
                    "Run : " + runTime + "</small>");
            spark.config().setTheme(Theme.DARK);
            spark.config().setTimeStampFormat("dd MMM yyyy  hh:mm:ss a");
            spark.config().setEncoding("UTF-8");

            // Improve readability of step log entries
            spark.config().setCss(
                ".step.info  .step-details { border-left: 3px solid #4a90d9; padding-left: 8px; }" +
                ".step.pass  .step-details { border-left: 3px solid #28a745; padding-left: 8px; }" +
                ".step.fail  .step-details { border-left: 3px solid #dc3545; padding-left: 8px; }" +
                ".step.skip  .step-details { border-left: 3px solid #ffc107; padding-left: 8px; }" +
                ".test-name  { font-weight: 600 !important; font-size: 14px !important; }"
            );

            // Inject project name into the navbar.
            // The Spark template uses div.nav-logo (not a.brand-logo).
            // The custom JS is placed at the end of <body> via scripts.ftl, so
            // DOMContentLoaded may have already fired — guard against both states.
            String projectName = ConfigReader.get("project.name");
            spark.config().setJs(
                "(function() {" +
                "  function injectProjectName() {" +
                "    var navLogo = document.querySelector('.nav-logo');" +
                "    if (!navLogo) return;" +
                "    var brand = document.createElement('a');" +
                "    brand.href = '#';" +
                "    brand.textContent = '" + projectName + "';" +
                "    brand.style.cssText = 'font-size:18px;font-weight:700;color:#fff;" +
                                           "margin-left:12px;vertical-align:middle;" +
                                           "line-height:60px;display:inline-block;';" +
                "    navLogo.appendChild(brand);" +
                "  }" +
                "  if (document.readyState === 'loading') {" +
                "    document.addEventListener('DOMContentLoaded', injectProjectName);" +
                "  } else {" +
                "    injectProjectName();" +
                "  }" +
                "})();"
            );

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Application",  "OrangeHRM Demo");
            extent.setSystemInfo("Environment",  "QA");
            extent.setSystemInfo("Base URL",     "https://opensource-demo.orangehrmlive.com");
            extent.setSystemInfo("Browser",      "Chrome");
            extent.setSystemInfo("OS",           System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Author",       System.getProperty("user.name"));
        }
        return extent;
    }
}
