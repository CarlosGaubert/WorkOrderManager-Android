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
import com.kendito.ote.databinding.ActivityModificarPersonalBinding;
import com.kendito.ote.model.Personal;
import com.kendito.ote.model.ResultPersonal;
import com.kendito.ote.model.valueMensaje;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class modificarPersonalActivity extends AppCompatActivity {

    public static final String URL = "http://kenditobd.ddns.net/ote/";
    private ProgressDialog progress;
    private final String[] datos = {"Selecciona tipo personal", "Organizador", "Personal"};
    private ActivityModificarPersonalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModificarPersonalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, R.layout.spinner_item, datos);
        binding.SpiMPTipoPersonal.setAdapter(adaptador);

        binding.btnModificarPersonal.setOnClickListener(view -> modificarPersonal());

        if (getIntent() != null && getIntent().getExtras() != null) {
            String rutPersonal = getIntent().getExtras().getString("rutPersonal");
            if (rutPersonal != null && rutPersonal.contains("-")) {
                String[] rutSeparar = rutPersonal.split("-");
                binding.rutPersonalModificar.setText(rutSeparar[0]);
                binding.txtEditDVM.setText(rutSeparar[1]);
                llamarDatosModificar(rutPersonal);
            }
        }
    }

    private void modificarPersonal() {
        String rut = binding.rutPersonalModificar.getText().toString().trim();
        String dv = binding.txtEditDVM.getText().toString().trim();
        String rutCompleto = rut + "-" + dv;
        String nombre = binding.nombrePersonalModifiar.getText().toString().trim();
        String contrasena = binding.contrasenaPersonalModificar.getText().toString().trim();
        int tipoPos = binding.SpiMPTipoPersonal.getSelectedItemPosition();
        String idTipoPersonal = Integer.toString(tipoPos);

        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(getApplicationContext(), "Ingresar nuevo nombre del personal", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(contrasena)) {
            Toast.makeText(getApplicationContext(), "Ingresar contrasena nueva", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tipoPos == 0) {
            Toast.makeText(getApplicationContext(), "Selecciona un tipo de personal", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (!verificarRut(Integer.parseInt(rut), dv)) {
                Toast.makeText(getApplicationContext(), "El rut ingresado no es valido", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Rut inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Intentando modificar personal...");
        progress.show();

        String personalAsignador = "1".equals(idTipoPersonal) ? "1" : "0";

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<valueMensaje> call = api.modificarPersonal(rutCompleto, idTipoPersonal, nombre, personalAsignador, contrasena);

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
                Toast.makeText(getApplicationContext(), "Algo ocurrio durante la modificacion del personal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void llamarDatosModificar(String rut) {
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Cargando datos de personal...");
        progress.show();

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<Personal> call = api.verPersonalEspecifico(rut);

        call.enqueue(new Callback<Personal>() {
            @Override
            public void onResponse(Call<Personal> call, Response<Personal> response) {
                progress.dismiss();
                if (response.body() != null && response.body().getValuePersonal() == 1) {
                    List<ResultPersonal> personalList = response.body().getResultPersonal();
                    if (personalList != null && !personalList.isEmpty()) {
                        binding.nombrePersonalModifiar.setText(personalList.get(0).getNOMBRE());
                        binding.contrasenaPersonalModificar.setText(personalList.get(0).getCONTRASENA());

                        if ("1".equals(personalList.get(0).getIDTIPOPERSONAL())) {
                            binding.SpiMPTipoPersonal.setSelection(1);
                        } else {
                            binding.SpiMPTipoPersonal.setSelection(2);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Personal> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Algo ocurrio durante la modificacion", Toast.LENGTH_SHORT).show();
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
