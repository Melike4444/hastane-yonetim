package com.hastane.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;

public abstract class BaseUiTest {

    protected WebDriver driver;

    @BeforeEach
    void setup() throws Exception {
        ChromeOptions options = new ChromeOptions();

        // Jenkins/Docker ortamında stabil çalışması için
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        // docker-compose içindeki selenium container adı genelde: selenium-chrome
        String seleniumUrl = System.getProperty("SELENIUM_URL",
                System.getenv().getOrDefault("SELENIUM_URL", "http://selenium-chrome:4444/wd/hub"));

        driver = new RemoteWebDriver(new URL(seleniumUrl), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // UI ana adresi (docker-compose’da app servisi: hastane-yonetim-app)
    protected String baseUrl() {
        String url = System.getProperty("BASE_URL",
                System.getenv().getOrDefault("BASE_URL", "http://hastane-yonetim-app:8080"));
        // / ile bitmesin diye normalize edelim
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    // Testlerde “sayfada şu yazı var mı?” kontrolü için helper
    protected boolean pageContains(String text) {
        // en basit ve sağlam yöntem:
        return driver.getPageSource().contains(text);

        // Alternatif görünür text kontrolü istersen:
        // return driver.findElement(By.tagName("body")).getText().contains(text);
    }
}
