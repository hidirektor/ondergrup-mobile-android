package me.t3sl4.countrypicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.widget.AppCompatSpinner;

import java.util.List;

import me.t3sl4.countrymodule.R;
import me.t3sl4.countrypicker.Model.Adapter.CountryAdapter;
import me.t3sl4.countrypicker.Model.Country;

public class CountryPicker extends AppCompatSpinner {

    private List<Country> countries;
    private Country selectedCountry;

    public CountryPicker(Context context) {
        super(context);
        init(context, null);
    }

    public CountryPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        String defaultCountryCode = "TR";

        this.setBackgroundResource(R.drawable.country_spinner_background);

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CountryPicker, 0, 0);
            try {
                defaultCountryCode = a.getString(R.styleable.CountryPicker_defaultCountryCode);
            } finally {
                a.recycle();
            }
        }

        countries = CountryData.getCountries(context);
        CountryAdapter adapter = new CountryAdapter(context, countries);
        this.setAdapter(adapter);

        int defaultPosition = getDefaultCountryPosition(defaultCountryCode);
        if (defaultPosition != -1) {
            setSelection(defaultPosition);
            selectedCountry = countries.get(defaultPosition);
        }

        this.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = countries.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCountry = null;
            }
        });
    }

    private int getDefaultCountryPosition(String defaultCountryCode) {
        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getCode().equals(defaultCountryCode)) {
                return i;
            }
        }
        return -1;
    }

    public Country getSelectedCountry() {
        return selectedCountry;
    }
}