package DB;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBC {
    /**
     * Thiết lập kết nối đến cơ sở dữ liệu.
     *
     * @return Đối tượng Connection đại diện cho kết nối đến cơ sở dữ liệu.
     */
    public static Connection getConnection() {
        try {
            // Tham số kết nối đến cơ sở dữ liệu
            String url = "jdbc:mysql://localhost:3306/usersecurity";
            String username = "root";
            String password = "";
            // Thiết lập kết nối và trả về
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            // Xử lý ngoại lệ khi có lỗi kết nối
            e.printStackTrace();
            return null; // Trả về null khi có lỗi
        }
    }
    /**
     * Đóng PreparedStatement được cung cấp.
     *
     * @param preparedStatement PreparedStatement cần đóng.
     */
    public static void closeConnection(PreparedStatement preparedStatement) {
        try {
            // Đóng PreparedStatement nếu nó chưa được đóng
            if (preparedStatement != null && !preparedStatement.isClosed()) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            // Xử lý ngoại lệ khi có lỗi đóng kết nối
            e.printStackTrace();
        }
    }


}