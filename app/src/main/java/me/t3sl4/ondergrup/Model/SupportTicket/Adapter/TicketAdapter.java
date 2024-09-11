package me.t3sl4.ondergrup.Model.SupportTicket.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.List;

import me.t3sl4.ondergrup.Model.SupportTicket.Ticket;
import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.UI.Screens.SupportTicket.SupportTicketDetails;

public class TicketAdapter extends BaseAdapter {
    private Context context;
    private List<Ticket> ticketList;
    private User receivedUser;

    public TicketAdapter(Context context, List<Ticket> ticketList, User receivedUser) {
        this.context = context;
        this.ticketList = ticketList;
        this.receivedUser = receivedUser;
    }

    @Override
    public int getCount() {
        return ticketList.size();
    }

    @Override
    public Object getItem(int position) {
        return ticketList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ticketList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_ticket_list_item, parent, false);
        }

        Ticket currentTicket = ticketList.get(position);

        TextView titleTextView = convertView.findViewById(R.id.ticketTitle);
        TextView subjectTextView = convertView.findViewById(R.id.ticketSubject);
        TextView dateTextView = convertView.findViewById(R.id.ticketCreatedDate);
        Button statusButton = convertView.findViewById(R.id.ticketStatus);
        ImageView showDetailsButton = convertView.findViewById(R.id.showTicketDetails);

        titleTextView.setText("#" + currentTicket.getId() + " " + currentTicket.getTitle());
        subjectTextView.setText(currentTicket.getSubject());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss");
        dateTextView.setText(sdf.format(currentTicket.getCreatedDate() * 1000L));

        String currentStatus = currentTicket.getTicketStatus();
        String statusButtonText = "";
        int statusTextColor = ContextCompat.getColor(context, R.color.errorsolution);
        if(currentStatus.equals("Created")) {
            statusButtonText = context.getResources().getString(R.string.ticket_status_created);
            statusTextColor = ContextCompat.getColor(context, R.color.errorsolution);
        } else if(currentStatus.equals("Answered")) {
            statusButtonText = context.getResources().getString(R.string.ticket_status_answered);
            statusTextColor = ContextCompat.getColor(context, R.color.colorAccent);
        } else if(currentStatus.equals("Customer Response")) {
            statusButtonText = context.getResources().getString(R.string.ticket_status_customer_response);
            statusTextColor = ContextCompat.getColor(context, R.color.softorange);
        } else if(currentStatus.equals("Closed")) {
            statusButtonText = context.getResources().getString(R.string.ticket_status_closed);
            statusTextColor = ContextCompat.getColor(context, R.color.white);
        }
        statusButton.setText(statusButtonText);
        statusButton.setTextColor(statusTextColor);

        showDetailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, SupportTicketDetails.class);
            intent.putExtra("user", receivedUser);
            intent.putExtra("ticket", currentTicket);
            context.startActivity(intent);
        });

        return convertView;
    }
}