package com.example.nechetmichailo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.nechetmichailo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private String[] arraysGender;
    private ActivityMainBinding binding;
    private String genderFinal, name;
    private double weihtFinal, heightFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.theme_color));

        arraysGender = new String[]{"Male", "Female"};

        binding.gender.setDisplayedValues(arraysGender);

        binding.height.setMinValue(100);
        binding.height.setMaxValue(220);

        binding.gender.setMinValue(0);
        binding.gender.setMaxValue(1);

        binding.weight.setMinValue(50);
        binding.weight.setMaxValue(200);

        binding.btnCalculate.setOnClickListener(v -> {
            if(binding.gender.getValue() == 0){
                genderFinal = "Male";
            }else if(binding.gender.getValue() == 1){
                genderFinal = "Female";
            }

            weihtFinal = binding.weight.getValue();
            heightFinal = binding.height.getValue();

            double heightTest = heightFinal /100;
            double squre = heightTest * heightTest;
            double res = weihtFinal / squre;

            String formattedDouble = String.format("%.2f", res);
            name = binding.etName.getText().toString();

            if (!name.equals("")){
                Intent intent = new Intent(this, CalculateActivity.class);
                intent.putExtra("RESULT", formattedDouble);
                intent.putExtra("NAME", name);
                startActivity(intent);
            }else {
                Toast.makeText(getApplicationContext(), "Is not empty", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
