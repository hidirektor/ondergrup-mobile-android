package me.t3sl4.ondergrup.Model.User.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Util.Util;

public class UserAdapter extends BaseAdapter {
    private Util util;
    private Context context;
    private ArrayList<User> userList;

    public UserAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
        this.util = new Util(context);
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_user_list_content, parent, false);
            holder = new ViewHolder();
            holder.roleUserName = convertView.findViewById(R.id.roleUserName);
            holder.nameSurname = convertView.findViewById(R.id.nameSurname);
            holder.eMail = convertView.findViewById(R.id.eMail);
            holder.phone = convertView.findViewById(R.id.phone);
            holder.company = convertView.findViewById(R.id.company);
            holder.owner = convertView.findViewById(R.id.owner);
            holder.createdAt = convertView.findViewById(R.id.createdAt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User user = userList.get(position);
        if(!Boolean.valueOf(user.getIsActive())) {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.tanitimekrani2));
        }

        holder.roleUserName.setText(user.getRole() + " -- " + user.getUserName());
        holder.nameSurname.setText(user.getNameSurname());
        holder.eMail.setText(user.geteMail());
        holder.phone.setText(user.getPhoneNumber());
        holder.company.setText(user.getCompanyName());
        holder.owner.setText(user.getOwnerName());

        holder.createdAt.setText(Util.convertUnixTimestampToDateString(Long.valueOf(user.getCreatedAt())));

        return convertView;
    }

    private static class ViewHolder {
        TextView roleUserName;
        TextView nameSurname;
        TextView eMail;
        TextView phone;
        TextView company;
        TextView owner;
        TextView createdAt;
    }
}
