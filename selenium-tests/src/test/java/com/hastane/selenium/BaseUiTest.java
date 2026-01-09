package com.hastane.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;

public class BaseUiTest {

    protected WebDriver driver;

    // UI testlerinin gideceği uygulama adresi
    // Jenkins/CI: host.docker.internal:8080 (compose app portu hosta mapli)
    protected String baseUrl() {
        return System.getenv().getOrDefault("BASE_URL", "http://host.docker.internal:8080");
    }

    // Selenium Grid adresi (docker compose içindeki selenium service adı)
    protected String gridUrl() {
        return System.getenv().getOrDefault("SELENIUM_GRID_URL", "http://hastane-yonetim-selenium:4444/wd/hub");
    }

    @BeforeEach
    void setUp() throws Exception {
        ChromeOptions options = new ChromeOptions();

        // Grid container zaten xvfb/headless ile çalışıyor ama stabil olsun:
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1400,900");

        driver = new RemoteWebDriver(new URL(gridUrl()), options);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
    }

    @AfterEach
    void teardown() {
        if (driver != null) driver.quit();
    }
}
