package ChessGroup.Chess;

import BoardStuff.Board;
import BoardStuff.DrawableObject;
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

    // Graphical elements
    private String playerTurnIndicator = "Black";
    private String playerCheckIndicator = "Black is NOT in check";
    private double turnOverlayWidth,turnOverlayHeight;

    private boolean perfOverlayEnabled = false;

    private double overlayUpdateCnt = 0;
    
    public GameOverlay(Color baseColor, Point2D centerPoint) {
        super(baseColor, centerPoint);
        repositionGeometryOnResize();
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
        playerTurnIndicator = blackPlayer.hasTurn()?"Black":"White";
    }
    
    public void updateIsCheck(boolean isCheck, Player player) {
        playerCheckIndicator = (player.isBlack()?"Black":"White") + " is " + (isCheck?"":"NOT") + " in check";
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
        gc.setFill(Constants.gameOverlayBackGroundColor);
        gc.fillRect(0, 0, turnOverlayWidth, turnOverlayHeight);
        gc.setFill(Color.BLACK);
        gc.setFont(playerTurnOverlayFont);
        gc.fillText(playerTurnIndicator, 20, 60);
        // is check overlay
        gc.setFill(Color.BLACK);
        gc.setFont(playerTurnOverlayFont);
        gc.fillText(playerCheckIndicator, 20, 100);

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
