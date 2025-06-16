package quanlycongviec;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class QuanLyCongViec {
    private List<CongViec> danhSach = new ArrayList<>();
    private static final String FILE_NAME = "congviec.csv";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public List<CongViec> getDanhSach() {
        return danhSach;
    }

    public void themCongViec(CongViec cv) {
        if (kiemTraNgayHopLe(cv.getHanChot())) {
            danhSach.add(cv);
            System.out.println("Đã thêm công việc thành công!");
        } else {
            System.out.println("Ngày không hợp lệ! Vui lòng nhập theo định dạng dd/MM/yyyy");
        }
    }

    public void xoaCongViec(int index) {
        if (index >= 0 && index < danhSach.size()) {
            danhSach.remove(index);
            System.out.println("Đã xóa công việc thành công!");
        } else {
            System.out.println("Vị trí không hợp lệ!");
        }
    }

    public void danhDauHoanThanh(int index) {
        if (index >= 0 && index < danhSach.size()) {
            CongViec cv = danhSach.get(index);
            cv.setHoanThanh(!cv.isHoanThanh());
            System.out.println("Đã cập nhật trạng thái công việc!");
        } else {
            System.out.println("Vị trí không hợp lệ!");
        }
    }

    public List<CongViec> timKiem(String tuKhoa) {
        List<CongViec> ketQua = new ArrayList<>();
        tuKhoa = tuKhoa.toLowerCase();
        for (CongViec cv : danhSach) {
            if (cv.getTieuDe().toLowerCase().contains(tuKhoa) || 
                cv.getMoTa().toLowerCase().contains(tuKhoa)) {
                ketQua.add(cv);
            }
        }
        return ketQua;
    }

    public void hienThiDanhSach() {
        if (danhSach.isEmpty()) {
            System.out.println("Danh sách công việc trống!");
            return;
        }
        System.out.println("\n=== DANH SÁCH CÔNG VIỆC ===");
        for (int i = 0; i < danhSach.size(); i++) {
            System.out.println((i + 1) + ". " + danhSach.get(i));
        }
        System.out.println("===========================");
    }

    public void luuVaoFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (CongViec cv : danhSach) {
                writer.println(cv.toCSV());
            }
            System.out.println("Đã lưu vào file " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Lỗi khi lưu file: " + e.getMessage());
        }
    }

    public void docTuFile() {
        danhSach.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                danhSach.add(CongViec.fromCSV(line));
            }
        } catch (IOException e) {
            System.out.println("Không thể đọc file: " + e.getMessage());
        }
    }

    private boolean kiemTraNgayHopLe(String ngay) {
        try {
            dateFormat.setLenient(false);
            dateFormat.parse(ngay);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
