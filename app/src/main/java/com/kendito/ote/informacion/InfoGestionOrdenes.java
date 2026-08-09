package com.kendito.ote.informacion;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kendito.ote.databinding.ActivityInfoGestionOrdenesBinding;

public class InfoGestionOrdenes extends AppCompatActivity {

    private ActivityInfoGestionOrdenesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoGestionOrdenesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent() != null && getIntent().getExtras() != null) {
            String codigoOrden = getIntent().getExtras().getString("codigoOrden");
            String rCliente = getIntent().getExtras().getString("rutCliente");
            String nombre = getIntent().getExtras().getString("nombreCliente");
            String fecha = getIntent().getExtras().getString("fechaOrden");
            String sitio = getIntent().getExtras().getString("nombreSitio");
            String area = getIntent().getExtras().getString("nombreArea");
            String descrip = getIntent().getExtras().getString("descripcion");

            binding.idCodigoOrden.setText(codigoOrden);
            binding.rutCliente.setText(rCliente);
            binding.nombreCliente.setText(nombre);
            binding.fechaOrdenPedida.setText(fecha);
            binding.nombreSitio.setText(sitio);
            binding.nombreArea.setText(area);
            binding.descripcion.setText(descrip);
        }
    }
}
