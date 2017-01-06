package com.mysstd.monsters;

import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;


public final class Squidly extends AbstractMonster {

    public Squidly(AssetManager assetManager, Node enemyNode) {
        initEnemyModel(assetManager, enemyNode);
    }

    @Override
    protected void initEnemyModel(AssetManager assetManager, Node enemyNode) {

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);

        monsterModel = assetManager.loadModel("Models/Squiddlythingy.j3o");
        monsterModel.setName("Squidly");
        monsterModel.setUserData("health", 20000);
        monsterModel.setUserData("damg", 5);
        monsterModel.setUserData("gold", 2);
        monsterModel.addLight(dl);
        monsterModel.scale(7);


        enemyNode.attachChild(monsterModel);

    }
}
