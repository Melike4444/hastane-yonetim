package com.hastane.hastane_yonetim.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hastane.hastane_yonetim.entity.Hasta;
import com.hastane.hastane_yonetim.repository.HastaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "test", roles = {"USER"})
@SpringBootTest
@AutoConfigureMockMvc
class HastaControllerUnitTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired HastaRepository hastaRepository;

    @BeforeEach
    void clean() {
        hastaRepository.deleteAll();
    }

    @Test
    void getAll_returnsList() throws Exception {
        hastaRepository.save(new Hasta(null, "Ali", "Yilmaz", "05550000000"));

        mockMvc.perform(get("/api/hastalar"))
                .andExpect(status().isOk());
    }

    @Test
    void create_returnsCreatedEntity() throws Exception {
        // loguna göre body örneği: {"ad":"Ayse","soyad":"Kara","telefon":"0500"}
        String body = objectMapper.writeValueAsString(
                java.util.Map.of("ad", "Ayse", "soyad", "Kara", "telefon", "0500")
        );

        mockMvc.perform(post("/api/hastalar")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    void delete_returns204() throws Exception {
        Hasta h = hastaRepository.save(new Hasta(null, "Del", "User", "000"));

        mockMvc.perform(delete("/api/hastalar/{id}", h.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
