package com.hastane.selenium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Senaryo2_HastaSayfasiTest extends BaseUiTest {

    @Test
    void hastaSayfasiAciliyorMu() {
        driver.get(baseUrl + "/hastalar");
        assertTrue(driver.getCurrentUrl().contains("hastalar"));
    }
}
