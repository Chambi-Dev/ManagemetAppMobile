package com.gadel.myapplication.data.remote.dto;

import lombok.Data;

@Data
public class AuthDTOs {

    // 1. Lo que envías primero (Usuario y Clave)
    public static class LoginRequest {
        public String username; // o username, según tu backend
        public String password;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    // 2. Lo que responde el primer paso
    public static class PreAuthResponse {
        public String preAuthToken;
    }

    // 3. Lo que envías después de seleccionar el rol
    public static class RoleSelectionRequest {
        public Integer userId;
        public Integer roleId;
        public String preAuthToken;

        public RoleSelectionRequest(Integer userId, Integer roleId, String preAuthToken) {
            this.userId = userId;
            this.roleId = roleId;
            this.preAuthToken = preAuthToken;
        }
    }

    // 4. Lo que responde el segundo paso (Tu pase de entrada final)
    public static class FinalAuthResponse {
        public String token;
    }

}
