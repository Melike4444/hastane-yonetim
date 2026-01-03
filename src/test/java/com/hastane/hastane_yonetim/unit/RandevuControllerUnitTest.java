package com.hastane.hastane_yonetim.unit;

import com.hastane.hastane_yonetim.TestcontainersConfiguration;
import org.springframework.context.annotation.Import;

import com.hastane.hastane_yonetim.controller.RandevuController;
import com.hastane.hastane_yonetim.dto.RandevuResponse;
import com.hastane.hastane_yonetim.service.RandevuService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RandevuController.class)
class RandevuControllerUnitTest {

    @Autowired MockMvc mockMvc;

    @MockBean RandevuService randevuService;

    @Test
    void getAll_returnsRandevuResponses() throws Exception {
        Mockito.when(randevuService.getAll()).thenReturn(List.of(
                new RandevuResponse(1L, LocalDateTime.parse("2026-01-10T14:30:00"),
                        1L,"Ali","Yilmaz", 1L,"Ahmet","Demir","Kardiyoloji")
        ));

        mockMvc.perform(get("/api/randevular"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].hastaAd").value("Ali"))
                .andExpect(jsonPath("$[0].doktorAd").value("Ahmet"));
    }
}
