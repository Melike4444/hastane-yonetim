package com.hastane.hastane_yonetim.unit;

import com.hastane.hastane_yonetim.TestcontainersConfiguration;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hastane.hastane_yonetim.controller.MuayeneController;
import com.hastane.hastane_yonetim.dto.MuayeneRequest;
import com.hastane.hastane_yonetim.dto.MuayeneResponse;
import com.hastane.hastane_yonetim.service.MuayeneService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MuayeneController.class)
class MuayeneControllerUnitTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private MuayeneService muayeneService;

    @Test
    void getAll_returns200() throws Exception {
        Mockito.when(muayeneService.getAll()).thenReturn(List.of(
                MuayeneResponse.builder().id(1L).randevuId(10L).teshis("Grip").build()
        ));

        mockMvc.perform(get("/api/muayeneler"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teshis").value("Grip"));
    }

    @Test
    void create_returns201() throws Exception {
        MuayeneRequest req = MuayeneRequest.builder()
                .randevuId(10L)
                .muayeneTarihi(LocalDateTime.of(2026, 1, 3, 10, 0))
                .teshis("Soğuk algınlığı")
                .build();

        Mockito.when(muayeneService.create(any(MuayeneRequest.class)))
                .thenReturn(MuayeneResponse.builder()
                        .id(1L)
                        .randevuId(10L)
                        .teshis("Soğuk algınlığı")
                        .build());

        mockMvc.perform(post("/api/muayeneler")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.teshis").value("Soğuk algınlığı"));
    }
}
