package com.example.btl_android.View.event;


import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.btl_android.Model.DanhMuc;
import com.example.btl_android.Model.GiaoDich;
import com.example.btl_android.Components.DataBaseManager;
import com.example.btl_android.Model.SuKien;

import java.util.List;

public class AddEventViewModel extends ViewModel {
    private MutableLiveData<List<DanhMuc>> _danhMucThu= new MutableLiveData<>();
    public LiveData<List<DanhMuc>> danhMucThu(){
        return _danhMucThu;
    }
    private SuKien editingSuKien;

    public void setEditingSuKien(SuKien suKien) {
        this.editingSuKien = suKien;
    }

    public SuKien getEditingSuKien() {
        return editingSuKien;
    }
    public void _getDanhMucThu(Context context){
        _danhMucThu.setValue(DataBaseManager.getInstance(context).getItemDAO().timKiemDanhMucThu());
    }

    public void saveSuKien(Context context, SuKien suKien) {
        Long id = DataBaseManager.getInstance(context).getItemDAO().themSuKien(suKien);
    }


}
