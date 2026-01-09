package com.hastane.hastane_yonetim.unit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test", roles = {"ADMIN"})
class DepartmentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void create_shouldReturn201() throws Exception {
        String body = """
          {"ad":"Nöroloji","aciklama":"Sinir"}
        """;

        mockMvc.perform(
                post("/api/bolumler")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        ).andExpect(status().isCreated());
    }

    @Test
    void list_shouldReturn200() throws Exception {
        // önce veri oluştur
        String body = """
          {"ad":"Ortopedi","aciklama":"Kemik"}
        """;

        mockMvc.perform(
                post("/api/bolumler")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        ).andExpect(status().isCreated());

        // sonra liste
        mockMvc.perform(get("/api/bolumler"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        // create
        String body = """
          {"ad":"Kardiyoloji","aciklama":"Kalp"}
        """;

        String createdJson = mockMvc.perform(
                post("/api/bolumler")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        ).andExpect(status().isCreated())
         .andReturn()
         .getResponse()
         .getContentAsString();

        JsonNode node = objectMapper.readTree(createdJson);
        long id = node.get("id").asLong();

        // delete
        mockMvc.perform(delete("/api/bolumler/" + id).with(csrf()))
                .andExpect(status().isNoContent());
    }
}
