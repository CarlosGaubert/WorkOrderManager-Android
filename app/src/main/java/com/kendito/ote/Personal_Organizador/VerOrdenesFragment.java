package com.kendito.ote.Personal_Organizador;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kendito.ote.adapter.RVAGestionOrdenes;
import com.kendito.ote.api.RegisterAPI;
import com.kendito.ote.databinding.FragmentVerOrdenesBinding;
import com.kendito.ote.model.GestionOrdenes;
import com.kendito.ote.model.ResultGestionOrdenes;
import com.kendito.ote.model.valueMensaje;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.kendito.ote.Personal_Organizador.VerOrdenesPersonal.verMisOrdenesFragment;
import static com.kendito.ote.adapter.RVAGestionOrdenes.codigoOrdenPasado;
import static com.kendito.ote.adapter.RVAGestionOrdenes.ordenAceptadaPasado;
import static com.kendito.ote.adapter.RVAGestionOrdenes.posicionPasado;
import static com.kendito.ote.loginActivity.rutLoginPasado;

public class VerOrdenesFragment extends Fragment {

    public static final String URL = "http://kenditobd.ddns.net/ote/";
    private ProgressDialog progress;

    private List<ResultGestionOrdenes> ordenes;
    private RVAGestionOrdenes adaptador;
    private FragmentVerOrdenesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVerOrdenesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager lim = new LinearLayoutManager(getContext());
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerViewVerOrdenes.setLayoutManager(lim);

        binding.btnVerificarOrdenTerminada.setOnClickListener(v -> finalizarOrden());
        binding.btnAceptarOrden.setOnClickListener(v -> aceptarOrden());

        obtenerOrdenes();
    }

    private void finalizarOrden() {
        final String codigoOrdenRecibido = codigoOrdenPasado;
        final String ordenAceptadaRecibido = ordenAceptadaPasado;

        if (codigoOrdenRecibido != null) {
            if ("1".equals(ordenAceptadaRecibido)) {
                if (getContext() == null) return;

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("¿ Deseas finalizar la orden[" + codigoOrdenRecibido + "] ?");
                builder.setTitle("Orden de trabajo");

                builder.setPositiveButton("Finalizar orden", (dialogInterface, i) -> {
                    progress = new ProgressDialog(getContext());
                    progress.setMessage("Finalizando orden...");
                    progress.show();

                    Gson gson = new GsonBuilder().setLenient().create();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                    RegisterAPI api = retrofit.create(RegisterAPI.class);
                    Call<valueMensaje> call = api.terminarOrden(codigoOrdenRecibido);

                    call.enqueue(new Callback<valueMensaje>() {
                        @Override
                        public void onResponse(Call<valueMensaje> call, Response<valueMensaje> response) {
                            progress.dismiss();
                            if (response.body() != null) {
                                String value = response.body().getValue();
                                String message = response.body().getMessage();

                                if ("1".equals(value)) {
                                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                    int posicionRecibido = posicionPasado;
                                    RecyclerView.ViewHolder viewHolder = binding.recyclerViewVerOrdenes.findViewHolderForAdapterPosition(posicionRecibido);

                                    if (viewHolder != null && adaptador != null) {
                                        adaptador.animateCircularDelete(viewHolder.itemView, posicionRecibido);
                                    }
                                    codigoOrdenPasado = null;
                                    if (verMisOrdenesFragment != null) {
                                        verMisOrdenesFragment.obtenerOrdenes();
                                    }
                                } else {
                                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<valueMensaje> call, Throwable t) {
                            progress.dismiss();
                            Toast.makeText(getContext(), "Fallo la conexion con el host", Toast.LENGTH_SHORT).show();
                        }
                    });
                });

                builder.setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.cancel());
                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                Toast.makeText(getContext(), "Esta orden no se ha aceptado", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Seleccionar una orden", Toast.LENGTH_LONG).show();
        }
    }

    private void aceptarOrden() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date now = new Date();
        final String strDate = sdfDate.format(now);

        final String codigoOrdenRecibido = codigoOrdenPasado;

        if (codigoOrdenRecibido != null) {
            if (getContext() == null) return;

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("¿ Deseas aceptar la orden[" + codigoOrdenRecibido + "] ?");
            builder.setTitle("Orden de trabajo");

            builder.setPositiveButton("Aceptar orden", (dialogInterface, i) -> {
                progress = new ProgressDialog(getContext());
                progress.setMessage("Aceptando orden...");
                progress.show();

                Gson gson = new GsonBuilder().setLenient().create();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                RegisterAPI api = retrofit.create(RegisterAPI.class);
                Call<valueMensaje> call = api.aceptarOrden(codigoOrdenRecibido, strDate);

                call.enqueue(new Callback<valueMensaje>() {
                    @Override
                    public void onResponse(Call<valueMensaje> call, Response<valueMensaje> response) {
                        progress.dismiss();
                        if (response.body() != null) {
                            String value = response.body().getValue();
                            String message = response.body().getMessage();

                            if ("1".equals(value)) {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                obtenerOrdenes();
                                codigoOrdenPasado = null;
                                if (verMisOrdenesFragment != null) {
                                    verMisOrdenesFragment.obtenerOrdenes();
                                }
                            } else {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<valueMensaje> call, Throwable t) {
                        progress.dismiss();
                        Toast.makeText(getContext(), "Fallo la conexion con el host", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            builder.setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.cancel());
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Toast.makeText(getContext(), "Seleccionar una orden", Toast.LENGTH_LONG).show();
        }
    }

    public void obtenerOrdenes() {
        String rutRecibido = rutLoginPasado;

        if (getContext() == null) return;
        progress = new ProgressDialog(getContext());
        progress.setCancelable(false);
        progress.setMessage("Cargando ordenes de trabajo...");
        progress.show();

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<GestionOrdenes> call = api.verOrdenesE(rutRecibido);

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
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void inicializarAdaptador() {
        adaptador = new RVAGestionOrdenes(ordenes);
        binding.recyclerViewVerOrdenes.setAdapter(adaptador);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
