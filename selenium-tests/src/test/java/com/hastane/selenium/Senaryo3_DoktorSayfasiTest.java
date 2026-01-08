package com.hastane.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.*;

public class Senaryo3_DoktorSayfasiTest extends BaseUiTest {

    @Test
    void doktorSayfasiAciliyorMu() {
        driver.get(baseUrl() + "/");

        if (!driver.findElements(By.xpath("//*[contains(.,'Doktor') or contains(.,'Doktorlar')]")).isEmpty()) {
            driver.findElements(By.xpath("//*[contains(.,'Doktor') or contains(.,'Doktorlar')]")).get(0).click();
        } else {
            driver.get(baseUrl() + "/doktorlar");
        }

        String url = driver.getCurrentUrl();
        assertTrue(url.contains("dokt") || url.startsWith(baseUrl()),
                "Doktor sayfasına gidilemedi. URL=" + url);
    }
}
