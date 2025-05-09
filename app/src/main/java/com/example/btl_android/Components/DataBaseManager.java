package com.example.btl_android.Components;

import static com.example.btl_android.Helper.ViewExtention.convertDrawableToBase64;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.btl_android.DAO.Queries;
import com.example.btl_android.Model.DanhMuc;
import com.example.btl_android.Model.GiaoDich;
import com.example.btl_android.Model.HoaDon;
import com.example.btl_android.Model.NguoiDung;
import com.example.btl_android.R;

import java.util.concurrent.CompletableFuture;

import kotlinx.coroutines.GlobalScope;

@Database(entities = {DanhMuc.class, GiaoDich.class, HoaDon.class, NguoiDung.class}, version = 1, exportSchema = false)
public abstract class DataBaseManager extends RoomDatabase {
    public abstract Queries getItemDAO();

    private static final String DATABASE_NAME = "database.db";
    private static volatile DataBaseManager instance;

    public static DataBaseManager getInstance(Context context) {
        if (instance == null) {
            synchronized (DataBaseManager.class) {
                if (instance == null) {
                    instance = buildDatabase(context);
                }
            }
        }
        return instance;
    }

    private static DataBaseManager buildDatabase(Context context) {
        return Room.databaseBuilder(context, DataBaseManager.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(new RoomDatabase.Callback() {

                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                            prepopulateDatabase(instance.getItemDAO(), context);
                        });
                    }
                })
                .allowMainThreadQueries()
                .build();
    }

    private static void prepopulateDatabase(Queries qu, Context context) {
        qu.themDanhMuc(new DanhMuc(1, convertDrawableToBase64(context, R.drawable.icon_tableware), "Ăn uống", true));
        qu.themDanhMuc(new DanhMuc(2, convertDrawableToBase64(context, R.drawable.icon_grocery), "Chi tiêu hàng ngày", true));
        qu.themDanhMuc(new DanhMuc(3, convertDrawableToBase64(context, R.drawable.icon_jersey), "Quần áo", true));
        qu.themDanhMuc(new DanhMuc(4, convertDrawableToBase64(context, R.drawable.icon_lipstick), "Mỹ phẩm", true));
        qu.themDanhMuc(new DanhMuc(5, convertDrawableToBase64(context, R.drawable.icon_cheer), "Phí giao lưu", true));
        qu.themDanhMuc(new DanhMuc(6, convertDrawableToBase64(context, R.drawable.icon_medicine), "Y tế", true));
        qu.themDanhMuc(new DanhMuc(7, convertDrawableToBase64(context, R.drawable.icon_book), "Giáo dục", true));
        qu.themDanhMuc(new DanhMuc(8, convertDrawableToBase64(context, R.drawable.icon_electricity), "Tiền điện", true));
        qu.themDanhMuc(new DanhMuc(9, convertDrawableToBase64(context, R.drawable.icon_train), "Đi lại", true));
        qu.themDanhMuc(new DanhMuc(10, convertDrawableToBase64(context, R.drawable.icon_phone), "Phí liên lạc", true));
        qu.themDanhMuc(new DanhMuc(11, convertDrawableToBase64(context, R.drawable.icon_home), "Tiền nhà", true));
        qu.themDanhMuc(new DanhMuc(12, convertDrawableToBase64(context, R.drawable.icon_other), "Khác", true));
        qu.themDanhMuc(new DanhMuc(21, convertDrawableToBase64(context, R.drawable.icon_coin_wallet), "Tiền lương", false));
        qu.themDanhMuc(new DanhMuc(22, convertDrawableToBase64(context, R.drawable.icon_piggy_bank), "Tiền phụ cấp", false));
        qu.themDanhMuc(new DanhMuc(23, convertDrawableToBase64(context, R.drawable.icon_gift), "Tiền thưởng", false));
        qu.themDanhMuc(new DanhMuc(24, convertDrawableToBase64(context, R.drawable.icon_invest), "Đầu tư", false));
        qu.themDanhMuc(new DanhMuc(25, convertDrawableToBase64(context, R.drawable.icon_money_bag), "Thu nhập phụ", false));
        qu.themDanhMuc(new DanhMuc(26, convertDrawableToBase64(context, R.drawable.icon_money_hand), "Thu nhập phụ", false));

    }
    public static void deleteAllTable(){
        instance.clearAllTables();
    }
}
