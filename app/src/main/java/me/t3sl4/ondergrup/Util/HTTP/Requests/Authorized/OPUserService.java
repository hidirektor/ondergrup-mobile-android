package me.t3sl4.ondergrup.Util.HTTP.Requests.Authorized;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.HTTP.HttpHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OPUserService {

    private static final String GET_ALL_USERS_URL = "/api/v2/authorized/getAllUsers";
    private static final String UPDATE_ROLE_URL = "/api/v2/authorized/updateRole";
    private static final String DEACTIVATE_USER_URL = "/api/v2/authorized/deActivateUser";
    private static final String ACTIVATE_USER_URL = "/api/v2/authorized/activateUser";
    private static final String DELETE_USER_URL = "/api/v2/authorized/deleteUser";

    // getAllUsers method
    public static void getAllUsers(Context context, ArrayList<User> users, Runnable onSuccess, Runnable onFailure) {
        String authToken = UserDataService.getAccessToken(context);

        Call<ResponseBody> call = HttpHelper.makeRequestWithAuth("GET", GET_ALL_USERS_URL, null, null, authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("GetAllUsers", "Success: " + response.body().string());

                        // JSON verisini Machines listesine parse et
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray usersArray = jsonResponse.getJSONObject("payload").getJSONArray("users");

                        for (int i = 0; i < usersArray.length(); i++) {
                            JSONObject userObject = usersArray.getJSONObject(i);

                            String userRole = userObject.getString("userType");
                            String userName = userObject.getString("userName");
                            String eMail = userObject.getString("eMail");
                            String userNameSurname = userObject.getString("nameSurname");
                            String userPhoneNumber = userObject.getString("phoneNumber");
                            String companyName = userObject.getString("companyName");
                            String createdAt = userObject.getString("createdAt");
                            String isActive = userObject.getString("isActive");

                            JSONObject userPreferencesObject = userObject.getJSONObject("preferences");
                            String selectedLanguage = userPreferencesObject.getString("language");
                            String selectedNightMode = userPreferencesObject.getString("nightMode");

                            User user = new User(userRole, userName, eMail, userNameSurname, userPhoneNumber, companyName, createdAt, isActive, selectedLanguage, selectedNightMode);
                            users.add(user);
                        }

                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        Log.e("GetAllUsers", "Failure: " + response.errorBody().string());
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("GetAllUsers", "Error: " + t.getMessage());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        });
    }

    // updateRole method
    public static void updateRole(Context context, String userName, String newRole, Runnable onSuccess, Runnable onFailure) {
        String authToken = UserDataService.getAccessToken(context); // Auth token'ı al

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
            jsonObject.put("newRole", newRole);
        } catch (JSONException e) {
            e.printStackTrace();
            if (onFailure != null) {
                onFailure.run();
            }
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequestWithAuth("PUT", UPDATE_ROLE_URL, null, jsonObject.toString(), authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("UpdateRole", "Success: " + response.body().string());
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    }
                } else {
                    try {
                        Log.e("UpdateRole", "Failure: " + response.errorBody().string());
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("UpdateRole", "Error: " + t.getMessage());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        });
    }

    public static void deActivateUser(Context context, String userName, Runnable onSuccess, Runnable onFailure) {
        String authToken = UserDataService.getAccessToken(context); // Auth token'ı al

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
        } catch (JSONException e) {
            e.printStackTrace();
            if (onFailure != null) {
                onFailure.run();
            }
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequestWithAuth("POST", DEACTIVATE_USER_URL, null, jsonObject.toString(), authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("DeActivateUser", "Success: " + response.body().string());
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    }
                } else {
                    try {
                        Log.e("DeActivateUser", "Failure: " + response.errorBody().string());
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("DeActivateUser", "Error: " + t.getMessage());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        });
    }

    public static void activateUser(Context context, String userName, Runnable onSuccess, Runnable onFailure) {
        String authToken = UserDataService.getAccessToken(context); // Auth token'ı al

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
        } catch (JSONException e) {
            e.printStackTrace();
            if (onFailure != null) {
                onFailure.run();
            }
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequestWithAuth("POST", ACTIVATE_USER_URL, null, jsonObject.toString(), authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("ActivateUser", "Success: " + response.body().string());
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    }
                } else {
                    try {
                        Log.e("ActivateUser", "Failure: " + response.errorBody().string());
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ActivateUser", "Error: " + t.getMessage());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        });
    }

    public static void deleteUser(Context context, String userName, Runnable onSuccess, Runnable onFailure) {
        String authToken = UserDataService.getAccessToken(context); // Auth token'ı al

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
        } catch (JSONException e) {
            e.printStackTrace();
            if (onFailure != null) {
                onFailure.run();
            }
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequestWithAuth("POST", DELETE_USER_URL, null, jsonObject.toString(), authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("DeleteUser", "Success: " + response.body().string());

                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    }
                } else {
                    try {
                        Log.e("DeleteUser", "Failure: " + response.errorBody().string());

                        if (onFailure != null) {
                            onFailure.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("DeleteUser", "Error: " + t.getMessage());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        });
    }
}