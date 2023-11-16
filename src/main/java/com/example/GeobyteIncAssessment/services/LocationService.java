package com.example.GeobyteIncAssessment.services;

import com.example.GeobyteIncAssessment.dto.LocationDto;
import com.example.GeobyteIncAssessment.models.Locations;
import com.example.GeobyteIncAssessment.response.BaseResponse;
import com.example.GeobyteIncAssessment.response.RouteResponse;

import java.util.List;

public interface LocationService {

    BaseResponse addLocation(LocationDto locationDto);
    BaseResponse removeLocation(String locationId);
    BaseResponse updateLocation(LocationDto locationDto, String locationId);

    RouteResponse generateOptimalRoute(String originId, String destinationId);
    List<Locations> calculateOptimalRoute(Locations origin, Locations destination);
    double calculateCostOfDelivery(List<Locations> route);
    double calculateTotalDistance(List<Locations> route);
    double calculateStraightLineDistance(Locations location1, Locations location2);


    List<Locations> listOfLocations();
}
