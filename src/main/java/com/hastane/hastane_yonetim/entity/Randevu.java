package com.hastane.hastane_yonetim.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "randevu")
public class Randevu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime tarihSaat;

    @JsonIgnoreProperties({"hibernateLazyInitializer","handler","randevular"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hasta_id")
    private Hasta hasta;

    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doktor_id")
    private Doktor doktor;

    public Randevu() {}

    public Randevu(Long id, LocalDateTime tarihSaat, Hasta hasta, Doktor doktor) {
        this.id = id;
        this.tarihSaat = tarihSaat;
        this.hasta = hasta;
        this.doktor = doktor;
    }

    public Long getId() { return id; }
    public LocalDateTime getTarihSaat() { return tarihSaat; }
    public Hasta getHasta() { return hasta; }
    public Doktor getDoktor() { return doktor; }

    public void setId(Long id) { this.id = id; }
    public void setTarihSaat(LocalDateTime tarihSaat) { this.tarihSaat = tarihSaat; }
    public void setHasta(Hasta hasta) { this.hasta = hasta; }
    public void setDoktor(Doktor doktor) { this.doktor = doktor; }
}
