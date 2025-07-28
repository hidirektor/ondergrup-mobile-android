package me.t3sl4.ondergrup.Util.HTTP.Requests.User;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.HTTP.HttpHelper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {

    private static final String GET_PROFILE_URL = "/api/v2/user/getProfile";
    private static final String GET_PREFERENCES_URL = "/api/v2/user/getPreferences";
    private static final String UPDATE_PREFERENCES_URL = "/api/v2/user/updatePreferences";
    private static final String UPDATE_PROFILE_URL = "/api/v2/user/updateProfile";
    private static final String UPLOAD_PROFILE_PHOTO_URL = "/api/v2/user/uploadProfilePhoto";
    private static final String CHECK_USER_URL = "/api/v2/user/checkUser";

    public static void getProfile(Context context, String userID, Runnable onSuccess, Runnable onFailure) {
        // Gerçek API isteği yorum satırına alındı, dummy response ile dolduruluyor
        /*
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userID", userID);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequest("POST", GET_PROFILE_URL, null, jsonObject.toString(), UserDataService.getAccessToken(context));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("GetProfile", "Success: " + responseBody);

                        JSONObject responseJson = new JSONObject(responseBody);
                        JSONObject payload = responseJson.getJSONObject("payload");
                        JSONObject user = payload.getJSONObject("user");
                        JSONObject userPreferences = payload.getJSONObject("userPreferences");


                        UserDataService.setUserRole(context, user.getString("userType"));
                        UserDataService.setUserName(context, user.getString("userName"));
                        UserDataService.seteMail(context, user.getString("eMail"));
                        UserDataService.setNameSurname(context, user.getString("nameSurname"));
                        UserDataService.setPhoneNumber(context, user.getString("phoneNumber"));
                        UserDataService.setCompanyName(context, user.getString("companyName"));
                        UserDataService.setCreatedAt(context, Util.convertUnixTimestampToDateString(Long.parseLong(String.valueOf(user.getInt("createdAt")))));
                        UserDataService.setSelectedLanguage(context, userPreferences.getString("language"));
                        UserDataService.setSelectedNightMode(context, userPreferences.getString("nightMode"));

                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("GetProfile", "Failure: " + response.errorBody().string());

                        if (onFailure != null) {
                            onFailure.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("GetProfile", "Error: " + t.getMessage());
            }
        });
        */

        // Dummy response
        UserDataService.setUserRole(context, "NORMAL");
        UserDataService.setUserName(context, "recep.baskurt@ondergrup.com");
        UserDataService.seteMail(context, "recep.baskurt@ondergrup.com");
        UserDataService.setNameSurname(context, "Recep Baskurt");
        UserDataService.setPhoneNumber(context, "5555555555");
        UserDataService.setCompanyName(context, "Önder Grup");
        UserDataService.setCreatedAt(context, "01.01.2024");
        UserDataService.setSelectedLanguage(context, "tr");
        UserDataService.setSelectedNightMode(context, "light");
        if (onSuccess != null) {
            onSuccess.run();
        }
    }

    public static void getPreferences(Context context, String userID) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userID", userID);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequest("POST", GET_PREFERENCES_URL, null, jsonObject.toString(), UserDataService.getAccessToken(context));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("GetPreferences", "Success: " + responseBody);

                        JSONObject responseJson = new JSONObject(responseBody);
                        JSONObject payload = responseJson.getJSONObject("payload");
                        JSONObject userPreferences = payload.getJSONObject("userPreferences");

                        // Update user preferences
                        // Process user preferences here
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("GetPreferences", "Failure: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("GetPreferences", "Error: " + t.getMessage());
            }
        });
    }

    public static void updatePreferences(Context context, String userID, String language, String nightMode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("operationPlatform", "Android");
            jsonObject.put("sourceUserID", userID);
            jsonObject.put("affectedUserID", userID);
            jsonObject.put("affectedUserName", null);
            jsonObject.put("affectedMachineID", null);
            jsonObject.put("affectedMaintenanceID", null);
            jsonObject.put("affectedHydraulicUnitID", null);
            jsonObject.put("userID", userID);
            JSONObject preferencesData = new JSONObject();
            preferencesData.put("language", language);
            preferencesData.put("nightMode", nightMode);
            jsonObject.put("preferencesData", preferencesData);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequest("POST", UPDATE_PREFERENCES_URL, null, jsonObject.toString(), UserDataService.getAccessToken(context));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("UpdatePreferences", "Success: " + responseBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("UpdatePreferences", "Failure: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("UpdatePreferences", "Error: " + t.getMessage());
            }
        });
    }

    public static void updateProfile(Context context, String userID, String nameSurname, String eMail, String companyName, String phoneNumber, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("operationPlatform", "Android");
            jsonObject.put("sourceUserID", userID);
            jsonObject.put("affectedUserID", userID);
            jsonObject.put("affectedUserName", null);
            jsonObject.put("affectedMachineID", null);
            jsonObject.put("affectedMaintenanceID", null);
            jsonObject.put("affectedHydraulicUnitID", null);
            jsonObject.put("userID", userID);
            JSONObject userData = new JSONObject();
            userData.put("nameSurname", nameSurname);
            userData.put("eMail", eMail);
            userData.put("phoneNumber", phoneNumber);
            userData.put("companyName", companyName);
            jsonObject.put("userData", userData);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequest("POST", UPDATE_PROFILE_URL, null, jsonObject.toString(), UserDataService.getAccessToken(context));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("UpdateProfile", "Success: " + responseBody);

                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("UpdateProfile", "Failure: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("UpdateProfile", "Error: " + t.getMessage());
            }
        });
    }

    public static void uploadProfilePhoto(Context context, String userName, File photoFile) {
        String accessToken = UserDataService.getAccessToken(context);

        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("accessToken", HttpHelper.createPartFromString(accessToken));
        partMap.put("userName", HttpHelper.createPartFromString(userName));

        MultipartBody.Part filePart = HttpHelper.prepareFilePart("file", photoFile);

        Call<ResponseBody> call = HttpHelper.uploadFilesWithAuth(UPLOAD_PROFILE_PHOTO_URL, partMap, List.of(filePart), UserDataService.getAccessToken(context));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("UploadProfilePhoto", "Success: " + responseBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("UploadProfilePhoto", "Failure: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("UploadProfilePhoto", "Error: " + t.getMessage());
            }
        });
    }

    public static void checkUser(Context context, String userName, Runnable onSuccess, Runnable onFailure) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequest("POST", "/api/v2/user/checkUser", null, jsonObject.toString(), UserDataService.getAccessToken(context));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("CheckUser", "Success: " + responseBody);

                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("CheckUser", "Failure: " + response.errorBody().string());

                        if (onFailure != null) {
                            onFailure.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("CheckUser", "Error: " + t.getMessage());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        });
    }
}