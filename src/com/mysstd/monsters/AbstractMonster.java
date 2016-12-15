/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysstd.monsters;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Odium
 */
public abstract class AbstractMonster {

    private Spatial monsterModel;
    private int monsterCount;

    protected abstract void initEnemyModel(AssetManager assetManager, Node rootNode);

    public void moveMonster(Vector3f wu) {
        monsterModel.move(wu);
    }

    public void moveFromPoint(Vector3f wu) {
        monsterModel.setLocalTranslation(wu);
    }

    public Vector3f currentLocation() {
        return monsterModel.getLocalTranslation();
    }

    public void removeFromScene() {
        monsterModel.removeFromParent();
        --monsterCount;
    }

    public int getMonsterCount() {
        return monsterCount;
    }
    /*
     public void addEnemyControl(EnemyControl control)
     {
     monsterModel.addControl(control);
     }
     */
}
