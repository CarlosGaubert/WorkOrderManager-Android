package com.kendito.ote.verMisOrdenes;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kendito.ote.adapter.RVAGestionOrdenes;
import com.kendito.ote.api.RegisterAPI;
import com.kendito.ote.databinding.FragmentVerMisOrdenesBinding;
import com.kendito.ote.model.GestionOrdenes;
import com.kendito.ote.model.ResultGestionOrdenes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.kendito.ote.loginActivity.rutLoginPasado;

public class VerMisOrdenesFragment extends Fragment {

    public static final String URL = "http://kenditobd.ddns.net/ote/";
    private List<ResultGestionOrdenes> ordenes;
    private RVAGestionOrdenes adaptador;
    private FragmentVerMisOrdenesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVerMisOrdenesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager lim = new LinearLayoutManager(getContext());
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerViewVerOrdenesF.setLayoutManager(lim);

        obtenerOrdenes();
    }

    public void obtenerOrdenes() {
        String rutRecibido = rutLoginPasado;

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<GestionOrdenes> call = api.verOrdenesEG(rutRecibido);

        call.enqueue(new Callback<GestionOrdenes>() {
            @Override
            public void onResponse(Call<GestionOrdenes> call, Response<GestionOrdenes> response) {
                try {
                    if (response.body() != null && response.body().getValue() == 1) {
                        ordenes = response.body().getResultGestionOrdenes();
                        inicializarAdaptador();
                    }
                } catch (Exception e) {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GestionOrdenes> call, Throwable t) {
                if (getView() != null) {
                    Snackbar.make(getView(), Html.fromHtml("<font color=\"#ffffff\">Fallo conexion</font>"), Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void inicializarAdaptador() {
        if (binding != null) {
            adaptador = new RVAGestionOrdenes(ordenes);
            binding.recyclerViewVerOrdenesF.setAdapter(adaptador);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
