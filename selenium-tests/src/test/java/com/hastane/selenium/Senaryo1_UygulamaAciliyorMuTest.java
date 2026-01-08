package com.hastane.selenium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Senaryo1_UygulamaAciliyorMuTest extends BaseUiTest {

    @Test
    void uygulamaAcilabiliyorMu() {
        driver.get(baseUrl() + "/");
        String title = driver.getTitle();
        // title boş olabilir ama sayfa açıldı mı diye çok basit kontrol:
        assertTrue(driver.getPageSource() != null && !driver.getPageSource().isBlank(),
                "Sayfa kaynağı boş geldi. Uygulama açılmıyor olabilir.");
    }
}
