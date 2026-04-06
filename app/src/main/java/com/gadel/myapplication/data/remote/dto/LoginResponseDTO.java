package com.gadel.myapplication.data.remote.dto;

import java.util.List;

import lombok.Data;

@Data
public class LoginResponseDTO {
    Long userId;
    Long companyId;
    String compCode;
    String compName;
    List<RoleResponseDTO> role;




}
