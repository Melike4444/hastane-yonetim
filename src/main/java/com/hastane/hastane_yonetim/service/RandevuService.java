package com.hastane.hastane_yonetim.service;

import com.hastane.hastane_yonetim.dto.RandevuCreateRequest;
import com.hastane.hastane_yonetim.dto.RandevuResponse;
import com.hastane.hastane_yonetim.entity.Doktor;
import com.hastane.hastane_yonetim.entity.Hasta;
import com.hastane.hastane_yonetim.entity.Randevu;
import com.hastane.hastane_yonetim.repository.DoktorRepository;
import com.hastane.hastane_yonetim.repository.HastaRepository;
import com.hastane.hastane_yonetim.repository.RandevuRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RandevuService {

    private final RandevuRepository randevuRepository;
    private final HastaRepository hastaRepository;
    private final DoktorRepository doktorRepository;

    public RandevuService(RandevuRepository randevuRepository,
                          HastaRepository hastaRepository,
                          DoktorRepository doktorRepository) {
        this.randevuRepository = randevuRepository;
        this.hastaRepository = hastaRepository;
        this.doktorRepository = doktorRepository;
    }

    @Transactional(readOnly = true)
    public List<RandevuResponse> getAll() {
        return randevuRepository.findAllWithHastaDoktor()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public RandevuResponse create(RandevuCreateRequest req) {
        if (req.getTarihSaat() == null || req.getHastaId() == null || req.getDoktorId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "tarihSaat, hastaId ve doktorId zorunlu");
        }

        Hasta hasta = hastaRepository.findById(req.getHastaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hasta bulunamadı: " + req.getHastaId()));

        Doktor doktor = doktorRepository.findById(req.getDoktorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doktor bulunamadı: " + req.getDoktorId()));

        Randevu r = new Randevu();
        r.setTarihSaat(req.getTarihSaat());
        r.setHasta(hasta);
        r.setDoktor(doktor);

        return toResponse(randevuRepository.save(r));
    }

    @Transactional
    public void delete(Long id) {
        if (!randevuRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Randevu bulunamadı: " + id);
        }
        randevuRepository.deleteById(id);
    }

    private RandevuResponse toResponse(Randevu r) {
        return new RandevuResponse(
                r.getId(),
                r.getTarihSaat(),
                r.getHasta() != null ? r.getHasta().getId() : null,
                r.getHasta() != null ? r.getHasta().getAd() : null,
                r.getHasta() != null ? r.getHasta().getSoyad() : null,
                r.getDoktor() != null ? r.getDoktor().getId() : null,
                r.getDoktor() != null ? r.getDoktor().getAd() : null,
                r.getDoktor() != null ? r.getDoktor().getSoyad() : null,
                r.getDoktor() != null ? r.getDoktor().getBrans() : null
        );
    }
}
