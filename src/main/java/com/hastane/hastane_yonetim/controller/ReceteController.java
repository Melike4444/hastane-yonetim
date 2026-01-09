package com.hastane.hastane_yonetim.controller;

import com.hastane.hastane_yonetim.dto.ReceteRequest;
import com.hastane.hastane_yonetim.entity.Recete;
import com.hastane.hastane_yonetim.service.ReceteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receteler")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ReceteController {

    private final ReceteService receteService;

    @GetMapping
    public ResponseEntity<List<Recete>> getAll() {
        return ResponseEntity.ok(receteService.tumReceteler());
    }

    @PostMapping
    public ResponseEntity<Recete> create(@Valid @RequestBody ReceteRequest request) {
        Recete saved = receteService.kaydetDto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
