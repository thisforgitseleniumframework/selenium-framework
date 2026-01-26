package com.framework.pages;

import com.framework.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private WebDriver driver;
    private WaitUtil wait;

    private By username = By.name("username");
    private By password = By.name("password");
    private By loginBtn = By.xpath("//button[@type='submit']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        wait = new WaitUtil(driver);
    }

    public void login(String user, String pass) {
        wait.waitForVisibility(username).sendKeys(user);
        wait.waitForVisibility(password).sendKeys(pass);
        wait.waitForClickability(loginBtn).click();
    }
}
