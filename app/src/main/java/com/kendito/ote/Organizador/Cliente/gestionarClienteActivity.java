package com.kendito.ote.Organizador.Cliente;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kendito.ote.R;
import com.kendito.ote.adapter.RVACliente;
import com.kendito.ote.api.RegisterAPI;
import com.kendito.ote.databinding.ActivityGestionarClienteBinding;
import com.kendito.ote.model.Cliente;
import com.kendito.ote.model.ResultCliente;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.kendito.ote.adapter.RVACliente.contrasenaClientePasado;
import static com.kendito.ote.adapter.RVACliente.nombreClientePasado;
import static com.kendito.ote.adapter.RVACliente.rutClientePasado;
import static com.kendito.ote.adapter.RVACliente.sitioClientePasado;

public class gestionarClienteActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static final String URL = "http://kenditobd.ddns.net/ote/";
    private ProgressDialog progress;
    private List<ResultCliente> clientes;
    private RVACliente adaptador;
    private ActivityGestionarClienteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGestionarClienteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        binding.btnClienteAgregar.setOnClickListener(view -> {
            Intent intent = new Intent(this, agregarClienteActivity.class);
            startActivity(intent);
        });

        binding.btnClienteModificar.setOnClickListener(view -> {
            if (rutClientePasado == null) {
                Toast.makeText(this, "Selecciona un cliente", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, modificarClienteActivity.class);
                intent.putExtra("Rutcliente", rutClientePasado);
                intent.putExtra("nombreCliente", nombreClientePasado);
                intent.putExtra("contrasenaCliente", contrasenaClientePasado);
                intent.putExtra("idSitio", sitioClientePasado);
                startActivity(intent);
            }
        });

        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerViewGestionarCliente.setLayoutManager(lim);

        obtenerCliente();
    }

    public void obtenerCliente() {
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Cargando datos de clientes...");
        progress.show();

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<Cliente> call = api.verCliente();

        call.enqueue(new Callback<Cliente>() {
            @Override
            public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                progress.dismiss();
                if (response.body() != null && response.body().getValueCliente() == 1) {
                    clientes = response.body().getResultCliente();
                    inicializarAdaptador();
                }
            }

            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Algo ocurrio mientras se cargaban datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void inicializarAdaptador() {
        adaptador = new RVACliente(clientes);
        binding.recyclerViewGestionarCliente.setAdapter(adaptador);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        if (searchView != null) {
            searchView.setQueryHint("Buscar por area");
            searchView.setIconified(true);
            searchView.setOnQueryTextListener(this);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        binding.recyclerViewGestionarCliente.setVisibility(View.GONE);

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<Cliente> call = api.verClienteE(s);

        call.enqueue(new Callback<Cliente>() {
            @Override
            public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                binding.recyclerViewGestionarCliente.setVisibility(View.VISIBLE);
                if (response.body() != null && response.body().getValueCliente() == 1) {
                    clientes = response.body().getResultCliente();
                    inicializarAdaptador();
                }
            }

            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {
                binding.recyclerViewGestionarCliente.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Fallo conexion a internet", Toast.LENGTH_LONG).show();
            }
        });

        return true;
    }
}
