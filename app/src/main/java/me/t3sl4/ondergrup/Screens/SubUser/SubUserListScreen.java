package me.t3sl4.ondergrup.Screens.SubUser;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.SubUser.Adapter.SubUser;
import me.t3sl4.ondergrup.Screens.SubUser.Adapter.SubUserAdapter;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Util.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class SubUserListScreen extends AppCompatActivity {
    public Util util;
    public User receivedUser;

    private ListView subUserListView;
    private SubUserAdapter subUserListAdapter;
    private ArrayList<SubUser> subUserList;

    private Dialog uyariDiyalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subuser_list);

        util = new Util(getApplicationContext());
        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");
        uyariDiyalog = new Dialog(this);

        subUserListView = findViewById(R.id.subUserListView);
        subUserList = getSubUserList();

        subUserListAdapter = new SubUserAdapter(getApplicationContext(), subUserList);
        subUserListView.setAdapter(subUserListAdapter);

        subUserListView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            SubUser selectedSubUser = subUserList.get(position);

            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.menu_option1:
                        deleteAndUpdateSubUser(selectedSubUser, subUserList);
                        return true;
                    case R.id.menu_option2:
                        //TODO
                        //Alt kullanıcı düzenleme işlemi
                        return true;
                    default:
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

        HTTP http = new HTTP(this);
        http.sendRequest(reqURL, jsonSubUserBody, new HTTP.HttpRequestCallback() {
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
        });
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

        HTTP http = new HTTP(this);
        http.sendRequest(reqURL, jsonSubUserBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                util.showSuccessPopup(uyariDiyalog, "Alt kullanıcı başarılı bir şekilde silindi !");
            }

            @Override
            public void onFailure(String errorMessage) {
                util.showErrorPopup(uyariDiyalog, "Kullanıcı silinmeedi. \nLütfen birazdan tekrar deneyin.");
            }
        });

        subUsers.remove(selectedSubUser);
        updateListView(subUsers);
    }
}
