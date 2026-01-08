package com.hastane.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;

public abstract class BaseUiTest {

    protected WebDriver driver;

    // ✅ Testlerin kullandığı değişken: baseUrl
    protected String baseUrl;

    @BeforeEach
    void setup() throws Exception {
        ChromeOptions options = new ChromeOptions();

        // Jenkins/Docker ortamında stabil çalışması için
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        // docker-compose içindeki selenium container adı: selenium-chrome
        String seleniumUrl = System.getProperty("SELENIUM_URL",
                System.getenv().getOrDefault("SELENIUM_URL", "http://selenium-chrome:4444/wd/hub"));

        driver = new RemoteWebDriver(new URL(seleniumUrl), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        // ✅ Uygulama base URL (docker-compose servis adı: hastane-yonetim-app)
        baseUrl = System.getProperty("BASE_URL",
                System.getenv().getOrDefault("BASE_URL", "http://hastane-yonetim-app:8080"));

        // / ile bitmesin diye normalize
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ✅ Senaryo5’te kullanılan helper (daha önce bunun yüzünden patlamıştı)
    protected boolean pageContains(String text) {
        return driver.getPageSource().contains(text);
    }
}
