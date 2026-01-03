package com.hastane.hastane_yonetim.controller;

import com.hastane.hastane_yonetim.dto.RandevuResponse;
import com.hastane.hastane_yonetim.dto.RandevuCreateRequest;
import com.hastane.hastane_yonetim.service.RandevuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/randevular")
@CrossOrigin
public class RandevuController {

    private final RandevuService randevuService;

    public RandevuController(RandevuService randevuService) {
        this.randevuService = randevuService;
    }

    @GetMapping
    public List<RandevuResponse> getAll() {
        return randevuService.getAll();
    }

    @PostMapping
    public ResponseEntity<RandevuResponse> create(@RequestBody RandevuCreateRequest req) {
        return ResponseEntity.ok(randevuService.create(req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        randevuService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
