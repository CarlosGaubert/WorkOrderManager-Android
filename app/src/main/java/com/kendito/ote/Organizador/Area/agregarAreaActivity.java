package com.kendito.ote.Organizador.Area;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kendito.ote.api.RegisterAPI;
import com.kendito.ote.databinding.ActivityAgregarAreaBinding;
import com.kendito.ote.model.valueMensaje;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class agregarAreaActivity extends AppCompatActivity {

    public static final String URL = "http://kenditobd.ddns.net/ote/";
    private ProgressDialog progress;
    private ActivityAgregarAreaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgregarAreaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        binding.btnAgregarArea.setOnClickListener(view -> ingresarArea());
    }

    private void ingresarArea() {
        String nombreArea = binding.txtNombreAreaAgregar.getText().toString().trim();

        if (TextUtils.isEmpty(nombreArea)) {
            Toast.makeText(getApplicationContext(), "Ingresar nombre de area", Toast.LENGTH_SHORT).show();
            return;
        }

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Agregando area...");
        progress.show();

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<valueMensaje> call = api.ingresarArea(nombreArea);

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
                Toast.makeText(getApplicationContext(), "Algo ocurrio durante la grabacion", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
