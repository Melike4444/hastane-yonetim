package com.hastane.hastane_yonetim.unit;

import com.hastane.hastane_yonetim.TestcontainersConfiguration;
import org.springframework.context.annotation.Import;

import com.hastane.hastane_yonetim.controller.HastaController;
import com.hastane.hastane_yonetim.entity.Hasta;
import com.hastane.hastane_yonetim.service.HastaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HastaController.class)
class HastaControllerUnitTest {

    @Autowired MockMvc mockMvc;

    @MockBean HastaService hastaService;

    @Test
    void getAll_returnsList() throws Exception {
        Mockito.when(hastaService.getAll())
                .thenReturn(List.of(new Hasta(1L,"Ali","Yilmaz","0555")));

        mockMvc.perform(get("/api/hastalar"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].ad").value("Ali"));
    }

    @Test
    void create_returnsCreatedEntity() throws Exception {
        Mockito.when(hastaService.create(any(Hasta.class)))
                .thenReturn(new Hasta(10L,"Ayse","Kara","0500"));

        mockMvc.perform(post("/api/hastalar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ad\":\"Ayse\",\"soyad\":\"Kara\",\"telefon\":\"0500\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.ad").value("Ayse"));
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/api/hastalar/5"))
                .andExpect(status().isNoContent());

        Mockito.verify(hastaService).delete(5L);
    }
}
