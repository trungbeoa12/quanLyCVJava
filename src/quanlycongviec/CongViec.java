package quanlycongviec;

public class CongViec {
    private String tieuDe;
    private String moTa;
    private String hanChot;
    private boolean hoanThanh;
    private String uuTien;
    private String nguoiThucHien;
    private String ghiChu;

    public CongViec(String tieuDe, String moTa, String hanChot, boolean hoanThanh, String uuTien, String nguoiThucHien, String ghiChu) {
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.hanChot = hanChot;
        this.hoanThanh = hoanThanh;
        this.uuTien = uuTien;
        this.nguoiThucHien = nguoiThucHien;
        this.ghiChu = ghiChu;
    }

    public String getTieuDe() { return tieuDe; }
    public String getMoTa() { return moTa; }
    public String getHanChot() { return hanChot; }
    public boolean isHoanThanh() { return hoanThanh; }
    public String getUuTien() { return uuTien; }
    public String getNguoiThucHien() { return nguoiThucHien; }
    public String getGhiChu() { return ghiChu; }

    public void setHoanThanh(boolean hoanThanh) {
        this.hoanThanh = hoanThanh;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public void setHanChot(String hanChot) {
        this.hanChot = hanChot;
    }

    public void setUuTien(String uuTien) { this.uuTien = uuTien; }
    public void setNguoiThucHien(String nguoiThucHien) { this.nguoiThucHien = nguoiThucHien; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public String toCSV() {
        return tieuDe + "," + moTa + "," + hanChot + "," + hoanThanh + "," + uuTien + "," + nguoiThucHien + "," + ghiChu;
    }

    public static CongViec fromCSV(String line) {
        String[] parts = line.split(",", -1);
        return new CongViec(parts[0], parts[1], parts[2], Boolean.parseBoolean(parts[3]),
            parts.length > 4 ? parts[4] : "", parts.length > 5 ? parts[5] : "", parts.length > 6 ? parts[6] : "");
    }

    @Override
    public String toString() {
        return "[Tiêu đề] " + tieuDe + " | [Hạn chót] " + hanChot + " | [Hoàn thành] " + (hoanThanh ? "✓" : "✗") +
               " | [Ưu tiên] " + uuTien + " | [Người thực hiện] " + nguoiThucHien + " | [Ghi chú] " + ghiChu;
    }
}
