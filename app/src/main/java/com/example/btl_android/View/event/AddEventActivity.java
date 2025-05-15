package com.example.btl_android.View.event;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.btl_android.Components.DataBaseManager;
import com.example.btl_android.Model.DanhMuc;
import com.example.btl_android.R;
import com.example.btl_android.View.edit_directory.EditDirectoryActivity;
import com.example.btl_android.Model.SuKien;
import com.example.btl_android.Model.ChuKy;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity {

    private DirectoryAdapter adapter;
    private Calendar calendar;
    private EditText editTextEventDate;

    private AddEventViewModel viewModel;

    private SuKien editingEvent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Intent intent = getIntent();
        editingEvent = (SuKien) intent.getSerializableExtra("edit_event");

        viewModel = new ViewModelProvider(this).get(AddEventViewModel.class);
        Button saveButton = findViewById(R.id.btn_save_event);
        EditText eventNameInput = findViewById(R.id.IncomeInput_ev);
        EditText amountInput = findViewById(R.id.IncomeInput); // Add this ID in XML if missing
        EditText noteInput = findViewById(R.id.NoteInput_ev);

        ImageView backIcon = findViewById(R.id.back_icon); // or R.id.toolbar_back for the first one
        backIcon.setOnClickListener(v -> finish());

        Spinner repeatSpinner = findViewById(R.id.spinner_repeat);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.repeat_options,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatSpinner.setAdapter(spinnerAdapter);

        calendar = Calendar.getInstance(); // current date
        editTextEventDate = findViewById(R.id.editText_event_date);

        // Display current date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        editTextEventDate.setText(sdf.format(calendar.getTime()));

        // Show DatePicker on click
        editTextEventDate.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
                calendar.set(Calendar.YEAR, selectedYear);
                calendar.set(Calendar.MONTH, selectedMonth);
                calendar.set(Calendar.DAY_OF_MONTH, selectedDay);

                editTextEventDate.setText(sdf.format(calendar.getTime()));
            }, year, month, day).show();
        });
        adapter = new DirectoryAdapter(new DirectoryAdapter.ClickListener() {
            @Override
            public void onClickEditDirectory() {
                // When clicking "Chỉnh sửa" item, start EditDirectoryActivity
                startActivity(new Intent(AddEventActivity.this, EditDirectoryActivity.class));
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview_add_event);  // Replace with the actual RecyclerView ID in your layout
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // Fetch data immediately when the activity is created
        fetchDanhMucThuData();

        if (editingEvent != null) {
            // Pre-fill data to edit
            eventNameInput.setText(editingEvent.getTenSuKien());
            editTextEventDate.setText(editingEvent.getNgayBatDau());
            amountInput.setText(String.valueOf(editingEvent.getSoTien()));
            noteInput.setText(editingEvent.getGhiChu());

            // Set spinner selection based on repeat value
            ChuKy chuKy = editingEvent.getChuKy();
            if (chuKy != null) {
                int spinnerIndex = spinnerAdapter.getPosition(chuKy.toString());
                if (spinnerIndex >= 0) {
                    repeatSpinner.setSelection(spinnerIndex);
                }
            }

            viewModel.setEditingSuKien(editingEvent);
        }

        saveButton.setOnClickListener(v -> {
            String tenSuKien = eventNameInput.getText().toString().trim();
            String ngayBatDau = editTextEventDate.getText().toString().trim();
            String amountStr = amountInput.getText().toString().trim();
            String ghiChu = noteInput.getText().toString().trim();
            String repeat = repeatSpinner.getSelectedItem().toString();

            if (tenSuKien.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Long soTien;
            try {
                soTien = Long.parseLong(amountStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số tiền không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get selected category
            DanhMuc selectedDanhMuc = adapter.getDanhMuc();
            if (selectedDanhMuc == null) {
                Toast.makeText(this, "Vui lòng chọn danh mục", Toast.LENGTH_SHORT).show();
                return;
            }

            int idDanhMuc = selectedDanhMuc.getId();
            ChuKy chuKy = ChuKy.fromString(repeat);

            SuKien suKien;
            if (viewModel.getEditingSuKien() != null) {
                // Update existing event
                suKien = new SuKien(
                        viewModel.getEditingSuKien().getId(), // Keep same ID
                        tenSuKien,
                        ngayBatDau,
                        soTien,
                        idDanhMuc,
                        chuKy,
                        ghiChu
                );
            } else {
                // Create new event
                suKien = new SuKien(
                        0,
                        tenSuKien,
                        ngayBatDau,
                        soTien,
                        idDanhMuc,
                        chuKy,
                        ghiChu
                );
            }

            viewModel.saveSuKien(this, suKien);
            Toast.makeText(this, "Đã lưu vào db", Toast.LENGTH_SHORT).show();
            Intent resultIntent = new Intent(AddEventActivity.this, EventActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(resultIntent);
            finish();
        });
    }

    private void fetchDanhMucThuData() {
        // Run the database query on a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Fetch data from the database (simulate with DAO)
                List<DanhMuc> danhMucs = DataBaseManager.getInstance(AddEventActivity.this).getItemDAO().timKiemDanhMucThu();
                Log.d("AddEventActivity", "Fetched size: " + danhMucs.size());
                // After fetching data, update the RecyclerView on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update the adapter with the fetched data
                        adapter.setAdapter(danhMucs);
                    }
                });
            }
        }).start();
    }
}