package me.t3sl4.ondergrup.Util.QR;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.t3sl4.ondergrup.Model.QR.Database.DatabaseHelper;
import me.t3sl4.ondergrup.R;

public class BottomDialog extends BottomSheetDialogFragment {

    private String fetchUrl;

    public interface OnQRResultListener {
        void onQRResult(String result);
    }

    private OnQRResultListener qrResultListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.qr_bottom_dialog, container, false);

        TextView time = view.findViewById(R.id.txt_date);
        TextView result = view.findViewById(R.id.txt_result);
        CardView btn_use = view.findViewById(R.id.use_scanned_id);
        CardView btn_share = view.findViewById(R.id.share);
        CardView btn_copy = view.findViewById(R.id.copy);
        ImageView btn_close = view.findViewById(R.id.close);

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());

        result.setText(fetchUrl);

        databaseHelper.insertData("Scanned", fetchUrl);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy hh:mm a");
        String dateTime = dateFormat.format(calendar.getTime());

        time.setText(dateTime);

        btn_close.setOnClickListener(v -> dismiss());

        btn_use.setOnClickListener(v -> {
            if (qrResultListener != null) {
                qrResultListener.onQRResult(fetchUrl);
            }

            if (getActivity() != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("scannedQRCode", fetchUrl);
                getActivity().setResult(Activity.RESULT_OK, resultIntent);
                dismiss();
                getActivity().finish();
            }
        });

        btn_share.setOnClickListener(v -> {
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share));
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, fetchUrl);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
        });

        btn_copy.setOnClickListener(v -> {
            ClipboardManager clipboardManager =
                    (ClipboardManager) requireContext().getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("label", fetchUrl);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(getContext(), "text copied...", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    public void fetchUrl(String url) {
        ExecutorService service = Executors.newSingleThreadExecutor();

        service.execute(() -> fetchUrl = url);
    }

    public void setOnQRResultListener(OnQRResultListener listener) {
        this.qrResultListener = listener;
    }
}
