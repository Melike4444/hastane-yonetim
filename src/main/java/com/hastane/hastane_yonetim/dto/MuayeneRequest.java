package com.hastane.hastane_yonetim.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MuayeneRequest {
    private Long randevuId;

    // İstersen göndermeden de olur, service "now" yapacak
    private LocalDateTime muayeneTarihi;

    private String sikayet;
    private String teshis;
    private String tedavi;
    private String notlar;
}

