package com.kendito.ote.informacion;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kendito.ote.databinding.ActivityInfoOteBinding;

public class InfoOTE extends AppCompatActivity {

    private ActivityInfoOteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoOteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.programador.setSelected(true);
    }
}
