package com.kendito.ote.Organizador.Personal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kendito.ote.Organizador.menuOrganizadorActivity;
import com.kendito.ote.R;
import com.kendito.ote.api.RegisterAPI;
import com.kendito.ote.databinding.ActivityGestionarPersonalBinding;
import com.kendito.ote.model.Personal;
import com.kendito.ote.model.ResultPersonal;
import com.kendito.ote.model.valueMensaje;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class gestionarPersonalActivity extends AppCompatActivity {

    public static final String URL = "http://kenditobd.ddns.net/ote/";
    private ProgressDialog progress;

    private List<ResultPersonal> personal;
    private List<String> values = new ArrayList<>();
    private List<String> valuesid = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ActivityGestionarPersonalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGestionarPersonalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        binding.btnPersonalModificar.setOnClickListener(view -> {
            if (binding.spinnerSelPersonal.getSelectedItemPosition() >= 0 && binding.spinnerSelPersonal.getSelectedItemPosition() < valuesid.size()) {
                Intent intent = new Intent(this, modificarPersonalActivity.class);
                intent.putExtra("rutPersonal", valuesid.get(binding.spinnerSelPersonal.getSelectedItemPosition()));
                startActivity(intent);
            }
        });

        binding.btnPersonalAgregar.setOnClickListener(view -> {
            Intent intent = new Intent(this, agregarPersonalActivity.class);
            if (binding.spinnerSelPersonal.getSelectedItem() != null) {
                intent.putExtra("spinerPersonal", binding.spinnerSelPersonal.getSelectedItem().toString());
            }
            startActivity(intent);
        });

        binding.btnPersonalEliminar.setOnClickListener(view -> mensajeEliminar());

        binding.spinnerSelPersonal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (personal != null && position >= 0 && position < personal.size()) {
                    binding.rutPersonal.setText(personal.get(position).getRUTPERSONAL());
                    binding.nombrePersonal.setText(personal.get(position).getNOMBRE());

                    if ("1".equals(personal.get(position).getIDTIPOPERSONAL())) {
                        binding.tipoPersonal.setText("Organizador");
                    } else {
                        binding.tipoPersonal.setText("Personal");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        llenarPersonal();
        binding.nombrePersonal.setSelected(true);
    }

    public void llenarPersonal() {
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
        Call<Personal> call = api.verPersonal();

        call.enqueue(new Callback<Personal>() {
            @Override
            public void onResponse(Call<Personal> call, Response<Personal> response) {
                progress.dismiss();
                if (response.body() != null && response.body().getValuePersonal() == 1) {
                    personal = response.body().getResultPersonal();
                    values.clear();
                    valuesid.clear();

                    for (int i = 0; i < personal.size(); i++) {
                        values.add(personal.get(i).getNOMBRE());
                        valuesid.add(personal.get(i).getRUTPERSONAL());
                    }

                    adapter = new ArrayAdapter<>(gestionarPersonalActivity.this, R.layout.spinner_item, values);
                    binding.spinnerSelPersonal.setAdapter(adapter);
                    binding.nombrePersonal.setSelected(true);
                }
            }

            @Override
            public void onFailure(Call<Personal> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Fallo la conexion a internet", Toast.LENGTH_SHORT).show();
                volver();
            }
        });
    }

    public void mensajeEliminar() {
        if (binding.spinnerSelPersonal.getSelectedItemPosition() < 0 || binding.spinnerSelPersonal.getSelectedItemPosition() >= valuesid.size()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿ Deseas eliminar este personal ?");
        builder.setTitle("Eliminado");

        builder.setPositiveButton("Si", (dialog, which) -> {
            final int index = binding.spinnerSelPersonal.getSelectedItemPosition();
            String personalRut = valuesid.get(index);
            final String indexf = binding.spinnerSelPersonal.getSelectedItem().toString();

            progress.setMessage("Eliminando personal...");
            progress.show();

            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            RegisterAPI api = retrofit.create(RegisterAPI.class);
            Call<valueMensaje> call = api.eliminarPersonal(personalRut);

            call.enqueue(new Callback<valueMensaje>() {
                @Override
                public void onResponse(Call<valueMensaje> call, Response<valueMensaje> response) {
                    progress.dismiss();
                    if (response.body() != null) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        if ("1".equals(value)) {
                            adapter.remove(indexf);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(Call<valueMensaje> call, Throwable t) {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Fallo la conexion a internet", Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void volver() {
        Intent intent = new Intent(this, menuOrganizadorActivity.class);
        startActivity(intent);
    }
}
