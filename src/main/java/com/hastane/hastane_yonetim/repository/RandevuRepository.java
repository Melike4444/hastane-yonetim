package com.hastane.hastane_yonetim.repository;

import com.hastane.hastane_yonetim.entity.Randevu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RandevuRepository extends JpaRepository<Randevu, Long> {

    boolean existsByHasta_Id(Long hastaId);
    void deleteByHasta_Id(Long hastaId);

    boolean existsByDoktor_Id(Long doktorId);
    void deleteByDoktor_Id(Long doktorId);

    // Listeleme sırasında hasta+doktor da gelsin (lazy proxy patlamasın)
    @Query("select r from Randevu r join fetch r.hasta join fetch r.doktor")
    List<Randevu> findAllWithHastaDoktor();
}
