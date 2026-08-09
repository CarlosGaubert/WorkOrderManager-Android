package com.kendito.ote.Organizador.Sitio;

import android.app.ProgressDialog;
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
import com.kendito.ote.databinding.ActivityAgregarSitioBinding;
import com.kendito.ote.model.Area;
import com.kendito.ote.model.ResultArea;
import com.kendito.ote.model.valueMensaje;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class agregarSitioActivity extends AppCompatActivity {

    public static final String URL = "http://kenditobd.ddns.net/ote/";
    private ProgressDialog progress;
    private List<String> values = new ArrayList<>();
    private List<String> valuesid = new ArrayList<>();
    private List<ResultArea> areas;
    private ActivityAgregarSitioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgregarSitioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        binding.btnAgregarSitio.setOnClickListener(view -> ingresarSitio());
        llenarComboArea();
    }

    private void ingresarSitio() {
        int index = binding.SpiASarea.getSelectedItemPosition();
        if (index < 0 || index >= valuesid.size()) {
            Toast.makeText(getApplicationContext(), "Seleccione un área válida", Toast.LENGTH_SHORT).show();
            return;
        }

        String idArea = valuesid.get(index);
        String nombreSitio = binding.txtnombreSitioAgregar.getText().toString().trim();

        if (!TextUtils.isEmpty(nombreSitio)) {
            progress = new ProgressDialog(this);
            progress.setMessage("Agregando sitio nuevo...");
            progress.show();

            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            RegisterAPI api = retrofit.create(RegisterAPI.class);
            Call<valueMensaje> call = api.ingresarSitio(nombreSitio, idArea);

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
                    Toast.makeText(getApplicationContext(), "Error al conectar con el host", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "Ingresar nombre del sitio nuevo", Toast.LENGTH_SHORT).show();
        }
    }

    public void llenarComboArea() {
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Cargando datos de areas...");
        progress.show();

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<Area> call = api.verArea();

        call.enqueue(new Callback<Area>() {
            @Override
            public void onResponse(Call<Area> call, Response<Area> response) {
                progress.dismiss();
                if (response.body() != null && response.body().getValueArea() == 1) {
                    areas = response.body().getResultArea();
                    values.clear();
                    valuesid.clear();

                    for (int i = 0; i < areas.size(); i++) {
                        values.add(areas.get(i).getNOMBREAREA());
                        valuesid.add(areas.get(i).getIDAREA());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(agregarSitioActivity.this, R.layout.spinner_item, values);
                    binding.SpiASarea.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Area> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Fallo conexion con el host", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
