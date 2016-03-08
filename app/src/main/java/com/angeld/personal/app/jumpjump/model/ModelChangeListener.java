package com.angeld.personal.app.jumpjump.model;

/**
 * Created by Angel_D on 2/27/16.
 */
public interface ModelChangeListener {
    void onBoardInit();
    void onPiecePick(Piece p);
    void onPieceDrop(Piece p);
    void onPieceRemoved(Piece p);
    void onGameOver(int leftPiece);
}
