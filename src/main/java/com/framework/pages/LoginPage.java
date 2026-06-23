package com.framework.pages;

import com.framework.utils.ExtentTestManager;
import com.framework.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;

public class LoginPage {

    private WebDriver driver;
    private WaitUtil  wait;

    private By username    = By.name("username");
    private By password    = By.name("password");
    private By loginBtn    = By.xpath("//button[@type='submit']");
    private By errorAlert  = By.xpath("//p[contains(@class,'oxd-alert-content-text')]");
    private By fieldErrors = By.xpath("//span[contains(@class,'oxd-input-field-error-message')]");
    private By forgotPasswordLink = By.xpath("//p[normalize-space()='Forgot your password?']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        wait = new WaitUtil(driver);
    }

    // ---------------------------------------------------------------- actions

    public void login(String user, String pass) {
        step("Entering username : <b>" + (user.trim().isEmpty() ? "(empty)" : user) + "</b>");
        wait.waitForVisibility(username).sendKeys(user);
        step("Entering password");
        wait.waitForVisibility(password).sendKeys(pass);
        step("Clicking the Login button");
        wait.waitForClickability(loginBtn).click();
    }

    public void enterUsername(String user) {
        step("Entering username : <b>" + (user.trim().isEmpty() ? "(empty)" : user) + "</b>");
        wait.waitForVisibility(username).sendKeys(user);
    }

    public void enterPassword(String pass) {
        step("Entering password");
        wait.waitForVisibility(password).sendKeys(pass);
    }

    public void clickLoginButton() {
        step("Clicking the Login button");
        wait.waitForClickability(loginBtn).click();
    }

    // --------------------------------------------------------------- verifiers

    public String getErrorAlertMessage() {
        step("Waiting for error alert to appear");
        String msg = wait.waitForVisibility(errorAlert).getText();
        step("Error alert text : <b>" + msg + "</b>");
        return msg;
    }

    public List<String> getFieldValidationErrors() {
        step("Checking inline field validation messages");
        List<String> errors = wait.waitForAllVisible(fieldErrors)
                .stream().map(e -> e.getText()).collect(Collectors.toList());
        step("Validation messages found (" + errors.size() + ") : " + errors);
        return errors;
    }

    public boolean isOnLoginPage() {
        step("Verifying current page is the Login page");
        try {
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                .until(org.openqa.selenium.support.ui.ExpectedConditions
                    .not(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("auth/login")));
            step("Page navigated away from Login — login succeeded");
            return false;
        } catch (org.openqa.selenium.TimeoutException e) {
            step("Still on Login page — login was rejected as expected");
            return driver.getCurrentUrl().contains("auth/login");
        }
    }

    // ----------------------------------------------------------------- helper
    private void step(String message) {
        if (ExtentTestManager.getTest() != null)
            ExtentTestManager.getTest().info(message);
    }

    public ForgotPasswordPage clickForgotPasswordLink() {
        step("Clicking 'Forgot your password?' link");
        wait.waitForClickability(forgotPasswordLink).click();
        return new ForgotPasswordPage(driver);
    }
}
