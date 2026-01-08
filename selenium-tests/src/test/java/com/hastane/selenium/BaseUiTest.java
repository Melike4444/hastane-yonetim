package com.hastane.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;

public abstract class BaseUiTest {

    protected WebDriver driver;

    // 🔴 DİKKAT: Jenkins + Docker için uygulama adresi
    protected String baseUrl = "http://hastane-yonetim-app:8080";

    // Bazı testler baseUrl() çağırıyor → O yüzden METOT DA VAR
    protected String baseUrl() {
        return baseUrl;
    }

    @BeforeEach
    void setUp() throws Exception {

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--headless=new");

        driver = new RemoteWebDriver(
                new URL("http://selenium-chrome:4444/wd/hub"),
                options
        );

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // 🔹 Yardımcı metotlar (testlerde kullanılıyor)

    protected boolean pageContains(String text) {
        return driver.getPageSource().contains(text);
    }

    protected WebElement findByText(String text) {
        return driver.findElement(By.xpath("//*[contains(text(),'" + text + "')]"));
    }
}
