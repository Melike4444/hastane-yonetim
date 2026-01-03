package com.hastane.hastane_yonetim.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hastane.hastane_yonetim.TestcontainersConfiguration;
import com.hastane.hastane_yonetim.entity.Department;
import com.hastane.hastane_yonetim.entity.Doktor;
import com.hastane.hastane_yonetim.entity.Hasta;
import com.hastane.hastane_yonetim.repository.DepartmentRepository;
import com.hastane.hastane_yonetim.repository.DoktorRepository;
import com.hastane.hastane_yonetim.repository.HastaRepository;
import com.hastane.hastane_yonetim.repository.MuayeneRepository;
import com.hastane.hastane_yonetim.repository.RandevuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class HastaApiIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private MuayeneRepository muayeneRepository;
    @Autowired private RandevuRepository randevuRepository;
    @Autowired private HastaRepository hastaRepository;
    @Autowired private DoktorRepository doktorRepository;
    @Autowired private DepartmentRepository departmentRepository;

    @BeforeEach
    void cleanDb() {
        // ✅ FK sırası: muayene -> randevu -> hasta/doktor -> department
        muayeneRepository.deleteAll();
        randevuRepository.deleteAll();
        hastaRepository.deleteAll();
        doktorRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Test
    void getAllHastalar_returns200() throws Exception {
        // seed
        hastaRepository.save(new Hasta(null, "Ali", "Yilmaz", "05550000000"));

        mockMvc.perform(get("/api/hastalar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ad").value("Ali"));
    }

    @Test
    void createHasta_returns201() throws Exception {
        var yeni = new Hasta(null, "Ayse", "Kaya", "05551112233");

        mockMvc.perform(post("/api/hastalar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(yeni)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ad").value("Ayse"))
                .andExpect(jsonPath("$.soyad").value("Kaya"));
    }
}

