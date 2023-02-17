package PieceStuff;

import java.util.ArrayList;

import BoardStuff.BoardCell;
import ChessGroup.Chess.GameLogic;
import ChessGroup.Chess.Player;

public class King extends Piece{

	public King(Player player, BoardCell boardCell) {
		super(player, boardCell);
		this.name = "K";
	}

	@Override
	public void update(double secondsSinceLastFrame) {
		
	}

	@Override
	public void setPossibleMoveBoardCells(BoardCell[][] boardCells,ArrayList<BoardCell> possibleMoveBoardCells, boolean ignoreKing) {
	    ArrayList<BoardCell> possibleMoveBoardCellsOtherTeam = new ArrayList<BoardCell>();
	    for(Piece piece: GameLogic.getTeamPieces(!this.isBlack())) {
	        if(!(piece instanceof King)) {
	            piece.setPossibleMoveBoardCells(boardCells, possibleMoveBoardCellsOtherTeam,true);
	        }
	    }
		for (int i = 0;i<8;i++) {
    		for (int j = 0;j<8;j++) {
				if(i >= boardCell.getRow()-1 && i <= boardCell.getRow()+1 && j >= boardCell.getColumn()-1 && j <= boardCell.getColumn()+1) {
	    			if(!boardCells[i][j].hasPiece() || boardCells[i][j].hasPiece() && !this.isSameColor(boardCells[i][j].getPiece())) {
	    			    if(!possibleMoveBoardCellsOtherTeam.contains(boardCells[i][j])) {
	    			        GameLogic.lastMovePiece = (Piece) this;
	    		            GameLogic.lastMoveBoardCell = this.getBoardCell();
	    		            GameLogic.lastRemovedPiece = boardCells[i][j].getPiece();
	    		            movePiece(boardCells[i][j]);
	    			        if(!GameLogic.getGameLogicInstance().isCheck(player,true)) {
	    			            addPossibleMoveBoardCell(possibleMoveBoardCells, boardCells[i][j]);
	    			        }
	    			        GameLogic.getGameLogicInstance().reverseLastMove();
	    			    }
	    			}
	    		}
    		}
		}
	}

    @Override
    protected int[] calculateLimits(BoardCell[][] boardCells,boolean ignoreKing) {
        return null;
    }
	
}
