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

            // Inject project name as the first item in ul.nav-left.
            // div.nav-logo is sized only for the logo image — appending text there causes wrapping.
            // ul.nav-left has the horizontal space and is the correct container for nav items.
            // The custom JS is at the end of <body> (scripts.ftl), so guard readyState.
            String projectName = ConfigReader.get("project.name");
            spark.config().setJs(
                "(function() {" +
                "  function injectProjectName() {" +
                "    var navLeft = document.querySelector('ul.nav-left');" +
                "    if (!navLeft) return;" +
                "    var li = document.createElement('li');" +
                "    li.style.cssText = 'padding:0 16px;white-space:nowrap;';" +
                "    var span = document.createElement('span');" +
                "    span.textContent = '" + projectName + "';" +
                "    span.style.cssText = 'font-size:16px;font-weight:700;color:#fff;" +
                                          "vertical-align:middle;line-height:60px;" +
                                          "display:inline-block;letter-spacing:0.4px;';" +
                "    li.appendChild(span);" +
                "    navLeft.insertBefore(li, navLeft.firstChild);" +
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
