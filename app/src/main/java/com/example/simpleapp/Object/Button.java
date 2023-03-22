package com.example.simpleapp.Object;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Button {
    private final Rect rect;
    private final Paint paint;
    private final String text;
    private final int textSize;

    public Button(int left, int top, int right, int bottom, int color, String text, int textSize) {
        rect = new Rect(left, top, right, bottom);
        paint = new Paint();
        paint.setColor(color);
        this.text = text;
        this.textSize = textSize;
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(rect, paint);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        int xPos = rect.centerX();
        int yPos = (int) ((rect.centerY()) - ((textPaint.descent() + textPaint.ascent()) / 2));

        canvas.drawText(text, xPos, yPos, textPaint);
    }

    public boolean isPressed(float x, float y) {
        return rect.contains((int) x, (int) y);
    }
}