package com.hastane.selenium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Senaryo2_DoktorlarEndpointTest extends BaseUiTest {

    @Test
    void doktorlarEndpointAciliyorMu() {
        driver.get(baseUrl() + "/api/doktorlar");
        String src = driver.getPageSource();
        assertNotNull(src);
        assertTrue(src.contains("[") || src.contains("{"),
                "Doktorlar endpoint JSON dönmüyor gibi görünüyor.");
    }
}
