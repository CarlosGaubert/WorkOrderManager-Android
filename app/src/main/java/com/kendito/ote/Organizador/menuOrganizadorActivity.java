package com.kendito.ote.Organizador;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.kendito.ote.Organizador.Area.gestionarAreaActivity;
import com.kendito.ote.Organizador.Cliente.gestionarClienteActivity;
import com.kendito.ote.Organizador.Ordenes.GestionarOrdenes;
import com.kendito.ote.Organizador.Personal.gestionarPersonalActivity;
import com.kendito.ote.Organizador.Sitio.gestionarSitioActivity;
import com.kendito.ote.Personal_Organizador.VerOrdenesPersonal;
import com.kendito.ote.databinding.ActivityMenuAdministradorBinding;

public class menuOrganizadorActivity extends AppCompatActivity {

    private ActivityMenuAdministradorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuAdministradorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        binding.btnAVerOrdenes.setOnClickListener(v -> {
            Intent intent = new Intent(this, VerOrdenesPersonal.class);
            startActivity(intent);
        });

        binding.btnAGestionarOrdenes.setOnClickListener(v -> {
            Intent intent = new Intent(this, GestionarOrdenes.class);
            startActivity(intent);
        });

        binding.btnAGestionarPersonal.setOnClickListener(v -> {
            Intent intent = new Intent(this, gestionarPersonalActivity.class);
            startActivity(intent);
        });

        binding.btnAGestionarClientes.setOnClickListener(v -> {
            Intent intent = new Intent(this, gestionarClienteActivity.class);
            startActivity(intent);
        });

        binding.btnAGestionarAreas.setOnClickListener(v -> {
            Intent intent = new Intent(this, gestionarAreaActivity.class);
            startActivity(intent);
        });

        binding.btnAGestionarSitios.setOnClickListener(v -> {
            Intent intent = new Intent(this, gestionarSitioActivity.class);
            startActivity(intent);
        });
    }
}