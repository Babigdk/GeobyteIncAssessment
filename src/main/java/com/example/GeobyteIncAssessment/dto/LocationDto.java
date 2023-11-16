package com.example.GeobyteIncAssessment.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    private String locationName;
    private String locationTitle;
    private double latitude;

    private double longitude;

    private double clearingCost;
}
