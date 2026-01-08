package com.hastane.selenium;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Senaryo5_HastalarEndpointTest extends BaseUiTest {

    @Test
    void uygulamaAciliyorMu() {
        driver.get(baseUrl());
        assertTrue(pageContains("Dashboard") || pageContains("Premium UI"),
                "Ana sayfa (Dashboard) görünmüyor gibi.");
    }
}

