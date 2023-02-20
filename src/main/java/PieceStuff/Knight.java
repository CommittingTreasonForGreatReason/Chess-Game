package PieceStuff;

import java.util.ArrayList;

import BoardStuff.BoardCell;
import ChessGroup.Chess.Player;

public class Knight extends Piece{

	public Knight(Player player, BoardCell boardCell) {
		super(player, boardCell);
		this.name = "k";
		this.sortingValue = 2;
	}

	@Override
	public void update(double secondsSinceLastFrame) {
		
	}

	@Override
	public void setPossibleMoveBoardCells(BoardCell[][] boardCells,ArrayList<BoardCell> possibleMoveBoardCells, boolean simulate) {
	    int row = this.boardCell.getRow(),column = this.boardCell.getColumn();
		for (int i = 0;i<8;i++) {
    		for (int j = 0;j<8;j++) {
				if((((i == row + 1) || (i == row - 1)) && ((j == column+2) || (j == column-2))) ||
				        (((i == row + 2) || (i == row - 2)) && ((j == column+1) || (j == column-1)))) {
	    			if(!boardCells[i][j].hasPiece() || boardCells[i][j].hasPiece() && !this.isSameColor(boardCells[i][j].getPiece())) {
	    			    trySetPossibleMove(boardCells, possibleMoveBoardCells, boardCells[i][j], simulate);
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
