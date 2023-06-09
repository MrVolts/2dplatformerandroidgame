package com.example.simpleapp.Object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.simpleapp.GameDisplay;
import com.example.simpleapp.MainThread;
import com.example.simpleapp.gamepanel.HealthBar;
import com.example.simpleapp.gamepanel.Joystick;
import com.example.simpleapp.graphics.SpriteRotator;
import com.example.simpleapp.map.MapLayout;
import com.example.simpleapp.map.Tile.TileType;
import com.example.simpleapp.map.TileMap;

/**
 * Player class that extends GameObject and is used to create a player object.
 * who is the main character in the game.
 */
public class Player extends GameObject {
    public static final double SPEED_PIXELS_PER_SECOND = 8;
    public static final int MAX_HEALTH = 50;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / MainThread.MAX_UPS;
    private static final double SPELLS_PER_MINUTE = 120; // Adjust this value to control the firing rate
    private static final double SPELLS_PER_SECOND = SPELLS_PER_MINUTE / 60;
    private static final double UPDATES_PER_SPELL = MainThread.MAX_UPS / SPELLS_PER_SECOND;
    private final HealthBar healthBar;
    private final TileMap tileMap;
    private int healthPoints;
    private float rotationAngle;
    private int updatesSinceLastSpell = 0;


    // Constructor for Player class that takes in a bitmap
    public Player(Bitmap bmp, int x, int y, int desiredWidth, int desiredHeight, Context context, TileMap tileMap) {
        super(bmp, x, y, desiredWidth, desiredHeight);
        this.healthBar = new HealthBar(this, context);
        this.healthPoints = MAX_HEALTH;
        this.tileMap = tileMap;
    }

    // update class that takes in a joystick and updates the player's position
    // also takes into account the bounds of the map
    public void update(Joystick joystick) {
        if (joystick.getJoystickX() != 0 || joystick.getJoystickY() != 0) {
            double adjustedSpeed = adjustSpeedForTile(MAX_SPEED);
            xVelocity = joystick.getJoystickX() * adjustedSpeed;
            yVelocity = joystick.getJoystickY() * adjustedSpeed;
            rotationAngle = (float) Math.toDegrees(joystick.getAngle());

            float newX = posX + (float) xVelocity;
            float newY = posY + (float) yVelocity;

            if (isPositionInsideMapBounds(MapLayout.TILE_WIDTH_PIXELS, MapLayout.TILE_HEIGHT_PIXELS, MapLayout.NUMBER_OF_ROW_TILES, MapLayout.NUMBER_OF_COLUMN_TILES, newX, newY)) {
                posX = newX;
                posY = newY;
            }
        } else {
            xVelocity = 0;
            yVelocity = 0;
        }
    }

    // Draws the player and the health bar
    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        float drawPosX = gameDisplay.gameToDisplayCoordinatesX(posX);
        float drawPosY = gameDisplay.gameToDisplayCoordinatesY(posY);

        SpriteRotator.drawRotatedBitmap(
                canvas,
                image,
                drawPosX,
                drawPosY,
                rotationAngle
        );
        healthBar.draw(canvas, gameDisplay);
    }

    // Method that takes in damage and reduces the player's health
    public void takeDamage(int damage) {
        if (damage >= healthPoints) {
            healthPoints = 0;
        } else {
            healthPoints -= damage;
        }
    }

    // Method that takes in a speed and adjusts it based on the tile the player is on
    private double adjustSpeedForTile(double speed) {
        TileType tileType = tileMap.getTileTypeAt(posX, posY);

        if (tileType == TileType.BASE1) {
            return speed * 0.8; // Adjust this factor to control the slowdown on BASE2 tiles
        }
        if (tileType == TileType.BASE2) {
            return speed * 0.6; // Adjust this factor to control the slowdown on BASE1 tiles
        }

        return speed;
    }

    // Getter and setter methods for the player's health
    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        if (healthPoints >= 0 && healthPoints <= MAX_HEALTH) {
            this.healthPoints = healthPoints;
        }
    }

    // Getter method for the player's max health
    public int getMaxHealth() {
        return MAX_HEALTH;
    }

    // Method that restores the player's health after a wave
    public void restoreHealthAfterWave() {
        int restoredHealth = healthPoints + MAX_HEALTH / 2;
        setHealthPoints(Math.min(restoredHealth, MAX_HEALTH));
    }

    // Method that checks if the player should fire a spell
    public boolean shouldFireSpell() {
        updatesSinceLastSpell++;

        if (updatesSinceLastSpell >= UPDATES_PER_SPELL) {
            updatesSinceLastSpell = 0;
            return true;
        }

        return false;
    }
}
