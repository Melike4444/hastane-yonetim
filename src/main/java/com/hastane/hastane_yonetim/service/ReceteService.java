package com.hastane.hastane_yonetim.service;

import com.hastane.hastane_yonetim.dto.ReceteRequest;
import com.hastane.hastane_yonetim.entity.Recete;
import com.hastane.hastane_yonetim.repository.ReceteRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class ReceteService {

    private final ReceteRepository receteRepository;

    public ReceteService(ReceteRepository receteRepository) {
        this.receteRepository = receteRepository;
    }

    public List<Recete> tumReceteler() {
        return receteRepository.findAll();
    }

    public Recete kaydetDto(ReceteRequest request) {

        if (request.getDoktorId() == null || request.getHastaId() == null) {
            throw new ResponseStatusException(BAD_REQUEST, "doktorId ve hastaId zorunludur");
        }
        if (request.getIlaclar() == null || request.getIlaclar().isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "ilaclar zorunludur");
        }

        Recete recete = new Recete();
        recete.setDoktorId(request.getDoktorId());
        recete.setHastaId(request.getHastaId());
        recete.setIlaclar(request.getIlaclar());
        recete.setNotlar(request.getNotlar());

        if (request.getTarihSaat() == null) {
            recete.setTarihSaat(LocalDateTime.now());
        } else {
            recete.setTarihSaat(request.getTarihSaat());
        }

        return receteRepository.save(recete);
    }
}
