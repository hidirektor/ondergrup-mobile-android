package me.t3sl4.ondergrup.Util.HTTP.Requests.Authorized;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.HTTP.HttpHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OPMaintenanceService {

    private static final String CREATE_MAINTENANCE_URL = "/api/v2/authorized/createMaintenance";
    private static final String DELETE_MAINTENANCE_URL = "/api/v2/authorized/deleteMaintenance";
    private static final String EDIT_MAINTENANCE_URL = "/api/v2/authorized/editMaintenance";

    public static void createMaintenance(Context context, String jsonBody, Runnable onSuccess) {

        String authToken = UserDataService.getAccessToken(context);

        Call<ResponseBody> call = HttpHelper.makeRequestWithAuth("POST", CREATE_MAINTENANCE_URL, null, jsonBody, authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    onSuccess.run();
                } else {
                    try {
                        Log.e("CreateMaintenance", "Failed: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("CreateMaintenance", "Error: " + t.getMessage());
            }
        });
    }

    public static void deleteMaintenance(Context context, String userID, String maintenanceID, String authToken, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("operationPlatform", "Android");
            jsonObject.put("sourceUserID", userID);
            jsonObject.put("affectedUserID", null);
            jsonObject.put("affectedUserName", null);
            jsonObject.put("affectedMachineID", null);
            jsonObject.put("affectedMaintenanceID", maintenanceID);
            jsonObject.put("affectedHydraulicUnitID", null);
            jsonObject.put("maintenanceID", maintenanceID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonBody = jsonObject.toString();

        Call<ResponseBody> call = HttpHelper.makeRequestWithAuth("DELETE", DELETE_MAINTENANCE_URL, null, jsonBody, authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    onSuccess.run();
                } else {
                    try {
                        Log.e("DeleteMaintenance", "Failed: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("DeleteMaintenance", "Error: " + t.getMessage());
            }
        });
    }

    public static void editMaintenance(Context context, String userID, String maintenanceID, int machineID, int technicianID, String kontrol11, String authToken, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("operationPlatform", "Android");
            jsonObject.put("sourceUserID", userID);
            jsonObject.put("affectedUserID", null);
            jsonObject.put("affectedUserName", null);
            jsonObject.put("affectedMachineID", null);
            jsonObject.put("affectedMaintenanceID", maintenanceID);
            jsonObject.put("affectedHydraulicUnitID", null);
            jsonObject.put("maintenanceID", maintenanceID);
            jsonObject.put("machineID", machineID);
            jsonObject.put("technicianID", technicianID);
            jsonObject.put("kontrol11", kontrol11);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonBody = jsonObject.toString();

        Call<ResponseBody> call = HttpHelper.makeRequestWithAuth("PUT", EDIT_MAINTENANCE_URL, null, jsonBody, authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    onSuccess.run();
                } else {
                    try {
                        Log.e("EditMaintenance", "Failed: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("EditMaintenance", "Error: " + t.getMessage());
            }
        });
    }
}