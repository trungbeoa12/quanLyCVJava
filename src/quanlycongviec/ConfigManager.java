package quanlycongviec;

import java.io.*;
import java.util.Properties;

public class ConfigManager {
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties;
    
    static {
        properties = new Properties();
        // Thiết lập giá trị mặc định
        properties.setProperty("column.width.0", "50");   // ID
        properties.setProperty("column.width.1", "200");  // Tên công việc
        properties.setProperty("column.width.2", "100");  // Ngày bắt đầu
        properties.setProperty("column.width.3", "100");  // Ngày kết thúc
        properties.setProperty("column.width.4", "100");  // Trạng thái
        properties.setProperty("column.width.5", "100");  // Độ ưu tiên
        loadConfig();
    }
    
    private static void loadConfig() {
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                properties.load(fis);
            } catch (IOException e) {
                System.err.println("Lỗi khi đọc file cấu hình: " + e.getMessage());
            }
        } else {
            // Nếu file chưa tồn tại, tạo file mới với giá trị mặc định
            saveConfig();
        }
    }
    
    public static void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            properties.store(fos, "QuanLyCongViec Configuration");
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu file cấu hình: " + e.getMessage());
        }
    }
    
    public static void setColumnWidth(int columnIndex, int width) {
        properties.setProperty("column.width." + columnIndex, String.valueOf(width));
        saveConfig();
    }
    
    public static int getColumnWidth(int columnIndex, int defaultWidth) {
        String width = properties.getProperty("column.width." + columnIndex);
        return width != null ? Integer.parseInt(width) : defaultWidth;
    }
} 