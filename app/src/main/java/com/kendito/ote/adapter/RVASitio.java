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
import com.kendito.ote.informacion.InfoSitio;
import com.kendito.ote.model.ResultSitioG;
import com.kendito.ote.model.valueMensaje;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.kendito.ote.Organizador.Sitio.gestionarSitioActivity.URL;

public class RVASitio extends RecyclerView.Adapter<RVASitio.SitiosViewHolder> {

    private ProgressDialog progress;
    private List<ResultSitioG> sitios;

    public static String idSitioPasado = null;
    public static String nombreSitioPasado = null;
    public static String nombreAreaPasado = null;

    int selected_position = -1;
    String dobleClick = null;

    public RVASitio(List<ResultSitioG> sitios) {
        this.sitios = sitios;
    }

    @NonNull
    @Override
    public SitiosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sitio, parent, false);
        return new SitiosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SitiosViewHolder sitiosViewHolder, int position) {
        ResultSitioG sitio = sitios.get(position);

        sitiosViewHolder.tvNombreArea.setText(sitio.getNOMBREAREA());
        sitiosViewHolder.tvNombreSitio.setText(sitio.getNOMBRESITIO());
        sitiosViewHolder.tvIdSitio.setText(sitio.getIDSITIO());

        sitiosViewHolder.tablaSitios.setBackgroundResource(selected_position == position ? R.drawable.cell_shape_seleccionado : R.drawable.cell_shape);
    }

    @Override
    public int getItemCount() {
        return sitios != null ? sitios.size() : 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewAttachedToWindow(@NonNull SitiosViewHolder holder) {
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
                    if (position >= 0 && position < sitios.size()) {
                        sitios.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            });
            animation.start();
        } else {
            if (position >= 0 && position < sitios.size()) {
                sitios.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    public class SitiosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvIdSitio;
        private TextView tvNombreSitio;
        private TextView tvNombreArea;
        private Button btnEliminarSitio;
        private TableLayout tablaSitios;

        public SitiosViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvIdSitio = itemView.findViewById(R.id.tvIdSitio);
            tvNombreSitio = itemView.findViewById(R.id.tvNombreSitio);
            tvNombreArea = itemView.findViewById(R.id.tvNombreArea);
            btnEliminarSitio = itemView.findViewById(R.id.btnEliminarSitio);
            tablaSitios = itemView.findViewById(R.id.tablaSitios);

            btnEliminarSitio.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION || position >= sitios.size()) return;
                final ResultSitioG sitio = sitios.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setMessage("¿ Deseas eliminar este sitio[" + sitio.getNOMBRESITIO() + "] ?\n" +
                        "[PRECAUCION]Si se elimina este sitio se eliminaran tambien los cliente relacionados a este.");
                builder.setTitle("Eliminado");

                builder.setPositiveButton("Eliminar", (dialog, which) -> {
                    progress = new ProgressDialog(itemView.getContext());
                    progress.setCancelable(false);
                    progress.setMessage("Eliminando sitio");
                    progress.show();

                    Gson gson = new GsonBuilder().setLenient().create();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                    RegisterAPI api = retrofit.create(RegisterAPI.class);
                    Call<valueMensaje> call = api.eliminarSitio(sitio.getIDSITIO());

                    call.enqueue(new Callback<valueMensaje>() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onResponse(Call<valueMensaje> call, Response<valueMensaje> response) {
                            progress.dismiss();
                            if (response.body() != null) {
                                String value = response.body().getValue();
                                String message = response.body().getMessage();

                                if ("1".equals(value)) {
                                    Toast.makeText(itemView.getContext(), message, Toast.LENGTH_SHORT).show();
                                    animateCircularDelete(itemView, getAdapterPosition());
                                } else {
                                    Toast.makeText(itemView.getContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<valueMensaje> call, Throwable t) {
                            progress.dismiss();
                            Toast.makeText(itemView.getContext(), "Ocurrio un problema al conectar con el host", Toast.LENGTH_SHORT).show();
                        }
                    });
                });

                builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                AlertDialog dialog = builder.create();
                dialog.show();
            });

            tvNombreArea.setSelected(true);
            tvNombreSitio.setSelected(true);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            notifyItemChanged(selected_position);
            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);

            Context context = v.getContext();
            int position = getAdapterPosition();
            if (position < 0 || position >= sitios.size()) return;
            ResultSitioG sitio = sitios.get(position);

            idSitioPasado = sitio.getIDSITIO();
            nombreSitioPasado = sitio.getNOMBRESITIO();
            nombreAreaPasado = sitio.getNOMBREAREA();

            if (sitio.getIDSITIO() != null && sitio.getIDSITIO().equals(dobleClick)) {
                Intent intent = new Intent(context.getApplicationContext(), InfoSitio.class);
                intent.putExtra("idSitio", sitio.getIDSITIO());
                intent.putExtra("nombreSitio", sitio.getNOMBRESITIO());
                intent.putExtra("nombreArea", sitio.getNOMBREAREA());
                context.startActivity(intent);
            } else {
                Snackbar.make(itemView, Html.fromHtml("<font color=\"#ffffff\">Presiona denuevo para ver detalles</font>"), Snackbar.LENGTH_SHORT).show();
            }

            dobleClick = sitio.getIDSITIO();
        }
    }
}
