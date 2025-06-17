package quanlycongviec;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CongViecDialog extends JDialog {
    private JTextField txtTieuDe, txtMoTa, txtHanChot, txtUuTien, txtNguoiThucHien, txtGhiChu, txtDuongDan;
    private JCheckBox chkHoanThanh;
    private boolean confirmed = false;
    private CongViec congViec;

    public CongViecDialog(Frame parent, CongViec congViec) {
        super(parent, true);
        setTitle(congViec == null ? "Thêm công việc" : "Sửa công việc");
        setSize(460, 430);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 7, 7);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;

        int row = 0;

        // Tiêu đề
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Tiêu đề:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtTieuDe = new JTextField();
        panel.add(txtTieuDe, gbc);

        // Mô tả
        row++; gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtMoTa = new JTextField();
        panel.add(txtMoTa, gbc);

        // Hạn chót
        row++; gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel("Hạn chót (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtHanChot = new JTextField();
        panel.add(txtHanChot, gbc);

        // Ưu tiên
        row++; gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel("Ưu tiên:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtUuTien = new JTextField();
        panel.add(txtUuTien, gbc);

        // Người thực hiện
        row++; gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel("Người thực hiện:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNguoiThucHien = new JTextField();
        panel.add(txtNguoiThucHien, gbc);

        // Ghi chú
        row++; gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtGhiChu = new JTextField();
        panel.add(txtGhiChu, gbc);

        // Đường dẫn (file/link)
        row++; gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel("Đường dẫn (file/link):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDuongDan = new JTextField();
        panel.add(txtDuongDan, gbc);
        gbc.gridx = 2; gbc.weightx = 0;
        JButton btnChonFile = new JButton("Chọn...");
        panel.add(btnChonFile, gbc);

        btnChonFile.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                txtDuongDan.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        // Đã hoàn thành
        row++; gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel("Đã hoàn thành:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        chkHoanThanh = new JCheckBox();
        panel.add(chkHoanThanh, gbc);

        // Nút xác nhận và hủy
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnOK = new JButton("OK");
        JButton btnCancel = new JButton("Hủy");
        buttonPanel.add(btnOK);
        buttonPanel.add(btnCancel);

        row++; gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        panel.add(buttonPanel, gbc);

        // Load dữ liệu lên form nếu sửa
        if (congViec != null) {
            txtTieuDe.setText(congViec.getTieuDe());
            txtMoTa.setText(congViec.getMoTa());
            txtHanChot.setText(congViec.getHanChot());
            txtUuTien.setText(congViec.getUuTien());
            txtNguoiThucHien.setText(congViec.getNguoiThucHien());
            txtGhiChu.setText(congViec.getGhiChu());
            chkHoanThanh.setSelected(congViec.isHoanThanh());
            txtDuongDan.setText(congViec.getDuongDan());
        }

        btnOK.addActionListener(e -> {
            if (txtTieuDe.getText().trim().isEmpty() || txtMoTa.getText().trim().isEmpty() || txtHanChot.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc!");
                return;
            }
            confirmed = true;
            this.congViec = new CongViec(
                txtTieuDe.getText().trim(),
                txtMoTa.getText().trim(),
                txtHanChot.getText().trim(),
                chkHoanThanh.isSelected(),
                txtUuTien.getText().trim(),
                txtNguoiThucHien.getText().trim(),
                txtGhiChu.getText().trim(),
                txtDuongDan.getText().trim()
            );
            dispose();
        });
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        setContentPane(panel);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public CongViec getCongViec() {
        return congViec;
    }
}

