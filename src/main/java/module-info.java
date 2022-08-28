module com.example.project04 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;

    opens com.example.project04 to javafx.fxml;
    exports com.example.project04;
}