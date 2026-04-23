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

public class DepenseAdapter extends RecyclerView.Adapter<DepenseAdapter.ViewHolder> {

    private List<DepenseAvecCategorie> list = new ArrayList<>();
    private final OnClick listener;

    public interface OnClick {
        void onClick(DepenseAvecCategorie depense);
    }

    public DepenseAdapter(OnClick listener) {
        this.listener = listener;
    }

    public void setData(List<DepenseAvecCategorie> newData) {
        this.list = newData != null ? newData : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_depense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvMontant, tvDescription, tvCategorie, tvDate;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMontant = itemView.findViewById(R.id.tv_montant);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvCategorie = itemView.findViewById(R.id.tv_categorie);
            tvDate = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onClick(list.get(pos));
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