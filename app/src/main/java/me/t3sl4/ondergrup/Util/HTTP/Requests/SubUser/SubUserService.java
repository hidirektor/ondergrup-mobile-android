package me.t3sl4.ondergrup.Util.HTTP.Requests.SubUser;

import android.content.Context;
import android.util.Log;

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
    private static final String DEACTIVATE_SUB_USER_URL = "/api/v2/subuser/deActivateSubUser";
    private static final String ACTIVATE_SUB_USER_URL = "/api/v2/subuser/activateSubUser";
    private static final String EDIT_SUB_USER_URL = "/api/v2/subuser/editSubUser";

    public static void createSubUser(Context context, String ownerID, String userName, String userType, String nameSurname, String eMail, String phoneNumber, String companyName, String password, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("operationPlatform", "Android");
            jsonObject.put("sourceUserID", ownerID);
            jsonObject.put("affectedUserID", null);
            jsonObject.put("affectedUserName", userName);
            jsonObject.put("affectedMachineID", null);
            jsonObject.put("affectedMaintenanceID", null);
            jsonObject.put("affectedHydraulicUnitID", null);
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

    public static void getSubUsers(Context context, String ownerID, ArrayList<SubUser> subUsers, Runnable onSuccess, Runnable onFailure) {
        // Gerçek API isteği yorum satırına alındı, dummy data ile dolduruluyor
        /*
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
                                    ownerID,
                                    subUserObject.getString("isActive"));

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
                Log.e("GetSubUsers", "Error: " + t.getMessage());
                //Boş kısım revize edilecek
            }
        });
        */

        // Dummy data
        subUsers.clear();
        
        // Dummy alt kullanıcı 1
        SubUser subUser1 = new SubUser("SUB001", "NORMAL", "subuser1@ondergrup.com", "subuser1@ondergrup.com", "Alt Kullanıcı 1", "5555555560", "Önder Grup", ownerID, "1");
        subUsers.add(subUser1);
        
        // Dummy alt kullanıcı 2
        SubUser subUser2 = new SubUser("SUB002", "NORMAL", "subuser2@ondergrup.com", "subuser2@ondergrup.com", "Alt Kullanıcı 2", "5555555561", "Önder Grup", ownerID, "1");
        subUsers.add(subUser2);
        
        // Dummy alt kullanıcı 3
        SubUser subUser3 = new SubUser("SUB003", "NORMAL", "subuser3@ondergrup.com", "subuser3@ondergrup.com", "Alt Kullanıcı 3", "5555555562", "Önder Grup", ownerID, "0");
        subUsers.add(subUser3);
        
        // Dummy alt kullanıcı 4
        SubUser subUser4 = new SubUser("SUB004", "NORMAL", "ahmet.yilmaz@ondergrup.com", "ahmet.yilmaz@ondergrup.com", "Ahmet Yılmaz", "5555555563", "Önder Grup", ownerID, "1");
        subUsers.add(subUser4);
        
        // Dummy alt kullanıcı 5
        SubUser subUser5 = new SubUser("SUB005", "NORMAL", "mehmet.demir@ondergrup.com", "mehmet.demir@ondergrup.com", "Mehmet Demir", "5555555564", "Önder Grup", ownerID, "1");
        subUsers.add(subUser5);
        
        // Dummy alt kullanıcı 6
        SubUser subUser6 = new SubUser("SUB006", "NORMAL", "fatma.kaya@ondergrup.com", "fatma.kaya@ondergrup.com", "Fatma Kaya", "5555555565", "Önder Grup", ownerID, "0");
        subUsers.add(subUser6);
        
        // Dummy alt kullanıcı 7
        SubUser subUser7 = new SubUser("SUB007", "NORMAL", "ali.ozturk@ondergrup.com", "ali.ozturk@ondergrup.com", "Ali Öztürk", "5555555566", "Önder Grup", ownerID, "1");
        subUsers.add(subUser7);
        
        // Dummy alt kullanıcı 8
        SubUser subUser8 = new SubUser("SUB008", "NORMAL", "ayse.celik@ondergrup.com", "ayse.celik@ondergrup.com", "Ayşe Çelik", "5555555567", "Önder Grup", ownerID, "1");
        subUsers.add(subUser8);

        if (onSuccess != null) {
            onSuccess.run();
        }
    }

    public static void deleteSubUser(Context context, String userID, String subUserID, Runnable onSuccess) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("operationPlatform", "Android");
            jsonObject.put("sourceUserID", userID);
            jsonObject.put("affectedUserID", subUserID);
            jsonObject.put("affectedUserName", null);
            jsonObject.put("affectedMachineID", null);
            jsonObject.put("affectedMaintenanceID", null);
            jsonObject.put("affectedHydraulicUnitID", null);
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

    public static void deActivateSubUser(Context context, String userID, String subUserID, Runnable onSuccess) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("operationPlatform", "Android");
            jsonObject.put("sourceUserID", userID);
            jsonObject.put("affectedUserID", subUserID);
            jsonObject.put("affectedUserName", null);
            jsonObject.put("affectedMachineID", null);
            jsonObject.put("affectedMaintenanceID", null);
            jsonObject.put("affectedHydraulicUnitID", null);
            jsonObject.put("subUserID", subUserID);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        String accessToken = UserDataService.getAccessToken(context);
        Call<ResponseBody> call = HttpHelper.makeRequestWithAuth("POST", DEACTIVATE_SUB_USER_URL, null, jsonObject.toString(), accessToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("DeActivateSubUser", "Success: " + response.body().string());
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("DeActivateSubUser", "Failure: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("DeActivateSubUser", "Error: " + t.getMessage());
            }
        });
    }

    public static void activateSubUser(Context context, String userID, String subUserID, Runnable onSuccess) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("operationPlatform", "Android");
            jsonObject.put("sourceUserID", userID);
            jsonObject.put("affectedUserID", subUserID);
            jsonObject.put("affectedUserName", null);
            jsonObject.put("affectedMachineID", null);
            jsonObject.put("affectedMaintenanceID", null);
            jsonObject.put("affectedHydraulicUnitID", null);
            jsonObject.put("subUserID", subUserID);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        String accessToken = UserDataService.getAccessToken(context);
        Call<ResponseBody> call = HttpHelper.makeRequestWithAuth("POST", ACTIVATE_SUB_USER_URL, null, jsonObject.toString(), accessToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("ActivateSubUser", "Success: " + response.body().string());
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("ActivateSubUser", "Failure: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ActivateSubUser", "Error: " + t.getMessage());
            }
        });
    }

    public static void editSubUser(Context context, String ownerID, String userID, String userName, String userType, String nameSurname, String eMail, String phoneNumber, String companyName, String password, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("operationPlatform", "Android");
            jsonObject.put("sourceUserID", UserDataService.getUserID(context));
            jsonObject.put("affectedUserID", userID);
            jsonObject.put("affectedUserName", null);
            jsonObject.put("affectedMachineID", null);
            jsonObject.put("affectedMaintenanceID", null);
            jsonObject.put("affectedHydraulicUnitID", null);
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