package com.example.btl_android.View.chart;

import static com.example.btl_android.Helper.ViewExtention.decodeBase64ToBitmap;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.Model.SpendingInChart;
import com.example.btl_android.databinding.ItemSpending2Binding;

import java.util.ArrayList;
import java.util.List;

public class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.ViewHoder> {
    private List<SpendingInChart> listAdapter = new ArrayList<>();

    public static class ViewHoder extends RecyclerView.ViewHolder {
        public ItemSpending2Binding binding;

        public ViewHoder(ItemSpending2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public ChartAdapter.ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSpending2Binding binding = ItemSpending2Binding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHoder(binding);
    }

    @Override
    public void onBindViewHolder(ChartAdapter.ViewHoder holder, int position) {
        holder.binding.imvAvtSpending.setImageBitmap(decodeBase64ToBitmap(listAdapter.get(position).getIcon()));
        holder.binding.tvNameDiretory.setText(listAdapter.get(position).getTenDanhMuc());
        holder.binding.tvSpendingMoney.setText(String.valueOf(listAdapter.get(position).getTien()) + " đ");
    }

    @Override
    public int getItemCount() {
        return listAdapter.size();
    }

    public void setAdapter(List<SpendingInChart> list) {
        listAdapter.clear();
        listAdapter.addAll(list);
        notifyDataSetChanged();
    }
}