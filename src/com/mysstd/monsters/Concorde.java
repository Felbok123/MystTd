package com.mysstd.monsters;

import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

public final class Concorde extends AbstractMonster {

    public Concorde(AssetManager assetManager, Node enemyNode) {
        initEnemyModel(assetManager, enemyNode);
    }

    @Override
    protected void initEnemyModel(AssetManager assetManager, Node enemyNode) {
        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);

        monsterModel = assetManager.loadModel("Models/concorde11.j3o");
        monsterModel.setName("Bomber");
        monsterModel.setUserData("health", 18000);
        monsterModel.setUserData("damg", 5);
        monsterModel.setUserData("gold", 3);
        monsterModel.addLight(dl);

        enemyNode.attachChild(monsterModel);

    }
}
