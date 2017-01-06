package com.mysstd.monsters;

import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

/**
 *
 * @author Odium
 */
public final class Teapot extends AbstractMonster {

    public Teapot(AssetManager assetManager, Node enemyNode) {
        initEnemyModel(assetManager, enemyNode);
    }

    @Override
    protected void initEnemyModel(AssetManager assetManager, Node enemyNode) {
        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        monsterModel = assetManager.loadModel("Models/Teapot.j3o");
        monsterModel.setName("Teapot");
        monsterModel.setUserData("health", 2000);
        monsterModel.setUserData("damg", 2);
        monsterModel.setUserData("gold", 1);

        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setFloat("Shininess", 1f);
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient", ColorRGBA.Black);
        mat.setColor("Diffuse", ColorRGBA.DarkGray);
        mat.setColor("Specular", ColorRGBA.White.mult(0.6f));

        monsterModel.setLocalScale(3);
        monsterModel.setMaterial(mat);
        monsterModel.addLight(dl);



        enemyNode.attachChild(monsterModel);
    }
}
