package com.example.simpleapp.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.simpleapp.graphics.MapTiles;

class Base1 extends Tile {
    private final Bitmap sprite;

    public Base1(MapTiles mapTiles, Rect mapLocationRect) {
        super(mapLocationRect, TileType.BASE1);
        sprite = mapTiles.getBASE1Bitmap();
    }

    @Override
    public void draw(Canvas canvas) {
        //draw the sprite to the canvas
        canvas.drawBitmap(sprite, mapLocationRect.left, mapLocationRect.top, null);
    }
}
