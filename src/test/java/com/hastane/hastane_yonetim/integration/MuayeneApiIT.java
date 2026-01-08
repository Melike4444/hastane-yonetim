package com.hastane.hastane_yonetim.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MuayeneApiIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HastaRepository hastaRepo;

    @Autowired
    private DepartmentRepository bolumRepo;

    @Autowired
    private DoktorRepository doktorRepo;

    @Autowired
    private RandevuRepository randevuRepo;

    private Long hastaId;
    private Long doktorId;
    private Long randevuId;

    @BeforeEach
    void setup() {
        randevuRepo.deleteAll();
        doktorRepo.deleteAll();
        bolumRepo.deleteAll();
        hastaRepo.deleteAll();

        Hasta h = new Hasta();
        h.setAd("Ali");
        h.setSoyad("Yilmaz");
        h.setTelefon("05550000000");
        h = hastaRepo.save(h);
        hastaId = h.getId();

        Department b = new Department();
        b.setAd("Kardiyoloji");
        b.setAciklama("Kalp ve damar");
        b = bolumRepo.save(b);

        Doktor d = new Doktor();
        d.setAd("Ahmet");
        d.setSoyad("Demir");
        d.setBrans("Kardiyoloji");
        d.setDepartment(b);
        d = doktorRepo.save(d);
        doktorId = d.getId();

        Randevu r = new Randevu();
        r.setTarihSaat(LocalDateTime.now().plusDays(1));
        r.setHasta(h);
        r.setDoktor(d);
        r = randevuRepo.save(r);
        randevuId = r.getId();
    }

    @Test
    void createMuayene_shouldReturn201() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("randevuId", randevuId);
        body.put("hastaId", hastaId);
        body.put("doktorId", doktorId);
        body.put("sikayet", "Bas agrisi");
        body.put("tani", "Migren");
        body.put("not", "test");

        mvc.perform(
                post("/api/muayeneler")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body))
        ).andExpect(status().isCreated());
    }
}

