package com.example.simpleapp.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;

import com.example.simpleapp.GameView;
import com.example.simpleapp.Object.Button;
import com.example.simpleapp.Object.Enemy;
import com.example.simpleapp.R;

public class GameOver {

    private static final float STATS_TEXT_SIZE_RATIO = 0.08f;
    private static final float GAME_OVER_TEXT_SIZE_RATIO = 0.15f;
    private static final float BOX_WIDTH_RATIO = 0.6f;
    private static final float BOX_HEIGHT_RATIO = 0.6f;
    private static final float BUTTON_WIDTH_RATIO = 3f;
    private static final float BUTTON_HEIGHT_RATIO = 0.15f;
    private final Context context;
    private final Button restartButton;
    private final float gameOverTextSize;
    private final float statsTextSize;

    public GameOver(Context context, int screenWidth, int screenHeight) {
        this.context = context;
        this.statsTextSize = screenHeight * STATS_TEXT_SIZE_RATIO;
        this.gameOverTextSize = screenHeight * GAME_OVER_TEXT_SIZE_RATIO;
        int buttonHeight = (int) (screenHeight * BUTTON_HEIGHT_RATIO);
        int buttonWidth = (int) (buttonHeight * BUTTON_WIDTH_RATIO);
        int buttonLeft = (screenWidth - buttonWidth) / 2;
        int buttonTop = (int) (screenHeight * 0.75);
        restartButton = new Button(buttonLeft, buttonTop, buttonLeft + buttonWidth, buttonTop + buttonHeight, Color.RED, "Restart", (int) (statsTextSize * 0.8));
    }

    public void draw(Canvas canvas, Stats stats) {
        drawShadedBox(canvas);
        drawGameOverText(canvas);
        drawStats(canvas, stats);
        restartButton.draw(canvas);
    }

    private void drawGameOverText(Canvas canvas) {
        String text = "Game Over";
        float x = canvas.getWidth() / 2;
        float y = (float) (canvas.getHeight() * 0.3);

        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.gameOver);
        paint.setColor(color);
        paint.setTextSize(gameOverTextSize);
        paint.setTextAlign(Paint.Align.CENTER);

        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        float textHeight = textBounds.height();

        canvas.drawText(text, x, y + textHeight / 2, paint);
    }

    private void drawStats(Canvas canvas, Stats stats) {
        String statsText = "Time Alive: " + stats.getTimeAlive() + "s\nEnemies Killed: " +
                stats.getEnemiesKilled() + "\nWaves Survived:" + (Enemy.getWaveNumber() - 1) + "\nTotal Score: " + stats.getScore();

        float x = canvas.getWidth() / 2;
        float y = (float) (canvas.getHeight() * 0.45);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(statsTextSize);
        paint.setTextAlign(Paint.Align.CENTER);

        for (String line : statsText.split("\n")) {
            canvas.drawText(line, x, y, paint);
            y += paint.descent() - paint.ascent();
        }
    }

    private void drawShadedBox(Canvas canvas) {
        int boxWidth = (int) (canvas.getWidth() * BOX_WIDTH_RATIO);
        int boxHeight = (int) (canvas.getHeight() * BOX_HEIGHT_RATIO);

        int boxLeft = (canvas.getWidth() - boxWidth) / 2;
        int boxTop = (canvas.getHeight() - boxHeight) / 2;
        int boxRight = boxLeft + boxWidth;
        int boxBottom = boxTop + boxHeight;

        Paint boxPaint = new Paint();
        boxPaint.setColor(Color.argb(150, 0, 0, 0)); // Semi-transparent black color
        float cornerRadius = 30f;

        canvas.drawRoundRect(boxLeft, boxTop, boxRight, boxBottom, cornerRadius, cornerRadius, boxPaint);
    }


    public boolean onTouchEvent(MotionEvent event, GameView gameView) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            if (restartButton.isPressed(x, y)) {
                gameView.restart();
                return true;
            }
        }
        return false;
    }
}

