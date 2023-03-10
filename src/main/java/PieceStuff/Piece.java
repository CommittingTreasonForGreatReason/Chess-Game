package PieceStuff;

import java.util.ArrayList;

import BoardStuff.BoardCell;
import BoardStuff.DrawableObject;
import ChessGroup.Chess.Constants;
import ChessGroup.Chess.GameLogic;
import ChessGroup.Chess.Player;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public abstract class Piece extends DrawableObject{
	
	protected String name;
	protected byte sortingValue;
	protected BoardCell boardCell;
	private static Font font = new Font("Arial",60);
	protected Player player;

	public Piece(Player player, BoardCell boardCell) {
		super(player.isBlack()?Constants.blackColorPiece:Constants.whiteColorPiece, new Point2D(boardCell.centerPoint.getX(),boardCell.centerPoint.getY()));
		this.boardCell = boardCell;
		this.player = player;
	}
	
	public void setBoardCell(BoardCell boardCell) {
        this.boardCell = boardCell;
        repositionGeometryOnResize();
    }
	
	public BoardCell getBoardCell() {
		return boardCell;
	}
	public byte getSortingValue() {
        return sortingValue;
    }
	
	public boolean isBlack() {
		return player.isBlack();
	}
	
	public boolean isSameColor(Piece piece) {
	    return this.isBlack() == piece.isBlack();
	}
	
	public boolean isSameColor(Player player) {
        return this.isBlack() == player.isBlack();
    }
	
	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(baseColor);
		gc.setFont(font);
		gc.fillText(name, centerPoint.getX()-font.getSize()/3, centerPoint.getY()+font.getSize()/3);
	}
	
	public void drawPromotion(GraphicsContext gc, Point2D centerPoint, double size) {
	    gc.setFill(baseColor);
        gc.setFont(new Font("Arial",size));
        gc.fillText(name, centerPoint.getX()-font.getSize()/3, centerPoint.getY()+font.getSize()/3);
	}
	
	@Override
	public void repositionGeometryOnResize() {
		setCenterPoint(new Point2D(boardCell.centerPoint.getX(),boardCell.centerPoint.getY()));
	}
	
	public void movePiece(BoardCell clickedBoardCell) {
	    boardCell.movePieceTo(clickedBoardCell);
	}
	
	public void forcePiece(BoardCell clickedBoardCell) {
	    boardCell.movePieceTo(clickedBoardCell);
	}
	
	protected void addPossibleMoveBoardCell(ArrayList<BoardCell> possibleMoveBoardCells, BoardCell boardCell) {
	    if(!possibleMoveBoardCells.contains(boardCell)) {
	        possibleMoveBoardCells.add(boardCell);
	    }
	}
	
	public abstract void setPossibleMoveBoardCells(BoardCell[][] boardCells,ArrayList<BoardCell> possibleMoveBoardCells, boolean simulate);
	
	protected abstract int[] calculateLimits(BoardCell[][] boardCells);
	
	protected void trySetPossibleMove(BoardCell[][] boardCells,ArrayList<BoardCell> possibleMoveBoardCells, BoardCell moveBoardCell, boolean simulate){
	    if(simulate) {
	        simulateMove(boardCells, possibleMoveBoardCells, moveBoardCell);
	    }else {
	        addPossibleMoveBoardCell(possibleMoveBoardCells, moveBoardCell);
	    }
	}
	
	protected void simulateMove(BoardCell[][] boardCells,ArrayList<BoardCell> possibleMoveBoardCells, BoardCell moveBoardCell) {
	    GameLogic.lastMovePiece = (Piece) this;
        GameLogic.lastMoveBoardCell = this.getBoardCell();
        GameLogic.lastRemovedPiece = moveBoardCell.getPiece();
        movePiece(moveBoardCell);
        if(!GameLogic.getGameLogicInstance().isCheck(player,false)) {
            addPossibleMoveBoardCell(possibleMoveBoardCells, moveBoardCell);
        }
        GameLogic.reverseLastMove();
	}
	
	protected int newBestLimit(int oldLimit, int newLimit) {
	    if(oldLimit > newLimit) {
            return newLimit;
        }
	    return oldLimit;
	}
	
	@Override
	public String toString() {
	    return "--------------------------------\n"+ (isBlack()?"black ":"white ") + name + " Piece:\nstanding on " + boardCell.toString();
	}
}
