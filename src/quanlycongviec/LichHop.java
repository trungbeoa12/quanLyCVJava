package quanlycongviec;

import java.util.Date;

public class LichHop {
    private int id;
    private String tenHop;
    private Date thoiGianBatDau;
    private Date thoiGianKetThuc;
    private String diaDiem;
    private String noiDung;
    private String nguoiThamDu;
    
    public LichHop() {
    }
    
    public LichHop(int id, String tenHop, Date thoiGianBatDau, Date thoiGianKetThuc, 
            String diaDiem, String noiDung, String nguoiThamDu) {
        this.id = id;
        this.tenHop = tenHop;
        this.thoiGianBatDau = thoiGianBatDau;
        this.thoiGianKetThuc = thoiGianKetThuc;
        this.diaDiem = diaDiem;
        this.noiDung = noiDung;
        this.nguoiThamDu = nguoiThamDu;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTenHop() {
        return tenHop;
    }
    
    public void setTenHop(String tenHop) {
        this.tenHop = tenHop;
    }
    
    public Date getThoiGianBatDau() {
        return thoiGianBatDau;
    }
    
    public void setThoiGianBatDau(Date thoiGianBatDau) {
        this.thoiGianBatDau = thoiGianBatDau;
    }
    
    public Date getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }
    
    public void setThoiGianKetThuc(Date thoiGianKetThuc) {
        this.thoiGianKetThuc = thoiGianKetThuc;
    }
    
    public String getDiaDiem() {
        return diaDiem;
    }
    
    public void setDiaDiem(String diaDiem) {
        this.diaDiem = diaDiem;
    }
    
    public String getNoiDung() {
        return noiDung;
    }
    
    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }
    
    public String getNguoiThamDu() {
        return nguoiThamDu;
    }
    
    public void setNguoiThamDu(String nguoiThamDu) {
        this.nguoiThamDu = nguoiThamDu;
    }
    
    public boolean isDaDienRa() {
        return thoiGianKetThuc.before(new Date());
    }
    
    public boolean isSapDienRa() {
        Date now = new Date();
        return thoiGianBatDau.after(now);
    }
    
    public boolean isDangDienRa() {
        Date now = new Date();
        return thoiGianBatDau.before(now) && thoiGianKetThuc.after(now);
    }
} 