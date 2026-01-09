package com.hastane.hastane_yonetim.unit;

import com.hastane.hastane_yonetim.entity.Department;
import com.hastane.hastane_yonetim.entity.Doktor;
import com.hastane.hastane_yonetim.entity.Hasta;
import com.hastane.hastane_yonetim.entity.Randevu;
import com.hastane.hastane_yonetim.repository.DepartmentRepository;
import com.hastane.hastane_yonetim.repository.DoktorRepository;
import com.hastane.hastane_yonetim.repository.HastaRepository;
import com.hastane.hastane_yonetim.repository.RandevuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "test", roles = {"USER"})
@SpringBootTest
@AutoConfigureMockMvc
class RandevuControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    DoktorRepository doktorRepository;

    @Autowired
    HastaRepository hastaRepository;

    @Autowired
    RandevuRepository randevuRepository;

    @BeforeEach
    void clean() {
        randevuRepository.deleteAll();
        doktorRepository.deleteAll();
        hastaRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Test
    void getAll_returnsRandevuResponses() throws Exception {
        Department dep = departmentRepository.save(
                new Department(null, "TestDepR", "Aciklama")
        );

        Doktor doc = doktorRepository.save(
                new Doktor(null, "Ahmet", "Demir", "Kardiyoloji", dep, java.util.List.of())
        );

        Hasta hasta = hastaRepository.save(
                new Hasta(null, "Ali", "Yilmaz", "0555")
        );

        randevuRepository.save(
                new Randevu(null, LocalDateTime.now().plusDays(1), hasta, doc)
        );

        mockMvc.perform(get("/api/randevular"))
                .andExpect(status().isOk());
    }
}
