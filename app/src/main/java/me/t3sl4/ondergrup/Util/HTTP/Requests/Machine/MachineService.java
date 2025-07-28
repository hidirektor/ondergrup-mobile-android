package me.t3sl4.ondergrup.Util.HTTP.Requests.Machine;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import me.t3sl4.ondergrup.Model.Machine.Machine;
import me.t3sl4.ondergrup.Model.MachineError.MachineError;
import me.t3sl4.ondergrup.Model.MachineMaintenance.Maintenance;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.HTTP.HttpHelper;
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

    public static void createMachine(Context context, String userID, String machineID, String machineType, Runnable onSuccess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("operationPlatform", "Android");
            jsonObject.put("sourceUserID", userID);
            jsonObject.put("affectedUserID", null);
            jsonObject.put("affectedUserName", null);
            jsonObject.put("affectedMachineID", machineID);
            jsonObject.put("affectedMaintenanceID", null);
            jsonObject.put("affectedHydraulicUnitID", null);
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

    public static void addMachine(Context context, String machineID, String ownerID, Runnable onSuccess, Runnable onFailure) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("operationPlatform", "Android");
            jsonObject.put("sourceUserID", ownerID);
            jsonObject.put("affectedUserID", null);
            jsonObject.put("affectedUserName", null);
            jsonObject.put("affectedMachineID", null);
            jsonObject.put("affectedMaintenanceID", null);
            jsonObject.put("affectedHydraulicUnitID", null);
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
                Log.e("AddMachine", "Error: " + t.getMessage());
                // Boş kısım revize edilecek.
            }
        });
    }

    public static void getErrors(Context context, String machineID, ArrayList<MachineError> machineErrorsTemp, Runnable onSuccess, Runnable onFailure) {
        // Gerçek API isteği yorum satırına alındı, dummy data ile dolduruluyor
        /*
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
        */

        // Dummy data
        machineErrorsTemp.clear();
        
        // Dummy hata 1
        MachineError error1 = new MachineError(machineID, "E001", "Basınç sensörü hatası", "15.01.2024 10:30:00");
        machineErrorsTemp.add(error1);
        
        // Dummy hata 2
        MachineError error2 = new MachineError(machineID, "E002", "Sıcaklık sensörü arızası", "14.01.2024 15:45:00");
        machineErrorsTemp.add(error2);
        
        // Dummy hata 3
        MachineError error3 = new MachineError(machineID, "E003", "Hidrolik sistem basınç düşüşü", "13.01.2024 09:15:00");
        machineErrorsTemp.add(error3);

        if (onSuccess != null) {
            onSuccess.run();
        }
    }

    public static void getErrorsAll(Context context, String userID, ArrayList<MachineError> machineErrorsTemp, Runnable onSuccess, Runnable onFailure) {
        // Gerçek API isteği yorum satırına alındı, dummy data ile dolduruluyor
        /*
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
        */

        // Dummy data
        machineErrorsTemp.clear();
        
        // Dummy hata 1 - MACH001
        MachineError error1 = new MachineError("MACH001", "E001", "Basınç sensörü hatası", "15.01.2024 10:30:00");
        machineErrorsTemp.add(error1);
        
        // Dummy hata 2 - MACH001
        MachineError error2 = new MachineError("MACH001", "E002", "Sıcaklık sensörü arızası", "14.01.2024 15:45:00");
        machineErrorsTemp.add(error2);
        
        // Dummy hata 3 - MACH002
        MachineError error3 = new MachineError("MACH002", "E003", "Hidrolik sistem basınç düşüşü", "13.01.2024 09:15:00");
        machineErrorsTemp.add(error3);
        
        // Dummy hata 4 - MACH002
        MachineError error4 = new MachineError("MACH002", "E004", "Motor aşırı ısınma", "12.01.2024 14:20:00");
        machineErrorsTemp.add(error4);
        
        // Dummy hata 5 - MACH001
        MachineError error5 = new MachineError("MACH001", "E005", "Yağ seviyesi düşük", "11.01.2024 08:45:00");
        machineErrorsTemp.add(error5);
        
        // Dummy hata 6 - MACH003
        MachineError error6 = new MachineError("MACH003", "E006", "Kontrol paneli arızası", "10.01.2024 16:30:00");
        machineErrorsTemp.add(error6);
        
        // Dummy hata 7 - MACH002
        MachineError error7 = new MachineError("MACH002", "E007", "Valf sistemi blokajı", "09.01.2024 11:20:00");
        machineErrorsTemp.add(error7);
        
        // Dummy hata 8 - MACH001
        MachineError error8 = new MachineError("MACH001", "E008", "Elektrik bağlantı sorunu", "08.01.2024 13:15:00");
        machineErrorsTemp.add(error8);
        
        // Dummy hata 9 - MACH003
        MachineError error9 = new MachineError("MACH003", "E009", "Hidrolik yağ sızıntısı", "07.01.2024 09:45:00");
        machineErrorsTemp.add(error9);
        
        // Dummy hata 10 - MACH002
        MachineError error10 = new MachineError("MACH002", "E010", "Sensör kalibrasyon hatası", "06.01.2024 17:00:00");
        machineErrorsTemp.add(error10);
        
        // Dummy hata 11 - MACH001
        MachineError error11 = new MachineError("MACH001", "E011", "Kompresör arızası", "05.01.2024 10:30:00");
        machineErrorsTemp.add(error11);
        
        // Dummy hata 12 - MACH003
        MachineError error12 = new MachineError("MACH003", "E012", "Kabin kilidi arızası", "04.01.2024 14:45:00");
        machineErrorsTemp.add(error12);

        if (onSuccess != null) {
            onSuccess.run();
        }
    }

    public static void getMachines(Context context, String ownerID, ArrayList<Machine> machines, Runnable onSuccess) {
        // Gerçek API isteği yorum satırına alındı, dummy data ile dolduruluyor
        /*
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
        */

        // Dummy data
        machines.clear();
        
        // Dummy makine 1
        Machine machine1 = new Machine("MACH001", "ESP", ownerID, "Recep Baskurt", "01.01.2024", "15.01.2024");
        machine1.setWifiSSID("OnderGrup_WiFi");
        machine1.setWifiPass("12345678");
        machine1.setDevirmeYuruyusSecim("1");
        machine1.setCalismaSekli("Normal");
        machine1.setEmniyetCercevesi("Açık");
        machine1.setYavaslamaLimit("100");
        machine1.setAltLimit("50");
        machine1.setKapiTablaAcKonum("Açık");
        machine1.setBasincSalteri("Aktif");
        machine1.setKapiSecimleri("Otomatik");
        machine1.setKapiAcTipi("Hidrolik");
        machine1.setKapi1Tip("Tek Kapı");
        machine1.setKapi1AcSure("3");
        machine1.setKapi2Tip("Çift Kapı");
        machine1.setKapi2AcSure("5");
        machine1.setKapitablaTip("Hidrolik");
        machine1.setKapiTablaAcSure("4");
        machine1.setYukariYavasLimit("80");
        machine1.setDevirmeYukariIleriLimit("90");
        machine1.setDevirmeAsagiGeriLimit("10");
        machine1.setDevirmeSilindirTipi("Çift Etkili");
        machine1.setPlatformSilindirTipi("Tek Etkili");
        machine1.setYukariValfTmr("2");
        machine1.setAsagiValfTmr("2");
        machine1.setDevirmeYukariIleriTmr("3");
        machine1.setDevirmeAsagiGeriTmr("3");
        machine1.setMakineCalismaTmr("10");
        machine1.setBuzzer("Aktif");
        machine1.setDemoMode("0");
        machine1.setCalismaSayisi("1500");
        machine1.setCalismaSayisiDemo("0");
        machine1.setDilSecim("1");
        machine1.setEepromData38("38");
        machine1.setEepromData39("39");
        machine1.setEepromData40("40");
        machine1.setEepromData41("41");
        machine1.setEepromData42("42");
        machine1.setEepromData43("43");
        machine1.setEepromData44("44");
        machine1.setEepromData45("45");
        machine1.setEepromData46("46");
        machine1.setEepromData47("47");
        machine1.setLcdBacklightSure("30");
        machines.add(machine1);

        // Dummy makine 2
        Machine machine2 = new Machine("MACH002", "CSP-D", ownerID, "Recep Baskurt", "01.02.2024", "20.01.2024");
        machine2.setWifiSSID("OnderGrup_WiFi_2");
        machine2.setWifiPass("87654321");
        machine2.setDevirmeYuruyusSecim("0");
        machine2.setCalismaSekli("Hızlı");
        machine2.setEmniyetCercevesi("Kapalı");
        machine2.setYavaslamaLimit("120");
        machine2.setAltLimit("30");
        machine2.setKapiTablaAcKonum("Kapalı");
        machine2.setBasincSalteri("Pasif");
        machine2.setKapiSecimleri("Manuel");
        machine2.setKapiAcTipi("Pnömatik");
        machine2.setKapi1Tip("Çift Kapı");
        machine2.setKapi1AcSure("4");
        machine2.setKapi2Tip("Tek Kapı");
        machine2.setKapi2AcSure("3");
        machine2.setKapitablaTip("Pnömatik");
        machine2.setKapiTablaAcSure("5");
        machine2.setYukariYavasLimit("100");
        machine2.setDevirmeYukariIleriLimit("95");
        machine2.setDevirmeAsagiGeriLimit("5");
        machine2.setDevirmeSilindirTipi("Tek Etkili");
        machine2.setPlatformSilindirTipi("Çift Etkili");
        machine2.setYukariValfTmr("3");
        machine2.setAsagiValfTmr("3");
        machine2.setDevirmeYukariIleriTmr("4");
        machine2.setDevirmeAsagiGeriTmr("4");
        machine2.setMakineCalismaTmr("15");
        machine2.setBuzzer("Pasif");
        machine2.setDemoMode("1");
        machine2.setCalismaSayisi("0");
        machine2.setCalismaSayisiDemo("500");
        machine2.setDilSecim("0");
        machine2.setEepromData38("138");
        machine2.setEepromData39("139");
        machine2.setEepromData40("140");
        machine2.setEepromData41("141");
        machine2.setEepromData42("142");
        machine2.setEepromData43("143");
        machine2.setEepromData44("144");
        machine2.setEepromData45("145");
        machine2.setEepromData46("146");
        machine2.setEepromData47("147");
        machine2.setLcdBacklightSure("60");
        machines.add(machine2);

        if (onSuccess != null) {
            onSuccess.run();
        }
    }

    public static void getMaintenances(Context context, String machineID, ArrayList<Maintenance> machineMaintenancesTemp, Runnable onSuccess, Runnable onFailure) {
        // Gerçek API isteği yorum satırına alındı, dummy data ile dolduruluyor
        /*
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
        */

        // Dummy data
        machineMaintenancesTemp.clear();
        
        // Dummy bakım 1
        Maintenance maintenance1 = new Maintenance("MAINT001", machineID, "TECH001", "Ahmet Tekniker", "15.01.2024");
        maintenance1.setKontrol11("Tamam");
        maintenance1.setKontrol12("Tamam");
        maintenance1.setKontrol13("Tamam");
        maintenance1.setKontrol14("Tamam");
        maintenance1.setKontrol21("Tamam");
        maintenance1.setKontrol22("Tamam");
        maintenance1.setKontrol23("Tamam");
        maintenance1.setKontrol24("Tamam");
        maintenance1.setKontrol31("Tamam");
        maintenance1.setKontrol32("Tamam");
        maintenance1.setKontrol33("Tamam");
        maintenance1.setKontrol34("Tamam");
        maintenance1.setKontrol35("Tamam");
        maintenance1.setKontrol36("Tamam");
        maintenance1.setKontrol41("Tamam");
        maintenance1.setKontrol42("Tamam");
        maintenance1.setKontrol43("Tamam");
        maintenance1.setKontrol44("Tamam");
        maintenance1.setKontrol45("Tamam");
        maintenance1.setKontrol46("Tamam");
        maintenance1.setKontrol51("Tamam");
        maintenance1.setKontrol52("Tamam");
        maintenance1.setKontrol53("Tamam");
        maintenance1.setKontrol54("Tamam");
        maintenance1.setKontrol55("Tamam");
        maintenance1.setKontrol56("Tamam");
        maintenance1.setKontrol61("Tamam");
        maintenance1.setKontrol62("Tamam");
        maintenance1.setKontrol63("Tamam");
        maintenance1.setKontrol71("Tamam");
        maintenance1.setKontrol72("Tamam");
        maintenance1.setKontrol81("Tamam");
        maintenance1.setKontrol82("Tamam");
        maintenance1.setKontrol83("Tamam");
        maintenance1.setKontrol91("Tamam");
        maintenance1.setKontrol92("Tamam");
        maintenance1.setKontrol93("Tamam");
        maintenance1.setKontrol94("Tamam");
        maintenance1.setKontrol95("Tamam");
        maintenance1.setKontrol96("Tamam");
        maintenance1.setKontrol97("Tamam");
        maintenance1.setKontrol98("Tamam");
        maintenance1.setKontrol99("Tamam");
        maintenance1.setKontrol910("Tamam");
        machineMaintenancesTemp.add(maintenance1);
        
        // Dummy bakım 2 - Tamamlanmamış
        Maintenance maintenance2 = new Maintenance("MAINT002", machineID, "TECH002", "Mehmet Tekniker", "14.01.2024");
        maintenance2.setKontrol11("Tamam");
        maintenance2.setKontrol12("Tamam");
        maintenance2.setKontrol13("Tamam");
        maintenance2.setKontrol14("Tamam");
        maintenance2.setKontrol21("Tamam");
        maintenance2.setKontrol22("Tamam");
        maintenance2.setKontrol23("Tamam");
        maintenance2.setKontrol24("Tamam");
        maintenance2.setKontrol31("Tamam");
        maintenance2.setKontrol32("Tamam");
        maintenance2.setKontrol33("Tamam");
        maintenance2.setKontrol34("Tamam");
        maintenance2.setKontrol35("Tamam");
        maintenance2.setKontrol36("Tamam");
        maintenance2.setKontrol41("Tamam");
        maintenance2.setKontrol42("Tamam");
        maintenance2.setKontrol43("Tamam");
        maintenance2.setKontrol44("Tamam");
        maintenance2.setKontrol45("Tamam");
        maintenance2.setKontrol46("Tamam");
        maintenance2.setKontrol51("Tamam");
        maintenance2.setKontrol52("Tamam");
        maintenance2.setKontrol53("Tamam");
        maintenance2.setKontrol54("Tamam");
        maintenance2.setKontrol55("Tamam");
        maintenance2.setKontrol56("Tamam");
        maintenance2.setKontrol61("Tamam");
        maintenance2.setKontrol62("Tamam");
        maintenance2.setKontrol63("Tamam");
        maintenance2.setKontrol71("Tamam");
        maintenance2.setKontrol72("Tamam");
        maintenance2.setKontrol81("Tamam");
        maintenance2.setKontrol82("Tamam");
        maintenance2.setKontrol83("Tamam");
        maintenance2.setKontrol91("Tamam");
        maintenance2.setKontrol92("Tamam");
        maintenance2.setKontrol93("Tamam");
        maintenance2.setKontrol94("Tamam");
        maintenance2.setKontrol95("Tamam");
        maintenance2.setKontrol96("Beklemede");
        maintenance2.setKontrol97("Beklemede");
        maintenance2.setKontrol98("Beklemede");
        maintenance2.setKontrol99("Beklemede");
        maintenance2.setKontrol910("Beklemede");
        machineMaintenancesTemp.add(maintenance2);

        if (onSuccess != null) {
            onSuccess.run();
        }
    }

    public static void getMaintenancesAll(Context context, String userID, ArrayList<Maintenance> machineMaintenancesTemp, Runnable onSuccess, Runnable onFailure) {
        // Gerçek API isteği yorum satırına alındı, dummy data ile dolduruluyor
        /*
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
        */

        // Dummy data
        machineMaintenancesTemp.clear();
        
        // Dummy bakım 1 - MACH001
        Maintenance maintenance1 = new Maintenance("MAINT001", "MACH001", "TECH001", "Ahmet Tekniker", "15.01.2024");
        maintenance1.setKontrol11("Tamam");
        maintenance1.setKontrol12("Tamam");
        maintenance1.setKontrol13("Tamam");
        maintenance1.setKontrol14("Tamam");
        maintenance1.setKontrol21("Tamam");
        maintenance1.setKontrol22("Tamam");
        maintenance1.setKontrol23("Tamam");
        maintenance1.setKontrol24("Tamam");
        maintenance1.setKontrol31("Tamam");
        maintenance1.setKontrol32("Tamam");
        maintenance1.setKontrol33("Tamam");
        maintenance1.setKontrol34("Tamam");
        maintenance1.setKontrol35("Tamam");
        maintenance1.setKontrol36("Tamam");
        maintenance1.setKontrol41("Tamam");
        maintenance1.setKontrol42("Tamam");
        maintenance1.setKontrol43("Tamam");
        maintenance1.setKontrol44("Tamam");
        maintenance1.setKontrol45("Tamam");
        maintenance1.setKontrol46("Tamam");
        maintenance1.setKontrol51("Tamam");
        maintenance1.setKontrol52("Tamam");
        maintenance1.setKontrol53("Tamam");
        maintenance1.setKontrol54("Tamam");
        maintenance1.setKontrol55("Tamam");
        maintenance1.setKontrol56("Tamam");
        maintenance1.setKontrol61("Tamam");
        maintenance1.setKontrol62("Tamam");
        maintenance1.setKontrol63("Tamam");
        maintenance1.setKontrol71("Tamam");
        maintenance1.setKontrol72("Tamam");
        maintenance1.setKontrol81("Tamam");
        maintenance1.setKontrol82("Tamam");
        maintenance1.setKontrol83("Tamam");
        maintenance1.setKontrol91("Tamam");
        maintenance1.setKontrol92("Tamam");
        maintenance1.setKontrol93("Tamam");
        maintenance1.setKontrol94("Tamam");
        maintenance1.setKontrol95("Tamam");
        maintenance1.setKontrol96("Tamam");
        maintenance1.setKontrol97("Tamam");
        maintenance1.setKontrol98("Tamam");
        maintenance1.setKontrol99("Tamam");
        maintenance1.setKontrol910("Tamam");
        machineMaintenancesTemp.add(maintenance1);
        
        // Dummy bakım 2 - MACH002
        Maintenance maintenance2 = new Maintenance("MAINT002", "MACH002", "TECH002", "Mehmet Tekniker", "10.01.2024");
        maintenance2.setKontrol11("Tamam");
        maintenance2.setKontrol12("Tamam");
        maintenance2.setKontrol13("Tamam");
        maintenance2.setKontrol14("Tamam");
        maintenance2.setKontrol21("Tamam");
        maintenance2.setKontrol22("Tamam");
        maintenance2.setKontrol23("Tamam");
        maintenance2.setKontrol24("Tamam");
        maintenance2.setKontrol31("Tamam");
        maintenance2.setKontrol32("Tamam");
        maintenance2.setKontrol33("Tamam");
        maintenance2.setKontrol34("Tamam");
        maintenance2.setKontrol35("Tamam");
        maintenance2.setKontrol36("Tamam");
        maintenance2.setKontrol41("Tamam");
        maintenance2.setKontrol42("Tamam");
        maintenance2.setKontrol43("Tamam");
        maintenance2.setKontrol44("Tamam");
        maintenance2.setKontrol45("Tamam");
        maintenance2.setKontrol46("Tamam");
        maintenance2.setKontrol51("Tamam");
        maintenance2.setKontrol52("Tamam");
        maintenance2.setKontrol53("Tamam");
        maintenance2.setKontrol54("Tamam");
        maintenance2.setKontrol55("Tamam");
        maintenance2.setKontrol56("Tamam");
        maintenance2.setKontrol61("Tamam");
        maintenance2.setKontrol62("Tamam");
        maintenance2.setKontrol63("Tamam");
        maintenance2.setKontrol71("Tamam");
        maintenance2.setKontrol72("Tamam");
        maintenance2.setKontrol81("Tamam");
        maintenance2.setKontrol82("Tamam");
        maintenance2.setKontrol83("Tamam");
        maintenance2.setKontrol91("Tamam");
        maintenance2.setKontrol92("Tamam");
        maintenance2.setKontrol93("Tamam");
        maintenance2.setKontrol94("Tamam");
        maintenance2.setKontrol95("Tamam");
        maintenance2.setKontrol96("Tamam");
        maintenance2.setKontrol97("Tamam");
        maintenance2.setKontrol98("Tamam");
        maintenance2.setKontrol99("Tamam");
        maintenance2.setKontrol910("Tamam");
        machineMaintenancesTemp.add(maintenance2);
        
        // Dummy bakım 3 - MACH001
        Maintenance maintenance3 = new Maintenance("MAINT003", "MACH001", "TECH003", "Ali Tekniker", "05.01.2024");
        maintenance3.setKontrol11("Tamam");
        maintenance3.setKontrol12("Tamam");
        maintenance3.setKontrol13("Tamam");
        maintenance3.setKontrol14("Tamam");
        maintenance3.setKontrol21("Tamam");
        maintenance3.setKontrol22("Tamam");
        maintenance3.setKontrol23("Tamam");
        maintenance3.setKontrol24("Tamam");
        maintenance3.setKontrol31("Tamam");
        maintenance3.setKontrol32("Tamam");
        maintenance3.setKontrol33("Tamam");
        maintenance3.setKontrol34("Tamam");
        maintenance3.setKontrol35("Tamam");
        maintenance3.setKontrol36("Tamam");
        maintenance3.setKontrol41("Tamam");
        maintenance3.setKontrol42("Tamam");
        maintenance3.setKontrol43("Tamam");
        maintenance3.setKontrol44("Tamam");
        maintenance3.setKontrol45("Tamam");
        maintenance3.setKontrol46("Tamam");
        maintenance3.setKontrol51("Tamam");
        maintenance3.setKontrol52("Tamam");
        maintenance3.setKontrol53("Tamam");
        maintenance3.setKontrol54("Tamam");
        maintenance3.setKontrol55("Tamam");
        maintenance3.setKontrol56("Tamam");
        maintenance3.setKontrol61("Tamam");
        maintenance3.setKontrol62("Tamam");
        maintenance3.setKontrol63("Tamam");
        maintenance3.setKontrol71("Tamam");
        maintenance3.setKontrol72("Tamam");
        maintenance3.setKontrol81("Tamam");
        maintenance3.setKontrol82("Tamam");
        maintenance3.setKontrol83("Tamam");
        maintenance3.setKontrol91("Tamam");
        maintenance3.setKontrol92("Tamam");
        maintenance3.setKontrol93("Tamam");
        maintenance3.setKontrol94("Tamam");
        maintenance3.setKontrol95("Tamam");
        maintenance3.setKontrol96("Tamam");
        maintenance3.setKontrol97("Tamam");
        maintenance3.setKontrol98("Tamam");
        maintenance3.setKontrol99("Tamam");
        maintenance3.setKontrol910("Tamam");
        machineMaintenancesTemp.add(maintenance3);
        
        // Dummy bakım 4 - MACH002
        Maintenance maintenance4 = new Maintenance("MAINT004", "MACH002", "TECH004", "Veli Tekniker", "03.01.2024");
        maintenance4.setKontrol11("Tamam");
        maintenance4.setKontrol12("Tamam");
        maintenance4.setKontrol13("Tamam");
        maintenance4.setKontrol14("Tamam");
        maintenance4.setKontrol21("Tamam");
        maintenance4.setKontrol22("Tamam");
        maintenance4.setKontrol23("Tamam");
        maintenance4.setKontrol24("Tamam");
        maintenance4.setKontrol31("Tamam");
        maintenance4.setKontrol32("Tamam");
        maintenance4.setKontrol33("Tamam");
        maintenance4.setKontrol34("Tamam");
        maintenance4.setKontrol35("Tamam");
        maintenance4.setKontrol36("Tamam");
        maintenance4.setKontrol41("Tamam");
        maintenance4.setKontrol42("Tamam");
        maintenance4.setKontrol43("Tamam");
        maintenance4.setKontrol44("Tamam");
        maintenance4.setKontrol45("Tamam");
        maintenance4.setKontrol46("Tamam");
        maintenance4.setKontrol51("Tamam");
        maintenance4.setKontrol52("Tamam");
        maintenance4.setKontrol53("Tamam");
        maintenance4.setKontrol54("Tamam");
        maintenance4.setKontrol55("Tamam");
        maintenance4.setKontrol56("Tamam");
        maintenance4.setKontrol61("Tamam");
        maintenance4.setKontrol62("Tamam");
        maintenance4.setKontrol63("Tamam");
        maintenance4.setKontrol71("Tamam");
        maintenance4.setKontrol72("Tamam");
        maintenance4.setKontrol81("Tamam");
        maintenance4.setKontrol82("Tamam");
        maintenance4.setKontrol83("Tamam");
        maintenance4.setKontrol91("Tamam");
        maintenance4.setKontrol92("Tamam");
        maintenance4.setKontrol93("Tamam");
        maintenance4.setKontrol94("Tamam");
        maintenance4.setKontrol95("Tamam");
        maintenance4.setKontrol96("Tamam");
        maintenance4.setKontrol97("Tamam");
        maintenance4.setKontrol98("Tamam");
        maintenance4.setKontrol99("Tamam");
        maintenance4.setKontrol910("Tamam");
        machineMaintenancesTemp.add(maintenance4);
        
        // Dummy bakım 5 - MACH003
        Maintenance maintenance5 = new Maintenance("MAINT005", "MACH003", "TECH005", "Can Tekniker", "01.01.2024");
        maintenance5.setKontrol11("Tamam");
        maintenance5.setKontrol12("Tamam");
        maintenance5.setKontrol13("Tamam");
        maintenance5.setKontrol14("Tamam");
        maintenance5.setKontrol21("Tamam");
        maintenance5.setKontrol22("Tamam");
        maintenance5.setKontrol23("Tamam");
        maintenance5.setKontrol24("Tamam");
        maintenance5.setKontrol31("Tamam");
        maintenance5.setKontrol32("Tamam");
        maintenance5.setKontrol33("Tamam");
        maintenance5.setKontrol34("Tamam");
        maintenance5.setKontrol35("Tamam");
        maintenance5.setKontrol36("Tamam");
        maintenance5.setKontrol41("Tamam");
        maintenance5.setKontrol42("Tamam");
        maintenance5.setKontrol43("Tamam");
        maintenance5.setKontrol44("Tamam");
        maintenance5.setKontrol45("Tamam");
        maintenance5.setKontrol46("Tamam");
        maintenance5.setKontrol51("Tamam");
        maintenance5.setKontrol52("Tamam");
        maintenance5.setKontrol53("Tamam");
        maintenance5.setKontrol54("Tamam");
        maintenance5.setKontrol55("Tamam");
        maintenance5.setKontrol56("Tamam");
        maintenance5.setKontrol61("Tamam");
        maintenance5.setKontrol62("Tamam");
        maintenance5.setKontrol63("Tamam");
        maintenance5.setKontrol71("Tamam");
        maintenance5.setKontrol72("Tamam");
        maintenance5.setKontrol81("Tamam");
        maintenance5.setKontrol82("Tamam");
        maintenance5.setKontrol83("Tamam");
        maintenance5.setKontrol91("Tamam");
        maintenance5.setKontrol92("Tamam");
        maintenance5.setKontrol93("Tamam");
        maintenance5.setKontrol94("Tamam");
        maintenance5.setKontrol95("Tamam");
        maintenance5.setKontrol96("Tamam");
        maintenance5.setKontrol97("Tamam");
        maintenance5.setKontrol98("Tamam");
        maintenance5.setKontrol99("Tamam");
        maintenance5.setKontrol910("Tamam");
        machineMaintenancesTemp.add(maintenance5);
        
        // Dummy bakım 6 - MACH001 (Eski bakım)
        Maintenance maintenance6 = new Maintenance("MAINT006", "MACH001", "TECH006", "Deniz Tekniker", "28.12.2023");
        maintenance6.setKontrol11("Tamam");
        maintenance6.setKontrol12("Tamam");
        maintenance6.setKontrol13("Tamam");
        maintenance6.setKontrol14("Tamam");
        maintenance6.setKontrol21("Tamam");
        maintenance6.setKontrol22("Tamam");
        maintenance6.setKontrol23("Tamam");
        maintenance6.setKontrol24("Tamam");
        maintenance6.setKontrol31("Tamam");
        maintenance6.setKontrol32("Tamam");
        maintenance6.setKontrol33("Tamam");
        maintenance6.setKontrol34("Tamam");
        maintenance6.setKontrol35("Tamam");
        maintenance6.setKontrol36("Tamam");
        maintenance6.setKontrol41("Tamam");
        maintenance6.setKontrol42("Tamam");
        maintenance6.setKontrol43("Tamam");
        maintenance6.setKontrol44("Tamam");
        maintenance6.setKontrol45("Tamam");
        maintenance6.setKontrol46("Tamam");
        maintenance6.setKontrol51("Tamam");
        maintenance6.setKontrol52("Tamam");
        maintenance6.setKontrol53("Tamam");
        maintenance6.setKontrol54("Tamam");
        maintenance6.setKontrol55("Tamam");
        maintenance6.setKontrol56("Tamam");
        maintenance6.setKontrol61("Tamam");
        maintenance6.setKontrol62("Tamam");
        maintenance6.setKontrol63("Tamam");
        maintenance6.setKontrol71("Tamam");
        maintenance6.setKontrol72("Tamam");
        maintenance6.setKontrol81("Tamam");
        maintenance6.setKontrol82("Tamam");
        maintenance6.setKontrol83("Tamam");
        maintenance6.setKontrol91("Tamam");
        maintenance6.setKontrol92("Tamam");
        maintenance6.setKontrol93("Tamam");
        maintenance6.setKontrol94("Tamam");
        maintenance6.setKontrol95("Tamam");
        maintenance6.setKontrol96("Tamam");
        maintenance6.setKontrol97("Tamam");
        maintenance6.setKontrol98("Tamam");
        maintenance6.setKontrol99("Tamam");
        maintenance6.setKontrol910("Tamam");
        machineMaintenancesTemp.add(maintenance6);

        if (onSuccess != null) {
            onSuccess.run();
        }
    }
}