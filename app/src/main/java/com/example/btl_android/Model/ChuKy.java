package com.example.btl_android.Model;

public enum ChuKy {
    NONE,
    WEEKLY,
    MONTHLY;

    public static ChuKy fromString(String value) {
        switch (value.toLowerCase()) {
            case "hàng tuần": return WEEKLY;
            case "hàng tháng": return MONTHLY;
            default: return NONE;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case WEEKLY: return "Hàng tuần";
            case MONTHLY: return "Hàng tháng";
            default: return "Không lặp";
        }
    }


}
