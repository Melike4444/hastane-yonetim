package com.hastane.selenium;

import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Senaryo5_ApiSmokeTest {

    private String baseUrl() {
        return System.getenv().getOrDefault("BASE_URL", "http://host.docker.internal:8080");
    }

    @Test
    void apiCalisiyorMu() throws Exception {
        URL url = new URL(baseUrl() + "/");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        int code = con.getResponseCode();
        // Security varsa 401/403 gelebilir, bu da "ayakta" demektir
        assertTrue(code >= 200 && code < 500, "API root beklenmeyen status: " + code);
    }
}
