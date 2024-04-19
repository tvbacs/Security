package Controller;

import DB.JDBC;
import Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignupController {
    @FXML
    AnchorPane mainSignup;
    @FXML
    TextField textFusernameSign;
    @FXML
    TextField textpassSign;
    public void changeLogin(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/login.fxml"));
            Parent root = loader.load();
            mainSignup.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void btn_signup(MouseEvent event) {
        String username = textFusernameSign.getText();
        String password = textpassSign.getText();
        User user = authenticateUser(username, password);
        if (user != null) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Login successful!");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/login.fxml"));
                Parent root = loader.load();
                mainSignup.getChildren().setAll(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Incorrect username or password.");
        }
    }
    // Phương thức showAlert
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Bỏ qua phần tiêu đề phụ
        alert.setContentText(message);
        alert.showAndWait(); // Hiển thị cảnh báo và đợi người dùng đóng nó lại
    }




    private User authenticateUser(String username, String password) {
        // Kiểm tra xem tên người dùng đã tồn tại hay chưa
        if (isUsernameExists(username)) {
            return null;
        } else {
            // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Nếu tên người dùng chưa tồn tại, thực hiện việc đăng ký người dùng vào cơ sở dữ liệu
            try (Connection connection = JDBC.getConnection();
                 PreparedStatement statement = connection.prepareStatement("INSERT INTO user (username, password) VALUES (?, ?)")) {
                statement.setString(1, username);
                statement.setString(2, hashedPassword); // Sử dụng mật khẩu đã mã hóa
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    return new User(username, hashedPassword); // Trả về một đối tượng User đã đăng ký
                } else {
                    return null; // Trả về null nếu không có hàng nào được thêm vào cơ sở dữ liệu
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("User registration error: " + e.getMessage());
                return null;
            }
        }
    }


    // Phương thức để kiểm tra xem tên người dùng đã tồn tại hay chưa
    private boolean isUsernameExists(String username) {
        try (Connection connection = JDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM user WHERE username = ?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Trả về true nếu có ít nhất một hàng trong ResultSet
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error checking username existence: " + e.getMessage());
            return false;
        }
    }

}
