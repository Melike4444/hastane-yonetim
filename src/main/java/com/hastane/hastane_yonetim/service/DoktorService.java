package com.hastane.hastane_yonetim.service;

import com.hastane.hastane_yonetim.entity.Department;
import com.hastane.hastane_yonetim.entity.Doktor;
import com.hastane.hastane_yonetim.repository.DepartmentRepository;
import com.hastane.hastane_yonetim.repository.DoktorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoktorService {

    private final DoktorRepository doktorRepository;
    private final DepartmentRepository departmentRepository;

    public List<Doktor> getAll() {
        return doktorRepository.findAll();
    }

    public Doktor create(String ad, String soyad, String brans, Long departmentId) {
        Department department = null;

        if (departmentId != null) {
            department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department bulunamadı"));
        }

        Doktor doktor = Doktor.builder()
                .ad(ad)
                .soyad(soyad)
                .brans(brans)
                .department(department)
                .build();

        return doktorRepository.save(doktor);
    }

    // ✅ SİLME METODU
    public void delete(Long id) {
        doktorRepository.deleteById(id);
    }
}
