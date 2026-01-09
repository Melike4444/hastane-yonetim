package com.hastane.hastane_yonetim.unit;

import com.hastane.hastane_yonetim.entity.Department;
import com.hastane.hastane_yonetim.entity.Doktor;
import com.hastane.hastane_yonetim.repository.DepartmentRepository;
import com.hastane.hastane_yonetim.repository.DoktorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "test", roles = {"USER"})
@SpringBootTest
@AutoConfigureMockMvc
class DoktorControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    DoktorRepository doktorRepository;

    @BeforeEach
    void clean() {
        doktorRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Test
    void getAllDoktorlar_returns200() throws Exception {
        Department dep = departmentRepository.save(
                new Department(null, "TestDepD", "Aciklama")
        );

        doktorRepository.save(
                new Doktor(null, "Mehmet", "Kaya", "Kardiyoloji", dep, java.util.List.of())
        );

        mockMvc.perform(get("/api/doktorlar"))
                .andExpect(status().isOk());
    }
}
