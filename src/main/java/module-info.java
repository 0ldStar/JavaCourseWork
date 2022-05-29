module com.company.coursework {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.company.coursework to javafx.fxml;
    exports com.company.coursework;
    exports com.company.coursework.Models;
    opens com.company.coursework.Models to javafx.fxml;
}