package quanlycongviec;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePicker extends JPanel {
    private JTextField textField;
    private JButton button;
    private JDialog dialog;
    private JSpinner yearSpinner;
    private JSpinner monthSpinner;
    private JSpinner daySpinner;
    private Date selectedDate;
    private SimpleDateFormat dateFormat;
    
    public DatePicker() {
        setLayout(new BorderLayout());
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        // Tạo text field để hiển thị ngày
        textField = new JTextField(10);
        textField.setEditable(false);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Tạo nút để mở calendar
        button = new JButton("📅");
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Thêm components vào panel
        add(textField, BorderLayout.CENTER);
        add(button, BorderLayout.EAST);
        
        // Tạo dialog chọn ngày
        createDateDialog();
        
        // Thêm sự kiện cho nút
        button.addActionListener(e -> showDateDialog());
        
        // Set ngày hiện tại
        setDate(new Date());
    }
    
    private void createDateDialog() {
        dialog = new JDialog();
        dialog.setTitle("Chọn ngày");
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());
        
        // Panel chứa các spinner
        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        // Tạo spinner cho ngày
        SpinnerNumberModel dayModel = new SpinnerNumberModel(1, 1, 31, 1);
        daySpinner = new JSpinner(dayModel);
        daySpinner.setPreferredSize(new Dimension(60, 30));
        
        // Tạo spinner cho tháng
        SpinnerNumberModel monthModel = new SpinnerNumberModel(1, 1, 12, 1);
        monthSpinner = new JSpinner(monthModel);
        monthSpinner.setPreferredSize(new Dimension(60, 30));
        
        // Tạo spinner cho năm
        SpinnerNumberModel yearModel = new SpinnerNumberModel(2024, 2000, 2100, 1);
        yearSpinner = new JSpinner(yearModel);
        yearSpinner.setPreferredSize(new Dimension(80, 30));
        
        // Thêm các spinner vào panel
        spinnerPanel.add(new JLabel("Ngày:"));
        spinnerPanel.add(daySpinner);
        spinnerPanel.add(new JLabel("Tháng:"));
        spinnerPanel.add(monthSpinner);
        spinnerPanel.add(new JLabel("Năm:"));
        spinnerPanel.add(yearSpinner);
        
        // Panel chứa nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Hủy");
        
        okButton.addActionListener(e -> {
            updateDate();
            dialog.dispose();
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        // Thêm các panel vào dialog
        dialog.add(spinnerPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Cập nhật số ngày khi thay đổi tháng/năm
        monthSpinner.addChangeListener(e -> updateDaySpinner());
        yearSpinner.addChangeListener(e -> updateDaySpinner());
    }
    
    private void updateDaySpinner() {
        int month = (int) monthSpinner.getValue();
        int year = (int) yearSpinner.getValue();
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        SpinnerNumberModel model = (SpinnerNumberModel) daySpinner.getModel();
        model.setMaximum(maxDay);
        if ((int) daySpinner.getValue() > maxDay) {
            daySpinner.setValue(maxDay);
        }
    }
    
    private void updateDate() {
        int day = (int) daySpinner.getValue();
        int month = (int) monthSpinner.getValue();
        int year = (int) yearSpinner.getValue();
        
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        selectedDate = cal.getTime();
        textField.setText(dateFormat.format(selectedDate));
    }
    
    private void showDateDialog() {
        if (selectedDate != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(selectedDate);
            yearSpinner.setValue(cal.get(Calendar.YEAR));
            monthSpinner.setValue(cal.get(Calendar.MONTH) + 1);
            daySpinner.setValue(cal.get(Calendar.DAY_OF_MONTH));
        }
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    public Date getDate() {
        return selectedDate;
    }
    
    public void setDate(Date date) {
        selectedDate = date;
        if (date != null) {
            textField.setText(dateFormat.format(date));
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            yearSpinner.setValue(cal.get(Calendar.YEAR));
            monthSpinner.setValue(cal.get(Calendar.MONTH) + 1);
            daySpinner.setValue(cal.get(Calendar.DAY_OF_MONTH));
        } else {
            textField.setText("");
        }
    }
    
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        textField.setEnabled(enabled);
        button.setEnabled(enabled);
    }
} 