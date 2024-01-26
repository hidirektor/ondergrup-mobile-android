package me.t3sl4.ondergrup.Screens.SubUser;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Profile.EditSubScreen;
import me.t3sl4.ondergrup.Model.SubUser.SubUser;
import me.t3sl4.ondergrup.Model.SubUser.Adapter.SubUserAdapter;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class SubUserListScreen extends AppCompatActivity {
    public Util util;
    public User receivedUser;

    private ListView subUserListView;
    private SubUserAdapter subUserListAdapter;
    private ArrayList<SubUser> subUserList;

    private ImageView backButton;

    private Dialog uyariDiyalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subuser_list);

        util = new Util(getApplicationContext());
        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");
        uyariDiyalog = new Dialog(this);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        subUserListView = findViewById(R.id.subUserListView);
        subUserList = getSubUserList();

        subUserListAdapter = new SubUserAdapter(getApplicationContext(), subUserList);
        subUserListView.setAdapter(subUserListAdapter);

        subUserListView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            SubUser selectedSubUser = subUserList.get(position);

            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if(item.getItemId() == R.id.deleteSub) {
                    deleteAndUpdateSubUser(selectedSubUser, subUserList);
                    return true;
                } else if(item.getItemId() == R.id.editSub) {
                    Intent editSubIntent = new Intent(SubUserListScreen.this, EditSubScreen.class);
                    editSubIntent.putExtra("user", receivedUser);
                    editSubIntent.putExtra("subuser", selectedSubUser);
                    startActivity(editSubIntent);
                    finish();
                    return true;
                } else {
                    return false;
                }
            });

            popupMenu.show();

            return true;
        });
    }

    private ArrayList<SubUser> getSubUserList() {
        ArrayList<SubUser> subUsers = new ArrayList<>();
        String reqURL = util.BASE_URL + util.getSubUsersPrefix;
        String jsonSubUserBody = "{\"username\": \"" + receivedUser.getUserName() + "\"}";

        HTTP.sendRequest(reqURL, jsonSubUserBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray subUserArray = response.getJSONArray("subUsers");
                    for (int i = 0; i < subUserArray.length(); i++) {
                        JSONObject subUserObj = subUserArray.getJSONObject(i);
                        String role = subUserObj.getString("Role");
                        String userName = subUserObj.getString("UserName");
                        String email = subUserObj.getString("Email");
                        String nameSurname = subUserObj.getString("NameSurname");
                        String phone = subUserObj.getString("Phone");
                        String profilePhoto = subUserObj.getString("Profile_Photo");
                        String companyName = subUserObj.getString("CompanyName");
                        String owner = subUserObj.getString("Owner");

                        SubUser subUser = new SubUser(role, userName, email, nameSurname, phone, companyName, owner);
                        subUsers.add(subUser);
                    }

                    updateListView(subUsers);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                util.showErrorPopup(uyariDiyalog, "Herhangi bir alt kullanıcı bulunamadı.");
            }
        }, Volley.newRequestQueue(this));
        return subUsers;
    }

    private void updateListView(ArrayList<SubUser> subUsers) {
        subUserList = subUsers;
        subUserListAdapter = new SubUserAdapter(getApplicationContext(), subUserList);
        subUserListView.setAdapter(subUserListAdapter);
    }

    private void deleteAndUpdateSubUser(SubUser selectedSubUser, ArrayList<SubUser> subUsers) {
        String reqURL = util.BASE_URL + util.deleteSubUserPrefix;

        String selectedSubUsername = selectedSubUser.getUserName();
        String ownerName = receivedUser.getUserName();
        String jsonSubUserBody = "{\"username\": \"" + selectedSubUsername + "\", \"OwnerName\": \"" + ownerName + "\"}";

        HTTP.sendRequest(reqURL, jsonSubUserBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                util.showSuccessPopup(uyariDiyalog, "Alt kullanıcı başarılı bir şekilde silindi !");
            }

            @Override
            public void onFailure(String errorMessage) {
                util.showErrorPopup(uyariDiyalog, "Kullanıcı silinmeedi. \nLütfen birazdan tekrar deneyin.");
            }
        }, Volley.newRequestQueue(this));

        subUsers.remove(selectedSubUser);
        updateListView(subUsers);
    }
}
