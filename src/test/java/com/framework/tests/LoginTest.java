package com.framework.tests;

import com.framework.base.BaseTest;
import com.framework.pages.LoginPage;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class LoginTest extends BaseTest {

    @Test(
        retryAnalyzer = RetryAnalyzer.class,
        description   = "Verify that a user can log in successfully with valid credentials",
        groups        = {"Smoke", "Positive"}
    )
    public void validLoginTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("Admin", "admin123");
    }

    @Test(
        description = "Verify that 'Required' validation appears on both fields when login is submitted empty",
        groups      = {"Regression", "Negative", "Validation"}
    )
    public void loginWithEmptyCredentials() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickLoginButton();
        List<String> errors = loginPage.getFieldValidationErrors();
        Assert.assertEquals(errors.size(), 2, "Expected 'Required' error on both fields");
        Assert.assertTrue(errors.stream().allMatch(e -> e.equals("Required")),
                "Both validation messages should be 'Required'");
    }

    @Test(
        description = "Verify that 'Required' validation appears on the username field when only password is filled",
        groups      = {"Regression", "Negative", "Validation"}
    )
    public void loginWithEmptyUsernameAndValidPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterPassword("admin123");
        loginPage.clickLoginButton();
        List<String> errors = loginPage.getFieldValidationErrors();
        Assert.assertEquals(errors.size(), 1, "Expected 'Required' error only on username");
        Assert.assertEquals(errors.get(0), "Required");
    }

    @Test(
        description = "Verify that 'Required' validation appears on the password field when only username is filled",
        groups      = {"Regression", "Negative", "Validation"}
    )
    public void loginWithValidUsernameAndEmptyPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername("Admin");
        loginPage.clickLoginButton();
        List<String> errors = loginPage.getFieldValidationErrors();
        Assert.assertEquals(errors.size(), 1, "Expected 'Required' error only on password");
        Assert.assertEquals(errors.get(0), "Required");
    }

    @Test(
        description = "Verify that 'Invalid credentials' error is shown when both username and password are wrong",
        groups      = {"Regression", "Negative", "Authentication"}
    )
    public void loginWithInvalidCredentials() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("invalidUser", "invalidPass");
        Assert.assertEquals(loginPage.getErrorAlertMessage(), "Invalid credentials");
    }

    @Test(
        description = "Verify that 'Invalid credentials' error is shown when a valid username is paired with a wrong password",
        groups      = {"Regression", "Negative", "Authentication"}
    )
    public void loginWithValidUsernameInvalidPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("Admin", "wrongPassword123");
        Assert.assertEquals(loginPage.getErrorAlertMessage(), "Invalid credentials");
    }

    @Test(
        description = "Verify that 'Invalid credentials' error is shown when an invalid username is paired with the correct password",
        groups      = {"Regression", "Negative", "Authentication"}
    )
    public void loginWithInvalidUsernameValidPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("invalidUser", "admin123");
        Assert.assertEquals(loginPage.getErrorAlertMessage(), "Invalid credentials");
    }

    @Test(
        description = "Verify that SQL injection input in login fields does not bypass authentication",
        groups      = {"Security", "Negative"}
    )
    public void loginWithSQLInjection() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("' OR '1'='1' --", "' OR '1'='1' --");
        Assert.assertTrue(loginPage.isOnLoginPage(),
                "SQL injection should not bypass login — user must remain on login page");
    }
}
