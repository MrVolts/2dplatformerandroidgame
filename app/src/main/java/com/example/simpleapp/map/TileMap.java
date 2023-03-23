package com.example.simpleapp.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.simpleapp.GameDisplay;
import com.example.simpleapp.graphics.MapTiles;


public class TileMap {

    private final MapLayout maplayout;
    private final MapTiles mapTiles;
    private Tile[][] tilemap;
    private Bitmap mapBitmaps;

    public TileMap(MapTiles mapTiles) {
        maplayout = new MapLayout();
        this.mapTiles = mapTiles;
        initialiseTilemap();
    }
    // This method is called from the constructor to intialise the tilemap
    private void initialiseTilemap() {
        int[][] layout = maplayout.getLayout();
        tilemap = new Tile[MapLayout.NUMBER_OF_ROW_TILES][MapLayout.NUMBER_OF_COLUMN_TILES];
        for (int iRow = 0; iRow < MapLayout.NUMBER_OF_ROW_TILES; iRow++) {
            for (int iColumn = 0; iColumn < MapLayout.NUMBER_OF_COLUMN_TILES; iColumn++) {
                tilemap[iRow][iColumn] = Tile.getTile(layout[iRow][iColumn], mapTiles, getRectByIndex(iRow, iColumn));
            }
        }
        // Create a bitmap to draw the map on in 8 bit color
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        mapBitmaps = Bitmap.createBitmap(
                MapLayout.NUMBER_OF_COLUMN_TILES * MapLayout.TILE_WIDTH_PIXELS,
                MapLayout.NUMBER_OF_ROW_TILES * MapLayout.TILE_HEIGHT_PIXELS,
                config
        );
        // Create a canvas to draw the map on
        Canvas mapCanvas = new Canvas(mapBitmaps);

        for (int iRow = 0; iRow < MapLayout.NUMBER_OF_ROW_TILES; iRow++) {
            for (int iColumn = 0; iColumn < MapLayout.NUMBER_OF_COLUMN_TILES; iColumn++) {
                tilemap[iRow][iColumn].draw(mapCanvas);
            }
        }
    }

    //
    private Rect getRectByIndex(int idxRow, int idxCol) {
        return new Rect(
                idxCol * MapLayout.TILE_WIDTH_PIXELS,
                idxRow * MapLayout.TILE_HEIGHT_PIXELS,
                (idxCol + 1) * MapLayout.TILE_WIDTH_PIXELS,
                (idxRow + 1) * MapLayout.TILE_HEIGHT_PIXELS
        );
    }

    public Tile.TileType getTileTypeAt(float x, float y) {
        int row = (int) (y / MapLayout.TILE_HEIGHT_PIXELS);
        int col = (int) (x / MapLayout.TILE_WIDTH_PIXELS);

        if (row >= 0 && row < MapLayout.NUMBER_OF_ROW_TILES && col >= 0 && col < MapLayout.NUMBER_OF_COLUMN_TILES) {
            return tilemap[row][col].tileType;
        }

        return null;
    }

    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        canvas.drawBitmap(mapBitmaps,
                gameDisplay.getGameRect(),
                gameDisplay.DISPLAY_RECT,
                null);
    }
}
