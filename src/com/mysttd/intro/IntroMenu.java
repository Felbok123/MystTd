package com.mysttd.intro;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FadeFilter;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.mysttd.GameScene.GameWorld;

public class IntroMenu extends AbstractAppState {

    private SimpleApplication simpleApp;
    private AppStateManager stateManager;
    private AssetManager assetManager;
    private FlyByCamera flyCam;
    private ViewPort viewPort;
    private InputManager inputManager;
    private Node rootNode;
    private Node guiNode;
    private boolean pressEnter;
    private FadeFilter fadeFilter;
    private AmbientLight ambientLight;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        super.initialize(stateManager, app);


        simpleApp = (SimpleApplication) app;

        this.stateManager = simpleApp.getStateManager();
        this.assetManager = simpleApp.getAssetManager();
        this.flyCam = simpleApp.getFlyByCamera();
        this.inputManager = simpleApp.getInputManager();
        this.rootNode = simpleApp.getRootNode();
        this.guiNode = simpleApp.getGuiNode();
        this.viewPort = simpleApp.getViewPort();
        flyCam.setEnabled(false);

        showTitle();

        initInstruction();

        initAtmosphere();

        initFadeFilter();

        initKeyboardControls();


    }

    private void showTitle() {
        BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/SketchFlowPrint2.fnt");
        BitmapText gameTitle = new BitmapText(guiFont, false);
        gameTitle.setSize(guiFont.getCharSet().getRenderedSize());
        gameTitle.setText("Myst TD");
        gameTitle.setColor(ColorRGBA.Red);
        gameTitle.setLocalTranslation(600, 500 + gameTitle.getLineHeight(), 0);
        guiNode.attachChild(gameTitle);
    }

    private void initInstruction() {

        BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/SketchFlowPrint2.fnt");
        BitmapText gameInstruction = new BitmapText(guiFont, false);
        BitmapText gameInstruction2 = new BitmapText(guiFont, false);
        gameInstruction2.setSize(guiFont.getCharSet().getRenderedSize());
        gameInstruction2.setText("Use W A S D to move around" + System.lineSeparator() + "Press Spacebar and then Enter to build a tower");
        gameInstruction2.setColor(ColorRGBA.Red);
        gameInstruction.setSize(guiFont.getCharSet().getRenderedSize());
        gameInstruction.setText("Press Enter To Start");
        gameInstruction.setColor(ColorRGBA.Red);
        gameInstruction.setLocalTranslation(520, 400 + gameInstruction.getLineHeight(), 0);
        gameInstruction2.setLocalTranslation(360, 300 + gameInstruction.getLineHeight(), 0);
        guiNode.attachChild(gameInstruction2);
        guiNode.attachChild(gameInstruction);
    }

    private void initKeyboardControls() {
        inputManager.addMapping("Start Game", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addListener(startGameListener, "Start Game");
    }
    private ActionListener startGameListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isKeyPressed, float tpf) {
            if (name.equals("Start Game") && !isKeyPressed) {
                pressEnter = true;
            }
        }
    };

    @Override
    public void update(float tpf) {
        if (pressEnter) {
            if (fadeFilter.getValue() == 1) {
                guiNode.detachAllChildren();
                fadeFilter.fadeOut();
            }
        }

        if (fadeFilter.getValue() <= 0) {
            stateManager.detach(this);
        }

    }

    private void initFadeFilter() {
        fadeFilter = new FadeFilter(5);
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(fadeFilter);
        viewPort.addProcessor(fpp);
    }

    private void initAtmosphere() {
        ambientLight = new AmbientLight();
        ambientLight.setColor(ColorRGBA.Gray.mult(5));
        rootNode.addLight(ambientLight);
    }

    @Override
    public void cleanup() {
        setEnabled(false);

        rootNode.detachAllChildren();

        fadeFilter.fadeIn();

        flyCam.setEnabled(true);

        stateManager.attach(new GameWorld(ambientLight));
    }
}
