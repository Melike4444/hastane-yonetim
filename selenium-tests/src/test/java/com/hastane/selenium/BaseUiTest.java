package com.hastane.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;

public abstract class BaseUiTest {

    protected WebDriver driver;

    @BeforeEach
    void setUp() {
        // Jenkins konteyneri Linux üzerinde çalışıyor.
        // Linux ortamında SafariDriver olmadığı için bu testleri SKIP ediyoruz.
        String osName = System.getProperty("os.name", "").toLowerCase();
        if (osName.contains("linux")) {
            Assumptions.assumeTrue(false,
                    "UI Selenium tests are skipped on CI (Linux Jenkins environment).");
        }

        // Lokal geliştirme ortamında (MacOS + Safari) çalıştırmak için:
        driver = new SafariDriver();
        driver.manage().window().maximize();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
