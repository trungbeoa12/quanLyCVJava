package quanlycongviec;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LichHopTableModel extends AbstractTableModel {
    private List<LichHop> danhSach;
    private SimpleDateFormat dateFormat;
    private String[] columnNames = {"ID", "Tên họp", "Thời gian bắt đầu", "Thời gian kết thúc", 
            "Địa điểm", "Nội dung", "Người tham dự", "Trạng thái"};
    
    public LichHopTableModel() {
        danhSach = new ArrayList<>();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    }
    
    public void setDanhSach(List<LichHop> danhSach) {
        this.danhSach = danhSach;
        fireTableDataChanged();
    }
    
    public LichHop getLichHop(int row) {
        return danhSach.get(row);
    }
    
    @Override
    public int getRowCount() {
        return danhSach.size();
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        LichHop lh = danhSach.get(rowIndex);
        switch (columnIndex) {
            case 0: return lh.getId();
            case 1: return lh.getTenHop();
            case 2: return dateFormat.format(lh.getThoiGianBatDau());
            case 3: return dateFormat.format(lh.getThoiGianKetThuc());
            case 4: return lh.getDiaDiem();
            case 5: return lh.getNoiDung();
            case 6: return lh.getNguoiThamDu();
            case 7: 
                if (lh.isDaDienRa()) return "Đã diễn ra";
                if (lh.isDangDienRa()) return "Đang diễn ra";
                if (lh.isSapDienRa()) return "Sắp diễn ra";
                return "Không xác định";
            default: return null;
        }
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
} 