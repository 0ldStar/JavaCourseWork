module com.company.coursework {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.company.client.Models;
    opens com.company.client.Models to javafx.fxml;
    exports com.company.client.Main;
    opens com.company.client.Main to javafx.fxml;
    exports com.company.client.StartWindow;
    opens com.company.client.StartWindow to javafx.fxml;
    exports com.company.share;
    opens com.company.share to javafx.fxml;
}