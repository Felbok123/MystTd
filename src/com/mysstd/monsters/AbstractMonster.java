package com.mysstd.monsters;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.mysstd.control.MonsterControl;

public abstract class AbstractMonster {

    protected Spatial monsterModel;
    protected int monsterCount;

    protected abstract void initEnemyModel(AssetManager assetManager, Node enemyNode);

    public Vector3f currentLocation() {
        return monsterModel.getLocalTranslation();
    }

    public void move(Vector3f amountInWorldUnits) {
        monsterModel.move(amountInWorldUnits);
    }

    public void moveFromOrigin(Vector3f amountInWorldUnits) {
        monsterModel.setLocalTranslation(amountInWorldUnits);
    }

    public void removeFromScene() {
        monsterModel.removeFromParent();
        --monsterCount;
    }

    public int getMonsterCount() {
        return monsterCount;
    }

    public void addEnemyControl(MonsterControl control) {
        monsterModel.addControl(control);
    }

    public Spatial getMonsterModel() {
        return monsterModel;
    }
}
