package com.gadel.myapplication.data.remote.dto;

import java.util.List;

public class JwtPayloadDTO {

        // Estos son los datos estándar del JWT
        public String token;
        public String sub;
        public String iss;
        public Long iat;
        public Long exp;

        // Aquí está tu lista de cuentas (como es un [ ] en JSON, es un List en Java)
        public List<AccountDTO> accounts;

        // --- CLASES ANIDADAS ---

        public static class AccountDTO {
            public Integer userId;
            public Integer companyId;
            public String companyName;
            public String companyCod;

            // Aquí está tu lista de roles anidada
            public List<RoleDTO> roles;
        }

        public static class RoleDTO {
            public Integer id;
            public String cod;
            public String name;
        }

}
