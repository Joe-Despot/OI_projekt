package com.example.oi_projekt.listeners;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oi_projekt.activities.GameChooserActivity;
import com.example.oi_projekt.activities.ShapesGameActivity;

public class DragTouchListener implements View.OnTouchListener {

    private float dX, dY;
    private View parentView;
    View innerView;
    View outerView;
    String shapeName;
    TextView outerView1;
    TextView outerView2;
    TextView outerView3;
    TextView outerView4;
    static boolean  outerViewFilled1 = false;
    static boolean outerViewFilled2 = false;
    static boolean outerViewFilled3 = false;
    static boolean outerViewFilled4 = false;
    ShapesGameActivity sga;

    public DragTouchListener(View parentView, View innerView, String shapeName, TextView outerView1, TextView outerView2, TextView outerView3, TextView outerView4, ShapesGameActivity sga) {
        this.parentView = parentView;
        this.innerView = innerView;
        this.outerView1 = outerView1;
        this.outerView2 = outerView2;
        this.outerView3 = outerView3;
        this.outerView4 = outerView4;
        this.shapeName = shapeName;
        this.sga = sga;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                return true;

            case MotionEvent.ACTION_MOVE:
                float newX = event.getRawX() + dX;
                float newY = event.getRawY() + dY;

                // Ensure the new position is within the bounds of the parent view
                int parentWidth = parentView.getWidth();
                int parentHeight = parentView.getHeight();
                int viewWidth = view.getWidth();
                int viewHeight = view.getHeight();

                if (newX < 0) newX = 0;
                if (newX + viewWidth > parentWidth) newX = parentWidth - viewWidth;
                if (newY < 0) newY = 0;
                if (newY + viewHeight > parentHeight) newY = parentHeight - viewHeight;

                view.setX(newX);
                view.setY(newY);
                return true;

            case MotionEvent.ACTION_UP:
                if (isViewFullyWithinAnotherView(innerView, outerView1) && shapeName.equals(outerView1.getText()) && !outerViewFilled1) {
                    outerViewFilled1 = true;
                    ShapesGameActivity.rounds++;
                    Log.d("TAG", "11");

                    if (outerViewFilled1 && outerViewFilled2 && outerViewFilled3 && outerViewFilled4) {
                        Log.d("TAG", "Condition 1: All views filled");
                        sga.randomizeField();
                        resetFlags();
                    }
                }
                if (isViewFullyWithinAnotherView(innerView, outerView2) && shapeName.equals(outerView2.getText()) && !outerViewFilled2) {
                    outerViewFilled2 = true;
                    ShapesGameActivity.rounds++;
                    Log.d("TAG", "21");

                    if (outerViewFilled1 && outerViewFilled2 && outerViewFilled3 && outerViewFilled4) {
                        Log.d("TAG", "Condition 2: All views filled");
                        sga.randomizeField();
                        resetFlags();
                    }
                }
                if (isViewFullyWithinAnotherView(innerView, outerView3) && shapeName.equals(outerView3.getText()) && !outerViewFilled3) {
                    outerViewFilled3 = true;
                    ShapesGameActivity.rounds++;
                    Log.d("TAG", "31");

                    if (outerViewFilled1 && outerViewFilled2 && outerViewFilled3 && outerViewFilled4) {
                        Log.d("TAG", "Condition 3: All views filled");
                        sga.randomizeField();
                        resetFlags();
                    }
                }
                if (isViewFullyWithinAnotherView(innerView, outerView4) && shapeName.equals(outerView4.getText()) && !outerViewFilled4) {
                    outerViewFilled4 = true;
                    ShapesGameActivity.rounds++;
                    Log.d("TAG", "41");

                    if (outerViewFilled1 && outerViewFilled2 && outerViewFilled3 && outerViewFilled4) {
                        Log.d("TAG", "Condition 4: All views filled");
                        sga.randomizeField();
                        resetFlags();
                    }
                }
                // Add logs to print the flag values after all checks
                Log.d("TAG", "Flags: " + outerViewFilled1 + ", " + outerViewFilled2 + ", " + outerViewFilled3 + ", " + outerViewFilled4);
                return true;
            default:
                return false;
        }
    }
    private void resetFlags() {
        outerViewFilled1 = false;
        outerViewFilled2 = false;
        outerViewFilled3 = false;
        outerViewFilled4 = false;
    }
    private boolean isViewOverlapping(View view1, View view2) {
        Rect rect1 = new Rect();
        Rect rect2 = new Rect();

        view1.getHitRect(rect1);
        view2.getHitRect(rect2);

        return Rect.intersects(rect1, rect2);
    }
    private boolean isViewFullyWithinAnotherView(View draggedView, View fixedView) {
        int[] fixedViewPos = new int[2];
        fixedView.getLocationOnScreen(fixedViewPos);
        Rect fixedViewRect = new Rect(
                fixedViewPos[0],
                fixedViewPos[1],
                fixedViewPos[0] + fixedView.getWidth(),
                fixedViewPos[1] + fixedView.getHeight()
        );

        int[] draggedViewPos = new int[2];
        draggedView.getLocationOnScreen(draggedViewPos);
        Rect draggedViewRect = new Rect(
                draggedViewPos[0],
                draggedViewPos[1],
                draggedViewPos[0] + draggedView.getWidth(),
                draggedViewPos[1] + draggedView.getHeight()
        );

        return fixedViewRect.contains(draggedViewRect);
    }
}
