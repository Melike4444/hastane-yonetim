package com.hastane.hastane_yonetim.unit;

import com.hastane.hastane_yonetim.TestcontainersConfiguration;
import org.springframework.context.annotation.Import;

import com.hastane.hastane_yonetim.controller.DoktorController;
import com.hastane.hastane_yonetim.entity.Doktor;
import com.hastane.hastane_yonetim.service.DoktorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoktorController.class)
class DoktorControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoktorService doktorService;

    @Test
    void getAllDoktorlar_returns200() throws Exception {

        Doktor doktor = Doktor.builder()
                .ad("Ahmet")
                .soyad("Demir")
                .brans("Kardiyoloji")
                .build();

        Mockito.when(doktorService.getAll()).thenReturn(List.of(doktor));

        mockMvc.perform(get("/api/doktorlar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

