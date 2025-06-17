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
        if (cv == null) {
            System.out.println("Không thể thêm công việc null!");
            return;
        }
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
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return new ArrayList<>(danhSach);
        }
        List<CongViec> ketQua = new ArrayList<>();
        tuKhoa = tuKhoa.toLowerCase().trim();
        for (CongViec cv : danhSach) {
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
        File file = new File(FILE_NAME);
        try {
            // Kiểm tra quyền ghi
            if (file.exists() && !file.canWrite()) {
                System.out.println("Không có quyền ghi vào file " + FILE_NAME);
                return;
            }
            
            // Tạo thư mục nếu chưa tồn tại
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                // Lưu từng công việc chính
                for (CongViec cv : danhSach) {
                    writer.println(cv.toCSV());
                    // Lưu tiếp các subtask nếu có
                    for (CongViec sub : cv.getSubTasks()) {
                        writer.println(sub.toCSV());
                    }
                }
                System.out.println("Đã lưu vào file " + FILE_NAME);
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi lưu file: " + e.getMessage());
        }
    }

    public void docTuFile() {
        danhSach.clear();
        File file = new File(FILE_NAME);
        
        // Nếu file không tồn tại, tạo file mới
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Đã tạo file mới " + FILE_NAME);
                return;
            } catch (IOException e) {
                System.out.println("Không thể tạo file mới: " + e.getMessage());
                return;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            CongViec currentParent = null;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    if (line.trim().isEmpty()) continue;
                    
                    CongViec cv = CongViec.fromCSV(line);
                    if (!cv.isSubTask()) {
                        // Task chính
                        danhSach.add(cv);
                        currentParent = cv;
                    } else if (currentParent != null) {
                        // Task con, gán vào cha gần nhất
                        currentParent.addSubTask(cv);
                    } else {
                        System.out.println("Lỗi định dạng file: Task con không có task cha (dòng " + lineNumber + ")");
                    }
                } catch (Exception e) {
                    System.out.println("Lỗi đọc dòng " + lineNumber + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Không thể đọc file: " + e.getMessage());
        }
    }

    private boolean kiemTraNgayHopLe(String ngay) {
        if (ngay == null || ngay.trim().isEmpty()) {
            return false;
        }
        try {
            dateFormat.setLenient(false);
            dateFormat.parse(ngay.trim());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}

