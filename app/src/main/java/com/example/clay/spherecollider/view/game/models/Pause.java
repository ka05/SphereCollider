package com.example.clay.spherecollider.view.game.models;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;


import com.example.clay.spherecollider.view.game.management.GameMediator;
import com.example.clay.spherecollider.view.game.util.PaintUtility;


/**
 * Class for pause button drawn on canvas on surface view
 */
public class Pause implements GameModel {
    private GameMediator gameMediator;
    private Paint backgroundPaint, textPaint;
    private ModelType type;

    private int xMax, yMax;
    private int circleX, circleY, radius;
    private Rect bounds;

    public Pause() {
        type = ModelType.PAUSE;
        gameMediator = GameMediator.getInstance();
        xMax = gameMediator.getXMax();
        yMax = gameMediator.getYMax();
        this.initializePaint();
        this.initializeRect();
    }

    @Override
    public void render(Canvas canvas) {
        canvas.drawCircle(circleX, circleY, radius, backgroundPaint);
        canvas.drawText("| |", 38, yMax - 40, textPaint); // "l l" => looks like pause button sort of.
                                                           // IDK you can think of a better idea if you want
    }

    @Override
    public Rect getBounds() {
        return bounds;
    }

    @Override
    public ModelType getType() {
        return type;
    }

    private void initializeRect() {
        bounds = new Rect(20, yMax - 70, 70, yMax - 20);
        radius = (bounds.right - bounds.left) / 2;
        circleX = bounds.left + radius;
        circleY = bounds.top + radius;
    }

    private void initializePaint() {
        backgroundPaint = PaintUtility.configurePaintWithAlpha("#333333", 0.8f);
        textPaint = PaintUtility.configurePaintForText("#ffffff", 18);
        Typeface typeface = Typeface.create("Helvetica", Typeface.BOLD);
        textPaint.setTypeface(typeface);
    }
}