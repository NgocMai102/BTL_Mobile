package com.example.btl_android.View.sharing_detail;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.btl_android.Components.DataBaseManager;
import com.example.btl_android.Model.HoaDon;
import com.example.btl_android.Model.NguoiDung;

import java.util.List;

public class MoneySharingDetailsViewModel extends ViewModel {

    private MutableLiveData<List<NguoiDung>> _nguoiDungs = new MutableLiveData<>();
    public LiveData<List<NguoiDung>> nguoiDungs() {
        return _nguoiDungs;
    }
    public void getAllMember(Long id,Context context){
        _nguoiDungs.setValue(DataBaseManager.getInstance(context).getItemDAO().timKiemNguoiDungTheoHoaDon(id));
    }

    private MutableLiveData<HoaDon> _hoaDon = new MutableLiveData<>();
    public LiveData<HoaDon> hoaDon() {
        return _hoaDon;
    }
    public void getHoaDon(Long id,Context context){
        _hoaDon.setValue(DataBaseManager.getInstance(context).getItemDAO().timKiemHoaDon(id));
    }
    public void editMember(NguoiDung nguoiDung,Context context){
        DataBaseManager.getInstance(context).getItemDAO().chinhSuaNguoiDung(nguoiDung);
    }

}
