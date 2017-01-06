package com.mysstd.control;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Line;
import com.mysttd.GameScene.GameWorld;

public class TowerControl extends AbstractControl {

    private GameWorld gameState;
    private Node beamNode;
    private Spatial currentEnemy;

    public TowerControl(GameWorld gameState) {
        this.gameState = gameState;
        beamNode = gameState.getNode("beam");
    }

    @Override
    protected void controlUpdate(float tpf) {

        for (Spatial enemy : gameState.getNode("enemy").getChildren()) {
            if (enemy.getLocalTranslation().getZ() - spatial.getLocalTranslation().getZ() < 10 || enemy.getLocalTranslation().getX() - spatial.getLocalTranslation().getX() < 10) {
                attackEnemy(enemy);
            }
        }
    }

    private void attackEnemy(Spatial newEnemy) {
        if (currentEnemy == null) {
            currentEnemy = newEnemy;

        }

        shootBeam();
        if (currentEnemy.getControl(MonsterControl.class) != null) {
            if (currentEnemy.getControl(MonsterControl.class).getHealth() <= 0) {
                currentEnemy = null;
                beamNode.detachAllChildren();
            }

        } else {
            currentEnemy = null;
            beamNode.detachAllChildren();
        }
    }

    public void shootBeam() {
        Vector3f beamStartLocation = new Vector3f(spatial.getLocalTranslation().getX(), getTowerHeight(), spatial.getLocalTranslation().getZ());
        beamNode.attachChild(getBeam(beamStartLocation, currentEnemy.getLocalTranslation()));
        currentEnemy.getControl(MonsterControl.class).decreaseHealth((int) spatial.getUserData("damage"));



    }

    public Geometry getBeam(Vector3f start, Vector3f end) {
        Line lineMesh = new Line(start, end);
        Geometry line = new Geometry("line", lineMesh);
        Material mat = gameState.getUnshadedMaterial();

        if (spatial.getUserData("type").equals("laser")) {
            mat.setColor("Color", ColorRGBA.Red);
        } else {
            mat.setColor("Color", ColorRGBA.Yellow);
        }

        line.setMaterial(mat);

        return line;
    }

    public float getTowerHeight() {
        return 16f;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
