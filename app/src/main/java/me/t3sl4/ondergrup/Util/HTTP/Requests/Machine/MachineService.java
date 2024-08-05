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
import me.t3sl4.ondergrup.Model.MachineMaintenance.Maintenance;
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

    public static void getErrors(Context context, String machineID, ArrayList<MachineError> machineErrorsTemp, Runnable onSuccess, Runnable onFailure) {
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
                Log.e("GetErrors", "Error: " + t.getMessage());
                //Boş kısım revize edilecek
            }
        });
    }

    public static void getErrorsAll(Context context, String userID, ArrayList<MachineError> machineErrorsTemp, Runnable onSuccess, Runnable onFailure) {
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
                        Log.e("GetErrorsAll1", "Failure: " + response.errorBody().string());

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
                Log.e("GetErrorsAll2", "Error: " + t.getMessage());
                //Boş kısım revize edilecek
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
                                machine.setWifiSSID(machineDataValues.getString("wifiSSID") != null ? machineDataValues.getString("wifiSSID") : context.getResources().getString(R.string.machine_data_null));
                                machine.setWifiPass(machineDataValues.getString("wifiPass") != null ? machineDataValues.getString("wifiPass") : context.getResources().getString(R.string.machine_data_null));
                                machine.setDevirmeYuruyusSecim(machineDataValues.getString("devirmeYuruyusSecim") != null ? machineDataValues.getString("devirmeYuruyusSecim") : context.getResources().getString(R.string.machine_data_null));
                                machine.setCalismaSekli(machineDataValues.getString("calismaSekli") != null ? machineDataValues.getString("calismaSekli") : context.getResources().getString(R.string.machine_data_null));
                                machine.setEmniyetCercevesi(machineDataValues.getString("emniyetCercevesi") != null ? machineDataValues.getString("emniyetCercevesi") : context.getResources().getString(R.string.machine_data_null));
                                machine.setYavaslamaLimit(machineDataValues.getString("yavaslamaLimit") != null ? machineDataValues.getString("yavaslamaLimit") : context.getResources().getString(R.string.machine_data_null));
                                machine.setAltLimit(machineDataValues.getString("altLimit") != null ? machineDataValues.getString("altLimit") : context.getResources().getString(R.string.machine_data_null));
                                machine.setKapiTablaAcKonum(machineDataValues.getString("kapiTablaAcKonum") != null ? machineDataValues.getString("kapiTablaAcKonum") : context.getResources().getString(R.string.machine_data_null));
                                machine.setBasincSalteri(machineDataValues.getString("basincSalteri") != null ? machineDataValues.getString("basincSalteri") : context.getResources().getString(R.string.machine_data_null));
                                machine.setKapiSecimleri(machineDataValues.getString("kapiSecimleri") != null ? machineDataValues.getString("kapiSecimleri") : context.getResources().getString(R.string.machine_data_null));
                                machine.setKapiAcTipi(machineDataValues.getString("kapiAcTipi") != null ? machineDataValues.getString("kapiAcTipi") : context.getResources().getString(R.string.machine_data_null));
                                machine.setKapi1Tip(machineDataValues.getString("kapi1Tip") != null ? machineDataValues.getString("kapi1Tip") : context.getResources().getString(R.string.machine_data_null));
                                machine.setKapi1AcSure(machineDataValues.getString("kapi1AcSure") != null ? machineDataValues.getString("kapi1AcSure") : context.getResources().getString(R.string.machine_data_null));
                                machine.setKapi2Tip(machineDataValues.getString("kapi2Tip") != null ? machineDataValues.getString("kapi2Tip") : context.getResources().getString(R.string.machine_data_null));
                                machine.setKapi2AcSure(machineDataValues.getString("kapi2AcSure") != null ? machineDataValues.getString("kapi2AcSure") : context.getResources().getString(R.string.machine_data_null));
                                machine.setKapitablaTip(machineDataValues.getString("kapitablaTip") != null ? machineDataValues.getString("kapitablaTip") : context.getResources().getString(R.string.machine_data_null));
                                machine.setKapiTablaAcSure(machineDataValues.getString("kapiTablaAcSure") != null ? machineDataValues.getString("kapiTablaAcSure") : context.getResources().getString(R.string.machine_data_null));
                                machine.setYukariYavasLimit(machineDataValues.getString("yukariYavasLimit") != null ? machineDataValues.getString("yukariYavasLimit") : context.getResources().getString(R.string.machine_data_null));
                                machine.setDevirmeYukariIleriLimit(machineDataValues.getString("devirmeYukariIleriLimit") != null ? machineDataValues.getString("devirmeYukariIleriLimit") : context.getResources().getString(R.string.machine_data_null));
                                machine.setDevirmeAsagiGeriLimit(machineDataValues.getString("devirmeAsagiGeriLimit") != null ? machineDataValues.getString("devirmeAsagiGeriLimit") : context.getResources().getString(R.string.machine_data_null));
                                machine.setDevirmeSilindirTipi(machineDataValues.getString("devirmeSilindirTipi") != null ? machineDataValues.getString("devirmeSilindirTipi") : context.getResources().getString(R.string.machine_data_null));
                                machine.setPlatformSilindirTipi(machineDataValues.getString("platformSilindirTipi") != null ? machineDataValues.getString("platformSilindirTipi") : context.getResources().getString(R.string.machine_data_null));
                                machine.setYukariValfTmr(machineDataValues.getString("yukariValfTmr") != null ? machineDataValues.getString("yukariValfTmr") : context.getResources().getString(R.string.machine_data_null));
                                machine.setAsagiValfTmr(machineDataValues.getString("asagiValfTmr") != null ? machineDataValues.getString("asagiValfTmr") : context.getResources().getString(R.string.machine_data_null));
                                machine.setDevirmeYukariIleriTmr(machineDataValues.getString("devirmeYukariIleriTmr") != null ? machineDataValues.getString("devirmeYukariIleriTmr") : context.getResources().getString(R.string.machine_data_null));
                                machine.setDevirmeAsagiGeriTmr(machineDataValues.getString("devirmeAsagiGeriTmr") != null ? machineDataValues.getString("devirmeAsagiGeriTmr") : context.getResources().getString(R.string.machine_data_null));
                                machine.setMakineCalismaTmr(machineDataValues.getString("makineCalismaTmr") != null ? machineDataValues.getString("makineCalismaTmr") : context.getResources().getString(R.string.machine_data_null));
                                machine.setBuzzer(machineDataValues.getString("buzzer") != null ? machineDataValues.getString("buzzer") : context.getResources().getString(R.string.machine_data_null));
                                machine.setDemoMode(machineDataValues.getString("demoMode") != null ? machineDataValues.getString("demoMode") : context.getResources().getString(R.string.machine_data_null));
                                machine.setCalismaSayisi(machineDataValues.getString("calismaSayisi") != null ? machineDataValues.getString("calismaSayisi") : context.getResources().getString(R.string.machine_data_null));
                                machine.setCalismaSayisiDemo(machineDataValues.getString("calismaSayisiDemo") != null ? machineDataValues.getString("calismaSayisiDemo") : context.getResources().getString(R.string.machine_data_null));
                                machine.setDilSecim(machineDataValues.getString("dilSecim") != null ? machineDataValues.getString("dilSecim") : context.getResources().getString(R.string.machine_data_null));
                                machine.setEepromData38(machineDataValues.getString("eepromData38") != null ? machineDataValues.getString("eepromData38") : context.getResources().getString(R.string.machine_data_null));
                                machine.setEepromData39(machineDataValues.getString("eepromData39") != null ? machineDataValues.getString("eepromData39") : context.getResources().getString(R.string.machine_data_null));
                                machine.setEepromData40(machineDataValues.getString("eepromData40") != null ? machineDataValues.getString("eepromData40") : context.getResources().getString(R.string.machine_data_null));
                                machine.setEepromData41(machineDataValues.getString("eepromData41") != null ? machineDataValues.getString("eepromData41") : context.getResources().getString(R.string.machine_data_null));
                                machine.setEepromData42(machineDataValues.getString("eepromData42") != null ? machineDataValues.getString("eepromData42") : context.getResources().getString(R.string.machine_data_null));
                                machine.setEepromData43(machineDataValues.getString("eepromData43") != null ? machineDataValues.getString("eepromData43") : context.getResources().getString(R.string.machine_data_null));
                                machine.setEepromData44(machineDataValues.getString("eepromData44") != null ? machineDataValues.getString("eepromData44") : context.getResources().getString(R.string.machine_data_null));
                                machine.setEepromData45(machineDataValues.getString("eepromData45") != null ? machineDataValues.getString("eepromData45") : context.getResources().getString(R.string.machine_data_null));
                                machine.setEepromData46(machineDataValues.getString("eepromData46") != null ? machineDataValues.getString("eepromData46") : context.getResources().getString(R.string.machine_data_null));
                                machine.setEepromData47(machineDataValues.getString("eepromData47") != null ? machineDataValues.getString("eepromData47") : context.getResources().getString(R.string.machine_data_null));
                                machine.setLcdBacklightSure(machineDataValues.getString("lcdBacklightSure") != null ? machineDataValues.getString("lcdBacklightSure") : context.getResources().getString(R.string.machine_data_null));
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

    public static void getMaintenances(Context context, String machineID, ArrayList<Maintenance> machineMaintenancesTemp, Runnable onSuccess, Runnable onFailure) {
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
                        String responseBody = response.body().string();
                        Log.d("GetMaintenances", "Success: " + response.body().string());

                        // JSON response'i işleyin
                        JSONObject responseJson = new JSONObject(responseBody);
                        JSONArray machineMaintenancesArray = responseJson.getJSONObject("payload").getJSONArray("machineMaintenancesWithTechnicianNames");

                        machineMaintenancesTemp.clear(); // Listeyi temizle
                        for (int i = 0; i < machineMaintenancesArray.length(); i++) {
                            JSONObject machineMaintenanceObj = machineMaintenancesArray.getJSONObject(i);

                            String maintenanceID = machineMaintenanceObj.getString("id");
                            String machineID = machineMaintenanceObj.getString("machineID");
                            String technicianID = machineMaintenanceObj.getString("technicianID");
                            String technicianName = machineMaintenanceObj.getString("technicianName");
                            String maintenanceDate = machineMaintenanceObj.getString("maintenanceDate");

                            Maintenance selectedMaintenance = new Maintenance(maintenanceID, machineID, technicianID, technicianName, maintenanceDate);
                            selectedMaintenance.setKontrol11(machineMaintenanceObj.getString("kontrol11"));
                            selectedMaintenance.setKontrol12(machineMaintenanceObj.getString("kontrol12"));
                            selectedMaintenance.setKontrol13(machineMaintenanceObj.getString("kontrol13"));
                            selectedMaintenance.setKontrol14(machineMaintenanceObj.getString("kontrol14"));
                            selectedMaintenance.setKontrol21(machineMaintenanceObj.getString("kontrol21"));
                            selectedMaintenance.setKontrol22(machineMaintenanceObj.getString("kontrol22"));
                            selectedMaintenance.setKontrol23(machineMaintenanceObj.getString("kontrol23"));
                            selectedMaintenance.setKontrol24(machineMaintenanceObj.getString("kontrol24"));
                            selectedMaintenance.setKontrol31(machineMaintenanceObj.getString("kontrol31"));
                            selectedMaintenance.setKontrol32(machineMaintenanceObj.getString("kontrol32"));
                            selectedMaintenance.setKontrol33(machineMaintenanceObj.getString("kontrol33"));
                            selectedMaintenance.setKontrol34(machineMaintenanceObj.getString("kontrol34"));
                            selectedMaintenance.setKontrol35(machineMaintenanceObj.getString("kontrol35"));
                            selectedMaintenance.setKontrol36(machineMaintenanceObj.getString("kontrol36"));
                            selectedMaintenance.setKontrol41(machineMaintenanceObj.getString("kontrol41"));
                            selectedMaintenance.setKontrol42(machineMaintenanceObj.getString("kontrol42"));
                            selectedMaintenance.setKontrol43(machineMaintenanceObj.getString("kontrol43"));
                            selectedMaintenance.setKontrol44(machineMaintenanceObj.getString("kontrol44"));
                            selectedMaintenance.setKontrol45(machineMaintenanceObj.getString("kontrol45"));
                            selectedMaintenance.setKontrol46(machineMaintenanceObj.getString("kontrol46"));
                            selectedMaintenance.setKontrol51(machineMaintenanceObj.getString("kontrol51"));
                            selectedMaintenance.setKontrol52(machineMaintenanceObj.getString("kontrol52"));
                            selectedMaintenance.setKontrol53(machineMaintenanceObj.getString("kontrol53"));
                            selectedMaintenance.setKontrol54(machineMaintenanceObj.getString("kontrol54"));
                            selectedMaintenance.setKontrol55(machineMaintenanceObj.getString("kontrol55"));
                            selectedMaintenance.setKontrol56(machineMaintenanceObj.getString("kontrol56"));
                            selectedMaintenance.setKontrol61(machineMaintenanceObj.getString("kontrol61"));
                            selectedMaintenance.setKontrol62(machineMaintenanceObj.getString("kontrol62"));
                            selectedMaintenance.setKontrol63(machineMaintenanceObj.getString("kontrol63"));
                            selectedMaintenance.setKontrol71(machineMaintenanceObj.getString("kontrol71"));
                            selectedMaintenance.setKontrol72(machineMaintenanceObj.getString("kontrol72"));
                            selectedMaintenance.setKontrol81(machineMaintenanceObj.getString("kontrol81"));
                            selectedMaintenance.setKontrol82(machineMaintenanceObj.getString("kontrol82"));
                            selectedMaintenance.setKontrol83(machineMaintenanceObj.getString("kontrol83"));
                            selectedMaintenance.setKontrol91(machineMaintenanceObj.getString("kontrol91"));
                            selectedMaintenance.setKontrol92(machineMaintenanceObj.getString("kontrol92"));
                            selectedMaintenance.setKontrol93(machineMaintenanceObj.getString("kontrol93"));
                            selectedMaintenance.setKontrol94(machineMaintenanceObj.getString("kontrol94"));
                            selectedMaintenance.setKontrol95(machineMaintenanceObj.getString("kontrol95"));
                            selectedMaintenance.setKontrol96(machineMaintenanceObj.getString("kontrol96"));
                            selectedMaintenance.setKontrol97(machineMaintenanceObj.getString("kontrol97"));
                            selectedMaintenance.setKontrol98(machineMaintenanceObj.getString("kontrol98"));
                            selectedMaintenance.setKontrol99(machineMaintenanceObj.getString("kontrol99"));
                            selectedMaintenance.setKontrol910(machineMaintenanceObj.getString("kontrol910"));

                            machineMaintenancesTemp.add(selectedMaintenance);
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
                        Log.e("GetMaintenances", "Failure: " + response.errorBody().string());

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
                Log.e("GetMaintenances", "Error: " + t.getMessage());
                //Boş kısım revize edilecek
            }
        });
    }

    public static void getMaintenancesAll(Context context, String userID, ArrayList<Maintenance> machineMaintenancesTemp, Runnable onSuccess, Runnable onFailure) {
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
                        String responseBody = response.body().string();
                        Log.d("GetMaintenancesAll", "Success: " + response.body().string());

                        // JSON response'i işleyin
                        JSONObject responseJson = new JSONObject(responseBody);
                        JSONArray machineMaintenancesArray = responseJson.getJSONObject("payload").getJSONArray("maintenancesWithTechnicianNames");

                        machineMaintenancesTemp.clear(); // Listeyi temizle
                        for (int i = 0; i < machineMaintenancesArray.length(); i++) {
                            JSONObject machineMaintenanceObj = machineMaintenancesArray.getJSONObject(i);

                            String maintenanceID = machineMaintenanceObj.getString("id");
                            String machineID = machineMaintenanceObj.getString("machineID");
                            String technicianID = machineMaintenanceObj.getString("technicianID");
                            String technicianName = machineMaintenanceObj.getString("technicianName");
                            String maintenanceDate = machineMaintenanceObj.getString("maintenanceDate");

                            Maintenance selectedMaintenance = new Maintenance(maintenanceID, machineID, technicianID, technicianName, maintenanceDate);
                            selectedMaintenance.setKontrol11(machineMaintenanceObj.getString("kontrol11"));
                            selectedMaintenance.setKontrol12(machineMaintenanceObj.getString("kontrol12"));
                            selectedMaintenance.setKontrol13(machineMaintenanceObj.getString("kontrol13"));
                            selectedMaintenance.setKontrol14(machineMaintenanceObj.getString("kontrol14"));
                            selectedMaintenance.setKontrol21(machineMaintenanceObj.getString("kontrol21"));
                            selectedMaintenance.setKontrol22(machineMaintenanceObj.getString("kontrol22"));
                            selectedMaintenance.setKontrol23(machineMaintenanceObj.getString("kontrol23"));
                            selectedMaintenance.setKontrol24(machineMaintenanceObj.getString("kontrol24"));
                            selectedMaintenance.setKontrol31(machineMaintenanceObj.getString("kontrol31"));
                            selectedMaintenance.setKontrol32(machineMaintenanceObj.getString("kontrol32"));
                            selectedMaintenance.setKontrol33(machineMaintenanceObj.getString("kontrol33"));
                            selectedMaintenance.setKontrol34(machineMaintenanceObj.getString("kontrol34"));
                            selectedMaintenance.setKontrol35(machineMaintenanceObj.getString("kontrol35"));
                            selectedMaintenance.setKontrol36(machineMaintenanceObj.getString("kontrol36"));
                            selectedMaintenance.setKontrol41(machineMaintenanceObj.getString("kontrol41"));
                            selectedMaintenance.setKontrol42(machineMaintenanceObj.getString("kontrol42"));
                            selectedMaintenance.setKontrol43(machineMaintenanceObj.getString("kontrol43"));
                            selectedMaintenance.setKontrol44(machineMaintenanceObj.getString("kontrol44"));
                            selectedMaintenance.setKontrol45(machineMaintenanceObj.getString("kontrol45"));
                            selectedMaintenance.setKontrol46(machineMaintenanceObj.getString("kontrol46"));
                            selectedMaintenance.setKontrol51(machineMaintenanceObj.getString("kontrol51"));
                            selectedMaintenance.setKontrol52(machineMaintenanceObj.getString("kontrol52"));
                            selectedMaintenance.setKontrol53(machineMaintenanceObj.getString("kontrol53"));
                            selectedMaintenance.setKontrol54(machineMaintenanceObj.getString("kontrol54"));
                            selectedMaintenance.setKontrol55(machineMaintenanceObj.getString("kontrol55"));
                            selectedMaintenance.setKontrol56(machineMaintenanceObj.getString("kontrol56"));
                            selectedMaintenance.setKontrol61(machineMaintenanceObj.getString("kontrol61"));
                            selectedMaintenance.setKontrol62(machineMaintenanceObj.getString("kontrol62"));
                            selectedMaintenance.setKontrol63(machineMaintenanceObj.getString("kontrol63"));
                            selectedMaintenance.setKontrol71(machineMaintenanceObj.getString("kontrol71"));
                            selectedMaintenance.setKontrol72(machineMaintenanceObj.getString("kontrol72"));
                            selectedMaintenance.setKontrol81(machineMaintenanceObj.getString("kontrol81"));
                            selectedMaintenance.setKontrol82(machineMaintenanceObj.getString("kontrol82"));
                            selectedMaintenance.setKontrol83(machineMaintenanceObj.getString("kontrol83"));
                            selectedMaintenance.setKontrol91(machineMaintenanceObj.getString("kontrol91"));
                            selectedMaintenance.setKontrol92(machineMaintenanceObj.getString("kontrol92"));
                            selectedMaintenance.setKontrol93(machineMaintenanceObj.getString("kontrol93"));
                            selectedMaintenance.setKontrol94(machineMaintenanceObj.getString("kontrol94"));
                            selectedMaintenance.setKontrol95(machineMaintenanceObj.getString("kontrol95"));
                            selectedMaintenance.setKontrol96(machineMaintenanceObj.getString("kontrol96"));
                            selectedMaintenance.setKontrol97(machineMaintenanceObj.getString("kontrol97"));
                            selectedMaintenance.setKontrol98(machineMaintenanceObj.getString("kontrol98"));
                            selectedMaintenance.setKontrol99(machineMaintenanceObj.getString("kontrol99"));
                            selectedMaintenance.setKontrol910(machineMaintenanceObj.getString("kontrol910"));

                            machineMaintenancesTemp.add(selectedMaintenance);
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
                        Log.e("GetMaintenancesAll", "Failure: " + response.errorBody().string());

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
                Log.e("GetMaintenancesAll", "Error: " + t.getMessage());

                //Boş kısım revize edilecek
            }
        });
    }
}