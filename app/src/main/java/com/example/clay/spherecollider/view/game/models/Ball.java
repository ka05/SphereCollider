package com.example.clay.spherecollider.view.game.models;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


import com.example.clay.spherecollider.view.game.management.GameMediator;
import com.example.clay.spherecollider.view.game.sensors.SensorHandler;
import com.example.clay.spherecollider.view.game.util.PaintUtility;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;


/**
 * Ball that will appear in the center of the screen
 */
public class Ball implements GameModel, Observer {
    private GameMediator mediator;
    private int size, originalSize;
    private Rect bounds;
    private Paint circlePaint;
    private ModelType type;

    private float xPosition, pitch , xVelocity = 0.0f;
    private float yPosition, roll , yVelocity = 0.0f;
    private int decreaseValue = 0, increaseValue;
    private int xMax, yMax;
    private int ballMaxSize, ballWarning;
    private long lastAlert;
    private boolean increasing = false,
            decreasing = false,
            isWarning = false,
            showingRed = false,
            tooBig = false;

    private String color;

    private final float FRAME_TIME = 0.45f;

    /**
     * Default constructor
     * @param size
     * @param color
     */
    public Ball(int size, String color) {
        this.color = color;
        this.type = ModelType.BALL;
        this.size = size;
        this.originalSize = size;
        this.mediator = GameMediator.getInstance();
        this.circlePaint = PaintUtility.configurePaintForCircle(color);
        this.setMax();
        this.ballMaxSize = Math.round((float) yMax * 0.6f);
        this.ballWarning = Math.round((float) yMax * 0.4f);
        this.lastAlert = new Date().getTime();
        this.initializeStartingPoint();
        this.setBounds(Math.round(xPosition), Math.round(yPosition));
    }

    @Override
    public void update(Observable observable, Object data) {
        this.pitch = ((SensorHandler)observable).getPitch();
        this.roll = ((SensorHandler)observable).getRoll();
        if (increasing) increaseBounds();
        if (decreasing) decreaseBounds();
        if (isWarning) showAlert();
        this.updatePositions();
        this.checkSize();
    }

    /**
     * Draws the ball on the canvas
     *
     * @param canvas
     */
    @Override
    public void render(Canvas canvas) {
        this.setBounds(Math.round(xPosition), Math.round(yPosition));
        int radius = size / 2;
        int centerX = bounds.left + radius;
        int centerY = bounds.top + radius;
        canvas.drawCircle(centerX, centerY, radius, circlePaint);
    }

    /**
     * Updates the starting location of the view port
     */
    private void updatePositions() {
        //Calculate new speed
        xVelocity += (pitch * FRAME_TIME);
        yVelocity += (roll * FRAME_TIME);

        //Calc distance travelled in that time
        float xS = ((xVelocity / 2) * FRAME_TIME);
        float yS = ((yVelocity / 2) * FRAME_TIME);

        //Add to position negative due to sensor
        //readings being opposite to what we want!
        xPosition -= xS;
        yPosition -= yS;

        if (xPosition > xMax || xPosition < 0) {
            xVelocity *= -0.5;
            xS = ((xVelocity / 2) * FRAME_TIME);
            yS = ((yVelocity / 2) * FRAME_TIME);
            xPosition -= xS;
            yPosition -= yS;
        }
        else if (yPosition > yMax || yPosition < 0) {
            yVelocity *= -0.5;
            xS = ((xVelocity / 2) * FRAME_TIME);
            yS = ((yVelocity / 2) * FRAME_TIME);
            xPosition -= xS;
            yPosition -= yS;
        }
        if (xPosition > xMax) {
            xPosition = xMax;
        } else if (xPosition < 0) {
            xPosition = 0;
        }
        if (yPosition > yMax) {
            yPosition = yMax;
        } else if (yPosition < 0) {
            yPosition = 0;
        }
    }

    /**
     * Gets the current bounds
     *
     * @return
     */
    @Override
    public Rect getBounds() {
        return bounds;
    }

    /**
     * Gets the model type
     *
     * @return
     */
    @Override
    public ModelType getType() {
        return type;
    }

    /**
     * Increases the size of the ball
     *
     */
    public void increaseSize(int inflateValue) {
        increaseValue = inflateValue;
        increasing = true;
    }

    /**
     * Decreases the size of the ball
     *
     * @param dec
     */
    public void decreaseSize(int reduceValue) {
        decreaseValue = reduceValue;
        decreasing = true;
    }

    /**
     * Initializes the starting point in the center of the screen
     */
    private void initializeStartingPoint() {
        int centerX = xMax / 2;
        int centerY = yMax / 2;
        xPosition = centerX - (size / 2);
        yPosition = centerY - (size / 2);
    }

    /**
     * Sets the bounds fo the ball
     * @param x
     * @param y
     */
    private void setBounds(int x, int y) {
        bounds = new Rect(x, y, x + size, y + size);
    }

    private void setMax() {
        xMax = mediator.getXMax() - size;
        yMax = mediator.getYMax() - size;
    }

    private void increaseBounds() {
        size++;
        increaseValue--;
        if (increaseValue == 0) increasing = false;
        setMax();
        this.setBounds(Math.round(xPosition), Math.round(yPosition));
    }

    private void decreaseBounds() {
        size--;
        if (size < originalSize) {
            size = originalSize;
            decreasing = false;
        }
        decreaseValue--;
        if (decreaseValue == 0) decreasing = false;
        setMax();
        this.setBounds(Math.round(xPosition), Math.round(yPosition));
    }

    private void showAlert() {
        if (new Date().getTime() - lastAlert > 500) {
            if (!showingRed) {
                circlePaint = PaintUtility.configurePaintForCircle("#ff1400");
                showingRed = true;
            } else {
                circlePaint = PaintUtility.configurePaintForCircle(color);
                showingRed = false;
            }
            lastAlert = new Date().getTime();
        }
    }

    private void checkSize() {
        if (size > ballMaxSize) {
            tooBig = true;
        }
        else if (size > ballWarning) {
            isWarning = true;
        }
        else {
            isWarning = false;
            circlePaint = PaintUtility.configurePaintForCircle(color);
        }
    }

    public boolean isTooBig() {
        return tooBig;
    }
}
