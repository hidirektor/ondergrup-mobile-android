package me.t3sl4.ondergrup.Screens.SupportTicket;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.t3sl4.ondergrup.Model.Message.Adapter.MessageAdapter;
import me.t3sl4.ondergrup.Model.Message.Message;
import me.t3sl4.ondergrup.Model.SupportTicket.Ticket;
import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.HTTP.Requests.SupportTickets.TicketService;

public class SupportTicketDetails extends AppCompatActivity {
    private User receivedUser;
    private Ticket currentTicket;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private RecyclerView recyclerView;

    private ImageView backButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_ticket_details);

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");
        currentTicket = intent.getParcelableExtra("ticket");

        recyclerView = findViewById(R.id.recycler_gchat);
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, receivedUser.getUserID());

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(messageAdapter);

        // Parse JSON
        try {
            JSONObject responsesObject = new JSONObject(currentTicket.getResponses());
            JSONArray responsesArray = responsesObject.getJSONArray("responses");

            for (int i = 0; i < responsesArray.length(); i++) {
                JSONObject response = responsesArray.getJSONObject(i);
                String userID = response.getString("userID");
                String userName = response.getString("userName");
                String nameSurname = response.getString("nameSurname");
                long commentDate = response.getLong("commentDate");
                String comment = response.getString("comment");

                String type = userID.equals(currentTicket.getOwnerID()) ? "selfMessage" : "otherMessage";

                messageList.add(new Message(userID, userName, nameSurname, commentDate, comment, type));
            }

            messageAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(messageList.size() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageView sendButton = findViewById(R.id.sendButton);
        EditText messageInput = findViewById(R.id.inputMessage);

        if ("Closed".equals(currentTicket.getTicketStatus())) {
            sendButton.setVisibility(View.GONE);
            messageInput.setVisibility(View.GONE);
        } else {
            sendButton.setOnClickListener(v -> {
                String newMessage = messageInput.getText().toString();
                if (!newMessage.isEmpty()) {
                    long currentTime = System.currentTimeMillis() / 1000L;
                    String userID = UserDataService.getUserID(this);

                    TicketService.responseTicket(this, String.valueOf(currentTicket.getId()), userID, newMessage, () -> {
                        messageList.add(new Message(userID, receivedUser.getUserName(), receivedUser.getNameSurname(), currentTime, newMessage, "selfMessage"));
                        messageAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(messageList.size() - 1);
                        messageInput.setText("");
                    }, () -> {
                        finish();
                    });
                }
            });
        }
    }

    private String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(timestamp * 1000L);
    }

    private String formatDateTop(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(timestamp * 1000L);
    }
}