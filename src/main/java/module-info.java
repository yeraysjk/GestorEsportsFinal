module com.torneo.projectegestoresportsfinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.torneo.projectegestoresportsfinal to javafx.fxml;
    exports com.torneo.projectegestoresportsfinal;
    exports com.torneo.projectegestoresportsfinal.controladors;
    opens com.torneo.projectegestoresportsfinal.controladors to javafx.fxml;
    exports com.torneo.projectegestoresportsfinal.libs;
    opens com.torneo.projectegestoresportsfinal.libs to javafx.fxml;
    exports com.torneo.projectegestoresportsfinal.classes;
    opens com.torneo.projectegestoresportsfinal.classes to javafx.fxml;
}