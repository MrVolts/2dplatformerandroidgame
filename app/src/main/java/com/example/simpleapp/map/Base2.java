package com.example.simpleapp.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.simpleapp.graphics.MapTiles;
/**
 * Base2 class
 *
 * This class is responsible for drawing the Base2 map tiles
 */

class Base2 extends Tile {
    private final Bitmap sprite;

    // Constructor takes the map tiles and the map location rectangle
    public Base2(MapTiles mapTiles, Rect mapLocationRect) {
        super(mapLocationRect, TileType.BASE2);
        sprite = mapTiles.getBASE2Bitmap();
    }

    // Draw the sprite to the canvas at a tile location
    @Override
    public void draw(Canvas canvas) {
        //draw the sprite to the canvas
        canvas.drawBitmap(sprite, mapLocationRect.left, mapLocationRect.top, null);
    }
}