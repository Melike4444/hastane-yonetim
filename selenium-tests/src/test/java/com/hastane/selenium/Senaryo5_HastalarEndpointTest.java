package com.hastane.selenium;

import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Senaryo5_HastalarEndpointTest {

    @Test
    void sistemAyaktaMi_HomeEndpoint() throws Exception {
        String base = System.getProperty("apiBaseUrl", "http://localhost:8090");
        URL url = new URL(base + "/");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(8000);
        con.setReadTimeout(8000);

        int code = con.getResponseCode();
        assertEquals(200, code, "Home endpoint 200 dönmedi: " + code);
    }
}

