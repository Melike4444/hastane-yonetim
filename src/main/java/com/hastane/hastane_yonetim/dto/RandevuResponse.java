package com.hastane.hastane_yonetim.dto;

import java.time.LocalDateTime;

public class RandevuResponse {
    private Long id;
    private LocalDateTime tarihSaat;
    private Long hastaId;
    private String hastaAd;
    private String hastaSoyad;
    private Long doktorId;
    private String doktorAd;
    private String doktorSoyad;
    private String brans;

    public RandevuResponse() {}

    public RandevuResponse(Long id, LocalDateTime tarihSaat,
                           Long hastaId, String hastaAd, String hastaSoyad,
                           Long doktorId, String doktorAd, String doktorSoyad, String brans) {
        this.id = id;
        this.tarihSaat = tarihSaat;
        this.hastaId = hastaId;
        this.hastaAd = hastaAd;
        this.hastaSoyad = hastaSoyad;
        this.doktorId = doktorId;
        this.doktorAd = doktorAd;
        this.doktorSoyad = doktorSoyad;
        this.brans = brans;
    }

    public Long getId() { return id; }
    public LocalDateTime getTarihSaat() { return tarihSaat; }
    public Long getHastaId() { return hastaId; }
    public String getHastaAd() { return hastaAd; }
    public String getHastaSoyad() { return hastaSoyad; }
    public Long getDoktorId() { return doktorId; }
    public String getDoktorAd() { return doktorAd; }
    public String getDoktorSoyad() { return doktorSoyad; }
    public String getBrans() { return brans; }

    public void setId(Long id) { this.id = id; }
    public void setTarihSaat(LocalDateTime tarihSaat) { this.tarihSaat = tarihSaat; }
    public void setHastaId(Long hastaId) { this.hastaId = hastaId; }
    public void setHastaAd(String hastaAd) { this.hastaAd = hastaAd; }
    public void setHastaSoyad(String hastaSoyad) { this.hastaSoyad = hastaSoyad; }
    public void setDoktorId(Long doktorId) { this.doktorId = doktorId; }
    public void setDoktorAd(String doktorAd) { this.doktorAd = doktorAd; }
    public void setDoktorSoyad(String doktorSoyad) { this.doktorSoyad = doktorSoyad; }
    public void setBrans(String brans) { this.brans = brans; }
}
