package quanlycongviec;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

public class GiaoDienChinh extends JFrame {
    private QuanLyCongViec ql;
    private JTable table;
    private CongViecTableModel tableModel;
    private JTextField txtTimKiem;
    private JButton btnThem, btnXoa, btnLuu, btnTimKiem;
    private JCheckBox chkHoanThanh, chkChuaHoanThanh;

    public GiaoDienChinh() {
        ql = new QuanLyCongViec();
        ql.docTuFile();
        setTitle("Quản Lý Công Việc");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Panel bộ lọc trạng thái
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Bộ lọc trạng thái"));
        chkHoanThanh = new JCheckBox("Đã hoàn thành", true);
        chkChuaHoanThanh = new JCheckBox("Chưa hoàn thành", true);
        filterPanel.add(chkHoanThanh);
        filterPanel.add(chkChuaHoanThanh);

        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));
        searchPanel.add(new JLabel("Tìm kiếm:"));
        txtTimKiem = new JTextField(20);
        searchPanel.add(txtTimKiem);
        btnTimKiem = new JButton("Tìm");
        searchPanel.add(btnTimKiem);

        // Bảng công việc
        tableModel = new CongViecTableModel(ql.getDanhSach());
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        JScrollPane scrollPane = new JScrollPane(table);

        // Renderer cho cột tiêu đề
        table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String text = (String) value;
                if (text.startsWith("    ↳")) {
                    // Công việc phụ: màu xanh lá
                    c.setForeground(new Color(0, 128, 0));
                    c.setFont(new Font("Arial", Font.ITALIC, 14));
                } else {
                    // Công việc chính: màu đen đậm
                    c.setForeground(Color.BLACK);
                    c.setFont(new Font("Arial", Font.BOLD, 14));
                }
                return c;
            }
        });

        // Renderer cho trạng thái, ưu tiên
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (String) value;
                if ("✓".equals(status)) c.setForeground(new Color(0, 128, 0));
                else c.setForeground(Color.RED);
                return c;
            }
        });
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String priority = (String) value;
                if (priority != null && priority.toLowerCase().contains("cao")) c.setForeground(Color.RED);
                else if (priority != null && priority.toLowerCase().contains("trung bình")) c.setForeground(new Color(255, 140, 0));
                else c.setForeground(new Color(0, 128, 0));
                return c;
            }
        });

        // Panel nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnThem = new JButton("Thêm công việc chính");
        btnXoa = new JButton("Xóa công việc");
        btnLuu = new JButton("Lưu tất cả");
        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnLuu);

        // Đặt các panel lên layout chính
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1; gbc.gridwidth = 2;
        add(filterPanel, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 1; gbc.gridwidth = 2;
        add(searchPanel, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 1; gbc.gridwidth = 2;
        add(buttonPanel, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 1; gbc.weighty = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

        // Bộ lọc trạng thái
        chkHoanThanh.addActionListener(e -> capNhatBang());
        chkChuaHoanThanh.addActionListener(e -> capNhatBang());

        // Nút thêm task chính
        btnThem.addActionListener(e -> {
            CongViecDialog dialog = new CongViecDialog(this, null);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                CongViec cv = dialog.getCongViec();
                ql.getDanhSach().add(cv);
                tableModel.updateFlattened();
            }
        });

        // Nút xóa task (chỉ xóa task chính/subtask được chọn)
        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn công việc cần xóa!");
                return;
            }
            int modelRow = table.convertRowIndexToModel(row);
            CongViecTableModel.RowItem item = tableModel.getRowItemAt(modelRow);
            if (item == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy công việc cần xóa!");
                return;
            }
            CongViec cv = item.task;
            if (item.level == 0) {
                // Task cha: xóa luôn cả subtask (sẽ hỏi xác nhận)
                int confirm = JOptionPane.showConfirmDialog(this, "Xóa công việc này và toàn bộ công việc con?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    ql.getDanhSach().remove(cv);
                    tableModel.updateFlattened();
                }
            } else {
                // Subtask: xóa khỏi task cha
                for (CongViec cha : ql.getDanhSach()) {
                    if (cha.getSubTasks().contains(cv)) {
                        cha.getSubTasks().remove(cv);
                        break;
                    }
                }
                tableModel.updateFlattened();
            }
        });

        // Nút lưu
        btnLuu.addActionListener(e -> {
            ql.luuVaoFile();
            JOptionPane.showMessageDialog(this, "Đã lưu tất cả công việc!");
        });

        // Sự kiện tìm kiếm
        btnTimKiem.addActionListener(e -> timKiemCongViec());

        // Chuột phải: thêm subtask cho task cha
        JPopupMenu menu = new JPopupMenu();
        JMenuItem itemAddSub = new JMenuItem("Thêm công việc con");
        menu.add(itemAddSub);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handlePopup(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                handlePopup(e);
            }
            private void handlePopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row != -1) {
                        table.setRowSelectionInterval(row, row);
                        int modelRow = table.convertRowIndexToModel(row);
                        CongViecTableModel.RowItem item = tableModel.getRowItemAt(modelRow);
                        if (item != null && item.level == 0) { // Chỉ task cha mới thêm subtask
                            menu.show(table, e.getX(), e.getY());
                            itemAddSub.putClientProperty("parentTask", item.task);
                        }
                    }
                }
            }
        });

        itemAddSub.addActionListener(e -> {
            CongViec cha = (CongViec) itemAddSub.getClientProperty("parentTask");
            if (cha != null) {
                CongViecDialog dialog = new CongViecDialog(this, null);
                dialog.setTitle("Thêm công việc con");
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    CongViec sub = dialog.getCongViec();
                    cha.addSubTask(sub);
                    cha.setExpanded(true); // Tự động expand
                    tableModel.updateFlattened();
                }
            }
        });

        // Sự kiện click expand/collapse
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                // Cột đầu expand/collapse
                if (col == 0 && row != -1) {
                    int modelRow = table.convertRowIndexToModel(row);
                    CongViecTableModel.RowItem item = tableModel.getRowItemAt(modelRow);
                    if (item != null && item.level == 0 && item.task.getSubTasks().size() > 0) {
                        item.task.setExpanded(!item.task.isExpanded());
                        tableModel.updateFlattened();
                    }
                    return;
                }

                // Double click để sửa (task chính/subtask)
                if (e.getClickCount() == 2 && row != -1 && col != 0) {
                    int modelRow = table.convertRowIndexToModel(row);
                    CongViecTableModel.RowItem item = tableModel.getRowItemAt(modelRow);
                    if (item != null) {
                        CongViec oldCv = item.task;
                        CongViecDialog dialog = new CongViecDialog(GiaoDienChinh.this, oldCv);
                        dialog.setVisible(true);
                        if (dialog.isConfirmed()) {
                            CongViec newCv = dialog.getCongViec();
                            // Copy dữ liệu vào object cũ
                            oldCv.setTieuDe(newCv.getTieuDe());
                            oldCv.setMoTa(newCv.getMoTa());
                            oldCv.setHanChot(newCv.getHanChot());
                            oldCv.setHoanThanh(newCv.isHoanThanh());
                            oldCv.setUuTien(newCv.getUuTien());
                            oldCv.setNguoiThucHien(newCv.getNguoiThucHien());
                            oldCv.setGhiChu(newCv.getGhiChu());
                            oldCv.setDuongDan(newCv.getDuongDan());
                            tableModel.updateFlattened();
                        }
                    }
                }
            }
        });

        capNhatBang();
    }

    private void capNhatBang() {
        List<CongViec> danhSach = ql.getDanhSach();
        // Lọc theo trạng thái, chỉ ở danh sách gốc (task cha)
        List<CongViec> filtered = danhSach.stream()
            .filter(cv -> (chkHoanThanh.isSelected() && cv.isHoanThanh()) ||
                          (chkChuaHoanThanh.isSelected() && !cv.isHoanThanh()))
            .collect(Collectors.toList());
        tableModel.setDanhSachGoc(filtered);
    }

    private void timKiemCongViec() {
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();
        List<CongViec> ketQua = ql.getDanhSach().stream()
            .filter(cv -> 
                cv.getTieuDe().toLowerCase().contains(tuKhoa) ||
                cv.getMoTa().toLowerCase().contains(tuKhoa) ||
                cv.getNguoiThucHien().toLowerCase().contains(tuKhoa) ||
                cv.getUuTien().toLowerCase().contains(tuKhoa) ||
                cv.getGhiChu().toLowerCase().contains(tuKhoa) ||
                (cv.getDuongDan() != null && cv.getDuongDan().toLowerCase().contains(tuKhoa))
            )
            .collect(Collectors.toList());
        tableModel.setDanhSachGoc(ketQua);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GiaoDienChinh().setVisible(true);
        });
    }
}

