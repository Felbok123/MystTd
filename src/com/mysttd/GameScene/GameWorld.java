package com.mysttd.GameScene;

import com.mysstd.monsters.MonsterMovement;
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
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.mysstd.control.*;
import com.mysstd.monsters.Concorde;
import com.mysstd.monsters.Squidly;
import com.mysttd.object.PlayerBase;
import com.mysttd.object.Tower;

public class GameWorld extends AbstractAppState {

    private GameGui gui;
    private SimpleApplication simpleApp;
    private AppStateManager stateManager;
    private AssetManager assetManager;
    private InputManager inputManager;
    private ViewPort viewPort;
    private Camera camera;
    private FlyByCamera flyCam;
    private Node rootNode;
    private Node beamNode = new Node("beam node");
    private Node towerNode = new Node("tower node");
    private Node enemyNode = new Node("enemy node");
    private AmbientLight atmosphere;
    private Spatial gameWorld;
    private boolean isGameOver = false;
    private boolean isGamePaused = false;
    private boolean isAddingTower = false;
    private static final String ADD_TOWER = "add tower";
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
    private CharacterControl player;
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    private PlayerBase base;
    private int baseHealth = 50;
    private MonsterMovement monstMov;
    private long initialTime;
    private long currentTime;
    private long initialTime2;
    private long timer2;
    private int gold;
    private float timerBeam = 0;

    public GameWorld(AmbientLight initialAtmosphere) {
        atmosphere = initialAtmosphere;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        simpleApp = (SimpleApplication) app;

        initResources();

        initGUI();

        initGameScene();

        initBase();

        attachNodes();

        setGold(50);
        initialTime2 = System.currentTimeMillis();
        initialTime = System.currentTimeMillis();
    }

    private void initGUI() {
        gui = new GameGui(this);
        stateManager.attach(gui);
    }

    public void initBase() {

        base = new PlayerBase(baseHealth, rootNode);

    }

    public void initGameScene() {

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        flyCam.setMoveSpeed(100);
        flyCam.setDragToRotate(false);
        setUpKeys();

        gameWorld = assetManager.loadModel("Scenes/thisScene.j3o");

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
        inputManager.addMapping(ADD_TOWER, new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addListener(actionListener, ADD_TOWER);

        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("1", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addListener(movmentListener, "Left");
        inputManager.addListener(movmentListener, "Right");
        inputManager.addListener(movmentListener, "Up");
        inputManager.addListener(movmentListener, "Down");
        inputManager.addListener(movmentListener, "1");

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
            } else if (binding.equals("Down")) {
                down = isPressed;
            } else if (binding.equals("1")) {
                if (isPressed) {
                }
            }
        }
    };
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals(ADD_TOWER) && isPressed && isAddingTower) {
                Spatial tower;

                Vector3f towerLocation = new Vector3f(camera.getLocation().getX(), 0, camera.getLocation().getZ());

                switch (gui.getSelectedTower()) {
                    case "laserTower":
                        tower = Tower.generate(assetManager, Tower.LASER, 2);
                        decreaseGold(20);
                        break;
                    case "lightTower":
                        tower = Tower.generate(assetManager, Tower.LIGHT, 5);
                        decreaseGold(30);
                        break;
                    default:
                        gui.setSelectedTower("");
                        isAddingTower = false;
                        return;
                }

                tower.setLocalTranslation(towerLocation);
                tower.addControl(new TowerControl(stateManager.getState(GameWorld.class)));


                towerNode.attachChild(tower);
                //  decreaseGold(20);
                gui.setSelectedTower("");

                isAddingTower = false;
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


    }

    private void attachNodes() {
        rootNode.attachChild(beamNode);
        rootNode.attachChild(towerNode);
        rootNode.attachChild(enemyNode);
        rootNode.attachChild(gameWorld);
    }

    @Override
    public void update(float tpf) {

        timerBeam += tpf;
        if (timerBeam > 30 * tpf) {
            beamNode.detachAllChildren();
            timerBeam = 0;
        }

        if (isGamePaused) {
            return;
        }

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
        player.setWalkDirection(walkDirection);
        camera.setLocation(player.getPhysicsLocation());
        if (base.getBaseHp() <= 0) {
            System.out.println("Game over");
            base.getBase().detachAllChildren();
            enemyNode.detachAllChildren();
            rootNode.detachAllChildren();
            cleanup();
            System.exit(0);
        }


        currentTime = System.currentTimeMillis();

        monstMov = new MonsterMovement(enemyNode, base);

        if (currentTime - initialTime >= 4000) {
            /*
             Teapot teapot = new Teapot(assetManager, enemyNode);
             teapot.addEnemyControl(new MonsterControl(this));
             monstMov.moveMonster(teapot.getMonsterModel(), monstMov.initNormalWaypoints(assetManager), (int) 1.5, 49);
             */
        }



        if (currentTime - initialTime2 >= 6000) {

            Squidly squid = new Squidly(assetManager, enemyNode);
            squid.addEnemyControl(new MonsterControl(this));
            monstMov.moveMonster(squid.getMonsterModel(), monstMov.initNormalWaypoints(assetManager), 1, 60);



            if (timer2 + 3000 >= 500) {

                Concorde bomber = new Concorde(assetManager, enemyNode);
                bomber.addEnemyControl(new MonsterControl(this));
                monstMov.moveMonster(bomber.getMonsterModel(), monstMov.initSkyWaypoints(assetManager), 1, 50);

            }
            initialTime = System.currentTimeMillis();
            timer2 = System.currentTimeMillis();
            initialTime2 = System.currentTimeMillis();
        }
    }

    public void setPlayerAddingTower(boolean isPlayerAddingTower) {
        this.isAddingTower = isPlayerAddingTower;
    }

    public boolean isPlayerAddingTower() {
        return isAddingTower;
    }

    public Material getUnshadedMaterial() {
        return new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    }

    public boolean isIsGameOver() {
        return isGameOver;
    }

    public void setIsGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public boolean isIsGamePaused() {
        return isGamePaused;
    }

    public void setIsGamePaused(boolean isGamePaused) {
        this.isGamePaused = isGamePaused;
    }

    public void setAmbientColor(ColorRGBA color) {
        atmosphere.setColor(color.mult(5));
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void decreaseGold(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
        gold -= amount;
    }

    public void increaseGold(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
        gold += amount;
    }

    public Node getNode(String desiredNode) {
        if (desiredNode == null) {
            throw new IllegalStateException("desiredNode cannot be null");
        }
        Node node;
        switch (desiredNode.toLowerCase()) {
            case "beam":
                node = beamNode;
                break;
            case "tower":
                node = towerNode;
                break;
            case "enemy":
                node = enemyNode;
                break;
            default:
                throw new IllegalArgumentException("desiredNode can either be beam, tower, or enemy");
        }

        return node;
    }

    @Override
    public void cleanup() {
        setEnabled(false);

        rootNode.removeLight(atmosphere);
        rootNode.detachAllChildren();

        stateManager.detach(gui);
        System.exit(0);
    }
}
