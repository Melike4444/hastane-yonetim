package com.hastane.hastane_yonetim.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test: DepartmentController (MockMvc üzerinden)
 * Not: Security varsa login gerektirmesin diye addFilters=false.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class DepartmentControllerIT {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    void list_departments_returns_200() throws Exception {
        mockMvc.perform(get("/api/bolumler"))
                .andExpect(status().isOk());
    }

    @Test
    void create_department_returns_201_or_200() throws Exception {
        // Controller'ında endpoint farklıysa sadece path'i düzeltmen yeterli.
        var body = """
                {"ad":"IT-Bolum","aciklama":"Integration Test"}
                """;

        var result = mockMvc.perform(post("/api/bolumler")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // response JSON ise id'yi doğrula (zorunlu değil)
        // String json = result.getResponse().getContentAsString();
        // System.out.println(json);
    }

    @Test
    void get_department_by_id_returns_2xx_or_404() throws Exception {
        // ID yoksa 404 normal; amaç endpoint'in çalıştığını görmek
        mockMvc.perform(get("/api/bolumler/{id}", 1))
                .andExpect(result -> {
            int s = result.getResponse().getStatus();
            if (!(s >= 200 && s < 300) && !(s >= 400 && s < 500)) {
                throw new AssertionError("Expected 2xx or 4xx but was " + s);
            }
        });
    }
}
