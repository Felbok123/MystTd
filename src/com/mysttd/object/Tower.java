package com.mysttd.object;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.util.TangentBinormalGenerator;

public class Tower {

    public static final String LASER = "laser";
    public static final String LIGHT = "light";

    public static Geometry generate(AssetManager assetManager, String type, int damage) {
        Geometry tower = (Geometry) assetManager.loadModel("Models/tower.j3o");
        Material mat = assetManager.loadMaterial("Materials/tower.j3m");
        mat.setTexture("GlowMap", assetManager.loadTexture("Textures/tower/laserGlow.png"));
        tower.setUserData("type", type);
        tower.setUserData("damage", damage);
        tower.setName("tower");
        tower.setMaterial(mat);
        tower.scale(2);
        TangentBinormalGenerator.generate(tower);

        return tower;
    }
}
