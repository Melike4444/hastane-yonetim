package com.hastane.hastane_yonetim.controller;

import com.hastane.hastane_yonetim.TestcontainersConfiguration;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hastane.hastane_yonetim.dto.DepartmentRequest;
import com.hastane.hastane_yonetim.dto.DepartmentResponse;
import com.hastane.hastane_yonetim.service.DepartmentService;
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

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean DepartmentService departmentService;

    @Test
    void create_shouldReturn201() throws Exception {
        DepartmentRequest req = DepartmentRequest.builder().ad("Kardiyoloji").aciklama("Kalp").build();
        DepartmentResponse res = DepartmentResponse.builder().id(1L).ad("Kardiyoloji").aciklama("Kalp").build();

        Mockito.when(departmentService.create(any())).thenReturn(res);

        mockMvc.perform(post("/api/bolumler")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ad").value("Kardiyoloji"));
    }

    @Test
    void list_shouldReturn200() throws Exception {
        Mockito.when(departmentService.list()).thenReturn(List.of(
                DepartmentResponse.builder().id(1L).ad("Kardiyoloji").aciklama("Kalp").build()
        ));

        mockMvc.perform(get("/api/bolumler"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].ad").value("Kardiyoloji"));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        Mockito.doNothing().when(departmentService).delete(eq(1L));

        mockMvc.perform(delete("/api/bolumler/1"))
                .andExpect(status().isNoContent());
    }
}
