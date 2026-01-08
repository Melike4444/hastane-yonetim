package com.hastane.selenium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Senaryo3_DoktorSayfasiTest extends BaseUiTest {

    @Test
    void doktorSayfasiAciliyorMu() {
        driver.get(baseUrl + "/doktorlar");
        assertTrue(driver.getCurrentUrl().contains("doktorlar"));
    }
}
