package com.hastane.selenium;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Senaryo1_UygulamaAciliyorMuTest extends BaseUiTest {

    @Test
    void uygulamaAciliyorMu() {
        driver.get("http://localhost:9091/");
        String url = driver.getCurrentUrl();
        assertTrue(url.contains("9091"), "Uygulama 9091'de açılmadı.");
    }
}
