/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysttd.GameScene;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.ui.Picture;

/**
 *
 * @author Odium
 */
public class GameGui extends AbstractAppState {

    String[] optionNames = {"laserTower", "lightTower",};
    private GameWorld gameState;
    private SimpleApplication simpleApp;
    private static final String TOGGLE_INV = "toggle inventory";
    private static final String TOWER_SELECT = "selected target";
    private static final String MOVE_RIGHT = "moving right";
    private static final String MOVE_LEFT = "moving left";
    private String selectedTower = "";
    private InputManager inputManager;
    private Picture inventoryBackground;
    private boolean isInventoryToggled = false;
    private boolean laserTowerSelected = false;
    private boolean lightTowerSelected = false;
//    private boolean unknownTowerSelected = false;

    public GameGui(GameWorld gameState) {
        this.gameState = gameState;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        simpleApp = (SimpleApplication) app;

        this.inputManager = simpleApp.getInputManager();

        initGuiElements();

        initKeyboardControls();
        //showBudget();
    }

    private void initGuiElements() {

        inventoryBackground = new Picture("inventoryBackground");
        inventoryBackground.setImage(simpleApp.getAssetManager(), "Interface/inventoryBackground.png", true);
        inventoryBackground.setWidth(798);
        inventoryBackground.setHeight(500);
        inventoryBackground.move(270, 50, -1);
    }

    private void initKeyboardControls() {

        inputManager.addMapping(TOWER_SELECT, new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addMapping(MOVE_RIGHT, new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping(MOVE_LEFT, new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping(TOGGLE_INV, new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addListener(actionListener, TOGGLE_INV, TOWER_SELECT, MOVE_LEFT, MOVE_RIGHT);
    }
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {

            if (name.equals(TOWER_SELECT) && !isPressed && !gameState.isPlayerAddingTower()) {
                if (laserTowerSelected) {
                    toggleInventory();
                    gameState.setPlayerAddingTower(true);
                    setSelectedTower("laserTower");
                    laserTowerSelected = false;
                    return;
                }

                if (lightTowerSelected) {
                    toggleInventory();
                    setSelectedTower("lightTower");
                    gameState.setPlayerAddingTower(true);
                    lightTowerSelected = false;
                    return;
                }
            }

            if (name.equals(TOGGLE_INV) && !isPressed) {
                toggleInventory();
            }


            if (name.equals(MOVE_RIGHT) && isInventoryToggled && !isPressed) {
                if (laserTowerSelected) {
                    highlightInventoryOption("lightTower");
                    return;
                }

                if (lightTowerSelected) {
                    highlightInventoryOption("laserTower");
                    return;
                }

                // highlightInventoryOption("laserTower");
            }

            if (name.equals(MOVE_LEFT) && isInventoryToggled && !isPressed) {
                if (lightTowerSelected) {
                    highlightInventoryOption("laserTower");
                    return;
                }

                if (laserTowerSelected) {
                    highlightInventoryOption("lightTower");
                    return;
                }

                highlightInventoryOption("none");
            }
        }
    };

    private void highlightInventoryOption(String desiredOption) {
        Picture option;

        switch (desiredOption) {
            case "laserTower":
                resetInventoryOptions();
                option = (Picture) simpleApp.getGuiNode().getChild(desiredOption);
                option.setImage(simpleApp.getAssetManager(), "Interface/laserTowerHover.png", true);
                laserTowerSelected = true;
                break;

            case "lightTower":
                resetInventoryOptions();
                option = (Picture) simpleApp.getGuiNode().getChild(desiredOption);
                option.setImage(simpleApp.getAssetManager(), "Interface/lightTowerHover.png", true);
                lightTowerSelected = true;
                break;

            case "unknownTower":
                resetInventoryOptions();

                break;

            case "none":
                resetInventoryOptions();

            default:
                break;
        }
    }

    private void resetInventoryOptions() {
        for (int i = 0; i < optionNames.length; i++) {
            Picture option = (Picture) simpleApp.getGuiNode().getChild(optionNames[i]);
            option.setImage(simpleApp.getAssetManager(), "Interface/" + optionNames[i] + ".png", true);
        }
        laserTowerSelected = false;
        lightTowerSelected = false;
        //   unknownTowerSelected = false;
    }

    private void addInventoryOptions() {
        for (int i = 0; i < optionNames.length; i++) {
            Picture option = new Picture(optionNames[i]);
            option.setImage(simpleApp.getAssetManager(),
                    "Interface/" + optionNames[i] + ".png",
                    true);
            option.setWidth(78);
            option.setHeight(247);
            option.move(500 + 250 * i, 175, 0);
            simpleApp.getGuiNode().attachChild(option);
        }


        highlightInventoryOption("laserTower");
    }

    private void toggleInventory() {
        if (isInventoryToggled) {
            simpleApp.getFlyByCamera().setEnabled(true);
            simpleApp.getGuiNode().getChild("goldIcon").removeFromParent();
            simpleApp.getGuiNode().getChild("goldText").removeFromParent();
            inputManager.setCursorVisible(false);

            for (int i = 0; i < optionNames.length; i++) {
                simpleApp.getGuiNode().getChild(optionNames[i]).removeFromParent();
            }

            simpleApp.getGuiNode().getChild("inventoryBackground").removeFromParent();

            gameState.setAmbientColor(ColorRGBA.Gray);
            gameState.setIsGamePaused(false);

            isInventoryToggled = false;
            return;
        }

        simpleApp.getFlyByCamera().setEnabled(false);

        simpleApp.getGuiNode().attachChild(inventoryBackground);
        addInventoryOptions();
        showBudget();

        gameState.setAmbientColor(ColorRGBA.Orange);

        inputManager.setCursorVisible(false);
        isInventoryToggled = true;

    }

    private void showBudget() {
        Picture goldIcon = new Picture("goldIcon");
        goldIcon.setImage(simpleApp.getAssetManager(), "Interface/goldbig.png", true);
        goldIcon.setWidth(64);
        goldIcon.setHeight(64);
        goldIcon.move(15, 40, 0);
        simpleApp.getGuiNode().attachChild(goldIcon);

        BitmapFont myFont = simpleApp.getAssetManager().loadFont("Interface/Fonts/SketchFlowPrint2.fnt");
        BitmapText goldText = new BitmapText(myFont);
        goldText.setName("goldText");
        goldText.setSize(myFont.getCharSet().getRenderedSize());
        goldText.setText(Integer.toString(gameState.getGold()));
        goldText.move(92, 86, 0);
        simpleApp.getGuiNode().attachChild(goldText);
    }

    public void setSelectedTower(String tower) {
        selectedTower = tower;
    }

    public String getSelectedTower() {
        return selectedTower;
    }
}
