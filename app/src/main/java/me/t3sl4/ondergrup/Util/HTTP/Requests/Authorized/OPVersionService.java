package me.t3sl4.ondergrup.Util.HTTP.Requests.Authorized;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import me.t3sl4.ondergrup.Model.MachineVersion.Version;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.HTTP.HttpHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OPVersionService {
    private static final String GET_VERSIONS_URL = "/api/v2/authorized/getAllVersions";

    // getAllVersions method
    public static void getAllVersions(Context context, ArrayList<Version> versions, Runnable onSuccess, Runnable onFailure) {
        String authToken = UserDataService.getAccessToken(context);

        Call<ResponseBody> call = HttpHelper.makeRequestWithAuth("GET", GET_VERSIONS_URL, null, null, authToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("GetAllVersions", "Success: " + response.body().string());

                        // JSON verisini Versions listesine parse et
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray versionsArray = jsonResponse.getJSONObject("payload").getJSONArray("versions");

                        for (int i = 0; i < versionsArray.length(); i++) {
                            JSONObject versionObject = versionsArray.getJSONObject(i);

                            String versionTitle = versionObject.getString("versionTitle");
                            String versionDesc = versionObject.getString("versionDesc");
                            String versionCode = versionObject.getString("versionCode");
                            String versionID = versionObject.getString("versionID");
                            String releaseDate = versionObject.getString("releaseDate");

                            Version version = new Version(versionTitle, versionDesc, versionCode, versionID, releaseDate);
                            versions.add(version);
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
                        Log.e("GetAllVersions", "Failure: " + response.errorBody().string());
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

}
