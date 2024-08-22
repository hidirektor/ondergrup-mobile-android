package me.t3sl4.ondergrup.Model.Message.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import me.t3sl4.ondergrup.Model.Message.Message;
import me.t3sl4.ondergrup.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private String currentUserID;

    public MessageAdapter(List<Message> messageList, String currentUserID) {
        this.messageList = messageList;
        this.currentUserID = currentUserID;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == 0) {
            view = inflater.inflate(R.layout.activity_support_ticket_self_message, parent, false);
        } else {
            view = inflater.inflate(R.layout.activity_support_ticket_other_message, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);

        holder.messageTextView.setText(message.getComment());

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String formattedTime = sdf.format(message.getCommentDate() * 1000L);

        holder.timestampTextView.setText(formattedTime);

        if (getItemViewType(position) == 1) {
            holder.userNameTextView.setText(message.getNameSurname());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getType().equals("selfMessage") ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView timestampTextView;
        TextView userNameTextView;

        MessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageText);
            timestampTextView = itemView.findViewById(R.id.messageTime);
            userNameTextView = itemView.findViewById(R.id.ownerUserName);
        }
    }
}