/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysttd.state;

import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.mysttd.object.PlayerBase;

/**
 *
 * @author Odium
 */
public class MonsterMovement {

    private Node rootNode;
    // private AssetManager assetManager;
    //  private InputManager inputManager;
    MotionPath path;
    //private MotionEvent motionControl;
    private PlayerBase base;
    private GameWorld gameWorld;


    /*
     *  lägg till listener på path vad den gör när den når en waypoint/slutet. 
     * Använda MotionEvent istället för MotionTrack?
     * Fixa så monster kommer i vågor kan ha knapp tryckning också?
     */
    public MonsterMovement(Node rootNode, PlayerBase base) {
        this.rootNode = rootNode;
        this.base = base;

    }

    public MotionPath initSkyWaypoints(AssetManager assetManager) {
        path = new MotionPath();
        path.addWayPoint(new Vector3f(-150.35995f, 4f, -14.297632f));
        path.addWayPoint(new Vector3f(-88.547516f, 4f, -17.409536f));
        path.addWayPoint(new Vector3f(-84.83237f, 4f, -62.80838f));
        path.addWayPoint(new Vector3f(-45.021725f, 4f, -63.23368f));
        path.addWayPoint(new Vector3f(-44.718445f, 4f, -18.281975f));
        path.addWayPoint(new Vector3f(-69.89613f, 4f, -17.996004f));
        path.addWayPoint(new Vector3f(-77.73884f, 4f, 66.10136f));
        path.addWayPoint(new Vector3f(17.973166f, 4f, 67.42557f));
        path.addWayPoint(new Vector3f(20.869114f, 4f, 31.84087f));
        path.addWayPoint(new Vector3f(-17.685957f, 4f, 31.998701f));
        path.addWayPoint(new Vector3f(-16.710148f, 4f, -62.578075f));
        path.addWayPoint(new Vector3f(24.27733f, 4f, -61.662155f));
        path.addWayPoint(new Vector3f(24.228615f, 4f, -13.684091f));
        path.addWayPoint(new Vector3f(52.40349f, 4f, -15.427767f));

        path.setCurveTension(0.009f);
        path.setPathSplineType(Spline.SplineType.Linear);

        path.enableDebugShape(assetManager, rootNode);
        // path.disableDebugShape();


        path.addListener(new MotionPathListener() {
            @Override
            public void onWayPointReach(MotionEvent control, int wayPointIndex) {

                if (path.getNbWayPoints() == wayPointIndex + 1) {

                    base.removeHealth((Integer) control.getSpatial().getUserData("damg"));
                    System.out.println("Your base is under attack! " + control.getSpatial().getName());
                    System.out.println("Your base has : " + base.getBaseHp() + " health left");
                    rootNode.detachChild(control.getSpatial());

                }

                //   System.out.println(motionControl.getCurrentWayPoint());
            }
        });
        return path;
    }

    public MotionPath initNormalWaypoints(AssetManager assetManager) {
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
        // path.disableDebugShape();


        path.addListener(new MotionPathListener() {
            @Override
            public void onWayPointReach(MotionEvent control, int wayPointIndex) {

                if (path.getNbWayPoints() == wayPointIndex + 1) {

                    base.removeHealth((Integer) control.getSpatial().getUserData("damg"));
                    System.out.println("Your base is under attack! " + control.getSpatial().getName());
                    System.out.println("Your base has : " + base.getBaseHp() + " health left");
                    rootNode.detachChild(control.getSpatial());

                }

                //   System.out.println(motionControl.getCurrentWayPoint());
            }
        });
        return path;
    }

    public void moveMonster(Spatial monster, MotionPath path, int speed, float duration) {

        MotionEvent motionControl = new MotionEvent(monster, path);
        motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
        motionControl.setRotation(new Quaternion().fromAngleNormalAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y));
        motionControl.setInitialDuration(duration);
        motionControl.setSpeed(speed);
        motionControl.play();


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
