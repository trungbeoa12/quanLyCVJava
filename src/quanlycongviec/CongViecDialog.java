package quanlycongviec;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CongViecDialog extends JDialog {
    private JTextField txtTieuDe, txtMoTa, txtHanChot, txtUuTien, txtNguoiThucHien, txtGhiChu;
    private JCheckBox chkHoanThanh;
    private boolean confirmed = false;
    private CongViec congViec;

    public CongViecDialog(Frame parent, CongViec congViec) {
        super(parent, true);
        setTitle(congViec == null ? "Thêm công việc" : "Sửa công việc");
        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Tiêu đề:"), gbc);
        gbc.gridx = 1;
        txtTieuDe = new JTextField(20);
        add(txtTieuDe, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1;
        txtMoTa = new JTextField(20);
        add(txtMoTa, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Hạn chót (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        txtHanChot = new JTextField(20);
        add(txtHanChot, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Ưu tiên:"), gbc);
        gbc.gridx = 1;
        txtUuTien = new JTextField(20);
        add(txtUuTien, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Người thực hiện:"), gbc);
        gbc.gridx = 1;
        txtNguoiThucHien = new JTextField(20);
        add(txtNguoiThucHien, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1;
        txtGhiChu = new JTextField(20);
        add(txtGhiChu, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Đã hoàn thành:"), gbc);
        gbc.gridx = 1;
        chkHoanThanh = new JCheckBox();
        add(chkHoanThanh, gbc);

        // Nút xác nhận và hủy
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnOK = new JButton("OK");
        JButton btnCancel = new JButton("Hủy");
        buttonPanel.add(btnOK);
        buttonPanel.add(btnCancel);
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        // Nếu sửa thì load dữ liệu lên form
        if (congViec != null) {
            txtTieuDe.setText(congViec.getTieuDe());
            txtMoTa.setText(congViec.getMoTa());
            txtHanChot.setText(congViec.getHanChot());
            txtUuTien.setText(congViec.getUuTien());
            txtNguoiThucHien.setText(congViec.getNguoiThucHien());
            txtGhiChu.setText(congViec.getGhiChu());
            chkHoanThanh.setSelected(congViec.isHoanThanh());
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
                txtGhiChu.getText().trim()
            );
            dispose();
        });
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public CongViec getCongViec() {
        return congViec;
    }
} 