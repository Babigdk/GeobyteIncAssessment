package com.example.GeobyteIncAssessment.controller;

import com.example.GeobyteIncAssessment.dto.StaffDto;
import com.example.GeobyteIncAssessment.response.BaseResponse;
import com.example.GeobyteIncAssessment.services.serviceImpl.StaffServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class StaffController {

    private final StaffServiceImpl staffService;


    @PostMapping("/registerStaff")
    public BaseResponse register(@RequestBody  StaffDto staffDto){
        return staffService.register(staffDto);
    }

    @PostMapping("/loginStaff")
    public BaseResponse login(@RequestBody  StaffDto staffDto){
        return staffService.login(staffDto);
    }
}
