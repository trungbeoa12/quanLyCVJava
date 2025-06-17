package quanlycongviec;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class CongViecTableModel extends AbstractTableModel {
    // Cột 0: Nút expand/collapse hoặc STT
    private final String[] columnNames = {"", "Tiêu đề", "Ngày tháng", "Mô tả", "Hạn chót", "Trạng thái", "Người thực hiện", "Ghi chú", "Đường dẫn"};
    // Danh sách công việc gốc (chỉ cha cấp 1)
    private List<CongViec> danhSachGoc;
    // Danh sách đã flatten theo expand/collapse (dùng để hiển thị)
    private List<RowItem> flattenedList;

    // Dùng class phụ để giữ trạng thái hiển thị (level)
    public static class RowItem {
        CongViec task;
        int level; // 0: cha, 1: subtask
        public RowItem(CongViec t, int l) { task = t; level = l; }
    }

    public CongViecTableModel(List<CongViec> danhSachGoc) {
        this.danhSachGoc = danhSachGoc;
        updateFlattened();
    }

    // Tạo lại list hiển thị theo expand/collapse
    public void updateFlattened() {
        flattenedList = new ArrayList<>();
        int idx = 1;
        for (CongViec task : danhSachGoc) {
            flattenedList.add(new RowItem(task, 0));
            if (task.isExpanded()) {
                for (CongViec sub : task.getSubTasks()) {
                    flattenedList.add(new RowItem(sub, 1));
                }
            }
        }
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return flattenedList.size();
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
        RowItem item = flattenedList.get(rowIndex);
        CongViec cv = item.task;
        boolean isSub = item.level > 0;

        switch (columnIndex) {
            case 0:
                if (!isSub) {
                    // Nếu là cha, hiện [+]/[-] nếu có subtask, còn không thì để trống
                    if (cv.getSubTasks() != null && cv.getSubTasks().size() > 0) {
                        return cv.isExpanded() ? "−" : "+";
                    } else {
                        return ""; // Không có subtask thì để trống
                    }
                } else {
                    return ""; // Subtask thì không nút gì cả
                }
            case 1:
                // Tiêu đề: thụt lề cho subtask và thêm prefix
                if (isSub) {
                    return "    ↳ " + cv.getTieuDe();
                } else {
                    return "● " + cv.getTieuDe();
                }
            case 2: return cv.getNgayThang();
            case 3: return cv.getMoTa();
            case 4: return cv.getHanChot();
            case 5: return cv.isHoanThanh() ? "✓" : "✗";
            case 6: return cv.getNguoiThucHien();
            case 7: return cv.getGhiChu();
            case 8: return cv.getDuongDan();
            default: return null;
        }
    }

    public CongViec getTaskAt(int row) {
        return flattenedList.get(row).task;
    }

    public int getLevelAt(int row) {    
        return flattenedList.get(row).level;
    }

    public void setDanhSachGoc(List<CongViec> danhSach) {
        this.danhSachGoc = danhSach;
        updateFlattened();
    }

    // Thêm phương thức để lấy RowItem tại một hàng
    public RowItem getRowItemAt(int row) {
        if (row >= 0 && row < flattenedList.size()) {
            return flattenedList.get(row);
        }
        return null;
    }
}

