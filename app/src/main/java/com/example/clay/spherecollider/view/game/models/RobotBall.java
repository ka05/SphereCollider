package com.example.clay.spherecollider.view.game.models;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.clay.spherecollider.view.game.management.GameMediator;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Computer operated balls
 */
public class RobotBall implements GameModel, RandomizedModel {
    private GameMediator mediator;
    private Rect bounds;
    private Paint circlePaint;
    private ModelType type;
    private ConcurrentLinkedQueue models;

    private int value, valueMax;
    private int circleX, circleY, radius;
    private int size, originalSize, increaseValue = 0, decreaseValue = 0;
    private int xMax, yMax;
    private float xVelocity, yVelocity, frameTime;
    private float xPosition, yPosition;

    private boolean increasing = false, decreasing = false, destroying = false;

    /**
     * Default constructor
     * @param size
     * @param value
     * @param type
     * @param circlePaint
     */
    public RobotBall(int size, int valueMax, int value, ModelType type, Paint circlePaint) {
        this.type = type;
        this.size = size;
        this.originalSize = size;
        this.value = value;
        this.valueMax = valueMax;
        this.circlePaint = circlePaint;
        this.mediator = GameMediator.getInstance();
        this.models = mediator.getModels();
        this.initVelocity();
    }

    /**
     * Renders the ball on the canvas
     * If any of the conditions are true execute first
     * @param canvas
     */
    @Override
    public void render(Canvas canvas) {
        if (increasing) increaseBounds();
        if (decreasing) decreaseBounds();
        if (destroying) destroyBounds();
        this.updatePositions();
        canvas.drawCircle(circleX, circleY, radius, circlePaint);
    }

    /**
     * Gets the bounds of the ball
     * @return
     */
    @Override
    public Rect getBounds() {
        return bounds;
    }

    @Override
    public void setBounds(int x, int y) {
        this.setMax();
        this.bounds = new Rect(x, y, x + size, y + size);
        this.configureLocation();
    }

    /**
     * Gets the type of ball
     * @return
     */
    @Override
    public ModelType getType() {
        return type;
    }

    /**
     * Gets the size of the ball
     * @return
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * Gets the value of the ball
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * Configures the location of the ball
     */
    private void configureLocation() {
        xPosition = bounds.left;
        yPosition = bounds.top;
        radius = size / 2;
        circleX = bounds.left + radius;
        circleY = bounds.top + radius;
        xMax = mediator.getXMax() - size;
        yMax = mediator.getYMax() - size;
    }

    /**
     * Initializes the velocity of the ball
     */
    private void initVelocity() {
        Random random = new Random();
        int xDir = (random.nextFloat() > 0.5)? 1 : -1;
        int yDir = (random.nextFloat() > 0.5)? 1 : -1;
        xVelocity = random.nextFloat() * xDir;
        yVelocity = random.nextFloat() * yDir;
        frameTime = random.nextFloat() / 2;
    }

    /**
     * Increases the size of the ball
     * @param inflateValue
     */
    public void increaseSize(int inflateValue) {
        increaseValue = inflateValue;
        increasing = true;
    }

    /**
     * Decreases the size of the ball
     * @param reduceValue
     */
    public void decreaseSize(int reduceValue) {
        decreaseValue = reduceValue;
        decreasing = true;
    }

    /**
     * Tells the ball to self destruct
     */
    public void destroy() {
        destroying = true;
    }

    /**
     * Destroys the bounds in an animated way
     */
    private void destroyBounds() {
        size -= 4;
        this.setBounds(Math.round(xPosition), Math.round(yPosition));
        if (size <= 0) models.remove(this);
    }

    /**
     * Increases the bounds of the circle
     */
    private void increaseBounds() {
        if (!destroying && valueMax < value) {
            size++;
            value++;
            increaseValue--;
            if (increaseValue == 0) increasing = false;
            this.setBounds(Math.round(xPosition), Math.round(yPosition));
        }
    }

    /**
     * Decreases the bounds of the circle
     */
    private void decreaseBounds() {
        if (!destroying) {
            if (size-- < originalSize) {
                size = originalSize;
                decreasing = false;
            }
            value--;
            decreaseValue--;
            if (decreaseValue == 0) decreasing = false;
            this.setBounds(Math.round(xPosition), Math.round(yPosition));
        }
    }

    /**
     * Sets the xMax and yMax when a bound is changed.
     */
    private void setMax() {
        xMax = mediator.getXMax() - size;
        yMax = mediator.getYMax() - size;
    }

    /**
     * Updates the X and Y Positions everytime drawn
     */
    private void updatePositions() {
        Random random = new Random();

        //Calc distance travelled in that time
        float xS = ((xVelocity * 20) * frameTime);
        float yS = ((yVelocity * 20) * frameTime);

        //Add to position negative due to sensor
        //readings being opposite to what we want!
        xPosition -= xS;
        yPosition -= yS;

        if (xPosition > xMax) {
            xVelocity *= -1.2;
        }
        else if (xPosition < 0) {
            xVelocity *= -.8;
        }
        if (yPosition > yMax) {
            yVelocity *= -1.2;
        }
        else if (yPosition < 0) {
            yVelocity *= -.8;
        }
        if (xPosition > xMax || xPosition < 0) {
            xS = ((xVelocity) * frameTime);
            xPosition -= xS;
        }
        else if (yPosition > yMax || yPosition < 0) {
            yS = ((yVelocity) * frameTime);
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
        this.setBounds(Math.round(xPosition), Math.round(yPosition));
    }
}
