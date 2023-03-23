package com.example.simpleapp.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
/**
 * SpriteRotator class
 *
 * This class is responsible for rotating the sprites
 */

public class SpriteRotator {

    // Constuctor takes the bitmap (image) and rotates it by angle
    public static Bitmap rotate(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.setRotate(angle, source.getWidth() / 2.0f, source.getHeight() / 2.0f);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    // Draw the rotated bitmap to canvas at position x and y and angle
    public static void drawRotatedBitmap(Canvas canvas, Bitmap source, float x, float y, float angle) {
        Bitmap rotatedBitmap = rotate(source, angle);
        float drawPosX = x - (rotatedBitmap.getWidth() / 2.0f);
        float drawPosY = y - (rotatedBitmap.getHeight() / 2.0f);
        canvas.drawBitmap(rotatedBitmap, drawPosX, drawPosY, new Paint());
    }
}