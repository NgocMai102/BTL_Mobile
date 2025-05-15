package com.example.btl_android.View.search_transaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.Model.GiaoDich;
import com.example.btl_android.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<GiaoDich> giaoDichList;

    public TransactionAdapter(List<GiaoDich> giaoDichList) {
        this.giaoDichList = giaoDichList;
    }

    @NonNull
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.ViewHolder holder, int position) {
        GiaoDich gd = giaoDichList.get(position);
        holder.tvGhiChu.setText("Nội dung: " + gd.getGhiChu());

        // Format tiền tệ
        String formattedMoney = NumberFormat.getNumberInstance(Locale.US).format(gd.getTien()) + " đ";
        holder.tvSoTien.setText("Số tiền: " + formattedMoney);

        // ThuChi: true -> Chi; false -> Thu
        holder.tvThuChi.setText("Loại: " + (gd.getThuChi() ? "Chi" : "Thu"));
    }

    @Override
    public int getItemCount() {
        return giaoDichList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvGhiChu, tvSoTien, tvThuChi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGhiChu = itemView.findViewById(R.id.tvGhiChu);
            tvSoTien = itemView.findViewById(R.id.tvSoTien);
            tvThuChi = itemView.findViewById(R.id.tvThuChi);
        }
    }
}
