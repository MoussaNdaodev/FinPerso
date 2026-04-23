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
import sn.esmt.finperso.model.DepenseAvecCategorie;

public class DepenseAdapter extends RecyclerView.Adapter<DepenseAdapter.DepenseViewHolder> {

    private List<DepenseAvecCategorie> depenses = new ArrayList<>();
    private final OnDepenseClickListener listener;

    public interface OnDepenseClickListener {
        void onDepenseClick(DepenseAvecCategorie depense);
    }

    public DepenseAdapter(OnDepenseClickListener listener) {
        this.listener = listener;
    }

    public DepenseAdapter(OnDepenseClickListener listener, boolean enableClick) {
        this.listener = enableClick ? listener : null;
    }

    public void setData(List<DepenseAvecCategorie> newData) {
        this.depenses = newData != null ? newData : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DepenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_depense, parent, false);
        return new DepenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepenseViewHolder holder, int position) {
        holder.bind(depenses.get(position));
    }

    @Override
    public int getItemCount() {
        return depenses != null ? depenses.size() : 0;
    }

    class DepenseViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvMontant, tvDescription, tvCategorie, tvDate;

        DepenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMontant = itemView.findViewById(R.id.tv_montant);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvCategorie = itemView.findViewById(R.id.tv_categorie);
            tvDate = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDepenseClick(depenses.get(pos));
                }
            });
        }

        void bind(DepenseAvecCategorie depense) {
            DecimalFormat df = new DecimalFormat("#,###");
            tvMontant.setText("-" + df.format(depense.montant) + " Fcfa");
            tvMontant.setTextColor(Color.parseColor("#F44336"));

            tvDescription.setText(depense.description != null ? depense.description : "");
            tvCategorie.setText(depense.categorieNom != null ? depense.categorieNom : "");

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
            tvDate.setText(sdf.format(new Date(depense.date)));
        }
    }
}