package com.kendito.ote.Organizador.Personal;

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
import com.kendito.ote.databinding.ActivityAgregarPersonalBinding;
import com.kendito.ote.model.valueMensaje;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class agregarPersonalActivity extends AppCompatActivity {

    public static final String URL = "http://kenditobd.ddns.net/ote/";
    private ProgressDialog progress;
    private final String[] datos = {"Selecciona tipo personal", "Organizador", "Personal"};
    private ActivityAgregarPersonalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgregarPersonalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, R.layout.spinner_item, datos);
        binding.SpiAPTipoPersonal.setAdapter(adaptador);

        binding.btnAgregarPersonal.setOnClickListener(v -> agregarPersonal());
    }

    private void agregarPersonal() {
        String rutP = binding.rutPersonalAgregar.getText().toString().trim();
        String dv = binding.txtEditDVA.getText().toString().trim();
        String rutCompleto = rutP + "-" + dv;
        String nombreP = binding.nombrePersonalAgregar.getText().toString().trim();
        String contrasena = binding.contrasenaPersonalAgregar.getText().toString().trim();
        int tipoPos = binding.SpiAPTipoPersonal.getSelectedItemPosition();
        String idTipoPersonal = Integer.toString(tipoPos);

        if (TextUtils.isEmpty(rutP) || TextUtils.isEmpty(dv)) {
            Toast.makeText(getApplicationContext(), "Ingresar rut", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(nombreP)) {
            Toast.makeText(getApplicationContext(), "Ingresar nombre del personal", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(contrasena)) {
            Toast.makeText(getApplicationContext(), "Ingresar contraseña para el personal", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tipoPos == 0) {
            Toast.makeText(getApplicationContext(), "Seleccionar un tipo de personal", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (!verificarRut(Integer.parseInt(rutP), dv)) {
                Toast.makeText(getApplicationContext(), "Rut no valido", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Rut numérico inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Intentando agregar personal...");
        progress.show();

        String personalAsignador = "1".equals(idTipoPersonal) ? "1" : "0";

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<valueMensaje> call = api.ingresarPersonal(rutCompleto, idTipoPersonal, nombreP, personalAsignador, contrasena);

        call.enqueue(new Callback<valueMensaje>() {
            @Override
            public void onResponse(Call<valueMensaje> call, Response<valueMensaje> response) {
                progress.dismiss();
                if (response.body() != null) {
                    String value = response.body().getValue();
                    String message = response.body().getMessage();

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    if ("1".equals(value)) {
                        binding.rutPersonalAgregar.setText("");
                        binding.txtEditDVA.setText("");
                        binding.nombrePersonalAgregar.setText("");
                        binding.contrasenaPersonalAgregar.setText("");
                    }
                }
            }

            @Override
            public void onFailure(Call<valueMensaje> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Algo ocurrio durante la grabacion", Toast.LENGTH_SHORT).show();
            }
        });
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
