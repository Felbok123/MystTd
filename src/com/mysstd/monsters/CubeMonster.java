/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysstd.monsters;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Odium
 */
public class CubeMonster {

    
    public Geometry makeCube(String name, float x, float y, float z, AssetManager assetManager) {
        Box box = new Box(10, 10, 10);
        Geometry cube = new Geometry(name, box);
        cube.setLocalTranslation(x, y, z);
        box.createCollisionData();

        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.randomColor());
        cube.setMaterial(mat1);
        return cube;
    }
}
