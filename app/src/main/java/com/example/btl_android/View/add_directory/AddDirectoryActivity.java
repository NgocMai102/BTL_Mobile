package com.example.btl_android.View.add_directory;

import static com.example.btl_android.Helper.ViewExtention.convertDrawableToBase64;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.btl_android.Model.DanhMuc;
import com.example.btl_android.databinding.ActivityAddDirectoryBinding;

public class AddDirectoryActivity extends AppCompatActivity {
    private AddDirectoryViewModel viewModel;
    private ActivityAddDirectoryBinding binding;
    private AddDirectoryAdapter adapter;
    private int id;
    private String icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddDirectoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(AddDirectoryViewModel.class);
        adapter = new AddDirectoryAdapter();
        id = getIntent().getIntExtra("id", -1);
        String mode = getIntent().getStringExtra("mode");
        if ("edit".equals(mode)) {
            binding.txtTitle.setText("Chỉnh sửa danh mục");
            icon = getIntent().getStringExtra("icon");
            setIconFromIntent(icon);
        } else {
            binding.txtTitle.setText("Thêm danh mục");
        }
        if (id != -1) {
            binding.edtName.getEditText().setText(getIntent().getStringExtra("name"));
        }
        binding.imbtnBack.setOnClickListener(view -> finish());
        binding.btnSave.setOnClickListener(view -> {
            onClickSave();
        });
        binding.recyclerview.setAdapter(adapter);
    }

    private void setIconFromIntent(String iconBase64) {
        int iconIndex = convertBase64ToIconIndex(iconBase64);
        adapter.setSelectedIcon(iconIndex);
    }

    private int convertBase64ToIconIndex(String base64) {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (base64.equals(convertDrawableToBase64(getApplicationContext(), adapter.getIcon(i)))) {
                return i;
            }
        }
        return -1;
    }

    private void onClickSave() {
        if (!binding.edtName.getEditText().getText().toString().isEmpty()) {
            try {
                DanhMuc danhMuc = new DanhMuc();
                danhMuc.setTenDanhMuc(binding.edtName.getEditText().getText().toString());
                danhMuc.setIcon(convertDrawableToBase64(
                        getApplicationContext(),
                        adapter.getIcon()
                ));
                danhMuc.setThuChi(getIntent().getBooleanExtra("thuChi", true));
                if (id != -1) {
                    DanhMuc dm = new DanhMuc();
                    danhMuc.setId(id);
                    viewModel.deleteDanhMuc(this,dm);
                }
                addDanhMuc(danhMuc);
                finish();
            } catch (Exception e) {
                e.getMessage();
            }
        } else {
            Toast.makeText(
                    getApplicationContext(), "Vui lòng chọn tên danh mục và ảnh!", Toast.LENGTH_SHORT
            ).show();
            binding.edtName.getEditText().requestFocus();
        }
    }

    private void addDanhMuc(DanhMuc danhMuc) {
        viewModel.addDanhMuc(this, danhMuc);
        viewModel.blDanhMuc().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                Toast.makeText(
                        getApplicationContext(), "Đã lưu!", Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}
