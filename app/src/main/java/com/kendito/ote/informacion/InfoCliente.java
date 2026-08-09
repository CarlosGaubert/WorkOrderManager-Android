package com.kendito.ote.informacion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kendito.ote.Organizador.Cliente.gestionarClienteActivity;
import com.kendito.ote.api.RegisterAPI;
import com.kendito.ote.databinding.ActivityInfoClienteBinding;
import com.kendito.ote.model.ClienteG;
import com.kendito.ote.model.ResultClienteG;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InfoCliente extends AppCompatActivity {

    public static final String URL = "http://kenditobd.ddns.net/ote/";
    private ProgressDialog progress;
    private ActivityInfoClienteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoClienteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent() != null && getIntent().getExtras() != null) {
            String rut = getIntent().getExtras().getString("rutCliente");
            if (rut != null) {
                cargarDato(rut);
            }
        }
    }

    public void cargarDato(String rut) {
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Cargando datos del cliente seleccionado...");
        progress.show();

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<ClienteG> call = api.verClienteG(rut);

        call.enqueue(new Callback<ClienteG>() {
            @Override
            public void onResponse(Call<ClienteG> call, Response<ClienteG> response) {
                progress.dismiss();
                if (response.body() != null && response.body().getValueCliente() != null && response.body().getValueCliente() == 1) {
                    List<ResultClienteG> cliente = response.body().getResultClienteG();
                    if (cliente != null && !cliente.isEmpty()) {
                        binding.rutClienteInfo.setText(cliente.get(0).getRUTCLIENTE());
                        binding.nombreClienteInfo.setText(cliente.get(0).getNOMBRECLIENTE());
                        binding.nombresitioInfo.setText(cliente.get(0).getNOMBRESITIO());
                        binding.nombreAreaInfo.setText(cliente.get(0).getNOMBREAREA());
                        binding.tipoPersonalInfo.setText(cliente.get(0).getCARGO());
                        binding.contrasenaInfo.setText(cliente.get(0).getCONTRASENA());
                    }
                }
            }

            @Override
            public void onFailure(Call<ClienteG> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Fallo conexion a internet", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), gestionarClienteActivity.class);
                startActivity(intent);
            }
        });
    }
}
