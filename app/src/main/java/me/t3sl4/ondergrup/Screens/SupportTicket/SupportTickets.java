package me.t3sl4.ondergrup.Screens.SupportTicket;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.search.SearchBar;

import java.util.ArrayList;

import me.t3sl4.ondergrup.Model.SupportTicket.Adapter.TicketAdapter;
import me.t3sl4.ondergrup.Model.SupportTicket.Ticket;
import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.HTTP.Requests.SupportTickets.TicketService;
import me.t3sl4.ondergrup.Util.Util;

public class SupportTickets extends AppCompatActivity {
    public User receivedUser;

    private ImageView backButton;

    private SearchBar ticketSearch;
    private Button allTickets;
    private Button openTickets;
    private Button pendingTickets;
    private Button resolvedTickets;

    private ListView allTicketsList;
    private Button createTicket;

    private ArrayList<Ticket> ticketArrayList;
    private TicketAdapter ticketAdapter;

    private Dialog uyariDiyalog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        uyariDiyalog = new Dialog(this);

        initializeComponents();

        buttonClickListeners();
    }

    private void initializeComponents() {
        backButton = findViewById(R.id.backButton);

        ticketSearch = findViewById(R.id.search_bar);
        allTickets = findViewById(R.id.allTickets);
        openTickets = findViewById(R.id.openTickets);
        pendingTickets = findViewById(R.id.pendingTickets);
        resolvedTickets = findViewById(R.id.resolvedTickets);

        allTicketsList = findViewById(R.id.allTicketsList);
        createTicket = findViewById(R.id.createTicketButton);

        ticketArrayList = loadTickets();

        ticketAdapter = new TicketAdapter(this, ticketArrayList);
        allTicketsList.setAdapter(ticketAdapter);
    }

    private void buttonClickListeners() {
        backButton.setOnClickListener(v -> finish());

        allTickets.setOnClickListener(v -> filterTickets("all"));
        openTickets.setOnClickListener(v -> filterTickets("Created"));
        pendingTickets.setOnClickListener(v -> filterTickets("Customer Response"));
        resolvedTickets.setOnClickListener(v -> filterTickets("Closed"));
    }

    private ArrayList<Ticket> loadTickets() {
        ArrayList<Ticket> ticketListTemp = new ArrayList<>();

        TicketService.getTickets(this, UserDataService.getUserID(this), ticketListTemp, () -> {
            runOnUiThread(() -> {
                if (ticketAdapter != null) {
                    ticketAdapter.notifyDataSetChanged();
                } else {
                    ticketAdapter = new TicketAdapter(this, ticketListTemp);
                    allTicketsList.setAdapter(ticketAdapter);
                }
            });
        }, () -> {
            runOnUiThread(() -> {
                String hataMesaj = this.getResources().getString(R.string.ticketyok_all);
                Util.showErrorPopup(uyariDiyalog, hataMesaj);
            });
        });

        return ticketListTemp;
    }

    private void filterTickets(String filter) {
        ArrayList<Ticket> filteredList = new ArrayList<>();
        ArrayList<Ticket> answeredList = new ArrayList<>();
        ArrayList<Ticket> closedList = new ArrayList<>();

        for (Ticket ticket : ticketArrayList) {
            if (filter.equals("all")) {
                filteredList.add(ticket);
            } else if (filter.equals("Created") && ticket.getTicketStatus().equals("Created")) {
                filteredList.add(ticket);
            } else if (filter.equals("Customer Response") && ticket.getTicketStatus().equals("Customer Response")) {
                filteredList.add(ticket);
            } else if (filter.equals("Closed") || filter.equals("Answered")) {
                if (ticket.getTicketStatus().equals("Answered")) {
                    answeredList.add(ticket);
                } else if (ticket.getTicketStatus().equals("Closed")) {
                    closedList.add(ticket);
                }
            }
        }

        if (filter.equals("Closed") || filter.equals("Answered")) {
            filteredList.addAll(answeredList);
            filteredList.addAll(closedList);
        }

        ticketAdapter = new TicketAdapter(this, filteredList);
        allTicketsList.setAdapter(ticketAdapter);
        ticketAdapter.notifyDataSetChanged();
    }
}