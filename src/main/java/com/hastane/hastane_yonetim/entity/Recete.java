package com.hastane.hastane_yonetim.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "receteler")
public class Recete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // İlişki yerine şimdilik düz alan: kırılma riskini azaltır
    private Long hastaId;
    private Long doktorId;

    @Column(length = 2000)
    private String ilaclar;   // örn: "Parol 500mg, 2x1; ..."

    @Column(length = 2000)
    private String notlar;    // örn: "Tok karnına, 5 gün"

    private LocalDateTime tarihSaat;

    public Recete() {}

    public Recete(Long hastaId, Long doktorId, String ilaclar, String notlar, LocalDateTime tarihSaat) {
        this.hastaId = hastaId;
        this.doktorId = doktorId;
        this.ilaclar = ilaclar;
        this.notlar = notlar;
        this.tarihSaat = tarihSaat;
    }

    public Long getId() { return id; }

    public Long getHastaId() { return hastaId; }
    public void setHastaId(Long hastaId) { this.hastaId = hastaId; }

    public Long getDoktorId() { return doktorId; }
    public void setDoktorId(Long doktorId) { this.doktorId = doktorId; }

    public String getIlaclar() { return ilaclar; }
    public void setIlaclar(String ilaclar) { this.ilaclar = ilaclar; }

    public String getNotlar() { return notlar; }
    public void setNotlar(String notlar) { this.notlar = notlar; }

    public LocalDateTime getTarihSaat() { return tarihSaat; }
    public void setTarihSaat(LocalDateTime tarihSaat) { this.tarihSaat = tarihSaat; }
}
