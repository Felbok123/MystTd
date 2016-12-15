/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysttd.state;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.cinematic.events.MotionTrack;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.mysstd.monsters.Teapot;
import com.mysttd.object.PlayerBase;

/**
 *
 * @author Odium
 */
public class GameWorld extends AbstractAppState {

    private SimpleApplication simpleApp;
    private AppStateManager stateManager;
    private AssetManager assetManager;
    private InputManager inputManager;
    private ViewPort viewPort;
    private Camera camera;
    private FlyByCamera flyCam;
    private Node rootNode;
    private Node guiNode;
    private Node sceneNode = new Node("scene node");
    private Node beamNode = new Node("beam node");
    private Node towerNode = new Node("tower node");
    private Node enemyNode = new Node("enemy node");
    private Node playerNode = new Node("player node");
    private AmbientLight atmosphere;
    private SpotLight cameraLighting;
    private Spatial gameWorld;
    /*
     * 
     */
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
    private CharacterControl player;
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;
    //Temporary vectors used on each frame.
    //They here to avoid instanciating new vectors on each frame
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    /*
     *  
     */
    private MotionEvent motionControl;
    /*
     * 
     */
    private PlayerBase base;
    private int baseHealth = 50;
    MonsterMovement monstMov;
    private long initialTime = 0;
    private long currentTime = 0;

    public GameWorld(AmbientLight initialAtmosphere) {
        atmosphere = initialAtmosphere;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        simpleApp = (SimpleApplication) app;
        simpleApp.setDisplayFps(true);
        initResources();

        initCameraLighting();


        // initWaypoint(makeTeapot());
        initGameInputs();

        // initInputs();
        attachNodes();
        initBase();

        initialTime = System.currentTimeMillis();
    }

    public void initBase() {

        base = new PlayerBase(baseHealth, rootNode);

    }

    public void initGameInputs() {

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.getPhysicsSpace().enableDebug(assetManager);

        // We re-use the flyby camera for rotation, while positioning is handled by physics
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        flyCam.setMoveSpeed(100);
        flyCam.setDragToRotate(false);
        setUpKeys();

        gameWorld = assetManager.loadModel("Scenes/thisScene.j3o");

        // We set up collision detection for the scene by creating a
        // compound collision shape and a static RigidBodyControl with mass zero.
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) gameWorld);
        landscape = new RigidBodyControl(sceneShape, 0);
        gameWorld.addControl(landscape);


        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        player = new CharacterControl(capsuleShape, 0.05f);
        player.setJumpSpeed(5);
        player.setFallSpeed(30);
        player.setGravity(30);
        // Start position 
        player.setPhysicsLocation(new Vector3f(50, 5, 5));


        bulletAppState.getPhysicsSpace().add(landscape);
        bulletAppState.getPhysicsSpace().add(player);



        TerrainLodControl lodControl = ((Node) rootNode).getControl(TerrainLodControl.class);
        if (lodControl != null) {
            lodControl.setCamera(simpleApp.getCamera());
        }


        rootNode.attachChild(gameWorld);

    }

    private void setUpKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addListener(movmentListener, "Left");
        inputManager.addListener(movmentListener, "Right");
        inputManager.addListener(movmentListener, "Up");
        inputManager.addListener(movmentListener, "Down");
        inputManager.addListener(movmentListener, "Jump");

    }
    private ActionListener movmentListener = new ActionListener() {
        @Override
        public void onAction(String binding, boolean isPressed, float tpf) {
            if (binding.equals("Left")) {
                left = isPressed;
            } else if (binding.equals("Right")) {
                right = isPressed;
            } else if (binding.equals("Up")) {
                up = isPressed;
                System.out.println(player.getPhysicsLocation());
            } else if (binding.equals("Down")) {
                down = isPressed;
            } else if (binding.equals("Jump")) {
                if (isPressed) {
                    int am = 1;
                    base.removeHealth(am);
                    System.out.println(base.getBaseHp());
                    --am;
                }
            }
        }
    };

    private void initResources() {
        if (simpleApp == null) {
            throw new IllegalStateException("simpleApp was not initialized.");
        }

        this.stateManager = simpleApp.getStateManager();
        this.assetManager = simpleApp.getAssetManager();
        this.camera = simpleApp.getCamera();
        this.flyCam = simpleApp.getFlyByCamera();
        this.viewPort = simpleApp.getViewPort();
        this.inputManager = simpleApp.getInputManager();
        this.rootNode = simpleApp.getRootNode();
        this.guiNode = simpleApp.getGuiNode();

    }

    private void attachNodes() {
        rootNode.attachChild(sceneNode);
        rootNode.attachChild(beamNode);
        rootNode.attachChild(towerNode);
        rootNode.attachChild(enemyNode);
        rootNode.attachChild(gameWorld);
    }

    private void initCameraLighting() {
        cameraLighting = new SpotLight();
        cameraLighting.setColor(ColorRGBA.Orange.mult(5f));
        cameraLighting.setSpotRange(1000);
        cameraLighting.setSpotOuterAngle(15 * FastMath.DEG_TO_RAD);
        cameraLighting.setSpotInnerAngle(10 * FastMath.DEG_TO_RAD);

        rootNode.addLight(cameraLighting);
        updateCameraLighting();
    }

    private void updateCameraLighting() {
        cameraLighting.setDirection(camera.getDirection());
        cameraLighting.setPosition(camera.getLocation());
    }

    @Override
    public void update(float tpf) {

        camDir.set(camera.getDirection()).multLocal(0.6f);
        camLeft.set(camera.getLeft()).multLocal(0.4f);
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
        if (base.getBaseHp() == 0) {
            System.out.println("Game over");
            base.getBase().detachAllChildren();
            motionControl.stop();
        }
        player.setWalkDirection(walkDirection);
        camera.setLocation(player.getPhysicsLocation());



        currentTime = System.currentTimeMillis();
        if (currentTime - initialTime >= 5000) {

            monstMov = new MonsterMovement(assetManager, inputManager, enemyNode, motionControl, base);
            // monstMov.moveMonster(teapot.makeTeapot(assetManager, enemyNode), monstMov.initWaypoint(assetManager))

        }
        if (currentTime - initialTime >= 5000) {
            Teapot teapot = new Teapot();
            motionControl = new MotionEvent(teapot.makeTeapot(assetManager, enemyNode), monstMov.initWaypoint(assetManager));
            motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
            motionControl.setRotation(new Quaternion().fromAngleNormalAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y));
            motionControl.setInitialDuration(60f);
            motionControl.setSpeed(1);
            motionControl.play();

            initialTime = System.currentTimeMillis();
        }



    }

    @Override
    public void cleanup() {
        setEnabled(false);

        rootNode.removeLight(atmosphere);
        rootNode.detachAllChildren();

        // stateManager.detach(gui);
        System.exit(0);
    }
}
