module com.company.coursework {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.company.coursework.Models;
    opens com.company.coursework.Models to javafx.fxml;
    exports com.company.coursework.Main;
    opens com.company.coursework.Main to javafx.fxml;
    exports com.company.coursework.StartWindow;
    opens com.company.coursework.StartWindow to javafx.fxml;
}