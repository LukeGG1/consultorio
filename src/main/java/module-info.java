module empresa.consultorio {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;


    opens empresa.consultorio to javafx.fxml;
    exports empresa.consultorio;
    exports clases;
}
