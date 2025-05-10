package com.example.btl_android.View.calendar;

import java.text.DecimalFormat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.btl_android.Model.SpendingInCalendar;
import com.example.btl_android.R;
import com.example.btl_android.View.edit_spending.EditSpendingActivity;
import com.example.btl_android.databinding.FragmentCalendarBinding;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {
    private FragmentCalendarBinding binding;
    private static final String TAG = "CalendarFragment";
    private List<SpendingInCalendar> listSpending;
    private List<SpendingInCalendar> listAdapter;
    private CalendarAdapter adapter;
    private Calendar calendar;
    private ViewPager2 viewPager2;
    private CalendarViewModel viewModel;
    private SharedPreferences sharedPreferences;

    DecimalFormat formatter = new DecimalFormat("#,###");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
        listAdapter = new ArrayList<>();
        listSpending = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
        binding = FragmentCalendarBinding.inflate(getLayoutInflater());
        sharedPreferences = getActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        adapter = new CalendarAdapter(new CalendarAdapter.ClickListener() {
            @Override
            public void onClickDelete(SpendingInCalendar spending, int position) {
                clickDelete();
            }
            @Override
            public void onClickSpending(SpendingInCalendar spendingInCalendar) {
                clickSpending(spendingInCalendar);
            }
        }, requireContext());
    }


    // hàm lấy dữ liệu bao gồm các giao dịch trong 1 ngày nhất định
    private void getData(int year, int month, int day) {
        viewModel.getDanhMuc(requireContext(), day, month, year);
        viewModel.danhMuc().observe(getViewLifecycleOwner(), new Observer<List<SpendingInCalendar>>() {
            @Override
            public void onChanged(List<SpendingInCalendar> spendingInCalendars) {
                listAdapter.clear();
                listSpending.clear();
                listSpending.addAll(spendingInCalendars);
                listAdapter.addAll(spendingInCalendars);
                String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);
                adapter.setAdapter(formattedDate, listAdapter);
            }
        });
    }

    // Hàm lắng nghe khi có sự thay đổi ngày tháng từ lịch
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewPager2 = getActivity().findViewById(R.id.viewpager);
        binding.calendarView2.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                Log.e(TAG, "onCreateView: " + i1);
                calendar.set(i, i1, i2);
                getData(i, i1 + 1, i2);
            }
        });

        binding.recyclerview.setAdapter(adapter);
        return binding.getRoot();
    }

    // Hàm get lại data khi có sự thay đổi từ ngày tháng
    @Override
    public void onResume() {
        getData(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        getTotal(calendar.get(Calendar.MONTH) + 1);
        super.onResume();
    }

    // Hàm get số tiền thu, tiền chi trong 1 tháng và đưa vào text view
    private void getTotal(int month) {
        viewModel.getGiaoDichChiThang(requireContext(), month);
        viewModel.getGiaoDichThuThang(requireContext(), month);
        viewModel.giaoDichChiThang().observe(this, new Observer<List<Long>>() {
            @Override
            public void onChanged(List<Long> longs) {
                long totalSpending = longs.stream().reduce(0L, Long::sum);
                binding.tvSpendingMoney.setText(formatter.format(totalSpending) + " đ");

                viewModel.giaoDichThuThang().observe(getViewLifecycleOwner(), new Observer<List<Long>>() {
                    @Override
                    public void onChanged(List<Long> longs) {
                        long totalRevenue = longs.stream().reduce(0L, Long::sum);
                        long total = totalRevenue - totalSpending;

                        binding.tvRevenue.setText(formatter.format(totalRevenue) + " đ");
                        binding.tvTotal.setText(formatter.format(total) + " đ");

                        if (total >= 0) {
                            binding.tvTotal.setTextColor(ContextCompat.getColor(getContext(), R.color.primary));
                        } else {
                            binding.tvTotal.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                        }
                    }
                });
            }

        });
    }

    // Hàm click vào 1 item ở list
    private void clickSpending(SpendingInCalendar spendingInCalendar) {
        Intent intent = new Intent(getActivity(), EditSpendingActivity.class);
        intent.putExtra("idGiaoDich", Long.valueOf(spendingInCalendar.getId()));
        startActivity(intent);
    }

    private void clickDelete() {
        showYesNoDialog();
    }

    private void showYesNoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Câu hỏi")
                .setMessage("Bạn có chắc chắn muốn xóa mục? Thao tác này không thể hoàn tác lại.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    try {
                        viewModel.delete(requireContext(), listSpending.get(adapter.getIndex()));
                        getData(
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH) + 1,
                                calendar.get(Calendar.DAY_OF_MONTH)
                        );
                    } catch (Exception e) {
                        Log.e(TAG, "showYesNoDialog: " + e.getMessage());

                    }

                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
