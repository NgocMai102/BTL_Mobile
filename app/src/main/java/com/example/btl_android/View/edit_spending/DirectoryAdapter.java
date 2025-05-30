package com.example.btl_android.View.edit_spending;

import static com.example.btl_android.Helper.ViewExtention.decodeBase64ToBitmap;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.Model.DanhMuc;
import com.example.btl_android.R;
import com.example.btl_android.databinding.ItemDirectoryBinding;

import java.util.ArrayList;
import java.util.List;

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.ViewHolder> {
    private static final String TAG = "DirectoryAdapter";
    private List<DanhMuc> listDirectory = new ArrayList<>();

    private int index = 0;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemDirectoryBinding binding;

        public ViewHolder(ItemDirectoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemDirectoryBinding binding = ItemDirectoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.imvDirectory.setImageBitmap(decodeBase64ToBitmap(listDirectory.get(position).getIcon()));
        holder.binding.tvNameDiretory.setText(listDirectory.get(position).getTenDanhMuc());
        holder.binding.getRoot().setOnClickListener(view -> {
            int i = position;
            index = i;
            notifyDataSetChanged();
        });
        if (position == index) {
            holder.binding.getRoot().setBackgroundResource(R.drawable.border_btn_tienthu);
        } else {
            holder.binding.getRoot().setBackgroundResource(R.drawable.border_directory);
        }
    }

    @Override
    public int getItemCount() {
        return listDirectory.size();
    }

    public void setAdapter(List<DanhMuc> list) {
        listDirectory.clear();
        listDirectory.addAll(list);
        notifyDataSetChanged();
    }

    public DanhMuc getDanhMuc() {
        return listDirectory.get(index);
    }

    public void setSelectedById(long idDanhMuc) {
        for (int i = 0; i < listDirectory.size(); i++) {
            if (listDirectory.get(i).getId() == idDanhMuc) {
                index = i;
                break;
            }
        }
        notifyDataSetChanged();
    }
}


