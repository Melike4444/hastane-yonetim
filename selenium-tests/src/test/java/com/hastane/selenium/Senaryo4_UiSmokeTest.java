package com.hastane.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Senaryo4_UiSmokeTest extends BaseUiTest {

    @Test
    void uygulamaAcilabiliyorMu() {
        WebDriver driver = getDriver();
        String baseUrl = getBaseUrl();

        driver.get(baseUrl);

        // Sayfa gerçekten açıldı mı?
        assertNotNull(driver.getTitle(), "Sayfa başlığı null, UI açılmamış olabilir");
    }
}

