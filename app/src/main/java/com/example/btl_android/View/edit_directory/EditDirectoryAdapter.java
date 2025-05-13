package com.example.btl_android.View.edit_directory;


import static com.example.btl_android.Helper.ViewExtention.decodeBase64ToBitmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.Model.DanhMuc;
import com.example.btl_android.databinding.ItemEditDirectoryBinding;

import java.util.ArrayList;
import java.util.List;

public class EditDirectoryAdapter extends RecyclerView.Adapter<EditDirectoryAdapter.ViewHolder> {
    private List<DanhMuc> listAdapter = new ArrayList<>();
    private ClickListener clickListener;
    private Context context;
    private int item = 0;

    public EditDirectoryAdapter(ClickListener clickListener, Context context) {
        this.clickListener = clickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public EditDirectoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEditDirectoryBinding binding = ItemEditDirectoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EditDirectoryAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(EditDirectoryAdapter.ViewHolder holder, int position) {
        holder.binding.tvNameDiretory.setText(listAdapter.get(position).getTenDanhMuc());
        holder.binding.imvIcon.setImageBitmap(decodeBase64ToBitmap(listAdapter.get(position).getIcon()));

        holder.binding.imvEdit.setOnClickListener(view -> {
            clickListener.onClickEdit(listAdapter.get(position));
        });

        holder.binding.imvDelete.setOnClickListener(view -> {
            clickListener.onClickDelete(listAdapter.get(position), position);
        });
    }

    @Override
    public int getItemCount() {
        return listAdapter.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemEditDirectoryBinding binding;

        public ViewHolder(ItemEditDirectoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setAdapter(List<DanhMuc> list) {
        listAdapter.clear();
        listAdapter.addAll(list);
        notifyDataSetChanged();
    }


    interface ClickListener {
        void onClickEdit(DanhMuc danhMuc);
        void onClickDelete(DanhMuc danhMuc, int position);
    }

    public DanhMuc getDanhMuc() {
        return listAdapter.get(item);
    }
}
