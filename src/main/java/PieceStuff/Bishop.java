package PieceStuff;

import java.util.ArrayList;

import BoardStuff.BoardCell;
import ChessGroup.Chess.Player;

public class Bishop extends Piece{

	public Bishop(Player player, BoardCell boardCell) {
		super(player, boardCell);
		this.name = "b";
		this.sortingValue = 3;
	}

	@Override
	public void update(double secondsSinceLastFrame) {
		
	}

	@Override
	public void setPossibleMoveBoardCells(BoardCell[][] boardCells,ArrayList<BoardCell> possibleMoveBoardCells, boolean simulate) {
	    int[] limits = calculateLimits(boardCells);
	    int row = this.boardCell.getRow(),column = this.boardCell.getColumn();
		for (int i = 0;i<8;i++) {
            for (int j = 0;j<8;j++) {
                if(!boardCells[i][j].hasPiece() || boardCells[i][j].hasPiece() && !this.isSameColor(boardCells[i][j].getPiece())) {
                    for(int k = -8;k<8;k++) {
                        if((row+k == i || row-k == i) && (column+k == j || column-k == j)) {
                            if(boardCell.isAbove(boardCells[i][j])) {
                                if(boardCell.isRight(boardCells[i][j])) {
                                    if(i >= row-limits[0] && j <= column+limits[1]) {
                                        trySetPossibleMove(boardCells, possibleMoveBoardCells, boardCells[i][j], simulate);
                                    }  
                                }else {
                                    if(i >= row-limits[2] && j >= column-limits[3]) {
                                        trySetPossibleMove(boardCells, possibleMoveBoardCells, boardCells[i][j], simulate);  
                                    }
                                }
                            }else {
                                if(boardCell.isRight(boardCells[i][j])) {
                                    if(i <= row+limits[4] && j <= column+limits[5]) {
                                        trySetPossibleMove(boardCells, possibleMoveBoardCells, boardCells[i][j], simulate);
                                    } 
                                }else {
                                    if(i <= row+limits[6] && j >= column-limits[7]) {
                                        trySetPossibleMove(boardCells, possibleMoveBoardCells, boardCells[i][j], simulate);
                                    } 
                                }
                            }
                        }
                    }
                }
            }
        }
	}

    @Override
    protected int[] calculateLimits(BoardCell[][] boardCells) {
        // formatted like this [aboveRightRow,aboveRightColumn,aboveLeftRow,aboveLeftColumn,belowRightRow,belowRightColumn,belowLeftRow,belowLeftColumn]
        int[] limits = {100,100,100,100,100,100,100,100};
        int row = this.boardCell.getRow(),column = this.boardCell.getColumn();
        for (int i = 0;i<8;i++) {
            for (int j = 0;j<8;j++) {
                int limitRow = boardCells[i][j].getRowDistance(boardCell);
                int limitColumn = boardCells[i][j].getColumnDistance(boardCell);
                for(int k = -8;k<8;k++) {
                    if(boardCells[i][j] == boardCell || !boardCells[i][j].hasPiece()) {
                        continue;
                    }
                    if((row+k == i || row-k == i) && (column+k == j || column-k == j)) {
                        if(boardCell.isAbove(boardCells[i][j])) {
                            if(boardCell.isRight(boardCells[i][j])) {
                                limits[0] = newBestLimit(limits[0], limitRow);
                                limits[1] = newBestLimit(limits[1], limitColumn);
                            }else {
                                limits[2] = newBestLimit(limits[2], limitRow);
                                limits[3] = newBestLimit(limits[3], limitColumn);
                            }
                        }else if(boardCell.isBelow(boardCells[i][j])) {
                            if(boardCell.isRight(boardCells[i][j])) {
                                limits[4] = newBestLimit(limits[4], limitRow);
                                limits[5] = newBestLimit(limits[5], limitColumn);
                            }else {
                                limits[6] = newBestLimit(limits[6], limitRow);
                                limits[7] = newBestLimit(limits[7], limitColumn);
                            }
                        }
                    }
                }
            }
        }
        return limits;
    }

	
}
