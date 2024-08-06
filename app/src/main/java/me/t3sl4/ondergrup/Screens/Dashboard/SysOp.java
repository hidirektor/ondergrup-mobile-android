package me.t3sl4.ondergrup.Screens.Dashboard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import me.t3sl4.ondergrup.Model.Machine.Adapter.MachineAdapter;
import me.t3sl4.ondergrup.Model.Machine.Machine;
import me.t3sl4.ondergrup.Model.User.Adapter.UserAdapter;
import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Machine.RestrictedMachineScreen;
import me.t3sl4.ondergrup.Screens.Profile.EditProfileScreen;
import me.t3sl4.ondergrup.Screens.Profile.ProfileScreen;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.Component.Button.ButtonManager;
import me.t3sl4.ondergrup.Util.HTTP.Requests.Authorized.OPMachineService;
import me.t3sl4.ondergrup.Util.HTTP.Requests.Authorized.OPUserService;
import me.t3sl4.ondergrup.Util.HTTP.Requests.User.UserService;
import me.t3sl4.ondergrup.Util.Util;

public class SysOp extends AppCompatActivity {

    public me.t3sl4.ondergrup.Model.User.User receivedUser;
    private Dialog uyariDiyalog;

    private TextView isimSoyisim;
    private TextView allText;

    private ImageView subLanguage;
    private ConstraintLayout profileButton;
    private ConstraintLayout settingsButton;

    private ImageView logoutButton;

    private ListView machineListView;
    private MachineAdapter machineListAdapter;
    private ArrayList<Machine> machineList;

    private ArrayList<me.t3sl4.ondergrup.Model.User.User> userList;
    private ListView userListView;
    private UserAdapter userListAdapter;

