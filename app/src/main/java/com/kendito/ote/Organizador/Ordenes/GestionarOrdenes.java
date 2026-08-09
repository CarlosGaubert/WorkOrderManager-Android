package com.kendito.ote.Organizador.Ordenes;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kendito.ote.R;
import com.kendito.ote.adapter.RVAGestionOrdenes;
import com.kendito.ote.api.RegisterAPI;
import com.kendito.ote.databinding.ActivityGestionarOrdenesBinding;
import com.kendito.ote.model.GestionOrdenes;
import com.kendito.ote.model.Personal;
import com.kendito.ote.model.ResultGestionOrdenes;
import com.kendito.ote.model.ResultPersonal;
import com.kendito.ote.model.valueMensaje;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.kendito.ote.adapter.RVAGestionOrdenes.codigoOrdenPasado;
import static com.kendito.ote.adapter.RVAGestionOrdenes.posicionPasado;

public class GestionarOrdenes extends AppCompatActivity {

    public static final String URL = "http://kenditobd.ddns.net/ote/";
    private ProgressDialog progress;

    private List<ResultGestionOrdenes> ordenes;
    private RVAGestionOrdenes adaptador;
    private List<ResultPersonal> personal;

    private List<String> values = new ArrayList<>();
    private List<String> valuesid = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ActivityGestionarOrdenesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGestionarOrdenesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerViewGestionarOrdenes.setLayoutManager(lim);

        binding.btnAsignarOrden.setOnClickListener(view -> asignarOrden());
        binding.btnEliminarOrden.setOnClickListener(view -> eliminarOrden());

        obtenerOrdenes();
        llenarPersonal();
    }

    private void asignarOrden() {
        if (binding.spinnerSelPersonal.getSelectedItemPosition() < 0 || binding.spinnerSelPersonal.getSelectedItemPosition() >= valuesid.size()) {
            Toast.makeText(getApplicationContext(), "Seleccione un personal válido", Toast.LENGTH_SHORT).show();
            return;
        }

        final String rut = valuesid.get(binding.spinnerSelPersonal.getSelectedItemPosition());
        final String codigoOrdenRecibido = codigoOrdenPasado;

        if (codigoOrdenRecibido != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿ Deseas asignar la orden[" + codigoOrdenRecibido + "] al personal " + binding.spinnerSelPersonal.getSelectedItem() + " ?");
            builder.setTitle("Asignar");

            builder.setPositiveButton("Asignar", (dialogInterface, i) -> {
                progress.setMessage("Asignando orden...");
                progress.show();

                Gson gson = new GsonBuilder().setLenient().create();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                RegisterAPI api = retrofit.create(RegisterAPI.class);
                Call<valueMensaje> call = api.asignarOrden(rut, codigoOrdenRecibido);

                call.enqueue(new Callback<valueMensaje>() {
                    @Override
                    public void onResponse(Call<valueMensaje> call, Response<valueMensaje> response) {
                        progress.dismiss();
                        if (response.body() != null) {
                            String value = response.body().getValue();
                            String message = response.body().getMessage();

                            if ("1".equals(value)) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                int posicionRecibido = posicionPasado;
                                RecyclerView.ViewHolder viewHolder = binding.recyclerViewGestionarOrdenes.findViewHolderForAdapterPosition(posicionRecibido);
                                if (viewHolder != null && adaptador != null) {
                                    adaptador.animateCircularDelete(viewHolder.itemView, posicionRecibido);
                                }
                                codigoOrdenPasado = null;
                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<valueMensaje> call, Throwable t) {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Fallo la conexion con el host", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            builder.setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.cancel());
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Toast.makeText(getApplicationContext(), "Seleccionar una orden", Toast.LENGTH_LONG).show();
        }
    }

    private void eliminarOrden() {
        final String codigoOrdenRecibido = codigoOrdenPasado;

        if (codigoOrdenRecibido != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿ Deseas eliminar la orden[" + codigoOrdenRecibido + "] ?");
            builder.setTitle("Eliminado");

            builder.setPositiveButton("Eliminar", (dialogInterface, i) -> {
                progress.setMessage("Eliminando orden...");
                progress.show();

                Gson gson = new GsonBuilder().setLenient().create();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                RegisterAPI api = retrofit.create(RegisterAPI.class);
                Call<valueMensaje> call = api.eliminarOrden(codigoOrdenRecibido);

                call.enqueue(new Callback<valueMensaje>() {
                    @Override
                    public void onResponse(Call<valueMensaje> call, Response<valueMensaje> response) {
                        progress.dismiss();
                        if (response.body() != null) {
                            String value = response.body().getValue();
                            String message = response.body().getMessage();

                            if ("1".equals(value)) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                int posicionRecibido = posicionPasado;
                                RecyclerView.ViewHolder viewHolder = binding.recyclerViewGestionarOrdenes.findViewHolderForAdapterPosition(posicionRecibido);
                                if (viewHolder != null && adaptador != null) {
                                    adaptador.animateCircularDelete(viewHolder.itemView, posicionRecibido);
                                }
                                codigoOrdenPasado = null;
                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<valueMensaje> call, Throwable t) {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Fallo la conexion con el host", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            builder.setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.cancel());
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Toast.makeText(getApplicationContext(), "Seleccionar una orden", Toast.LENGTH_LONG).show();
        }
    }

    public void obtenerOrdenes() {
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Cargando ordenes de trabajo...");
        progress.show();

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<GestionOrdenes> call = api.verGestionOrdenes();

        call.enqueue(new Callback<GestionOrdenes>() {
            @Override
            public void onResponse(Call<GestionOrdenes> call, Response<GestionOrdenes> response) {
                progress.dismiss();
                if (response.body() != null && response.body().getValue() == 1) {
                    ordenes = response.body().getResultGestionOrdenes();
                    inicializarAdaptador();
                }
            }

            @Override
            public void onFailure(Call<GestionOrdenes> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Hubo un problema con el host :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void inicializarAdaptador() {
        adaptador = new RVAGestionOrdenes(ordenes);
        binding.recyclerViewGestionarOrdenes.setAdapter(adaptador);
    }

    public void llenarPersonal() {
        if (progress == null) {
            progress = new ProgressDialog(this);
            progress.setCancelable(false);
        }
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

                    adapter = new ArrayAdapter<>(GestionarOrdenes.this, R.layout.spinner_item, values);
                    binding.spinnerSelPersonal.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Personal> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Fallo la conexion a internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
