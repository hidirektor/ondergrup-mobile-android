package me.t3sl4.ondergrup.Util.HTTP.Requests.SupportTickets;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
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
    public static void createTicket(Context context, String ownerID, String title, String subject, String desc, String operationPlatform, String sourceUserID, Runnable onSuccess, Runnable onFailure) {
        String authToken = UserDataService.getAccessToken(context);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ownerID", ownerID);
            jsonObject.put("title", title);
            jsonObject.put("subject", subject);
            jsonObject.put("desc", desc);
            jsonObject.put("operationPlatform", operationPlatform);
            jsonObject.put("sourceUserID", sourceUserID);
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
    }
}