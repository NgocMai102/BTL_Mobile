package com.example.btl_android.View.calendar;

import static com.example.btl_android.Helper.ViewExtention.decodeBase64ToBitmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.Model.DanhMuc;
import com.example.btl_android.Model.SpendingInCalendar;
import com.example.btl_android.R;
import com.example.btl_android.databinding.ItemSpendingBinding;
import com.example.btl_android.databinding.ItemTimeBinding;

import java.util.ArrayList;
import java.util.List;


import java.text.DecimalFormat;


public class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "CalendarAdapter";
    private final int typeTime = 0;
    private final int typeSpending = 1;
    private String time = "";
    private int index;
    private final List<SpendingInCalendar> listSpending;
    private Context context;
    private ClickListener clickListener;
    private MutableLiveData<DanhMuc> _danhMucThu = new MutableLiveData<>();

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_SPENDING = 1;

    DecimalFormat formatter = new DecimalFormat("#,###");

    public LiveData<DanhMuc> danhMucThu() {
        return _danhMucThu;
    }

    public CalendarAdapter(ClickListener onClickDelete, Context context) {
        this.clickListener = onClickDelete;
        this.context = context;
        this.listSpending = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        final int positionTime = 0;
        if (position == positionTime) {
            return typeTime;
        } else {
            return typeSpending;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTimeBinding bindingTime = ItemTimeBinding.inflate(layoutInflater, parent, false);
        ItemSpendingBinding bindingSpending = ItemSpendingBinding.inflate(layoutInflater, parent, false);
        if (viewType == typeTime) {
            return new TimeViewHolder(bindingTime);
        } else {
            return new SpendingViewHolder(bindingSpending);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int i = position;
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            TimeViewHolder timeViewHolder = (TimeViewHolder) holder;
            timeViewHolder.binding.getRoot().setText(time + "");
        } else {
            int spendingIndex = position - 1;
            SpendingViewHolder spendingViewHolder = (SpendingViewHolder) holder;
            SpendingInCalendar spending = listSpending.get(spendingIndex);

            spendingViewHolder.binding.tvNameDiretory.setText(spending.getTenDanhMuc());
            spendingViewHolder.binding.imvAvtSpending.setImageBitmap(decodeBase64ToBitmap(spending.getIcon()));

            spendingViewHolder.binding.imvDelete.setOnClickListener(view -> {
                clickListener.onClickDelete(spending, position);
            });

            spendingViewHolder.binding.getRoot().setOnClickListener(view -> {
                clickListener.onClickSpending(spending);
            });

            if (spending.getThuChi()) {
                spendingViewHolder.binding.tvSpendingMoney.setText("-" + formatter.format(spending.getTien()) + " đ");
            } else {
                spendingViewHolder.binding.tvSpendingMoney.setText("+" + formatter.format(spending.getTien()) + " đ");
            }

            spendingViewHolder.binding.tvNote.setText(spending.getGhiChu());
        }
    }


    @Override
    public int getItemCount() {
        return listSpending.size() + 1;
    }

    public void setAdapter(String Time, List<SpendingInCalendar> list) {
        time = Time;
        listSpending.clear();
        listSpending.addAll(list);
        notifyDataSetChanged();
    }

    public int getIndex() {
        return index;
    }

    public static class TimeViewHolder extends RecyclerView.ViewHolder {
        private final ItemTimeBinding binding;

        public TimeViewHolder(ItemTimeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class SpendingViewHolder extends RecyclerView.ViewHolder {
        private final ItemSpendingBinding binding;

        public SpendingViewHolder(ItemSpendingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    interface ClickListener {
        void onClickDelete(SpendingInCalendar spendingInCalendar, int position);
        void onClickSpending(SpendingInCalendar spendingInCalendar);
    }
}

