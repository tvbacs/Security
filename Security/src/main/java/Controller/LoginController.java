package Controller;

import DB.JDBC;
import Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.mindrot.jbcrypt.BCrypt;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML
    AnchorPane mainLogin;
    @FXML
    TextField textFusername;
    @FXML
    TextField textFpass;
   @FXML
   Button  btnLogin;
 
   

    public void changeSignup(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Signup.fxml"));
            Parent root = loader.load();
            mainLogin.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btn_login(MouseEvent event) {
        String username = textFusername.getText();
        String password = textFpass.getText();
        User user = authenticateUser(username, password);
        if (user != null) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Login successful!");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/home.fxml"));
                Parent root = loader.load();
                mainLogin.getChildren().setAll(root);
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
        try (Connection connection = JDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM user WHERE username = ?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Lấy mật khẩu đã lưu trong cơ sở dữ liệu
                    String hashedPassword = resultSet.getString("password");

                    // Kiểm tra xem mật khẩu đã nhập có khớp với mật khẩu đã mã hóa trong cơ sở dữ liệu không
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        return new User(username, hashedPassword); // Trả về một đối tượng User nếu xác thực thành công
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("User authentication error: " + e.getMessage());
        }
        return null; // Trả về null nếu xác thực không thành công
    }


}




