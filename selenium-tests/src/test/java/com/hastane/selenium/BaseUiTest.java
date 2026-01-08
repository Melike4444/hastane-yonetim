package com.hastane.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.URL;
import java.time.Duration;

public class BaseUiTest {

    protected WebDriver driver;

    // Docker network içinden uygulama
    protected String baseUrl = "http://hastane-yonetim-app:8080";
    protected String baseUrl() { return baseUrl; }

    @BeforeEach
    void setup() throws Exception {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Selenium Grid container
        driver = new RemoteWebDriver(new URL("http://selenium-chrome:4444/wd/hub"), options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
    }

    @AfterEach
    void teardown() {
        if (driver != null) driver.quit();
    }
}
