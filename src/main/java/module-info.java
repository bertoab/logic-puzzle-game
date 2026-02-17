module com.mcnz.test.gradletest {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mcnz.test.gradletest to javafx.fxml;
    exports com.mcnz.test.gradletest;
}