/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysstd.monsters;

import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Odium
 */
public class Teapot {

    public Spatial makeTeapot(AssetManager assetManager, Node rootNode) {
        Spatial teapot;
        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);

        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");

        mat.setFloat("Shininess", 1f);

        mat.setBoolean("UseMaterialColors", true);

        mat.setColor("Ambient", ColorRGBA.Black);

        mat.setColor("Diffuse", ColorRGBA.DarkGray);

        mat.setColor("Specular", ColorRGBA.White.mult(0.6f));

        teapot = assetManager.loadModel("Models/Teapot.j3o");

        teapot.setName("Teapot");
        teapot.setLocalScale(3);
        teapot.setMaterial(mat);
        teapot.addLight(dl);
        teapot.setUserData("health", 2);

        rootNode.attachChild(teapot);
        return teapot;

    }
}
