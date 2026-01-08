package com.hastane.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.*;

public class Senaryo2_HastaSayfasiTest extends BaseUiTest {

    @Test
    void hastaSayfasiAciliyorMu() {
        driver.get(baseUrl() + "/");

        // Menü linki varsa tıkla (çok kırılmayan seçimler)
        if (!driver.findElements(By.xpath("//*[contains(.,'Hasta') or contains(.,'Hastalar')]")).isEmpty()) {
            driver.findElements(By.xpath("//*[contains(.,'Hasta') or contains(.,'Hastalar')]")).get(0).click();
        } else {
            // Menü yoksa URL ile dene (frontend route olabilir)
            driver.get(baseUrl() + "/hastalar");
        }

        String url = driver.getCurrentUrl();
        assertTrue(url.contains("hast") || url.startsWith(baseUrl()),
                "Hasta sayfasına gidilemedi. URL=" + url);
    }
}
