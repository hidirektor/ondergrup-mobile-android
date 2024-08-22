package me.t3sl4.ondergrup.Screens.SupportTicket;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;

public class SupportTicketDetails extends AppCompatActivity {
    public User receivedUser;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

    }
}
