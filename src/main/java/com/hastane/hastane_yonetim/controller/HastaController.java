package com.hastane.hastane_yonetim.controller;

import com.hastane.hastane_yonetim.entity.Hasta;
import com.hastane.hastane_yonetim.service.HastaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hastalar")
@CrossOrigin(origins = "*")
public class HastaController {

    private final HastaService hastaService;

    public HastaController(HastaService hastaService) {
        this.hastaService = hastaService;
    }

    @GetMapping
    public ResponseEntity<List<Hasta>> getAll() {
        return ResponseEntity.ok(hastaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hasta> getById(@PathVariable Long id) {
        return ResponseEntity.ok(hastaService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Hasta> create(@RequestBody Hasta hasta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hastaService.create(hasta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hasta> update(@PathVariable Long id, @RequestBody Hasta hasta) {
        return ResponseEntity.ok(hastaService.update(id, hasta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hastaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
