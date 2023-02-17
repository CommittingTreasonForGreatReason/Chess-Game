package PieceStuff;

import java.util.ArrayList;

import BoardStuff.BoardCell;
import ChessGroup.Chess.Player;

public class Rook extends Piece{

	public Rook(Player player, BoardCell boardCell) {
		super(player, boardCell);
		this.name = "r";
	}

	@Override
	public void update(double secondsSinceLastFrame) {
		
	}

	@Override
	public void setPossibleMoveBoardCells(BoardCell[][] boardCells,ArrayList<BoardCell> possibleMoveBoardCells, boolean ignoreKing) {
	    int[] limits = calculateLimits(boardCells,ignoreKing);
	    int row = this.boardCell.getRow(),column = this.boardCell.getColumn();
		for (int i = 0;i<8;i++) {
            for (int j = 0;j<8;j++) {
                if(!boardCells[i][j].hasPiece()|| boardCells[i][j].hasPiece() && (!this.isSameColor(boardCells[i][j].getPiece()) || ignoreKing)) {
                    if((i <= row+limits[1] && i >= row-limits[0] && j == column)) {
                        addPossibleMoveBoardCell(possibleMoveBoardCells, boardCells[i][j]);
                    }else if((j <= column+limits[2] && j >= column-limits[3] && i == row)) {
                        addPossibleMoveBoardCell(possibleMoveBoardCells, boardCells[i][j]);
                    }
                }
            }
        }
	}

    @Override
    protected int[] calculateLimits(BoardCell[][] boardCells, boolean ignoreKing) {
        // formatted like this [above,below,right,left]
        int[] limits = {100,100,100,100};
        for (int i = 0;i<8;i++) {
            for (int j = 0;j<8;j++) {
                if(boardCells[i][j] == boardCell || !boardCells[i][j].hasPiece()) {
                    continue;
                }
                if(ignoreKing && boardCells[i][j].hasPiece() && boardCells[i][j].getPiece() instanceof King) {
                    continue; 
                }
                if((i == boardCell.getRow() || j == boardCell.getColumn())) {
                    int limitRow = boardCells[i][j].getRowDistance(boardCell);
                    int limitColumn = boardCells[i][j].getColumnDistance(boardCell);
                    if(boardCell.isAbove(boardCells[i][j])) {
                        limits[0] = newBestLimit(limits[0], limitRow);
                    }else if(boardCell.isBelow(boardCells[i][j])) {
                        limits[1] = newBestLimit(limits[1], limitRow);
                    } else if(boardCell.isRight(boardCells[i][j])) {
                        limits[2] = newBestLimit(limits[2], limitColumn);
                    } else if(boardCell.isLeft(boardCells[i][j])){
                        limits[3] = newBestLimit(limits[3], limitColumn);
                    }
                }
            }
        }
        return limits;
    }

	
}
