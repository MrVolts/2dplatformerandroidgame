package com.example.simpleapp.Object;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.simpleapp.GameDisplay;
import com.example.simpleapp.MainThread;

import java.util.List;

public class Enemy extends GameObject {
    // Needs to be a larger multiplier than the player's speed otherwise very slow
    private static final double SPEED_PIXELS_PER_SECOND = Player.SPEED_PIXELS_PER_SECOND * 30;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / MainThread.MAX_UPS;
    private static final double SPEED_SCALAR_PER_WAVE = 1.1; // Adjust this value to change the speed increase per wave
    private static final double DAMAGE_SCALAR_PER_WAVE = 1.1; // Adjust this value to change the damage increase per wave
    private static final double ENEMY_COUNT_SCALAR_PER_WAVE = 1.1; // Adjust this value to change the enemy count increase per wave
    private static final double SPAWN_RATE_SCALAR_PER_WAVE = 1.1; // Adjust this value to change the spawn rate increase per wave
    private static double SPAWNS_PER_MINUTE = 20;
    private static double SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE / 60;
    private static double updatesPerSpawn = MainThread.MAX_UPS / SPAWNS_PER_SECOND;
    private static double updatesUntilSpawn = updatesPerSpawn;
    private static int waveSize = 5;
    private static int enemiesRemainingInWave = waveSize;
    private static double speedScalar = 1.0;
    private static double damageScalar = 1.0;
    private static int waveNumber = 1;
    private final Player player;

    // Constructor for Enemy class that takes in a bitmap
    public Enemy(Context context, Player player, int resourceId, int x, int y, int desiredWidth, int desiredHeight) {
        super(BitmapFactory.decodeResource(context.getResources(), resourceId), x, y, desiredWidth, desiredHeight);
        this.player = player;
    }

    // getter for wave number
    public static int getWaveNumber() {
        return waveNumber;
    }

    // getter for enemies remaining in wave
    public static int getEnemiesRemainingInWave() {
        return enemiesRemainingInWave;
    }

    //getter for wave size
    public static int getWaveSize() {
        return waveSize;
    }

    // Resets wave info on game restart
    public static void resetWaveInfo() {
        waveSize = 5;
        enemiesRemainingInWave = waveSize;
        speedScalar = 1.0;
        damageScalar = 1.0;
        waveNumber = 1;
    }

    // Checks if the an enemy should spawn
    public static boolean readyToSpawn() {
        if (enemiesRemainingInWave <= 0) {
            return false;
        }

        if (updatesUntilSpawn <= 0) {
            updatesUntilSpawn += updatesPerSpawn;
            enemiesRemainingInWave--;
            return true;
        } else {
            updatesUntilSpawn--;
            return false;
        }
    }

    // Spawns an enemy randomly on the bounds of the screen
    public static Enemy spawnEnemy(Context context, Player player, GameDisplay gameDisplay, int resourceId, int desiredWidth, int desiredHeight) {
        // Choose a random border (0: top, 1: right, 2: bottom, 3: left)
        int border = (int) (Math.random() * 4);

        double spawnX, spawnY;
        double displayCentreX = gameDisplay.getDisplayCentreX();
        double displayCentreY = gameDisplay.getDisplayCentreY();
        double screenWidth = 2 * displayCentreX;
        double screenHeight = 2 * displayCentreY;

        switch (border) {
            case 0: // top
                spawnX = Math.random() * screenWidth;
                spawnY = displayCentreY - screenHeight / 2;
                break;
            case 1: // right
                spawnX = displayCentreX + screenWidth / 2;
                spawnY = Math.random() * screenHeight;
                break;
            case 2: // bottom
                spawnX = Math.random() * screenWidth;
                spawnY = displayCentreY + screenHeight / 2;
                break;
            default: // left
                spawnX = displayCentreX - screenWidth / 2;
                spawnY = Math.random() * screenHeight;
                break;
        }

        // Convert spawnX and spawnY to game coordinates
        double gameSpawnX = gameDisplay.displayToGameCoordinatesX(spawnX);
        double gameSpawnY = gameDisplay.displayToGameCoordinatesY(spawnY);

        return new Enemy(context, player, resourceId, (int) gameSpawnX, (int) gameSpawnY, desiredWidth, desiredHeight);
    }

    // Preps the next wave
    public static void resetWave() {
        waveSize = (int) Math.ceil(waveSize * ENEMY_COUNT_SCALAR_PER_WAVE);
        enemiesRemainingInWave = waveSize;
        speedScalar *= SPEED_SCALAR_PER_WAVE;
        damageScalar *= DAMAGE_SCALAR_PER_WAVE;
        waveNumber++;

        // Increase the spawn rate for the next wave
        SPAWNS_PER_MINUTE *= SPAWN_RATE_SCALAR_PER_WAVE;
        SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE / 60;
        updatesPerSpawn = MainThread.MAX_UPS / SPAWNS_PER_SECOND;
    }

    // Updates all enemies and handles player damage
    public static void updateAll(List<Enemy> enemies, Player player, GameDisplay gameDisplay) {
        int baseDamage = 10;
        int damage = (int) (baseDamage * damageScalar);

        enemies.removeIf(enemy -> {
            enemy.update(player);
            boolean isCollidingWithPlayer = enemy.isColliding(enemy, player);

            if (isCollidingWithPlayer) {
                player.takeDamage(damage);
            }

            boolean isOutOfBounds = enemy.isOutOfBounds(gameDisplay, 10f);

            return isCollidingWithPlayer || isOutOfBounds;
        });

        if (enemies.isEmpty() && enemiesRemainingInWave == 0) {
            resetWave();
            player.restoreHealthAfterWave(); // Restore player's health after the wave is finished
        }
    }

    // Updates the enemy's position
    public void update(Player player) {
        double distance = getDistance(this, player);
        if (distance > 0) {
            double directionX = (player.getPositionX() - posX) / distance;
            double directionY = (player.getPositionY() - posY) / distance;

            xVelocity = directionX * MAX_SPEED * speedScalar;
            yVelocity = directionY * MAX_SPEED * speedScalar;
        } else {
            xVelocity = 0;
            yVelocity = 0;
        }

        posX += (float) xVelocity;
        posY += (float) yVelocity;
    }
}

