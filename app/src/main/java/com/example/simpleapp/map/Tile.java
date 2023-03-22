package com.example.simpleapp.map;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.simpleapp.graphics.MapTiles;

public abstract class Tile {

    protected final Rect mapLocationRect;
    protected final TileType tileType;

    public Tile(Rect mapLocationRect, TileType tileType) {
        this.mapLocationRect = mapLocationRect;
        this.tileType = tileType;
    }

    public static Tile getTile(int idxTileType, MapTiles mapTiles, Rect mapLocationRect) {

        switch (TileType.values()[idxTileType]) {
            case BASE1:
                return new Base1(mapTiles, mapLocationRect);
            case BASE2:
                return new Base2(mapTiles, mapLocationRect);
            case BASE3:
                return new Base3(mapTiles, mapLocationRect);
            default:
                return null;
        }
    }

    public abstract void draw(Canvas canvas);

    public enum TileType {
        BASE2,
        BASE1,
        BASE3
    }
}
