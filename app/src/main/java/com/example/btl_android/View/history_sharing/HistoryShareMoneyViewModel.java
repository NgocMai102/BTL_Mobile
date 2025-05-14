package com.example.btl_android.View.history_sharing;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.btl_android.Model.HoaDon;
import com.example.btl_android.Components.DataBaseManager;

import java.util.List;

public class HistoryShareMoneyViewModel extends ViewModel {
    private MutableLiveData<List<HoaDon>> _hoaDons = new MutableLiveData<>();
    public LiveData<List<HoaDon>> hoaDons() {
            return _hoaDons;
    }
    private MutableLiveData<Integer> _nguoiDung = new MutableLiveData<>();
    public LiveData<Integer> nguoiDung() {
            return _nguoiDung;
    }
    public void getAllHoaDon(Context context) {
        _hoaDons.setValue(DataBaseManager.getInstance(context).getItemDAO().getAllHoaDon());
    }
    public void deteteHoaDon(Context context,HoaDon hoaDon){
        DataBaseManager.getInstance(context).getItemDAO().xoaHoaDon(hoaDon);
    }
    public void deteteNguoiDungs(Context context,HoaDon hoaDon){
        _nguoiDung.setValue(DataBaseManager.getInstance(context).getItemDAO().xoaNguoiDung(hoaDon.getId()));
    }
}
