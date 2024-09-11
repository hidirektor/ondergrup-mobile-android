package me.t3sl4.ondergrup.Model.OnBoard.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.t3sl4.ondergrup.Model.OnBoard.OnBoard;
import me.t3sl4.ondergrup.R;

public class OnBoardAdapter extends RecyclerView.Adapter<OnBoardAdapter.OnBoardViewHolder> {

    private List<OnBoard> onBoardList;

    public OnBoardAdapter(List<OnBoard> onBoardList) {
        this.onBoardList = onBoardList;
    }

    @NonNull
    @Override
    public OnBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.onboard_item, parent, false);
        return new OnBoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnBoardViewHolder holder, int position) {
        OnBoard onBoard = onBoardList.get(position);
        holder.itemView.setBackgroundColor(onBoard.getBackgroundColor());
        holder.textView.setText(onBoard.getText());
        holder.imageView.setImageResource(onBoard.getImageRes());
    }

    @Override
    public int getItemCount() {
        return onBoardList.size();
    }

    public static class OnBoardViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public OnBoardViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.onBoardText);
            imageView = itemView.findViewById(R.id.onBoardImage);
        }
    }
}