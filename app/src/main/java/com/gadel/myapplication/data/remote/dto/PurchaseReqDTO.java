package com.gadel.myapplication.data.remote.dto;

import java.util.List;

public class PurchaseReqDTO {
    public Long purreqhId;
    public String purreqhNo; // ¡Este es el correlativo real (Ej. MR310000024)!
    public String companyName;
    public String currencyCod;
    public String purreqhDesc;
    public String purreqhRequeDate;
    public Integer purreqhRequeById;
    public String purreqhRequeByName;

    // Agregamos esta variable esperando que Spring Boot la envíe pronto
    public String requesterName;

    public List<DetailDTO> detail;

    public static class DetailDTO {
        public Long purreqdId;
        public String materialNo;
        public String purreqdDesc;
        public Double purreqdQtyReq;
        public Double purreqdUnitPrice;
    }
}