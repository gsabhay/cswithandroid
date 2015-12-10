package com.google.engedu.puzzle8;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;


public class PuzzleBoard {

    private static final int NUM_TILES = 3;
    private static final int[][] NEIGHBOUR_COORDS = {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
    };
    private ArrayList<PuzzleTile> tiles  = new ArrayList<PuzzleTile>();
    public Bitmap bMap[][];
    PuzzleBoard(Bitmap bitmap, int parentWidth) {
        bMap =   splitBitmap(bitmap,NUM_TILES,NUM_TILES);

        for (int i=0; i<NUM_TILES; i++)
        {

            for (int j=0; j<NUM_TILES; j++)
            {
               // Bitmap tile = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false);

                tiles.add(new PuzzleTile(bMap[i][j], i * NUM_TILES + j));

            }
        }

        tiles.remove(NUM_TILES*NUM_TILES-1);
    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
    }

    public Bitmap[][] splitBitmap(Bitmap bitmap, int xCount, int yCount) {
        // Allocate a two dimensional array to hold the individual images.
        Bitmap[][] bitmaps = new Bitmap[xCount][yCount];
        int width, height;
        // Divide the original bitmap width by the desired vertical column count
        width = bitmap.getWidth() / xCount;
        // Divide the original bitmap height by the desired horizontal row count
        height = bitmap.getHeight() / yCount;
        // Loop the array and create bitmaps for each coordinate
        for(int x = 0; x < xCount; ++x) {
            for(int y = 0; y < yCount; ++y) {
                // Create the sliced bitmap
                bitmaps[x][y] = Bitmap.createBitmap(bitmap, x * width, y * height, width, height);
            }
        }
        // Return the array
        return bitmaps;
    }

    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null ) {
                tile.draw(canvas, i % NUM_TILES, i / NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    public ArrayList<PuzzleBoard> neighbours() {
        int i;
        ArrayList<PuzzleBoard> boards = new ArrayList<PuzzleBoard>();
        for( i=0; i<NUM_TILES * NUM_TILES; i++)
        {

                if(tiles.get(i) == null)
                    break;

        }
       Log.d("i:", String.valueOf(i));

        for (int j=0; j<NEIGHBOUR_COORDS.length; j++)
        {
            int x = NEIGHBOUR_COORDS[j][0] + i % NUM_TILES;
            int y = NEIGHBOUR_COORDS[j][1] + i / NUM_TILES;
            if( x >= 0 && x< NUM_TILES && y>=0 && y<NUM_TILES )
            {
                PuzzleBoard boardConf = new PuzzleBoard(this);
                boardConf.tiles.add(i, tiles.get(x * NUM_TILES + y));
                boardConf.tiles.remove(x * NUM_TILES + y);
                for( i=0; i<NUM_TILES * NUM_TILES; i++)
                {
                    if(boardConf.tiles.get(i) == null)
                        break;

                }
                Log.d("i:", String.valueOf(i));
                boards.add(boardConf);
            }
        }

        return boards;
    }

    public int priority() {
        return 0;
    }

}
