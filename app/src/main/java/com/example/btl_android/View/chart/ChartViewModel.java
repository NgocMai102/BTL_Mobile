package com.example.btl_android.View.chart;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.btl_android.Components.DataBaseManager;
import com.example.btl_android.Model.SpendingInChart;

import java.util.List;

public class ChartViewModel extends ViewModel {

    private MutableLiveData<List<SpendingInChart>> _SpendingInChartChi = new MutableLiveData<>();

    public LiveData<List<SpendingInChart>> SpendingInChartChi() {
        return _SpendingInChartChi;
    }

    private MutableLiveData<List<SpendingInChart>> _SpendingInChartThu = new MutableLiveData<>();

    public LiveData<List<SpendingInChart>> SpendingInChartThu() {
        return _SpendingInChartThu;
    }

    public void get_SpendingInChartChi(Context context, int month) {
        _SpendingInChartChi.setValue(DataBaseManager.getInstance(context).getItemDAO().timKiemGiaoDichChiBieuDo(month));
    }
    public void get_SpendingInChartThu(Context context, int month) {
        _SpendingInChartThu.setValue(DataBaseManager.getInstance(context).getItemDAO().timKiemGiaoDichThuBieuDo(month));
    }
}