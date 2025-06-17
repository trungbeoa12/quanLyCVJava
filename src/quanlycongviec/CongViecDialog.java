package quanlycongviec;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class CongViecDialog extends JDialog {
    private JTextField txtTieuDe, txtMoTa, txtNguoiThucHien, txtGhiChu, txtDuongDan;
    private JSpinner spnNgayThang, spnHanChot;
    private JCheckBox chkHoanThanh;
    private boolean confirmed = false;
    private CongViec congViec;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public CongViecDialog(Frame parent, CongViec congViec) {
        super(parent, true);
        setTitle(congViec == null ? "Thêm công việc" : "Sửa công việc");
        setSize(480, 470);
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

        // Ngày tháng (calendar, bắt buộc)
        row++; gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel("Ngày tháng:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        spnNgayThang = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditorNgay = new JSpinner.DateEditor(spnNgayThang, "dd/MM/yyyy");
        spnNgayThang.setEditor(dateEditorNgay);
        panel.add(spnNgayThang, gbc);

        // Mô tả
        row++; gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtMoTa = new JTextField();
        panel.add(txtMoTa, gbc);

        // Hạn chót (calendar, không bắt buộc)
        row++; gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel("Hạn chót:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        spnHanChot = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditorHanChot = new JSpinner.DateEditor(spnHanChot, "dd/MM/yyyy");
        spnHanChot.setEditor(dateEditorHanChot);
        panel.add(spnHanChot, gbc);

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
            // Ngày tháng
            try {
                if (!congViec.getNgayThang().isEmpty()) {
                    Date d = dateFormat.parse(congViec.getNgayThang());
                    spnNgayThang.setValue(d);
                }
            } catch (Exception ex) {}
            // Hạn chót
            try {
                if (!congViec.getHanChot().isEmpty()) {
                    Date d = dateFormat.parse(congViec.getHanChot());
                    spnHanChot.setValue(d);
                }
            } catch (Exception ex) {}
            txtNguoiThucHien.setText(congViec.getNguoiThucHien());
            txtGhiChu.setText(congViec.getGhiChu());
            chkHoanThanh.setSelected(congViec.isHoanThanh());
            txtDuongDan.setText(congViec.getDuongDan());
        }

        btnOK.addActionListener(e -> {
            if (txtTieuDe.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tiêu đề!");
                return;
            }
            String ngayThangStr = "";
            try {
                ngayThangStr = dateFormat.format((Date) spnNgayThang.getValue());
            } catch (Exception ex) {}
            if (ngayThangStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày tháng!");
                return;
            }
            String hanChotStr = "";
            try {
                hanChotStr = dateFormat.format((Date) spnHanChot.getValue());
            } catch (Exception ex) {}
            confirmed = true;
            this.congViec = new CongViec(
                txtTieuDe.getText().trim(),
                txtMoTa.getText().trim(),
                ngayThangStr,
                hanChotStr,
                chkHoanThanh.isSelected(),
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

