package PieceStuff;

import java.util.ArrayList;

import BoardStuff.BoardCell;
import BoardStuff.DrawableObject;
import ChessGroup.Chess.Constants;
import ChessGroup.Chess.Player;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public abstract class Piece extends DrawableObject{
	
	protected String name;
	protected BoardCell boardCell;
	private static Font font = new Font("Arial",60);
	private boolean isBlack;

	public Piece(Color baseColor, BoardCell boardCell) {
		super(baseColor, new Point2D(boardCell.centerPoint.getX(),boardCell.centerPoint.getY()));
		this.boardCell = boardCell;
		this.isBlack = baseColor == Constants.blackColorPiece;
	}
	
	public void setBoardCell(BoardCell boardCell) {
        this.boardCell = boardCell;
    }
	
	public BoardCell getBoardCell() {
		return boardCell;
	}
	
	public boolean isBlack() {
		return isBlack;
	}
	
	public boolean isSameColor(Piece piece) {
	    return this.isBlack == piece.isBlack;
	}
	
	public boolean isSameColor(Player player) {
        return this.isBlack == player.isBlack();
    }
	
	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(baseColor);
		gc.setFont(font);
		gc.fillText(name, centerPoint.getX()-font.getSize()/3, centerPoint.getY()+font.getSize()/3);
	}
	
	@Override
	public void repositionGeometryOnResize() {
		setCenterPoint(new Point2D(boardCell.centerPoint.getX(),boardCell.centerPoint.getY()));
	}
	
	public void movePiece(BoardCell clickedBoardCell) {
	    if(clickedBoardCell.hasPiece()) {
	        if(!this.isSameColor(clickedBoardCell.getPiece())) {
	            boardCell.movePieceTo(clickedBoardCell);
	        }
	    }else {
	        boardCell.movePieceTo(clickedBoardCell);
	    }
	}
	
	protected void addPossibleMoveBoardCell(ArrayList<BoardCell> possibleMoveBoardCells, BoardCell boardCell) {
	    if(!possibleMoveBoardCells.contains(boardCell)) {
	        possibleMoveBoardCells.add(boardCell);
	    }
	}
	
	public abstract void setpossibleMoveBoardCells(BoardCell[][] boardCells,ArrayList<BoardCell> possibleMoveBoardCells);
	
	protected abstract int[] calculateLimits(BoardCell[][] boardCells);
	
	protected int newBestLimit(int oldLimit, int newLimit) {
	    if(oldLimit > newLimit) {
            return newLimit;
        }
	    return oldLimit;
	}
	
	@Override
	public String toString() {
	    return isBlack?"black ":"white " + name + " Piece:\nstanding on " + boardCell.toString();
	}
}
