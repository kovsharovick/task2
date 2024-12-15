module  ru.vsu.cs.yesikov.seabattle {
    requires javafx.controls;
    requires javafx.fxml;


    opens  ru.vsu.cs.yesikov.seabattle to javafx.fxml;
    exports  ru.vsu.cs.yesikov.seabattle;
}