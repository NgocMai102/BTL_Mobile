package com.example.btl_android.View.event;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.Components.DataBaseManager;
import com.example.btl_android.Model.ChuKy;
import com.example.btl_android.Model.DanhMuc;
import com.example.btl_android.Model.SuKien;
import com.example.btl_android.R;
import com.google.android.material.appbar.MaterialToolbar;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EventActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<SuKien> suKienList;

    private EventViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        List<DanhMuc> danhMucs = DataBaseManager.getInstance(EventActivity.this).getItemDAO().timKiemDanhMucThu();
        Map<Integer, String> danhMucMap = new HashMap<>();
        for (DanhMuc dm : danhMucs) {
            danhMucMap.put(dm.getId(), dm.getTenDanhMuc()); // Adjust getter names if needed
        }
        recyclerView = findViewById(R.id.event_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ImageView backIcon = findViewById(R.id.imv_back_day); // or R.id.toolbar_back for the first one
        backIcon.setOnClickListener(v -> finish());

        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
        viewModel.loadSuKien(this);

        viewModel.getAllSuKien().observe(this, suKiens -> {
            if (suKiens != null) {
                adapter.setSuKienList(suKiens);
            }
        });

        suKienList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        adapter = new EventAdapter(new ArrayList<>(), danhMucMap, new EventAdapter.OnEventActionListener() {
            @Override
            public void onEdit(SuKien suKien) {
                Intent intent = new Intent(EventActivity.this, AddEventActivity.class);
                intent.putExtra("edit_event", suKien);
                startActivity(intent);
            }

            @Override
            public void onDelete(SuKien suKien) {
                viewModel.deleteSuKien(EventActivity.this, suKien);
            }
        });
        recyclerView.setAdapter(adapter);

        findViewById(R.id.fab_add_event).setOnClickListener(v -> {
            Intent intent = new Intent(EventActivity.this, AddEventActivity.class);
            startActivity(intent);
        });
    }

}