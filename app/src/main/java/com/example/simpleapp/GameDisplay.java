package com.example.simpleapp;

import android.graphics.Rect;

import com.example.simpleapp.Object.GameObject;

public class GameDisplay {
    public final Rect DISPLAY_RECT;
    private final double heightPixels;
    private final double widthPixels;
    private final double displayCentreX;
    private final double displayCentreY;
    private final GameObject centreObject;
    private double gameToDisplayCoordinateOffsetX;
    private double gameToDisplayCoordinateOffsetY;
    private double gameCenterX;
    private double gameCenterY;


    public GameDisplay(int widthPixels, int heightPixels, GameObject centreObject) {
        this.centreObject = centreObject;
        this.displayCentreX = widthPixels / 2;
        this.displayCentreY = heightPixels / 2;

        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;
        DISPLAY_RECT = new Rect(0, 0, widthPixels, heightPixels);
    }

    public void update() {
        gameCenterX = centreObject.getPositionX();
        gameCenterY = centreObject.getPositionY();

        gameToDisplayCoordinateOffsetX = displayCentreX - gameCenterX;
        gameToDisplayCoordinateOffsetY = displayCentreY - gameCenterY;
    }

    // Removed parentheses from gameToDisplayCoordinateOffsetX and gameToDisplayCoordinateOffsetY
    public float gameToDisplayCoordinatesX(double x) {
        return (float) (x + gameToDisplayCoordinateOffsetX);
    }

    public float gameToDisplayCoordinatesY(double y) {
        return (float) (y + gameToDisplayCoordinateOffsetY);
    }

    public double getDisplayCentreX() {
        return displayCentreX;
    }

    public double getDisplayCentreY() {
        return displayCentreY;
    }

    public double displayToGameCoordinatesX(double x) {
        return x - gameToDisplayCoordinateOffsetX;
    }

    public double displayToGameCoordinatesY(double y) {
        return y - gameToDisplayCoordinateOffsetY;
    }

    public Rect getGameRect() {
        return new Rect(
                (int) (gameCenterX - widthPixels / 2),
                (int) (gameCenterY - heightPixels / 2),
                (int) (gameCenterX + widthPixels / 2),
                (int) (gameCenterY + heightPixels / 2)
        );
    }
}
