package me.t3sl4.ondergrup.Screens.SubUser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

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

        initializeComponents();

        buttonClickListeners();
    }

    private void initializeComponents() {
        backButton = findViewById(R.id.backButton);

        subUserListView = findViewById(R.id.subUserListView);
        subUserList = getSubUserList();

        subUserListAdapter = new SubUserAdapter(getApplicationContext(), subUserList);
        subUserListView.setAdapter(subUserListAdapter);
    }

    private void buttonClickListeners() {
        backButton.setOnClickListener(v -> finish());

        subUserListView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            SubUser selectedSubUser = subUserList.get(position);

            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if(item.getItemId() == R.id.deActivateSub) {
                    deActivateSubUser(selectedSubUser);
                    return true;
                } else if(item.getItemId() == R.id.deActivateSub) {
                    activateSubUser(selectedSubUser);
                    return true;
                } else if(item.getItemId() == R.id.deleteSub) {
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
        }, () -> {
            String hataMesaj = this.getResources().getString(R.string.subuseryok);
            Util.showErrorPopup(uyariDiyalog, hataMesaj);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                uyariDiyalog.dismiss();
                finish();
            }, 1000);
        });

        return subUsers;
    }

    private void updateListView(ArrayList<SubUser> subUsers) {
        subUserList = subUsers;
        subUserListAdapter = new SubUserAdapter(getApplicationContext(), subUserList);
        subUserListView.setAdapter(subUserListAdapter);
    }

    private void deleteAndUpdateSubUser(SubUser selectedSubUser) {
        AtomicBoolean isConfirmed = new AtomicBoolean(false);
        new AlertDialog.Builder(this)
                .setTitle(this.getResources().getString(R.string.confirm_subuser_delete_header))
                .setMessage(this.getResources().getString(R.string.confirm_subuser_delete_desc))
                .setPositiveButton(this.getResources().getString(R.string.button_yes), (dialog, which) -> {
                    isConfirmed.set(true);
                })
                .setNegativeButton(this.getResources().getString(R.string.button_no), (dialog, which) -> {
                    isConfirmed.set(false);
                })
                .setOnDismissListener(dialog -> {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        if (isConfirmed.get()) {
                            SubUserService.deleteSubUser(this, selectedSubUser.getSubUserID(), () -> {
                                getSubUserList();

                                Util.showSuccessPopup(uyariDiyalog, this.getResources().getString(R.string.subuser_deleted));
                            });
                        }
                    }, 500);
                })
                .show();
    }

    private void deActivateSubUser(SubUser selectedSubUser) {
        SubUserService.deActivateSubUser(this, selectedSubUser.getSubUserID(), () -> {
            getSubUserList();
            Util.showSuccessPopup(uyariDiyalog, this.getResources().getString(R.string.subuser_deactivated));
        });
    }

    private void activateSubUser(SubUser selectedSubUser) {
        SubUserService.activateSubUser(this, selectedSubUser.getSubUserID(), () -> {
            getSubUserList();
            Util.showSuccessPopup(uyariDiyalog, this.getResources().getString(R.string.subuser_activated));
        });
    }
}
