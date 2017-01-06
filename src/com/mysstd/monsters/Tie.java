package com.mysstd.monsters;

import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.scene.Node;

public final class Tie extends AbstractMonster {

    public Tie(AssetManager assetManager, Node enemyNode) {
        initEnemyModel(assetManager, enemyNode);
    }

    @Override
    protected void initEnemyModel(AssetManager assetManager, Node enemyNode) {
        DirectionalLight dl = new DirectionalLight();

        monsterModel = assetManager.loadModel("Models/TIE.j3o");
        monsterModel.addLight(dl);
        monsterModel.setName("Baddy");
        monsterModel.setUserData("health", 28000);
        monsterModel.setUserData("damg", 20);
        monsterModel.setUserData("gold", 3);



        enemyNode.attachChild(monsterModel);
    }
}
