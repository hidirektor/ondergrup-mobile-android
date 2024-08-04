package me.t3sl4.ondergrup.Screens.SubUser;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import me.t3sl4.ondergrup.Model.SubUser.Adapter.SubUserAdapter;
import me.t3sl4.ondergrup.Model.SubUser.SubUser;
import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Profile.EditSubScreen;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.HTTP.Requests.SubUser.SubUserService;
import me.t3sl4.ondergrup.Util.Util;

public class SubUserListScreen extends AppCompatActivity {
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
                    deleteAndUpdateSubUser(selectedSubUser);
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

        SubUserService.getSubUsers(this, UserDataService.getUserID(this), subUsers, () -> {
            updateListView(subUsers);
        });

        return subUsers;
    }

    private void updateListView(ArrayList<SubUser> subUsers) {
        subUserList = subUsers;
        subUserListAdapter = new SubUserAdapter(getApplicationContext(), subUserList);
        subUserListView.setAdapter(subUserListAdapter);
    }

    private void deleteAndUpdateSubUser(SubUser selectedSubUser) {
        SubUserService.deleteSubUser(this, selectedSubUser.getSubUserID(), () -> {
            getSubUserList();
            Util.showSuccessPopup(uyariDiyalog, "Alt kullanıcı başarılı bir şekilde silindi !");
        });
    }
}
