package com.hastane.hastane_yonetim.service;

import com.hastane.hastane_yonetim.entity.Hasta;
import com.hastane.hastane_yonetim.repository.HastaRepository;
import com.hastane.hastane_yonetim.repository.RandevuRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class HastaService {

    private final HastaRepository hastaRepository;
    private final RandevuRepository randevuRepository;

    public HastaService(HastaRepository hastaRepository, RandevuRepository randevuRepository) {
        this.hastaRepository = hastaRepository;
        this.randevuRepository = randevuRepository;
    }

    public List<Hasta> getAll() {
        return hastaRepository.findAll();
    }

    public Hasta getById(Long id) {
        return hastaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hasta bulunamadı: " + id));
    }

    public Hasta create(Hasta hasta) {
        hasta.setId(null);
        return hastaRepository.save(hasta);
    }

    public Hasta update(Long id, Hasta hasta) {
        Hasta existing = getById(id);
        existing.setAd(hasta.getAd());
        existing.setSoyad(hasta.getSoyad());
        existing.setTelefon(hasta.getTelefon());
        return hastaRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Hasta hasta = getById(id);

        if (randevuRepository.existsByHasta_Id(id)) {
            randevuRepository.deleteByHasta_Id(id);
        }

        hastaRepository.delete(hasta);
    }
}
