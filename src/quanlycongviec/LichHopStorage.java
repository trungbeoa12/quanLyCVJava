package quanlycongviec;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LichHopStorage {
    private static final String FILE_PATH = "lichhop.json";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static void saveToFile(List<LichHop> danhSach) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            writer.println("[");
            for (int i = 0; i < danhSach.size(); i++) {
                LichHop lh = danhSach.get(i);
                writer.println("  {");
                writer.println("    \"id\": " + lh.getId() + ",");
                writer.println("    \"tenHop\": \"" + escapeJson(lh.getTenHop()) + "\",");
                writer.println("    \"thoiGianBatDau\": \"" + dateFormat.format(lh.getThoiGianBatDau()) + "\",");
                writer.println("    \"thoiGianKetThuc\": \"" + dateFormat.format(lh.getThoiGianKetThuc()) + "\",");
                writer.println("    \"diaDiem\": \"" + escapeJson(lh.getDiaDiem()) + "\",");
                writer.println("    \"noiDung\": \"" + escapeJson(lh.getNoiDung()) + "\",");
                writer.println("    \"nguoiThamDu\": \"" + escapeJson(lh.getNguoiThamDu()) + "\"");
                writer.println("  }" + (i < danhSach.size() - 1 ? "," : ""));
            }
            writer.println("]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static List<LichHop> loadFromFile() {
        List<LichHop> danhSach = new ArrayList<>();
        File file = new File(FILE_PATH);
        
        if (!file.exists()) {
            return danhSach;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            
            String jsonStr = json.toString().trim();
            if (jsonStr.startsWith("[") && jsonStr.endsWith("]")) {
                jsonStr = jsonStr.substring(1, jsonStr.length() - 1).trim();
                if (jsonStr.isEmpty()) {
                    return danhSach;
                }
                
                String[] items = jsonStr.split("},");
                for (String item : items) {
                    if (item.endsWith("}")) {
                        item = item.substring(0, item.length() - 1);
                    }
                    item = item.trim();
                    if (item.startsWith("{")) {
                        item = item.substring(1);
                    }
                    
                    LichHop lh = new LichHop();
                    String[] lines = item.split(",");
                    for (String line2 : lines) {
                        String[] parts = line2.trim().split(":", 2);
                        if (parts.length == 2) {
                            String key = parts[0].trim().replace("\"", "");
                            String value = parts[1].trim().replace("\"", "");
                            
                            switch (key) {
                                case "id":
                                    lh.setId(Integer.parseInt(value));
                                    break;
                                case "tenHop":
                                    lh.setTenHop(unescapeJson(value));
                                    break;
                                case "thoiGianBatDau":
                                    lh.setThoiGianBatDau(dateFormat.parse(value));
                                    break;
                                case "thoiGianKetThuc":
                                    lh.setThoiGianKetThuc(dateFormat.parse(value));
                                    break;
                                case "diaDiem":
                                    lh.setDiaDiem(unescapeJson(value));
                                    break;
                                case "noiDung":
                                    lh.setNoiDung(unescapeJson(value));
                                    break;
                                case "nguoiThamDu":
                                    lh.setNguoiThamDu(unescapeJson(value));
                                    break;
                            }
                        }
                    }
                    danhSach.add(lh);
                }
            }
        } catch (IOException | java.text.ParseException e) {
            e.printStackTrace();
        }
        
        return danhSach;
    }
    
    private static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                 .replace("\"", "\\\"")
                 .replace("\n", "\\n")
                 .replace("\r", "\\r")
                 .replace("\t", "\\t");
    }
    
    private static String unescapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\\"", "\"")
                 .replace("\\n", "\n")
                 .replace("\\r", "\r")
                 .replace("\\t", "\t")
                 .replace("\\\\", "\\");
    }
} 