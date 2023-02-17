package PieceStuff;

import java.util.ArrayList;

import BoardStuff.BoardCell;
import javafx.scene.paint.Color;

public class Knight extends Piece{

	public Knight(Color baseColor, BoardCell boardCell) {
		super(baseColor, boardCell);
		this.name = "k";
	}

	@Override
	public void update(double secondsSinceLastFrame) {
		
	}

	@Override
	public void setpossibleMoveBoardCells(BoardCell[][] boardCells,ArrayList<BoardCell> possibleMoveBoardCells) {
	    int row = this.boardCell.getRow(),column = this.boardCell.getColumn();
		for (int i = 0;i<8;i++) {
    		for (int j = 0;j<8;j++) {
				if((((i == row + 1) || (i == row - 1)) && ((j == column+2) || (j == column-2))) ||
				        (((i == row + 2) || (i == row - 2)) && ((j == column+1) || (j == column-1)))) {
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
