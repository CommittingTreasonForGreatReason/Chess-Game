module ChessGroup.Chess {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;

    opens ChessGroup.Chess to javafx.fxml;
    exports ChessGroup.Chess;
}
