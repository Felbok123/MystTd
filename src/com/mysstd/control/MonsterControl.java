package com.mysstd.control;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.mysttd.GameScene.GameWorld;
import java.util.LinkedList;

public class MonsterControl extends AbstractControl {

    private long initialTime;
    private long currentTime;
    GameWorld currentGame;
    private LinkedList<Vector3f> movements;

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
                //   moveEnemyForward();


                initialTime = System.currentTimeMillis();
            }
        }
        if (getHealth() <= 0) {
            currentGame.increaseGold((int) spatial.getUserData("gold"));
            spatial.removeFromParent();


        }




    }

    private void moveEnemyForward() {
        movements = new LinkedList<>();
        movements.add(new Vector3f(-88.547516f, 0.5f, -17.409536f));
        movements.add(new Vector3f(-84.83237f, 0.5f, -62.80838f));
        movements.add(new Vector3f(-45.021725f, 0.5f, -63.23368f));
        movements.add(new Vector3f(-44.718445f, 0.5f, -18.281975f));
        movements.add(new Vector3f(-69.89613f, 0.5f, -17.996004f));
        movements.add(new Vector3f(-77.73884f, 0.5f, 66.10136f));
        movements.add(new Vector3f(17.973166f, 0.5f, 67.42557f));
        movements.add(new Vector3f(20.869114f, 0.5f, 31.84087f));
        movements.add(new Vector3f(-17.685957f, 0.5f, 31.998701f));
        movements.add(new Vector3f(-16.710148f, 0.5f, -62.578075f));
        movements.add(new Vector3f(24.27733f, 0.5f, -61.662155f));
        movements.add(new Vector3f(24.228615f, 0.5f, -13.684091f));
        movements.add(new Vector3f(52.40349f, 0.5f, -15.427767f));





        Vector3f wp1 = new Vector3f(-88.547516f, 0.5f, -17.409536f);
        Vector3f wp2 = new Vector3f(-84.83237f, 0.5f, -62.80838f);
        Vector3f wp3 = new Vector3f(-45.021725f, 0.5f, -63.23368f);
        Vector3f wp4 = new Vector3f(-44.718445f, 0.5f, -18.281975f);
        Vector3f wp5 = new Vector3f(-88.547516f, 0.5f, -17.409536f);

        Vector3f motion1 = new Vector3f();
        motion1.interpolate(wp1, wp2, 500);
        // spatial.move(motion1);
        spatial.move(0.5f, 0, 0);
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
