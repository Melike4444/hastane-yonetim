package com.hastane.hastane_yonetim.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hastane.hastane_yonetim.entity.Department; // <-- Eğer sende farklıysa importu düzelt
import com.hastane.hastane_yonetim.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // ✅ Security 401/403 bitirir
@Transactional // ✅ rollback
public class DepartmentControllerWebMvcTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired DepartmentRepository departmentRepository;

    private String uniqueName() {
        return "Kardiyoloji-" + UUID.randomUUID();
    }

    @Test
    void create_shouldReturn201() throws Exception {
        String name = uniqueName();

        String body = """
                {"ad":"%s","aciklama":"Kalp"}
                """.formatted(name);

        mockMvc.perform(post("/api/bolumler")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.ad", is(name)))
                .andExpect(jsonPath("$.aciklama", is("Kalp")));
    }

    @Test
    void list_shouldReturn200() throws Exception {
        Department d = new Department();
        d.setAd(uniqueName());
        d.setAciklama("Test");
        departmentRepository.save(d);

        mockMvc.perform(get("/api/bolumler"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        Department d = new Department();
        d.setAd(uniqueName());
        d.setAciklama("Silinecek");
        d = departmentRepository.save(d);

        mockMvc.perform(delete("/api/bolumler/{id}", d.getId()))
                .andExpect(status().isNoContent());

        assertFalse(departmentRepository.existsById(d.getId()));
    }
}
