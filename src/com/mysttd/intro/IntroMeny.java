/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FadeFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.mysttd.state.GameWorld;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Odium
 */
public class IntroMeny extends AbstractAppState {

    private SimpleApplication simpleApp;
    private AppStateManager stateManager;
    private AssetManager assetManager;
    private FlyByCamera flyCam;
    private ViewPort viewPort;
    private InputManager inputManager;
    private Node rootNode;
    private Node guiNode;
    private boolean hasPlayerPressedEnter;
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
        gameInstruction.setSize(guiFont.getCharSet().getRenderedSize());
        gameInstruction.setText("Press Enter To Start");
        gameInstruction.setColor(ColorRGBA.Red);
        gameInstruction.setLocalTranslation(520, 300 + gameInstruction.getLineHeight(), 0);
        guiNode.attachChild(gameInstruction);
    }

    private void initBackgroundIntro() {
        /*
         * Skapa intro Menyn bakrund
         */
    }

    private void initKeyboardControls() {
        inputManager.addMapping("Start Game", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addListener(actionListener, "Start Game");
    }
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isKeyPressed, float tpf) {
            if (name.equals("Start Game") && !isKeyPressed) {
                hasPlayerPressedEnter = true;
            }
        }
    };

    @Override
    public void update(float tpf) {
        if (hasPlayerPressedEnter) {
            if (fadeFilter.getValue() == 1) {
                guiNode.detachAllChildren();
                fadeFilter.fadeOut();
            }
        }

        if (fadeFilter.getValue() <= 0) {
            stateManager.detach(this);
        }

        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Logger.getLogger(IntroMeny.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    private void initFadeFilter() {
        fadeFilter = new FadeFilter(5);
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(fadeFilter);
        viewPort.addProcessor(fpp);
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
