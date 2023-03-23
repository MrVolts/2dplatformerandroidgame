package com.example.simpleapp.Object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.example.simpleapp.GameDisplay;

public abstract class GameObject {
    protected float posX, posY;
    protected Bitmap image;
    protected double xVelocity, yVelocity;


    // Constructor for GameObject class that takes in a bitmap, x, and y
    public GameObject(Bitmap bmp, int x, int y, int desiredWidth, int desiredHeight) {
        this.image = scaleBitmap(bmp, desiredWidth, desiredHeight);
        this.posX = x;
        this.posY = y;
    }

    // Scale the bitmap to the desired width and height
    protected static Bitmap scaleBitmap(Bitmap originalBitmap, int desiredWidth, int desiredHeight) {
        float widthScaleFactor = (float) desiredWidth / originalBitmap.getWidth();
        float heightScaleFactor = (float) desiredHeight / originalBitmap.getHeight();

        int scaledWidth = (int) (originalBitmap.getWidth() * widthScaleFactor);
        int scaledHeight = (int) (originalBitmap.getHeight() * heightScaleFactor);
        return Bitmap.createScaledBitmap(originalBitmap, scaledWidth, scaledHeight, true);
    }

    // Draw the bitmap to the screen
    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        canvas.drawBitmap(image,
                gameDisplay.gameToDisplayCoordinatesX(posX - image.getWidth() / 2.0f),
                gameDisplay.gameToDisplayCoordinatesY(posY - image.getHeight() / 2.0f),
                null);
    }

    // Getters for x and y
    public float getPositionX() {
        return posX;
    }

    // Getters for x and y
    public float getPositionY() {
        return posY;
    }

    // Getters for width
    public int getWidth() {
        return image.getWidth();
    }

    // Getters for height
    public int getHeight() {
        return image.getHeight();
    }

    // Check if two game objects are colliding
    public RectF getRectF() {
        return new RectF(
                posX - image.getWidth() / 2.0f,
                posY - image.getHeight() / 2.0f,
                posX + image.getWidth() / 2.0f,
                posY + image.getHeight() / 2.0f
        );
    }

    public boolean isColliding(GameObject obj1, GameObject obj2) {
        RectF rect1 = obj1.getRectF();
        RectF rect2 = obj2.getRectF();
        return RectF.intersects(rect1, rect2);
    }

    // get distance between two game objects taking two objects as parameters
    public double getDistance(GameObject obj1, GameObject obj2) {
        return Math.sqrt(
                Math.pow(obj1.getPositionX() - obj2.getPositionX(), 2)
                        + Math.pow(obj1.getPositionY() - obj2.getPositionY(), 2)
        );

    }

    // Checks if an object is out of bounds of the screen
    public boolean isOutOfBounds(GameDisplay gameDisplay, float factor) {
        float screenPosX = gameDisplay.gameToDisplayCoordinatesX(posX);
        float screenPosY = gameDisplay.gameToDisplayCoordinatesY(posY);
        float screenWidth = (float) (2 * gameDisplay.getDisplayCentreX());
        float screenHeight = (float) (2 * gameDisplay.getDisplayCentreY());

        float offScreenX = factor * screenWidth;
        float offScreenY = factor * screenHeight;

        return screenPosX < -offScreenX || screenPosX > screenWidth + offScreenX ||
                screenPosY < -offScreenY || screenPosY > screenHeight + offScreenY;
    }

    // Checks if an object is inside the map bounds
    public boolean isPositionInsideMapBounds(int tileWidthPixels, int tileHeightPixels, int numberOfRows, int numberOfColumns, float newX, float newY) {
        int mapWidthPixels = tileWidthPixels * numberOfColumns;
        int mapHeightPixels = tileHeightPixels * numberOfRows;

        float halfWidth = image.getWidth() / 2.0f;
        float halfHeight = image.getHeight() / 2.0f;

        return newX - halfWidth >= 0 &&
                newX + halfWidth <= mapWidthPixels &&
                newY - halfHeight >= 0 &&
                newY + halfHeight <= mapHeightPixels;
    }
}
