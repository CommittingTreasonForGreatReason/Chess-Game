package BoardStuff;

import ChessGroup.Chess.Constants;
import ChessGroup.Chess.GameLogic;
import PieceStuff.King;
import PieceStuff.Piece;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class BoardCell extends DrawableObject{
	private int row,column;
	private double size;
	private Piece piece;
	private static Color selectedColor = Constants.redColor;
	private static Color possibleMoveColor = Constants.redTransparentColor;
	private static Color allPossibleMoveColor = Constants.greenTransparentColor;
	private boolean isHover = false;
	
	public BoardCell(Color baseColor, Point2D centerPoint, int row, int column, double size) {
		super(baseColor, centerPoint);
		this.row = row;
		this.column = column;
		this.size = size;
	}
	
	public void setSize(double size) {
        this.size = size;
    }
	public void setHover(boolean isHover) {
        this.isHover = isHover;
    }
	
	public int getRowDistance(BoardCell boardCell) {
        return Math.abs(boardCell.getRow()-this.getRow());
    }
	public int getColumnDistance(BoardCell boardCell) {
        return Math.abs(boardCell.getColumn()-this.getColumn());
    }
	
	public boolean isAbove(BoardCell boardCell) {
	    return boardCell.getRow() < this.getRow();
	}
	
    public boolean isBelow(BoardCell boardCell) {
        return boardCell.getRow() > this.getRow();    
    }
    
    public boolean isRight(BoardCell boardCell) {
        return boardCell.getColumn() > this.getColumn();
    }
    
    public boolean isLeft(BoardCell boardCell) {
        return boardCell.getColumn() < this.getColumn();
    }
    
    public boolean isCastleKingBoardCell(King king) {
        int r = king.isBlack()?0:7;
        return row == r && (column == 2 || column == 6);
    }
	
	public void setPiece(Piece piece) {
		this.piece = piece;
	}
	
	public Piece getPiece() {
		return piece;
	}
	
	public void movePieceTo(BoardCell boardCell) {
	    Piece thisPiece = this.getPiece();
	    thisPiece.setBoardCell(boardCell);
	    thisPiece.repositionGeometryOnResize();
	    
	    if(boardCell.hasPiece()) {
	        GameLogic.removePiece(boardCell.getPiece());
	    }
	    this.setPiece(null);
	    boardCell.setPiece(thisPiece);
	}
	
	public int getRow() {
		return row;
	}
	public int getColumn() {
		return column;
	}
	
	public boolean hasPiece() {
		return piece != null;
	}
	
	public boolean contains(Point2D mousePoint) {
		Rectangle rect = new Rectangle(centerPoint.getX()-size/2, centerPoint.getY()-size/2, size, size);
		return rect.contains(mousePoint);
	}

	@Override
	public void update(double secondsSinceLastFrame) {
			
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(baseColor);
		gc.fillRect(centerPoint.getX()-size/2, centerPoint.getY()-size/2, size, size);
		
        if(isHover) {
            gc.setFill(Constants.blueTransparentColor);
            gc.fillRect(centerPoint.getX()-size/2, centerPoint.getY()-size/2, size, size);
        }
        
        gc.setFill(Color.RED);
        gc.setFont(new Font("Arial", 15));
        gc.fillText(row+"/"+column, centerPoint.getX()+size/2-gc.getFont().getSize()*2, centerPoint.getY()+size/2-gc.getFont().getSize());
	}
	
	public void drawSelected(GraphicsContext gc) {
		gc.setStroke(selectedColor);
		gc.setLineWidth(5);
		gc.strokeRect(centerPoint.getX()-size/2, centerPoint.getY()-size/2, size, size);
	}
	
	public void drawPossibleMove(GraphicsContext gc) {
		gc.setFill(possibleMoveColor);
		gc.fillRect(centerPoint.getX()-size/2, centerPoint.getY()-size/2, size, size);
	}
	public void drawAllPossibleMove(GraphicsContext gc) {
        gc.setFill(allPossibleMoveColor);
        gc.fillRect(centerPoint.getX()-size/2, centerPoint.getY()-size/2, size, size);
    }

	@Override
	public void repositionGeometryOnResize() {
		if(piece != null) {
			piece.repositionGeometryOnResize();
		}
	}
	
	public String toString() {
	    return "BoardCell at:\n(Row/Column) = (" + row + "/" + column + ")";
	}
	
}
