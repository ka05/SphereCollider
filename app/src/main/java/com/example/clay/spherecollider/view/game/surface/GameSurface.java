package com.example.clay.spherecollider.view.game.surface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import com.example.clay.spherecollider.view.game.management.GameLoop;
import com.example.clay.spherecollider.view.game.management.GameMediator;
import com.example.clay.spherecollider.view.game.management.LevelManager;
import com.example.clay.spherecollider.view.game.models.Ball;
import com.example.clay.spherecollider.view.game.models.GameModel;
import com.example.clay.spherecollider.view.game.models.Score;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Responsible for running the game off the main thread
 */
public class GameSurface extends SurfaceView {
    private GameMediator gameMediator;
    private SurfaceHolder holder;
    private LevelManager levelManager;
    private ConcurrentLinkedQueue models;
    private GameLoop gameLoop;
    private Ball gameBall;
    private Score gameScore;

    /**
     * Default constructor
     * Creates a holder callback to change the surface
     *
     * @param context
     */
    public GameSurface(Context context) {
        super(context);
        this.gameMediator = GameMediator.getInstance();
        this.gameLoop = new GameLoop(this);
        this.levelManager = new LevelManager();
        this.models = gameMediator.getModels();
        this.gameBall = gameMediator.getGameBall();
        this.gameScore = gameMediator.getGameScore();
        this.holder = getHolder();

        gameMediator.setSurface(this);

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoop.setRunning(true);
                gameLoop.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoop.setRunning(false);
                while (retry) {
                    try {
                        gameLoop.join();
                        retry = false;
                    } catch(InterruptedException ie) {}
                }
            }
        });
    }

    /**
     * Draws to the canvas
     * @param canvas
     */
    public void render(Canvas canvas) {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        canvas.drawColor(Color.WHITE);

        Iterator iterator = models.iterator();
        while (iterator.hasNext()) {
            GameModel model = (GameModel) iterator.next();
            model.render(canvas);
        }
        gameBall.render(canvas);
        gameScore.render(canvas);
    }

    public void gameOver() {
        gameLoop.setRunning(false);
    }
}
