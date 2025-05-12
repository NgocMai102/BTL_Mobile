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

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
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
    private long revenue;
    private long spending;
    private Pie pie;
    private AnyChartView anyChartView;

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

        // Load view configurations first
        loadView();

        // Setup observers before initializing the chart
        setupObservers();

        // Setup chart view with delay to ensure view is properly initialized
        view.post(() -> {
            setupAnyChartView();
            fetchAllData();
        });
    }

    private void setupAnyChartView() {
        try {
            Log.d(TAG, "Setting up AnyChartView");
            anyChartView = binding.anyChart;

            // Ensure the view is not null
            if (anyChartView != null) {
                // Initialize AnyChart library
                APIlib.getInstance().setActiveAnyChartView(anyChartView);

                // Create pie chart only if it's null
                if (pie == null) {
                    pie = AnyChart.pie();
                    pie.title("Biểu đồ chi tiêu");
                    pie.labels().position("outside");
                    pie.legend().position("bottom");

                    // Set an empty dataset initially
                    List<DataEntry> emptyData = new ArrayList<>();
                    emptyData.add(new ValueDataEntry("Không có dữ liệu", 1));
                    pie.data(emptyData);

                    // Set the chart to the view
                    anyChartView.setChart(pie);
                }

                // Ensure visibility
                anyChartView.setVisibility(View.VISIBLE);
                Log.d(TAG, "AnyChartView setup complete");
            } else {
                Log.e(TAG, "AnyChartView is null");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting up AnyChartView", e);
        }
    }

    private void initializeChart() {
        try {
            // Ensure the chart view has the correct visibility and dimensions
            if (anyChartView != null) {
                // Log the view's dimensions
                Log.d(TAG, "AnyChartView Width: " + anyChartView.getWidth());
                Log.d(TAG, "AnyChartView Height: " + anyChartView.getHeight());

                // Ensure the view is visible and has dimensions
                anyChartView.setVisibility(View.VISIBLE);

                pie = AnyChart.pie();
                pie.title("Biểu đồ chi tiêu");
                pie.labels().position("outside");
                pie.legend().position("bottom");

                // Explicitly set chart
                anyChartView.setChart(pie);

                // Force layout update
                anyChartView.requestLayout();
                anyChartView.invalidate();
            } else {
                Log.e(TAG, "AnyChartView is null - cannot initialize chart");
            }
        } catch (Exception e) {
            Log.e(TAG, "Chart initialization error", e);
        }
    }

    private void loadView() {
        binding.recyclerview.setAdapter(adapter);
        binding.tapLayout.addTab(binding.tapLayout.newTab().setText("Chi tiêu"));
        binding.tapLayout.addTab(binding.tapLayout.newTab().setText("Thu nhập"));
        binding.tvMonth.setText("Tháng " + (calendar.get(Calendar.MONTH) + 1));

        binding.tapLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateCurrentTabData();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                updateCurrentTabData();
            }
        });

        binding.imvBackNonth.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            binding.tvMonth.setText("Tháng " + (calendar.get(Calendar.MONTH) + 1));
            fetchAllData();
        });

        binding.imvIncreaseMonth.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, 1);
            binding.tvMonth.setText("Tháng " + (calendar.get(Calendar.MONTH) + 1));
            fetchAllData();
        });
    }

    // Set up observers for spending and revenue data
    private void setupObservers() {
        // Observer for spending data
        viewModel.SpendingInChartChi().observe(getViewLifecycleOwner(), spendingInCharts -> {
            Log.d(TAG, "Spending data received: " + spendingInCharts.size() + " items");

            spending = 0;
            if (!spendingInCharts.isEmpty()) {
                Map<String, List<SpendingInChart>> listMap = spendingInCharts.stream()
                        .collect(Collectors.groupingBy(SpendingInChart::getTenDanhMuc));

                listMap.forEach((_tenDanhMuc, list) -> {
                    long totalAmount = list.stream().mapToLong(SpendingInChart::getTien).sum();
                    spending -= totalAmount;
                });
            }

            updateTotal();

            // Update chart if we're on the spending tab
            if (binding.tapLayout.getSelectedTabPosition() == 0) {
                updateSpendingChart(spendingInCharts);
            }
        });

        // Observer for revenue data
        viewModel.SpendingInChartThu().observe(getViewLifecycleOwner(), spendingInCharts -> {
            Log.d(TAG, "Revenue data received: " + spendingInCharts.size() + " items");

            revenue = 0;
            if (!spendingInCharts.isEmpty()) {
                Map<String, List<SpendingInChart>> listMap = spendingInCharts.stream()
                        .collect(Collectors.groupingBy(SpendingInChart::getTenDanhMuc));

                listMap.forEach((_tenDanhMuc, list) -> {
                    long totalAmount = list.stream().mapToLong(SpendingInChart::getTien).sum();
                    revenue += totalAmount;
                });
            }

            updateTotal();

            // Update chart if we're on the revenue tab
            if (binding.tapLayout.getSelectedTabPosition() == 1) {
                updateRevenueChart(spendingInCharts);
            }
        });
    }

    // Fetch both spending and revenue data
    private void fetchAllData() {
        viewModel.get_SpendingInChartChi(requireContext(), calendar.get(Calendar.MONTH) + 1);
        viewModel.get_SpendingInChartThu(requireContext(), calendar.get(Calendar.MONTH) + 1);
    }

    // Update current tab's chart based on which tab is selected
    private void updateCurrentTabData() {
        if (binding == null) return;

        Log.d(TAG, "Updating current tab: " + binding.tapLayout.getSelectedTabPosition());

        if (binding.tapLayout.getSelectedTabPosition() == 0) {
            // Update spending chart with existing data
            updateSpendingChart(viewModel.SpendingInChartChi().getValue());
        } else {
            // Update revenue chart with existing data
            updateRevenueChart(viewModel.SpendingInChartThu().getValue());
        }
    }

    private void updateSpendingChart(List<SpendingInChart> spendingInCharts) {
        if (spendingInCharts == null || spendingInCharts.isEmpty()) {
            resetChartAndAdapter();
            return;
        }

        try {
            List<DataEntry> dataEntries = new ArrayList<>();
            List<SpendingInChart> spendingInChart = new ArrayList<>();

            Map<String, List<SpendingInChart>> listMap = spendingInCharts.stream()
                    .collect(Collectors.groupingBy(SpendingInChart::getTenDanhMuc));

            listMap.forEach((_tenDanhMuc, list) -> {
                long totalAmount = list.stream().mapToLong(SpendingInChart::getTien).sum();
                SpendingInChart s = new SpendingInChart(
                        totalAmount,
                        _tenDanhMuc,
                        list.get(0).getIcon()
                );
                spendingInChart.add(s);
                dataEntries.add(new ValueDataEntry(s.getTenDanhMuc(), Math.abs(totalAmount)));
            });

            if (!dataEntries.isEmpty()) {
                updateChartAndAdapter(dataEntries, spendingInChart);
            } else {
                resetChartAndAdapter();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in updateSpendingChart", e);
            resetChartAndAdapter();
        }
    }

    private void updateRevenueChart(List<SpendingInChart> spendingInCharts) {
        if (spendingInCharts == null || spendingInCharts.isEmpty()) {
            resetChartAndAdapter();
            return;
        }

        try {
            List<DataEntry> dataEntries = new ArrayList<>();
            List<SpendingInChart> spendingInChart = new ArrayList<>();

            Map<String, List<SpendingInChart>> listMap = spendingInCharts.stream()
                    .collect(Collectors.groupingBy(SpendingInChart::getTenDanhMuc));

            listMap.forEach((_tenDanhMuc, list) -> {
                long totalAmount = list.stream().mapToLong(SpendingInChart::getTien).sum();
                SpendingInChart s = new SpendingInChart(
                        totalAmount,
                        _tenDanhMuc,
                        list.get(0).getIcon()
                );
                spendingInChart.add(s);
                dataEntries.add(new ValueDataEntry(s.getTenDanhMuc(), totalAmount));
            });

            if (!dataEntries.isEmpty()) {
                updateChartAndAdapter(dataEntries, spendingInChart);
            } else {
                resetChartAndAdapter();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in updateRevenueChart", e);
            resetChartAndAdapter();
        }
    }

    private void updateChartAndAdapter(List<DataEntry> dataEntries, List<SpendingInChart> spendingInChart) {
        if (getActivity() == null) {
            Log.e(TAG, "Cannot update chart: activity is null");
            return;
        }

        requireActivity().runOnUiThread(() -> {
            try {
                // Handle empty data case
                if (dataEntries == null || dataEntries.isEmpty()) {
                    resetChartAndAdapter();
                    return;
                }

                // Defensive checks and reinitialize if needed
                if (anyChartView == null || pie == null) {
                    Log.d(TAG, "Chart components null during update, trying to reinitialize");
                    setupAnyChartView();

                    // If still null after setup, bail out
                    if (anyChartView == null || pie == null) {
                        Log.e(TAG, "Failed to reinitialize chart components");
                        return;
                    }
                }

                // Re-activate the chart view to prevent JS listener errors
                APIlib.getInstance().setActiveAnyChartView(anyChartView);

                // Set data and title based on tab
                String title = binding.tapLayout.getSelectedTabPosition() == 0 ?
                        "Biểu đồ chi tiêu" : "Biểu đồ thu nhập";
                pie.title(title);
                pie.data(dataEntries);

                // Ensure the chart is visible
                anyChartView.setVisibility(View.VISIBLE);

                // Force chart update
                anyChartView.invalidate();

                // Update adapter
                adapter.setAdapter(spendingInChart);

                Log.d(TAG, "Chart updated successfully with " + dataEntries.size() + " entries");
            } catch (Exception e) {
                Log.e(TAG, "Error updating chart", e);

                // Attempt recovery
                try {
                    setupAnyChartView();
                } catch (Exception recovery) {
                    Log.e(TAG, "Recovery failed", recovery);
                }
            }
        });
    }

    private void resetChartAndAdapter() {
        if (getActivity() == null) {
            Log.e(TAG, "Cannot reset chart: activity is null");
            return;
        }

        requireActivity().runOnUiThread(() -> {
            try {
                Log.d(TAG, "Resetting chart and adapter");

                // Create an empty data list
                List<DataEntry> emptyData = new ArrayList<>();

                // Add a single placeholder data entry to show an empty chart
                emptyData.add(new ValueDataEntry("Không có dữ liệu", 1));

                // Check if anyChartView is initialized
                if (anyChartView == null) {
                    Log.d(TAG, "AnyChartView is null during reset, trying to reinitialize");
                    setupAnyChartView();
                }

                // Re-activate the chart view to prevent JS listener errors
                if (anyChartView != null) {
                    APIlib.getInstance().setActiveAnyChartView(anyChartView);

                    // Reset or initialize the pie chart
                    if (pie == null) {
                        pie = AnyChart.pie();
                        pie.title("Không có dữ liệu cho thời gian này");
                        pie.labels().position("outside");
                        pie.legend().position("bottom");
                        anyChartView.setChart(pie);
                    } else {
                        pie.title("Không có dữ liệu cho thời gian này");
                    }

                    // Set the empty data
                    pie.data(emptyData);

                    // Ensure visibility
                    anyChartView.setVisibility(View.VISIBLE);
                    anyChartView.invalidate(); // Force refresh
                }

                // Reset adapter
                adapter.setAdapter(new ArrayList<>());

                Log.d(TAG, "Chart and adapter reset complete");
            } catch (Exception e) {
                Log.e(TAG, "Error resetting chart and adapter", e);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Reinitialize chart if needed
        if (anyChartView == null) {
            setupAnyChartView();
        }

        // Refresh all data when coming back to this fragment
        fetchAllData();
    }

    private void updateTotal() {
        if (binding != null) {
            binding.tvSpending.setText(Math.abs(spending) + " đ");
            binding.tvRevenue.setText(revenue + " đ");
            binding.tvTotal.setText((spending + revenue) + " đ");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Cleanup
        if (anyChartView != null) {
            anyChartView.clear();
        }

        binding = null;
        anyChartView = null;
        pie = null;
    }
}