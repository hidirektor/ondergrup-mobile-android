package me.t3sl4.ondergrup.Model.MachineVersion.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import me.t3sl4.ondergrup.Model.MachineVersion.Version;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Util.Util;

public class VersionAdapter extends BaseAdapter {
    private Util util;
    private Context context;
    private ArrayList<Version> versionList;

    public VersionAdapter(Context context, ArrayList<Version> versionList) {
        this.context = context;
        this.versionList = versionList;
        this.util = new Util(context);
    }

    @Override
    public int getCount() {
        return versionList.size();
    }

    @Override
    public Object getItem(int position) {
        return versionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_version_history_list_content, parent, false);
            holder = new ViewHolder();
            holder.versionTitle = convertView.findViewById(R.id.versionTitle);
            holder.versionDesc = convertView.findViewById(R.id.versionDesc);
            holder.versionCode = convertView.findViewById(R.id.versionCode);
            holder.releaseDate = convertView.findViewById(R.id.releaseDate);
            holder.versionID = convertView.findViewById(R.id.versionID);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Version version = versionList.get(position);

        //Default Strings:
        String titlePrefix = context.getResources().getString(R.string.version_title_prefix);
        String descriptionPrefix = context.getResources().getString(R.string.version_description_prefix);
        String codePrefix = context.getResources().getString(R.string.version_code_prefix);
        String datePrefix = context.getResources().getString(R.string.version_release_prefix);
        String idPrefix = context.getResources().getString(R.string.version_id_prefix);

        holder.versionTitle.setText(titlePrefix + " " + version.getVersionTitle());
        holder.versionDesc.setText(descriptionPrefix + " " + version.getVersionDesc());
        holder.versionCode.setText(codePrefix + " " + version.getVersionCode());
        holder.releaseDate.setText(datePrefix + " " + Util.convertUnixTimestampToDateString(Long.valueOf(version.getReleaseDate())));
        holder.versionID.setText(idPrefix + " " + version.getVersionID());

        return convertView;
    }

    private static class ViewHolder {
        TextView versionTitle;
        TextView versionDesc;
        TextView versionCode;
        TextView releaseDate;
        TextView versionID;
    }
}
