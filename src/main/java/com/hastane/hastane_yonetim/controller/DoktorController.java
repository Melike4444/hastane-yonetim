package com.hastane.hastane_yonetim.controller;

import com.hastane.hastane_yonetim.dto.DoktorRequest;
import com.hastane.hastane_yonetim.entity.Doktor;
import com.hastane.hastane_yonetim.service.DoktorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doktorlar")
@RequiredArgsConstructor
public class DoktorController {

    private final DoktorService doktorService;

    @GetMapping
    public ResponseEntity<List<Doktor>> getAll() {
        return ResponseEntity.ok(doktorService.getAll());
    }

    @PostMapping
    public ResponseEntity<Doktor> create(@Valid @RequestBody DoktorRequest request) {

        Doktor doktor = doktorService.create(
                request.getAd(),
                request.getSoyad(),
                request.getBrans(),
                request.getDepartmentId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(doktor);
    }
}

