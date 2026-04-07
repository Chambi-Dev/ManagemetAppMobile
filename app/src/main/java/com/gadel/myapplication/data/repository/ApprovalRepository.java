package com.gadel.myapplication.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gadel.myapplication.data.local.AppDatabase;
import com.gadel.myapplication.data.local.dao.ApprovalDetailDao;
import com.gadel.myapplication.data.local.dao.ApprovalTaskDao;
import com.gadel.myapplication.data.local.dao.SyncActionDao;
import com.gadel.myapplication.data.local.entity.ApprovalDetail;
import com.gadel.myapplication.data.local.entity.ApprovalTask;
import com.gadel.myapplication.data.local.entity.SyncAction;
import com.gadel.myapplication.data.local.model.CategoryCount;
import com.gadel.myapplication.data.remote.api.ErpApiService;
import com.gadel.myapplication.data.remote.api.RetrofitClient;
import com.gadel.myapplication.data.remote.dto.PurchaseReqDTO;
import com.gadel.myapplication.data.remote.dto.UpdateStatusRequest;
import com.gadel.myapplication.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApprovalRepository {

    private final ApprovalTaskDao approvalTaskDao;
    private final ApprovalDetailDao approvalDetailDao;
    private final SyncActionDao syncActionDao;
    private final ErpApiService apiService;
    private final SessionManager sessionManager;
    private final MutableLiveData<Boolean> tokenExpiredError = new MutableLiveData<>(false);

    public ApprovalRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        approvalTaskDao = db.approvalTaskDao();
        approvalDetailDao = db.approvalDetailDao();
        syncActionDao = db.syncActionDao();
        apiService = RetrofitClient.getInstance().getApi();
        sessionManager = new SessionManager(application);
    }

    public LiveData<Boolean> getTokenExpiredError() {
        return tokenExpiredError;
    }

    public LiveData<List<CategoryCount>> getCategoryCounts() {
        return approvalTaskDao.getCategoryCounts();
    }

    public LiveData<List<ApprovalTask>> getTasksByCategory(String categoryName) {
        return approvalTaskDao.getTasksByCategory(categoryName);
    }

    public LiveData<List<ApprovalDetail>> getDetailsByTaskId(String taskId) {
        return approvalDetailDao.getDetailsByTaskId(taskId);
    }

    public void syncPurchaseRequisitions() {
        String token = sessionManager.getAuthToken();
        if (token == null || token.isEmpty()) {
            tokenExpiredError.postValue(true);
            return;
        }

        String authHeader = "Bearer " + token;

        apiService.getPendingPurchaseReqs(authHeader).enqueue(new Callback<List<PurchaseReqDTO>>() {
            @Override
            public void onResponse(Call<List<PurchaseReqDTO>> call, Response<List<PurchaseReqDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ApprovalTask> tasksToSave = new ArrayList<>();
                    List<ApprovalDetail> detailsToSave = new ArrayList<>();

                    for (PurchaseReqDTO dto : response.body()) {
                        ApprovalTask task = new ApprovalTask();
                        task.taskId = dto.purreqhNo;
                        task.backendDocId = dto.purreqhId;
                        task.moduloType = "Requerimientos de Compra";
                        task.requesterName = dto.purreqhRequeByName != null ? dto.purreqhRequeByName : "Sin Especificar";

                        double totalCalculated = 0.0;
                        if (dto.detail != null) {
                            for (PurchaseReqDTO.DetailDTO detDTO : dto.detail) {
                                totalCalculated += (detDTO.purreqdQtyReq * detDTO.purreqdUnitPrice);

                                ApprovalDetail detailEntity = new ApprovalDetail();
                                detailEntity.taskId = task.taskId;
                                detailEntity.materialNo = detDTO.materialNo;
                                detailEntity.description = detDTO.purreqdDesc;
                                detailEntity.quantity = detDTO.purreqdQtyReq;
                                detailEntity.unitPrice = detDTO.purreqdUnitPrice;

                                detailsToSave.add(detailEntity);
                            }
                        }
                        task.totalAmount = totalCalculated;
                        tasksToSave.add(task);
                    }

                    new Thread(() -> {
                        approvalTaskDao.insertTasks(tasksToSave);
                        approvalDetailDao.insertDetails(detailsToSave);
                        tokenExpiredError.postValue(false);
                    }).start();
                } else {
                    if (response.code() == 401 || response.code() == 403) {
                        tokenExpiredError.postValue(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PurchaseReqDTO>> call, Throwable t) {
                Log.e("SYNC", "Sin internet al descargar.");
            }
        });
    }

    public void updateTaskStatusLocal(String taskId, String status, String comment) {
        new Thread(() -> {
            approvalTaskDao.updateTaskStatus(taskId, status);

            SyncAction action = new SyncAction();
            action.taskId = taskId;
            action.actionType = status;
            action.comments = comment;
            action.createdAt = System.currentTimeMillis();

            syncActionDao.insert(action);
        }).start();
    }

    // =========================================================
    // ¡EL MOTOR DE SUBIDA (CARTERO)! Lee la cola y envía a Spring
    // =========================================================
    public void syncOutboxToServer() {
        new Thread(() -> {
            String token = sessionManager.getAuthToken();
            if (token == null || token.isEmpty()) return;
            String authHeader = "Bearer " + token;

            // 1. Buscamos todas las acciones pendientes
            List<SyncAction> pendingActions = syncActionDao.getAllPendingSyncs();
            if (pendingActions.isEmpty()) return; // Si la cola está vacía, no hacemos nada

            for (SyncAction action : pendingActions) {
                // 2. Buscamos el ID real (ej. 18) en base al código (MR31...)
                Long docId = approvalTaskDao.getBackendIdByTaskId(action.taskId);
                if (docId == null) continue;

                // 3. Transformamos "APPROVED" al código que pide tu backend
                String statusCode = action.actionType.equals("APPROVED") ? "60" : "48";
                String rejectComment = action.comments != null ? action.comments : "";

                // 4. Armamos el paquete JSON
                UpdateStatusRequest request = new UpdateStatusRequest(docId, statusCode, rejectComment);

                try {
                    // 5. Enviamos la petición y ESPERAMOS la respuesta (.execute() en lugar de .enqueue())
                    Response<Object> response = apiService.updateTaskStatus(authHeader, request).execute();

                    if (response.isSuccessful()) {
                        // ¡MAGIA! Si Spring Boot dijo 200 OK, borramos la orden de la cola
                        syncActionDao.delete(action);
                        Log.d("OUTBOX", " Enviado a Spring Boot y borrado de la cola: " + action.taskId);
                    } else {
                        Log.e("OUTBOX", " Error al enviar " + action.taskId + ": " + response.code());
                    }
                } catch (Exception e) {
                    Log.e("OUTBOX", "📡 Sin internet, se intentará luego. Error: " + e.getMessage());
                    break; // Cortamos el ciclo for para no seguir intentando si no hay red
                }
            }
        }).start();
    }
}