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

public class GameLogic {
	private ArrayList<Piece> pieces = new ArrayList<Piece>();
	
	// singleton instance
    private static GameLogic gameLogic;
    
    private Board board;
    
    private static BoardCell selectedBoardCell = null;
    private static ArrayList<BoardCell> possibleMoveBoardCells = new ArrayList<BoardCell>();
    private static ArrayList<BoardCell> allPossibleMoveBoardCells = new ArrayList<BoardCell>();
    
    Player blackPlayer,whitePlayer;
    
    public GameLogic() {
    	board = Board.getBoardInstance();
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
    
    // singleton getter
    public static GameLogic getGameLogicInstance() {
        if (gameLogic == null) {
        	gameLogic = new GameLogic();
        }
        return gameLogic;
    }
    
    public void removePiece(Piece piece) {
        GameLogic.getGameLogicInstance().pieces.remove(piece);
    }
    
    private void addPiece(Piece piece) {
    	piece.getBoardCell().setPiece(piece);
    	GameLogic.getGameLogicInstance().pieces.add(piece);
    }
    
    public void initPlayers(boolean blackHasTurn) {
        blackPlayer = new Player(true, blackHasTurn);
        whitePlayer = new Player(false, !blackHasTurn);
    }
    
    public void initPieces() {
    	for(int i = 0;i<8;i++) {
    		addPiece(new Pawn(Constants.blackColorPiece, board.getBoardCell(1, i)));
    		addPiece(new Pawn(Constants.whiteColorPiece, board.getBoardCell(6, i)));
    	}
    	addPiece(new Rook(Constants.blackColorPiece, board.getBoardCell(0, 0)));
    	addPiece(new Rook(Constants.blackColorPiece, board.getBoardCell(0, 7)));
    	addPiece(new Rook(Constants.whiteColorPiece, board.getBoardCell(7, 0)));
    	addPiece(new Rook(Constants.whiteColorPiece, board.getBoardCell(7, 7)));
    	
    	addPiece(new Knight(Constants.blackColorPiece, board.getBoardCell(0, 1)));
    	addPiece(new Knight(Constants.blackColorPiece, board.getBoardCell(0, 6)));
    	addPiece(new Knight(Constants.whiteColorPiece, board.getBoardCell(7, 1)));
    	addPiece(new Knight(Constants.whiteColorPiece, board.getBoardCell(7, 6)));
    	
    	addPiece(new Bishop(Constants.blackColorPiece, board.getBoardCell(0, 2)));
        addPiece(new Bishop(Constants.blackColorPiece, board.getBoardCell(0, 5)));
        addPiece(new Bishop(Constants.whiteColorPiece, board.getBoardCell(7, 2)));
        addPiece(new Bishop(Constants.whiteColorPiece, board.getBoardCell(7, 5)));
        
        addPiece(new Queen(Constants.blackColorPiece, board.getBoardCell(0, 3)));
        addPiece(new King(Constants.blackColorPiece, board.getBoardCell(0, 4)));
        addPiece(new Queen(Constants.whiteColorPiece, board.getBoardCell(7, 3)));
        addPiece(new King(Constants.whiteColorPiece, board.getBoardCell(7, 4)));
    }
    
    private void selectBoardCell(BoardCell boardCell) {
    	selectedBoardCell = boardCell;
    }
    
    private void toggleTurns() {
        blackPlayer.toggleTurn();
        whitePlayer.toggleTurn();
        GameOverlay.getOverlayInstance().updateTurningPlayerInfo(blackPlayer);
    }
    
    private boolean isCheck(Player player) {
        allPossibleMoveBoardCells.clear(); 
        for (Piece piece : GameLogic.getGameLogicInstance().pieces) {
            if(!piece.isSameColor(player)) {
                piece.setpossibleMoveBoardCells(board.getBoardCells(), allPossibleMoveBoardCells);
            }
        }
        for(BoardCell boardCell : allPossibleMoveBoardCells) {
            if(boardCell.hasPiece() && boardCell.getPiece() instanceof King && boardCell.getPiece().isSameColor(player)) {
                return true;
            }
        }
        return false;
    }
    
    private void tryClickBoardCell(Point2D mousePoint) {
    	BoardCell clickedBoardCell = board.getClickedBoardCell(mousePoint);
    	
    	if(clickedBoardCell!=null) {
    	    if(trySelectPiece(clickedBoardCell)) {
    	        return;
    	    }
    	    if(tryMovePiece(clickedBoardCell)){
    	        toggleTurns();
    	        Player newTurningPlayer = blackPlayer.hasTurn()?blackPlayer:whitePlayer;
    	        GameOverlay.getOverlayInstance().updateIsCheck(isCheck(newTurningPlayer),newTurningPlayer);
    	    }
    	}
    	possibleMoveBoardCells.clear(); 
    	selectedBoardCell = null;
    }
    
    private boolean trySelectPiece(BoardCell clickedBoardCell) {
        Player turningPlayer = blackPlayer.hasTurn()?blackPlayer:whitePlayer;
        if(clickedBoardCell.hasPiece() && clickedBoardCell.getPiece().isBlack() == turningPlayer.isBlack() && selectedBoardCell == null) {
            Piece selectedPiece = clickedBoardCell.getPiece();
            selectBoardCell(clickedBoardCell);
            possibleMoveBoardCells.clear(); 
            selectedPiece.setpossibleMoveBoardCells(board.getBoardCells(), possibleMoveBoardCells);
            return true;
        }
        return false;
    }
    
    private boolean tryMovePiece(BoardCell clickedBoardCell) {
        if(selectedBoardCell!=null && possibleMoveBoardCells.contains(clickedBoardCell)){
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
    
    
}
