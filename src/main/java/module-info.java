module com.torneo.projectegestoresportsfinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.torneo.projectegestoresportsfinal to javafx.fxml;
    exports com.torneo.projectegestoresportsfinal;
}