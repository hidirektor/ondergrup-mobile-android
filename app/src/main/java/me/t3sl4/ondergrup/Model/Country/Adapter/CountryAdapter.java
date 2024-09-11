package me.t3sl4.ondergrup.Model.Country.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.t3sl4.ondergrup.Model.Country.Country;
import me.t3sl4.ondergrup.R;

public class CountryAdapter extends ArrayAdapter<Country> {

    public CountryAdapter(Context context, List<Country> countries) {
        super(context, 0, countries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent, false);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent, true);
    }

    private View initView(int position, View convertView, ViewGroup parent, boolean isDropDown) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.country_spinner_item, parent, false);
        }

        ImageView flagImageView = convertView.findViewById(R.id.country_flag);
        TextView countryTextView = convertView.findViewById(R.id.country_text);
        TextView countryNameTextView = convertView.findViewById(R.id.country_name);

        Country currentItem = getItem(position);

        if (currentItem != null) {
            flagImageView.setImageResource(currentItem.getFlagResId());
            countryNameTextView.setText(currentItem.getName());

            if (isDropDown) {
                countryTextView.setVisibility(View.GONE); // Spinner açıldığında GONE
            } else {
                countryTextView.setVisibility(View.VISIBLE); // Spinner kapalıyken VISIBLE
            }
        }

        return convertView;
    }
}