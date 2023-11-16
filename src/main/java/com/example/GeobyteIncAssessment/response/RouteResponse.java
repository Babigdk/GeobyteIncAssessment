package com.example.GeobyteIncAssessment.response;

import com.example.GeobyteIncAssessment.models.Locations;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteResponse {
    private List<Locations> optimalRoute;
    private double costOfDelivery;
    private String message;
    private String responseCode;
    private Boolean setSuccess;

}
