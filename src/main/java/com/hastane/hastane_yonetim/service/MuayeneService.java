package com.hastane.hastane_yonetim.service;

import com.hastane.hastane_yonetim.dto.MuayeneRequest;
import com.hastane.hastane_yonetim.dto.MuayeneResponse;
import com.hastane.hastane_yonetim.entity.Muayene;
import com.hastane.hastane_yonetim.entity.Randevu;
import com.hastane.hastane_yonetim.repository.MuayeneRepository;
import com.hastane.hastane_yonetim.repository.RandevuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MuayeneService {

    private final MuayeneRepository muayeneRepository;
    private final RandevuRepository randevuRepository;

    public List<MuayeneResponse> getAll() {
        return muayeneRepository.findAll().stream().map(this::toResponse).toList();
    }

    public MuayeneResponse getById(Long id) {
        Muayene muayene = muayeneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Muayene bulunamadı: " + id));
        return toResponse(muayene);
    }

    public MuayeneResponse create(MuayeneRequest request) {
        if (request.getRandevuId() == null) {
            throw new RuntimeException("randevuId zorunludur.");
        }

        if (muayeneRepository.existsByRandevuId(request.getRandevuId())) {
            throw new RuntimeException("Bu randevu için zaten muayene kaydı var. randevuId=" + request.getRandevuId());
        }

        Randevu randevu = randevuRepository.findById(request.getRandevuId())
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı: " + request.getRandevuId()));

        Muayene muayene = Muayene.builder()
                .randevu(randevu)
                .muayeneTarihi(request.getMuayeneTarihi() != null ? request.getMuayeneTarihi() : LocalDateTime.now())
                .sikayet(request.getSikayet())
                .teshis(request.getTeshis())
                .tedavi(request.getTedavi())
                .notlar(request.getNotlar())
                .build();

        return toResponse(muayeneRepository.save(muayene));
    }

    public MuayeneResponse update(Long id, MuayeneRequest request) {
        Muayene muayene = muayeneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Muayene bulunamadı: " + id));

        // randevu değişimini kapatmak daha güvenli
        if (request.getMuayeneTarihi() != null) muayene.setMuayeneTarihi(request.getMuayeneTarihi());
        if (request.getSikayet() != null) muayene.setSikayet(request.getSikayet());
        if (request.getTeshis() != null) muayene.setTeshis(request.getTeshis());
        if (request.getTedavi() != null) muayene.setTedavi(request.getTedavi());
        if (request.getNotlar() != null) muayene.setNotlar(request.getNotlar());

        return toResponse(muayeneRepository.save(muayene));
    }

    public void delete(Long id) {
        if (!muayeneRepository.existsById(id)) {
            throw new RuntimeException("Muayene bulunamadı: " + id);
        }
        muayeneRepository.deleteById(id);
    }

    private MuayeneResponse toResponse(Muayene muayene) {
        Long randevuId = (muayene.getRandevu() != null) ? muayene.getRandevu().getId() : null;

        return MuayeneResponse.builder()
                .id(muayene.getId())
                .muayeneTarihi(muayene.getMuayeneTarihi())
                .sikayet(muayene.getSikayet())
                .teshis(muayene.getTeshis())
                .tedavi(muayene.getTedavi())
                .notlar(muayene.getNotlar())
                .randevuId(randevuId)
                .build();
    }
}

