package com.hastane.hastane_yonetim.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MuayeneResponse {
    private Long id;
    private LocalDateTime muayeneTarihi;

    private String sikayet;
    private String teshis;
    private String tedavi;
    private String notlar;

    private Long randevuId;
}

