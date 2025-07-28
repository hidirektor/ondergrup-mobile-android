package me.t3sl4.ondergrup.Util.HTTP.Requests.SupportTickets;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import me.t3sl4.ondergrup.Model.SupportTicket.Ticket;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.HTTP.HttpHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketService {

    private static final String CREATE_TICKET_URL = "/api/v2/ticket/createTicket";
    private static final String RESPONSE_TICKET_URL = "/api/v2/ticket/responseTicket";
    private static final String GET_TICKETS_URL = "/api/v2/ticket/getTickets";

    // createTicket method
    public static void createTicket(Context context, String ownerID, String title, String subject, String desc, String operationPlatform, Runnable onSuccess, Runnable onFailure) {
        String authToken = UserDataService.getAccessToken(context);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ownerID", ownerID);
            jsonObject.put("title", title);
            jsonObject.put("subject", subject);
            jsonObject.put("desc", desc);
            jsonObject.put("operationPlatform", operationPlatform);
            jsonObject.put("sourceUserID", ownerID);
        } catch (JSONException e) {
            e.printStackTrace();
            if (onFailure != null) {
                onFailure.run();
            }
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequestWithAuth("POST", CREATE_TICKET_URL, null, jsonObject.toString(), authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("CreateTicket", "Success: " + response.body().string());
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
                        Log.e("CreateTicket", "Failure: " + response.errorBody().string());
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
                Log.e("CreateTicket", "Error: " + t.getMessage());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        });
    }

    // responseTicket method
    public static void responseTicket(Context context, String ticketID, String userID, String comment, Runnable onSuccess, Runnable onFailure) {
        String authToken = UserDataService.getAccessToken(context);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", ticketID);
            jsonObject.put("userID", userID);
            jsonObject.put("comment", comment);
        } catch (JSONException e) {
            e.printStackTrace();
            if (onFailure != null) {
                onFailure.run();
            }
            return;
        }

        Log.d("BODY: ", jsonObject.toString());

        Call<ResponseBody> call = HttpHelper.makeRequestWithAuth("POST", RESPONSE_TICKET_URL, null, jsonObject.toString(), authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("ResponseTicket", "Success: " + response.body().string());
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
                        Log.e("ResponseTicket", "Failure: " + response.errorBody().string());
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
                Log.e("ResponseTicket", "Error: " + t.getMessage());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        });
    }

    // getTickets method
    public static void getTickets(Context context, String userID, List<Ticket> ticketList, Runnable onSuccess, Runnable onFailure) {
        // Gerçek API isteği yorum satırına alındı, dummy data ile dolduruluyor
        /*
        String authToken = UserDataService.getAccessToken(context);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userID", userID);
        } catch (JSONException e) {
            e.printStackTrace();
            if (onFailure != null) {
                onFailure.run();
            }
            return;
        }

        Call<ResponseBody> call = HttpHelper.makeRequestWithAuth("POST", GET_TICKETS_URL, null, jsonObject.toString(), authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("GetTickets", "Success: " + responseBody);

                        // JSON verisini ticketList'e parse et
                        JSONArray ticketsArray = new JSONArray(responseBody);
                        for (int i = 0; i < ticketsArray.length(); i++) {
                            JSONObject ticketObject = ticketsArray.getJSONObject(i);
                            Ticket ticket = new Ticket(
                                    ticketObject.getInt("id"),
                                    ticketObject.getString("ownerID"),
                                    ticketObject.getString("title"),
                                    ticketObject.getString("subject"),
                                    ticketObject.getString("ticketStatus"),
                                    ticketObject.getString("responses"),
                                    ticketObject.getLong("createdDate")
                            );
                            ticketList.add(ticket);
                        }

                        if (onSuccess != null) {
                            onSuccess.run();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        if (onFailure != null) {
                            onFailure.run();
                        }
                    }
                } else {
                    try {
                        Log.e("GetTickets", "Failure: " + response.errorBody().string());
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
                Log.e("GetTickets", "Error: " + t.getMessage());
                if (onFailure != null) {
                    onFailure.run();
                }
            }
        });
        */

        // Dummy data
        ticketList.clear();
        
        // Dummy ticket 1
        Ticket ticket1 = new Ticket(1, userID, "Makine Arızası", "ESP makinesi çalışmıyor", "Açık", "2", 1704067200000L); // 01.01.2024
        ticketList.add(ticket1);
        
        // Dummy ticket 2
        Ticket ticket2 = new Ticket(2, userID, "Teknik Destek", "CSP-D makinesi ayarları", "Kapalı", "5", 1704153600000L); // 02.01.2024
        ticketList.add(ticket2);
        
        // Dummy ticket 3
        Ticket ticket3 = new Ticket(3, userID, "Yazılım Sorunu", "Uygulama açılmıyor", "Açık", "1", 1704240000000L); // 03.01.2024
        ticketList.add(ticket3);
        
        // Dummy ticket 4
        Ticket ticket4 = new Ticket(4, userID, "Parça Talebi", "Hidrolik pompa değişimi", "Beklemede", "3", 1704326400000L); // 04.01.2024
        ticketList.add(ticket4);
        
        // Dummy ticket 5
        Ticket ticket5 = new Ticket(5, userID, "Yazılım Güncelleme", "Uygulama güncelleme talebi", "Açık", "1", 1704412800000L); // 05.01.2024
        ticketList.add(ticket5);
        
        // Dummy ticket 6
        Ticket ticket6 = new Ticket(6, userID, "Eğitim Talebi", "Yeni makine kullanım eğitimi", "Kapalı", "4", 1704499200000L); // 06.01.2024
        ticketList.add(ticket6);
        
        // Dummy ticket 7
        Ticket ticket7 = new Ticket(7, userID, "Kontrol Paneli Sorunu", "Kontrol paneli yanıt vermiyor", "Açık", "2", 1704585600000L); // 07.01.2024
        ticketList.add(ticket7);
        
        // Dummy ticket 8
        Ticket ticket8 = new Ticket(8, userID, "Ses Sistemi Arızası", "Buzzer sesi gelmiyor", "Beklemede", "1", 1704672000000L); // 08.01.2024
        ticketList.add(ticket8);
        
        // Dummy ticket 9
        Ticket ticket9 = new Ticket(9, userID, "Acil Servis", "Makine tamamen durdu", "Açık", "5", 1704758400000L); // 09.01.2024
        ticketList.add(ticket9);
        
        // Dummy ticket 10
        Ticket ticket10 = new Ticket(10, userID, "Kullanım Kılavuzu", "Yeni makine kullanım kılavuzu", "Kapalı", "2", 1704844800000L); // 10.01.2024
        ticketList.add(ticket10);
        
        // Dummy ticket 11
        Ticket ticket11 = new Ticket(11, userID, "Güvenlik Sorunu", "Emniyet sistemi çalışmıyor", "Açık", "3", 1704931200000L); // 11.01.2024
        ticketList.add(ticket11);
        
        // Dummy ticket 12
        Ticket ticket12 = new Ticket(12, userID, "Performans Sorunu", "Makine yavaş çalışıyor", "Beklemede", "2", 1705017600000L); // 12.01.2024
        ticketList.add(ticket12);

        if (onSuccess != null) {
            onSuccess.run();
        }
    }
}