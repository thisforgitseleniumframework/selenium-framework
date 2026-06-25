package com.framework.locators;

import org.openqa.selenium.By;

public class ForgotPasswordPageLocators {

    public static final By PAGE_HEADING    = By.xpath("//h6");
    public static final By USERNAME_FIELD  = By.name("username");
    public static final By RESET_BTN       = By.xpath("//button[@type='submit']");
    public static final By CANCEL_BTN      = By.xpath("//button[normalize-space()='Cancel']");
    public static final By FIELD_ERROR     = By.xpath("//span[contains(@class,'oxd-input-field-error-message')]");
}
