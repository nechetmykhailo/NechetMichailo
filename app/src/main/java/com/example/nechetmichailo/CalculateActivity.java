package com.example.nechetmichailo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.example.nechetmichailo.databinding.ActivityCalculateBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class CalculateActivity extends AppCompatActivity {
    ActivityCalculateBinding binding;

    int bmi;

    private Uri uriPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calculate);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.theme_color));

        init();

        Bundle extras = getIntent().getExtras();
        String result = extras.getString("RESULT");
        String name = extras.getString("NAME");
        String [] parsedData = result.split("\\,|\\.");

        binding.tvBMIOne.setText(parsedData[0] + ".");
        binding.tvBMITwo.setText(parsedData[1]);
        binding.tvName.setText(" " + name.toUpperCase());

        bmi = Integer.parseInt(parsedData[0]);

        if (bmi < 17){
            binding.tvStatus.setText(" Moderate Thinness".toUpperCase());
            binding.tvStatusText.setText(R.string.moderateThinness);
        }else if (bmi >= 17 || bmi <= 19){
            binding.tvStatus.setText(" Mild Thinness".toUpperCase());
            binding.tvStatusText.setText(R.string.mildThinness);
        }else if (bmi >= 19 || bmi <= 25){
            binding.tvStatus.setText(" Normal".toUpperCase());
            binding.tvStatusText.setText(R.string.normal);
        }else if (bmi >= 26 || bmi <= 30){
            binding.tvStatus.setText(" Overweight".toUpperCase());
            binding.tvStatusText.setText(R.string.overweight);
        }else if (bmi >= 31 || bmi <= 35){
            binding.tvStatus.setText(" Obese Class I".toUpperCase());
            binding.tvStatusText.setText(R.string.obeseClassOne);
        }else if (bmi >= 36){
            binding.tvStatus.setText(" Obese Class II".toUpperCase());
            binding.tvStatusText.setText(R.string.obeseClassTwo);
        }

        bmi = Integer.parseInt(parsedData[0]);

        binding.btnRate.setOnClickListener(v -> {

        });

        binding.btnShare.setOnClickListener(v -> {
            takeScreenshot(this, v);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uriPath);
            intent.setType("image/*");
            startActivity(intent);
        });
    }



    private void takeScreenshot(Context context, View v) {

        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String destPath = context.getExternalFilesDir(null).getAbsolutePath();

            String mPath = destPath + "/" + now + ".png";

            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            File file = new File(imageFile.getPath());
            uriPath = Uri.fromFile(file);

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void init() {
        Runnable runnable = () -> {
            MobileAds.initialize(getApplicationContext(), initializationStatus -> {
            });
            AdRequest adRequest = new AdRequest.Builder().build();
            binding.adView.loadAd(adRequest);
        };
        runOnUiThread(runnable);
    }
}
