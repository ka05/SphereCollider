package com.example.clay.spherecollider.view.game.models;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.example.clay.spherecollider.view.game.management.GameMediator;
import com.example.clay.spherecollider.view.game.util.PaintUtility;


/**
 * Created by wyattmcbain on 5/17/15.
 */
public class Score implements GameModel {
    private GameMediator gameMediator;
    private Paint backgroundPaint, textPaint;
    private ModelType type;

    private int xMax, yMax;
    private String scoreText = "";
    private Rect bounds;

    public Score() {
        type = ModelType.SCORE;
        gameMediator = GameMediator.getInstance();
        xMax = gameMediator.getXMax();
        yMax = gameMediator.getYMax();
        this.initializePaint();
        this.initializeRect();
    }

    @Override
    public void render(Canvas canvas) {
        canvas.drawRect(bounds, backgroundPaint);
        canvas.drawText(scoreText, xMax - 120, 30, textPaint);
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
        bounds = new Rect(xMax - 140, 0, xMax, 50);
    }

    private void initializePaint() {
        backgroundPaint = PaintUtility.configurePaintWithAlpha("#333333", 0.8f);
        textPaint = PaintUtility.configurePaintForText("#ffffff", 18);
        Typeface typeface = Typeface.create("Helvetica", Typeface.BOLD);
        textPaint.setTypeface(typeface);
    }

    public void setScore(int score) {
        scoreText = "Score: " + String.valueOf(score);
    }
}