    private Button allMachinesButton;
    private Button allUsersButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sysop);

        uyariDiyalog = new Dialog(this);

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        initializeComponents();

        buttonClickListeners();

        sectionPager();
        setUserInfo();
    }

    private void initializeComponents() {
        isimSoyisim = findViewById(R.id.loggedUserName);
        allText = findViewById(R.id.allText);

        subLanguage = findViewById(R.id.languageImageView);
        profileButton = findViewById(R.id.profileConstraint);
        settingsButton = findViewById(R.id.profileUpdateConstraint);

        //Top buttons:
        logoutButton = findViewById(R.id.logoutButton);

        allMachinesButton = findViewById(R.id.allMachinesButton);
        allUsersButton = findViewById(R.id.allUsersButton);

        //ListView Definitians:
        machineListView = findViewById(R.id.machineListView);
        userListView = findViewById(R.id.userListView);

        machineList = getMachineList();
        userList = getUserList();
    }

    private void buttonClickListeners() {
        userListView.setOnItemClickListener((parent, view, position, id) -> {
            User selectedUser = (User) parent.getItemAtPosition(position);

            PopupMenu popup = new PopupMenu(SysOp.this, view);
            popup.getMenuInflater().inflate(R.menu.user_operations_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                if(item.getItemId() == R.id.deActivate) {
                    //Kullanıcı deaktive işlemi
                    deActivateUser(selectedUser.getUserName());

                } else if(item.getItemId() == R.id.activate) {
                    //Kullanıcı aktive işlemi
                    activateUser(selectedUser.getUserName());
                } else if(item.getItemId() == R.id.delete) {
                    //Kullanıcı silme işlemi
                    deleteUser(selectedUser.getUserName());

                } else if(item.getItemId() == R.id.editUser) {
                    //Kullanıcı düzenleme işlemi
                    Intent userEditIntent = new Intent(SysOp.this, EditProfileScreen.class);
                    userEditIntent.putExtra("user", selectedUser);
                    userEditIntent.putExtra("mainUser", receivedUser);
                    userEditIntent.putExtra("incomeScreen", "sysop");

                    startActivity(userEditIntent);

                } else if(item.getItemId() == R.id.editRole) {
                    //Kullanıcı rol yükseltme işlemi
                    roleUpdate(selectedUser);

                } else {
                    return false;
                }

                return true;
            });

            popup.show();
        });

        machineListView.setOnItemClickListener((parent, view, position, id) -> {
            Machine selectedMachine = machineList.get(position);

            Intent machineIntent = new Intent(SysOp.this, RestrictedMachineScreen.class);
            machineIntent.putExtra("machine", selectedMachine);
            machineIntent.putExtra("user", receivedUser);
            startActivity(machineIntent);
        });

        profileButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(SysOp.this, ProfileScreen.class);
            profileIntent.putExtra("user", receivedUser);
            startActivity(profileIntent);
        });

        settingsButton.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(SysOp.this, EditProfileScreen.class);
            settingsIntent.putExtra("user", receivedUser);
            startActivity(settingsIntent);
        });

        logoutButton.setOnClickListener(v -> UserDataService.logout(this));

        subLanguage.setOnClickListener(v -> {
            switchLanguage();
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        machineList = getMachineList();
        updateListView(machineList);
    }

    public void setUserInfo() {
        isimSoyisim.setText(this.getResources().getString(R.string.hello_prefix) + receivedUser.getNameSurname());
    }

    private void sectionPager() {
        allMachinesButton.setOnClickListener(v -> {
            ButtonManager.orderButtonColorEffect(1, allMachinesButton, allUsersButton, this);
            machineListView.setVisibility(View.VISIBLE);
            userListView.setVisibility(View.GONE);
            allText.setText(this.getResources().getString(R.string.allmachines));
            machineList = getMachineList();
            updateListView(machineList);
        });

        allUsersButton.setOnClickListener(v -> {
            ButtonManager.orderButtonColorEffect(2, allMachinesButton, allUsersButton, this);
            userListView.setVisibility(View.VISIBLE);
            machineListView.setVisibility(View.GONE);
            allText.setText(this.getResources().getString(R.string.allusers));
            userList = getUserList();
            updateUserListView(userList);
        });
    }

    private void deleteUser(String userName) {
        AtomicBoolean isConfirmed = new AtomicBoolean(false);
        if(userName.equals(UserDataService.getUserName(this))) {
            Util.showErrorPopup(uyariDiyalog, this.getResources().getString(R.string.self_delete_error));
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(this.getResources().getString(R.string.confirm_delete_header))
                    .setMessage(this.getResources().getString(R.string.confirm_delete_desc))
                    .setPositiveButton(this.getResources().getString(R.string.button_yes), (dialog, which) -> {
                        isConfirmed.set(true);
                    })
                    .setNegativeButton(this.getResources().getString(R.string.button_no), (dialog, which) -> {
                        isConfirmed.set(false);
                    })
                    .setOnDismissListener(dialog -> {
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            if (isConfirmed.get()) {
                                OPUserService.deleteUser(this, userName, () -> {
                                    Util.showSuccessPopup(uyariDiyalog, this.getResources().getString(R.string.user_deleted));
                                    getUserList();
                                }, () -> {
                                    Util.showErrorPopup(uyariDiyalog, this.getResources().getString(R.string.user_cant_deleted));
                                });
                            }
                        }, 500);
                    })
                    .show();
        }
    }

    private void deActivateUser(String userName) {
        if(userName.equals(UserDataService.getUserName(this))) {
            Util.showErrorPopup(uyariDiyalog, this.getResources().getString(R.string.self_deactivate_error));
        } else {
            OPUserService.deActivateUser(this, userName, () -> {
                getUserList();
                Util.showSuccessPopup(uyariDiyalog, this.getResources().getString(R.string.deactivation_successful));
            }, () -> {
                Util.showErrorPopup(uyariDiyalog, this.getResources().getString(R.string.deactivation_error));
            });
        }
    }

    private void activateUser(String userName) {
        if(userName.equals(UserDataService.getUserName(this))) {
            Util.showErrorPopup(uyariDiyalog, this.getResources().getString(R.string.self_activate_error));
        } else {
            OPUserService.activateUser(this, userName, () -> {
                getUserList();
                Util.showSuccessPopup(uyariDiyalog, this.getResources().getString(R.string.activation_successful));
            }, () -> {
                Util.showErrorPopup(uyariDiyalog, this.getResources().getString(R.string.activation_error));
            });
        }
    }

    private void roleUpdate(User selectedUser) {
        Dialog roleDialog = new Dialog(this);
        roleDialog.setContentView(R.layout.activity_popup_role);
        roleDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button normalRoleButton = roleDialog.findViewById(R.id.normalRole);
        Button technicianRoleButton = roleDialog.findViewById(R.id.technicianRole);
        Button engineerRoleButton = roleDialog.findViewById(R.id.engineerRole);
        Button sysopRoleButton = roleDialog.findViewById(R.id.sysopRole);

        normalRoleButton.setOnClickListener(v -> {
            sendRoleRequest(selectedUser, "NORMAL");
            userList = getUserList();
            updateUserListView(userList);
            roleDialog.dismiss();
        });

        technicianRoleButton.setOnClickListener(v -> {
            sendRoleRequest(selectedUser, "TECHNICIAN");
            userList = getUserList();
            updateUserListView(userList);
            roleDialog.dismiss();
        });

        engineerRoleButton.setOnClickListener(v -> {
            sendRoleRequest(selectedUser, "ENGINEER");
            userList = getUserList();
            updateUserListView(userList);
            roleDialog.dismiss();
        });

        sysopRoleButton.setOnClickListener(v -> {
            sendRoleRequest(selectedUser, "SYSOP");
            userList = getUserList();
            updateUserListView(userList);
            roleDialog.dismiss();
        });

        roleDialog.show();
    }

    private void sendRoleRequest(User selectedUser, String newRole) {
        OPUserService.updateRole(this, selectedUser.getUserName(), newRole, () -> {
            // onSuccess callback
            getUserList();
        }, () -> {
            //onFailure callback
        });
    }

    private ArrayList<Machine> getMachineList() {
        ArrayList<Machine> machines = new ArrayList<>();

        OPMachineService.getAllMachines(this, machines, () -> {
            // onSuccess callback
            updateListView(machines);
        }, () -> {
            //onFailure callback
        });

        return machines;
    }

    private ArrayList<me.t3sl4.ondergrup.Model.User.User> getUserList() {
        ArrayList<me.t3sl4.ondergrup.Model.User.User> users = new ArrayList<>();

        OPUserService.getAllUsers(this, users, () -> {
            // onSuccess callback
            updateUserListView(users);
        }, () -> {
            //onFailure callback
        });

        return users;
    }

    private void updateListView(ArrayList<Machine> machines) {
        machineList = machines;
        machineListAdapter = new MachineAdapter(this, machineList);
        machineListView.setAdapter(machineListAdapter);
    }

    private void updateUserListView(ArrayList<User> users) {
        userList = users;
        userListAdapter = new UserAdapter(this, userList);
        userListView.setAdapter(userListAdapter);
    }

    private void switchLanguage() {
        String currentLanguage = UserDataService.getSelectedLanguage(this);
        String nextLang = "";

        if (currentLanguage.equals("true")) {
            UserDataService.setSelectedLanguage(this, "false");
            nextLang = "false";
        } else {
            UserDataService.setSelectedLanguage(this, "true");
            nextLang = "true";
        }

        UserService.updatePreferences(this, UserDataService.getUserID(this), UserDataService.getSelectedLanguage(this), UserDataService.getSelectedNightMode(this));

        Util.setLocale(SysOp.this, nextLang);
        recreate();
    }
}
