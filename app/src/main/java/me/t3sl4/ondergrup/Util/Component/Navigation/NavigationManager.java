package me.t3sl4.ondergrup.Util.Component.Navigation;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.navigation.NavigationView;

import me.t3sl4.ondergrup.R;

public class NavigationManager {
    public static void showNavigationViewWithAnimation(NavigationView hamburgerMenu, Context context) {
        Animation slideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
        hamburgerMenu.setVisibility(View.VISIBLE);
        hamburgerMenu.startAnimation(slideIn);
    }

    public static void hideNavigationViewWithAnimation(NavigationView hamburgerMenu, Context context) {
        Animation slideOut = AnimationUtils.loadAnimation(context, R.anim.slide_out_right);
        slideOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                hamburgerMenu.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        hamburgerMenu.startAnimation(slideOut);
    }
}
