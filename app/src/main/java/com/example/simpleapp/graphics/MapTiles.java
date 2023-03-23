package com.example.simpleapp.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.simpleapp.R;

/**
 * MapTiles class
 *
 * This class is responsible for loading the map tiles
 */

public class MapTiles {
    private final Bitmap BASE1Bitmap;
    private final Bitmap BASE2Bitmap;
    private final Bitmap BASE3Bitmap;

    // Constructor takes the context and loads the map tiles
    public MapTiles(Context context) {
        BASE1Bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sand);
        BASE2Bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.water);
        BASE3Bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.grass);
    }

    // Getters for the BASE1 map tiles
    public Bitmap getBASE1Bitmap() {
        return BASE1Bitmap;
    }

    // Getters for the BASE2 map tiles
    public Bitmap getBASE2Bitmap() {
        return BASE2Bitmap;
    }

    // Getters for the BASE3 map tiles
    public Bitmap getBASE3Bitmap() {
        return BASE3Bitmap;
    }
}

