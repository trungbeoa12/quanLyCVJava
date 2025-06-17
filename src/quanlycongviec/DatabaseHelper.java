package quanlycongviec;

import java.sql.*;
import java.util.*;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:congviec.db";

    public DatabaseHelper() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS congviec (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tieuDe TEXT NOT NULL," +
                "moTa TEXT," +
                "ngayThang TEXT NOT NULL," +
                "hanChot TEXT," +
                "hoanThanh INTEGER," +
                "nguoiThucHien TEXT," +
                "ghiChu TEXT," +
                "duongDan TEXT," +
                "isSubTask INTEGER," +
                "parentId INTEGER" +
                ")";
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // Thêm công việc (trả về id mới)
    public int insertCongViec(CongViec cv, Integer parentId) {
        String sql = "INSERT INTO congviec (tieuDe, moTa, ngayThang, hanChot, hoanThanh, nguoiThucHien, ghiChu, duongDan, isSubTask, parentId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cv.getTieuDe());
            ps.setString(2, cv.getMoTa());
            ps.setString(3, cv.getNgayThang());
            ps.setString(4, cv.getHanChot());
            ps.setInt(5, cv.isHoanThanh() ? 1 : 0);
            ps.setString(6, cv.getNguoiThucHien());
            ps.setString(7, cv.getGhiChu());
            ps.setString(8, cv.getDuongDan());
            ps.setInt(9, cv.isSubTask() ? 1 : 0);
            if (parentId != null) ps.setInt(10, parentId); else ps.setNull(10, Types.INTEGER);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Cập nhật công việc
    public void updateCongViec(int id, CongViec cv) {
        String sql = "UPDATE congviec SET tieuDe=?, moTa=?, ngayThang=?, hanChot=?, hoanThanh=?, nguoiThucHien=?, ghiChu=?, duongDan=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cv.getTieuDe());
            ps.setString(2, cv.getMoTa());
            ps.setString(3, cv.getNgayThang());
            ps.setString(4, cv.getHanChot());
            ps.setInt(5, cv.isHoanThanh() ? 1 : 0);
            ps.setString(6, cv.getNguoiThucHien());
            ps.setString(7, cv.getGhiChu());
            ps.setString(8, cv.getDuongDan());
            ps.setInt(9, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa công việc (và subtask nếu là cha)
    public void deleteCongViec(int id) {
        String sql = "DELETE FROM congviec WHERE id=? OR parentId=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy danh sách công việc cha và subtask
    public List<CongViecWithId> getAllCongViec() {
        List<CongViecWithId> result = new ArrayList<>();
        Map<Integer, CongViecWithId> idToCv = new HashMap<>();
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM congviec ORDER BY COALESCE(parentId, id), id");
            while (rs.next()) {
                int id = rs.getInt("id");
                String tieuDe = rs.getString("tieuDe");
                String moTa = rs.getString("moTa");
                String ngayThang = rs.getString("ngayThang");
                String hanChot = rs.getString("hanChot");
                boolean hoanThanh = rs.getInt("hoanThanh") == 1;
                String nguoiThucHien = rs.getString("nguoiThucHien");
                String ghiChu = rs.getString("ghiChu");
                String duongDan = rs.getString("duongDan");
                boolean isSubTask = rs.getInt("isSubTask") == 1;
                int parentId = rs.getInt("parentId");
                boolean hasParent = !rs.wasNull();
                CongViecWithId cv = new CongViecWithId(id, tieuDe, moTa, ngayThang, hanChot, hoanThanh, nguoiThucHien, ghiChu, duongDan, isSubTask);
                idToCv.put(id, cv);
                if (!isSubTask || !hasParent) {
                    result.add(cv);
                } else {
                    CongViecWithId parent = idToCv.get(parentId);
                    if (parent != null) {
                        parent.cv.addSubTask(cv.cv);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Lớp phụ để giữ id cho thao tác update/xóa
    public static class CongViecWithId {
        public int id;
        public CongViec cv;
        public CongViecWithId(int id, String tieuDe, String moTa, String ngayThang, String hanChot, boolean hoanThanh, String nguoiThucHien, String ghiChu, String duongDan, boolean isSubTask) {
            this.id = id;
            this.cv = new CongViec(tieuDe, moTa, ngayThang, hanChot, hoanThanh, nguoiThucHien, ghiChu, duongDan, isSubTask);
            this.cv.setId(id);
        }
    }
} 