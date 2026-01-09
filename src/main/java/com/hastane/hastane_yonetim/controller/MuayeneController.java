package com.hastane.hastane_yonetim.controller;

import com.hastane.hastane_yonetim.dto.MuayeneRequest;
import com.hastane.hastane_yonetim.dto.MuayeneResponse;
import com.hastane.hastane_yonetim.service.MuayeneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/muayeneler")
@RequiredArgsConstructor
public class MuayeneController {

    private final MuayeneService muayeneService;

    @GetMapping
    public ResponseEntity<List<MuayeneResponse>> getAll() {
        return ResponseEntity.ok(muayeneService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MuayeneResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(muayeneService.getById(id));
    }

    @PostMapping
    public ResponseEntity<MuayeneResponse> create(@RequestBody MuayeneRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(muayeneService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MuayeneResponse> update(@PathVariable Long id,
                                                  @RequestBody MuayeneRequest request) {
        return ResponseEntity.ok(muayeneService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        muayeneService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
