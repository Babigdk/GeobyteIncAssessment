package com.example.GeobyteIncAssessment.services;

import com.example.GeobyteIncAssessment.dto.StaffDto;
import com.example.GeobyteIncAssessment.response.BaseResponse;

public interface StaffServices {

    BaseResponse register(StaffDto staffDto);

    BaseResponse login(StaffDto staffDto);
}
