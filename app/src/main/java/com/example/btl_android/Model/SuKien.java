package com.example.btl_android.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(
        tableName = "SuKien", foreignKeys = @ForeignKey(
        entity = DanhMuc.class,
        parentColumns = "Id",
        childColumns = "IdDanhMuc",
        onDelete = ForeignKey.NO_ACTION
),
        indices = {@Index(value = {"IdDanhMuc"})})

public class SuKien implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "TenSuKien")
    private String tenSuKien;

    @ColumnInfo(name = "NgayBatDau")
    private String ngayBatDau;
    @ColumnInfo(name = "SoTien")
    private Long soTien;

    @ColumnInfo(name = "IdDanhMuc")
    private int idDanhMuc;

    @ColumnInfo(name = "ChuKy")
    private ChuKy chuKy = ChuKy.NONE;;

    @ColumnInfo(name = "GhiChu")
    private String ghiChu;

    public SuKien(int id, String tenSuKien, String ngayBatDau, Long soTien, int idDanhMuc, ChuKy chuKy, String ghiChu) {
        this.id = id;
        this.tenSuKien = tenSuKien;
        this.ngayBatDau = ngayBatDau;
        this.soTien = soTien;
        this.idDanhMuc = idDanhMuc;
        this.chuKy = chuKy;
        this.ghiChu = ghiChu;
    }

    public String getTenSuKien() {
        return tenSuKien;
    }

    public String getNgayBatDau() {
        return ngayBatDau;
    }

    public Long getSoTien() {
        return soTien;
    }

    public int getIdDanhMuc() {
        return idDanhMuc;
    }

    public ChuKy getChuKy() {
        return chuKy;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public int getId() {
        return id;
    }
}


