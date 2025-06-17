package quanlycongviec;

import java.util.*;

public class QuanLyCongViec {
    private DatabaseHelper dbHelper;

    public QuanLyCongViec() {
        dbHelper = new DatabaseHelper();
    }

    // Lấy danh sách công việc (bao gồm cả subtask)
    public List<CongViec> getDanhSach() {
        List<DatabaseHelper.CongViecWithId> list = dbHelper.getAllCongViec();
        List<CongViec> result = new ArrayList<>();
        for (DatabaseHelper.CongViecWithId c : list) {
            result.add(c.cv);
        }
        return result;
    }

    // Thêm công việc chính (trả về id mới)
    public int themCongViec(CongViec cv) {
        return dbHelper.insertCongViec(cv, null);
    }

    // Thêm subtask (trả về id mới)
    public int themSubTask(CongViec sub, int parentId) {
        return dbHelper.insertCongViec(sub, parentId);
    }

    // Cập nhật công việc (cần truyền id)
    public void capNhatCongViec(int id, CongViec cv) {
        dbHelper.updateCongViec(id, cv);
    }

    // Xóa công việc (và subtask nếu là cha)
    public void xoaCongViec(int id) {
        dbHelper.deleteCongViec(id);
    }

    public void danhDauHoanThanh(int index) {
        if (index >= 0 && index < getDanhSach().size()) {
            CongViec cv = getDanhSach().get(index);
            cv.setHoanThanh(!cv.isHoanThanh());
            System.out.println("Đã cập nhật trạng thái công việc!");
        } else {
            System.out.println("Vị trí không hợp lệ!");
        }
    }

    public List<CongViec> timKiem(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return new ArrayList<>(getDanhSach());
        }
        List<CongViec> ketQua = new ArrayList<>();
        tuKhoa = tuKhoa.toLowerCase().trim();
        for (CongViec cv : getDanhSach()) {
            if ((cv.getTieuDe() != null && cv.getTieuDe().toLowerCase().contains(tuKhoa)) ||
                (cv.getMoTa() != null && cv.getMoTa().toLowerCase().contains(tuKhoa)) ||
                (cv.getNguoiThucHien() != null && cv.getNguoiThucHien().toLowerCase().contains(tuKhoa)) ||
                (cv.getGhiChu() != null && cv.getGhiChu().toLowerCase().contains(tuKhoa))) {
                ketQua.add(cv);
            }
        }
        return ketQua;
    }

    public void hienThiDanhSach() {
        if (getDanhSach().isEmpty()) {
            System.out.println("Danh sách công việc trống!");
            return;
        }
        System.out.println("\n=== DANH SÁCH CÔNG VIỆC ===");
        for (int i = 0; i < getDanhSach().size(); i++) {
            System.out.println((i + 1) + ". " + getDanhSach().get(i));
        }
        System.out.println("===========================");
    }
}

