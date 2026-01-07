package com.hastane.hastane_yonetim.service;

import com.hastane.hastane_yonetim.entity.Recete;
import com.hastane.hastane_yonetim.repository.ReceteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReceteService {

    private final ReceteRepository receteRepository;

    public ReceteService(ReceteRepository receteRepository) {
        this.receteRepository = receteRepository;
    }

    public List<Recete> tumReceteler() {
        return receteRepository.findAll();
    }

    public Recete kaydet(Recete recete) {
        // tarih boş geldiyse otomatik bas
        if (recete.getTarihSaat() == null) {
            recete.setTarihSaat(LocalDateTime.now());
        }
        return receteRepository.save(recete);
    }
}
