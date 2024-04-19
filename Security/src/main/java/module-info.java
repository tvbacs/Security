module com.example.security {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires jbcrypt;

    opens View to javafx.fxml;
    exports View;
    exports Controller;
    opens Controller to javafx.fxml;

}