module com.mcnz.test.test {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mcnz.test.test to javafx.fxml;
    exports com.mcnz.test.test;
}