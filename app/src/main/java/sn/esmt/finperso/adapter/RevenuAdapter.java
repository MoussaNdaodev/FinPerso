package sn.esmt.finperso.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sn.esmt.finperso.R;
import sn.esmt.finperso.model.Revenu;

public class RevenuAdapter extends RecyclerView.Adapter<RevenuAdapter.ViewHolder> {

    private List<Revenu> list = new ArrayList<>();
    private final OnClick listener;

    public interface OnClick {
        void onClick(Revenu revenu);
    }

    public RevenuAdapter(OnClick listener) {
        this.listener = listener;
    }

    public void setData(List<Revenu> newData) {
        this.list = newData != null ? newData : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_revenu, parent, false);
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
        private final TextView tvMontant, tvSource, tvDescription, tvDate;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMontant = itemView.findViewById(R.id.tv_montant);
            tvSource = itemView.findViewById(R.id.tv_source);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvDate = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onClick(list.get(pos));
                }
            });
        }

        void bind(Revenu revenu) {
            DecimalFormat df = new DecimalFormat("#,###");
            tvMontant.setText("+" + df.format(revenu.montant) + " Fcfa");
            tvMontant.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.positive));
            tvSource.setText(revenu.source != null ? revenu.source : "");
            tvDescription.setText(revenu.description != null ? revenu.description : "");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
            tvDate.setText(sdf.format(new Date(revenu.date)));
        }
    }
}
