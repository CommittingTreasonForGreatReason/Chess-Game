package PieceStuff;

import java.util.ArrayList;

import BoardStuff.BoardCell;
import ChessGroup.Chess.Player;

public class Pawn extends Piece{
	
	private boolean movedOnce = false;
	private boolean lastMovedOnceStatus = false;

	public Pawn(Player player, BoardCell boardCell) {
		super(player, boardCell);
		this.name = "p";
		this.sortingValue = 5;
	}
	
	public void reverseMovedOnceStatus() {
	    movedOnce = lastMovedOnceStatus;
	}
	
	public boolean isOnPromotionBoardCell() {
	    if(isBlack()) {
	        return boardCell.getRow() == 7;
	    }else {
	        return boardCell.getRow() == 0;
	    }
	}

	@Override
	public void update(double secondsSinceLastFrame) {
		
	}
	
	@Override
	public void movePiece(BoardCell clickedBoardCell) {
	    boardCell.movePieceTo(clickedBoardCell);
	    lastMovedOnceStatus = movedOnce;
        movedOnce = true;
    }

	@Override
	public void setPossibleMoveBoardCells(BoardCell[][] boardCells,ArrayList<BoardCell> possibleMoveBoardCells, boolean simulate) {
	    int[] limits = calculateLimits(boardCells);
	    int row = this.boardCell.getRow(),column = this.boardCell.getColumn();
		for (int i = 0;i<8;i++) {
    		for (int j = 0;j<8;j++) {
    			if(isBlack()) {
    				if((i <= row+(movedOnce?1:2) && i > row) && j == column) {
	    				if(!boardCells[i][j].hasPiece() && i < row+limits[1]) {
	    				    trySetPossibleMove(boardCells, possibleMoveBoardCells, boardCells[i][j], simulate);
	    				}
	    			}
    				if(boardCells[i][j].hasPiece() && !this.isSameColor(boardCells[i][j].getPiece())) {
    				    if((j == column + 1 || j == column - 1) && i == row+1) {
    				        trySetPossibleMove(boardCells, possibleMoveBoardCells, boardCells[i][j], simulate);
    				    }
    				}
    			}else {
					if((i >= row-(movedOnce?1:2) && i < row) && j == column) {
	    				if(!boardCells[i][j].hasPiece() && i > row-limits[0]) {
	    				    trySetPossibleMove(boardCells, possibleMoveBoardCells, boardCells[i][j], simulate);
	    				}
	    			}
					if(boardCells[i][j].hasPiece() && !this.isSameColor(boardCells[i][j].getPiece())) {
					    if((j == column + 1 || j == column - 1) && i == row-1) {
					        trySetPossibleMove(boardCells, possibleMoveBoardCells, boardCells[i][j], simulate);
                        }
                    }
				}
    			
    		}
		}
	}

    @Override
    protected int[] calculateLimits(BoardCell[][] boardCells) {
     // formatted like this [above,below]
        int[] limits = {100,100};
        for (int i = 0;i<8;i++) {
            for (int j = 0;j<8;j++) {
                if(j == boardCell.getColumn() && boardCells[i][j].hasPiece()) {
                    int limitRow = boardCells[i][j].getRowDistance(boardCell);
                    if(boardCell.isAbove(boardCells[i][j])) {
                        limits[0] = newBestLimit(limits[0], limitRow);
                    }else if(boardCell.isBelow(boardCells[i][j])) {
                        limits[1] = newBestLimit(limits[1], limitRow);
                    }
                }
            }
        }
        return limits;
    }
	
}
