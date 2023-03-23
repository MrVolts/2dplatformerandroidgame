package com.example.simpleapp.map;

import java.util.Random;


public class MapLayout {
    public static final int TILE_WIDTH_PIXELS = 96;
    public static final int TILE_HEIGHT_PIXELS = 96;
    public static final int NUMBER_OF_ROW_TILES = 200;
    public static final int NUMBER_OF_COLUMN_TILES = 200;
    private static final int BASE1 = 0;
    private static final int BASE2 = 1;
    private static final int BASE3 = 2;

    private int[][] layout;

    public MapLayout() {
        initialiseLayout();
    }

    public int[][] getLayout() {
        return layout;
    }
    // This method is called from the constructor to intialise the layout and procedurally generate the map
    public void initialiseLayout() {
        layout = new int[NUMBER_OF_ROW_TILES][NUMBER_OF_COLUMN_TILES];
        OpenSimplex2 noise = new OpenSimplex2();
        Random random = new Random();
        long seed = random.nextLong();
        double scale = 0.1;

        for (int y = 0; y < NUMBER_OF_ROW_TILES; y++) {
            for (int x = 0; x < NUMBER_OF_COLUMN_TILES; x++) {
                float value = OpenSimplex2.noise2(seed, x * scale, y * scale);

                if (value < -0.2) {
                    layout[y][x] = BASE1;
                } else if (value < 0.2) {
                    layout[y][x] = BASE2;
                } else {
                    layout[y][x] = BASE3;
                }
            }
        }
    }
}