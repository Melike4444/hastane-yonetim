package com.hastane.selenium;

import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Senaryo5_HastalarEndpointTest {

    @Test
    void hastalarEndpointCalisiyorMu() throws Exception {
        URL url = new URL("http://localhost:8080/api/hastalar");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        int code = con.getResponseCode();
        assertEquals(200, code, "Hastalar endpoint 200 dönmedi: " + code);
    }
}

