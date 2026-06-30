package com.framework.tests;

import com.framework.base.BaseTest;
import com.framework.pages.ForgotPasswordPage;
import com.framework.pages.LoginPage;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ForgotPasswordTest extends BaseTest {

    @Test(
        description = "Verify that clicking 'Forgot your password?' navigates to the Reset Password page",
        groups      = {"Smoke", "Navigation"}
    )
    public void clickForgotPasswordLinkNavigatesToResetPage() {
        LoginPage loginPage = new LoginPage(driver);
        ForgotPasswordPage forgotPage = loginPage.clickForgotPasswordLink();
        Assert.assertTrue(forgotPage.isOnResetPasswordPage(),
                "Clicking 'Forgot your password?' should navigate to the Reset Password page");
    }

    @Test(
        description = "Verify that the Reset Password page displays the correct heading",
        groups      = {"Smoke", "Navigation"}
    )
    public void verifyForgotPasswordPageHeading() {
        LoginPage loginPage = new LoginPage(driver);
        ForgotPasswordPage forgotPage = loginPage.clickForgotPasswordLink();
        Assert.assertEquals(forgotPage.getPageHeading(), "Reset Password",
                "Reset Password page heading should be 'Reset Password'");
    }

    @Test(
        description = "Verify that submitting the reset form with any non-empty username shows the success confirmation page. " +
                      "Note: this is a demo site — email is never actually sent; the UI always shows the confirmation regardless of username validity.",
        groups      = {"Regression", "Positive"}
    )
    public void resetPasswordFormSubmissionShowsConfirmation() {
        LoginPage loginPage = new LoginPage(driver);
        ForgotPasswordPage forgotPage = loginPage.clickForgotPasswordLink();
        forgotPage.enterUsername("nonExistentUser_xyz123");
        forgotPage.clickResetPasswordButton();
        Assert.assertTrue(forgotPage.isOnSuccessPage(),
                "Submitting the reset form should always display the success confirmation page");
    }

    @Test(
        description = "Verify that submitting the reset form without a username shows a 'Required' validation error",
        groups      = {"Regression", "Negative", "Validation"}
    )
    public void resetPasswordWithEmptyUsername() {
        LoginPage loginPage = new LoginPage(driver);
        ForgotPasswordPage forgotPage = loginPage.clickForgotPasswordLink();
        forgotPage.clickResetPasswordButton();
        Assert.assertEquals(forgotPage.getFieldValidationError(), "Required",
                "An empty username should trigger a 'Required' validation error");
    }

    @Test(
        description = "Verify that clicking Cancel on the Reset Password page returns the user to the Login page",
        groups      = {"Regression", "Navigation"}
    )
    public void cancelResetPasswordNavigatesBackToLogin() {
        LoginPage loginPage = new LoginPage(driver);
        ForgotPasswordPage forgotPage = loginPage.clickForgotPasswordLink();
        forgotPage.clickCancelButton();
        Assert.assertTrue(forgotPage.isOnLoginPage(),
                "Clicking Cancel should return the user to the Login page");
    }
}
