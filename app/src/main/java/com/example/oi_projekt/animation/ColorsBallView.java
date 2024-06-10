package com.example.oi_projekt.animation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ColorsBallView extends View {
    private Paint paint;
    private float x;
    private float y;
    private float radius;

    public ColorsBallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        radius = 70; // Set the ball radius
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(x, y, radius, paint);
    }

    public void updatePosition(float deltaX, float deltaY) {
        x += deltaX;
        y += deltaY;

        // Ensure the ball stays within the bounds of the view
        if (x < radius) {
            x = radius;
        } else if (x > getWidth() - radius) {
            x = getWidth() - radius;
        }
        if (y < radius) {
            y = radius;
        } else if (y > getHeight() - radius) {
            y = getHeight() - radius;
        }

        invalidate(); // Redraw the view
    }
}
