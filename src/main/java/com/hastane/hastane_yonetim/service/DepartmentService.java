package com.hastane.hastane_yonetim.service;

import com.hastane.hastane_yonetim.dto.DepartmentRequest;
import com.hastane.hastane_yonetim.dto.DepartmentResponse;
import com.hastane.hastane_yonetim.entity.Department;
import com.hastane.hastane_yonetim.exception.ResourceNotFoundException;
import com.hastane.hastane_yonetim.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentResponse create(DepartmentRequest request) {
        Department department = Department.builder()
                .ad(request.getAd())
                .aciklama(request.getAciklama())
                .build();

        Department saved = departmentRepository.save(department);
        return toResponse(saved);
    }

    public List<DepartmentResponse> list() {
        return departmentRepository.findAll().stream().map(this::toResponse).toList();
    }

    public DepartmentResponse get(Long id) {
        Department d = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + id));
        return toResponse(d);
    }

    public DepartmentResponse update(Long id, DepartmentRequest request) {
        Department d = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + id));

        d.setAd(request.getAd());
        d.setAciklama(request.getAciklama());

        Department saved = departmentRepository.save(d);
        return toResponse(saved);
    }

    public void delete(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found: " + id);
        }
        departmentRepository.deleteById(id);
    }

    private DepartmentResponse toResponse(Department d) {
        return DepartmentResponse.builder()
                .id(d.getId())
                .ad(d.getAd())
                .aciklama(d.getAciklama())
                .build();
    }
}
