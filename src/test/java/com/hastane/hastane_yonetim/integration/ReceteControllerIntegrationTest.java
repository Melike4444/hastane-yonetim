
package com.hastane.hastane_yonetim.integration;

import com.hastane.hastane_yonetim.entity.Recete;
import com.hastane.hastane_yonetim.repository.ReceteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@Disabled("Geçici olarak CI ortamında devre dışı")
class ReceteControllerIntegrationTest {
    ...
}

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ReceteControllerIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ReceteRepository receteRepository;

    @BeforeEach
    void setup() {
        receteRepository.deleteAll();
        Recete r = new Recete(
                1L,
                1L,
                "Parol 500mg 2x1",
                "Tok karnına 5 gün",
                LocalDateTime.now()
        );
        receteRepository.save(r);
    }

    @Test
    void getAllReceteler_returnsList() throws Exception {
        mockMvc.perform(get("/api/receteler"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].ilaclar").value("Parol 500mg 2x1"));
    }
}
