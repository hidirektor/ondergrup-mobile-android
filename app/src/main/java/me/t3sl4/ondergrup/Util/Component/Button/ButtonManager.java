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

    public static void resetAll(Context context, Button first, Button second, Button thirth, Button fourth, Button fifth, Button sixth, Button seventh, Button eighth, Button nineth) {
        first.setBackgroundResource(R.drawable.button_filter_start);
        first.setTextColor(ContextCompat.getColor(context, R.color.editTextTopColor));
        second.setBackgroundResource(R.drawable.button_filter_mid);
        second.setTextColor(ContextCompat.getColor(context, R.color.editTextTopColor));
        thirth.setBackgroundResource(R.drawable.button_filter_end);
        thirth.setTextColor(ContextCompat.getColor(context, R.color.editTextTopColor));

        fourth.setBackgroundResource(R.drawable.button_filter_start);
        fourth.setTextColor(ContextCompat.getColor(context, R.color.editTextTopColor));
        fifth.setBackgroundResource(R.drawable.button_filter_mid);
        fifth.setTextColor(ContextCompat.getColor(context, R.color.editTextTopColor));
        sixth.setBackgroundResource(R.drawable.button_filter_end);
        sixth.setTextColor(ContextCompat.getColor(context, R.color.editTextTopColor));

        seventh.setBackgroundResource(R.drawable.button_filter_start);
        seventh.setTextColor(ContextCompat.getColor(context, R.color.editTextTopColor));
        eighth.setBackgroundResource(R.drawable.button_filter_mid);
        eighth.setTextColor(ContextCompat.getColor(context, R.color.editTextTopColor));
        nineth.setBackgroundResource(R.drawable.button_filter_end);
        nineth.setTextColor(ContextCompat.getColor(context, R.color.editTextTopColor));
    }
}
