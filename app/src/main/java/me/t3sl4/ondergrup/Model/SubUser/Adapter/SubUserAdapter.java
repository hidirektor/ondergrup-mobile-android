package me.t3sl4.ondergrup.Model.SubUser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import me.t3sl4.ondergrup.Model.SubUser.SubUser;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Util.Util;

public class SubUserAdapter extends BaseAdapter {
    private Util util;
    private Context context;
    private ArrayList<SubUser> subUserList;

    public SubUserAdapter(Context context, ArrayList<SubUser> subUserList) {
        this.context = context;
        this.subUserList = subUserList;
        this.util = new Util(context);
    }

    @Override
    public int getCount() {
        return subUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return subUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_subuser_list_content, parent, false);
            holder = new ViewHolder();
            holder.profilePhoto = convertView.findViewById(R.id.subUserImage);
            holder.nameSurname = convertView.findViewById(R.id.subUserNameSurname);
            holder.userName = convertView.findViewById(R.id.subUserUserName);
            holder.companyName = convertView.findViewById(R.id.subUserCompanyName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SubUser subUser = subUserList.get(position);
        if(!Boolean.valueOf(subUser.getIsActive())) {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.tanitimekrani2));
        }
        holder.profilePhoto.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ikon_profilephoto));
        holder.nameSurname.setText(subUser.getNameSurname());
        holder.userName.setText(subUser.getUserName());
        holder.companyName.setText(subUser.getCompanyName());

        return convertView;
    }

    private static class ViewHolder {
        ImageView profilePhoto;
        TextView nameSurname;
        TextView userName;
        TextView companyName;
    }
}
