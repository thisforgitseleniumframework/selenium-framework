package com.framework.locators;

import org.openqa.selenium.By;

public class LoginPageLocators {

    public static final By USERNAME              = By.name("username");
    public static final By PASSWORD              = By.name("password");
    public static final By LOGIN_BTN             = By.xpath("//button[@type='submit']");
    public static final By ERROR_ALERT           = By.xpath("//p[contains(@class,'oxd-alert-content-text')]");
    public static final By FIELD_ERRORS          = By.xpath("//span[contains(@class,'oxd-input-field-error-message')]");
    public static final By FORGOT_PASSWORD_LINK  = By.xpath("//p[normalize-space()='Forgot your password?']");
}
