package com.example.btl_android.Helper;

import androidx.room.TypeConverter;

import com.example.btl_android.Model.ChuKy;

public class Converters {
    @TypeConverter
    public static ChuKy fromString(String value) {
        return ChuKy.valueOf(value);
    }

    @TypeConverter
    public static String chuKyToString(ChuKy chuKy) {
        return chuKy.name();
    }
}
