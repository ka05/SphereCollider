package com.example.clay.spherecollider.view.game.util;

import android.graphics.Rect;


import com.example.clay.spherecollider.view.game.management.GameMediator;
import com.example.clay.spherecollider.view.game.models.GameModel;
import com.example.clay.spherecollider.view.game.models.ModelType;
import com.example.clay.spherecollider.view.game.models.RandomizedModel;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by wyattmcbain on 5/16/15.
 */
public class RandomUtility {

    /**
     * Randomizes the starting location for a model
     * Checks to see if it intersects with any other models on the board
     *
     * @param model
     * @return
     */
    public static GameModel randomizeLocation(RandomizedModel model) {
        GameMediator mediator = GameMediator.getInstance();
        ConcurrentLinkedQueue models = mediator.getModels();
        int xMax = mediator.getXMax();
        int yMax = mediator.getYMax();
        Random rn = new Random();
        Rect rect = null;
        int size = model.getSize();
        int xBound = xMax - size;
        int yBound = yMax - size;
        boolean random = true;

        do {
            Iterator iterator = models.iterator();
            int x = rn.nextInt(xBound);
            int y = rn.nextInt(yBound);
            rect = new Rect(x, y, x + size, y + size);

            while (iterator.hasNext()) {
                GameModel test = (GameModel)iterator.next();
                if (test.getType() == ModelType.BACKGROUND) continue;
                if (Rect.intersects(rect, test.getBounds())) {
                    random = false;
                    break;
                }
                random = true;
            }
        } while(!random);
        model.setBounds(rect.left, rect.top);
        return (GameModel)model;
    }

    /**
     * returns a random int within a range
     * @param min
     * @param max
     * @return
     */
    public static int randIntInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}
