package me.t3sl4.ondergrup.Model.MachineMaintenance.Adapter;

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

import me.t3sl4.ondergrup.Model.MachineMaintenance.Maintenance;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Util.Util;

public class MaintenanceAdapter extends BaseAdapter {
    private Util util;
    private Context context;
    private ArrayList<Maintenance> machineMaintenanceList;

    public MaintenanceAdapter(Context context, ArrayList<Maintenance> machineMaintenanceList) {
        this.context = context;
        this.machineMaintenanceList = machineMaintenanceList;
        this.util = new Util(context);
    }

    @Override
    public int getCount() {
        return machineMaintenanceList.size();
    }

    @Override
    public Object getItem(int position) {
        return machineMaintenanceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_maintenancelog_list_item, parent, false);
            holder = new ViewHolder();
            holder.maintenanceIcon = convertView.findViewById(R.id.maintenanceIcon);
            holder.incharge_technician = convertView.findViewById(R.id.incharge_technician);
            holder.maintenance_date = convertView.findViewById(R.id.maintenance_date);
            holder.maintenance_id = convertView.findViewById(R.id.maintenance_id);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Maintenance machineMaintenance = machineMaintenanceList.get(position);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ikon_maintenancelog);

        String preIncharge = context.getResources().getString(R.string.incharge);
        String preDate = context.getResources().getString(R.string.maintenance_date);
        String preID = context.getResources().getString(R.string.maintenance_id);

        holder.maintenanceIcon.setImageDrawable(drawable);
        holder.incharge_technician.setText(preIncharge + " " + machineMaintenance.getTechnicianName());
        
        // Bakım durumunu göster
        String maintenanceStatus = machineMaintenance.getMaintenanceStatus();
        String completionText = String.format(" (%d/%d - %.1f%%)", 
            machineMaintenance.getCompletedKontrolCount(), 
            machineMaintenance.getTotalKontrolCount(), 
            machineMaintenance.getCompletionPercentage());
        holder.incharge_technician.setText(preIncharge + " " + machineMaintenance.getTechnicianName() + " - " + maintenanceStatus + completionText);
        
        // Bakım tarihini güvenli bir şekilde işle
        String maintenanceDateStr = machineMaintenance.getMaintenanceDate();
        String formattedDate;
        try {
            // Eğer tarih zaten string formatındaysa (15.01.2024 gibi) direkt kullan
            if (maintenanceDateStr.contains(".") || maintenanceDateStr.contains("/")) {
                formattedDate = maintenanceDateStr;
            } else {
                // Unix timestamp ise convert et
                formattedDate = Util.convertUnixTimestampToDateString(Long.valueOf(maintenanceDateStr));
            }
        } catch (NumberFormatException e) {
            // Parse edilemezse orijinal string'i kullan
            formattedDate = maintenanceDateStr;
        }
        holder.maintenance_date.setText(preDate + " " + formattedDate);
        holder.maintenance_id.setText(preID + " " + machineMaintenance.getMaintenanceID());

        return convertView;
    }

    private static class ViewHolder {
        ImageView maintenanceIcon;
        TextView incharge_technician;
        TextView maintenance_date;
        TextView maintenance_id;
    }
}
