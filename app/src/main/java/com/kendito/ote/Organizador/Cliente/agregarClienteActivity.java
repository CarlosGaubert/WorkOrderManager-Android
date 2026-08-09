package com.kendito.ote.Organizador.Cliente;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kendito.ote.R;
import com.kendito.ote.api.RegisterAPI;
import com.kendito.ote.databinding.ActivityAgregarClienteBinding;
import com.kendito.ote.model.Result;
import com.kendito.ote.model.Sitio;
import com.kendito.ote.model.valueMensaje;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class agregarClienteActivity extends AppCompatActivity {

    public static final String URL = "http://kenditobd.ddns.net/ote/";
    private ProgressDialog progress;

    private List<String> values = new ArrayList<>();
    private List<String> valuesid = new ArrayList<>();
    private List<Result> sitios;
    private ActivityAgregarClienteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgregarClienteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        binding.btnAgregarCliente.setOnClickListener(view -> ingresarCliente());
        llenarComboSitio();
    }

    private void ingresarCliente() {
        int index = binding.SpiACSitio.getSelectedItemPosition();
        if (index < 0 || index >= valuesid.size()) {
            Toast.makeText(this, "Seleccione un sitio válido", Toast.LENGTH_SHORT).show();
            return;
        }

        String idSitioSel = valuesid.get(index);
        String rut = binding.rutClienteAgregar.getText().toString().trim();
        String dv = binding.txtEditDVA.getText().toString().trim();
        String rutCompleto = rut + "-" + dv;
        String nombre = binding.nombreClienteAgregar.getText().toString().trim();
        String contrasena = binding.contrasenaClienteAgregar.getText().toString().trim();
        String idtipoPersonal = "3";

        if (TextUtils.isEmpty(rut) || TextUtils.isEmpty(dv)) {
            Toast.makeText(this, "Ingresar rut del cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(this, "Ingresar nombre del cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(contrasena)) {
            Toast.makeText(this, "Ingresar contraseña del cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (!verificarRut(Integer.parseInt(rut), dv)) {
                Toast.makeText(this, "Rut invalido", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Rut numérico inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Agregando cliente...");
        progress.show();

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<valueMensaje> call = api.ingresarCliente(rutCompleto, idtipoPersonal, idSitioSel, nombre, contrasena);

        call.enqueue(new Callback<valueMensaje>() {
            @Override
            public void onResponse(Call<valueMensaje> call, Response<valueMensaje> response) {
                progress.dismiss();
                if (response.body() != null) {
                    String message = response.body().getMessage();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<valueMensaje> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Ocurrio un problema al ingresar el cliente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void llenarComboSitio() {
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Cargando datos de sitios...");
        progress.show();

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<Sitio> call = api.verSitios();

        call.enqueue(new Callback<Sitio>() {
            @Override
            public void onResponse(Call<Sitio> call, Response<Sitio> response) {
                progress.dismiss();
                if (response.body() != null && response.body().getValue() == 1) {
                    sitios = response.body().getResult();
                    values.clear();
                    valuesid.clear();

                    for (int i = 0; i < sitios.size(); i++) {
                        values.add(sitios.get(i).getNOMBRESITIO());
                        valuesid.add(sitios.get(i).getIDSITIO());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(agregarClienteActivity.this, R.layout.spinner_item, values);
                    binding.SpiACSitio.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Sitio> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Fallo conexion a internet", Toast.LENGTH_SHORT).show();
                volver();
            }
        });
    }

    public void volver() {
        Intent intent = new Intent(this, gestionarClienteActivity.class);
        startActivity(intent);
    }

    public boolean verificarRut(int rut, String digitoVerificador) {
        if ("k".equalsIgnoreCase(digitoVerificador)) digitoVerificador = "K";

        int contador = 2;
        int acomulador = 0;
        int rut1 = rut;

        for (int i = 0; i < 8; i++) {
            int multiplo = (rut1 % 10) * contador;
            acomulador += multiplo;
            rut1 /= 10;
            contador += 1;
            if (contador == 8) {
                contador = 2;
            }
        }
        int digito = 11 - (acomulador % 11);

        String digitoFinal = Integer.toString(digito);
        if ("10".equals(digitoFinal)) digitoFinal = "K";
        if ("11".equals(digitoFinal)) digitoFinal = "0";

        return digitoFinal.equalsIgnoreCase(digitoVerificador);
    }
}
