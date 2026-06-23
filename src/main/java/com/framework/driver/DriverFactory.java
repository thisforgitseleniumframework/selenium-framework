package com.framework.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverFactory {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initDriver(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
        	ChromeOptions options = new ChromeOptions();
        	options.addArguments("--no-sandbox");
        	options.addArguments("--disable-dev-shm-usage");
        	// Run headless when the system property is set (e.g. CI / scheduled jobs)
        	if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
        	    options.addArguments("--headless=new");
        	    options.addArguments("--window-size=1920,1080");
        	}

        //	WebDriver driver = new ChromeDriver(options);

            driver.set(new ChromeDriver(options));
        } else if (browser.equalsIgnoreCase("firefox")) {
            driver.set(new FirefoxDriver());
        } else {
            throw new RuntimeException("Browser not supported");
        }

        getDriver().manage().window().maximize();
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
