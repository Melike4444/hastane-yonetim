package com.hastane.hastane_yonetim.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentRequest {

    @NotBlank
    @Size(max = 100)
    private String ad;

    @Size(max = 255)
    private String aciklama;
}
