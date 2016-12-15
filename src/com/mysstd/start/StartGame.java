/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysstd.start;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.terrain.geomipmap.TerrainLodControl;

/**
 *
 * @author Odium
 */
public class StartGame extends SimpleApplication implements ActionListener {

    private Spatial sceneModel;
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
    private CharacterControl player;
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;
    //Temporary vectors used on each frame.
    //They here to avoid instanciating new vectors on each frame
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    Box b = new Box(1, 1, 1);
    int hp = 50;
    Node Home;

    public static void main(String args[]) {
        StartGame app = new StartGame();
        app.start();


    }

    @Override
    public void simpleInitApp() {
        //   flyCam.setMoveSpeed(100);


        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.getPhysicsSpace().enableDebug(assetManager);

        // We re-use the flyby camera for rotation, while positioning is handled by physics
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        flyCam.setMoveSpeed(100);
        setUpKeys();
        //   setUpLight();

        sceneModel = assetManager.loadModel("Scenes/thisScene.j3o");




        // We set up collision detection for the scene by creating a
        // compound collision shape and a static RigidBodyControl with mass zero.
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) sceneModel);
        landscape = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(landscape);


        // We set up collision detection for the player by creating
        // a capsule collision shape and a CharacterControl.
        // The CharacterControl offers extra settings for
        // size, stepheight, jumping, falling, and gravity.
        // We also put the player in its starting position.
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        player = new CharacterControl(capsuleShape, 0.05f);
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        player.setGravity(30);
        player.setPhysicsLocation(new Vector3f(50, 5, 5));

        System.out.println(player.getPhysicsLocation());



        // We attach the scene and the player to the rootnode and the physics space,
        // to make them appear in the game world.

        bulletAppState.getPhysicsSpace().add(landscape);
        bulletAppState.getPhysicsSpace().add(player);
        TerrainLodControl lodControl = ((Node) rootNode).getControl(TerrainLodControl.class);
        if (lodControl != null) {
            lodControl.setCamera(getCamera());
        }
        rootNode.attachChild(sceneModel);

        Home = (Node) rootNode.getChild("Home");
        Home.setUserData("health", hp);

        /*
         SceneGraphVisitor visitor = new SceneGraphVisitor() {
         @Override
         public void visit(Spatial spat) {
         // search criterion can be control class:
         MyControl control = spatial.getControl(MyControl.class);
         if (control != null) {
         // you have access to any method, e.g. name.
         System.out.println("Instance of " + control.getClass().getName()
         + " found for " + spatial.getName());
         }
         }
         };

         // Now scan the tree either depth first...
         rootNode.depthFirstTraversal(visitor);
         // ... or scan it breadth first.
         rootNode.breadthFirstTraversal(visitor);

         */

    }

    /**
     * We over-write some navigational key mappings here, so we can add
     * physics-controlled walking and jumping:
     */
    private void setUpKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Jump");

    }

    @Override
    public void simpleUpdate(float tpf) {

        camDir.set(cam.getDirection()).multLocal(0.6f);
        camLeft.set(cam.getLeft()).multLocal(0.4f);
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }
        player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());

        if (getBaseHp() == 0) {
            Home.detachAllChildren();
        }


    }

    public void onAction(String binding, boolean isPressed, float tpf) {

        if (binding.equals("Left")) {
            left = isPressed;
        } else if (binding.equals("Right")) {
            right = isPressed;
        } else if (binding.equals("Up")) {
            up = isPressed;
        } else if (binding.equals("Down")) {
            down = isPressed;
        } else if (binding.equals("Jump")) {
            if (isPressed) {
                int am = 1;
                healthAmount(am);
                System.out.println(getBaseHp());
                --am;

            }
        }
    }

    public Geometry makeCube(String name, float x, float y, float z) {
        Box box = new Box(10, 10, 10);
        Geometry cube = new Geometry(name, box);
        cube.setLocalTranslation(x, y, z);

        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.randomColor());
        cube.setMaterial(mat1);
        return cube;
    }

    public int getBaseHp() {
        return Home.getUserData("health");
    }

    public void healthAmount(int amount) {


        Home.setUserData("health", getBaseHp() - amount);
    }
}
