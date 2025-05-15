package com.example.btl_android.View.event;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.Model.ChuKy;
import com.example.btl_android.R;
import com.example.btl_android.Model.SuKien;

import java.util.List;
import java.util.Map;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<SuKien> suKienList;
    private OnEventActionListener listener;

    private Map<Integer, String> danhMucMap;

    public interface OnEventActionListener {
        void onEdit(SuKien suKien);
        void onDelete(SuKien suKien);
    }

    public EventAdapter(List<SuKien> suKienList, Map<Integer, String> danhMucMap, OnEventActionListener listener) {
        this.suKienList = suKienList;
        this.danhMucMap = danhMucMap;
        this.listener = listener;
    }

    public void setSuKienList(List<SuKien> newList) {
        this.suKienList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        SuKien suKien = suKienList.get(position);
        holder.name.setText(suKien.getTenSuKien());
        holder.date.setText(suKien.getNgayBatDau());
        holder.time.setText(suKien.getSoTien().toString());

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(suKien));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(suKien));
        ChuKy chuKy = suKien.getChuKy();
        if (chuKy != null) {
            holder.chuKy.setText("Chu kỳ: " + chuKy.toString()); // hoặc kiểu hiển thị khác tùy bạn
        } else {
            holder.chuKy.setText("Không có chu kỳ");
        }
        String danhMucName = danhMucMap.get(suKien.getIdDanhMuc());
        holder.tvDanhMuc.setText(danhMucName != null ? danhMucName : "Không rõ");
    }

    @Override
    public int getItemCount() {
        return suKienList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, time, type;
        ImageView btnEdit, btnDelete;
        TextView chuKy, tvDanhMuc;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.event_name);
            date = itemView.findViewById(R.id.event_date);
            time = itemView.findViewById(R.id.event_time);
            type = itemView.findViewById(R.id.event_type);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            chuKy = itemView.findViewById(R.id.event_chuky);
            tvDanhMuc = itemView.findViewById(R.id.tv_danhmuc_name);
        }
    }
}

