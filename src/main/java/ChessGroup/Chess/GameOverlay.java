package ChessGroup.Chess;
import java.util.ArrayList;

import BoardStuff.Board;
import BoardStuff.DrawableObject;
import BoardStuff.PawnPromotion;
import PieceStuff.Piece;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public final class GameOverlay extends DrawableObject {
    
    // Debugging settings
    private static final boolean framerateCapEnabled = false;

    // overlay text displays
    private static final String fpsString = "FPS: ";

    // fonts for debugging text
    private static final Font perfOverlayFont = new Font("Arial", 13);
    private static final Font playerTurnOverlayFont = new Font("Arial", 20);

    // singleton instance
    private static GameOverlay overlayInstance;

    private final StringBuilder fpsCntStr = new StringBuilder(fpsString);

    // Graphical Elements
    // Turn Overlay
    private String playerTurnIndicator = "uninitalized";
    private String playerCheckIndicator = "uninitalized";
    private Color playerCheckColor = null;
    private String playerCheckMateIndicator = "uninitalized";
    private Color playerCheckMateColor = null;
    private double turnOverlayWidth,turnOverlayHeight;
    // Pawn Promotion Overlay
    private boolean pawnPromotionOverlayEnabled; 
    

    private boolean perfOverlayEnabled = false;

    private double overlayUpdateCnt = 0;

    public GameOverlay(Color baseColor, Point2D centerPoint) {
        super(baseColor, centerPoint);
        repositionGeometryOnResize();
    }
    
    public void init() {
        updateTurningPlayerInfo(GameLogic.blackPlayer);
        updateIsCheck(false, GameLogic.blackPlayer);
        updateIsCheckMate(false, GameLogic.blackPlayer);
    }
    
    public static Point2D getScreenCenterPoint() {
        return new Point2D(GUIController.getCanvasWidth()/2,GUIController.getCanvasHeight()/2);
    }
    
    public void togglePawnPromotionOverlay() {
        pawnPromotionOverlayEnabled = !pawnPromotionOverlayEnabled;
    }
    // singleton instance getter
    public static GameOverlay getOverlayInstance() {
        if (overlayInstance == null) {
            overlayInstance = new GameOverlay(null,null);
        }
        return overlayInstance;
    }

    // method to enable / disable performance overlay
    public void enableFpsOverlay(boolean enabled) {
        perfOverlayEnabled = enabled;
    }

    // Setters for overlay texts
    public void updateTurningPlayerInfo(Player blackPlayer) {
        playerTurnIndicator = "Turn: " + (blackPlayer.hasTurn()?"Black":"White"); 
    }
    
    public void updateIsCheck(boolean isCheck, Player player) {
        playerCheckIndicator = "is " + (isCheck?"":"NOT") + " check";
        playerCheckColor = isCheck?Constants.redColor:Constants.greenColor;
    }
    public void updateIsCheckMate(boolean isCheckMate, Player player) {
        playerCheckMateIndicator = "is " + (isCheckMate?"":"NOT") + " check mate";
        playerCheckMateColor = isCheckMate?Constants.redColor:Constants.greenColor;
    }

    @Override
    public void update(final double secondsSinceLastFrame) {
        // update the performance overlay text
        if (!perfOverlayEnabled) {
            return;
        }

        // update turning player and player state displays
        // ...

        // don't update every frame (too fast)
        if (overlayUpdateCnt >= 170.0 && secondsSinceLastFrame > 0.0001) {
            fpsCntStr.delete(fpsString.length(), Integer.MAX_VALUE);
            fpsCntStr.insert(fpsString.length(), (int) (1.0 / secondsSinceLastFrame));
            overlayUpdateCnt = 0;
        }
        overlayUpdateCnt += secondsSinceLastFrame * 1000.0;
    }

    @Override
    public void draw(GraphicsContext gc) {
        // draw overlay text (mainly for debugging and performance analysis)
        if (perfOverlayEnabled) {
            gc.setFill(Color.GREEN);
            gc.setFont(perfOverlayFont);
            gc.fillText(fpsCntStr.toString(), 20, 20);
        }
        // turn overlay
        gc.setFont(playerTurnOverlayFont);
        gc.setFill(Constants.gameOverlayBackGroundColor);
        gc.fillRect(0, 0, turnOverlayWidth, turnOverlayHeight);
        
        gc.setFill(Color.WHITE);
        gc.fillText(playerTurnIndicator, turnOverlayWidth/6, turnOverlayHeight/6);
        // is check overlay
        gc.setFill(playerCheckColor);
        gc.fillText(playerCheckIndicator, turnOverlayWidth/6, turnOverlayHeight/6*2);
        gc.setFill(playerCheckMateColor);
        gc.fillText(playerCheckMateIndicator, turnOverlayWidth/6, turnOverlayHeight/6*3);
        // pawn promotion overlay
        if(pawnPromotionOverlayEnabled) {
            GameLogic.pawnPromotion.draw(gc);
        }
        

        // avoid to run application at maximum speed (cap fps)
        if (framerateCapEnabled) {
            try {
                Thread.sleep(4);
            } catch (InterruptedException ignored) {
            }
        }
    }

    @Override
    public void repositionGeometryOnResize() {
        turnOverlayWidth = Board.getBoardInstance().getSize()/2;
        turnOverlayHeight = Board.getBoardInstance().getSize()/3;
    }
}
