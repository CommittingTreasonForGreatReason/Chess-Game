package PieceStuff;

import java.util.ArrayList;

import BoardStuff.BoardCell;
import ChessGroup.Chess.Player;

public class King extends MoveOnceAbilityPiece{

	public King(Player player, BoardCell boardCell) {
		super(player, boardCell);
		this.name = "K";
	}

	@Override
	public void update(double secondsSinceLastFrame) {
		
	}

	@Override
	public void setPossibleMoveBoardCells(BoardCell[][] boardCells,ArrayList<BoardCell> possibleMoveBoardCells, boolean simulate) {
	    int row = this.boardCell.getRow(),column = this.boardCell.getColumn();
		for (int i = 0;i<8;i++) {
    		for (int j = 0;j<8;j++) {
				if(i >= row-1 && i <= row+1 && j >= column-1 && j <= column+1) {
	    			if(!boardCells[i][j].hasPiece() || boardCells[i][j].hasPiece() && !this.isSameColor(boardCells[i][j].getPiece())) {
	    			    trySetPossibleMove(boardCells, possibleMoveBoardCells, boardCells[i][j], simulate);
	    			}
	    			continue;
	    		}
				// castle
				if(movedOnce) {
				    continue;
				}
				// short castle
				if(i == row && j == column + 2 && !boardCells[i][j].hasPiece()) {
				    if(RookAbleToCastle(row, true, boardCells) && !boardCells[i][j-1].hasPiece()) {
				        trySetPossibleMove(boardCells, possibleMoveBoardCells, boardCells[i][j], simulate);
				    }
				    continue;
				}
				// long castle
				if(i == row && j == column - 2 && !boardCells[i][j].hasPiece()) {
                    if(RookAbleToCastle(row, false, boardCells) && !boardCells[i][j+1].hasPiece()) {
                        trySetPossibleMove(boardCells, possibleMoveBoardCells, boardCells[i][j], simulate);
                    }
                    continue;
                }
    		}
		}
	}
	
	private boolean RookAbleToCastle(int row, boolean isShortCastle,BoardCell[][] boardCells) {
	    int column = (isShortCastle?7:0);
	    return boardCells[row][column].hasPiece() && boardCells[row][column].getPiece() instanceof Rook && !((Rook)boardCells[row][column].getPiece()).movedOnce;
	}

    @Override
    protected int[] calculateLimits(BoardCell[][] boardCells) {
        return null;
    }
	
}
