package com.hastane.hastane_yonetim.controller;

import com.hastane.hastane_yonetim.entity.Recete;
import com.hastane.hastane_yonetim.service.ReceteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receteler")
@CrossOrigin(origins = "*")
public class ReceteController {

    private final ReceteService receteService;

    public ReceteController(ReceteService receteService) {
        this.receteService = receteService;
    }

    @GetMapping
    public List<Recete> getAll() {
        return receteService.tumReceteler();
    }

    @PostMapping
    public Recete create(@RequestBody Recete recete) {
        return receteService.kaydet(recete);
    }
}
