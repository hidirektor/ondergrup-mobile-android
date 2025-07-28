package me.t3sl4.ondergrup.Util.HTTP.Requests.Authorized;

import android.content.Context;
import android.util.Log;

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

        // Gerçek API isteği yorum satırına alındı, dummy data ile dolduruluyor
        /*
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
        */

        // Dummy data
        machines.clear();
        
        // Dummy makine 1
        Machine machine1 = new Machine("MACH001", "ESP", "USER001", "Recep Baskurt", "01.01.2024", "15.01.2024");
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
        Machine machine2 = new Machine("MACH002", "CSP-D", "USER001", "Recep Baskurt", "01.02.2024", "20.01.2024");
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

        // Dummy makine 3
        Machine machine3 = new Machine("MACH003", "ESP", "USER002", "Ahmet Yılmaz", "01.03.2024", "25.01.2024");
        machine3.setWifiSSID("OnderGrup_WiFi_3");
        machine3.setWifiPass("11111111");
        machine3.setDevirmeYuruyusSecim("1");
        machine3.setCalismaSekli("Yavaş");
        machine3.setEmniyetCercevesi("Açık");
        machine3.setYavaslamaLimit("80");
        machine3.setAltLimit("40");
        machine3.setKapiTablaAcKonum("Açık");
        machine3.setBasincSalteri("Aktif");
        machine3.setKapiSecimleri("Otomatik");
        machine3.setKapiAcTipi("Hidrolik");
        machine3.setKapi1Tip("Tek Kapı");
        machine3.setKapi1AcSure("2");
        machine3.setKapi2Tip("Tek Kapı");
        machine3.setKapi2AcSure("2");
        machine3.setKapitablaTip("Hidrolik");
        machine3.setKapiTablaAcSure("3");
        machine3.setYukariYavasLimit("70");
        machine3.setDevirmeYukariIleriLimit("85");
        machine3.setDevirmeAsagiGeriLimit("15");
        machine3.setDevirmeSilindirTipi("Çift Etkili");
        machine3.setPlatformSilindirTipi("Çift Etkili");
        machine3.setYukariValfTmr("1");
        machine3.setAsagiValfTmr("1");
        machine3.setDevirmeYukariIleriTmr("2");
        machine3.setDevirmeAsagiGeriTmr("2");
        machine3.setMakineCalismaTmr("8");
        machine3.setBuzzer("Aktif");
        machine3.setDemoMode("0");
        machine3.setCalismaSayisi("800");
        machine3.setCalismaSayisiDemo("0");
        machine3.setDilSecim("1");
        machine3.setEepromData38("238");
        machine3.setEepromData39("239");
        machine3.setEepromData40("240");
        machine3.setEepromData41("241");
        machine3.setEepromData42("242");
        machine3.setEepromData43("243");
        machine3.setEepromData44("244");
        machine3.setEepromData45("245");
        machine3.setEepromData46("246");
        machine3.setEepromData47("247");
        machine3.setLcdBacklightSure("45");
        machines.add(machine3);

        if (onSuccess != null) {
            onSuccess.run();
        }
    }

    public static void updateOwner(Context context, String userID, String machineID, String userName, Runnable onSuccess, Runnable onFailure) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("operationPlatform", "Android");
            jsonObject.put("sourceUserID", userID);
            jsonObject.put("affectedUserID", null);
            jsonObject.put("affectedUserName", null);
            jsonObject.put("affectedMachineID", machineID);
            jsonObject.put("affectedMaintenanceID", null);
            jsonObject.put("affectedHydraulicUnitID", null);
            jsonObject.put("userID", userID);
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