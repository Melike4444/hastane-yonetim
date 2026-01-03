package com.hastane.hastane_yonetim.dto;

import java.time.LocalDateTime;

public class RandevuCreateRequest {
    private LocalDateTime tarihSaat;
    private Long hastaId;
    private Long doktorId;

    public RandevuCreateRequest() {}

    public LocalDateTime getTarihSaat() { return tarihSaat; }
    public Long getHastaId() { return hastaId; }
    public Long getDoktorId() { return doktorId; }

    public void setTarihSaat(LocalDateTime tarihSaat) { this.tarihSaat = tarihSaat; }
    public void setHastaId(Long hastaId) { this.hastaId = hastaId; }
    public void setDoktorId(Long doktorId) { this.doktorId = doktorId; }
}
