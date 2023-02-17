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
    
    public void removePiece(Piece piece) {
        removedPieces.add(piece);
        if(piece.isBlack()) {
            blackPieces.remove(piece);
        }else {
            whitePieces.remove(piece);
        }
    }
    
    private void addPiece(Piece piece) {
    	piece.getBoardCell().setPiece(piece);
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
    
    public void reverseLastMove() {
        BoardCell lastMovePieceCurrentBoardCell = lastMovePiece.getBoardCell();
        lastMovePiece.getBoardCell().movePieceTo(lastMoveBoardCell);
        lastMovePieceCurrentBoardCell.setPiece(lastRemovedPiece);
        if(lastMovePiece instanceof Pawn) {
            ((Pawn)lastMovePiece).reverseMovedOnceStatus();
        }
        if(lastRemovedPiece != null) {
            addPiece(lastRemovedPiece);
            lastRemovedPiece.movePiece(lastMovePieceCurrentBoardCell);
            removedPieces.remove(removedPieces.get(removedPieces.size()-1));
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
    
    public boolean isCheck(Player player,boolean ignoreKing) {
        allPossibleMoveBoardCells.clear(); 
        ArrayList<Piece> otherTeamPieces = new ArrayList<Piece>();
        if(player.isBlack()) {
            otherTeamPieces = whitePieces;
        }else {
            otherTeamPieces = blackPieces;
        }
        for (Piece piece : otherTeamPieces) {
            if(ignoreKing && piece instanceof King) {
                continue;
            }
            piece.setPossibleMoveBoardCells(board.getBoardCells(), allPossibleMoveBoardCells,false);
        }
        for(BoardCell boardCell : allPossibleMoveBoardCells) {
            if(boardCell.hasPiece() && boardCell.getPiece() instanceof King && boardCell.getPiece().isSameColor(player)) {
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<Piece> getResponsibleCheckPieces(Player player) {
        ArrayList<Piece> responsiblePieces = new ArrayList<Piece>();
        ArrayList<BoardCell> piecePossibleMoveBoardCells = new ArrayList<BoardCell>();
        
        ArrayList<Piece> otherTeamPieces = new ArrayList<Piece>();
        if(player.isBlack()) {
            otherTeamPieces = whitePieces;
        }else {
            otherTeamPieces = blackPieces;
        }
        for (Piece piece : otherTeamPieces) {
            piecePossibleMoveBoardCells.clear();
                piece.setPossibleMoveBoardCells(board.getBoardCells(), piecePossibleMoveBoardCells,false);
                for(BoardCell boardCell : piecePossibleMoveBoardCells) {
                    if(boardCell.hasPiece() && boardCell.getPiece() instanceof King && boardCell.getPiece().isSameColor(player)) {
                        responsiblePieces.add(piece);
                    }
                }
        }
        
        return responsiblePieces;
    }
    
    private void printPieces(ArrayList<Piece> pieces) {
        for(Piece piece : pieces) {
            System.out.println(piece);
        }
    }
    
    private void printResponsiblePieces(Player player) {
        ArrayList<Piece> responsiblePieces = getResponsibleCheckPieces(player);
        if(responsiblePieces.size() == 0) {
            return;
        }
        System.out.println("responsible Pieces are:________________");
    	printPieces(getResponsibleCheckPieces(player));
    	System.out.println("_______________________________________");
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
    	        GameOverlay.getOverlayInstance().updateIsCheck(isCheck(newTurningPlayer,false),newTurningPlayer);
//    	        printPieces(removedPieces);
    	        printResponsiblePieces(newTurningPlayer);
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
            selectedPiece.setPossibleMoveBoardCells(board.getBoardCells(), possibleMoveBoardCells,false);
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
