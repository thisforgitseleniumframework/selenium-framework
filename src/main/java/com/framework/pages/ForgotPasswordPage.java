package com.framework.pages;

import com.framework.utils.ExtentTestManager;
import com.framework.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ForgotPasswordPage {

    private WebDriver driver;
    private WaitUtil  wait;

    // Locators — reset password form
    private By pageHeading   = By.xpath("//h6");
    private By usernameField = By.name("username");
    private By resetBtn      = By.xpath("//button[@type='submit']");
    private By cancelBtn     = By.xpath("//button[normalize-space()='Cancel']");
    private By fieldError    = By.xpath("//span[contains(@class,'oxd-input-field-error-message')]");

    public ForgotPasswordPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WaitUtil(driver);
    }

    // ---------------------------------------------------------------- actions

    public void enterUsername(String username) {
        step("Entering username : <b>" + (username.trim().isEmpty() ? "(empty)" : username) + "</b>");
        wait.waitForVisibility(usernameField).sendKeys(username);
    }

    public void clickResetPasswordButton() {
        step("Clicking the Reset Password button");
        wait.waitForClickability(resetBtn).click();
    }

    public void clickCancelButton() {
        step("Clicking the Cancel button");
        wait.waitForClickability(cancelBtn).click();
    }

    // --------------------------------------------------------------- verifiers

    public String getPageHeading() {
        step("Reading page heading");
        String text = wait.waitForVisibility(pageHeading).getText();
        step("Page heading : <b>" + text + "</b>");
        return text;
    }

    public String getFieldValidationError() {
        step("Checking field validation error message");
        String error = wait.waitForVisibility(fieldError).getText();
        step("Validation error : <b>" + error + "</b>");
        return error;
    }

    public boolean isOnResetPasswordPage() {
        step("Verifying current page is the Reset Password page");
        boolean result = driver.getCurrentUrl().contains("requestPasswordResetCode");
        step("On Reset Password page : <b>" + result + "</b>");
        return result;
    }

    public boolean isOnSuccessPage() {
        step("Waiting for password reset confirmation (URL change or form disappears)");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(45))
                    .until(ExpectedConditions.or(
                        ExpectedConditions.urlContains("sendPasswordReset"),
                        ExpectedConditions.invisibilityOfElementLocated(By.name("username"))
                    ));
            step("Password reset confirmation received");
            return true;
        } catch (TimeoutException e) {
            step("Reset confirmation not received within timeout");
            return false;
        }
    }

    public boolean isOnLoginPage() {
        step("Verifying navigation back to Login page");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.urlContains("auth/login"));
            step("Successfully returned to Login page");
            return true;
        } catch (TimeoutException e) {
            step("Not on Login page");
            return false;
        }
    }

    // ----------------------------------------------------------------- helper
    private void step(String message) {
        if (ExtentTestManager.getTest() != null)
            ExtentTestManager.getTest().info(message);
    }
}
