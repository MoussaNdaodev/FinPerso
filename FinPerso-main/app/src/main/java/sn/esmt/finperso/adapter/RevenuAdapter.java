package sn.esmt.finperso.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sn.esmt.finperso.R;
import sn.esmt.finperso.model.Revenu;

public class RevenuAdapter extends RecyclerView.Adapter<RevenuAdapter.RevenuViewHolder> {

    private List<Revenu> revenus = new ArrayList<>();
    private final OnRevenuClickListener listener;

    public interface OnRevenuClickListener {
        void onRevenuClick(Revenu revenu);
    }

    public RevenuAdapter(OnRevenuClickListener listener) {
        this.listener = listener;
    }

    public RevenuAdapter(OnRevenuClickListener listener, boolean enableClick) {
        this.listener = enableClick ? listener : null;
    }

    public void setData(List<Revenu> newData) {
        this.revenus = newData != null ? newData : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RevenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_revenu, parent, false);
        return new RevenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RevenuViewHolder holder, int position) {
        holder.bind(revenus.get(position));
    }

    @Override
    public int getItemCount() {
        return revenus != null ? revenus.size() : 0;
    }

    class RevenuViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvMontant, tvSource, tvDescription, tvDate;

        RevenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMontant = itemView.findViewById(R.id.tv_montant);
            tvSource = itemView.findViewById(R.id.tv_source);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvDate = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onRevenuClick(revenus.get(pos));
                }
            });
        }

        void bind(Revenu revenu) {
            DecimalFormat df = new DecimalFormat("#,###");
            tvMontant.setText("+" + df.format(revenu.montant) + " Fcfa");
            tvMontant.setTextColor(Color.parseColor("#4CAF50"));

            tvSource.setText(revenu.source != null ? revenu.source : "");
            tvDescription.setText(revenu.description != null ? revenu.description : "");

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
            tvDate.setText(sdf.format(new Date(revenu.date)));
        }
    }
}