package com.hastane.selenium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Senaryo4_UiSmokeTest extends BaseUiTest {

    @Test
    void uiSayfasiAcilabiliyorMu() {
        driver.get(baseUrl() + "/");

        assertNotNull(driver.getTitle(), "Sayfa başlığı null, UI açılmamış olabilir");
        assertTrue(driver.getPageSource() != null && !driver.getPageSource().isBlank(),
                "UI sayfa kaynağı boş geldi.");
    }
}
