package quanlycongviec;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class LichHopDialog extends JDialog {
    private JTextField txtTenHop;
    private DatePicker datePickerBatDau;
    private TimePicker timePickerBatDau;
    private DatePicker datePickerKetThuc;
    private TimePicker timePickerKetThuc;
    private JTextField txtDiaDiem;
    private JTextArea txtNoiDung;
    private JTextArea txtNguoiThamDu;
    private JButton btnLuu;
    private JButton btnHuy;
    private LichHop lichHop;
    private boolean isEdit;
    private List<String> danhSachNguoiThamDu;
    private JComboBox<String> cboNguoiThamDu;
    private JButton btnThemNguoiThamDu;
    private JList<String> lstNguoiThamDu;
    private DefaultListModel<String> listModel;
    
    public LichHopDialog(Frame parent, LichHop lichHop, boolean isEdit) {
        super(parent, "Thêm lịch họp", true);
        this.lichHop = lichHop;
        this.isEdit = isEdit;
        this.danhSachNguoiThamDu = new ArrayList<>();
        
        // Khởi tạo danh sách người tham dự từ database
        loadDanhSachNguoiThamDu();
        
        initComponents();
        if (isEdit) {
            setTitle("Sửa lịch họp");
            hienThiDuLieu();
        }
        
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void loadDanhSachNguoiThamDu() {
        // TODO: Load danh sách người tham dự từ database
        danhSachNguoiThamDu.add("Nguyễn Văn A");
        danhSachNguoiThamDu.add("Trần Thị B");
        danhSachNguoiThamDu.add("Lê Văn C");
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel thông tin
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Tên họp
        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("Tên họp:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtTenHop = new JTextField(20);
        infoPanel.add(txtTenHop, gbc);
        
        // Thời gian bắt đầu
        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("Thời gian bắt đầu:"), gbc);
        gbc.gridx = 1;
        JPanel startTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePickerBatDau = new DatePicker();
        timePickerBatDau = new TimePicker();
        startTimePanel.add(datePickerBatDau);
        startTimePanel.add(timePickerBatDau);
        infoPanel.add(startTimePanel, gbc);
        
        // Thời gian kết thúc
        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("Thời gian kết thúc:"), gbc);
        gbc.gridx = 1;
        JPanel endTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePickerKetThuc = new DatePicker();
        timePickerKetThuc = new TimePicker();
        endTimePanel.add(datePickerKetThuc);
        endTimePanel.add(timePickerKetThuc);
        infoPanel.add(endTimePanel, gbc);
        
        // Địa điểm
        gbc.gridx = 0; gbc.gridy = 3;
        infoPanel.add(new JLabel("Địa điểm:"), gbc);
        gbc.gridx = 1;
        txtDiaDiem = new JTextField(20);
        infoPanel.add(txtDiaDiem, gbc);
        
        // Nội dung
        gbc.gridx = 0; gbc.gridy = 4;
        infoPanel.add(new JLabel("Nội dung:"), gbc);
        gbc.gridx = 1;
        txtNoiDung = new JTextArea(3, 20);
        txtNoiDung.setLineWrap(true);
        txtNoiDung.setWrapStyleWord(true);
        JScrollPane scrollNoiDung = new JScrollPane(txtNoiDung);
        infoPanel.add(scrollNoiDung, gbc);
        
        // Người tham dự
        gbc.gridx = 0; gbc.gridy = 5;
        infoPanel.add(new JLabel("Người tham dự:"), gbc);
        gbc.gridx = 1;
        JPanel nguoiThamDuPanel = new JPanel(new BorderLayout(5, 5));
        
        // Panel chọn người tham dự
        JPanel selectPanel = new JPanel(new BorderLayout(5, 0));
        cboNguoiThamDu = new JComboBox<>(danhSachNguoiThamDu.toArray(new String[0]));
        cboNguoiThamDu.setEditable(true);
        btnThemNguoiThamDu = new JButton("Thêm");
        selectPanel.add(cboNguoiThamDu, BorderLayout.CENTER);
        selectPanel.add(btnThemNguoiThamDu, BorderLayout.EAST);
        
        // List người tham dự đã chọn
        listModel = new DefaultListModel<>();
        lstNguoiThamDu = new JList<>(listModel);
        JScrollPane scrollNguoiThamDu = new JScrollPane(lstNguoiThamDu);
        
        nguoiThamDuPanel.add(selectPanel, BorderLayout.NORTH);
        nguoiThamDuPanel.add(scrollNguoiThamDu, BorderLayout.CENTER);
        infoPanel.add(nguoiThamDuPanel, gbc);
        
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        
        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");
        buttonPanel.add(btnLuu);
        buttonPanel.add(btnHuy);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Thêm sự kiện
        btnLuu.addActionListener(e -> xuLyLuu());
        btnHuy.addActionListener(e -> dispose());
        btnThemNguoiThamDu.addActionListener(e -> themNguoiThamDu());
        
        // Thêm sự kiện xóa người tham dự
        lstNguoiThamDu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = lstNguoiThamDu.locationToIndex(e.getPoint());
                    if (index != -1) {
                        listModel.remove(index);
                    }
                }
            }
        });
    }
    
    private void themNguoiThamDu() {
        String nguoiThamDu = cboNguoiThamDu.getSelectedItem().toString().trim();
        if (!nguoiThamDu.isEmpty() && !listModel.contains(nguoiThamDu)) {
            listModel.addElement(nguoiThamDu);
            cboNguoiThamDu.setSelectedItem("");
        }
    }
    
    private void hienThiDuLieu() {
        txtTenHop.setText(lichHop.getTenHop());
        datePickerBatDau.setDate(lichHop.getThoiGianBatDau());
        timePickerBatDau.setTime(lichHop.getThoiGianBatDau());
        datePickerKetThuc.setDate(lichHop.getThoiGianKetThuc());
        timePickerKetThuc.setTime(lichHop.getThoiGianKetThuc());
        txtDiaDiem.setText(lichHop.getDiaDiem());
        txtNoiDung.setText(lichHop.getNoiDung());
        
        // Hiển thị danh sách người tham dự
        String[] nguoiThamDu = lichHop.getNguoiThamDu().split("\n");
        for (String nguoi : nguoiThamDu) {
            if (!nguoi.trim().isEmpty()) {
                listModel.addElement(nguoi.trim());
            }
        }
    }
    
    private void xuLyLuu() {
        // Kiểm tra dữ liệu
        if (txtTenHop.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên họp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTenHop.requestFocus();
            return;
        }
        
        Date ngayBatDau = datePickerBatDau.getDate();
        Date gioBatDau = timePickerBatDau.getTime();
        if (ngayBatDau == null || gioBatDau == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thời gian bắt đầu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Date ngayKetThuc = datePickerKetThuc.getDate();
        Date gioKetThuc = timePickerKetThuc.getTime();
        if (ngayKetThuc == null || gioKetThuc == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thời gian kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (txtDiaDiem.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập địa điểm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtDiaDiem.requestFocus();
            return;
        }
        
        // Tạo đối tượng Calendar cho thời gian bắt đầu
        Calendar calBatDau = Calendar.getInstance();
        calBatDau.setTime(ngayBatDau);
        Calendar calTimeBatDau = Calendar.getInstance();
        calTimeBatDau.setTime(gioBatDau);
        calBatDau.set(Calendar.HOUR_OF_DAY, calTimeBatDau.get(Calendar.HOUR_OF_DAY));
        calBatDau.set(Calendar.MINUTE, calTimeBatDau.get(Calendar.MINUTE));
        calBatDau.set(Calendar.SECOND, 0);
        calBatDau.set(Calendar.MILLISECOND, 0);
        
        // Tạo đối tượng Calendar cho thời gian kết thúc
        Calendar calKetThuc = Calendar.getInstance();
        calKetThuc.setTime(ngayKetThuc);
        Calendar calTimeKetThuc = Calendar.getInstance();
        calTimeKetThuc.setTime(gioKetThuc);
        calKetThuc.set(Calendar.HOUR_OF_DAY, calTimeKetThuc.get(Calendar.HOUR_OF_DAY));
        calKetThuc.set(Calendar.MINUTE, calTimeKetThuc.get(Calendar.MINUTE));
        calKetThuc.set(Calendar.SECOND, 0);
        calKetThuc.set(Calendar.MILLISECOND, 0);
        
        // Kiểm tra thời gian kết thúc phải sau thời gian bắt đầu
        if (calKetThuc.before(calBatDau)) {
            JOptionPane.showMessageDialog(this, "Thời gian kết thúc phải sau thời gian bắt đầu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Lưu dữ liệu vào đối tượng LichHop
        lichHop.setTenHop(txtTenHop.getText().trim());
        lichHop.setThoiGianBatDau(calBatDau.getTime());
        lichHop.setThoiGianKetThuc(calKetThuc.getTime());
        lichHop.setDiaDiem(txtDiaDiem.getText().trim());
        lichHop.setNoiDung(txtNoiDung.getText().trim());
        
        // Lưu danh sách người tham dự
        StringBuilder nguoiThamDu = new StringBuilder();
        for (int i = 0; i < listModel.size(); i++) {
            if (i > 0) nguoiThamDu.append("\n");
            nguoiThamDu.append(listModel.get(i));
        }
        lichHop.setNguoiThamDu(nguoiThamDu.toString());
        
        // Đóng dialog
        dispose();
    }
    
    public LichHop getLichHop() {
        return lichHop;
    }
} 