package com.hastane.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public abstract class BaseUiTest {

    protected WebDriver driver;
    protected String baseUrl;

    @BeforeEach
    void setup() throws Exception {
        // baseUrl: önce system property, yoksa env, yoksa default
        baseUrl = System.getProperty(
                "baseUrl",
                System.getenv().getOrDefault("BASE_URL", "http://localhost:8080")
        );

        // Remote URL: Jenkins’te vereceğiz
        String remoteUrl = System.getProperty(
                "seleniumRemoteUrl",
                System.getenv().getOrDefault("SELENIUM_REMOTE_URL", "")
        );

        ChromeOptions options = new ChromeOptions();
        // Remote container zaten headless değil ama sorun olmaz
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        if (remoteUrl != null && !remoteUrl.isBlank()) {
            // ✅ CI: RemoteWebDriver
            driver = new RemoteWebDriver(new URL(remoteUrl), options);
        } else {
            // ✅ Local: Eski davranış (ChromeDriver)
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(options);
        }
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
