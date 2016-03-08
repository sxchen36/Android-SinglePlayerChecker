package com.angeld.personal.app.jumpjump.model;

/**
 * Created by Angel_D on 2/26/16.
 */
public class GameModel {
    private static final int DIMEN = 5;
    public static GameModel singleton;

    private Piece[][] mBoard;
    private Piece mSelectedPiece;
    private ModelChangeListener listener;


    private GameModel() {
        //initBoard();
    }

    public static GameModel getModel() {
        if (singleton == null) {
            singleton = new GameModel();
        }
        return singleton;
    }

    public void setListener(ModelChangeListener l) {
        listener = l;
    }

    public Piece[][] initBoard(){
        // init
        mBoard = new Piece[DIMEN][DIMEN];
        for (int i = 0 ; i < DIMEN; i++) {
            for (int j = 0; j <= i; j++) {
                mBoard[i][j] = new Piece(i, j);
            }
        }
        // Foo
        removePiece(2, 0);
        return mBoard;
    }


    public Piece getPiece(int x, int y) {
        return mBoard[x][y];
    }

    public int getStatus(int x, int y) {
        if (x >= mBoard.length || y >= mBoard[x].length) {
            // Error
            return -1;
        }
        return mBoard[x][y].status;
    }

    public boolean isValidMove(Piece dropPiece){
        if (dropPiece.status != Piece.EMPTY) {
            return false;
        }
        if (!(mSelectedPiece.x - dropPiece.x == 0 && Math.abs(mSelectedPiece.y - dropPiece.y) == 2) &&
                !(Math.abs(mSelectedPiece.x - dropPiece.x) == 2 && mSelectedPiece.y - dropPiece.y == 0) &&
                !(Math.abs(mSelectedPiece.x - dropPiece.x) == 2 && Math.abs(mSelectedPiece.y - dropPiece.y) == 2)
                ) {
            return false;
        }

        return true;
    }

    public void movePiece(Piece dropPiece) {
        removePiece(mSelectedPiece.x, mSelectedPiece.y);
        if (mSelectedPiece.x == dropPiece.x) {
            int diff = (mSelectedPiece.y > dropPiece.y) ? -1 : 1;
            removePiece(mSelectedPiece.x, mSelectedPiece.y + diff);
        } else if (mSelectedPiece.y == dropPiece.y) {
            int diff = (mSelectedPiece.x > dropPiece.x) ? -1 : 1;
            removePiece(mSelectedPiece.x + diff, mSelectedPiece.y);
        } else {
            int diffx = (mSelectedPiece.x > dropPiece.x) ? -1 : 1;
            int diffy = (mSelectedPiece.y > dropPiece.y) ? -1 : 1;
            removePiece(mSelectedPiece.x + diffx, mSelectedPiece.y + diffy);
        }
        selectPiece(dropPiece);
    }

    public void removePiece(Piece p) {
        p.status = Piece.EMPTY;
        if (listener != null) {
            listener.onPieceRemoved(p);
        }
    }

    public void removePiece(int x, int y) {
        mBoard[x][y].status = Piece.EMPTY;
        if (listener != null) {
            listener.onPieceRemoved(mBoard[x][y]);
        }
    }

    public void selectPiece(int x, int y) {
        mSelectedPiece = mBoard[x][y];
        if (listener != null) {
            listener.onPieceSelect(mSelectedPiece);
        }
    }

    public void selectPiece(Piece selectPiece) {
        mSelectedPiece = selectPiece;
        if (listener != null) {
            listener.onPieceSelect(mSelectedPiece);
        }
    }
}
