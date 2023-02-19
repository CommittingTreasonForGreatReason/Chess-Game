package ChessGroup.Chess;

import java.util.ArrayList;

import BoardStuff.Board;
import BoardStuff.BoardCell;
import PieceStuff.Bishop;
import PieceStuff.King;
import PieceStuff.Knight;
import PieceStuff.Pawn;
import PieceStuff.Piece;
import PieceStuff.Queen;
import PieceStuff.Rook;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyEvent;

public class GameLogic {
    private static ArrayList<Piece> blackPieces = new ArrayList<Piece>();
	private static ArrayList<Piece> whitePieces = new ArrayList<Piece>();
	private static ArrayList<Piece> removedPieces = new ArrayList<Piece>();
	public static Piece lastMovePiece,lastRemovedPiece;
	public static BoardCell lastMoveBoardCell;
	
	// singleton instance
    private static GameLogic gameLogic;
    
    private Board board;
    
    private static BoardCell selectedBoardCell = null;
    private static ArrayList<BoardCell> possibleMoveBoardCells = new ArrayList<BoardCell>();
    private static ArrayList<BoardCell> allPossibleMoveBoardCells = new ArrayList<BoardCell>();
    
    static Player blackPlayer,whitePlayer;
    
    public GameLogic() {
    	board = Board.getBoardInstance();
    }
    
    public static ArrayList<Piece> getBlackPieces() {
        return blackPieces;
    }
    public static ArrayList<Piece> getWhitePieces() {
        return whitePieces;
    }
    
    public static BoardCell getSelectedBoardCell() {
		return selectedBoardCell;
	}
    
    public static ArrayList<BoardCell> getPossibleMoveBoardCells() {
		return possibleMoveBoardCells;
	}
    public static ArrayList<BoardCell> getAllPossibleMoveBoardCells() {
        return allPossibleMoveBoardCells;
    }
    
    public static ArrayList<Piece> getTeamPieces(boolean isBlack){
        return isBlack?blackPieces:whitePieces;
    }
    
    // singleton getter
    public static GameLogic getGameLogicInstance() {
        if (gameLogic == null) {
        	gameLogic = new GameLogic();
        }
        return gameLogic;
    }
    
    public static void removePiece(Piece piece) {
        removedPieces.add(piece);
        if(piece.isBlack()) {
            blackPieces.remove(piece);
        }else {
            whitePieces.remove(piece);
        }
    }
    
    private static void addPiece(Piece piece) {
    	piece.getBoardCell().setPiece(piece);
    	removedPieces.remove(piece);
    	if(piece.isBlack()) {
            blackPieces.add(piece);
        }else {
            whitePieces.add(piece);
        }
    }
    
    public void initPlayers(boolean blackHasTurn) {
        blackPlayer = new Player(true, blackHasTurn);
        whitePlayer = new Player(false, !blackHasTurn);
    }
    
    public void initPieces() {
    	for(int i = 0;i<8;i++) {
    		addPiece(new Pawn(blackPlayer, board.getBoardCell(1, i)));
    		addPiece(new Pawn(whitePlayer, board.getBoardCell(6, i)));
    	}
    	addPiece(new Rook(blackPlayer, board.getBoardCell(0, 0)));
    	addPiece(new Rook(blackPlayer, board.getBoardCell(0, 7)));
    	addPiece(new Rook(whitePlayer, board.getBoardCell(7, 0)));
    	addPiece(new Rook(whitePlayer, board.getBoardCell(7, 7)));
    	
    	addPiece(new Knight(blackPlayer, board.getBoardCell(0, 1)));
    	addPiece(new Knight(blackPlayer, board.getBoardCell(0, 6)));
    	addPiece(new Knight(whitePlayer, board.getBoardCell(7, 1)));
    	addPiece(new Knight(whitePlayer, board.getBoardCell(7, 6)));
    	
    	addPiece(new Bishop(blackPlayer, board.getBoardCell(0, 2)));
        addPiece(new Bishop(blackPlayer, board.getBoardCell(0, 5)));
        addPiece(new Bishop(whitePlayer, board.getBoardCell(7, 2)));
        addPiece(new Bishop(whitePlayer, board.getBoardCell(7, 5)));
        
        addPiece(new Queen(blackPlayer, board.getBoardCell(0, 3)));
        addPiece(new King(blackPlayer, board.getBoardCell(0, 4)));
        addPiece(new Queen(whitePlayer, board.getBoardCell(7, 3)));
        addPiece(new King(whitePlayer, board.getBoardCell(7, 4)));
    }
    
    private static void selectBoardCell(BoardCell boardCell) {
    	selectedBoardCell = boardCell;
    }
    
