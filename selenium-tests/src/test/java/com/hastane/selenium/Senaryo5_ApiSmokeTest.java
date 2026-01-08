package com.hastane.selenium;

import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class Senaryo5_ApiSmokeTest {

    @Test
    void apiCalisiyorMu() throws Exception {
        URL url = new URL("http://hastane-yonetim-app:8080/");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        int code = con.getResponseCode();
        assertTrue(code >= 200 && code < 500, "API root beklenmeyen status: " + code);
    }
}
