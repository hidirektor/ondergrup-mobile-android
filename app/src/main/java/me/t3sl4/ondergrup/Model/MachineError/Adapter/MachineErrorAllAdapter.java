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

public class MachineErrorAllAdapter extends BaseAdapter {
    private Util util;
    private Context context;
    private ArrayList<MachineError> machineErrorList;

    public MachineErrorAllAdapter(Context context, ArrayList<MachineError> machineErrorList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_errorlog_all_list_item, parent, false);
            holder = new ViewHolder();
            holder.machineErrorImage = convertView.findViewById(R.id.machineErrorImage);
            holder.errorName = convertView.findViewById(R.id.errorName);
            holder.errorDate = convertView.findViewById(R.id.errorDate);
            holder.machineID = convertView.findViewById(R.id.machineIDText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MachineError machineError = machineErrorList.get(position);
        Drawable drawable = null;
        String errorName = "";

        // Hata kodunu güvenli bir şekilde parse et
        int errorCode = 1; // Varsayılan değer
        try {
            String errorCodeStr = machineError.getErrorCode();
            // E001, E002 gibi formatları 1, 2 gibi sayılara çevir
            if (errorCodeStr.startsWith("E")) {
                String numberPart = errorCodeStr.substring(1);
                errorCode = Integer.parseInt(numberPart);
            } else {
                errorCode = Integer.parseInt(errorCodeStr);
            }
        } catch (NumberFormatException e) {
            // Parse edilemezse varsayılan değer kullan
            errorCode = 1;
        }

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
        } else {
            // Bilinmeyen hata kodu için varsayılan değer
            errorName = context.getResources().getString(R.string.acilstop);
            drawable = ContextCompat.getDrawable(context, R.drawable.ikon_hata_acilstop);
        }

        holder.machineErrorImage.setImageDrawable(drawable);
        holder.errorName.setText(errorName);
        // Tarihi güvenli bir şekilde göster
        String errorDateStr = machineError.getErrorDate();
        String formattedDate;
        try {
            // Eğer tarih zaten string formatındaysa (15.01.2024 10:30:00 gibi) direkt kullan
            if (errorDateStr != null && (errorDateStr.contains(".") || errorDateStr.contains("/"))) {
                formattedDate = errorDateStr;
            } else {
                // Unix timestamp ise convert et
                formattedDate = Util.dateTimeConvert(errorDateStr);
            }
        } catch (Exception e) {
            // Parse edilemezse orijinal string'i kullan
            formattedDate = errorDateStr != null ? errorDateStr : "";
        }
        holder.errorDate.setText(formattedDate);
        String idText = context.getResources().getString(R.string.machine_id) + " " + machineError.getMachineID();
        holder.machineID.setText(idText);

        return convertView;
    }

    private static class ViewHolder {
        ImageView machineErrorImage;
        TextView errorName;
        TextView errorDate;
        TextView machineID;
    }
}
