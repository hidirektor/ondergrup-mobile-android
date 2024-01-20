package me.t3sl4.ondergrup.Screens.SubUser;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Util.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class SubUserScreen extends AppCompatActivity {
    public Util util;
    public User receivedUser;

    private Button subUserListeleButton;
    private Button subUsterEkleButton;
    private ImageView backButton;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subuser);

        util = new Util(getApplicationContext());
        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        subUserListeleButton = findViewById(R.id.showButton);
        subUsterEkleButton = findViewById(R.id.addSubUserButton);
        backButton = findViewById(R.id.backButton);

        subUserListeleButton.setOnClickListener(v -> {
            Intent addIntent = new Intent(SubUserScreen.this, SubUserListScreen.class);
            addIntent.putExtra("user", receivedUser);
            startActivity(addIntent);
        });

        subUsterEkleButton.setOnClickListener(v -> {
            Intent addIntent = new Intent(SubUserScreen.this, SubUserAddScreen.class);
            addIntent.putExtra("user", receivedUser);
            startActivity(addIntent);
        });

        backButton.setOnClickListener(v -> {
            finish();
        });
    }
}