    public static void reverseLastMove() {
        BoardCell lastMovePieceCurrentBoardCell = lastMovePiece.getBoardCell();
        lastMovePiece.forcePiece(lastMoveBoardCell);
        if(lastRemovedPiece != null) {
            addPiece(lastRemovedPiece);
        }
        lastMovePieceCurrentBoardCell.setPiece(lastRemovedPiece);
        
        if(lastMovePiece instanceof Pawn) {
            ((Pawn)lastMovePiece).reverseMovedOnceStatus();
        }
        
        lastMoveBoardCell = null;
        lastRemovedPiece = null;
        lastRemovedPiece = null;
    }
    
    private void toggleTurns() {
        blackPlayer.toggleTurn();
        whitePlayer.toggleTurn();
        GameOverlay.getOverlayInstance().updateTurningPlayerInfo(blackPlayer);
    }
    
    public boolean isCheck(Player player, boolean simulate) {
        allPossibleMoveBoardCells.clear(); 
        ArrayList<Piece> otherTeamPieces = player.isBlack()?whitePieces:blackPieces;
        for (Piece piece : otherTeamPieces) {
            piece.setPossibleMoveBoardCells(board.getBoardCells(), allPossibleMoveBoardCells,simulate);
        }
        for(BoardCell boardCell : allPossibleMoveBoardCells) {
            if(boardCell.hasPiece() && boardCell.getPiece() instanceof King && boardCell.getPiece().isSameColor(player)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isCheckMate(Player player) {
        ArrayList<BoardCell> allPossibleMoveBoardCellsCheckMate = new ArrayList<BoardCell>();
        ArrayList<Piece> thisTeamPieces = player.isBlack()?blackPieces:whitePieces;
        for (Piece piece : thisTeamPieces) {
            
            piece.setPossibleMoveBoardCells(board.getBoardCells(), allPossibleMoveBoardCellsCheckMate,true);
        }
        System.out.println("possible Moves to Make:" + allPossibleMoveBoardCellsCheckMate.size());
        return allPossibleMoveBoardCellsCheckMate.size()==0;
    }
    
    private void printPieces(ArrayList<Piece> pieces) {
        System.out.println("_________________________________________");
        for(Piece piece : pieces) {
            System.out.println(piece);
        }
        System.out.println("_________________________________________");
    }
    
    private void tryClickBoardCell(Point2D mousePoint) {
    	BoardCell clickedBoardCell = board.getClickedBoardCell(mousePoint);
    	
    	if(clickedBoardCell!=null) {
    	    if(trySelectPiece(clickedBoardCell)) {
    	        return;
    	    }
    	    if(tryMovePiece(clickedBoardCell)){
    	        toggleTurns();
    	        printPieces(removedPieces);
    	        Player turningPlayer = blackPlayer.hasTurn()?blackPlayer:whitePlayer;
    	        GameOverlay.getOverlayInstance().updateIsCheck(isCheck(turningPlayer,false),turningPlayer);
    	        GameOverlay.getOverlayInstance().updateIsCheckMate(isCheckMate(turningPlayer),turningPlayer);
    	    }
    	}

    	possibleMoveBoardCells.clear(); 
    	selectedBoardCell = null;
    	printCheckState(blackPlayer);
    	printCheckState(whitePlayer);
    }
    
    private void printCheckState(Player player) {
        System.out.println("_________________________________________");
        System.out.println("Check States");
        System.out.println((player.isBlack()?"black":"white") + " has turn:");
        System.out.println("isCheck: "+isCheck(player,false));
        System.out.println("isCheckMate: "+isCheckMate(player));
        System.out.println("_________________________________________");
    }
    
    private boolean trySelectPiece(BoardCell clickedBoardCell) {
        Player turningPlayer = blackPlayer.hasTurn()?blackPlayer:whitePlayer;
        if(clickedBoardCell.hasPiece() && clickedBoardCell.getPiece().isBlack() == turningPlayer.isBlack() && selectedBoardCell == null) {
            Piece selectedPiece = clickedBoardCell.getPiece();
            selectBoardCell(clickedBoardCell);
            possibleMoveBoardCells.clear(); 
            selectedPiece.setPossibleMoveBoardCells(board.getBoardCells(), possibleMoveBoardCells,true);
            return true;
        }
        return false;
    }
    
    public boolean tryMovePiece(BoardCell clickedBoardCell) {
        if(selectedBoardCell!=null && possibleMoveBoardCells.contains(clickedBoardCell)){
            GameLogic.lastMovePiece = selectedBoardCell.getPiece();
            GameLogic.lastMoveBoardCell = selectedBoardCell;
            GameLogic.lastRemovedPiece = clickedBoardCell.getPiece();
            selectedBoardCell.getPiece().movePiece(clickedBoardCell);
            return true;
        }
        return false;
    }
    
    void mouseClicked(Point2D mousePoint) {
    	tryClickBoardCell(mousePoint);
    }
    void mouseMoved(Point2D mousePoint) {
    	
    }

    void keyPressed(KeyEvent e) {
        if(e.getText().equals("r")) {
            if(lastMoveBoardCell != null) {
                System.err.println("reversing move");
                reverseLastMove();
                toggleTurns();
            }else {
                System.err.println("cannot reverse move");
            }
            
        }
    }
    
    
}
