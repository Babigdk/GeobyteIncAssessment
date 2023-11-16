package com.example.GeobyteIncAssessment.services.serviceImpl;

import com.example.GeobyteIncAssessment.dto.StaffDto;
import com.example.GeobyteIncAssessment.models.Staff;
import com.example.GeobyteIncAssessment.repository.StaffRepository;
import com.example.GeobyteIncAssessment.response.BaseResponse;
import com.example.GeobyteIncAssessment.services.StaffServices;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StaffServiceImpl implements StaffServices {

    private final StaffRepository staffRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public BaseResponse register(StaffDto staffDto) {

      if(staffRepository.existsStaffByEmail(staffDto.getEmail())){
          return BaseResponse.builder()
                  .responseMessage("User with email already exist")
                  .responseCode(500)
                  .info(null)
                  .build();
      }

        Staff staff = Staff.builder()
                .fullName(staffDto.getFullName())
                .email(staffDto.getEmail())
                .password(passwordEncoder.encode(staffDto.getPassword()))
                .build();

      staffRepository.save(staff);

        return BaseResponse.builder()
                .responseMessage("Staff added successfully")
                .responseCode(200)
                .info(null)
                .build();
    }



    @Override
    public BaseResponse login(StaffDto staffDto) {
        Staff staff = Staff.builder()
                .build();

        if(staff.getEmail().equals(staffDto.getEmail()) && staff.getPassword().equals(staffDto.getPassword())){
            return BaseResponse.builder()
                    .responseMessage("Login Successfully")
                    .responseCode(200)
                    .info(null)
                    .build();
        }
        return BaseResponse.builder()
                .responseMessage("Invalid credentials try again!!!")
                .responseCode(400)
                .info(null)
                .build();
    }
}
