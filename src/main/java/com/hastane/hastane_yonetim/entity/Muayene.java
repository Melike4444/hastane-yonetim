package com.hastane.hastane_yonetim.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "muayeneler")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Muayene {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime muayeneTarihi;

    @Column(length = 500)
    private String sikayet;

    @Column(length = 500)
    private String teshis;

    @Column(length = 500)
    private String tedavi;

    @Column(length = 1000)
    private String notlar;

    // 1 randevuya 1 muayene (unique)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "randevu_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Randevu randevu;
}

