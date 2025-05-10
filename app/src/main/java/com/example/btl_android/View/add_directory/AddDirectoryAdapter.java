package com.example.btl_android.View.add_directory;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.databinding.ItemAddDirectoryBinding;
import com.example.btl_android.R;

import java.util.ArrayList;
import java.util.List;

public class AddDirectoryAdapter extends RecyclerView.Adapter<AddDirectoryAdapter.ViewHolder>{

    private final List<Integer> listDirectory = new ArrayList<>(List.of(R.drawable.icon_eat_and_drink,
            R.drawable.icon_daily_spending,
            R.drawable.icon_clothes,
            R.drawable.icon_cosmetics,
            R.drawable.icon_communication_fee,
            R.drawable.icon_medical,
            R.drawable.icon_education,
            R.drawable.icon_electricity_bill,
            R.drawable.icon_go,
            R.drawable.icon_bill_contact,
            R.drawable.icon_bill_home,
            R.drawable.icon_salary,
            R.drawable.icon_allowance,
            R.drawable.icon_bonus,
            R.drawable.icon_investment_money,
            R.drawable.icon_supplementary_income,
            R.drawable.icon_cup_of_coffee,
            R.drawable.icon_toilet_paper,
            R.drawable.icon_bear,
            R.drawable.icon_heart,
            R.drawable.icon_note,
            R.drawable.icon_setting,
            R.drawable.icon_game,
            R.drawable.icon_pet,
            R.drawable.icon_birthday_cake,
            R.drawable.icon_plane,
            R.drawable.icon_gym,
            R.drawable.icon_micro,
            R.drawable.icon_camera,
            R.drawable.icon_flowers));

    private int item = 0;

    @Override
    public AddDirectoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAddDirectoryBinding binding = ItemAddDirectoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(AddDirectoryAdapter.ViewHolder holder, int position) {
        holder.binding.imvDirectory.setImageResource(listDirectory.get(position));
        holder.binding.getRoot().setOnClickListener(view -> {
            int i = position;
            item = i;
            notifyDataSetChanged();
        });
        if (position == item) {
            holder.binding.getRoot().setBackgroundResource(R.drawable.border_btn_tienthu);
        } else {
            holder.binding.getRoot().setBackgroundResource(R.drawable.border_directory);
        }
    }

    @Override
    public int getItemCount() {
        return listDirectory.size();
    }

    public int getIcon() {
        return listDirectory.get(item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAddDirectoryBinding binding;

        public ViewHolder(ItemAddDirectoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
