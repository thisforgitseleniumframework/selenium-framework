package com.framework.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.framework.driver.DriverFactory;
import com.framework.utils.ExtentManager;
import com.framework.utils.ExtentTestManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.concurrent.atomic.AtomicInteger;

public class TestListener implements ITestListener {

    private static final ExtentReports extent = ExtentManager.getReport();

    private static final AtomicInteger total   = new AtomicInteger(0);
    private static final AtomicInteger passed  = new AtomicInteger(0);
    private static final AtomicInteger failed  = new AtomicInteger(0);
    private static final AtomicInteger skipped = new AtomicInteger(0);

    // ------------------------------------------------------------------ suite
    @Override
    public void onStart(ITestContext context) {
        log("==================================================================");
        log(" SUITE STARTED  : " + context.getName());
        log("==================================================================");
    }

    // ----------------------------------------------------------------- per test
    @Override
    public void onTestStart(ITestResult result) {
        int    num    = total.incrementAndGet();
        String method = result.getMethod().getMethodName();
        String clazz  = result.getTestClass().getRealClass().getSimpleName();
        String desc   = result.getMethod().getDescription();
        String[] groups = result.getMethod().getGroups();
        String browser  = result.getTestContext()
                                .getCurrentXmlTest().getParameter("browser");

        // Build description shown under the test name
        String nodeDesc = (desc != null && !desc.isEmpty())
                ? desc + "<br><span style='color:#999;font-size:12px'>Class: " + clazz + "</span>"
                : "Class: <b>" + clazz + "</b>";

        ExtentTest test = extent.createTest(
                String.format("[TC-%02d]  %s", num, method), nodeDesc);

        // Metadata tags visible as coloured badges in the report
        test.assignAuthor("QA Automation");
        for (String g : groups) test.assignCategory(g);
        if (groups.length == 0)  test.assignCategory("Regression");
        if (browser != null && !browser.isEmpty())
            test.assignDevice(browser.substring(0, 1).toUpperCase() + browser.substring(1));

        test.info("Test started : <b>" + method + "</b>");
        ExtentTestManager.setTest(test);

        log(String.format("%n[TC-%02d] STARTED   : %s", num, method));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        passed.incrementAndGet();
        long   ms     = result.getEndMillis() - result.getStartMillis();
        String method = result.getMethod().getMethodName();

        ExtentTestManager.getTest()
                .pass("<b>PASSED</b>")
                .info("Duration : " + ms + " ms");

        ExtentTestManager.removeTest();
        log(String.format("[TC-%02d] PASSED     : %s  (%.2f s)",
                total.get(), method, ms / 1000.0));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        failed.incrementAndGet();
        long   ms     = result.getEndMillis() - result.getStartMillis();
        String method = result.getMethod().getMethodName();

        ExtentTest test = ExtentTestManager.getTest();
        test.fail("<b>FAILED</b>");
        test.fail(result.getThrowable());
        test.info("Duration : " + ms + " ms");

        // Attach inline screenshot
        try {
            WebDriver driver = DriverFactory.getDriver();
            if (driver != null) {
                String base64 = ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BASE64);
                test.fail(MediaEntityBuilder
                        .createScreenCaptureFromBase64String(base64, "Failure Screenshot")
                        .build());
            }
        } catch (Exception ignored) { }

        ExtentTestManager.removeTest();
        log(String.format("[TC-%02d] FAILED     : %s  (%.2f s)  =>  %s",
                total.get(), method, ms / 1000.0,
                result.getThrowable().getMessage()));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        skipped.incrementAndGet();
        String method = result.getMethod().getMethodName();
        String reason = result.getThrowable() != null
                ? result.getThrowable().getMessage()
                : "No reason provided";

        ExtentTestManager.getTest()
                .skip("<b>SKIPPED</b>")
                .skip("Reason : " + reason);

        ExtentTestManager.removeTest();
        log(String.format("[TC-%02d] SKIPPED    : %s  =>  %s",
                total.get(), method, reason));
    }

    // --------------------------------------------------------------- on finish
    @Override
    public void onFinish(ITestContext context) {
        int t = total.get(), p = passed.get(), f = failed.get(), s = skipped.get();
        String rate = t > 0
                ? String.format("%.1f%%", (p * 100.0) / t)
                : "N/A";

        extent.setSystemInfo("Tests Executed", String.valueOf(t));
        extent.setSystemInfo("Passed",         String.valueOf(p));
        extent.setSystemInfo("Failed",         String.valueOf(f));
        extent.setSystemInfo("Skipped",        String.valueOf(s));
        extent.setSystemInfo("Pass Rate",      rate);

        extent.flush();

        log("");
        log("==================================================================");
        log(" EXECUTION SUMMARY  —  " + context.getName());
        log("------------------------------------------------------------------");
        log(String.format(" Total Executed : %d", t));
        log(String.format(" Passed         : %d", p));
        log(String.format(" Failed         : %d", f));
        log(String.format(" Skipped        : %d", s));
        log(String.format(" Pass Rate      : %s", rate));
        log("------------------------------------------------------------------");
        log(" Report : target/ExtentReport.html");
        log("==================================================================");
    }

    // ----------------------------------------------------------------- helpers
    private void log(String msg) { System.out.println(msg); }
}