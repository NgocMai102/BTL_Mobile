package com.example.btl_android.View.search_transaction;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.Components.DataBaseManager;
import com.example.btl_android.Model.DanhMuc;
import com.example.btl_android.Model.GiaoDich;
import com.example.btl_android.R;
import com.example.btl_android.View.event.EventActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchTransactionActivity extends AppCompatActivity {

    private EditText edtSearch;
    private RecyclerView rvSearchResult;
    private TransactionAdapter adapter;
    private List<GiaoDich> giaoDichList;      // full data
    private List<GiaoDich> filteredList;      // filtered results

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_transaction);

        edtSearch = findViewById(R.id.edtSearch);
        rvSearchResult = findViewById(R.id.rvSearchResult);

        giaoDichList = getAllGiaoDichFromDatabase(); // get full list
        filteredList = new ArrayList<>();

        adapter = new TransactionAdapter(filteredList); // Your existing adapter
        rvSearchResult.setLayoutManager(new LinearLayoutManager(this));
        rvSearchResult.setAdapter(adapter);

        ImageView backIcon = findViewById(R.id.imv_back_day); // or R.id.toolbar_back for the first one
        backIcon.setOnClickListener(v -> finish());

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String keyword) {
        filteredList.clear();
        for (GiaoDich gd : giaoDichList) {
            if (gd.getGhiChu().toLowerCase().contains(keyword.toLowerCase()) ||
                    String.valueOf(gd.getThuChi()).contains(keyword)) {
                filteredList.add(gd);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private List<GiaoDich> getAllGiaoDichFromDatabase() {
        // Use your DAO or SQLite helper here
        // For now, mock some data:
        List<GiaoDich> list =  DataBaseManager.getInstance(SearchTransactionActivity.this).getItemDAO().getAllGiaoDich();
        return list;
    }
}