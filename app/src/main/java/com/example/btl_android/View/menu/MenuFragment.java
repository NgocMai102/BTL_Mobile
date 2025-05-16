package com.example.btl_android.View.menu;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.btl_android.Components.DataBaseManager;
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

    private void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Câu hỏi")
                .setMessage(
                        "Bạn có chắc chắn muốn xóa tất cả dữ liệu? Thao tác này không thể\n" +
                                "hoàn tác lại."
                )
                .setPositiveButton("Yes", (dialog, which) -> {
                    try {
                        DataBaseManager.deleteAllTable();

                    } catch (Exception e) {
                        e.getMessage();

                    }

                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        recyclerView = view.findViewById(R.id.menu_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<MenuItem> menuList = new ArrayList<>();
        menuList.add(new MenuItem(R.drawable.icon_schedule, "Sự kiện"));
        menuList.add(new MenuItem(R.drawable.icon_find, "Tìm kiếm giao dịch"));
        menuList.add(new MenuItem(R.drawable.money, "Chia tiền")) ;
        menuList.add(new MenuItem(R.drawable.clock, "Lịch sử chia tiền")) ;
        menuList.add(new MenuItem(R.drawable.deleteall, "Xoá tất cả dữ liệu")) ;
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
                case 4:
                    delete();
                    break;
            }
        });

        recyclerView.setAdapter(adapter);

        return view;
    }
}
