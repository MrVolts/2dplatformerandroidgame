package com.example.simpleapp.gamepanel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
/**
 * Joystick class to create a joystick on the screen for player control
 */

public class Joystick {
    private final int outerCirclePosx;
    private final int outerCirclePosy;
    private final int outerCircleRadius;
    private final int innerCircleRadius;
    private final Paint outerCirclePaint;
    private final Paint innerCirclePaint;
    private int innerCirclePosX;
    private int innerCirclePosY;
    private double joystickCenterToTouchDistance;
    private double lastAngle;

    /**
     * Constructor for joystick class to position the joystick
     * @param xPosition x position of the joystick
     * @param yPosition y position of the joystick
     * @param outerCircleRadius radius of the outer circle
     * @param innerCircleRadius radius of the inner circle
     */

    // Constructor for joystick class to position the joystick
    public Joystick(int xPosition, int yPosition, int outerCircleRadius, int innerCircleRadius) {
        outerCirclePosx = xPosition;
        outerCirclePosy = yPosition;
        innerCirclePosX = xPosition;
        innerCirclePosY = yPosition;

        // radius of the outer circle
        this.outerCircleRadius = outerCircleRadius;

        // radius of the inner circle
        this.innerCircleRadius = innerCircleRadius;

        // paint outer circle
        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.GRAY);
        outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        // paint inner circle
        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(Color.BLUE);
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    /**
     * Draw method to draw the joystick on the screen
     * @param canvas canvas to draw the joystick on
     */

    // method to draw the joystick on the screen
    public void draw(Canvas canvas) {
        // draw outer circle
        canvas.drawCircle(
                outerCirclePosx,
                outerCirclePosy,
                outerCircleRadius,
                outerCirclePaint);

        // draw inner circle
        canvas.drawCircle(
                innerCirclePosX,
                innerCirclePosY,
                innerCircleRadius,
                innerCirclePaint);
    }

    /**
     * Methods to get the distance between the joystick and the touch position
     * @param touchPositionX x position of the touch
     * @param touchPositionY y position of the touch
     * @return distance between the joystick and the touch position
     */

    // method to set the position of the joystick
    public void setActuatorPos(double touchPositionX, double touchPositionY) {
        joystickCenterToTouchDistance = getDistance(touchPositionX, touchPositionY);
        if (joystickCenterToTouchDistance < outerCircleRadius) {
            innerCirclePosX = (int) touchPositionX;
            innerCirclePosY = (int) touchPositionY;
        } else {
            innerCirclePosX = (int) (outerCirclePosx + (outerCircleRadius * (touchPositionX - outerCirclePosx) / joystickCenterToTouchDistance));
            innerCirclePosY = (int) (outerCirclePosy + (outerCircleRadius * (touchPositionY - outerCirclePosy) / joystickCenterToTouchDistance));
        }
        lastAngle = getAngle();
    }

    // method to reset the joystick to the center
    public void resetActuator() {
        innerCirclePosX = outerCirclePosx;
        innerCirclePosY = outerCirclePosy;
    }

    /**
     * getters used by other classes to get the joystick position and angle
     */

    // gets the joystick position X
    public int getJoystickX() {
        return innerCirclePosX - outerCirclePosx;
    }

    // gets the joystick position Y
    public int getJoystickY() {
        return innerCirclePosY - outerCirclePosy;
    }

    // gets the joystick angle
    public double getAngle() {
        return Math.atan2(getJoystickY(), getJoystickX());
    }

    // gets the last joystick angle useful for spell casting
    public double getLastAngle() {
        return lastAngle;
    }

    // method to calculate distance between the center of the joystick and the touch point
    public double getDistance(double x, double y) {
        return Math.sqrt(Math.pow(x - outerCirclePosx, 2) + Math.pow(y - outerCirclePosy, 2));
    }
}
