package com.hastane.selenium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Senaryo4_UiSmokeTest extends BaseUiTest {

    @Test
    void uygulamaAcilabiliyorMu() {
        // BaseUiTest içinde driver zaten setUp() ile oluşuyor
        driver.get("http://localhost:8080/");

        // Sayfa başlığı geldi mi?
        assertNotNull(driver.getTitle(), "Sayfa başlığı null, UI açılmamış olabilir");
    }
}

