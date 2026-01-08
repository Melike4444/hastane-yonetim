package com.hastane.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.URL;

public abstract class BaseUiTest {

    protected WebDriver driver;

    protected String baseUrl() {
        // 🔥 Docker network içinden APP container
        return "http://hastane-yonetim-app:8080";
    }

    protected boolean pageContains(String text) {
        return driver.getPageSource().contains(text);
    }

    @BeforeEach
    void setUp() throws Exception {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new RemoteWebDriver(
                new URL("http://selenium-chrome:4444/wd/hub"),
                options
        );
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
