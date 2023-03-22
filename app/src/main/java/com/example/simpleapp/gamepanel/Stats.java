package com.example.simpleapp.gamepanel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.simpleapp.MainThread;
import com.example.simpleapp.Object.Enemy;

public class Stats {
    private final MainThread thread;
    private final long startTime;
    private int score;
    private long lastScoreUpdateTime;
    private int enemiesKilled;
    private boolean gameOver;
    private long endTime;


    public Stats(MainThread thread) {
        this.thread = thread;
        this.score = 0;
        this.lastScoreUpdateTime = System.currentTimeMillis();
        this.startTime = System.currentTimeMillis(); // Initialize the startTime variable
        this.enemiesKilled = 0; // Initialize the enemiesKilled variable
        this.gameOver = false;
    }

    public void addScore(int points) {
        score += points;
    }

    public void updateScore() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastScoreUpdateTime >= 1000) {
            score += 1; // Add 1 point to the score every second
            lastScoreUpdateTime = currentTime;
        }
    }

    public void drawWaveInfo(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(70);
        canvas.drawText("Wave: " + Enemy.getWaveNumber(), canvas.getWidth() / 2 - 150, 70, paint);
        canvas.drawText("Enemies Spawned: " + Enemy.getEnemiesRemainingInWave() + " / " + Enemy.getWaveSize(), canvas.getWidth() / 2 - 150, 150, paint);
    }

    public void drawScore(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(70);
        canvas.drawText("Score: " + score, canvas.getWidth() - 400, 70, paint);
    }

    public void drawFPS(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(70);
        canvas.drawText("FPS: " + thread.getAverageFPS(), 70, 70, paint);
    }

    public void drawUPS(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(70);
        canvas.drawText("UPS: " + thread.getAverageUPS(), 70, 170, paint);
    }

    public void draw(Canvas canvas) {
        drawFPS(canvas);
        drawUPS(canvas);
        drawScore(canvas);
        drawWaveInfo(canvas);
    }

    public int getScore() {
        return score;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public int getTimeAlive() {
        if (gameOver) {
            return (int) ((endTime - startTime) / 1000);
        } else {
            return (int) ((System.currentTimeMillis() - startTime) / 1000);
        }
    }

    public int getEnemiesKilled() {
        return enemiesKilled;
    }

    public void incrementEnemiesKilled() {
        enemiesKilled++;
    }
}