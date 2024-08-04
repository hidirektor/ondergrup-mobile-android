package me.t3sl4.ondergrup.Util.HTTP.Requests.SubUser;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import me.t3sl4.ondergrup.Model.SubUser.SubUser;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.HTTP.HttpHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubUserService {

    private static final String CREATE_SUB_USER_URL = "/api/v2/subuser/createSubUser";
    private static final String GET_SUB_USERS_URL = "/api/v2/subuser/getSubUsers";
    private static final String DELETE_SUB_USER_URL = "/api/v2/subuser/deleteSubUser";
    private static final String EDIT_SUB_USER_URL = "/api/v2/subuser/editSubUser";

    public static void createSubUser(Context context, String ownerID, String userName, String userType, String nameSurname, String eMail, String phoneNumber, String companyName, String password, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ownerID", ownerID);
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

        String accessToken = UserDataService.getAccessToken(context);
        Call<ResponseBody> call = HttpHelper.makeRequestWithAuth("POST", CREATE_SUB_USER_URL, null, jsonObject.toString(), accessToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("CreateSubUser", "Success: " + response.body().string());
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("CreateSubUser", "Failure: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("CreateSubUser", "Error: " + t.getMessage());
            }
        });
    }

    public static void getSubUsers(Context context, String ownerID, ArrayList<SubUser> subUsers, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ownerID", ownerID);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        String accessToken = UserDataService.getAccessToken(context);
        Call<ResponseBody> call = HttpHelper.makeRequestWithAuth("POST", GET_SUB_USERS_URL, null, jsonObject.toString(), accessToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("GetSubUsers", "Success: " + response.body().string());

                        // JSON verisini Machines listesine parse et
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray subUsersArray = jsonResponse.getJSONObject("payload").getJSONArray("users");

                        for (int i = 0; i < subUsersArray.length(); i++) {
                            JSONObject subUserObject = subUsersArray.getJSONObject(i);

                            SubUser subUser = new SubUser(subUserObject.getString("userID"),
                                    subUserObject.getString("userType"),
                                    subUserObject.getString("userName"),
                                    subUserObject.getString("eMail"),
                                    subUserObject.getString("nameSurname"),
                                    subUserObject.getString("phoneNumber"),
                                    subUserObject.getString("companyName"),
                                    ownerID);

                            subUsers.add(subUser);
                        }

                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        Log.e("GetSubUsers", "Failure: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("GetSubUsers", "Error: " + t.getMessage());
            }
        });
    }

    public static void deleteSubUser(Context context, String subUserID, Runnable onSuccess) {

        Log.d("Sub ID", subUserID);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("subUserID", subUserID);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        String accessToken = UserDataService.getAccessToken(context);
        Call<ResponseBody> call = HttpHelper.makeRequestWithAuth("POST", DELETE_SUB_USER_URL, null, jsonObject.toString(), accessToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("DeleteSubUser", "Success: " + response.body().string());
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("DeleteSubUser", "Failure: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("DeleteSubUser", "Error: " + t.getMessage());
            }
        });
    }

    public static void editSubUser(Context context, String ownerID, String userID, String userName, String userType, String nameSurname, String eMail, String phoneNumber, String companyName, String password, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ownerID", ownerID);
            jsonObject.put("userID", userID);
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

        String accessToken = UserDataService.getAccessToken(context);
        Call<ResponseBody> call = HttpHelper.makeRequestWithAuth("POST", EDIT_SUB_USER_URL, null, jsonObject.toString(), accessToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("EditSubUser", "Success: " + response.body().string());
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("EditSubUser", "Failure: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("EditSubUser", "Error: " + t.getMessage());
            }
        });
    }
}