package com.example.btl_android.View.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import com.example.btl_android.R;
import com.example.btl_android.databinding.FragmentHomeBinding;
import java.util.Calendar;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private boolean trangThai;
    private Calendar calendar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
        trangThai = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        loadView();
        return binding.getRoot();
    }

    private void loadView() {
        updateDateText();

        binding.btnAccept.setOnClickListener(v -> {
            // UI only: No actual logic
        });

        binding.btnTienChi.setOnClickListener(v -> {
            trangThai = true;
            onClickTienChi();
        });

        binding.btnTienThu.setOnClickListener(v -> {
            trangThai = false;
            onClickTienThu();
        });

        binding.tvDay.setOnClickListener(v -> showDatePicker());

        binding.imvBackDay.setOnClickListener(v -> {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            updateDateText();
        });

        binding.imvIncreaseDay.setOnClickListener(v -> {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            updateDateText();
        });

        binding.SpendInput.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    binding.SpendInput.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[,]", "");

                    try {
                        long parsed = Long.parseLong(cleanString);
                        String formatted = String.format("%,d", parsed);
                        current = formatted;
                        binding.SpendInput.setText(formatted);
                        binding.SpendInput.setSelection(formatted.length());
                    } catch (NumberFormatException e) {
                        // If not a valid number (empty or too large), just reset
                        binding.SpendInput.setText("");
                    }

                    binding.SpendInput.addTextChangedListener(this);
                }
            }
        });

    }

    private void updateDateText() {
        binding.tvDay.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
    }

    private void showDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(
                requireContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateText();
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }

    private void onClickTienThu() {
        // Update appearance for btn_tien_thu (active)
        binding.btnTienThu.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_blue));
        binding.btnTienThu.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));

        // Reset appearance for btn_tien_chi (inactive)
        binding.btnTienChi.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_gray));
        binding.btnTienChi.setTextColor(ContextCompat.getColor(requireContext(), R.color.bluenav));

        // Update other UI if needed
        binding.btnAccept.setText("Nhập khoản tiền thu");
        binding.tvSpendingMoneyOrRevenue.setText("Tiền thu");
    }

    private void onClickTienChi() {
        // Update appearance for btn_tien_chi (active)
        binding.btnTienChi.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_blue));
        binding.btnTienChi.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));

        // Reset appearance for btn_tien_thu (inactive)
        binding.btnTienThu.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_gray));
        binding.btnTienThu.setTextColor(ContextCompat.getColor(requireContext(), R.color.bluenav));

        // Update other UI if needed
        binding.btnAccept.setText("Nhập khoản tiền chi");
        binding.tvSpendingMoneyOrRevenue.setText("Tiền chi");
    }
}