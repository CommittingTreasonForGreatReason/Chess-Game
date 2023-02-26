package BoardStuff;

import java.util.ArrayList;

import ChessGroup.Chess.Constants;
import ChessGroup.Chess.GameLogic;
import ChessGroup.Chess.GameOverlay;
import ChessGroup.Chess.Player;
import PieceStuff.Pawn;
import PieceStuff.Piece;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PawnPromotion extends DrawableObject{
    public PawnPromotion(Color baseColor, Point2D centerPoint) {
        super(baseColor, centerPoint);
        size = Board.getBoardInstance().getSize()*1.1;
    }
    
    public static void initPawnPromotion() {
        initPromotionBoard();
        promotionWhitePieces.clear();
        promotionBlackPieces.clear();
        promotingPlayer=null;
        promotionBoardCell=null;
        System.out.println("initalized PawnPromotion:");
    }
    
    private static double size;
    private static ArrayList<Piece> promotionWhitePieces = new ArrayList<Piece>();
    private static ArrayList<Piece> promotionBlackPieces = new ArrayList<Piece>();
    private static Player promotingPlayer = null;
    private static BoardCell promotionBoardCell = null;
    
    private static BoardCell[][] promotionBlackBoardCells = new BoardCell[4][4];
    private static BoardCell[][] promotionWhiteBoardCells = new BoardCell[4][4];
    
    public static void initPromotionBoard() {
        for (int i = 0;i<4;i++) {
            for (int j = 0;j<4;j++) {
                Color boardCellColor = Constants.gameOverlayBackGroundColor;
                Point2D screenCenterPoint2D = GameOverlay.getScreenCenterPoint();
                Point2D boardCellCenterPoint = new Point2D(screenCenterPoint2D.getX()+size/8+size/4*j+screenCenterPoint2D.getX()-size/2,screenCenterPoint2D.getY()+size/8+size/4*i+screenCenterPoint2D.getY()-size/2);
                promotionBlackBoardCells[i][j] = new BoardCell(boardCellColor,boardCellCenterPoint,i,j,size/4);
                promotionWhiteBoardCells[i][j] = new BoardCell(boardCellColor,boardCellCenterPoint,i,j,size/4);
            }
        }
    }
    
    private static void setPiecesOnPromotionBoard(ArrayList<Piece> pieces, BoardCell[][] promotionBoardCells) {
        int k = 0;
        for (int i = 0;i<4;i++) {
            for (int j = 0;j<4;j++) {
                if(k < pieces.size()) {
                    promotionBoardCells[i][j].setPiece(pieces.get(k));
                    pieces.get(k).setBoardCell(promotionBoardCells[i][j]);
                }else {
                    promotionBoardCells[i][j].setPiece(null); 
                }
                k++;
            }
        }
    }
    
    public static void setPromotingPlayer(Player promotingPlayer) {
        PawnPromotion.promotingPlayer = promotingPlayer;
        sortPromotionPieces(getCurrentPromotionPieces());
    }
    public static void setPromotionBoardCell(BoardCell promotionBoardCell) {
        PawnPromotion.promotionBoardCell = promotionBoardCell;
    }
    
    public static Player getPromotingPlayer() {
        return promotingPlayer;
    }
    public static ArrayList<Piece> getCurrentPromotionPieces() {
        return promotingPlayer.isBlack()?promotionBlackPieces:promotionWhitePieces;
    }
    
    public static boolean isPawnPromotion() {
        return promotingPlayer != null;
    }
    
    public static boolean hasPromotiblePieces(Piece piece) {
        ArrayList<Piece> promotionPieces = piece.isBlack()?promotionBlackPieces:promotionWhitePieces;
        for(int i = 0;i<(promotionPieces.size());i++) {
            if(!(promotionPieces.get(i) instanceof Pawn)) {
                return true;
            }
        }
        return false;
    }
    
    public static void addPromotionPieces(Piece piece) {
        if(piece.isBlack()) {
            promotionBlackPieces.add(piece);
        }else {
            promotionWhitePieces.add(piece);
        }
    }
    
    public static void removePromotionPieces(Piece piece) {
        if(piece.isBlack()) {
            promotionBlackPieces.remove(piece);
        }else {
            promotionWhitePieces.remove(piece);
        }
    }
    
    private static void swapPieces(int index1, int index2, ArrayList<Piece> pieces) {
        Piece temp = pieces.get(index1);
        pieces.set(index1, pieces.get(index2));
        pieces.set(index2, temp);
    }
    
    public static void sortPromotionPieces(ArrayList<Piece> promotionPieces) {
        if(promotionPieces.size() == 0) {
            return;
        }
        for(int k = 0; k<promotionPieces.size();k++) {
            for(int i = 0; i<promotionPieces.size()-1;i++) {
                if(promotionPieces.get(i).getSortingValue() > promotionPieces.get(i+1).getSortingValue()) {
                    swapPieces(i, i+1, promotionPieces); 
                }
            }
        }
        BoardCell[][] promotionBoardCells = promotionPieces.get(0).isBlack()?promotionBlackBoardCells:promotionWhiteBoardCells;
        setPiecesOnPromotionBoard(promotionPieces,promotionBoardCells);
    }
    
    public void updateHover(Point2D mousePoint) {
        for (int i = 0;i<4;i++) {
            for (int j = 0;j<4;j++) {
                promotionBlackBoardCells[i][j].setHover(promotionBlackBoardCells[i][j].contains(mousePoint));
                promotionWhiteBoardCells[i][j].setHover(promotionWhiteBoardCells[i][j].contains(mousePoint));
            }
        }
    }
    
    public boolean trySelectPromotionPiece(Point2D mousePoint) {
        BoardCell[][] promotionBoardCells = promotingPlayer.isBlack()?promotionBlackBoardCells:promotionWhiteBoardCells;
        for (int i = 0;i<4;i++) {
            for (int j = 0;j<4;j++) {
                if(promotionBoardCells[i][j].contains(mousePoint) && promotionBoardCells[i][j].hasPiece() && !(promotionBoardCells[i][j].getPiece() instanceof Pawn)) {
                        GameLogic.removePiece(promotionBoardCell.getPiece());
                    
                        Piece selectedPiece = promotionBoardCells[i][j].getPiece();
                        selectedPiece.setBoardCell(promotionBoardCell);
                        
                        GameLogic.addPiece(selectedPiece);
                        selectedPiece.repositionGeometryOnResize();
                        
                        promotionBoardCell.setPiece(selectedPiece);
                        promotionBoardCell = null;
                        promotingPlayer = null;
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public void update(double secondsSinceLastFrame) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void draw(GraphicsContext gc) {
        BoardCell[][] promotionBoardCells = promotingPlayer.isBlack()?promotionBlackBoardCells:promotionWhiteBoardCells;
        for (int i = 0;i<4;i++) {
            for (int j = 0;j<4;j++) {
                promotionBoardCells[i][j].draw(gc);
            }
        }
        ArrayList<Piece> promotionPieces = getCurrentPromotionPieces();
        
        for(Piece piece : promotionPieces) {
            if(!(piece instanceof Pawn)) {
                piece.draw(gc);
            }
        }
    }

    @Override
    public void repositionGeometryOnResize() {
        size = Board.getBoardInstance().getSize()*1.1;
        centerPoint = GameOverlay.getScreenCenterPoint();
        for (int i = 0;i<4;i++) {
            for (int j = 0;j<4;j++) {
                Point2D boardCellCenterPoint = new Point2D(size/8+size/4*j+centerPoint.getX()-size/2,size/8+size/4*i+centerPoint.getY()-size/2);
                promotionBlackBoardCells[i][j].setCenterPoint(boardCellCenterPoint);
                promotionBlackBoardCells[i][j].setSize(size/4);
                promotionBlackBoardCells[i][j].repositionGeometryOnResize();
                promotionWhiteBoardCells[i][j].setCenterPoint(boardCellCenterPoint);
                promotionWhiteBoardCells[i][j].setSize(size/4);
                promotionWhiteBoardCells[i][j].repositionGeometryOnResize();
            }
        }    
    }
}
