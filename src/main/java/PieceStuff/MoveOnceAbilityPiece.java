package PieceStuff;

import java.util.ArrayList;

import BoardStuff.BoardCell;
import ChessGroup.Chess.Player;

public abstract class MoveOnceAbilityPiece extends Piece{
    
    protected boolean movedOnce = false;
    private boolean lastMovedOnceStatus = false;

    public MoveOnceAbilityPiece(Player player, BoardCell boardCell) {
        super(player, boardCell);
    }
    
    public boolean hasMovedOnce() {
        return movedOnce;
    }
    
    public void reverseMovedOnceStatus() {
        movedOnce = lastMovedOnceStatus;
    }
    
    @Override
    public void movePiece(BoardCell clickedBoardCell) {
        boardCell.movePieceTo(clickedBoardCell);
        lastMovedOnceStatus = movedOnce;
        movedOnce = true;
    }

    @Override
    public abstract void setPossibleMoveBoardCells(BoardCell[][] boardCells, ArrayList<BoardCell> possibleMoveBoardCells,boolean simulate);

    @Override
    protected abstract int[] calculateLimits(BoardCell[][] boardCells);

    @Override
    public abstract void update(double secondsSinceLastFrame);

}
