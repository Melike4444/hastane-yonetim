package com.hastane.hastane_yonetim.controller;

import com.hastane.hastane_yonetim.dto.DepartmentRequest;
import com.hastane.hastane_yonetim.dto.DepartmentResponse;
import com.hastane.hastane_yonetim.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bolumler")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public DepartmentResponse create(@Valid @RequestBody DepartmentRequest request) {
        return departmentService.create(request);
    }

    @GetMapping
    public List<DepartmentResponse> list() { // 200
        return departmentService.list();
    }

    @GetMapping("/{id}")
    public DepartmentResponse get(@PathVariable Long id) { // 200
        return departmentService.get(id);
    }

    @PutMapping("/{id}")
    public DepartmentResponse update(@PathVariable Long id, @Valid @RequestBody DepartmentRequest request) { // 200
        return departmentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void delete(@PathVariable Long id) {
        departmentService.delete(id);
    }
}
