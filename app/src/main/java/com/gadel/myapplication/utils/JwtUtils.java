package com.gadel.myapplication.utils;

import android.util.Base64;

import com.gadel.myapplication.data.remote.dto.JwtPayloadDTO;
import com.google.gson.Gson;

public class JwtUtils {

        // Esta función hace exactamente lo que hace jwt.io
        public static JwtPayloadDTO decodeToken(String jwtToken) {
            try {
                // Un JWT siempre tiene 3 partes separadas por puntos (Header.Payload.Signature)
                String[] split = jwtToken.split("\\.");
                if (split.length < 2) return null;

                // La parte 1 (índice 1) es el Payload (los datos)
                String base64EncodedBody = split[1];

                // Decodificamos de Base64 a texto plano (El JSON crudo)
                byte[] decodedBytes = Base64.decode(base64EncodedBody, Base64.URL_SAFE);
                String jsonBody = new String(decodedBytes, "UTF-8");

                // Convertimos ese JSON en nuestra clase de Java usando Gson
                Gson gson = new Gson();
                return gson.fromJson(jsonBody, JwtPayloadDTO.class);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

}
