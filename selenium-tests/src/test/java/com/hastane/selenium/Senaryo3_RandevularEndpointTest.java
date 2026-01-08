package com.hastane.selenium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Senaryo3_RandevularEndpointTest extends BaseUiTest {

    @Test
    void randevularEndpointAciliyorMu() {
        driver.get(baseUrl() + "/api/randevular");
        String src = driver.getPageSource();
        assertNotNull(src);
        assertTrue(src.contains("[") || src.contains("{"),
                "Randevular endpoint JSON dönmüyor gibi görünüyor.");
    }
}
