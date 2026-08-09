package com.kendito.ote.informacion;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kendito.ote.databinding.ActivityInfoAreaBinding;

public class InfoArea extends AppCompatActivity {

    private ActivityInfoAreaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoAreaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent() != null && getIntent().getExtras() != null) {
            String txtidAreaInfo = getIntent().getExtras().getString("idAreaInfo");
            String txtnombreAreaInfo = getIntent().getExtras().getString("nombreAreaInfo");

            binding.idAreaInfo.setText(txtidAreaInfo);
            binding.nombreAreaInfo.setText(txtnombreAreaInfo);
        }
    }
}
