package com.example.clay.spherecollider.view.game.management;

import android.graphics.Rect;
import android.os.Handler;

import com.example.clay.spherecollider.view.game.models.Ball;
import com.example.clay.spherecollider.view.game.models.GameModel;
import com.example.clay.spherecollider.view.game.models.Inflater;
import com.example.clay.spherecollider.view.game.models.ModelType;
import com.example.clay.spherecollider.view.game.models.Pause;
import com.example.clay.spherecollider.view.game.models.Point;
import com.example.clay.spherecollider.view.game.models.Reducer;
import com.example.clay.spherecollider.view.game.models.Score;
import com.example.clay.spherecollider.view.game.util.RandomUtility;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Responsible for getting the drawable objects from the database
 * Mapping the current bounds of the different objects on the board
 */
public class LevelManager implements Observer {
    private GameMediator gameMediator;
    private ModelManager modelManager;

    private ConcurrentLinkedQueue models, interactiveModels, inflaters, reducers, points;
    private Ball gameBall;
    private Score gameScore;
    private Rect ballRect;
    private int xMax, yMax, inflaterMax, reducerMax, pointMax, maxPoints;
    private Handler handler;
    private int maxScore = 0, score = 0, drawnPoints = 0;

    /**
     * Default constructor
     */
    public LevelManager() {
        this.gameMediator = GameMediator.getInstance();
        this.models = new ConcurrentLinkedQueue();
        this.gameMediator.setModels(models);
        this.handler = new Handler();
        this.modelManager = new ModelManager();
        this.modelManager.createLevelObjects();
        this.xMax = gameMediator.getXMax();
        this.yMax = gameMediator.getYMax();
        this.maxPoints = gameMediator.getMaxPoints();
        this.initializeLevelManager();
    }

    /**
     * Called when the sensor manager updates
     * @param observable
     * @param data
     */
    @Override
    public void update(Observable observable, Object data) {

        if (gameBall.isTooBig()) this.lost();
        if (points.size() == 0) this.determineWin();

        // Check for ball intersections and deal with appropriately
        Iterator iterator = interactiveModels.iterator();
        ballRect = gameBall.getBounds();
        while (iterator.hasNext()) {
            GameModel model = (GameModel)iterator.next();
            if (model == gameBall) continue;
            if (Rect.intersects(ballRect, model.getBounds())) {
                determineBallInteraction(model);
            }
        }

        // Check for interactive model intersections and deal with appropriately
        iterator = interactiveModels.iterator();
        while (iterator.hasNext()) {
            GameModel model = (GameModel)iterator.next();
            Iterator innerIterator = interactiveModels.iterator();
            while (innerIterator.hasNext()) {
                GameModel test = (GameModel)innerIterator.next();
                if (model == test) continue;
                if (Rect.intersects(model.getBounds(), test.getBounds())) {
                    if (model.getType() == ModelType.INFLATER) {
                        determineInflaterInteraction(((Inflater)model), test);
                    }
                    if (model.getType() == ModelType.REDUCER) {
                        determineReducerInteraction((Reducer)model, test);
                    }
                }
            }
        }
        // Tidy things up
        if (inflaters.size() < inflaterMax) createNewInflater();
        if (reducers.size() < reducerMax) createNewReducer();
        if (points.size() < pointMax) createNewPoint();
        gameScore.setScore(score);
    }

    /**
     * Determines what to do when the ball intersects with another object
     * @param model
     */
    private void determineBallInteraction(GameModel model) {
        ModelType type = model.getType();
        switch(type) {
            case INFLATER:
                gameBall.increaseSize(((Inflater)model).getValue());
                removeInflator(model);
                break;
            case REDUCER:
                gameBall.decreaseSize(((Reducer)model).getValue());
                removeReducer(model);
                break;
            case POINT:
                score += ((Point)model).getValue();
                removePoint(model);
                break;
            default:
                break;
        }
    }

    /**
     * Determines what to do with an inflater interaction
     * @param source
     * @param intersect
     */
    private void determineInflaterInteraction(Inflater source, GameModel intersect) {
        ModelType type = intersect.getType();
        switch(type) {
            case INFLATER:
                Inflater collision = (Inflater)intersect;
                if (source.getValue() > collision.getValue()) {
                    source.increaseSize(collision.getValue());
                    removeInflator(collision);
                } else {
                    collision.increaseSize(collision.getValue());
                    removeInflator(source);
                }
                break;
            case POINT:
                removePoint(intersect);
                break;
            default:
                break;
        }
    }

    /**
     * Determines the reducer interaction
     * @param source
     * @param intersect
     */
    private void determineReducerInteraction(Reducer source, GameModel intersect) {
        ModelType type = intersect.getType();
        switch(type) {
            case REDUCER:
                Reducer collision = (Reducer)intersect;
                if (source.getValue() > collision.getValue()) {
                    source.increaseSize(collision.getValue());
                    removeReducer(collision);
                } else {
                    collision.increaseSize(collision.getValue());
                    removeReducer(source);
                }
                break;
            case INFLATER:
                ((Inflater)intersect).decreaseSize(source.getValue());
                removeReducer(source);
                break;
            case POINT:
                removePoint(intersect);
                break;
            default:
                break;
        }
    }

