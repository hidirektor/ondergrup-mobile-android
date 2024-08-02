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
import me.t3sl4.ondergrup.Util.SharedPrefUtil;
import me.t3sl4.ondergrup.Util.Util;
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

    public static void getProfile(Context context, String userID) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userID", userID);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequest("POST", GET_PROFILE_URL, null, jsonObject.toString(), SharedPrefUtil.getAccessToken(context));
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


                        UserDataService.setUserRole(context, user.getString("userType"));
                        UserDataService.setUserName(context, user.getString("userName"));
                        UserDataService.seteMail(context, user.getString("eMail"));
                        UserDataService.setNameSurname(context, user.getString("nameSurname"));
                        UserDataService.setPhoneNumber(context, user.getString("phoneNumber"));
                        UserDataService.setCompanyName(context, user.getString("companyName"));
                        UserDataService.setCreatedAt(context, Util.convertUnixTimestampToDateString(Long.parseLong(String.valueOf(user.getInt("createdAt")))));

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("GetProfile", "Failure: " + response.errorBody().string());
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
    }

    public static void getPreferences(Context context, String userID) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userID", userID);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequest("POST", GET_PREFERENCES_URL, null, jsonObject.toString(), SharedPrefUtil.getAccessToken(context));
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

    public static void updatePreferences(Context context, String userID, boolean language, boolean nightMode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userID", userID);
            JSONObject preferencesData = new JSONObject();
            preferencesData.put("language", language);
            preferencesData.put("nightMode", nightMode);
            jsonObject.put("preferencesData", preferencesData);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequest("POST", UPDATE_PREFERENCES_URL, null, jsonObject.toString(), SharedPrefUtil.getAccessToken(context));
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

    public static void updateProfile(Context context, String userID, String nameSurname, String eMail, String companyName, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userID", userID);
            JSONObject userData = new JSONObject();
            userData.put("nameSurname", nameSurname);
            userData.put("eMail", eMail);
            userData.put("companyName", companyName);
            userData.put("password", password);
            jsonObject.put("userData", userData);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequest("POST", UPDATE_PROFILE_URL, null, jsonObject.toString(), SharedPrefUtil.getAccessToken(context));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("UpdateProfile", "Success: " + responseBody);
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
        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("userName", HttpHelper.createPartFromString(userName));

        MultipartBody.Part filePart = HttpHelper.prepareFilePart("file", photoFile);

        Call<ResponseBody> call = HttpHelper.uploadFilesWithAuth(UPLOAD_PROFILE_PHOTO_URL, partMap, List.of(filePart), SharedPrefUtil.getAccessToken(context));
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
}