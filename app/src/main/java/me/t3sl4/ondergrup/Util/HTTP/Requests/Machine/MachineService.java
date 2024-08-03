package me.t3sl4.ondergrup.Util.HTTP.Requests.Machine;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import me.t3sl4.ondergrup.Model.Machine.Machine;
import me.t3sl4.ondergrup.Model.MachineError.MachineError;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.HTTP.HttpHelper;
import me.t3sl4.ondergrup.Util.Util;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MachineService {
    private static final String CREATE_MACHINE_URL = "/api/v2/machine/createMachine";
    private static final String ADD_MACHINE_URL = "/api/v2/machine/addMachine";
    private static final String GET_ERRORS_URL = "/api/v2/machine/getErrors";
    private static final String GET_ERRORS_ALL_URL = "/api/v2/machine/getErrorsAll";
    private static final String GET_MACHINES_URL = "/api/v2/machine/getMachines";
    private static final String GET_MAINTENANCES_URL = "/api/v2/machine/getMaintenances";
    private static final String GET_MAINTENANCES_ALL_URL = "/api/v2/machine/getMaintenancesAll";

    public static void createMachine(Context context, String machineID, String machineType, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("machineID", machineID);
            jsonObject.put("machineType", machineType);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        String authToken = UserDataService.getAccessToken(context);
        Call<ResponseBody> call = HttpHelper.makeRequest("POST", CREATE_MACHINE_URL, null, jsonObject.toString(), authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("CreateMachine", "Success: " + response.body().string());
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("CreateMachine", "Failure: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("CreateMachine", "Error: " + t.getMessage());
            }
        });
    }

    public static void addMachine(Context context, String machineID, String ownerID, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("machineID", machineID);
            jsonObject.put("ownerID", ownerID);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        String authToken = UserDataService.getAccessToken(context);
        Call<ResponseBody> call = HttpHelper.makeRequest("POST", ADD_MACHINE_URL, null, jsonObject.toString(), authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("AddMachine", "Success: " + response.body().string());
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("AddMachine", "Failure: " + response.errorBody().string());
                        // Burada hata mesajını kullanıcıya gösteriyoruz.
                        Util.showErrorPopup(new Dialog(context), "Makine eklenirken bir hata oluştu. Lütfen tekrar deneyin.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("AddMachine", "Error: " + t.getMessage());
                // Burada hata mesajını kullanıcıya gösteriyoruz.
                Util.showErrorPopup(new Dialog(context), "Bir hata oluştu. Lütfen internet bağlantınızı kontrol edin.");
            }
        });
    }

    public static void getErrors(Context context, String machineID, ArrayList<MachineError> machineErrorsTemp, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("machineID", machineID);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        String authToken = UserDataService.getAccessToken(context);
        Call<ResponseBody> call = HttpHelper.makeRequest("POST", GET_ERRORS_URL, null, jsonObject.toString(), authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("GetErrors", "Success: " + responseBody);

                        // JSON response'i işleyin
                        JSONObject responseJson = new JSONObject(responseBody);
                        JSONArray machineErrorsArray = responseJson.getJSONObject("payload").getJSONArray("machineErrors");

                        machineErrorsTemp.clear(); // Listeyi temizle
                        for (int i = 0; i < machineErrorsArray.length(); i++) {
                            JSONObject machineErrorObj = machineErrorsArray.getJSONObject(i);
                            String machineID = machineErrorObj.getString("machineID");
                            String errorCode = machineErrorObj.getString("errorCode");
                            String errorMessage = machineErrorObj.getString("errorMessage");
                            String errorTime = machineErrorObj.getString("errorTime");

                            MachineError selectedMachine = new MachineError(machineID, errorCode, errorMessage, errorTime);
                            machineErrorsTemp.add(selectedMachine);
                        }

                        if (onSuccess != null) {
                            onSuccess.run();
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("GetErrors", "Failure: " + errorBody);
                        String hataMesaj = context.getResources().getString(R.string.hatayok);
                        Util.showErrorPopup(new Dialog(context), hataMesaj);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("GetErrors", "Error: " + t.getMessage());
                String hataMesaj = context.getResources().getString(R.string.hatayok);
                Util.showErrorPopup(new Dialog(context), hataMesaj);
            }
        });
    }

    public static void getErrorsAll(Context context, String userID, ArrayList<MachineError> machineErrorsTemp, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userID", userID);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        String authToken = UserDataService.getAccessToken(context);
        Call<ResponseBody> call = HttpHelper.makeRequest("POST", GET_ERRORS_ALL_URL, null, jsonObject.toString(), authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("GetErrorsAll", "Success: " + response.body().string());

                        // JSON response'i işleyin
                        JSONObject responseJson = new JSONObject(responseBody);
                        JSONArray machineErrorsArray = responseJson.getJSONObject("payload").getJSONArray("errors");

                        machineErrorsTemp.clear(); // Listeyi temizle
                        for (int i = 0; i < machineErrorsArray.length(); i++) {
                            JSONObject machineErrorObj = machineErrorsArray.getJSONObject(i);
                            String machineID = machineErrorObj.getString("machineID");
                            String errorCode = machineErrorObj.getString("errorCode");
                            String errorMessage = machineErrorObj.getString("errorMessage");
                            String errorTime = machineErrorObj.getString("errorTime");

                            MachineError selectedMachine = new MachineError(machineID, errorCode, errorMessage, errorTime);
                            machineErrorsTemp.add(selectedMachine);
                        }

                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("GetErrorsAll", "Failure: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("GetErrorsAll", "Error: " + t.getMessage());
            }
        });
    }

    public static void getMachines(Context context, String ownerID, ArrayList<Machine> machines, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ownerID", ownerID);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        String authToken = UserDataService.getAccessToken(context);
        Call<ResponseBody> call = HttpHelper.makeRequest("POST", GET_MACHINES_URL, null, jsonObject.toString(), authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("GetMachines", "Success: " + responseBody);

                        // JSON verisini Machines listesine parse et
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray machinesArray = jsonResponse.getJSONObject("payload").getJSONArray("machines");

                        for (int i = 0; i < machinesArray.length(); i++) {
                            JSONObject machineObject = machinesArray.getJSONObject(i);
                            JSONArray machineDataArray = machineObject.getJSONArray("machineData");

                            Machine machine = new Machine(machineObject.getString("machineID"), machineObject.getString("machineType"), machineObject.getString("ownerID"), machineObject.getString("ownerName"), machineObject.getString("createdAt"), machineObject.getString("lastUpdate"));

                            if (machineDataArray.length() > 0) {
                                JSONObject machineDataValues = machineDataArray.getJSONObject(0);
                                machine.setWifiSSID(machineDataValues.getString("wifiSSID"));
                                machine.setWifiPass(machineDataValues.getString("wifiPass"));
                                machine.setDevirmeYuruyusSecim(machineDataValues.getString("devirmeYuruyusSecim"));
                                machine.setCalismaSekli(machineDataValues.getString("calismaSekli"));
                                machine.setEmniyetCercevesi(machineDataValues.getString("emniyetCercevesi"));
                                machine.setYavaslamaLimit(machineDataValues.getString("yavaslamaLimit"));
                                machine.setAltLimit(machineDataValues.getString("altLimit"));
                                machine.setKapiTablaAcKonum(machineDataValues.getString("kapiTablaAcKonum"));
                                machine.setBasincSalteri(machineDataValues.getString("basincSalteri"));
                                machine.setKapiSecimleri(machineDataValues.getString("kapiSecimleri"));
                                machine.setKapiAcTipi(machineDataValues.getString("kapiAcTipi"));
                                machine.setKapi1Tip(machineDataValues.getString("kapi1Tip"));
                                machine.setKapi1AcSure(machineDataValues.getString("kapi1AcSure"));
                                machine.setKapi2Tip(machineDataValues.getString("kapi2Tip"));
                                machine.setKapi2AcSure(machineDataValues.getString("kapi2AcSure"));
                                machine.setKapitablaTip(machineDataValues.getString("kapitablaTip"));
                                machine.setKapiTablaAcSure(machineDataValues.getString("kapiTablaAcSure"));
                                machine.setYukariYavasLimit(machineDataValues.getString("yukariYavasLimit"));
                                machine.setDevirmeYukariIleriLimit(machineDataValues.getString("devirmeYukariIleriLimit"));
                                machine.setDevirmeAsagiGeriLimit(machineDataValues.getString("devirmeAsagiGeriLimit"));
                                machine.setDevirmeSilindirTipi(machineDataValues.getString("devirmeSilindirTipi"));
                                machine.setPlatformSilindirTipi(machineDataValues.getString("platformSilindirTipi"));
                                machine.setYukariValfTmr(machineDataValues.getString("yukariValfTmr"));
                                machine.setAsagiValfTmr(machineDataValues.getString("asagiValfTmr"));
                                machine.setDevirmeYukariIleriTmr(machineDataValues.getString("devirmeYukariIleriTmr"));
                                machine.setDevirmeAsagiGeriTmr(machineDataValues.getString("devirmeAsagiGeriTmr"));
                                machine.setMakineCalismaTmr(machineDataValues.getString("makineCalismaTmr"));
                                machine.setBuzzer(machineDataValues.getString("buzzer"));
                                machine.setDemoMode(machineDataValues.getString("demoMode"));
                                machine.setCalismaSayisi(machineDataValues.getString("calismaSayisi"));
                                machine.setCalismaSayisiDemo(machineDataValues.getString("calismaSayisiDemo"));
                                machine.setDilSecim(machineDataValues.getString("dilSecim"));
                                machine.setEepromData38(machineDataValues.getString("eepromData38"));
                                machine.setEepromData39(machineDataValues.getString("eepromData39"));
                                machine.setEepromData40(machineDataValues.getString("eepromData40"));
                                machine.setEepromData41(machineDataValues.getString("eepromData41"));
                                machine.setEepromData42(machineDataValues.getString("eepromData42"));
                                machine.setEepromData43(machineDataValues.getString("eepromData43"));
                                machine.setEepromData44(machineDataValues.getString("eepromData44"));
                                machine.setEepromData45(machineDataValues.getString("eepromData45"));
                                machine.setEepromData46(machineDataValues.getString("eepromData46"));
                                machine.setEepromData47(machineDataValues.getString("eepromData47"));
                                machine.setLcdBacklightSure(machineDataValues.getString("lcdBacklightSure"));
                            }

                            machines.add(machine);
                        }

                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("GetMachines", "Failure: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("GetMachines", "Error: " + t.getMessage());
            }
        });
    }

    public static void getMaintenances(Context context, String machineID, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("machineID", machineID);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        String authToken = UserDataService.getAccessToken(context);
        Call<ResponseBody> call = HttpHelper.makeRequest("POST", GET_MAINTENANCES_URL, null, jsonObject.toString(), authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("GetMaintenances", "Success: " + response.body().string());
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("GetMaintenances", "Failure: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("GetMaintenances", "Error: " + t.getMessage());
            }
        });
    }

    public static void getMaintenancesAll(Context context, String userID, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userID", userID);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        String authToken = UserDataService.getAccessToken(context);
        Call<ResponseBody> call = HttpHelper.makeRequest("POST", GET_MAINTENANCES_ALL_URL, null, jsonObject.toString(), authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("GetMaintenancesAll", "Success: " + response.body().string());
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("GetMaintenancesAll", "Failure: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("GetMaintenancesAll", "Error: " + t.getMessage());
            }
        });
    }
}