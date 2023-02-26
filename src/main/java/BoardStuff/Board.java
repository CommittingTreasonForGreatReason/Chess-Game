package BoardStuff;

import java.util.ArrayList;

import ChessGroup.Chess.Constants;
import ChessGroup.Chess.GUIController;
import ChessGroup.Chess.GameLogic;
import PieceStuff.Piece;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Board extends DrawableObject{
	private double size;
	private BoardCell[][] boardCells = new BoardCell[8][8];
	// singleton instance
    private static Board board;
	
	private Board(final int size, final Point2D centerPoint, final Color baseColor) {
		super(baseColor, centerPoint);
		this.size = size;
	}
	
	public double getSize() {
        return size;
    }
	
	public BoardCell[][] getBoardCells() {
		return boardCells;
	}
	
	public BoardCell getBoardCell(int row, int column){
		return boardCells[row][column];
	}
	
	public BoardCell getClickedBoardCell(Point2D mousePoint) {
		for (int i = 0;i<8;i++) {
    		for (int j = 0;j<8;j++) {
    			if(boardCells[i][j].contains(mousePoint)) {
    				return boardCells[i][j];
    			}
    		}
		}
		return null;
	}
	// singleton getter
    public static Board getBoardInstance() {
        if (board == null) {
            board = new Board((int)(GUIController.smallestSize()*0.8),
            		new Point2D(GUIController.getCanvasWidth()/2,GUIController.getCanvasHeight()/2), Color.BLACK);
        }
        return board;
    }
    
    public void initBoard() {
    	for (int i = 0;i<8;i++) {
    		for (int j = 0;j<8;j++) {
    			Color boardCellColor;
    			if((i%2==0)) {
    				boardCellColor = (j%2==0)?Constants.whiteColor:Constants.blackColor;
    			}else {
    				boardCellColor = (j%2==1)?Constants.whiteColor:Constants.blackColor;
    			}
    			
    			Point2D boardCellCenterPoint = new Point2D(size/16+size/8*j+centerPoint.getX()-size/2,size/16+size/8*i+centerPoint.getY()-size/2);
    			boardCells[i][j] = new BoardCell(boardCellColor,boardCellCenterPoint,i,j,size/8);
    		}
		}
    	System.out.println("initialized Board:");
    }
    
    public void updateHover(Point2D mousePoint) {
        for (int i = 0;i<8;i++) {
            for (int j = 0;j<8;j++) {
                boardCells[i][j].setHover(boardCells[i][j].contains(mousePoint));
            }
        }
    }
    
	@Override
	public void update(double secondsSinceLastFrame) {
		
	}
	@Override
	public void draw(GraphicsContext gc) {
		gc.setLineWidth(20);
		gc.setStroke(baseColor);
		gc.strokeRect(centerPoint.getX()-size/2, centerPoint.getY()-size/2, size, size);
		for (int i = 0;i<8;i++) {
    		for (int j = 0;j<8;j++) {
    			boardCells[i][j].draw(gc);
    		}
		}
		BoardCell selectedBoardCell = GameLogic.getSelectedBoardCell();
		if(selectedBoardCell!=null) {
			selectedBoardCell.drawSelected(gc);
		}
		ArrayList<BoardCell> possibleMoveBoardCells = GameLogic.getPossibleMoveBoardCells();
		for(BoardCell possibleMoveBoardCell : possibleMoveBoardCells) {
			possibleMoveBoardCell.drawPossibleMove(gc);
		}
//		ArrayList<BoardCell> allPossibleMoveBoardCells = GameLogic.getAllPossibleMoveBoardCells();
//        for(BoardCell allPossibleMoveBoardCell : allPossibleMoveBoardCells) {
//            allPossibleMoveBoardCell.drawAllPossibleMove(gc);
//        }
        ArrayList<Piece> blackPieces = GameLogic.getBlackPieces();
        for(Piece piece : blackPieces) {
            piece.draw(gc);
        }
        ArrayList<Piece> whitePieces = GameLogic.getWhitePieces();
        for(Piece piece : whitePieces) {
            piece.draw(gc);
        }
	}
	
	@Override
	public void repositionGeometryOnResize() {
		centerPoint = new Point2D(GUIController.getCanvasWidth()/2,GUIController.getCanvasHeight()/2);
		size = (int)(GUIController.smallestSize()*0.8);
		for (int i = 0;i<8;i++) {
    		for (int j = 0;j<8;j++) {
    			Point2D boardCellCenterPoint = new Point2D(size/16+size/8*j+centerPoint.getX()-size/2,size/16+size/8*i+centerPoint.getY()-size/2);
    			boardCells[i][j].setCenterPoint(boardCellCenterPoint);
    			boardCells[i][j].setSize(size/8);
    			boardCells[i][j].repositionGeometryOnResize();
    		}
		}
	}
}
