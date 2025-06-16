package quanlycongviec;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

public class GiaoDienChinh extends JFrame {
    private QuanLyCongViec ql;
    private JTable table;
    private CongViecTableModel tableModel;
    private JTextField txtTieuDe, txtMoTa, txtHanChot, txtTimKiem;
    private JButton btnThem, btnXoa, btnLuu, btnTimKiem, btnCapNhat, btnHuy;
    private JCheckBox chkHoanThanh, chkChuaHoanThanh;
    private int editingRow = -1;
    private TableRowSorter<DefaultTableModel> sorter;

    public GiaoDienChinh() {
        ql = new QuanLyCongViec();
        ql.docTuFile();
        setTitle("Quản Lý Công Việc");
        setSize(1100, 600);
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
        // Renderer màu sắc cho trạng thái và ưu tiên
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (String) value;
                if ("✓".equals(status)) {
                    c.setForeground(new Color(0, 128, 0)); // xanh lá
                } else {
                    c.setForeground(Color.RED);
                }
                return c;
            }
        });
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String priority = (String) value;
                if (priority != null && priority.toLowerCase().contains("cao")) {
                    c.setForeground(Color.RED);
                } else if (priority != null && priority.toLowerCase().contains("trung bình")) {
                    c.setForeground(new Color(255, 140, 0)); // cam
                } else {
                    c.setForeground(new Color(0, 128, 0)); // xanh
                }
                return c;
            }
        });

        // Panel nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnThem = new JButton("Thêm công việc");
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

        // Sự kiện cho bộ lọc trạng thái
        chkHoanThanh.addActionListener(e -> capNhatBang());
        chkChuaHoanThanh.addActionListener(e -> capNhatBang());

        // Sự kiện cho nút Thêm
        btnThem.addActionListener(e -> {
            CongViecDialog dialog = new CongViecDialog(this, null);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                CongViec cv = dialog.getCongViec();
                ql.themCongViec(cv);
                tableModel.fireTableDataChanged();
            }
        });

        // Sự kiện cho nút Xóa
        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn công việc cần xóa!");
                return;
            }
            int modelRow = table.convertRowIndexToModel(row);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa công việc này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ql.xoaCongViec(modelRow);
                tableModel.fireTableDataChanged();
            }
        });

        // Sự kiện cho nút Lưu
        btnLuu.addActionListener(e -> {
            ql.luuVaoFile();
            JOptionPane.showMessageDialog(this, "Đã lưu tất cả công việc!");
        });

        // Sự kiện double-click để sửa
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (e.getClickCount() == 2 && row != -1) {
                    int modelRow = table.convertRowIndexToModel(row);
                    CongViec oldCv = ql.getDanhSach().get(modelRow);
                    CongViecDialog dialog = new CongViecDialog(GiaoDienChinh.this, oldCv);
                    dialog.setVisible(true);
                    if (dialog.isConfirmed()) {
                        CongViec newCv = dialog.getCongViec();
                        ql.getDanhSach().set(modelRow, newCv);
                        tableModel.fireTableDataChanged();
                    }
                }
            }
        });

        // Sự kiện tìm kiếm
        btnTimKiem.addActionListener(e -> timKiemCongViec());

        capNhatBang();
    }

    private void capNhatBang() {
        List<CongViec> danhSach = ql.getDanhSach();
        tableModel.setDanhSach(danhSach.stream().filter(cv ->
            (chkHoanThanh.isSelected() && cv.isHoanThanh()) ||
            (chkChuaHoanThanh.isSelected() && !cv.isHoanThanh())
        ).collect(Collectors.toList()));
    }

    private void timKiemCongViec() {
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();
        List<CongViec> ketQua = ql.getDanhSach().stream().filter(cv ->
            cv.getTieuDe().toLowerCase().contains(tuKhoa) ||
            cv.getMoTa().toLowerCase().contains(tuKhoa) ||
            cv.getNguoiThucHien().toLowerCase().contains(tuKhoa) ||
            cv.getUuTien().toLowerCase().contains(tuKhoa) ||
            cv.getGhiChu().toLowerCase().contains(tuKhoa)
        ).collect(Collectors.toList());
        tableModel.setDanhSach(ketQua);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GiaoDienChinh().setVisible(true);
        });
    }
}
