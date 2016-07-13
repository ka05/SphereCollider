package com.example.clay.spherecollider.view.game.models;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


import com.example.clay.spherecollider.view.game.management.GameMediator;
import com.example.clay.spherecollider.view.game.util.PaintUtility;

import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Responsible for the coin sprite and emulating animation
 */
public class Point implements GameModel, RandomizedModel {
    private GameMediator gameMediator;
    private Rect bounds;
    private Paint circlePaint;
    private ModelType type;
    private ConcurrentLinkedQueue models;

    private int value;
    private int circleX, circleY, radius;
    private int size;

    private boolean destroying;

    /**
     * Default constructor
     *
     * @param size
     * @param value
     * @param color
     */
    public Point(int size, int value, String color)  {
        this.type = ModelType.POINT;
        this.gameMediator = GameMediator.getInstance();
        this.models = gameMediator.getModels();
        this.size = size;
        this.value = value;
        this.circlePaint = PaintUtility.configurePaintForCircle(color);
    }

    /**
     * Renders the point on the canvas
     * @param canvas
     */
    @Override
    public void render(Canvas canvas) {
        if (destroying) destroyBounds();
        canvas.drawCircle(circleX, circleY, radius, circlePaint);
    }

    /**
     * Gets the bounds
     * @return
     */
    @Override
    public Rect getBounds() {
        return bounds;
    }

    /**
     * Gets the type
     * @return
     */
    @Override
    public ModelType getType() {
        return type;
    }

    /**
     * Sets the bounds
     * @param x
     * @param y
     */
    @Override
    public void setBounds(int x, int y) {
        this.bounds = new Rect(x, y, x + size, y + size);
        this.configureLocation();
    }

    /**
     * Gets the size
     * @return
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * Gets the value
     * @return
     */
    public int getValue() {
        return value;
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
        this.setBounds(bounds.left, bounds.top);
        if (size <= 0) models.remove(this);
    }

    /**
     * Configures the location of the ball
     */
    private void configureLocation() {
        radius = size / 2;
        circleX = bounds.left + radius;
        circleY = bounds.top + radius;
    }
}
