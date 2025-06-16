package quanlycongviec;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CongViecTableModel extends AbstractTableModel {
    private final String[] columnNames = {"STT", "Tiêu đề", "Mô tả", "Hạn chót", "Trạng thái", "Ưu tiên", "Người thực hiện", "Ghi chú"};
    private List<CongViec> danhSach;

    public CongViecTableModel(List<CongViec> danhSach) {
        this.danhSach = danhSach;
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
        CongViec cv = danhSach.get(rowIndex);
        switch (columnIndex) {
            case 0: return rowIndex + 1;
            case 1: return cv.getTieuDe();
            case 2: return cv.getMoTa();
            case 3: return cv.getHanChot();
            case 4: return cv.isHoanThanh() ? "✓" : "✗";
            case 5: return cv.getUuTien();
            case 6: return cv.getNguoiThucHien();
            case 7: return cv.getGhiChu();
            default: return null;
        }
    }

    public CongViec getCongViecAt(int row) {
        return danhSach.get(row);
    }

    public void setDanhSach(List<CongViec> danhSach) {
        this.danhSach = danhSach;
        fireTableDataChanged();
    }

    public void addCongViec(CongViec cv) {
        danhSach.add(cv);
        fireTableRowsInserted(danhSach.size() - 1, danhSach.size() - 1);
    }

    public void removeCongViec(int row) {
        danhSach.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void updateCongViec(int row, CongViec cv) {
        danhSach.set(row, cv);
        fireTableRowsUpdated(row, row);
    }
} 