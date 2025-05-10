package com.example.btl_android.View.chart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.btl_android.Model.SpendingInChart;
import com.example.btl_android.databinding.FragmentChartBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartFragment extends Fragment {
    private static final String TAG = "ChartFragment";
    private ChartViewModel viewModel;
    private FragmentChartBinding binding;
    private ChartAdapter adapter;
    private Calendar calendar;
    private int revenue;
    private int spending;
    private Pie pie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ChartViewModel.class);
        adapter = new ChartAdapter();
        calendar = Calendar.getInstance();
        revenue = 0;
        spending = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize chart after view is created
        pie = AnyChart.pie();
        binding.anyChart.setChart(pie);

        loadView();
        check();
    }

    private void loadView() {
        binding.recyclerview.setAdapter(adapter);
        binding.tapLayout.addTab(binding.tapLayout.newTab().setText("Chi tiêu"));
        binding.tapLayout.addTab(binding.tapLayout.newTab().setText("Thu nhập"));
        binding.tvMonth.setText("Tháng " + (calendar.get(Calendar.MONTH) + 1));

        binding.tapLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                check();
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {
                check();
            }
        });

        binding.imvBackNonth.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            binding.tvMonth.setText("Tháng " + (calendar.get(Calendar.MONTH) + 1));
            check();
        });

        binding.imvIncreaseMonth.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, 1);
            binding.tvMonth.setText("Tháng " + (calendar.get(Calendar.MONTH) + 1));
            check();
        });
    }

    private void spendingAdapter() {
        viewModel.get_SpendingInChartChi(requireContext(), calendar.get(Calendar.MONTH) + 1);
        viewModel.SpendingInChartChi().observe(getViewLifecycleOwner(), spendingInCharts -> {
            if (binding.anyChart == null || pie == null) return;

            spending = 0;
            List<DataEntry> dataEntries = new ArrayList<>();
            List<SpendingInChart> spendingInChart = new ArrayList<>();

            if (spendingInCharts.isEmpty()) {
                binding.anyChart.setVisibility(View.GONE);
                binding.tvNothing.setVisibility(View.VISIBLE);
            } else {
                Map<String, List<SpendingInChart>> listMap = spendingInCharts.stream()
                        .collect(Collectors.groupingBy(SpendingInChart::getTenDanhMuc));
                listMap.forEach((_tenDanhMuc, list) -> {
                    SpendingInChart s = new SpendingInChart(
                            list.stream().mapToLong(SpendingInChart::getTien).sum(),
                            _tenDanhMuc,
                            list.get(0).getIcon()
                    );
                    spendingInChart.add(s);
                    dataEntries.add(new ValueDataEntry(s.getTenDanhMuc(), s.getTien()));
                    spending -= s.getTien();
                });

                try {
                    pie.data(dataEntries);
                    binding.anyChart.setVisibility(View.VISIBLE);
                    binding.tvNothing.setVisibility(View.GONE);
                } catch (Exception e) {
                    Log.e(TAG, "Error updating chart data", e);
                }
            }
            adapter.setAdapter(spendingInChart);
            updateTotal();
        });
    }

    private void revenueAdapter() {
        viewModel.get_SpendingInChartThu(requireContext(), calendar.get(Calendar.MONTH) + 1);
        viewModel.SpendingInChartThu().observe(getViewLifecycleOwner(), spendingInCharts -> {
            if (binding.anyChart == null || pie == null) return;

            revenue = 0;
            List<DataEntry> dataEntries = new ArrayList<>();
            List<SpendingInChart> spendingInChart = new ArrayList<>();

            if (spendingInCharts.isEmpty()) {
                binding.anyChart.setVisibility(View.GONE);
                binding.tvNothing.setVisibility(View.VISIBLE);
            } else {
                Map<String, List<SpendingInChart>> listMap = spendingInCharts.stream()
                        .collect(Collectors.groupingBy(SpendingInChart::getTenDanhMuc));
                listMap.forEach((_tenDanhMuc, list) -> {
                    SpendingInChart s = new SpendingInChart(
                            list.stream().mapToLong(SpendingInChart::getTien).sum(),
                            _tenDanhMuc,
                            list.get(0).getIcon()
                    );
                    spendingInChart.add(s);
                    dataEntries.add(new ValueDataEntry(s.getTenDanhMuc(), s.getTien()));
                    revenue += s.getTien();
                });

                try {
                    pie.data(dataEntries);
                    binding.anyChart.setVisibility(View.VISIBLE);
                    binding.tvNothing.setVisibility(View.GONE);
                } catch (Exception e) {
                    Log.e(TAG, "Error updating chart data", e);
                }
            }
            adapter.setAdapter(spendingInChart);
            updateTotal();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (binding != null) {
            check();
        }
    }

    private void check() {
        if (binding.tapLayout.getSelectedTabPosition() == 0) {
            spendingAdapter();
        } else {
            revenueAdapter();
        }
    }

    private void updateTotal() {
        if (binding != null) {
            binding.tvSpending.setText(spending + " đ");
            binding.tvRevenue.setText(revenue + " đ");
            binding.tvTotal.setText((spending + revenue) + " đ");
        }
    }
}
