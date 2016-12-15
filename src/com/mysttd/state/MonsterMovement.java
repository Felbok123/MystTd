/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysttd.state;

import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.mysstd.monsters.Teapot;
import com.mysttd.object.PlayerBase;
import java.util.List;

/**
 *
 * @author Odium
 */
public class MonsterMovement {

    private Node rootNode;
    private AssetManager assetManager;
    private InputManager inputManager;
    // private Spatial teapot;
    private boolean active = true;
    private boolean playing = false;
    private MotionPath path;
    private MotionEvent motionControl;
    PlayerBase base;
    //Spatial teapot;


    /*
     *  lägg till listener på path vad den gör när den når en waypoint/slutet. 
     * Använda MotionEvent istället för MotionTrack?
     * Fixa så monster kommer i vågor kan ha knapp tryckning också?
     */
    public MonsterMovement(AssetManager assetManager, InputManager inputManager, Node rootNode, MotionEvent motionControl, PlayerBase base) {
        this.assetManager = assetManager;
        this.inputManager = inputManager;
        this.rootNode = rootNode;
        this.base = base;
        // this.path = path;

        // initInputs();
        // initWaypoint(assetManager);
    }

    public MotionPath initWaypoint(AssetManager assetManager) {


        path = new MotionPath();
        path.addWayPoint(new Vector3f(-150.35995f, 0.5f, -14.297632f));
        path.addWayPoint(new Vector3f(-88.547516f, 0.5f, -17.409536f));
        path.addWayPoint(new Vector3f(-84.83237f, 0.5f, -62.80838f));
        path.addWayPoint(new Vector3f(-45.021725f, 0.5f, -63.23368f));
        path.addWayPoint(new Vector3f(-44.718445f, 0.5f, -18.281975f));
        path.addWayPoint(new Vector3f(-69.89613f, 0.5f, -17.996004f));
        path.addWayPoint(new Vector3f(-77.73884f, 0.5f, 66.10136f));
        path.addWayPoint(new Vector3f(17.973166f, 0.5f, 67.42557f));
        path.addWayPoint(new Vector3f(20.869114f, 0.5f, 31.84087f));
        path.addWayPoint(new Vector3f(-17.685957f, 0.5f, 31.998701f));
        path.addWayPoint(new Vector3f(-16.710148f, 0.5f, -62.578075f));
        path.addWayPoint(new Vector3f(24.27733f, 0.5f, -61.662155f));
        path.addWayPoint(new Vector3f(24.228615f, 0.5f, -13.684091f));
        path.addWayPoint(new Vector3f(52.40349f, 0.5f, -15.427767f));
        path.setCurveTension(0.009f);
        path.setPathSplineType(Spline.SplineType.Linear);

        path.enableDebugShape(assetManager, rootNode);


        path.addListener(new MotionPathListener() {
            public void onWayPointReach(MotionEvent control, int wayPointIndex) {

                if (path.getNbWayPoints() == wayPointIndex + 1) {

                    // System.out.println(control.getSpatial().getName() + "Finished!!! ");
                    base.removeHealth((Integer) control.getSpatial().getUserData("health"));
                    System.out.println(base.getBaseHp());
                    rootNode.detachChild(control.getSpatial());
                }
                //   System.out.println(motionControl.getCurrentWayPoint());
            }
        });
        return path;
    }

    public void moveMonster(Spatial spatial, MotionPath path) {
        motionControl = new MotionEvent(spatial, path);
        motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
        motionControl.setRotation(new Quaternion().fromAngleNormalAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y));

        motionControl.setInitialDuration(30f);
        motionControl.setSpeed(1f);

    }

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

        rootNode.attachChild(teapot);
        return teapot;

    }
    /*
     public void initInputs() {

     inputManager.addMapping("display_hidePath", new KeyTrigger(KeyInput.KEY_P));

     inputManager.addMapping("SwitchPathInterpolation", new KeyTrigger(KeyInput.KEY_I));

     inputManager.addMapping("tensionUp", new KeyTrigger(KeyInput.KEY_U));

     inputManager.addMapping("tensionDown", new KeyTrigger(KeyInput.KEY_J));

     inputManager.addMapping("play_stop", new KeyTrigger(KeyInput.KEY_SPACE));

     ActionListener acl = new ActionListener() {
     public void onAction(String name, boolean keyPressed, float tpf) {
     /*
     if (name.equals("display_hidePath") && keyPressed) {

     if (active) {

     active = false;

     path.disableDebugShape();

     } else {

     active = true;

     path.enableDebugShape(assetManager, rootNode);

     }

     }
     /
     if (name.equals("play_stop") && keyPressed) {

     if (playing) {

     playing = false;

     motionControl.stop();
     rootNode.detachChild(teapot);
     } else {
     rootNode.attachChild(teapot);
     playing = true;
     motionControl.play();
     }

     }
     if (name.equals("SwitchPathInterpolation") && keyPressed) {

     if (path.getPathSplineType() == Spline.SplineType.CatmullRom) {

     path.setPathSplineType(Spline.SplineType.Linear);

     } else {

     path.setPathSplineType(Spline.SplineType.CatmullRom);

     }

     }
     if (name.equals("tensionUp") && keyPressed) {

     path.setCurveTension(path.getCurveTension() + 0.1f);

     System.err.println("Tension : " + path.getCurveTension());

     }

     if (name.equals("tensionDown") && keyPressed) {

     path.setCurveTension(path.getCurveTension() - 0.1f);

     System.err.println("Tension : " + path.getCurveTension());
     }
     }
     };

     inputManager.addListener(acl, "display_hidePath", "play_stop", "SwitchPathInterpolation", "tensionUp", "tensionDown");

     }
     */
}
