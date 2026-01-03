package com.hastane.hastane_yonetim.integration;

import com.hastane.hastane_yonetim.TestcontainersConfiguration;
import com.hastane.hastane_yonetim.entity.Department;
import com.hastane.hastane_yonetim.entity.Doktor;
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
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class DoktorApiIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private MuayeneRepository muayeneRepository;
    @Autowired private RandevuRepository randevuRepository;
    @Autowired private DoktorRepository doktorRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired(required = false) private HastaRepository hastaRepository; // projende varsa

    @BeforeEach
    void cleanDb() {
        // ✅ FK sırası (en bağımlıdan en bağımsıza)
        muayeneRepository.deleteAll();
        randevuRepository.deleteAll();
        doktorRepository.deleteAll();
        departmentRepository.deleteAll();
        if (hastaRepository != null) {
            hastaRepository.deleteAll();
        }
    }

    @Test
    void getAllDoktorlar_returns200() throws Exception {
        // Test verisi ekleyelim (liste boş gelmesin)
        Department dep = new Department();
        dep.setAd("Kardiyoloji");
        dep.setAciklama("Kalp ve damar hastalıkları");
        dep = departmentRepository.save(dep);

        Doktor d = new Doktor();
        d.setAd("Ahmet");
        d.setSoyad("Demir");
        d.setBrans("Kardiyoloji");
        d.setDepartment(dep);
        doktorRepository.save(d);

        mockMvc.perform(get("/api/doktorlar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(1)));
    }
}
