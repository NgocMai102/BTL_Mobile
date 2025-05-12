package com.example.btl_android.View.edit_directory;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.btl_android.Model.DanhMuc;
import com.example.btl_android.Components.DataBaseManager;

import java.util.List;

public class EditDirectoryViewModel extends ViewModel {

    private MutableLiveData<List<DanhMuc>> _danhMucChi = new MutableLiveData<>();

    public LiveData<List<DanhMuc>> danhMucChi() {
        return _danhMucChi;
    }

    private MutableLiveData<List<DanhMuc>> _danhMucThu = new MutableLiveData<>();

    public LiveData<List<DanhMuc>> danhMucThu() {
        return _danhMucThu;
    }

    public void deleteDanhMuc(Context context, DanhMuc danhMuc) {
        DataBaseManager.getInstance(context).getItemDAO().xoaDanhMuc(danhMuc);
    }

    public void getDanhMucChi(Context context) {
        _danhMucChi.setValue(DataBaseManager.getInstance(context).getItemDAO().timKiemDanhMucChi());
    }

    public void getDanhMucThu(Context context) {
        _danhMucThu.setValue(DataBaseManager.getInstance(context).getItemDAO().timKiemDanhMucThu());
    }
}