    public void lost() {
        int percent = Math.round((((float)score) / (float)maxScore) * 100f);
        gameMediator.alertGameFinished(3, score, percent);
        gameMediator.getSurface().gameOver();
        gameMediator.getSensorHandler().stopSensorListener();
    }

    public void determineWin() {
        int percent = Math.round((((float)score) / (float)maxScore) * 100f);
        if (percent < 30) {
            gameMediator.alertGameFinished(2, score, percent);
        } else {
            gameMediator.alertGameFinished(1, score, percent);
        }
        gameMediator.getSurface().gameOver();
        gameMediator.getSensorHandler().stopSensorListener();
    }

    /**
     * Removes the Inflator from the manager
     * @param inflator
     */
    private void removeInflator(GameModel inflator) {
        ((Inflater)inflator).destroy();
        interactiveModels.remove(inflator);
        inflaters.remove(inflator);
    }

    /**
     * Removes the reducer from the manager
     * @param reducer
     */
    private void removeReducer(GameModel reducer) {
        ((Reducer)reducer).destroy();
        interactiveModels.remove(reducer);
        reducers.remove(reducer);
    }

    /**
     * Removes a point from the manager
     *
     * @param point
     */
    private void removePoint(GameModel point) {
        ((Point)point).destroy();
        interactiveModels.remove(point);
        points.remove(point);
    }

    /**
     * Initializes the level manager with the objects it needs to run the level
     */
    private void initializeLevelManager() {
        interactiveModels = new ConcurrentLinkedQueue();
        inflaters = new ConcurrentLinkedQueue();
        reducers = new ConcurrentLinkedQueue();
        points = new ConcurrentLinkedQueue();
        Iterator iterator = models.iterator();
        while (iterator.hasNext()) {
            GameModel model = (GameModel)iterator.next();
            if (model.getType() == ModelType.BALL) {
                gameBall = (Ball)model;
                gameMediator.getSensorHandler().addObserver((Ball) model);
                gameMediator.setGameBall(gameBall);
            } else if (model.getType() == ModelType.SCORE) {
                gameScore = (Score)model;
                gameMediator.setGameScore((Score)model);
            } else if (model.getType() == ModelType.PAUSE) {
                gameMediator.setPause((Pause)model);
                models.remove(model);
            }
            else if (model.getType() == ModelType.INFLATER) {
                inflaters.add(model);
            } else if (model.getType() == ModelType.REDUCER) {
                reducers.add(model);
            } else if (model.getType() == ModelType.POINT) {
                points.add(model);
                maxScore += ((Point)model).getValue();
                drawnPoints++;
            }
            if (model.getType() != ModelType.BACKGROUND && model.getType() != ModelType.BALL) {
                interactiveModels.add(model);
            }
        }
        models.remove(gameBall);
        models.remove(gameScore);
        inflaterMax = inflaters.size();
        reducerMax = reducers.size();
        pointMax = points.size();
        gameMediator.getSensorHandler().addObserver(this);
        gameMediator.setScore(score);
        gameMediator.setMaxScore(maxScore);
    }

    /**
     * Creates a new Inflater
     */
    private void createNewInflater() {
        int inflateValue = RandomUtility.randIntInRange(10, 40);
        int maxValue = inflateValue * 3;
        int size = Math.round(inflateValue * 2);
        Inflater inflater = new Inflater(size, maxValue, inflateValue, gameMediator.getInflaterColor());
        inflater = (Inflater)RandomUtility.randomizeLocation(inflater);
        models.add(inflater);
        interactiveModels.add(inflater);
        inflaters.add(inflater);
    }

    /**
     * Creates a new reducer
     */
    private void createNewReducer() {
        int reduceValue = RandomUtility.randIntInRange(10, 20);
        int maxValue = reduceValue * 3;
        int size = Math.round(reduceValue * 2);
        Reducer reducer = new Reducer(size, maxValue, reduceValue, gameMediator.getReducerColor());
        reducer = (Reducer)RandomUtility.randomizeLocation(reducer);
        models.add(reducer);
        interactiveModels.add(reducer);
        reducers.add(reducer);
    }

    /**
     * Creates a new point
     */
    private void createNewPoint() {
        if (drawnPoints < maxPoints) {
            int value = RandomUtility.randIntInRange(10, 30);
            maxScore += value;
            drawnPoints++;
            int size = value * 2;
            Point point = new Point(size, value, "#ffd600");
            point = (Point)RandomUtility.randomizeLocation(point);
            models.add(point);
            interactiveModels.add(point);
            points.add(point);
        }
    }
}
