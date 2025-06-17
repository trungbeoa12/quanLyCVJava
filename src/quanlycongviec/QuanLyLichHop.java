package quanlycongviec;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class QuanLyLichHop {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private List<LichHop> danhSach;
    
    public QuanLyLichHop() {
        danhSach = LichHopStorage.loadFromFile();
    }
    
    public void themLichHop(LichHop lichHop) {
        // Tìm ID lớn nhất hiện tại
        int maxId = 0;
        for (LichHop lh : danhSach) {
            if (lh.getId() > maxId) {
                maxId = lh.getId();
            }
        }
        lichHop.setId(maxId + 1);
        
        danhSach.add(lichHop);
        LichHopStorage.saveToFile(danhSach);
    }
    
    public void capNhatLichHop(LichHop lichHop) {
        for (int i = 0; i < danhSach.size(); i++) {
            if (danhSach.get(i).getId() == lichHop.getId()) {
                danhSach.set(i, lichHop);
                LichHopStorage.saveToFile(danhSach);
                break;
            }
        }
    }
    
    public void xoaLichHop(int id) {
        danhSach.removeIf(lh -> lh.getId() == id);
        LichHopStorage.saveToFile(danhSach);
    }
    
    public List<LichHop> getDanhSach() {
        return danhSach;
    }
    
    public List<LichHop> timKiem(String tuKhoa) {
        List<LichHop> ketQua = new ArrayList<>();
        String tuKhoaLower = tuKhoa.toLowerCase();
        
        for (LichHop lh : danhSach) {
            if (lh.getTenHop().toLowerCase().contains(tuKhoaLower) ||
                lh.getDiaDiem().toLowerCase().contains(tuKhoaLower) ||
                lh.getNguoiThamDu().toLowerCase().contains(tuKhoaLower)) {
                ketQua.add(lh);
            }
        }
        
        return ketQua;
    }
    
    public List<LichHop> locTheoThoiGian(boolean chiLaySapDienRa) {
        List<LichHop> ketQua = new ArrayList<>();
        long thoiGianHienTai = System.currentTimeMillis();
        
        for (LichHop lh : danhSach) {
            if (chiLaySapDienRa) {
                if (lh.getThoiGianBatDau().getTime() > thoiGianHienTai) {
                    ketQua.add(lh);
                }
            } else {
                ketQua.add(lh);
            }
        }
        
        return ketQua;
    }
    
    public List<LichHop> locTheoNguoiThamDu(String nguoiThamDu) {
        List<LichHop> ketQua = new ArrayList<>();
        String nguoiThamDuLower = nguoiThamDu.toLowerCase();
        
        for (LichHop lh : danhSach) {
            if (lh.getNguoiThamDu().toLowerCase().contains(nguoiThamDuLower)) {
                ketQua.add(lh);
            }
        }
        
        return ketQua;
    }
} 