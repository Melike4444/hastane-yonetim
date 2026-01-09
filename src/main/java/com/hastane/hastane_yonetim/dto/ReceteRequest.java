package com.hastane.hastane_yonetim.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReceteRequest {

    @NotNull
    private Long doktorId;

    @NotNull
    private Long hastaId;

    @NotBlank
    private String ilaclar;

    private String notlar;

    // opsiyonel: null gelirse service now() atar
    private LocalDateTime tarihSaat;
}
