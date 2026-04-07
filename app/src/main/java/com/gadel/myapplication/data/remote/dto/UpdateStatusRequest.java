package com.gadel.myapplication.data.remote.dto;

public class UpdateStatusRequest {
    public Long purreqhId;
    public String statusCod;
    public String reject;

    // Constructor para armar el paquete rápido
    public UpdateStatusRequest(Long purreqhId, String statusCod, String reject) {
        this.purreqhId = purreqhId;
        this.statusCod = statusCod;
        this.reject = reject;
    }
}