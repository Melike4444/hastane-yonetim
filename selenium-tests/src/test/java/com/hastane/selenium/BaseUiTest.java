package com.hastane.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public abstract class BaseUiTest {

    protected WebDriver driver;

    // ✅ Bazı testlerin kullandığı: driver.get(baseUrl)
    protected String baseUrl;

    // Docker compose servis adı üzerinden (Jenkins içinde)
    private static final String DEFAULT_BASE_URL = "http://hastane-yonetim-app:8080";
    private static final String SELENIUM_GRID_URL = "http://selenium-chrome:4444/wd/hub";

    // ✅ Diğer testlerin kullandığı: driver.get(baseUrl())
    protected String baseUrl() {
        return baseUrl;
    }

    @BeforeEach
    void setup() throws MalformedURLException {
        // İstersen Jenkins'te parametreyle override edebil:
        // -DbaseUrl=http://...  (yoksa default)
        baseUrl = System.getProperty("baseUrl", DEFAULT_BASE_URL);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new RemoteWebDriver(new URL(SELENIUM_GRID_URL), options);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
