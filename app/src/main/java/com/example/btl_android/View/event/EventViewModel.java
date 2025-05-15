// EventViewModel.java
package com.example.btl_android.View.event;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.btl_android.Components.DataBaseManager;
import com.example.btl_android.Model.ChuKy;
import com.example.btl_android.Model.GiaoDich;
import com.example.btl_android.Model.SuKien;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class EventViewModel extends ViewModel {

    private LiveData<List<SuKien>> allSuKien;

    public void loadSuKien(Context context) {
        allSuKien = DataBaseManager.getInstance(context).getItemDAO().getAllSuKien();
    }

    public LiveData<List<SuKien>> getAllSuKien() {
        return allSuKien;
    }

    public void deleteSuKien(Context context, SuKien suKien) {
        DataBaseManager.getInstance(context).getItemDAO().xoaSuKien(suKien);
    }

    public void saveSuKien(Context context, SuKien suKien) {
        Long id = DataBaseManager.getInstance(context).getItemDAO().themSuKien(suKien);

        // If this is a recurring event, create the first transaction immediately
        if (suKien.getChuKy() != ChuKy.NONE) {
            createTransactionFromEvent(context, suKien);
        }
    }

    private void createTransactionFromEvent(Context context, SuKien event) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate eventDate = LocalDate.parse(event.getNgayBatDau(), formatter);

        GiaoDich transaction = new GiaoDich(
                0,
                eventDate.getDayOfMonth(),
                eventDate.getMonthValue(),
                eventDate.getYear(),
                event.getSoTien(),
                event.getGhiChu() + " (Tự động từ sự kiện: " + event.getTenSuKien() + ")",
                true, // Assuming these are income events
                event.getIdDanhMuc()
        );

        DataBaseManager.getInstance(context).getItemDAO().themNguoiGiaoDich(transaction);
    }
}
