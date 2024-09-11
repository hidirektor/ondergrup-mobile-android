package me.t3sl4.ondergrup.UI.Screens.Log.Error;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Objects;

import me.t3sl4.ondergrup.Model.MachineError.Adapter.MachineErrorAdapter;
import me.t3sl4.ondergrup.Model.MachineError.MachineError;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Util.HTTP.Requests.Machine.MachineService;
import me.t3sl4.ondergrup.Util.Util;

public class ErrorLog extends AppCompatActivity {
    private ImageView backToMachine;

    private ListView machineErrors;
    private MachineErrorAdapter machineErrorAdapter;
    private ArrayList<MachineError> machineErrorList;

    private Dialog uyariDiyalog;

    String currentMachineID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_errorlog);

        uyariDiyalog = new Dialog(this);

        Intent intent = getIntent();
        if(intent != null) {
            currentMachineID = intent.getStringExtra("currentmachine");
        }

        initializeComponents();

        buttonClickListeners();
    }

    private void initializeComponents() {
        backToMachine = findViewById(R.id.backToMachine);

        machineErrors = findViewById(R.id.machineErrors);
        machineErrorList = getMachineErrorList();
    }

    private void buttonClickListeners() {
        machineErrors.setOnItemClickListener((parent, view, position, id) -> {
            MachineError selectedError = machineErrorList.get(position);

            showErroDetail(uyariDiyalog, selectedError.getErrorCode());
        });

        backToMachine.setOnClickListener(v -> {
            finish();
        });
    }

    private ArrayList<MachineError> getMachineErrorList() {
        ArrayList<MachineError> machineErrorsTemp = new ArrayList<>();

        MachineService.getErrors(this, currentMachineID, machineErrorsTemp, () -> {
            updateListView(machineErrorsTemp);
        }, () -> {
            String hataMesaj = this.getResources().getString(R.string.hatayok);
            Util.showErrorPopup(uyariDiyalog, hataMesaj);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                uyariDiyalog.dismiss();
                finish();
            }, 1000);
        });

        return machineErrorsTemp;
    }

    private void updateListView(ArrayList<MachineError> machineErrorsTemp) {
        machineErrorList = machineErrorsTemp;
        machineErrorAdapter = new MachineErrorAdapter(this, machineErrorList);
        machineErrors.setAdapter(machineErrorAdapter);
    }

    private void showErroDetail(Dialog diyalog, String errorDetail) {
        diyalog.setContentView(R.layout.activity_popup_errordetail);

        int errorCode = Integer.parseInt(errorDetail);

        ImageView errorImage = diyalog.findViewById(R.id.errorImage);
        Button close = diyalog.findViewById(R.id.kapatButton);
        TableLayout tableLayout = diyalog.findViewById(R.id.tableLayout);
        TextView errorName = diyalog.findViewById(R.id.errorName);

        Drawable drawable = null;
        String errorDesc = "";
        String[][] data = {
                {"Veri 1", "Veri 2"},
                {"Veri 3", "Veri 4"}
        };

        if(errorCode == 1) {
            drawable = ContextCompat.getDrawable(this, R.drawable.ikon_hata_acilstop);
            errorDesc = this.getResources().getString(R.string.acilstop);

            String sebep1 = this.getResources().getString(R.string.error_acilstop_sebep1);
            String cozum1 = this.getResources().getString(R.string.error_acilstop_cozum1);
            String sebep2 = this.getResources().getString(R.string.error_acilstop_sebep2);
            String cozum2 = this.getResources().getString(R.string.error_acilstop_cozum2);

            data = new String[][]{
                    {sebep1, cozum1},
                    {sebep2, cozum2}
            };
        } else if(errorCode == 2) {
            drawable = ContextCompat.getDrawable(this, R.drawable.ikon_hata_emniyetcerceve);
            errorDesc = this.getResources().getString(R.string.emniyetcercevesi);

            String sebep1 = this.getResources().getString(R.string.error_emniyetcerceve_sebep1);
            String cozum1 = this.getResources().getString(R.string.error_emniyetcerceve_cozum1);
            String sebep2 = this.getResources().getString(R.string.error_emniyetcerceve_sebep2);
            String cozum2 = this.getResources().getString(R.string.error_emniyetcerceve_cozum2);
            String sebep3 = this.getResources().getString(R.string.error_emniyetcerceve_sebep3);
            String cozum3 = this.getResources().getString(R.string.error_emniyetcerceve_cozum3);

            data = new String[][]{
                    {sebep1, cozum1},
                    {sebep2, cozum2},
                    {sebep3, cozum3}
            };
        } else if(errorCode == 3) {
            drawable = ContextCompat.getDrawable(this, R.drawable.ikon_hata_basincasiriyuk);
            errorDesc = this.getResources().getString(R.string.basincasiriyuk);

            String sebep1 = this.getResources().getString(R.string.error_basinc_sebep1);
            String cozum1 = this.getResources().getString(R.string.error_basinc_cozum1);
            String sebep2 = this.getResources().getString(R.string.error_basinc_sebep2);
            String cozum2 = this.getResources().getString(R.string.error_basinc_cozum2);

            data = new String[][]{
                    {sebep1, cozum1},
                    {sebep2, cozum2}
            };
        } else if(errorCode == 4) {
            drawable = ContextCompat.getDrawable(this, R.drawable.ikon_hata_kapiswitch);
            errorDesc = this.getResources().getString(R.string.kapiswitch);

            String sebep1 = this.getResources().getString(R.string.error_kapiswitch_sebep1);
            String cozum1 = this.getResources().getString(R.string.error_kapiswitch_cozum1);
            String sebep2 = this.getResources().getString(R.string.error_kapiswitch_sebep2);
            String cozum2 = this.getResources().getString(R.string.error_kapiswitch_cozum2);

            data = new String[][]{
                    {sebep1, cozum1},
                    {sebep2, cozum2}
            };
        } else if(errorCode == 5) {
            drawable = ContextCompat.getDrawable(this, R.drawable.ikon_hata_tablakapiswitch);
            errorDesc = this.getResources().getString(R.string.tablakapiswitch);

            String sebep1 = this.getResources().getString(R.string.error_tablakapiswitch_sebep1);
            String cozum1 = this.getResources().getString(R.string.error_tablakapiswitch_cozum1);
            String sebep2 = this.getResources().getString(R.string.error_tablakapiswitch_sebep2);
            String cozum2 = this.getResources().getString(R.string.error_tablakapiswitch_cozum2);

            data = new String[][]{
                    {sebep1, cozum1},
                    {sebep2, cozum2}
            };
        } else if(errorCode == 6) {
            drawable = ContextCompat.getDrawable(this, R.drawable.ikon_hata_maximumcalisma);
            errorDesc = this.getResources().getString(R.string.maximumcalisma);

            String sebep1 = this.getResources().getString(R.string.error_maxcalisma_sebep1);
            String cozum1 = this.getResources().getString(R.string.error_maxcalisma_cozum1);
            String sebep2 = this.getResources().getString(R.string.error_maxcalisma_sebep2);
            String cozum2 = this.getResources().getString(R.string.error_maxcalisma_cozum2);
            String sebep3 = this.getResources().getString(R.string.error_maxcalisma_sebep3);
            String cozum3 = this.getResources().getString(R.string.error_maxcalisma_cozum3);
            String sebep4 = this.getResources().getString(R.string.error_maxcalisma_sebep4);
            String cozum4 = this.getResources().getString(R.string.error_maxcalisma_cozum4);

            data = new String[][]{
                    {sebep1, cozum1},
                    {sebep2, cozum2},
                    {sebep3, cozum3},
                    {sebep4, cozum4}
            };
        }

        errorImage.setImageDrawable(drawable);
        errorName.setText(errorDesc);
        fillTableWithData(tableLayout, data);

        close.setOnClickListener(v -> diyalog.dismiss());

        Objects.requireNonNull(diyalog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        diyalog.show();
    }

    private void fillTableWithData(TableLayout tableLayout, String[][] data) {
        for (String[] rowData : data) {
            TableRow tableRow = new TableRow(this);

            for (String item : rowData) {
                TextView textView = new TextView(this);
                textView.setLayoutParams(new TableRow.LayoutParams(
                        0, // width
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1f // weight
                ));
                textView.setText(item);
                textView.setTypeface(ResourcesCompat.getFont(this, R.font.outfit_medium));
                textView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                textView.setTextColor(getResources().getColor(android.R.color.black));
                textView.setGravity(Gravity.CENTER);
                tableRow.addView(textView);
            }

            tableLayout.addView(tableRow);
        }
    }


}
