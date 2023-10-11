package me.t3sl4.ondergrup.Screens.Machine.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Objects;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Util.Util;

public class MachineAdapter extends BaseAdapter {
    private Util util;
    private Context context;
    private ArrayList<Machine> machineList;

    public MachineAdapter(Context context, ArrayList<Machine> machineList) {
        this.context = context;
        this.machineList = machineList;
        this.util = new Util(context);
    }

    @Override
    public int getCount() {
        return machineList.size();
    }

    @Override
    public Object getItem(int position) {
        return machineList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_machine_list_content, parent, false);
            holder = new ViewHolder();
            holder.machineImage = convertView.findViewById(R.id.machineImage);
            holder.machineOwnerName = convertView.findViewById(R.id.machineOwnerName);
            holder.machineSelectedLanguage = convertView.findViewById(R.id.machineSelectedLanguage);
            holder.machineStatus = convertView.findViewById(R.id.machineStatus);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Machine machine = machineList.get(position);
        Drawable drawable = null;
        String secilenDil = "";
        String machineStat = "";
        if(Objects.equals(machine.getMachineType(), "ESP")) {
            drawable = context.getResources().getDrawable(R.drawable.tanitimekrani_esp1);
        } else if(Objects.equals(machine.getMachineType(), "CSP-D")) {
            drawable = context.getResources().getDrawable(R.drawable.tanitimekrani_csp1);
        }

        if(Objects.equals(machine.getDilSecim(), "0")) {
            secilenDil = "Türkçe";
        } else if(Objects.equals(machine.getDilSecim(), "1")) {
            secilenDil = "İngilizce";
        }

        if(Objects.nonNull(machine.getLastUpdate())) {
            machineStat = "Aktif";
        } else {
            machineStat = "Aktif Değil";
        }
        holder.machineImage.setImageDrawable(drawable);
        holder.machineOwnerName.setText(machine.getMachineID() + " - " + machine.getOwnerUser() + " - " + machine.getMachineType());
        holder.machineSelectedLanguage.setText(secilenDil);
        holder.machineStatus.setText(machineStat);

        return convertView;
    }

    private static class ViewHolder {
        ImageView machineImage;
        TextView machineOwnerName;
        TextView machineSelectedLanguage;
        TextView machineStatus;
    }
}
