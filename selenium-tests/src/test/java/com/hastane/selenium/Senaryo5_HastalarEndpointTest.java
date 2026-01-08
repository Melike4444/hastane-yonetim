package com.hastane.selenium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Senaryo5_HastalarEndpointTest extends BaseUiTest {

    @Test
    void hastalarEndpointAciliyorMu() {
        driver.get(baseUrl() + "/api/hastalar");
        String src = driver.getPageSource();
        assertNotNull(src);
        assertTrue(src.contains("[") || src.contains("{"),
                "Hastalar endpoint JSON dönmüyor gibi görünüyor.");
    }
}
