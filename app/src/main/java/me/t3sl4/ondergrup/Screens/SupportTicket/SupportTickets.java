package me.t3sl4.ondergrup.Screens.SupportTicket;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

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

    private SearchView ticketSearch;
    private Button allTickets;
    private Button openTickets;
    private Button pendingTickets;
    private Button resolvedTickets;

    private ListView allTicketsList;
    private Button createTicket;

    private ArrayList<Ticket> ticketArrayList;
    private TicketAdapter ticketAdapter;

    private Dialog uyariDiyalog;
    private Dialog createTicketDialog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        uyariDiyalog = new Dialog(this);
        createTicketDialog = new Dialog(this);

        initializeComponents();

        buttonClickListeners();
        setupSearchBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTicketList();
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

        loadTicketList();
    }

    private void buttonClickListeners() {
        backButton.setOnClickListener(v -> finish());

        allTickets.setOnClickListener(v -> filterTickets("all"));
        openTickets.setOnClickListener(v -> filterTickets("Created"));
        pendingTickets.setOnClickListener(v -> filterTickets("Customer Response"));
        resolvedTickets.setOnClickListener(v -> filterTickets("Closed"));

        createTicket.setOnClickListener(v -> openCreateTicketDialog());
    }

    private ArrayList<Ticket> loadTickets() {
        ArrayList<Ticket> ticketListTemp = new ArrayList<>();

        TicketService.getTickets(this, UserDataService.getUserID(this), ticketListTemp, () -> {
            runOnUiThread(() -> {
                if (ticketAdapter != null) {
                    ticketAdapter.notifyDataSetChanged();
                } else {
                    ticketAdapter = new TicketAdapter(this, ticketListTemp, receivedUser);
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

        ticketAdapter = new TicketAdapter(this, filteredList, receivedUser);
        allTicketsList.setAdapter(ticketAdapter);
        ticketAdapter.notifyDataSetChanged();
    }

    private void setupSearchBar() {
        ticketSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterTicketById(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void filterTicketById(String query) {
        ArrayList<Ticket> filteredList = new ArrayList<>();

        try {
            int ticketId = Integer.parseInt(query);

            for (Ticket ticket : ticketArrayList) {
                if (ticket.getId() == ticketId) {
                    filteredList.add(ticket);
                    break;
                }
            }

            if (filteredList.isEmpty()) {
                Util.showErrorPopup(uyariDiyalog, getString(R.string.ticket_not_found));
            }

            ticketAdapter = new TicketAdapter(this, filteredList, receivedUser);
            allTicketsList.setAdapter(ticketAdapter);
            ticketAdapter.notifyDataSetChanged();
        } catch (NumberFormatException e) {
            Util.showErrorPopup(uyariDiyalog, getString(R.string.invalid_ticket_id));
        }
    }

    private void loadTicketList() {
        ticketArrayList = new ArrayList<>();
        ticketArrayList = loadTickets();

        ticketAdapter = new TicketAdapter(this, ticketArrayList, receivedUser);
        allTicketsList.setAdapter(ticketAdapter);
    }

    private void openCreateTicketDialog() {
        createTicketDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        createTicketDialog.setContentView(R.layout.bottom_sheet_ticket);

        EditText ticketTitle = createTicketDialog.findViewById(R.id.ticketTitle);
        Spinner ticketSubject = createTicketDialog.findViewById(R.id.ticketSubject);
        EditText ticketDescription = createTicketDialog.findViewById(R.id.ticketDescription);
        Button createTicketButton = createTicketDialog.findViewById(R.id.createTicketButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ticket_subjects, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ticketSubject.setAdapter(adapter);

        int defaultPosition = 0;
        ticketSubject.setSelection(defaultPosition);

        createTicketButton.setOnClickListener(v -> {
            String title = ticketTitle.getText().toString().trim();
            String subject = ticketSubject.getSelectedItem().toString();
            String description = ticketDescription.getText().toString().trim();

            if (title.isEmpty() || description.isEmpty()) {
                Util.showErrorPopup(uyariDiyalog, getString(R.string.ticket_fields_empty));
                return;
            }

            TicketService.createTicket(this,
                    UserDataService.getUserID(this),
                    title,
                    subject,
                    description,
                    "Android",
                    () -> {
                        createTicketDialog.dismiss();
                        Util.showSuccessPopup(uyariDiyalog, getString(R.string.ticket_created_success));
                        loadTicketList();
                    },
                    () -> Util.showErrorPopup(uyariDiyalog, getString(R.string.ticket_creation_failed))
            );
        });

        createTicketDialog.show();
        createTicketDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        createTicketDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        createTicketDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        createTicketDialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}