package com.example.simpleapp.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.simpleapp.GameDisplay;
import com.example.simpleapp.Object.Player;
import com.example.simpleapp.R;

public class HealthBar {
    private final Player player;
    private final Paint borderPaint;
    private final Paint healthPaint;
    private final int width;
    private final int height;
    private final int margin;
    private final Context context;

    public HealthBar(Player player, Context context) {
        this.player = player;
        this.context = context;
        this.width = 200;
        this.height = 40;
        this.margin = 5;

        this.borderPaint = new Paint();
        this.borderPaint.setColor(ContextCompat.getColor(context, R.color.healthBarBorder));
        this.borderPaint.setStyle(Paint.Style.STROKE);
        this.borderPaint.setStrokeWidth(2);

        // Initialize the healthPaint variable
        this.healthPaint = new Paint();
        this.healthPaint.setColor(ContextCompat.getColor(context, R.color.healthBar));
        this.healthPaint.setStyle(Paint.Style.FILL);
    }

    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        float x = (float) player.getPositionX();
        float y = (float) player.getPositionY();
        float distanceToPlayer = 50;
        float healthPointPercentage = (float) player.getHealthPoints() / (float) player.getMaxHealth();

        // Draw border
        float borderLeft, borderTop, borderRight, borderBottom;
        borderLeft = x - width / 2;
        borderTop = y - distanceToPlayer - height;
        borderRight = x + width / 2;
        borderBottom = y - distanceToPlayer;
        canvas.drawRect(gameDisplay.gameToDisplayCoordinatesX(borderLeft),
                gameDisplay.gameToDisplayCoordinatesY(borderTop),
                gameDisplay.gameToDisplayCoordinatesX(borderRight),
                gameDisplay.gameToDisplayCoordinatesY(borderBottom),
                borderPaint);
        // Draw Health
        float healthWidth, healthHeight, healthLeft, healthRight, healthBottom, healthTop;
        healthWidth = width - 2 * margin;
        healthHeight = height - 2 * margin;
        healthLeft = borderLeft + margin;
        healthRight = healthLeft + healthWidth * healthPointPercentage;
        healthBottom = borderBottom - margin;
        healthTop = healthBottom - healthHeight;
        canvas.drawRect(gameDisplay.gameToDisplayCoordinatesX(healthLeft),
                gameDisplay.gameToDisplayCoordinatesY(healthTop),
                gameDisplay.gameToDisplayCoordinatesX(healthRight),
                gameDisplay.gameToDisplayCoordinatesY(healthBottom),
                healthPaint);

    }
}