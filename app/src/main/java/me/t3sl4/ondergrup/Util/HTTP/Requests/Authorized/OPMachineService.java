package me.t3sl4.ondergrup.Util.HTTP.Requests.Authorized;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import me.t3sl4.ondergrup.Model.Machine.Machine;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.HTTP.HttpHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OPMachineService {
    private static final String GET_ALL_MACHINES_URL = "/api/v2/authorized/getAllMachines";
    private static final String UPDATE_OWNER_URL = "/api/v2/authorized/updateOwner";

    public static void getAllMachines(Context context, ArrayList<Machine> machines, Runnable onSuccess, Runnable onFailure) {

        String authToken = UserDataService.getAccessToken(context);

        Call<ResponseBody> call = HttpHelper.makeRequestWithAuth("GET", GET_ALL_MACHINES_URL, null, null, authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("GetAllMachines", "Success: " + response.body().string());

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
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        Log.e("GetAllMachines", "Failure: " + response.errorBody().string());
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
                Log.e("GetAllMachines", "Error: " + t.getMessage());
                //boş kısım revize edilecek
            }
        });
    }

    public static void updateOwner(Context context, String machineID, String userName, Runnable onSuccess, Runnable onFailure) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("machineID", machineID);
            jsonObject.put("userName", userName);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        String authToken = UserDataService.getAccessToken(context);
        Call<ResponseBody> call = HttpHelper.makeRequestWithAuth("PUT", UPDATE_OWNER_URL, null, jsonObject.toString(), authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("UpdateOwner", "Success: " + response.body().string());
                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.e("UpdateOwner", "Failure: " + response.errorBody().string());
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
                Log.e("UpdateOwner", "Error: " + t.getMessage());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        });
    }
}