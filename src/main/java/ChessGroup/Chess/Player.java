package ChessGroup.Chess;

public class Player {
    private boolean isBlack;
    private boolean hasTurn;
    
    public Player(boolean isBlack,boolean hasTurn) {
        this.isBlack = isBlack;
        this.hasTurn = hasTurn;
    }
    
    public boolean isBlack() {
        return isBlack;
    }
    public boolean hasTurn() {
        return hasTurn;
    }
    public void toggleTurn() {
        hasTurn = !hasTurn;
    }
}
