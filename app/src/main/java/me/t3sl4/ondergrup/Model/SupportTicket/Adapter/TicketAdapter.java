package me.t3sl4.ondergrup.Model.SupportTicket.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import me.t3sl4.ondergrup.Model.SupportTicket.Ticket;
import me.t3sl4.ondergrup.R;

public class TicketAdapter extends BaseAdapter {
    private Context context;
    private List<Ticket> ticketList;

    public TicketAdapter(Context context, List<Ticket> ticketList) {
        this.context = context;
        this.ticketList = ticketList;
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
        TextView statusTextView = convertView.findViewById(R.id.ticketStatus);
        TextView dateTextView = convertView.findViewById(R.id.ticketCreatedDate);

        titleTextView.setText(currentTicket.getTitle());
        subjectTextView.setText(currentTicket.getSubject());
        statusTextView.setText(currentTicket.getTicketStatus());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        dateTextView.setText(sdf.format(currentTicket.getCreatedDate() * 1000L)); // Unix timestamp to date

        return convertView;
    }
}