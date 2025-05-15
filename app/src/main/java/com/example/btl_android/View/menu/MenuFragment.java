package com.example.btl_android.View.menu;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.btl_android.R;
import com.example.btl_android.View.event.EventActivity;
import com.example.btl_android.View.history_sharing.HistoryShareMoneyActivity;
import com.example.btl_android.View.search_transaction.SearchTransactionActivity;
import com.example.btl_android.View.searchmoney.ShareMoneyActivity;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {

    private RecyclerView recyclerView;

    public MenuFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        recyclerView = view.findViewById(R.id.menu_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<MenuItem> menuList = new ArrayList<>();
        menuList.add(new MenuItem(R.drawable.icon_schedule, "Sự kiện"));
        menuList.add(new MenuItem(R.drawable.icon_find, "Tìm kiếm giao dịch"));
        menuList.add(new MenuItem(R.drawable.baseline_bank, "Chia tiền")) ;
        menuList.add(new MenuItem(R.drawable.history, "Lịch sử chia tiền")) ;
        MenuAdapter adapter = new MenuAdapter(menuList, position -> {
            switch (position) {
                case 0: // Events
                    startActivity(new Intent(getActivity(), EventActivity.class));
                    break;
                case 1: // Yearly Report
                    startActivity(new Intent(getActivity(), SearchTransactionActivity.class));
                    break;
                case 2: // Find Transaction
                    startActivity(new Intent(getActivity(), ShareMoneyActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(getActivity(), HistoryShareMoneyActivity.class));
                    break;
            }
        });

        recyclerView.setAdapter(adapter);

        return view;
    }
}
