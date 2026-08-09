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
import com.kendito.ote.informacion.InfoArea;
import com.kendito.ote.model.ResultArea;
import com.kendito.ote.model.valueMensaje;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.kendito.ote.Organizador.Area.gestionarAreaActivity.URL;

public class RVAArea extends RecyclerView.Adapter<RVAArea.AreaViewHolder> {

    private ProgressDialog progress;
    private List<ResultArea> areas;

    public static String idAreaPasado = null;
    public static String nombreAreaPasado = null;

    int selected_position = -1;
    String dobleClick = null;

    public RVAArea(List<ResultArea> areas) {
        this.areas = areas;
    }

    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_area, parent, false);
        return new AreaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder areaViewHolder, int position) {
        ResultArea area = areas.get(position);
        areaViewHolder.tvIdArea.setText(area.getIDAREA());
        areaViewHolder.tvNombreArea.setText(area.getNOMBREAREA());

        areaViewHolder.tablaArea.setBackgroundResource(selected_position == position ? R.drawable.cell_shape_seleccionado : R.drawable.cell_shape);
    }

    @Override
    public int getItemCount() {
        return areas != null ? areas.size() : 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewAttachedToWindow(@NonNull AreaViewHolder holder) {
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
                    if (position >= 0 && position < areas.size()) {
                        areas.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            });
            animation.start();
        } else {
            if (position >= 0 && position < areas.size()) {
                areas.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    public class AreaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvIdArea;
        private TextView tvNombreArea;
        private Button btnEliminarCliente;
        private TableLayout tablaArea;

        public AreaViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvIdArea = itemView.findViewById(R.id.tvIdArea);
            tvNombreArea = itemView.findViewById(R.id.tvNombreArea);
            btnEliminarCliente = itemView.findViewById(R.id.btnEliminarCliente);
            tablaArea = itemView.findViewById(R.id.tablaArea);

            btnEliminarCliente.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION || position >= areas.size()) return;
                final ResultArea area = areas.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setMessage("¿ Deseas eliminar esta area  [" + area.getNOMBREAREA() + "] ?\n" +
                        "[PRECAUCION]Si se elimina esta area tambien se eliminaran los cliente y sitios relacionados a esta.");
                builder.setTitle("Eliminado");

                builder.setPositiveButton("Eliminar", (dialog, which) -> {
                    progress = new ProgressDialog(itemView.getContext());
                    progress.setCancelable(false);
                    progress.setMessage("Eliminando area");
                    progress.show();

                    Gson gson = new GsonBuilder().setLenient().create();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                    RegisterAPI api = retrofit.create(RegisterAPI.class);
                    Call<valueMensaje> call = api.eliminarArea(area.getIDAREA());

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
                            Toast.makeText(itemView.getContext(), "Hubo un problema con el host", Toast.LENGTH_SHORT).show();
                        }
                    });
                });

                builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                AlertDialog dialog = builder.create();
                dialog.show();
            });

            tvNombreArea.setSelected(true);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            notifyItemChanged(selected_position);
            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);

            Context context = v.getContext();
            int position = getAdapterPosition();
            if (position < 0 || position >= areas.size()) return;
            ResultArea area = areas.get(position);

            idAreaPasado = area.getIDAREA();
            nombreAreaPasado = area.getNOMBREAREA();

            if (area.getIDAREA() != null && area.getIDAREA().equals(dobleClick)) {
                Intent intent = new Intent(context, InfoArea.class);
                intent.putExtra("idAreaInfo", area.getIDAREA());
                intent.putExtra("nombreAreaInfo", area.getNOMBREAREA());
                context.startActivity(intent);
            } else {
                Snackbar.make(itemView, Html.fromHtml("<font color=\"#ffffff\">Presiona denuevo para ver detalles</font>"), Snackbar.LENGTH_SHORT).show();
            }

            dobleClick = area.getIDAREA();
        }
    }
}
