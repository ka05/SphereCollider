package com.example.clay.spherecollider.view.game.models;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by wyattmcbain on 5/14/15.
 */
public interface GameModel {
    void render(Canvas canvas);
    Rect getBounds();
    ModelType getType();
}
