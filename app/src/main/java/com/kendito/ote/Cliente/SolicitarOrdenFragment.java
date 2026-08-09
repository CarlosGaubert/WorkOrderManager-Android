package com.kendito.ote.Cliente;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kendito.ote.api.RegisterAPI;
import com.kendito.ote.databinding.FragmentSolicitarOrdenBinding;
import com.kendito.ote.model.valueMensaje;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.kendito.ote.Cliente.OrdenesYCliente.verMisOrdenesFragment;

public class SolicitarOrdenFragment extends Fragment {

    public static final String URL = "http://kenditobd.ddns.net/ote/";
    private ProgressDialog progress;

    private String rutCliente = null;
    private String nombre = null;
    private FragmentSolicitarOrdenBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSolicitarOrdenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null && getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
            rutCliente = getActivity().getIntent().getExtras().getString("rut");
            nombre = getActivity().getIntent().getExtras().getString("nombre");
            binding.nombreCliente.setText(nombre);
        }

        binding.btnSolicitarOrden.setOnClickListener(v -> ingresarOrden());
    }

    private void ingresarOrden() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date now = new Date();
        final String strDate = sdfDate.format(now);

        if (!TextUtils.isEmpty(binding.txtOrden.getText())) {
            if (getContext() == null) return;

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("¿ Desea pedir esta orden ?");
            builder.setTitle("Orden de trabajo");

            builder.setPositiveButton("Pedir orden", (dialogInterface, i) -> {
                progress = new ProgressDialog(getContext());
                progress.setCancelable(false);
                progress.setMessage("Ingresando orden...");
                progress.show();

                Gson gson = new GsonBuilder().setLenient().create();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                RegisterAPI api = retrofit.create(RegisterAPI.class);
                Call<valueMensaje> call = api.ingresarSolicitud(rutCliente, strDate, binding.txtOrden.getText().toString());

                call.enqueue(new Callback<valueMensaje>() {
                    @Override
                    public void onResponse(Call<valueMensaje> call, Response<valueMensaje> response) {
                        progress.dismiss();
                        if (response.body() != null) {
                            String value = response.body().getValue();
                            if ("1".equals(value)) {
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                                binding.txtOrden.setText("");
                                if (verMisOrdenesFragment != null) {
                                    verMisOrdenesFragment.obtenerOrdenes();
                                }
                                if (getView() != null) {
                                    Snackbar.make(getView(), Html.fromHtml("<font color=\"#ffffff\">Revisa nueva orden en la pestaña de Ordenes</font>"), Snackbar.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<valueMensaje> call, Throwable t) {
                        progress.dismiss();
                        Toast.makeText(getContext(), "Fallo conexion a internet", Toast.LENGTH_LONG).show();
                    }
                });
            });

            builder.setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.cancel());
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Toast.makeText(getContext(), "Llenar solicitud para pedir una orden de trabajo", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
