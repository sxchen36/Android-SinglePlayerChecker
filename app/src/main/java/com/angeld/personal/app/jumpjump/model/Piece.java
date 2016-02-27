package com.angeld.personal.app.jumpjump.model;

/**
 * Created by Angel_D on 2/26/16.
 */
public class Piece {
    public final static int OCCUPIED = 1;
    public final static int EMPTY = 0;
    int x, y;
    int status;

    public Piece(int x, int y) {
        this.x = x;
        this.y = y;
        this.status = OCCUPIED;
    }

    @Override
    public String toString() {
        return x +"_" + y;
    }
}
