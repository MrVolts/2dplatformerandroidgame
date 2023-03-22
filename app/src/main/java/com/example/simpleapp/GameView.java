package com.example.simpleapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.simpleapp.Object.Enemy;
import com.example.simpleapp.Object.Player;
import com.example.simpleapp.Object.Spell;
import com.example.simpleapp.gamepanel.GameOver;
import com.example.simpleapp.gamepanel.Joystick;
import com.example.simpleapp.gamepanel.Stats;
import com.example.simpleapp.graphics.MapTiles;
import com.example.simpleapp.map.TileMap;

import java.util.ArrayList;
import java.util.List;

/**
 * GameView is responsible for drawing and updating all objects to the screen.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private final TileMap tilemap;
    private final Joystick joystick;
    private final List<Enemy> enemies = new ArrayList<Enemy>();
    private final List<Spell> spells = new ArrayList<>();
    private final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
    private final int screenWidth = displayMetrics.widthPixels;
    private final int screenHeight = displayMetrics.heightPixels;
    private final GameOver gameOver;
    private MainThread thread;
    private MapTiles mapTiles;
    private Player player;
    private Stats stats;
    private GameDisplay gameDisplay;


    public GameView(Context context) {
        super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        thread = new MainThread(surfaceHolder, this);
        stats = new Stats(thread);
        joystick = new Joystick(200, screenHeight - 200, 200, 100);
        MapTiles mapTiles = new MapTiles(context);
        tilemap = new TileMap(mapTiles);
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.playership), 9600, 9600, 150, 150, getContext(), tilemap);
        gameDisplay = new GameDisplay(screenWidth, screenHeight, player);
        gameOver = new GameOver(context, screenWidth, screenHeight);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (thread.getState().equals(Thread.State.TERMINATED)) {
            SurfaceHolder surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            thread = new MainThread(surfaceHolder, this);
        }
        thread.startLoop();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // If the player is dead, the game is over and the player can only restart the game
        if (player.getHealthPoints() <= 0) {
            return gameOver.onTouchEvent(event, this);
        }

        int pointerCount = event.getPointerCount();

        for (int i = 0; i < pointerCount; i++) {
            MotionEvent.PointerCoords pointerCoords = new MotionEvent.PointerCoords();
            event.getPointerCoords(i, pointerCoords);
            float x = pointerCoords.x;
            float y = pointerCoords.y;

            int action = event.getActionMasked();
            int pointerIndex = event.getActionIndex();

            if (i == pointerIndex) {
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        joystick.setActuatorPos(x, y);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_CANCEL:
                        joystick.resetActuator();
                        break;
                }
            }
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // Draw the background (tilemap)
        tilemap.draw(canvas, gameDisplay);
        // Draws the sprite to the screen
        if (canvas != null) {
            player.draw(canvas, gameDisplay);
            joystick.draw(canvas);
            // Iterate through the enemies list and draw each one
            for (Enemy enemy : enemies) {
                enemy.draw(canvas, gameDisplay);
            }
            for (Spell spell : spells) {
                spell.draw(canvas, gameDisplay);
            }
        }
        // Draws the FPS/UPS to the screen
        stats.draw(canvas);
        // Draw game over if player health is 0
        if (player.getHealthPoints() <= 0) {
            gameOver.draw(canvas, stats);
        }
    }


    public void update() {

        // Stop updating if the player is dead
        if (player.getHealthPoints() <= 0) {
            return;
        }

        if (player.shouldFireSpell()) {
            fireSpell();
        }

        // Update the player
        player.update(joystick);

        // Spawn an enemy if it is ready to spawn
        if (Enemy.readyToSpawn()) {
            enemies.add(Enemy.spawnEnemy(getContext(), player, gameDisplay, R.drawable.scoutdrone, 124, 78));
        }

        // Update enemies and remove them if they collide with the player or are out of bounds
        Enemy.updateAll(enemies, player, gameDisplay);

        // Update spells and remove them if they collide with the enemies or are out of bounds
        Spell.updateAll(spells, enemies, gameDisplay, stats); // Pass stats to updateAll()

        // Update game display
        gameDisplay.update();

        stats.updateScore();

        stats.updateScore();

        if (player.getHealthPoints() <= 0) {
            stats.setGameOver(true);
            stats.setEndTime(System.currentTimeMillis());
        }
    }

    public void pause() {
        thread.stopLoop();
    }

    public void restart() {
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        thread.stopLoop();
        thread = new MainThread(surfaceHolder, this);

        // Re-initialize game objects
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.playership), 9600, 9600, 150, 150, getContext(), tilemap);
        enemies.clear();
        spells.clear();
        stats = new Stats(thread);
        gameDisplay = new GameDisplay(screenWidth, screenHeight, player);
        Enemy.resetWaveInfo();

        thread.startLoop();
    }

    private void fireSpell() {
        double angle = joystick.getLastAngle();
        int spellWidth = 50; // Replace with desired width
        int spellHeight = 50; // Replace with desired height

        // Calculate the spawn position of the spell
        float offsetX = (float) (Math.cos(angle) * (player.getWidth() / 2.0f));
        float offsetY = (float) (Math.sin(angle) * (player.getHeight() / 2.0f));

        float spawnX = player.getPositionX() + offsetX;
        float spawnY = player.getPositionY() + offsetY;

        spells.add(new Spell(BitmapFactory.decodeResource(getResources(), R.drawable.spell),
                (int) spawnX - (spellWidth / 2), (int) spawnY - (spellHeight / 2),
                spellWidth, spellHeight, angle, 30));
    }

}
