package com.example.GeobyteIncAssessment.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse {
    private String responseMessage;
    private int responseCode;
    private String info;
}
