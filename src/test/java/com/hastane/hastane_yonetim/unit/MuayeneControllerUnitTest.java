package com.hastane.hastane_yonetim.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hastane.hastane_yonetim.entity.*; // <-- sende ayrı ayrıysa tek tek import et
import com.hastane.hastane_yonetim.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // ✅ 401/403 olmasın
@Transactional // ✅ test bitince rollback -> DB temiz kalır
public class MuayeneControllerUnitTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Autowired DepartmentRepository departmentRepository;
    @Autowired DoktorRepository doktorRepository;
    @Autowired HastaRepository hastaRepository;
    @Autowired RandevuRepository randevuRepository;

    private String uniq(String prefix) {
        return prefix + "-" + UUID.randomUUID();
    }

    @Test
    void create_returns201() throws Exception {
        // 1) Department (benzersiz ad!)
        Department dep = new Department();
        dep.setAd(uniq("TestDep"));
        dep.setAciklama("Test");
        dep = departmentRepository.save(dep);

        // 2) Doktor
        Doktor doktor = new Doktor();
        doktor.setAd("TestDoktor");
        doktor.setSoyad("A");
        doktor.setBrans("Kardiyoloji");
        doktor.setDepartment(dep);
        doktor = doktorRepository.save(doktor);

        // 3) Hasta
        Hasta hasta = new Hasta();
        hasta.setAd("TestHasta");
        hasta.setSoyad("B");
        hasta.setTelefon("05000000000");
        hasta = hastaRepository.save(hasta);

        // 4) Randevu
        Randevu randevu = new Randevu();
        randevu.setDoktor(doktor);
        randevu.setHasta(hasta);
        randevu.setTarihSaat(LocalDateTime.now().plusDays(1));
        randevu = randevuRepository.save(randevu);

        // 5) Muayene create request (randevuId kesin var)
        String body = """
                {
                  "randevuId": %d,
                  "sikayet": "Bas agrisi",
                  "teshis": "Migren",
                  "tedavi": "Ilac",
                  "notlar": "Kontrol 1 hafta"
                }
                """.formatted(randevu.getId());

        mockMvc.perform(post("/api/muayeneler")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.sikayet", is("Bas agrisi")))
                .andExpect(jsonPath("$.teshis", is("Migren")))
                .andExpect(jsonPath("$.tedavi", is("Ilac")));
    }
}
