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

    // 🔴 TESTLERİN BEKLEDİĞİ DEĞİŞKEN (HATAYI BİTİREN SATIR)
    protected String baseUrl = "http://hastane-yonetim-app:8080";

    @BeforeEach
    void setup() throws Exception {

        ChromeOptions options = new ChromeOptions();

        // Jenkins + Docker için ZORUNLU
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        // Selenium container’a bağlan
        driver = new RemoteWebDriver(
                new URL("http://selenium-chrome:4444/wd/hub"),
                options
        );

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Yardımcı metot
    protected boolean pageContains(String text) {
        return driver.findElements(
                By.xpath("//*[contains(text(),'" + text + "')]")
        ).size() > 0;
    }
}
