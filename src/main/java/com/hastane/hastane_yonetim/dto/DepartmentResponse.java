package com.hastane.hastane_yonetim.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponse {
    private Long id;
    private String ad;
    private String aciklama;
}
