package ChessGroup.Chess;

import BoardStuff.PawnPromotion;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;

public class GUIController {
	public static final double initial_width = Screen.getPrimary().getBounds().getWidth() * 0.99;
    public static final double initial_height = Screen.getPrimary().getBounds().getHeight() * 0.9;
	
	@FXML
	AnchorPane anchorPane;
	static ResizeableCanvas resizeableCanvas;
	static Renderer renderer;
	static GameLogic gameLogic;

    @FXML 
    private void initialize() {
    	resizeableCanvas = new ResizeableCanvas(initial_width,initial_height);
    	System.out.println("initialized ResizeableCanvas:\nwidth=" + resizeableCanvas.getWidth() + "\nheight=" + resizeableCanvas.getHeight());
    	anchorPane.getChildren().add(resizeableCanvas);
    	gameLogic = new GameLogic();
    	System.out.println("initialized GameLogic:");
    	PawnPromotion.initPromotionBoard();
        System.out.println("initalized PromotionBoard:");
    	gameLogic.initPlayers(true);
        System.out.println("initalized Players:");
        
    	renderer = new Renderer(resizeableCanvas);
    	System.out.println("initialized Renderer:");
    	
    	final GameLoopTimer timer = new GameLoopTimer() {
            @Override
            public void tick(float secondsSinceLastFrame) {
                renderer.update(secondsSinceLastFrame);
                renderer.prepare();
                renderer.render();
            }
        };
        
        // add listeners to capture and handle window resize events
        resizeableCanvas.widthProperty().addListener(e -> resizedCanvas());
        resizeableCanvas.heightProperty().addListener(e -> resizedCanvas());
        renderer.bind(anchorPane);
        System.out.println("bound renderer to anchorPane:");
        
        gameLogic.initPieces();
        System.out.println("initalized GamePieces:");
        
        resizeableCanvas.setOnMouseClicked(this::mouseClicked);
        resizeableCanvas.setOnMouseMoved(this::mouseMoved);

        // start the main loop
        timer.start();
    }
    
    // event handle method for window resizing
    private void resizedCanvas() {
        // reposition geometry elements based on new window/canvas dimensions
        renderer.onResize();
    }
    
    public static double smallestSize() {
    	return resizeableCanvas.getWidth() > resizeableCanvas.getHeight()? resizeableCanvas.getHeight():resizeableCanvas.getWidth();
    }
    public static double getCanvasWidth() {
    	return resizeableCanvas.getWidth();
    }
    public static double getCanvasHeight() {
    	return resizeableCanvas.getHeight();
    }
    
    private void mouseClicked(final MouseEvent e) {
    	gameLogic.mouseClicked(new Point2D(e.getX(), e.getY()));
    }

    private void mouseMoved(final MouseEvent e) {
        gameLogic.mouseMoved(new Point2D(e.getX(), e.getY()));
    }
}
