package com.example.simpleapp.gamepanel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

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

    //method to calculate distance between the center of the joystick and the touch point
    public double getDistance(double x, double y) {
        return Math.sqrt(Math.pow(x - outerCirclePosx, 2) + Math.pow(y - outerCirclePosy, 2));
    }

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

    public int getJoystickX() {
        return innerCirclePosX - outerCirclePosx;
    }

    public int getJoystickY() {
        return innerCirclePosY - outerCirclePosy;
    }

    public double getAngle() {
        return Math.atan2(getJoystickY(), getJoystickX());
    }

    public boolean isPressed(float x, float y) {
        return getDistance(x, y) <= outerCircleRadius;
    }

    public double getLastAngle() {
        return lastAngle;
    }
}
