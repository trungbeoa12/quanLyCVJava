package quanlycongviec;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimePicker extends JPanel {
    private JTextField textField;
    private JButton button;
    private JDialog dialog;
    private JSpinner hourSpinner;
    private JSpinner minuteSpinner;
    private Date selectedTime;
    private SimpleDateFormat timeFormat;
    
    public TimePicker() {
        setLayout(new BorderLayout());
        timeFormat = new SimpleDateFormat("HH:mm");
        
        // Tạo text field để hiển thị thời gian
        textField = new JTextField(5);
        textField.setEditable(false);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Tạo nút để mở time picker
        button = new JButton("🕒");
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Thêm components vào panel
        add(textField, BorderLayout.CENTER);
        add(button, BorderLayout.EAST);
        
        // Tạo dialog chọn thời gian
        createTimeDialog();
        
        // Thêm sự kiện cho nút
        button.addActionListener(e -> showTimeDialog());
        
        // Set thời gian hiện tại
        setTime(new Date());
    }
    
    private void createTimeDialog() {
        dialog = new JDialog();
        dialog.setTitle("Chọn thời gian");
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());
        
        // Panel chứa các spinner
        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        // Tạo spinner cho giờ
        SpinnerNumberModel hourModel = new SpinnerNumberModel(0, 0, 23, 1);
        hourSpinner = new JSpinner(hourModel);
        hourSpinner.setPreferredSize(new Dimension(60, 30));
        
        // Tạo spinner cho phút
        SpinnerNumberModel minuteModel = new SpinnerNumberModel(0, 0, 59, 1);
        minuteSpinner = new JSpinner(minuteModel);
        minuteSpinner.setPreferredSize(new Dimension(60, 30));
        
        // Thêm các spinner vào panel
        spinnerPanel.add(new JLabel("Giờ:"));
        spinnerPanel.add(hourSpinner);
        spinnerPanel.add(new JLabel("Phút:"));
        spinnerPanel.add(minuteSpinner);
        
        // Panel chứa nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Hủy");
        
        okButton.addActionListener(e -> {
            updateTime();
            dialog.dispose();
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        // Thêm các panel vào dialog
        dialog.add(spinnerPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void updateTime() {
        int hour = (int) hourSpinner.getValue();
        int minute = (int) minuteSpinner.getValue();
        
        Calendar cal = Calendar.getInstance();
        if (selectedTime != null) {
            cal.setTime(selectedTime);
        }
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        selectedTime = cal.getTime();
        textField.setText(timeFormat.format(selectedTime));
    }
    
    private void showTimeDialog() {
        if (selectedTime != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(selectedTime);
            hourSpinner.setValue(cal.get(Calendar.HOUR_OF_DAY));
            minuteSpinner.setValue(cal.get(Calendar.MINUTE));
        }
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    public Date getTime() {
        return selectedTime;
    }
    
    public void setTime(Date time) {
        selectedTime = time;
        if (time != null) {
            textField.setText(timeFormat.format(time));
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            hourSpinner.setValue(cal.get(Calendar.HOUR_OF_DAY));
            minuteSpinner.setValue(cal.get(Calendar.MINUTE));
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