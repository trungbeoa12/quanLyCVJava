package quanlycongviec;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class GiaoDienChinh extends JFrame {
    private QuanLyCongViec ql;
    private JTable table;
    private CongViecTableModel tableModel;
    private JTextField txtTimKiem;
    private JButton btnThem, btnXoa, btnLuu, btnTimKiem;
    private JCheckBox chkHoanThanh, chkChuaHoanThanh;

    // --- Tông màu văn phòng nhã nhặn ---
    private static final Color PRIMARY_COLOR = new Color(33, 37, 41); // text đen/xám đậm
    private static final Color BACKGROUND_COLOR = new Color(244, 246, 248); // nền xám sáng
    private static final Color PANEL_BACKGROUND = new Color(255, 255, 255); // panel trắng
    private static final Color TABLE_HEADER_BG = new Color(222, 226, 230); // header bảng xám trung tính
    private static final Color TABLE_HEADER_FG = PRIMARY_COLOR;
    private static final Color TABLE_ROW_BG1 = new Color(248, 249, 250); // dòng lẻ xám rất nhạt
    private static final Color TABLE_ROW_BG2 = new Color(237, 242, 247); // dòng chẵn xám nhạt
    private static final Color TABLE_ROW_SELECTED = new Color(197, 220, 242); // xanh nhạt khi chọn
    private static final Color BORDER_COLOR = new Color(206, 212, 218); // border xám nhạt
    private static final Color SUCCESS_COLOR = new Color(33, 150, 243); // xanh dương nhạt cho nút Thêm
    private static final Color DANGER_COLOR = new Color(229, 115, 115); // đỏ nhạt cho nút Xóa
    private static final Color SAVE_COLOR = new Color(96, 125, 139); // xám xanh cho nút Lưu
    private static final Color SEARCH_BTN_BG = new Color(176, 190, 197); // xám trung tính cho nút Tìm
    private static final Color SEARCH_BTN_FG = PRIMARY_COLOR;
    private static final Color ACCENT_COLOR = PRIMARY_COLOR; // text chính

    public GiaoDienChinh() {
        ql = new QuanLyCongViec();
        setTitle("Quản Lý Công Việc");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Gộp các panel trên vào một panel dọc
        JPanel topGroupPanel = new JPanel();
        topGroupPanel.setLayout(new BoxLayout(topGroupPanel, BoxLayout.Y_AXIS));
        topGroupPanel.setBackground(BACKGROUND_COLOR);
        topGroupPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Panel bộ lọc trạng thái
        JPanel filterPanel = createStyledPanel("Bộ lọc trạng thái");
        chkHoanThanh = createStyledCheckBox("Đã hoàn thành", true);
        chkChuaHoanThanh = createStyledCheckBox("Chưa hoàn thành", true);
        filterPanel.add(chkHoanThanh);
        filterPanel.add(chkChuaHoanThanh);

        // Panel tìm kiếm
        JPanel searchPanel = createStyledPanel("Tìm kiếm");
        searchPanel.add(new JLabel("<html><font color='black'>Tìm kiếm:</font></html>"));
        txtTimKiem = new JTextField(20);
        txtTimKiem.setPreferredSize(new Dimension(200, 30));
        txtTimKiem.setBackground(TABLE_ROW_BG2);
        txtTimKiem.setForeground(PRIMARY_COLOR);
        txtTimKiem.setCaretColor(PRIMARY_COLOR);
        txtTimKiem.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        searchPanel.add(txtTimKiem);
        btnTimKiem = createStyledButton("Tìm", SEARCH_BTN_BG, SEARCH_BTN_FG, BORDER_COLOR);
        searchPanel.add(btnTimKiem);

        // Button panel
        JPanel buttonPanel = createStyledPanel("");
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnThem = createStyledButton("Thêm công việc chính", SUCCESS_COLOR, Color.WHITE, SUCCESS_COLOR.darker());
        btnXoa = createStyledButton("Xóa công việc", DANGER_COLOR, Color.WHITE, DANGER_COLOR.darker());
        btnLuu = createStyledButton("Tải lại dữ liệu", SAVE_COLOR, Color.WHITE, SAVE_COLOR.darker());
        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnLuu);

        topGroupPanel.add(filterPanel);
        topGroupPanel.add(searchPanel);
        topGroupPanel.add(buttonPanel);

        // Table setup
        tableModel = new CongViecTableModel(ql.getDanhSach());
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(TABLE_HEADER_BG);
        table.getTableHeader().setForeground(TABLE_HEADER_FG);
        table.setGridColor(BORDER_COLOR);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(PANEL_BACKGROUND);
        table.setForeground(PRIMARY_COLOR);
        table.setSelectionBackground(TABLE_ROW_SELECTED);
        table.setSelectionForeground(PRIMARY_COLOR);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(PANEL_BACKGROUND);

        mainPanel.add(topGroupPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);

        // Bộ lọc trạng thái
        chkHoanThanh.addActionListener(e -> capNhatBang());
        chkChuaHoanThanh.addActionListener(e -> capNhatBang());

        // Nút thêm task chính
        btnThem.addActionListener(e -> {
            CongViecDialog dialog = new CongViecDialog(this, null);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                CongViec cv = dialog.getCongViec();
                int newId = ql.themCongViec(cv);
                cv.setId(newId);
                capNhatBang();
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
                int confirm = JOptionPane.showConfirmDialog(this, "Xóa công việc này và toàn bộ công việc con?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    ql.xoaCongViec(cv.getId());
                    capNhatBang();
                }
            } else {
                // Subtask: xóa khỏi DB bằng id
                ql.xoaCongViec(cv.getId());
                capNhatBang();
            }
        });

        // Nút tải lại dữ liệu
        btnLuu.addActionListener(e -> {
            capNhatBang();
            JOptionPane.showMessageDialog(this, "Đã tải lại dữ liệu từ database!");
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
                    sub.setSubTask(true);  // Đánh dấu là subtask
                    int newId = ql.themSubTask(sub, cha.getId());
                    sub.setId(newId);
                    cha.setExpanded(true);  // Mở rộng công việc cha
                    capNhatBang();
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

                // Xử lý click vào cột Đường dẫn
                if (col == 8 && row != -1) {
                    int modelRow = table.convertRowIndexToModel(row);
                    CongViecTableModel.RowItem item = tableModel.getRowItemAt(modelRow);
                    if (item != null) {
                        String path = item.task.getDuongDan();
                        if (path != null && !path.isEmpty()) {
                            try {
                                File file = new File(path);
                                File folderToOpen = file.isDirectory() ? file : file.getParentFile();
                                if (folderToOpen != null && folderToOpen.exists()) {
                                    String os = System.getProperty("os.name").toLowerCase();
                                    if (os.contains("linux")) {
                                        Runtime.getRuntime().exec(new String[]{"xdg-open", folderToOpen.getAbsolutePath()});
                                    } else if (os.contains("win")) {
                                        Runtime.getRuntime().exec(new String[]{"explorer", folderToOpen.getAbsolutePath()});
                                    } else if (os.contains("mac")) {
                                        Runtime.getRuntime().exec(new String[]{"open", folderToOpen.getAbsolutePath()});
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(GiaoDienChinh.this, "Đường dẫn không tồn tại!");
                                }
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(GiaoDienChinh.this, "Không thể mở thư mục: " + ex.getMessage());
                            }
                        }
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
                            // Cập nhật vào DB
                            ql.capNhatCongViec(oldCv.getId(), newCv);
                            capNhatBang();
                        }
                    }
                }
            }
        });

        // Add this line after table setup
        setupTableRenderers();

        capNhatBang();
    }

    private void capNhatBang() {
        List<CongViec> danhSach = ql.getDanhSach();
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
                cv.getNgayThang().toLowerCase().contains(tuKhoa) ||
                cv.getHanChot().toLowerCase().contains(tuKhoa) ||
                cv.getNguoiThucHien().toLowerCase().contains(tuKhoa) ||
                cv.getGhiChu().toLowerCase().contains(tuKhoa) ||
                (cv.getDuongDan() != null && cv.getDuongDan().toLowerCase().contains(tuKhoa))
            )
            .collect(Collectors.toList());
        tableModel.setDanhSachGoc(ketQua);
    }

    private JPanel createStyledPanel(String title) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(PANEL_BACKGROUND);
        if (!title.isEmpty()) {
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    title,
                    javax.swing.border.TitledBorder.LEFT,
                    javax.swing.border.TitledBorder.TOP,
                    new Font("Segoe UI", Font.BOLD, 14),
                    ACCENT_COLOR
                ),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
        } else {
            panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }
        return panel;
    }

    private JCheckBox createStyledCheckBox(String text, boolean selected) {
        JCheckBox checkBox = new JCheckBox(text, selected);
        checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        checkBox.setBackground(PANEL_BACKGROUND);
        checkBox.setForeground(ACCENT_COLOR);
        checkBox.setFocusPainted(false);
        return checkBox;
    }

    // --- Hàm tạo nút chuyên nghiệp, nhã nhặn ---
    private JButton createStyledButton(String text, Color bg, Color fg, Color borderColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(fg);
        button.setBackground(bg);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 1, true),
            BorderFactory.createEmptyBorder(4, 20, 4, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(170, 40));
        button.setOpaque(true);
        button.setFocusable(false);
        // Hover effect: sáng nhẹ lên
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(bg.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(bg);
            }
        });
        return button;
    }
    // --------------------------------------

    private void setupTableRenderers() {
        // Renderer cho cột tiêu đề
        table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String text = (String) value;
                if (text.startsWith("    ↳")) {
                    c.setForeground(new Color(76, 175, 80)); // xanh lá nhạt cho subtask
                    c.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                } else {
                    c.setForeground(PRIMARY_COLOR);
                    c.setFont(new Font("Segoe UI", Font.BOLD, 14));
                }
                if (isSelected) {
                    c.setBackground(TABLE_ROW_SELECTED);
                } else {
                    c.setBackground(row % 2 == 0 ? TABLE_ROW_BG1 : TABLE_ROW_BG2);
                }
                return c;
            }
        });
        // Renderer cho trạng thái
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (String) value;
                if ("✓".equals(status)) {
                    c.setForeground(new Color(56, 142, 60)); // xanh lá nhạt
                } else {
                    c.setForeground(new Color(211, 47, 47)); // đỏ nhạt
                }
                if (isSelected) {
                    c.setBackground(TABLE_ROW_SELECTED);
                } else {
                    c.setBackground(row % 2 == 0 ? TABLE_ROW_BG1 : TABLE_ROW_BG2);
                }
                return c;
            }
        });
        // Default renderer cho các cột khác
        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(PRIMARY_COLOR);
                if (isSelected) {
                    c.setBackground(TABLE_ROW_SELECTED);
                } else {
                    c.setBackground(row % 2 == 0 ? TABLE_ROW_BG1 : TABLE_ROW_BG2);
                }
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (table.getColumnModel().getColumn(i).getCellRenderer() == null) {
                table.getColumnModel().getColumn(i).setCellRenderer(defaultRenderer);
            }
        }
    }

    public static void main(String[] args) {
        try { Class.forName("org.sqlite.JDBC"); } catch (Exception e) { e.printStackTrace(); }
        SwingUtilities.invokeLater(() -> {
            new GiaoDienChinh().setVisible(true);
        });
    }
}
