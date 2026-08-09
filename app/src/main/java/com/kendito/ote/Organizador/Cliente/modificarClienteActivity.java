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
import com.kendito.ote.databinding.ActivityModificarClienteBinding;
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

public class modificarClienteActivity extends AppCompatActivity {

    public static final String URL = "http://kenditobd.ddns.net/ote/";
    private ProgressDialog progress;

    private List<String> values = new ArrayList<>();
    private List<String> valuesid = new ArrayList<>();
    private List<Result> sitios;
    private ActivityModificarClienteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModificarClienteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent() != null && getIntent().getExtras() != null) {
            String rutRecibido = getIntent().getExtras().getString("Rutcliente");
            String nombreRecibido = getIntent().getExtras().getString("nombreCliente");
            String contrasenaRecibido = getIntent().getExtras().getString("contrasenaCliente");

            if (rutRecibido != null && rutRecibido.contains("-")) {
                String[] rutSeparar = rutRecibido.split("-");
                binding.rutClienteModificar.setText(rutSeparar[0]);
                binding.txtEditDVM.setText(rutSeparar[1]);
            }
            binding.nombreClienteModificar.setText(nombreRecibido);
            binding.contrasenaClienteModificar.setText(contrasenaRecibido);
        }

        binding.btnModifciarCliente.setOnClickListener(view -> modificarCliente());
        llenarDatos();
    }

    private void modificarCliente() {
        int index = binding.SpiMCSitio.getSelectedItemPosition();
        if (index < 0 || index >= valuesid.size()) {
            Toast.makeText(this, "Seleccione un sitio válido", Toast.LENGTH_SHORT).show();
            return;
        }

        String rut = binding.rutClienteModificar.getText().toString().trim();
        String dv = binding.txtEditDVM.getText().toString().trim();
        String rutCompleto = rut + "-" + dv;
        String nombre = binding.nombreClienteModificar.getText().toString().trim();
        String contrasena = binding.contrasenaClienteModificar.getText().toString().trim();
        String idTipoPersonal = "3";
        String idSitio = valuesid.get(index);

        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(getApplicationContext(), "Ingresar nombre nuevo del cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(contrasena)) {
            Toast.makeText(getApplicationContext(), "Ingresar contraseña nueva del cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Modificando cliente...");
        progress.show();

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<valueMensaje> call = api.modificarCliente(rutCompleto, idTipoPersonal, idSitio, nombre, contrasena);

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
                Toast.makeText(getApplicationContext(), "Algo ocurrio durante la modificacion del cliente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void llenarDatos() {
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

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(modificarClienteActivity.this, R.layout.spinner_item, values);
                    binding.SpiMCSitio.setAdapter(adapter);
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
}
