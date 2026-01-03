package com.hastane.hastane_yonetim.repository;

import com.hastane.hastane_yonetim.entity.Muayene;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MuayeneRepository extends JpaRepository<Muayene, Long> {
    Optional<Muayene> findByRandevuId(Long randevuId);
    boolean existsByRandevuId(Long randevuId);
}

