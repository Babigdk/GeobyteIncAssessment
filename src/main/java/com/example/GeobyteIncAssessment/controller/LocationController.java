package com.example.GeobyteIncAssessment.controller;

import com.example.GeobyteIncAssessment.dto.LocationDto;
import com.example.GeobyteIncAssessment.models.Locations;
import com.example.GeobyteIncAssessment.response.BaseResponse;
import com.example.GeobyteIncAssessment.response.RouteResponse;
import com.example.GeobyteIncAssessment.services.serviceImpl.LocationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationServiceImpl locationService;


    @PostMapping("/addLocation")
    public BaseResponse addLocation(@RequestBody LocationDto locationDto){
        return locationService.addLocation(locationDto);
    }

    @DeleteMapping("/deleteLocation/{id}")
    public BaseResponse deleteLocation(@PathVariable String id){
        return locationService.removeLocation(id);
    }

    @PutMapping("/updateLocation/{id}")
    public BaseResponse updateLocation(@RequestBody LocationDto locationDto, @PathVariable String id){
        return locationService.updateLocation(locationDto, id);
    }

    @GetMapping("/allLocations")
    public List<Locations> getAllLocations(){
        return locationService.listOfLocations();
    }

    @PostMapping("/optimal-route")
    public ResponseEntity<RouteResponse> generateOptimalRoute(
            @RequestParam String originId,
            @RequestParam String destinationId
    ) {
        RouteResponse response = locationService.generateOptimalRoute(originId, destinationId);

        if (response.getSetSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
