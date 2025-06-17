package quanlycongviec;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class GiaoDienChinh extends JFrame {
    private QuanLyCongViec ql;
    private QuanLyLichHop qlLichHop;
    private JTable table;
    private JTable tableLichHop;
    private CongViecTableModel tableModel;
    private LichHopTableModel lichHopTableModel;
    private JTextField txtTimKiem;
    private JTextField txtTimKiemLichHop;
    private JButton btnThem, btnXoa, btnLuu, btnTimKiem;
    private JButton btnThemLichHop, btnXoaLichHop, btnTimKiemLichHop;
    private JCheckBox chkHoanThanh, chkChuaHoanThanh;
    private JTabbedPane tabbedPane;
    private JCheckBox chkChiHienThiSapToi;
    private JComboBox<String> cboNguoiThamDu;

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
        qlLichHop = new QuanLyLichHop();
        lichHopTableModel = new LichHopTableModel();
        lichHopTableModel.setDanhSach(qlLichHop.getDanhSach());
        setTitle("Quản Lý Công Việc & Lịch Họp");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);

        // Tạo tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Tab Quản lý công việc
        JPanel tabCongViec = createTabCongViec();
        tabbedPane.addTab("Quản lý công việc", tabCongViec);
        
        // Tab Quản lý lịch họp
        JPanel tabLichHop = createTabLichHop();
        tabbedPane.addTab("Quản lý lịch họp", tabLichHop);
        
        add(tabbedPane);
        
        capNhatBang();
        capNhatBangLichHop();
    }

    private JPanel createTabCongViec() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

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

        // Thêm renderer cho các cột cần xuống dòng
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i == 1 || i == 3 || i == 6 || i == 7) { // Tiêu đề, Mô tả, Người thực hiện, Ghi chú
                table.getColumnModel().getColumn(i).setCellRenderer(new MultiLineCellRenderer());
                table.getColumnModel().getColumn(i).setCellEditor(new MultiLineCellEditor());
            }
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(PANEL_BACKGROUND);

        panel.add(topGroupPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Thêm các event listener
        setupCongViecEventListeners();

        return panel;
    }

    private JPanel createTabLichHop() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        
        // Panel tìm kiếm và lọc
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(PANEL_BACKGROUND);
        
        JTextField txtTimKiem = new JTextField(20);
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JButton btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnTimKiem.setBackground(PRIMARY_COLOR);
        btnTimKiem.setForeground(Color.WHITE);
        
        JCheckBox chkChiHienThiSapToi = new JCheckBox("Chỉ hiển thị lịch sắp tới");
        chkChiHienThiSapToi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkChiHienThiSapToi.setBackground(PANEL_BACKGROUND);
        
        JComboBox<String> cboNguoiThamDu = new JComboBox<>(new String[]{"Tất cả người tham dự", "Nguyễn Văn A", "Trần Thị B", "Lê Văn C"});
        cboNguoiThamDu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTimKiem);
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(chkChiHienThiSapToi);
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(new JLabel("Người tham dự:"));
        searchPanel.add(cboNguoiThamDu);
        
        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(PANEL_BACKGROUND);
        
        JButton btnThem = new JButton("Thêm lịch họp");
        JButton btnXoa = new JButton("Xóa lịch họp");
        
        btnThem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnXoa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        btnThem.setBackground(PRIMARY_COLOR);
        btnXoa.setBackground(PRIMARY_COLOR);
        
        btnThem.setForeground(Color.WHITE);
        btnXoa.setForeground(Color.WHITE);
        
        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);
        
        // Bảng lịch họp
        tableLichHop = new JTable(lichHopTableModel);
        tableLichHop.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableLichHop.setRowHeight(35);
        tableLichHop.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableLichHop.setShowGrid(false);
        tableLichHop.setIntercellSpacing(new Dimension(0, 0));
        
        // Khởi tạo dữ liệu cho bảng
        capNhatBangLichHop();
        
        // Thiết lập renderer cho các cột
        if (tableLichHop.getColumnCount() > 0) {
            MultiLineCellRenderer renderer = new MultiLineCellRenderer();
            tableLichHop.getColumnModel().getColumn(1).setCellRenderer(renderer); // Tên họp
            tableLichHop.getColumnModel().getColumn(5).setCellRenderer(renderer); // Nội dung
            tableLichHop.getColumnModel().getColumn(6).setCellRenderer(renderer); // Người tham dự
            
            // Thiết lập renderer cho cột trạng thái
            tableLichHop.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    
                    if (value != null) {
                        String status = value.toString();
                        if (status.equals("Đã diễn ra")) {
                            c.setForeground(new Color(128, 128, 128)); // Màu xám
                        } else if (status.equals("Đang diễn ra")) {
                            c.setForeground(new Color(0, 128, 0)); // Màu xanh lá
                        } else if (status.equals("Sắp diễn ra")) {
                            c.setForeground(new Color(255, 165, 0)); // Màu cam
                        }
                    }
                    
                    return c;
                }
            });
        }
        
        JScrollPane scrollPane = new JScrollPane(tableLichHop);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Thêm các panel vào tab
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Thêm sự kiện
        btnThem.addActionListener(e -> themLichHop());
        btnXoa.addActionListener(e -> xoaLichHop());
        
        btnTimKiem.addActionListener(e -> {
            String tuKhoa = txtTimKiem.getText().trim();
            if (!tuKhoa.isEmpty()) {
                List<LichHop> ketQua = qlLichHop.timKiem(tuKhoa);
                lichHopTableModel.setDanhSach(ketQua);
            } else {
                capNhatBangLichHop();
            }
        });
        
        chkChiHienThiSapToi.addActionListener(e -> {
            boolean chiHienThiSapToi = chkChiHienThiSapToi.isSelected();
            List<LichHop> ketQua = qlLichHop.locTheoThoiGian(chiHienThiSapToi);
            lichHopTableModel.setDanhSach(ketQua);
        });
        
        cboNguoiThamDu.addActionListener(e -> {
            String nguoiThamDu = cboNguoiThamDu.getSelectedItem().toString();
            if (!nguoiThamDu.equals("Tất cả người tham dự")) {
                List<LichHop> ketQua = qlLichHop.locTheoNguoiThamDu(nguoiThamDu);
                lichHopTableModel.setDanhSach(ketQua);
            } else {
                capNhatBangLichHop();
            }
        });
        
        // Thêm sự kiện double click để sửa
        tableLichHop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tableLichHop.getSelectedRow();
                    if (row != -1) {
                        suaLichHop(row);
                    }
                }
            }
        });
        
        tabbedPane.addTab("Quản lý lịch họp", panel);
        return panel;
    }

    private void setupCongViecEventListeners() {
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

        // Nút xóa task
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

        // Double click để sửa
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        int modelRow = table.convertRowIndexToModel(row);
                        CongViecTableModel.RowItem item = tableModel.getRowItemAt(modelRow);
                        if (item != null) {
                            CongViec oldCv = item.task;
                            CongViecDialog dialog = new CongViecDialog(GiaoDienChinh.this, oldCv);
                            dialog.setVisible(true);
                            if (dialog.isConfirmed()) {
                                CongViec newCv = dialog.getCongViec();
                                ql.capNhatCongViec(oldCv.getId(), newCv);
                                capNhatBang();
                            }
                        }
                    }
                }
            }
        });
    }

    private void themLichHop() {
        LichHop lichHop = new LichHop();
        LichHopDialog dialog = new LichHopDialog(this, lichHop, false);
        dialog.setVisible(true);
        
        if (dialog.getLichHop() != null) {
            qlLichHop.themLichHop(dialog.getLichHop());
            capNhatBangLichHop();
        }
    }
    
    private void suaLichHop(int row) {
        LichHop lichHop = lichHopTableModel.getLichHop(row);
        LichHopDialog dialog = new LichHopDialog(this, lichHop, true);
        dialog.setVisible(true);
        
        if (dialog.getLichHop() != null) {
            qlLichHop.capNhatLichHop(dialog.getLichHop());
            capNhatBangLichHop();
        }
    }
    
    private void xoaLichHop() {
        int row = tableLichHop.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lịch họp cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa lịch họp này?", 
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            LichHop lichHop = lichHopTableModel.getLichHop(row);
            qlLichHop.xoaLichHop(lichHop.getId());
            capNhatBangLichHop();
        }
    }
    
    private void capNhatBangLichHop() {
        List<LichHop> danhSach = qlLichHop.getDanhSach();
        
        // Áp dụng filter thời gian nếu đang bật
        if (chkChiHienThiSapToi != null && chkChiHienThiSapToi.isSelected()) {
            danhSach = qlLichHop.locTheoThoiGian(true);
        }
        
        // Áp dụng filter người tham dự nếu đã chọn
        if (cboNguoiThamDu != null && cboNguoiThamDu.getSelectedIndex() > 0) {
            String nguoiThamDu = cboNguoiThamDu.getSelectedItem().toString();
            danhSach = qlLichHop.locTheoNguoiThamDu(nguoiThamDu);
        }
        
        // Cập nhật TableModel
        lichHopTableModel.setDanhSach(danhSach);
        
        // Thông báo nếu không có dữ liệu
        if (danhSach.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Không có lịch họp nào thỏa mãn điều kiện lọc!", 
                "Thông báo", 
                JOptionPane.INFORMATION_MESSAGE);
        }
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

    // Thêm class MultiLineCellRenderer
    private class MultiLineCellRenderer extends DefaultTableCellRenderer {
        private JTextArea textArea;
        private JPanel panel;
        private static final int MIN_HEIGHT = 35;
        private static final int MAX_HEIGHT = 200;
        private static final int PADDING = 10;

        public MultiLineCellRenderer() {
            textArea = new JTextArea();
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textArea.setBackground(PANEL_BACKGROUND);
            textArea.setForeground(PRIMARY_COLOR);
            textArea.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
            textArea.setEditable(false);
            textArea.setOpaque(true);

            panel = new JPanel(new BorderLayout(0, 0));
            panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            panel.add(textArea, BorderLayout.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            // Xử lý giá trị null
            String text = value != null ? value.toString() : "";
            
            // Thay thế \n bằng xuống dòng thực sự
            text = text.replace("\\n", "\n");
            
            // Đặt text và tính toán chiều cao
            textArea.setText(text);
            
            // Lấy độ rộng cột hiện tại
            int width = table.getColumnModel().getColumn(column).getWidth() - PADDING * 2;
            textArea.setSize(new Dimension(width, Short.MAX_VALUE));
            
            // Tính toán chiều cao cần thiết
            int height = textArea.getPreferredSize().height;
            height = Math.max(MIN_HEIGHT, Math.min(height, MAX_HEIGHT));
            
            // Điều chỉnh chiều cao dòng nếu cần
            if (height > table.getRowHeight(row)) {
                table.setRowHeight(row, height);
            }

            // Điều chỉnh màu nền
            Color bg = isSelected ? TABLE_ROW_SELECTED : (row % 2 == 0 ? TABLE_ROW_BG1 : TABLE_ROW_BG2);
            textArea.setBackground(bg);
            panel.setBackground(bg);

            // Căn giữa nội dung theo chiều dọc
            textArea.setAlignmentY(Component.CENTER_ALIGNMENT);
            panel.setAlignmentY(Component.CENTER_ALIGNMENT);

            return panel;
        }
    }

    // Thêm class MultiLineCellEditor
    private class MultiLineCellEditor extends AbstractCellEditor implements TableCellEditor {
        private JTextArea textArea;
        private JScrollPane scrollPane;
        private JDialog dialog;
        private JButton okButton;
        private JButton cancelButton;
        private String originalValue;
        private boolean editing = false;

        public MultiLineCellEditor() {
            textArea = new JTextArea();
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 200));

            okButton = new JButton("OK");
            cancelButton = new JButton("Hủy");

            okButton.addActionListener(e -> {
                editing = false;
                dialog.dispose();
                fireEditingStopped();
            });

            cancelButton.addActionListener(e -> {
                textArea.setText(originalValue);
                editing = false;
                dialog.dispose();
                fireEditingCanceled();
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(okButton);
            buttonPanel.add(cancelButton);

            dialog = new JDialog();
            dialog.setModal(true);
            dialog.setLayout(new BorderLayout());
            dialog.add(scrollPane, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            
            // Thêm window listener để xử lý khi đóng dialog
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    textArea.setText(originalValue);
                    editing = false;
                    fireEditingCanceled();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            originalValue = value != null ? value.toString() : "";
            // Thay thế \n bằng xuống dòng thực sự
            originalValue = originalValue.replace("\\n", "\n");
            textArea.setText(originalValue);
            textArea.setCaretPosition(0);
            editing = true;
            
            // Đặt vị trí dialog
            Point p = table.getLocationOnScreen();
            p.x += table.getCellRect(row, column, true).x;
            p.y += table.getCellRect(row, column, true).y;
            dialog.setLocation(p);
            
            dialog.pack();
            dialog.setVisible(true);
            
            return textArea;
        }

        @Override
        public Object getCellEditorValue() {
            return textArea.getText();
        }

        @Override
        public boolean stopCellEditing() {
            if (!editing) return true;
            editing = false;
            dialog.dispose();
            return super.stopCellEditing();
        }

        @Override
        public void cancelCellEditing() {
            if (!editing) return;
            textArea.setText(originalValue);
            editing = false;
            dialog.dispose();
            super.cancelCellEditing();
        }
    }

    public static void main(String[] args) {
        try { Class.forName("org.sqlite.JDBC"); } catch (Exception e) { e.printStackTrace(); }
        SwingUtilities.invokeLater(() -> {
            new GiaoDienChinh().setVisible(true);
        });
    }
}
