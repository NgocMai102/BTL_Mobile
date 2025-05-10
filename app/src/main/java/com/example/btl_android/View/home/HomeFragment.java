package com.example.btl_android.View.home;

import static com.example.btl_android.Helper.ViewExtention.showToast;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.btl_android.Helper.Constants;
import com.example.btl_android.Model.DanhMuc;
import com.example.btl_android.Model.GiaoDich;
import com.example.btl_android.R;
import com.example.btl_android.View.edit_directory.EditDirectoryActivity;
import com.example.btl_android.databinding.FragmentHomeBinding;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private boolean trangThai;
    Calendar calendar;
    private DirectoryAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel._getDanhMucThu(requireContext());
        viewModel._getDanhMucChi(requireContext());
        calendar = Calendar.getInstance();
        trangThai = true;
        adapter = new DirectoryAdapter(
                new DirectoryAdapter.ClickListener() {
                    @Override
                    public void onClickEditDirectory() {
                        startActivity(new Intent(requireContext(), EditDirectoryActivity.class));
                    }
                }
        );
        getDanhMucChi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        loadView();
        return binding.getRoot();

    }

    private void loadView() {
        binding.tvDay.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
        binding.recyclerview.setAdapter(adapter);
        binding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.edtSpendingMoney.getEditText().getText().toString().isEmpty()) {
                    showToast(requireContext(), Constants.PLS_ENTER_THE_AMOUNT);
                } else {
                    updateSpending();
                }
            }
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

        binding.btnTienChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trangThai = true;
                onClickTienChi();
            }
        });
        binding.btnTienThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trangThai = false;
                onClickTienThu();
            }
        });
        binding.tvDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickday();
            }
        });
        binding.imvBackDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                String selectedDate = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR); // Month is zero-based
                binding.tvDay.setText(selectedDate + "");
            }
        });
        binding.imvIncreaseDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                String selectedDate = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR); // Month is zero-based
                binding.tvDay.setText(selectedDate + "");
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel._getDanhMucThu(requireContext());
        viewModel._getDanhMucChi(requireContext());
        calendar = Calendar.getInstance();
        trangThai = true;
    }

    private void onClickday() {
        DatePickerDialog datePicker = new DatePickerDialog(
                requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int yearPicker, int monthPicker, int dayOfMonthPicker) {
                calendar.set(Calendar.YEAR, yearPicker);
                calendar.set(Calendar.MONTH, monthPicker );
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonthPicker);
                String selectedDate = calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                        (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR); // Month is zero-based
                binding.tvDay.setText(selectedDate);
            }
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }

    private void onClickTienThu() {
        getDanhMucThu();

        binding.btnTienThu.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_blue));
        binding.btnTienThu.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));

        binding.btnTienChi.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_gray));
        binding.btnTienChi.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary));

        binding.tvSpendingMoneyOrRevenue.setText(Constants.TIEN_THU);
        binding.btnAccept.setText(Constants.BTN_TIEN_THU);
    }

    private void onClickTienChi() {
        getDanhMucChi();

        binding.btnTienChi.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_blue));
        binding.btnTienChi.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));

        binding.btnTienThu.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_gray));
        binding.btnTienThu.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary));

        binding.tvSpendingMoneyOrRevenue.setText(Constants.TIEN_CHI);
        binding.btnAccept.setText(Constants.BTN_TIEN_CHI);
    }

    private void getDanhMucChi() {
        viewModel._getDanhMucChi(requireContext());
        viewModel.danhMucChi().observe(this, new Observer<List<DanhMuc>>() {
            @Override
            public void onChanged(List<DanhMuc> danhMucs) {
                adapter.setAdapter(danhMucs);
            }
        });
    }

    private void getDanhMucThu() {
        viewModel._getDanhMucThu(requireContext());
        viewModel.danhMucThu().observe(this, new Observer<List<DanhMuc>>() {
            @Override
            public void onChanged(List<DanhMuc> danhMucs) {
                adapter.setAdapter(danhMucs);
            }
        });
    }

    private void updateSpending() {
        try {
            // Remove commas from the input string
            String rawAmount = binding.edtSpendingMoney.getEditText().getText().toString().replace(",", "").trim();
            long amount = Long.parseLong(rawAmount);

            GiaoDich giaoDich = new GiaoDich(
                    0,
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.YEAR),
                    amount,
                    binding.edtNote.getEditText().getText().toString(),
                    trangThai,
                    adapter.getDanhMuc().getId()
            );

            viewModel.set_giaoDich(requireContext(), giaoDich);
            showToast(requireContext(), Constants.ADD_SUCCESSFUL);

            // Clear fields
            binding.edtNote.getEditText().setText("");
            binding.edtSpendingMoney.getEditText().setText("");
        } catch (NumberFormatException e) {
            showToast(requireContext(), "Số tiền không hợp lệ");
            Log.e("updateSpending", "Invalid number format", e);
        }
    }
}


