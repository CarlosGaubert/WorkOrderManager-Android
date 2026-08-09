package com.kendito.ote.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kendito.ote.R;
import com.kendito.ote.api.RegisterAPI;
import com.kendito.ote.informacion.InfoCliente;
import com.kendito.ote.model.ResultCliente;
import com.kendito.ote.model.valueMensaje;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.kendito.ote.Organizador.Cliente.gestionarClienteActivity.URL;

public class RVACliente extends RecyclerView.Adapter<RVACliente.ClienteViewHolder> {

    private ProgressDialog progress;
    private List<ResultCliente> clientes;

    public static String rutClientePasado = null;
    public static String nombreClientePasado = null;
    public static String contrasenaClientePasado = null;
    public static String sitioClientePasado = null;

    int selected_position = -1;
    String dobleClick = null;

    public RVACliente(List<ResultCliente> clientes) {
        this.clientes = clientes;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cliente, parent, false);
        return new ClienteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder clienteViewHolder, int position) {
        ResultCliente cliente = clientes.get(position);
        clienteViewHolder.tvRut.setText(cliente.getRUTCLIENTE());
        clienteViewHolder.tvNombre.setText(cliente.getNOMBRECLIENTE());

        clienteViewHolder.tablaCliente.setBackgroundResource(selected_position == position ? R.drawable.cell_shape_seleccionado : R.drawable.cell_shape);
    }

    @Override
    public int getItemCount() {
        return clientes != null ? clientes.size() : 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewAttachedToWindow(@NonNull ClienteViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        animateCircularReveal(holder.itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateCircularReveal(View view) {
        int centerX = 0;
        int centerY = 0;
        int startRadius = 0;
        int endRadius = Math.max(view.getWidth(), view.getHeight());
        if (endRadius > 0) {
            Animator animation = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
            view.setVisibility(View.VISIBLE);
            animation.start();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateCircularDelete(final View view, final int position) {
        int centerX = view.getWidth();
        int centerY = view.getHeight();
        int startRadius = view.getWidth();
        int endRadius = 0;
        if (startRadius > 0) {
            Animator animation = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                    if (position >= 0 && position < clientes.size()) {
                        clientes.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            });
            animation.start();
        } else {
            if (position >= 0 && position < clientes.size()) {
                clientes.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    public class ClienteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvRut;
        private TextView tvNombre;
        private Button btnEliminarCliente;
        private TableLayout tablaCliente;

        public ClienteViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvRut = itemView.findViewById(R.id.tvRut);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            btnEliminarCliente = itemView.findViewById(R.id.btnEliminarCliente);
            tablaCliente = itemView.findViewById(R.id.tablaCliente);

            btnEliminarCliente.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION || position >= clientes.size()) return;
                final ResultCliente cliente = clientes.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setMessage("¿ Deseas eliminar este cliente[" + cliente.getNOMBRECLIENTE() + "] ?");
                builder.setTitle("Eliminado");

                builder.setPositiveButton("Si", (dialog, which) -> {
                    progress = new ProgressDialog(itemView.getContext());
                    progress.setCancelable(false);
                    progress.setMessage("Eliminando cliente...");
                    progress.show();

                    Gson gson = new GsonBuilder().setLenient().create();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                    RegisterAPI api = retrofit.create(RegisterAPI.class);
                    Call<valueMensaje> call = api.eliminarCliente(cliente.getRUTCLIENTE());

                    call.enqueue(new Callback<valueMensaje>() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onResponse(Call<valueMensaje> call, Response<valueMensaje> response) {
                            progress.dismiss();
                            if (response.body() != null) {
                                String value = response.body().getValue();
                                String message = response.body().getMessage();

                                Toast.makeText(itemView.getContext(), message, Toast.LENGTH_SHORT).show();
                                if ("1".equals(value)) {
                                    animateCircularDelete(itemView, getAdapterPosition());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<valueMensaje> call, Throwable t) {
                            progress.dismiss();
                            Toast.makeText(itemView.getContext(), "Fallo la conexion con el host", Toast.LENGTH_SHORT).show();
                        }
                    });
                });

                builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                AlertDialog dialog = builder.create();
                dialog.show();
            });

            tvNombre.setSelected(true);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            notifyItemChanged(selected_position);
            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);

            Context context = v.getContext();
            int position = getAdapterPosition();
            if (position < 0 || position >= clientes.size()) return;
            ResultCliente cliente = clientes.get(position);

            rutClientePasado = cliente.getRUTCLIENTE();
            nombreClientePasado = cliente.getNOMBRECLIENTE();
            contrasenaClientePasado = cliente.getCONTRASENA();
            sitioClientePasado = cliente.getIDSITIO();

            if (cliente.getRUTCLIENTE() != null && cliente.getRUTCLIENTE().equals(dobleClick)) {
                Intent intent = new Intent(context.getApplicationContext(), InfoCliente.class);
                intent.putExtra("rutCliente", cliente.getRUTCLIENTE());
                context.startActivity(intent);
            } else {
                Snackbar.make(itemView, Html.fromHtml("<font color=\"#ffffff\">Presiona denuevo para ver detalles</font>"), Snackbar.LENGTH_SHORT).show();
            }

            dobleClick = cliente.getRUTCLIENTE();
        }
    }
}
