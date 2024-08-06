package me.t3sl4.ondergrup.Util.HTTP.Requests.Auth;

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

public class AuthService {
    private static final String REGISTER_URL = "/api/v2/auth/register";
    private static final String LOGIN_URL = "/api/v2/auth/login";
    private static final String LOGOUT_URL = "/api/v2/auth/logout";
    private static final String RESET_PASS_URL = "/api/v2/auth/resetPass";
    private static final String CHANGE_PASS_URL = "/api/v2/auth/changePass";

    public static void register(Context context, String userName, String userType, String nameSurname, String eMail, String phoneNumber, String companyName, String password, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
            jsonObject.put("userType", userType);
            jsonObject.put("nameSurname", nameSurname);
            jsonObject.put("eMail", eMail);
            jsonObject.put("phoneNumber", phoneNumber);
            jsonObject.put("companyName", companyName);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequest("POST", REGISTER_URL, null, jsonObject.toString(), null);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("Register", "Success: " + response.body().string());
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("Register", "Failure: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Register", "Error: " + t.getMessage());
            }
        });
    }

    public static void login(Context context, String userName, String password, Runnable onSuccess, Runnable onFailure) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequest("POST", LOGIN_URL, null, jsonObject.toString(), null);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("Login", "Success: " + responseBody);

                        JSONObject responseJson = new JSONObject(responseBody);
                        JSONObject payload = responseJson.getJSONObject("payload");

                        String userID = payload.getString("userID");
                        String userType = payload.getString("userType");
                        String accessToken = payload.getString("accessToken");
                        String refreshToken = payload.getString("refreshToken");

                        UserDataService.setUserID(context, userID);
                        UserDataService.setUserRole(context, userType);
                        UserDataService.setAccessToken(context, accessToken);
                        UserDataService.setRefreshToken(context, refreshToken);

                        // Check if onSuccess callback is not null and run it
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("Login", "Failure: " + response.errorBody().string());

                        if(onFailure != null) {
                            onFailure.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Login", "Error: " + t.getMessage());
            }
        });
    }

    public static void logout(Context context) {
        String accessToken = UserDataService.getAccessToken(context);

        if (accessToken == null) {
            Log.e("Logout", "Error: No access token found.");
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", accessToken);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequest("POST", LOGOUT_URL, null, jsonObject.toString(), accessToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("Logout", "Success: " + response.body().string());
                        UserDataService.clearUser(context);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("Logout", "Failure: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Logout", "Error: " + t.getMessage());
            }
        });
    }

    public static void resetPass(String userName, String newPassword, String otpSentTime, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
            jsonObject.put("newPassword", newPassword);
            jsonObject.put("otpSentTime", otpSentTime);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequest("POST", RESET_PASS_URL, null, jsonObject.toString(), null);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("ResetPass", "Success: " + response.body().string());
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("ResetPass", "Failure: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ResetPass", "Error: " + t.getMessage());
            }
        });
    }

    public static void changePass(Context context, String userName, String oldPassword, String newPassword, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
            jsonObject.put("oldPassword", oldPassword);
            jsonObject.put("newPassword", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        String accessToken = UserDataService.getAccessToken(context);
        Call<ResponseBody> call = HttpHelper.makeRequest("POST", CHANGE_PASS_URL, null, jsonObject.toString(), accessToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("ChangePass", "Success: " + response.body().string());
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("ChangePass", "Failure: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ChangePass", "Error: " + t.getMessage());
            }
        });
    }
}