package com.example.btl_android.Helper;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.btl_android.Components.DataBaseManager;
import com.example.btl_android.Model.ChuKy;
import com.example.btl_android.Model.GiaoDich;
import com.example.btl_android.Model.SuKien;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PeriodicTransactionWorker extends Worker {
    public PeriodicTransactionWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Get all recurring events from database
        List<SuKien> recurringEvents = DataBaseManager.getInstance(getApplicationContext())
                .getItemDAO()
                .getRecurringEvents();

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (SuKien event : recurringEvents) {
            if (event.getChuKy() != ChuKy.NONE) {
                // Parse the event's start date
                LocalDate eventDate = LocalDate.parse(event.getNgayBatDau(), formatter);

                // Check if we should create a transaction today
                boolean shouldCreate = false;

                if (event.getChuKy() == ChuKy.WEEKLY) {
                    // Weekly - check if today is the same day of week as event date
                    shouldCreate = today.getDayOfWeek() == eventDate.getDayOfWeek();
                } else if (event.getChuKy() == ChuKy.MONTHLY) {
                    // Monthly - check if today is the same day of month as event date
                    shouldCreate = today.getDayOfMonth() == eventDate.getDayOfMonth();
                }

                if (shouldCreate) {
                    // Create a new GiaoDich
                    GiaoDich transaction = new GiaoDich(
                            0,
                            today.getDayOfMonth(),
                            today.getMonthValue(),
                            today.getYear(),
                            event.getSoTien(),
                            event.getGhiChu() + " (Tự động từ sự kiện: " + event.getTenSuKien() + ")",
                            true, // Assuming these are income events
                            event.getIdDanhMuc()
                    );

                    // Save to database
                    DataBaseManager.getInstance(getApplicationContext())
                            .getItemDAO()
                            .themNguoiGiaoDich(transaction);
                }
            }
        }

        return Result.success();
    }
}
