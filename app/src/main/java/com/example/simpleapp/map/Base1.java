package com.example.simpleapp.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.simpleapp.graphics.MapTiles;
/**
 * Base1 class
 *
 * This class is responsible for drawing the Base1 map tiles
 */

class Base1 extends Tile {
    private final Bitmap sprite;

    // Constructor takes the map tiles and the map location rectangle
    public Base1(MapTiles mapTiles, Rect mapLocationRect) {
        super(mapLocationRect, TileType.BASE1);
        sprite = mapTiles.getBASE1Bitmap();
    }

    // Draw the sprite to the canvas
    @Override
    public void draw(Canvas canvas) {
        //draw the sprite to the canvas
        canvas.drawBitmap(sprite, mapLocationRect.left, mapLocationRect.top, null);
    }
}
