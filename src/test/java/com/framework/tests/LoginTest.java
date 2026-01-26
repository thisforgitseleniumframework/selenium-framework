package com.framework.tests;

import com.framework.base.BaseTest;
import com.framework.pages.LoginPage;

import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void validLoginTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("Admin", "admin123");
    }
}
