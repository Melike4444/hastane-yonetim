package com.hastane.hastane_yonetim.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoktorRequest {

    @NotBlank
    private String ad;

    @NotBlank
    private String soyad;

    @NotBlank
    private String brans;

    // Doktorun bağlı olduğu bölüm
    private Long departmentId;
}

