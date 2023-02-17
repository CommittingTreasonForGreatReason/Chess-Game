package PieceStuff;

import java.util.ArrayList;

import BoardStuff.BoardCell;
import javafx.scene.paint.Color;

public class King extends Piece{

	public King(Color baseColor, BoardCell boardCell) {
		super(baseColor, boardCell);
		this.name = "K";
	}

	@Override
	public void update(double secondsSinceLastFrame) {
		
	}

	@Override
	public void setpossibleMoveBoardCells(BoardCell[][] boardCells,ArrayList<BoardCell> possibleMoveBoardCells) {
		for (int i = 0;i<8;i++) {
    		for (int j = 0;j<8;j++) {
				if(i >= boardCell.getRow()-1 && i <= boardCell.getRow()+1 && j >= boardCell.getColumn()-1 && j <= boardCell.getColumn()+1) {
	    			if(!boardCells[i][j].hasPiece() || boardCells[i][j].hasPiece() && !this.isSameColor(boardCells[i][j].getPiece())) {
	    			    addPossibleMoveBoardCell(possibleMoveBoardCells, boardCells[i][j]);
	    			}
	    		}
    		}
		}
	}

    @Override
    protected int[] calculateLimits(BoardCell[][] boardCells) {
        return null;
    }
	
}
