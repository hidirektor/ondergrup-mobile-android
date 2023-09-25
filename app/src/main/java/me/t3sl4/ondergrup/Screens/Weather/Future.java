package me.t3sl4.ondergrup.Screens.Weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import me.t3sl4.ondergrup.Screens.MainActivity;
import me.t3sl4.ondergrup.Util.Weather.adapters.FutureAdapter;
import me.t3sl4.ondergrup.Util.Weather.domain.FutureDomain;
import me.t3sl4.ondergrup.R;

import java.util.ArrayList;

public class Future extends AppCompatActivity {
    private RecyclerView.Adapter adapterTommorow;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_future);

        initRecyclerView();
        setVariable();
    }

    private void setVariable() {
        ConstraintLayout backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> startActivity(new Intent(Future.this, MainActivity.class)));
    }

    private void initRecyclerView() {
        ArrayList<FutureDomain> items = new ArrayList<>();

        items.add(new FutureDomain("Sat", "storm", "storm", 25, 10));
        items.add(new FutureDomain("Sun", "cloudy", "cloudy", 24, 16));
        items.add(new FutureDomain("Mon", "windy", "windy", 29, 15));
        items.add(new FutureDomain("Tue", "cloudy_sunny", "Cloudy Sunny", 22, 13));
        items.add(new FutureDomain("Wen", "sunny", "sunny", 28, 11));
        items.add(new FutureDomain("Thu", "rainy", "Rainy", 23, 12));

        recyclerView = findViewById(R.id.view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapterTommorow = new FutureAdapter(items);
        recyclerView.setAdapter(adapterTommorow);
    }
}