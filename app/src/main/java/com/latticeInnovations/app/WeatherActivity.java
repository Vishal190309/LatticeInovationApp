package com.latticeInnovations.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.latticeInnovations.app.controller.AppController;
import com.latticeInnovations.app.data.Repository;
import com.latticeInnovations.app.databinding.ActivityWeatherBinding;

import java.util.Objects;

public class WeatherActivity extends AppCompatActivity {
    ActivityWeatherBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather);
        Intent intent = getIntent();

        String city = intent.getStringExtra("city");

        binding.cityText.setText(city);
        showResults(city);
        binding.showResultButton.setOnClickListener(view ->
            showResults(Objects.requireNonNull(binding.cityText.getText()).toString().trim())
        );

    }

    public void showResults(String city) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
        alertDialog.setView(R.layout.dialogue_layout);
        AlertDialog dialog = alertDialog.create();
        dialog.show();


        new Repository(city, AppController.getInstance().getResourceProvider()).getWeatherData((tempF, tempC, lat, lon) -> {

            if (tempF.equals("") || lat.equals("")) {
                binding.cityTextLayout.setError("City Not found");
            } else {
                binding.cityTextLayout.setError(null);
                binding.tempfText.setText(tempF);
                binding.tempcText.setText(tempC);
                binding.latitudeText.setText(lat);
                binding.longitudeText.setText(lon);
            }
            dialog.dismiss();
        });
    }
}