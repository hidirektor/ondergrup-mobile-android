package me.t3sl4.ondergrup.Model.MachineError.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import me.t3sl4.ondergrup.Model.MachineError.MachineError;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Util.Util;

public class MachineErrorAdapter extends BaseAdapter {
    private Util util;
    private Context context;
    private ArrayList<MachineError> machineErrorList;

    public MachineErrorAdapter(Context context, ArrayList<MachineError> machineErrorList) {
        this.context = context;
        this.machineErrorList = machineErrorList;
        this.util = new Util(context);
    }

    @Override
    public int getCount() {
        return machineErrorList.size();
    }

    @Override
    public Object getItem(int position) {
        return machineErrorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_errorlog_list_item, parent, false);
            holder = new ViewHolder();
            holder.machineErrorImage = convertView.findViewById(R.id.machineErrorImage);
            holder.errorName = convertView.findViewById(R.id.errorName);
            holder.errorDate = convertView.findViewById(R.id.errorDate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MachineError machineError = machineErrorList.get(position);
        Drawable drawable = null;
        String errorName = "";

        int errorCode = Integer.parseInt(machineError.getErrorCode());

        if(errorCode == 1) {
            errorName = context.getResources().getString(R.string.acilstop);
            drawable = ContextCompat.getDrawable(context, R.drawable.ikon_hata_acilstop);
        } else if(errorCode == 2) {
            errorName = context.getResources().getString(R.string.emniyetcercevesi);
            drawable = ContextCompat.getDrawable(context, R.drawable.ikon_hata_emniyetcerceve);
        } else if(errorCode == 3) {
            errorName = context.getResources().getString(R.string.basincasiriyuk);
            drawable = ContextCompat.getDrawable(context, R.drawable.ikon_hata_basincasiriyuk);
        } else if(errorCode == 4) {
            errorName = context.getResources().getString(R.string.kapiswitch);
            drawable = ContextCompat.getDrawable(context, R.drawable.ikon_hata_kapiswitch);
        } else if(errorCode == 5) {
            errorName = context.getResources().getString(R.string.tablakapiswitch);
            drawable = ContextCompat.getDrawable(context, R.drawable.ikon_hata_tablakapiswitch);
        } else if(errorCode == 6) {
            errorName = context.getResources().getString(R.string.maximumcalisma);
            drawable = ContextCompat.getDrawable(context, R.drawable.ikon_hata_maximumcalisma);
        }

        holder.machineErrorImage.setImageDrawable(drawable);
        holder.errorName.setText(errorName);
        holder.errorDate.setText(Util.dateTimeConvert(machineError.getErrorDate()));

        return convertView;
    }

    private static class ViewHolder {
        ImageView machineErrorImage;
        TextView errorName;
        TextView errorDate;
    }
}
