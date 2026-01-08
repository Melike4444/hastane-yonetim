package com.hastane.selenium;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Senaryo1_UygulamaAciliyorMuTest extends BaseUiTest {

    @Test
    void uygulamaAciliyorMu() {
        driver.get(baseUrl());
        assertNotNull(driver.getTitle(), "Uygulama açılmadı");
    }
}
