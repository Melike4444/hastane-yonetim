package com.hastane.selenium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Senaryo1_UygulamaAciliyorMuTest extends BaseUiTest {

    @Test
    void uygulamaAciliyorMu() {
        driver.get(baseUrl() + "/");
        String url = driver.getCurrentUrl();

        assertTrue(url.startsWith(baseUrl()), "Uygulama beklenen baseUrl ile açılmadı. URL=" + url);
        assertNotNull(driver.getTitle(), "Title null geldi (sayfa yüklenmemiş olabilir).");
    }
}
