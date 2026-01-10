package com.hastane.selenium;

import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Senaryo5_ApiSmokeTest {

    private static String env(String k, String def) {
        String v = System.getenv(k);
        return (v == null || v.isBlank()) ? def : v;
    }

    @Test
    void apiCalisiyorMu() throws Exception {
        // API testi host (Mac/Jenkins) üzerinde çalışır, bu yüzden localhost default olmalı.
        String rawBase = env("API_BASE_URL", env("BASE_URL", "http://localhost:8080"));
        String path = env("API_PATH", "/");

        URI uri = URI.create(rawBase);

        // URL içinde user:pass@ varsa ayıkla
        String userInfo = uri.getUserInfo(); // admin:admin123 gibi

        URI noAuth = new URI(
                uri.getScheme(),
                null,
                uri.getHost(),
                uri.getPort(),
                uri.getPath(),
                uri.getQuery(),
                uri.getFragment()
        );

        String base = noAuth.toString();
        if (base.endsWith("/")) base = base.substring(0, base.length() - 1);

        String full = base + (path.startsWith("/") ? path : ("/" + path));

        HttpURLConnection con = (HttpURLConnection) new URL(full).openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);
        con.setReadTimeout(10000);

        // Basic auth header: önce userInfo, yoksa BASIC_USER/BASIC_PASS
        if (userInfo == null || userInfo.isBlank()) {
            String u = env("BASIC_USER", "");
            String p = env("BASIC_PASS", "");
            if (!u.isBlank() && !p.isBlank()) userInfo = u + ":" + p;
        }
        if (userInfo != null && !userInfo.isBlank()) {
            String token = Base64.getEncoder().encodeToString(userInfo.getBytes(StandardCharsets.UTF_8));
            con.setRequestProperty("Authorization", "Basic " + token);
        }

        int code = con.getResponseCode();
        assertTrue(code >= 200 && code < 500, "API ayakta olmali (2xx-4xx): " + full + " status=" + code);
    }
}
