package com.angeld.personal.app.jumpjump;

import android.content.ClipData;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.angeld.personal.app.jumpjump.model.GameModel;
import com.angeld.personal.app.jumpjump.model.ModelChangeListener;
import com.angeld.personal.app.jumpjump.model.Piece;

public class MainActivity extends AppCompatActivity implements ModelChangeListener{
    RelativeLayout mParentLayout;
    GameModel mModel;
    private View mSelectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mParentLayout = (RelativeLayout)findViewById(R.id.parentLayout);
        mModel = GameModel.getModel();
        mModel.setListener(this);
        mModel.initBoard();
        mParentLayout.setOnDragListener(new MyDragListener());
        for(int i=0; i<mParentLayout.getChildCount(); ++i) {
            View child = mParentLayout.getChildAt(i);
            if (child instanceof ImageButton) {
                child.setOnTouchListener(new MyTouchListener());
                child.setOnDragListener(new MyDragListener());
            }
        }
    }

    private Piece getPieceFromTag(String tag) {
        String[] loc = tag.split("_");
        return mModel.getPiece(Integer.parseInt(loc[0]), Integer.parseInt(loc[1]));
    }

    private String getTagFromPiece(Piece p) {
        return p.toString();
    }

    // This defines your touch listener
    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                String viewTag = view.getTag().toString();
                mSelectedImage = view;
                boolean pick = mModel.pickPiece(getPieceFromTag(viewTag));
                if (!pick) return false;
                ClipData data = ClipData.newPlainText(viewTag, viewTag); //TODO: why not "",""??
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setBackgroundColor(Color.WHITE);
                return true;
            } else {
                return false;
            }
        }
    }

    class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
//                    Log.v("Test", "Entered start");
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
//                    Log.v("Test", "Entered drag");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
//                    Log.v("Test", "Entered drop");
                    if (v.getTag() != null) {
                        String viewTag = v.getTag().toString();
                        Piece dropPiece = getPieceFromTag(viewTag);
                        if (mModel.isValidMove(dropPiece)) {
                            mModel.movePiece(dropPiece);
                        }
                    } else {
                        String viewTag = mSelectedImage.getTag().toString();
                        Piece dropPiece = getPieceFromTag(viewTag);
                        mModel.dropPiece(dropPiece);
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
//                    Log.v("Test", "Ended drop");
                default:
                    break;
            }
            return true;
        }
    }

    @Override
    public void onBoardInit() {

    }

    @Override
    public void onPiecePick(Piece p) {
        String tag = getTagFromPiece(p);
        View v = mParentLayout.findViewWithTag(tag);
        v.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void onPieceDrop(Piece p) {
        String tag = getTagFromPiece(p);
        View v = mParentLayout.findViewWithTag(tag);
        v.setBackgroundColor(Color.BLACK);
    }


    @Override
    public void onGameOver(int leftPiece) {
//        Log.v("lalalal", "Game over " + leftPiece);
    }

    @Override
    public void onPieceRemoved(Piece p) {
          String tag = getTagFromPiece(p);
        View v = mParentLayout.findViewWithTag(tag);
        v.setBackgroundColor(Color.WHITE);
    }
}
