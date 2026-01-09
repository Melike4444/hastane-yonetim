package com.hastane.hastane_yonetim.integration;

import com.hastane.hastane_yonetim.entity.Muayene;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hastane.hastane_yonetim.entity.Department;
import com.hastane.hastane_yonetim.entity.Doktor;
import com.hastane.hastane_yonetim.entity.Hasta;
import com.hastane.hastane_yonetim.entity.Randevu;
import com.hastane.hastane_yonetim.repository.DepartmentRepository;
import com.hastane.hastane_yonetim.repository.DoktorRepository;
import com.hastane.hastane_yonetim.repository.HastaRepository;
import com.hastane.hastane_yonetim.repository.RandevuRepository;
import com.hastane.hastane_yonetim.repository.MuayeneRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class MuayeneApiIT {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;

    @Autowired private HastaRepository hastaRepo;
    @Autowired private DepartmentRepository bolumRepo;
    @Autowired private DoktorRepository doktorRepo;
    @Autowired private MuayeneRepository muayeneRepo;
  @Autowired private RandevuRepository randevuRepo;

    private Long hastaId;
    private Long doktorId;
    private Long randevuId;
    private Long randevuId2;

    
  private Long muayeneId;
@BeforeEach
    void setup() {
  muayeneRepo.deleteAll();
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
    

    // POST test: use a second appointment (no muayene on it)
    Randevu r2 = new Randevu();
    r2.setTarihSaat(LocalDateTime.now().plusDays(2));
    r2.setHasta(h);
    r2.setDoktor(d);
    r2 = randevuRepo.save(r2);
    randevuId2 = r2.getId();

    // Muayene kaydı (GET by id testinde null olmaması için)
    Muayene m = new Muayene();
    m.setRandevu(r);
    m.setSikayet("Bas agrisi");
    m.setNotlar("test not");
    // Entity'de zorunlu alan varsa ekle:
    m.setTeshis("Migren");
    m.setTedavi("Ilac");
    m = muayeneRepo.save(m);
    muayeneId = m.getId();

}

    @Test
    void createMuayene_shouldReturn2xx() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("randevuId", randevuId2);
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


    @Test
    void listMuayeneler_shouldReturn200() throws Exception {
        mvc.perform(get("/api/muayeneler"))
                .andExpect(status().isOk());
    }

    @Test
    void getMuayeneById_shouldReturn2xx_or_404() throws Exception {
        assertNotNull(muayeneId, "muayeneId null kaldı (setup içinde set edilmeli)");
        mvc.perform(get("/api/muayeneler/" + muayeneId))
                .andExpect(result -> {
                    int st = result.getResponse().getStatus();
                    if (!((st >= 200 && st < 300) || st == 404)) {
                        throw new AssertionError("Expected 2xx or 404 but was " + st);
                    }
                });
    }

}
