package com.hastane.hastane_yonetim.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doktorlar")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doktor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ad;

    private String soyad;

    private String brans;

    // ✅ Department ilişkisi
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    // 🔁 Randevular (zaten vardı büyük ihtimalle)
    @JsonIgnore
    @OneToMany(mappedBy = "doktor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Randevu> randevular = new ArrayList<>();
}

