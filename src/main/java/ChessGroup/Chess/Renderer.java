package ChessGroup.Chess;
import BoardStuff.Board;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;

public final class Renderer {

    // canvas and graphic context
    private final ResizeableCanvas resizeableCanvas;

    private final GraphicsContext gc;
    
    private final Board board;
    
    private final GameOverlay gameOverlay;

    // constructor
    Renderer(ResizeableCanvas resizeableCanvas) {
        // Set Debugging options for overlay
    	this.resizeableCanvas = resizeableCanvas;
    	gc = resizeableCanvas.getGraphicsContext2D();
    	board = Board.getBoardInstance();
    	board.initBoard();
    	gameOverlay = GameOverlay.getOverlayInstance();
    	gameOverlay.enableFpsOverlay(true);
    	
    }

    // binds the anchor pane width/height to the canvas width/height
    public void bind(final AnchorPane pane) {
    	resizeableCanvas.widthProperty().bind(pane.widthProperty());
    	resizeableCanvas.heightProperty().bind(pane.heightProperty());
    }

    // the update method (called once per tick)
    public void update(final double secondsSinceLastFrame) {

    	board.update(secondsSinceLastFrame);
    	gameOverlay.update(secondsSinceLastFrame);
    }

    // the render method (called once per tick)
    public void render() {
        gc.save();

        board.draw(gc);
        gameOverlay.draw(gc);

        gc.restore();
    }

    // prepares next frame to be drawn
    public void prepare() {
        // clear entire canvas and fill it with background color
        gc.setFill(Constants.backGroundColor);
        gc.fillRect(0, 0, resizeableCanvas.getWidth(), resizeableCanvas.getHeight());
//        gc.setStroke(Color.RED);
//        gc.strokeLine(0, 0, resizeableCanvas.getWidth(), resizeableCanvas.getHeight());
//        gc.strokeLine(resizeableCanvas.getWidth(), 0, 0, resizeableCanvas.getHeight());
    }

    public void onResize() {
    	board.repositionGeometryOnResize();
    }
}
