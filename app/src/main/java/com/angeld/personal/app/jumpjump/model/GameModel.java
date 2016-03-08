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
    private int remainNum;


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
                remainNum++;
            }
        }
        // Foo
        removePiece(2, 0);
        remainNum--;
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
        if (mSelectedPiece == null) {
            return false;
        }
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
        remainNum--;
        dropPiece(dropPiece);
        print();
        if (isGameOver()) {
            listener.onGameOver(remainNum);
        }
    }

    public void removePiece(int x, int y) {
        mBoard[x][y].status = Piece.EMPTY;
        if (listener != null) {
            listener.onPieceRemoved(mBoard[x][y]);
        }
    }

    public boolean pickPiece(Piece pickCell) {
        if (mBoard[pickCell.x][pickCell.y].status == Piece.EMPTY) {
            return false;
        }
        mBoard[pickCell.x][pickCell.y].status = Piece.EMPTY;
        mSelectedPiece = mBoard[pickCell.x][pickCell.y];
        if (listener != null) {
            listener.onPiecePick(pickCell);
        }
        return true;
    }

    public boolean dropPiece(Piece dropCell) {
        if (mBoard[dropCell.x][dropCell.y].status != Piece.EMPTY) {
            return false;
        }
        mBoard[dropCell.x][dropCell.y].status = Piece.OCCUPIED;
        mSelectedPiece = null;
        if (listener != null) {
            listener.onPieceDrop(dropCell);
        }
        return true;
    }


    // Support methods
    private void print() {
        for (int i = 0; i < mBoard.length; i++) {
            String row = "";
            for (int j = 0; j <= i; j++) {

                if (mBoard[i][j].status == Piece.OCCUPIED) {
                    row += "*";
                } else {
                    row += "x";
                }
            }
            System.out.println(row);
        }
    }

    private boolean isGameOver() {
        boolean isOver = true;
        for (int i = 0; i < mBoard.length; i++) {
            for (int j = 0; j <= i; j++) {
                if (mBoard[i][j].status == Piece.OCCUPIED) {
                    if ((i + 1 < mBoard.length && mBoard[i + 1][j].status == Piece.OCCUPIED)
                            || (j + 1 <= i && mBoard[i][j + 1].status == Piece.OCCUPIED)
                            || (i + 1 < mBoard.length && j + 1 <= i + 1 && mBoard[i + 1][j + 1].status == Piece.OCCUPIED)
                            || (i + 1 < mBoard.length && j - 1 >= 0  && mBoard[i + 1][j - 1].status == Piece.OCCUPIED)) {
                        isOver = false;
                        return isOver;
                    }
                }
            }
        }
        return isOver;
    }
}
