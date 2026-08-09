package com.kendito.ote.informacion;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kendito.ote.databinding.ActivityInfoSitioBinding;

public class InfoSitio extends AppCompatActivity {

    private ActivityInfoSitioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoSitioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent() != null && getIntent().getExtras() != null) {
            String txtidSitio = getIntent().getExtras().getString("idSitio");
            String txtnombreSitio = getIntent().getExtras().getString("nombreSitio");
            String txtnombreArea = getIntent().getExtras().getString("nombreArea");

            binding.idSitioInfo.setText(txtidSitio);
            binding.nombreSitioInfo.setText(txtnombreSitio);
            binding.nombreAreaInfo.setText(txtnombreArea);
        }
    }
}
