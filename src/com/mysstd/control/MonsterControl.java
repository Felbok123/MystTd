package com.mysstd.control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.mysttd.GameScene.GameWorld;

public class MonsterControl extends AbstractControl {

    private long initialTime;
    private long currentTime;
    private GameWorld currentGame;

    public MonsterControl(GameWorld gameWorld) {
        initialTime = System.currentTimeMillis();
        currentGame = gameWorld;

    }

    @Override
    protected void controlUpdate(float tpf) {
        if (currentGame.isIsGamePaused()) {
            return;
        }
        if (getHealth() > 0) {
            currentTime = System.currentTimeMillis();
            if (currentTime - initialTime >= 30) {

                initialTime = System.currentTimeMillis();
            }
        }
        if (getHealth() <= 0) {
            currentGame.increaseGold((int) spatial.getUserData("gold"));
            spatial.removeFromParent();
        }
    }

    public int getHealth() {
        return spatial.getUserData("health");
    }

    public void decreaseHealth(int amount) {
        if (amount < 0) {
            throw new IllegalStateException("Amount to decrease should be specified as a positive int.");
        }

        spatial.setUserData("health", getHealth() - amount);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
