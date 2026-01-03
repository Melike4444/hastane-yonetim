package com.hastane.hastane_yonetim.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hastalar")
public class Hasta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ad;
    private String soyad;
    private String telefon;

    @JsonIgnore
    @OneToMany(mappedBy = "hasta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Randevu> randevular = new ArrayList<>();

    public Hasta() {}

    public Hasta(Long id, String ad, String soyad, String telefon) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
        this.telefon = telefon;
    }

    public Long getId() { return id; }
    public String getAd() { return ad; }
    public String getSoyad() { return soyad; }
    public String getTelefon() { return telefon; }
    public List<Randevu> getRandevular() { return randevular; }

    public void setId(Long id) { this.id = id; }
    public void setAd(String ad) { this.ad = ad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public void setRandevular(List<Randevu> randevular) { this.randevular = randevular; }
}
