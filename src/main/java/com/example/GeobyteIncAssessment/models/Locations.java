package com.example.GeobyteIncAssessment.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Locations extends BaseClass {

    @Column(nullable = false)
    private String locationName;

    @Column(nullable = false)
    private String locationTitle;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private double clearingCost;
}
