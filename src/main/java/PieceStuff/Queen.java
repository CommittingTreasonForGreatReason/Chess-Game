package PieceStuff;

import java.util.ArrayList;

import BoardStuff.BoardCell;
import ChessGroup.Chess.Player;

public class Queen extends Piece{

	public Queen(Player player, BoardCell boardCell) {
		super(player, boardCell);
		this.name = "Q";
	}

	@Override
	public void update(double secondsSinceLastFrame) {
		
	}

	@Override
	public void setPossibleMoveBoardCells(BoardCell[][] boardCells,ArrayList<BoardCell> possibleMoveBoardCells, boolean simulate) {
	    int[] limitsDiagonal = calculateLimits(boardCells);
	    int[] limitsLinear = calculateLimits(boardCells);
	    int row = this.boardCell.getRow(),column = this.boardCell.getColumn();
		for (int i = 0;i<8;i++) {
            for (int j = 0;j<8;j++) {
                if(!boardCells[i][j].hasPiece() || boardCells[i][j].hasPiece()  && !this.isSameColor(boardCells[i][j].getPiece())) {
                    // add diagonal possible moves
                    for(int k = -8;k<8;k++) {
                        if((row+k == i || row-k == i) && (column+k == j || column-k == j)) {
                            if(boardCell.isAbove(boardCells[i][j])) {
                                if(boardCell.isRight(boardCells[i][j])) {
                                    if(i >= row-limitsDiagonal[0] && j <= column+limitsDiagonal[1]) {
                                        trySetPossibleMove(boardCells, possibleMoveBoardCells, boardCells[i][j], simulate);
                                    }  
                                }else {
                                    if(i >= row-limitsDiagonal[2] && j >= column-limitsDiagonal[3]) {
                                        trySetPossibleMove(boardCells, possibleMoveBoardCells, boardCells[i][j], simulate);
                                    }
                                }
                            }else {
                                if(boardCell.isRight(boardCells[i][j])) {
                                    if(i <= row+limitsDiagonal[4] && j <= column+limitsDiagonal[5]) {
                                        trySetPossibleMove(boardCells, possibleMoveBoardCells, boardCells[i][j], simulate);
                                    } 
                                }else {
                                    if(i <= row+limitsDiagonal[6] && j >= column-limitsDiagonal[7]) {
                                        trySetPossibleMove(boardCells, possibleMoveBoardCells, boardCells[i][j], simulate);
                                    } 
                                }
                            }
                        }
                    }
                    // add linear possible moves
                    if((i <= row+limitsLinear[9] && i >= row-limitsLinear[8] && j == column)) {
                        trySetPossibleMove(boardCells, possibleMoveBoardCells, boardCells[i][j], simulate);
                    }else if((j <= column+limitsLinear[10] && j >= column-limitsLinear[11] && i == row)) {
                        trySetPossibleMove(boardCells, possibleMoveBoardCells, boardCells[i][j], simulate);
                    }
                }
            }
        }
	}

    @Override
    protected int[] calculateLimits(BoardCell[][] boardCells) {
        // formatted like this [aboveRightRow,aboveRightColumn,aboveLeftRow,aboveLeftColumn,belowRightRow,belowRightColumn,belowLeftRow,belowLeftColumn,
        // above,below,right,left]
        int[] limits = {100,100,100,100,100,100,100,100,100,100,100,100};
        int row = this.boardCell.getRow(),column = this.boardCell.getColumn();
        for (int i = 0;i<8;i++) {
            for (int j = 0;j<8;j++) {
                int limitRow = boardCells[i][j].getRowDistance(boardCell);
                int limitColumn = boardCells[i][j].getColumnDistance(boardCell);
                if(boardCells[i][j] == boardCell || !boardCells[i][j].hasPiece()) {
                    continue;
                }
                // calculate diagonal limits
                for(int k = -8;k<8;k++) {
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
                // calculate linear limits
                if((i == boardCell.getRow() || j == boardCell.getColumn()) && boardCells[i][j] != boardCell && boardCells[i][j].hasPiece()) {
                    if(boardCell.isAbove(boardCells[i][j])) {
                        limits[8] = newBestLimit(limits[8], limitRow);
                    }else if(boardCell.isBelow(boardCells[i][j])) {
                        limits[9] = newBestLimit(limits[9], limitRow);
                    } else if(boardCell.isRight(boardCells[i][j])) {
                        limits[10] = newBestLimit(limits[10], limitColumn);
                    } else if(boardCell.isLeft(boardCells[i][j])){
                        limits[11] = newBestLimit(limits[11], limitColumn);
                    }
                }
            }
        }
        return limits;
    }

	
}
