module empresa.consultorio {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires com.jfoenix;
    requires org.controlsfx.controls;
    requires bcrypt;
    requires jasperreports;
    requires java.desktop;
    opens empresa.consultorio to javafx.fxml;
    opens modelos to javafx.base;

    exports empresa.consultorio;
    exports clases;
}
