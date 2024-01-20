package me.t3sl4.ondergrup.Util.Component.Button;

import android.content.Context;
import android.util.Log;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import me.t3sl4.ondergrup.R;

public class ButtonManager {
    public static void orderButtonColorEffect(int type, Button firstButton, Button secondButton, Context context) {
        switch (type) {
            case 1:
                firstButton.setBackgroundResource(R.drawable.button_filter_start_dark);
                firstButton.setTextColor(ContextCompat.getColor(context, R.color.white));

                secondButton.setBackgroundResource(R.drawable.button_filter_end);
                secondButton.setTextColor(ContextCompat.getColor(context, R.color.editTextTopColor));
                break;
            case 2:
                firstButton.setBackgroundResource(R.drawable.button_filter_start);
                firstButton.setTextColor(ContextCompat.getColor(context, R.color.editTextTopColor));

                secondButton.setBackgroundResource(R.drawable.button_filter_end_dark);
                secondButton.setTextColor(ContextCompat.getColor(context, R.color.white));
                break;
            default:
                Log.e("ButtonManager", "Unsupported button color change type !");
        }
    }
}
