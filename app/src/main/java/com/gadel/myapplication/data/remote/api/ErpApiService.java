package com.gadel.myapplication.data.remote.api;

import com.gadel.myapplication.data.remote.dto.AuthDTOs;
import com.gadel.myapplication.data.remote.dto.PurchaseReqDTO;
import com.gadel.myapplication.data.remote.dto.TaskDTO;
import com.gadel.myapplication.data.remote.dto.UpdateStatusRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface ErpApiService {


    //paso 1
    @POST("/api/auth/login")
    Call<AuthDTOs.PreAuthResponse> login(@Body AuthDTOs.LoginRequest request);

    //paso 2
    @POST("/api/auth/generateFinalToken")
    Call<AuthDTOs.FinalAuthResponse> selectRole(@Body AuthDTOs.RoleSelectionRequest request);

    // NUEVO MÉTODO: Traer las tareas pendientes
    // Reemplaza "/api/approvals/pending" con la ruta real de tu Spring Boot
    @GET("/api/approvals/pending")
    Call<List<TaskDTO>> getPendingTasks(@Header("Authorization") String token);

    @GET("/api/purchaseRequisitionHeader/getAllForApproval") // <--- ¡OJO! Pon tu ruta real aquí
    Call<List<PurchaseReqDTO>> getPendingPurchaseReqs(@Header("Authorization") String token);

    @PATCH("/api/purchaseRequisitionHeader/updateHeaderStatus")
    Call<Object> updateTaskStatus(
            @Header("Authorization") String token,
            @Body UpdateStatusRequest request
    );

}
