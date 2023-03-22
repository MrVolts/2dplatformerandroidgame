package com.example.simpleapp.Object;

import android.graphics.Bitmap;

import com.example.simpleapp.GameDisplay;
import com.example.simpleapp.gamepanel.Stats;

import java.util.List;
import java.util.Optional;

public class Spell extends GameObject {
    private final double speed;

    public Spell(Bitmap bmp, int x, int y, int desiredWidth, int desiredHeight, double angle, double speed) {
        super(bmp, x, y, desiredWidth, desiredHeight);
        this.speed = speed;
        this.xVelocity = speed * Math.cos(angle);
        this.yVelocity = speed * Math.sin(angle);
    }

    public static void updateAll(List<Spell> spells, List<Enemy> enemies, GameDisplay gameDisplay, Stats stats) {
        spells.removeIf(spell -> {
            spell.update();
            if (spell.isOutOfBounds(gameDisplay, 1.5f)) {
                return true;
            }

            // Remove the collided enemy and the spell if a collision occurs
            Optional<Enemy> collidedEnemy = enemies.stream()
                    .filter(enemy -> spell.isColliding(spell, enemy))
                    .findFirst();

            if (collidedEnemy.isPresent()) {
                enemies.remove(collidedEnemy.get());
                stats.addScore(10); // Add 10 points to the score when an enemy is destroyed
                stats.incrementEnemiesKilled(); // Increment the enemiesKilled counter
            }
            return collidedEnemy.isPresent();
        });
    }

    public void update() {
        posX += xVelocity;
        posY += yVelocity;
    }
}

