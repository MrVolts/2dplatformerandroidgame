package com.example.simpleapp.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.content.SharedPreferences;

import com.example.simpleapp.MainThread;
import com.example.simpleapp.Object.Enemy;

public class Stats {
    private final MainThread thread;
    private final long startTime;
    private final SharedPreferences sharedPreferences;
    private int score;
    private long lastScoreUpdateTime;
    private int enemiesKilled;
    private boolean gameOver;
    private long endTime;
    private int highScore;

    // Constructor for the Stats class
    public Stats(MainThread thread, Context context) {
        this.thread = thread;
        this.score = 0;
        this.lastScoreUpdateTime = System.currentTimeMillis();
        this.startTime = System.currentTimeMillis(); // Initialize the startTime variable
        this.enemiesKilled = 0; // Initialize the enemiesKilled variable
        this.gameOver = false;
        this.sharedPreferences = context.getSharedPreferences("highscore", Context.MODE_PRIVATE);
        this.highScore = loadHighScore();
    }

    // Add points to the score
    public void addScore(int points) {
        score += points;
    }

    // Update the score every second
    public void updateScore() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastScoreUpdateTime >= 1000) {
            score += 1; // Add 1 point to the score every second
            lastScoreUpdateTime = currentTime;
        }
    }

    // Draw the score, wave number, and enemies spawned
    public void drawWaveInfo(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(70);
        canvas.drawText("Level: " + Enemy.getWaveNumber(), canvas.getWidth() / 2 - 150, 70, paint);
        canvas.drawText("Enemies Spawned: " + (Enemy.getWaveSize() - Enemy.getEnemiesRemainingInWave())
                + " / " + Enemy.getWaveSize(), canvas.getWidth() / 2 - 150, 150, paint);
    }

    // Draw the score
    public void drawScore(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(70);
        canvas.drawText("Score: " + score, canvas.getWidth() - 400, 70, paint);
    }

    // Draw the FPS
    public void drawFPS(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(70);
        canvas.drawText("FPS: " + thread.getAverageFPS(), 70, 70, paint);
    }

    // Draw the UPS
    public void drawUPS(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(70);
        canvas.drawText("UPS: " + thread.getAverageUPS(), 70, 170, paint);
    }

    // Draw the UPS, FPS, score, and wave info
    public void draw(Canvas canvas) {
        drawFPS(canvas);
        drawUPS(canvas);
        drawScore(canvas);
        drawWaveInfo(canvas);
    }

    // get time alive
    public int getTimeAlive() {
        if (gameOver) {
            return (int) ((endTime - startTime) / 1000);
        } else {
            return (int) ((System.currentTimeMillis() - startTime) / 1000);
        }
    }

    public int getHighScore() {
        return highScore;
    }

    // get enemies killed
    public int getEnemiesKilled() {
        return enemiesKilled;
    }

    // get score
    public int getScore() {
        return score;
    }

    // set end time for how long the player survived
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    // set game over when player died
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    // increment enemies killed when player kills an enemy
    public void incrementEnemiesKilled() {
        enemiesKilled++;
    }

    private int loadHighScore() {
        return sharedPreferences.getInt("highScore", 0);
    }

    public void saveHighScore(int highScore) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("highScore", highScore);
        editor.apply();
        this.highScore = highScore;
    }
}