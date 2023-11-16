package com.example.GeobyteIncAssessment.services.serviceImpl;

import com.example.GeobyteIncAssessment.dto.LocationDto;
import com.example.GeobyteIncAssessment.models.Locations;
import com.example.GeobyteIncAssessment.repository.LocationRepository;
import com.example.GeobyteIncAssessment.response.BaseResponse;
import com.example.GeobyteIncAssessment.response.RouteResponse;
import com.example.GeobyteIncAssessment.services.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationService {


    private final LocationRepository locationRepository;

    private List<Locations> neighbors;
    private double distance;
    @Override
    public BaseResponse addLocation(LocationDto locationDto) {
        BaseResponse response = new BaseResponse();
        try {
            if (locationRepository.existsByLocationName(locationDto.getLocationName())) {
                response.setResponseMessage("Location with the same attributes already exists.");
                response.setResponseCode(400);
                response.setInfo(null);
            } else {
                // Create a new Location entity and save it to the database
                Locations newLocation = Locations.builder()
                                .locationName(locationDto.getLocationName())
                                 .locationTitle(locationDto.getLocationTitle())
                                        .latitude(locationDto.getLatitude())
                                                .longitude(locationDto.getLongitude())
                                                        .clearingCost(locationDto.getClearingCost())
                                                                .build();

                locationRepository.save(newLocation);

                response.setResponseMessage("Location added successfully.");
                response.setResponseCode(200);
            }
        } catch (Exception e) {
            response.setResponseMessage("Error adding location: " + e.getMessage());
            response.setResponseCode(500);
        }
        return response;
    }

    @Override
    public BaseResponse removeLocation(String locationId) {
        BaseResponse response = new BaseResponse();
        try {
            if (locationRepository.existsLocationsById(locationId)) {
                locationRepository.deleteById(locationId);
                response.setResponseMessage("Location removed successfully.");
                response.setResponseCode(200);
            } else {
                response.setResponseMessage("Location not found.");
                response.setResponseCode(400);
            }
        } catch (Exception e) {
            response.setResponseMessage("Error removing location: " + e.getMessage());
            response.setResponseCode(500);
        }
        return response;
    }

    @Override
    public BaseResponse updateLocation(LocationDto locationDto, String locationId) {
        BaseResponse response = new BaseResponse();
        try {
            if (locationRepository.existsLocationsById(locationId)) {
                Locations existingLocation = locationRepository.findLocationsById(locationId);
                existingLocation.setLocationName(locationDto.getLocationName());
                existingLocation.setLatitude(locationDto.getLatitude());
                existingLocation.setLongitude(locationDto.getLongitude());
                existingLocation.setClearingCost(locationDto.getClearingCost());

                locationRepository.save(existingLocation);

                response.setResponseMessage("Location updated successfully.");
                response.setResponseCode(200);
            } else {
                response.setResponseMessage("Location not found.");
                response.setResponseCode(400);
            }
        } catch (Exception e) {
            response.setResponseMessage("Error updating location: " + e.getMessage());
            response.setResponseCode(500);
        }
        return response;
    }

    @Override
    public RouteResponse generateOptimalRoute(String originId, String destinationId) {
        RouteResponse response = new RouteResponse();
        try {
            Locations origin = locationRepository.findLocationsById(originId);
            Locations destination = locationRepository.findLocationsById(destinationId);

            if (origin == null || destination == null) {
                response.setMessage("Invalid origin or destination location.");
                response.setSetSuccess(false);
                return response;
            }

            List<Locations> optimalRoute = calculateOptimalRoute(origin, destination);
            log.info("optimalRoute {}",optimalRoute);
            double costOfDelivery = calculateCostOfDelivery(optimalRoute);

            response.setOptimalRoute(optimalRoute);
            response.setCostOfDelivery(costOfDelivery);
            response.setMessage("Optimal route generated successfully.");
            response.setSetSuccess(true);
        } catch (Exception e) {
            response.setMessage("Error generating optimal route: " + e.getMessage());
            response.setSetSuccess(false);
        }
        return response;
    }


    @Override
    public List<Locations> calculateOptimalRoute(Locations origin, Locations destination) {
        Map<Locations, Double> distanceMap = new HashMap<>();
        Map<Locations, Locations> predecessorMap = new HashMap<>();
        Set<Locations> visitedLocations = new HashSet<>();

        distanceMap.put(origin, 0.0);

        PriorityQueue<Locations> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(distanceMap::get));
        priorityQueue.add(origin);

        while (!priorityQueue.isEmpty()) {
            Locations currentLocation = priorityQueue.poll();
            visitedLocations.add(currentLocation);

            for (Locations neighbor : getNeighbors()) {
                if (!visitedLocations.contains(neighbor)) {
                    double newDistance = distanceMap.get(currentLocation) + getDistanceTo(neighbor);
                    if (newDistance < distanceMap.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                        distanceMap.put(neighbor, newDistance);
                        predecessorMap.put(neighbor, currentLocation);
                        priorityQueue.add(neighbor);
                    }
                }
            }
        }

        List<Locations> optimalRoute = new ArrayList<>();
        Locations currentLocation = destination;
        while (currentLocation != null) {
            optimalRoute.add(currentLocation);
            currentLocation = predecessorMap.get(currentLocation);
        }

        Collections.reverse(optimalRoute);

        return optimalRoute;
    }
    private List<Locations> getNeighbors() {
        return neighbors != null ? neighbors : Collections.emptyList();
    }

    private double getDistanceTo(Locations to) {
        return to != null ? distance : 0.0;
    }
    @Override
    public double calculateCostOfDelivery(List<Locations> route) {
        double costPerKilometer = 1.00;
        double clearingCost = route.stream()
                .skip(1)
                .mapToDouble(Locations::getClearingCost)
                .sum();

        double totalDistance = calculateTotalDistance(route);
        double costOfDelivery = costPerKilometer * totalDistance + clearingCost;

        return costOfDelivery;
    }

    @Override
    public double calculateTotalDistance(List<Locations> route) {
        double totalDistance = 0.0;
        for (int i = 0; i < route.size() - 1; i++) {
            Locations currentLocation = route.get(i);
            Locations nextLocation = route.get(i + 1);
            double distance = calculateStraightLineDistance(currentLocation, nextLocation);
            totalDistance += distance;
        }
        return totalDistance;
    }

    @Override
    public double calculateStraightLineDistance(Locations location1, Locations location2) {
        double lat1 = location1.getLatitude();
        double lon1 = location1.getLongitude();
        double lat2 = location2.getLatitude();
        double lon2 = location2.getLongitude();

        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    @Override
    public List<Locations> listOfLocations() {
        return locationRepository.findAll();
    }
}
