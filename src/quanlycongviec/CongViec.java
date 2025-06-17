package quanlycongviec;

import java.util.ArrayList;
import java.util.List;

public class CongViec {
    private String tieuDe;
    private String moTa;
    private String hanChot;
    private boolean hoanThanh;
    private String uuTien;
    private String nguoiThucHien;
    private String ghiChu;
    private String duongDan;
    private List<CongViec> subTasks; // Công việc con (nếu có)
    private boolean isExpanded = false; // trạng thái expand/collapse
    private boolean isSubTask = false;  // đánh dấu là task con

    public CongViec(String tieuDe, String moTa, String hanChot, boolean hoanThanh,
                    String uuTien, String nguoiThucHien, String ghiChu, String duongDan) {
        this.tieuDe = tieuDe != null ? tieuDe.trim() : "";
        this.moTa = moTa != null ? moTa.trim() : "";
        this.hanChot = hanChot != null ? hanChot.trim() : "";
        this.hoanThanh = hoanThanh;
        this.uuTien = uuTien != null ? uuTien.trim() : "";
        this.nguoiThucHien = nguoiThucHien != null ? nguoiThucHien.trim() : "";
        this.ghiChu = ghiChu != null ? ghiChu.trim() : "";
        this.duongDan = duongDan != null ? duongDan.trim() : "";
        this.subTasks = new ArrayList<>();
    }

    // Dùng cho subtask
    public CongViec(String tieuDe, String moTa, String hanChot, boolean hoanThanh,
                    String uuTien, String nguoiThucHien, String ghiChu, String duongDan, boolean isSubTask) {
        this(tieuDe, moTa, hanChot, hoanThanh, uuTien, nguoiThucHien, ghiChu, duongDan);
        this.isSubTask = isSubTask;
    }

    // Getter/Setter
    public String getTieuDe() { return tieuDe != null ? tieuDe : ""; }
    public String getMoTa() { return moTa != null ? moTa : ""; }
    public String getHanChot() { return hanChot != null ? hanChot : ""; }
    public boolean isHoanThanh() { return hoanThanh; }
    public String getUuTien() { return uuTien != null ? uuTien : ""; }
    public String getNguoiThucHien() { return nguoiThucHien != null ? nguoiThucHien : ""; }
    public String getGhiChu() { return ghiChu != null ? ghiChu : ""; }
    public String getDuongDan() { return duongDan != null ? duongDan : ""; }
    public List<CongViec> getSubTasks() { return subTasks != null ? subTasks : new ArrayList<>(); }
    public void setSubTasks(List<CongViec> subTasks) { this.subTasks = subTasks != null ? subTasks : new ArrayList<>(); }
    public boolean isExpanded() { return isExpanded; }
    public void setExpanded(boolean expanded) { isExpanded = expanded; }
    public boolean isSubTask() { return isSubTask; }
    public void setSubTask(boolean subTask) { isSubTask = subTask; }

    public void setHoanThanh(boolean hoanThanh) { this.hoanThanh = hoanThanh; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe != null ? tieuDe.trim() : ""; }
    public void setMoTa(String moTa) { this.moTa = moTa != null ? moTa.trim() : ""; }
    public void setHanChot(String hanChot) { this.hanChot = hanChot != null ? hanChot.trim() : ""; }
    public void setUuTien(String uuTien) { this.uuTien = uuTien != null ? uuTien.trim() : ""; }
    public void setNguoiThucHien(String nguoiThucHien) { this.nguoiThucHien = nguoiThucHien != null ? nguoiThucHien.trim() : ""; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu != null ? ghiChu.trim() : ""; }
    public void setDuongDan(String duongDan) { this.duongDan = duongDan != null ? duongDan.trim() : ""; }

    public void addSubTask(CongViec subTask) {
        if (subTask != null) {
            subTask.setSubTask(true);
            this.subTasks.add(subTask);
        }
    }

    public String toCSV() {
        // Đánh dấu có phải subtask không để khi load lên phân biệt
        return (tieuDe != null ? tieuDe : "") + ";" +
               (moTa != null ? moTa : "") + ";" +
               (hanChot != null ? hanChot : "") + ";" +
               hoanThanh + ";" +
               (uuTien != null ? uuTien : "") + ";" +
               (nguoiThucHien != null ? nguoiThucHien : "") + ";" +
               (ghiChu != null ? ghiChu : "") + ";" +
               (duongDan != null ? duongDan : "") + ";" +
               isSubTask;
    }

    public static CongViec fromCSV(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Dòng CSV không được để trống");
        }

        String[] parts = line.split(";", -1);
        if (parts.length < 4) {
            throw new IllegalArgumentException("Định dạng CSV không hợp lệ: thiếu thông tin bắt buộc");
        }

        boolean isSubTask = false;
        if (parts.length >= 9) {
            isSubTask = Boolean.parseBoolean(parts[8]);
        }

        return new CongViec(
            parts[0], parts[1], parts[2], Boolean.parseBoolean(parts[3]),
            parts.length > 4 ? parts[4] : "",
            parts.length > 5 ? parts[5] : "",
            parts.length > 6 ? parts[6] : "",
            parts.length > 7 ? parts[7] : "",
            isSubTask
        );
    }

    @Override
    public String toString() {
        return (isSubTask ? "    [Sub]" : "[Main]") + " " + (tieuDe != null ? tieuDe : "");
    }
}

