package com.kendito.ote;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kendito.ote.Cliente.OrdenesYCliente;
import com.kendito.ote.Organizador.menuOrganizadorActivity;
import com.kendito.ote.Personal_Organizador.VerOrdenesPersonal;
import com.kendito.ote.api.RegisterAPI;
import com.kendito.ote.databinding.ActivityLoginBinding;
import com.kendito.ote.informacion.InfoOTE;
import com.kendito.ote.model.valueLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class loginActivity extends AppCompatActivity {

    // URL de servicio web
    public static final String URL = "http://kenditobd.ddns.net/ote/";

    // variables globales publicas
    public static String rutLoginPasado;
    public static String idTipoPersonalPasado;

    // variables globales
    private ProgressDialog progress;
    private String rut;
    private String contrasena;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSalir.setOnClickListener(v -> finish());
        binding.btnIngresar.setOnClickListener(v -> ingresarSistema());

        ActionBar actionBar = getSupportActionBar();
        try {
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    Common.currentToken = task.getResult();
                    Log.d("MY TOKEN", Common.currentToken);
                }
            });

            Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.vibrate(1000);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ingresarSistema() {
        // validar datos vacios
        if (TextUtils.isEmpty(binding.txtEditRut.getText()) || TextUtils.isEmpty(binding.txtEditDigitoVerificador.getText())) {
            Toast.makeText(getApplicationContext(), "Ingresar rut", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(binding.txtEditContrasena.getText())) {
            Toast.makeText(getApplicationContext(), "Ingresar contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // buscar rut y contraseña correspondiente en la base de datos
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Intentando ingresar por favor espere");
        progress.show();

        // datos temporales
        rut = binding.txtEditRut.getText().toString() + "-" + binding.txtEditDigitoVerificador.getText().toString();
        contrasena = binding.txtEditContrasena.getText().toString();

        // llamar consulta
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<valueLogin> call = api.verificarLoginPersonal(rut, contrasena);

        // ejecutar consulta
        call.enqueue(new Callback<valueLogin>() {
            @Override
            public void onResponse(Call<valueLogin> call, Response<valueLogin> response) {
                progress.dismiss();
                if (response.body() == null) {
                    Toast.makeText(getApplicationContext(), "Respuesta vacía del servidor", Toast.LENGTH_SHORT).show();
                    return;
                }

                String value = response.body().getValue();
                String nombre = response.body().getNombre();

                if ("1".equals(value)) {
                    rutLoginPasado = rut;
                    idTipoPersonalPasado = value;
                    Toast.makeText(getApplicationContext(), "Bienvenido " + nombre, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(loginActivity.this, menuOrganizadorActivity.class);
                    startActivity(intent);
                } else if ("2".equals(value)) {
                    rutLoginPasado = rut;
                    idTipoPersonalPasado = value;
                    Toast.makeText(getApplicationContext(), "Bienvenido " + nombre, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(loginActivity.this, VerOrdenesPersonal.class);
                    startActivity(intent);
                } else if ("3".equals(value)) {
                    rutLoginPasado = rut;
                    Toast.makeText(getApplicationContext(), "Bienvenido " + nombre, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(loginActivity.this, OrdenesYCliente.class);
                    intent.putExtra("rut", rut);
                    intent.putExtra("nombre", nombre);
                    startActivity(intent);
                } else if ("4".equals(value)) {
                    Toast.makeText(getApplicationContext(), "No existe este usuario", Toast.LENGTH_SHORT).show();
                } else if ("6".equals(value)) {
                    Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<valueLogin> call, Throwable t) {
                Log.e("Error", t.toString());
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Fallo conexión a internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ajustes) {
            Toast.makeText(getApplicationContext(), "Ir a ajustes", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_info) {
            Intent intent = new Intent(this, InfoOTE.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}