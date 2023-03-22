package com.example.simpleapp.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.simpleapp.R;

public class MapTiles {
    private final Bitmap BASE1Bitmap;
    private final Bitmap BASE2Bitmap;
    private final Bitmap BASE3Bitmap;

    public MapTiles(Context context) {
        BASE1Bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sand);
        BASE2Bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.water);
        BASE3Bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.grass);
    }

    public Bitmap getBASE1Bitmap() {
        return BASE1Bitmap;
    }

    public Bitmap getBASE2Bitmap() {
        return BASE2Bitmap;
    }

    public Bitmap getBASE3Bitmap() {
        return BASE3Bitmap;
    }
}

