package me.t3sl4.ondergrup.Util.HTTP.Requests.Authorized;

import android.content.Context;
import android.util.Log;

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
        // Gerçek API isteği yorum satırına alındı, dummy data ile dolduruluyor
        /*
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
        */

        // Dummy data
        users.clear();
        
        // Dummy kullanıcı 1
        User user1 = new User("NORMAL", "recep.baskurt@ondergrup.com", "recep.baskurt@ondergrup.com", "Recep Baskurt", "5555555555", "Önder Grup", "01.01.2024", "1", "tr", "light");
        users.add(user1);
        
        // Dummy kullanıcı 2
        User user2 = new User("TECHNICIAN", "ahmet.tekniker@ondergrup.com", "ahmet.tekniker@ondergrup.com", "Ahmet Tekniker", "5555555556", "Önder Grup", "01.02.2024", "1", "tr", "light");
        users.add(user2);
        
        // Dummy kullanıcı 3
        User user3 = new User("ENGINEER", "mehmet.muhendis@ondergrup.com", "mehmet.muhendis@ondergrup.com", "Mehmet Mühendis", "5555555557", "Önder Grup", "01.03.2024", "1", "tr", "dark");
        users.add(user3);
        
        // Dummy kullanıcı 4
        User user4 = new User("NORMAL", "ali.kullanici@ondergrup.com", "ali.kullanici@ondergrup.com", "Ali Kullanıcı", "5555555558", "Önder Grup", "01.04.2024", "1", "en", "light");
        users.add(user4);
        
        // Dummy kullanıcı 5
        User user5 = new User("TECHNICIAN", "veli.tekniker@ondergrup.com", "veli.tekniker@ondergrup.com", "Veli Tekniker", "5555555559", "Önder Grup", "01.05.2024", "0", "tr", "dark");
        users.add(user5);
        
        // Dummy kullanıcı 6
        User user6 = new User("ENGINEER", "can.muhendis@ondergrup.com", "can.muhendis@ondergrup.com", "Can Mühendis", "5555555560", "Önder Grup", "01.06.2024", "1", "tr", "light");
        users.add(user6);
        
        // Dummy kullanıcı 7
        User user7 = new User("NORMAL", "deniz.kullanici@ondergrup.com", "deniz.kullanici@ondergrup.com", "Deniz Kullanıcı", "5555555561", "Önder Grup", "01.07.2024", "1", "en", "dark");
        users.add(user7);
        
        // Dummy kullanıcı 8
        User user8 = new User("TECHNICIAN", "emre.tekniker@ondergrup.com", "emre.tekniker@ondergrup.com", "Emre Tekniker", "5555555562", "Önder Grup", "01.08.2024", "1", "tr", "light");
        users.add(user8);
        
        // Dummy kullanıcı 9
        User user9 = new User("ENGINEER", "burak.muhendis@ondergrup.com", "burak.muhendis@ondergrup.com", "Burak Mühendis", "5555555563", "Önder Grup", "01.09.2024", "0", "tr", "dark");
        users.add(user9);
        
        // Dummy kullanıcı 10
        User user10 = new User("NORMAL", "seda.kullanici@ondergrup.com", "seda.kullanici@ondergrup.com", "Seda Kullanıcı", "5555555564", "Önder Grup", "01.10.2024", "1", "tr", "light");
        users.add(user10);
        
        // Dummy kullanıcı 11
        User user11 = new User("TECHNICIAN", "kaan.tekniker@ondergrup.com", "kaan.tekniker@ondergrup.com", "Kaan Tekniker", "5555555565", "Önder Grup", "01.11.2024", "1", "en", "dark");
        users.add(user11);
        
        // Dummy kullanıcı 12
        User user12 = new User("ENGINEER", "zeynep.muhendis@ondergrup.com", "zeynep.muhendis@ondergrup.com", "Zeynep Mühendis", "5555555566", "Önder Grup", "01.12.2024", "1", "tr", "light");
        users.add(user12);

        if (onSuccess != null) {
            onSuccess.run();
        }
    }

    // updateRole method
    public static void updateRole(Context context, String userID, String userName, String newRole, Runnable onSuccess, Runnable onFailure) {
        String authToken = UserDataService.getAccessToken(context); // Auth token'ı al

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("operationPlatform", "Android");
            jsonObject.put("sourceUserID", userID);
            jsonObject.put("affectedUserID", null);
            jsonObject.put("affectedUserName", userName);
            jsonObject.put("affectedMachineID", null);
            jsonObject.put("affectedMaintenanceID", null);
            jsonObject.put("affectedHydraulicUnitID", null);
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

    public static void deActivateUser(Context context, String userID, String userName, Runnable onSuccess, Runnable onFailure) {
        String authToken = UserDataService.getAccessToken(context); // Auth token'ı al

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("operationPlatform", "Android");
            jsonObject.put("sourceUserID", userID);
            jsonObject.put("affectedUserID", null);
            jsonObject.put("affectedUserName", userName);
            jsonObject.put("affectedMachineID", null);
            jsonObject.put("affectedMaintenanceID", null);
            jsonObject.put("affectedHydraulicUnitID", null);
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

    public static void activateUser(Context context, String userID, String userName, Runnable onSuccess, Runnable onFailure) {
        String authToken = UserDataService.getAccessToken(context); // Auth token'ı al

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("operationPlatform", "Android");
            jsonObject.put("sourceUserID", userID);
            jsonObject.put("affectedUserID", null);
            jsonObject.put("affectedUserName", userName);
            jsonObject.put("affectedMachineID", null);
            jsonObject.put("affectedMaintenanceID", null);
            jsonObject.put("affectedHydraulicUnitID", null);
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

    public static void deleteUser(Context context, String userID, String userName, Runnable onSuccess, Runnable onFailure) {
        String authToken = UserDataService.getAccessToken(context); // Auth token'ı al

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("operationPlatform", "Android");
            jsonObject.put("sourceUserID", userID);
            jsonObject.put("affectedUserID", null);
            jsonObject.put("affectedUserName", userName);
            jsonObject.put("affectedMachineID", null);
            jsonObject.put("affectedMaintenanceID", null);
            jsonObject.put("affectedHydraulicUnitID", null);
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