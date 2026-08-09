package com.kendito.ote.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.kendito.ote.R;
import com.kendito.ote.informacion.InfoGestionOrdenes;
import com.kendito.ote.model.ResultGestionOrdenes;

import java.util.List;

public class RVAGestionOrdenes extends RecyclerView.Adapter<RVAGestionOrdenes.OrdenesViewHolder> {

    private List<ResultGestionOrdenes> ordenes;

    public static String codigoOrdenPasado = null;
    public static String ordenAceptadaPasado = null;
    public static int posicionPasado = 0;

    int selected_position = -1;
    String dobleClick = null;

    public RVAGestionOrdenes(List<ResultGestionOrdenes> ordenes) {
        this.ordenes = ordenes;
    }

    @NonNull
    @Override
    public OrdenesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_gestion_ordenes, parent, false);
        return new OrdenesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdenesViewHolder holder, int position) {
        ResultGestionOrdenes orden = ordenes.get(position);
        holder.tvcodigoOrden.setText(orden.getCODIGOORDEN());
        holder.tvCliente.setText(orden.getNOMBRECLIENTE());
        holder.tvDescripcion.setText(orden.getDESCRIPCIONORDEN());
        holder.tvFechaOrden.setText(orden.getFECHAPEDIDAORDEN());

        holder.tvFechaAOrden.setText(orden.getFECHAACEPTADAORDEN() == null ? "Sin datos" : orden.getFECHAACEPTADAORDEN());

        if ("0".equals(orden.getORDENACEPTADA())) {
            holder.tvEstado.setText("Orden sin aceptar");
        } else if ("0".equals(orden.getORDENREALIZADA()) && "1".equals(orden.getORDENACEPTADA())) {
            holder.tvEstado.setText("Orden en proceso");
        } else {
            holder.tvEstado.setText("Orden terminada");
        }

        if ("0".equals(orden.getORDENACEPTADA())) {
            holder.tablaOrdenes.setBackgroundResource(selected_position == position ? R.drawable.cell_shape_orden_nada_seleccionado : R.drawable.cell_shape_orden_nada);
        } else if ("0".equals(orden.getORDENREALIZADA())) {
            holder.tablaOrdenes.setBackgroundResource(selected_position == position ? R.drawable.cell_shape_orden_en_proceso_seleccionado : R.drawable.cell_shape_orden_en_proceso);
        } else {
            holder.tablaOrdenes.setBackgroundResource(selected_position == position ? R.drawable.cell_shape_orden_finalizada_seleccionado : R.drawable.cell_shape_orden_finalizada);
        }
    }

    @Override
    public int getItemCount() {
        return ordenes != null ? ordenes.size() : 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewAttachedToWindow(@NonNull OrdenesViewHolder holder) {
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
                    if (position >= 0 && position < ordenes.size()) {
                        ordenes.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            });
            animation.start();
        } else {
            if (position >= 0 && position < ordenes.size()) {
                ordenes.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    public class OrdenesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvcodigoOrden;
        private TextView tvCliente;
        private TextView tvDescripcion;
        private TextView tvFechaOrden;
        private TextView tvFechaAOrden;
        private TextView tvEstado;
        private TableLayout tablaOrdenes;

        public OrdenesViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvcodigoOrden = itemView.findViewById(R.id.tvcodigoOrden);
            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvFechaOrden = itemView.findViewById(R.id.tvFechaOrden);
            tablaOrdenes = itemView.findViewById(R.id.tablaOrdenes);
            tvFechaAOrden = itemView.findViewById(R.id.tvFechaAOrden);
            tvEstado = itemView.findViewById(R.id.tvEstado);

            tvCliente.setSelected(true);
            tvFechaOrden.setSelected(true);
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            notifyItemChanged(selected_position);
            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);

            Context context = view.getContext();
            int posicion = getAdapterPosition();
            if (posicion < 0 || posicion >= ordenes.size()) return;
            ResultGestionOrdenes orden = ordenes.get(posicion);

            codigoOrdenPasado = orden.getCODIGOORDEN();
            ordenAceptadaPasado = orden.getORDENACEPTADA();
            posicionPasado = posicion;

            if (orden.getCODIGOORDEN() != null && orden.getCODIGOORDEN().equals(dobleClick)) {
                Intent intent = new Intent(context, InfoGestionOrdenes.class);
                intent.putExtra("codigoOrden", orden.getCODIGOORDEN());
                intent.putExtra("rutCliente", orden.getRUTCLIENTE());
                intent.putExtra("nombreCliente", orden.getNOMBRECLIENTE());
                intent.putExtra("fechaOrden", orden.getFECHAPEDIDAORDEN());
                intent.putExtra("nombreSitio", orden.getNOMBRESITIO());
                intent.putExtra("nombreArea", orden.getNOMBREAREA());
                intent.putExtra("descripcion", orden.getDESCRIPCIONORDEN());
                context.startActivity(intent);
            } else {
                Snackbar.make(itemView, Html.fromHtml("<font color=\"#ffffff\">Presiona denuevo para ver detalles</font>"), Snackbar.LENGTH_SHORT).show();
            }

            dobleClick = orden.getCODIGOORDEN();
        }
    }
}
