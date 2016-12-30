/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysstd.monsters;

import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

/**
 *
 * @author Odium
 */
public final class Squidly extends AbstractMonster {

    public Squidly(AssetManager assetManager, Node enemyNode) {
        initEnemyModel(assetManager, enemyNode);
    }

    @Override
    protected void initEnemyModel(AssetManager assetManager, Node enemyNode) {

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);

        monsterModel = assetManager.loadModel("Models/Squiddlythingy.j3o");
        monsterModel.setName("Squidly" + monsterCount++);
        monsterModel.setUserData("health", 4000);
        monsterModel.setUserData("damg", 5);
        monsterModel.setUserData("gold", 10);
        monsterModel.addLight(dl);
        monsterModel.scale(7);


        enemyNode.attachChild(monsterModel);

    }
}
