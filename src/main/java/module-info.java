module com.example.finishedproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.finishedproject to javafx.fxml;
    exports com.example.edpfx;
    opens com.example.edpfx to javafx.fxml;
}