package com.hastane.selenium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Senaryo4_UiSmokeTest extends BaseUiTest {

    @Test
    void uygulamaAcilabiliyorMu() {
        driver.get(baseUrl() + "/");
        String url = driver.getCurrentUrl();
        assertTrue(url.startsWith(baseUrl()), "UI smoke: baseUrl açılmadı. URL=" + url);
    }
}
