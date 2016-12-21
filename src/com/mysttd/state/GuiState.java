/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysttd.state;

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
import com.jme3.scene.Spatial;
import com.jme3.ui.Picture;

/**
 *
 * @author Odium
 */
public class GuiState extends AbstractAppState {

    String[] optionNames = {"laserTower", "lightTower", "unknownTower"};
    private GameWorld gameState;
    private SimpleApplication simpleApp;
    private static final String TOGGLED_INVENTORY = "toggle inventory";
    private static final String SELECTED_TOWER = "selected target";
    private static final String MOVING_RIGHT = "moving right";
    private static final String MOVING_LEFT = "moving left";
    private String selectedTower = "";
    private InputManager inputManager;
    private Picture inventoryBackground;
    private boolean isInventoryToggled = false;
    private boolean laserTowerSelected = false;
    private boolean lightTowerSelected = false;
    private boolean unknownTowerSelected = false;

    public GuiState(GameWorld gameState) {
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

        inputManager.addMapping(SELECTED_TOWER, new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addMapping(MOVING_RIGHT, new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping(MOVING_LEFT, new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping(TOGGLED_INVENTORY, new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addListener(actionListener, TOGGLED_INVENTORY, SELECTED_TOWER, MOVING_LEFT, MOVING_RIGHT);
    }
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {

            if (name.equals(SELECTED_TOWER) && !isPressed && !gameState.isPlayerAddingTower()) {
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

            if (name.equals(TOGGLED_INVENTORY) && !isPressed) {
                toggleInventory();
            }


            if (name.equals(MOVING_RIGHT) && isInventoryToggled && !isPressed) {
                if (laserTowerSelected) {
                    highlightInventoryOption("lightTower");
                    return;
                }

                if (lightTowerSelected) {
                    highlightInventoryOption("unknownTower");
                    return;
                }

                highlightInventoryOption("laserTower");
            }

            if (name.equals(MOVING_LEFT) && isInventoryToggled && !isPressed) {
                if (lightTowerSelected) {
                    highlightInventoryOption("laserTower");
                    return;
                }

                if (unknownTowerSelected) {
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
                setLaserTowerText(true);
                laserTowerSelected = true;
                break;

            case "lightTower":
                resetInventoryOptions();
                option = (Picture) simpleApp.getGuiNode().getChild(desiredOption);
                option.setImage(simpleApp.getAssetManager(), "Interface/lightTowerHover.png", true);
                setLightTowerText(true);
                lightTowerSelected = true;
                break;

            case "unknownTower":
                resetInventoryOptions();
                option = (Picture) simpleApp.getGuiNode().getChild(desiredOption);
                option.setImage(simpleApp.getAssetManager(), "Interface/unknownTowerHover.png", true);
                setUnknownTowerText(true);
                unknownTowerSelected = true;
                break;

            case "none":
                resetInventoryOptions();

            default:
                break;
        }
    }

    private void setLaserTowerText(boolean canSetText) {
        if (canSetText) {
            BitmapFont myFont = simpleApp.getAssetManager().loadFont("Interface/Fonts/PoorRichardBig.fnt");
            BitmapText laserTowerText = new BitmapText(myFont);
            laserTowerText.setName("laserTowerText");
            laserTowerText.setSize(myFont.getCharSet().getRenderedSize());
            laserTowerText.setText("Laser              10");
            laserTowerText.move(350, 160, 0);
            simpleApp.getGuiNode().attachChild(laserTowerText);
        }

        Spatial text = simpleApp.getGuiNode().getChild("laserTowerText");
        if (text != null) {
            text.removeFromParent();
        }
    }

    private void setLightTowerText(boolean canSetText) {
        if (canSetText) {
            BitmapFont myFont = simpleApp.getAssetManager().loadFont("Interface/Fonts/PoorRichardBig.fnt");
            BitmapText lightTowerText = new BitmapText(myFont);
            lightTowerText.setName("lightTowerText");
            lightTowerText.setSize(myFont.getCharSet().getRenderedSize());
            lightTowerText.setText("Light              15");
            lightTowerText.move(600, 160, 0);
            simpleApp.getGuiNode().attachChild(lightTowerText);
        }

        Spatial text = simpleApp.getGuiNode().getChild("lightTowerText");
        if (text != null) {
            text.removeFromParent();
        }
    }

    private void setUnknownTowerText(boolean canSetText) {
        if (canSetText) {
            BitmapFont myFont = simpleApp.getAssetManager().loadFont("Interface/Fonts/PoorRichardBig.fnt");
            BitmapText unknownTowerText = new BitmapText(myFont);
            unknownTowerText.setName("unknownTowerText");
            unknownTowerText.setSize(myFont.getCharSet().getRenderedSize());
            unknownTowerText.setText("?????              30");
            unknownTowerText.move(850, 160, 0);
            simpleApp.getGuiNode().attachChild(unknownTowerText);
        }

        Spatial text = simpleApp.getGuiNode().getChild("unknownTowerText");
        if (text != null) {
            text.removeFromParent();
        }
    }

    private void resetInventoryOptions() {
        for (int i = 0; i < optionNames.length; i++) {
            Picture option = (Picture) simpleApp.getGuiNode().getChild(optionNames[i]);
            option.setImage(simpleApp.getAssetManager(), "Interface/" + optionNames[i] + ".png", true);
        }
        laserTowerSelected = false;
        setLaserTowerText(false);
        setLightTowerText(false);
        setUnknownTowerText(false);

        lightTowerSelected = false;
        unknownTowerSelected = false;
    }

    private void addInventoryOptions() {
        for (int i = 0; i < optionNames.length; i++) {
            Picture option = new Picture(optionNames[i]);
            option.setImage(simpleApp.getAssetManager(),
                    "Interface/" + optionNames[i] + ".png",
                    true);
            option.setWidth(78);
            option.setHeight(247);
            option.move(370 + 250 * i, 175, 0);
            simpleApp.getGuiNode().attachChild(option);
        }

        /* Highlight first option */
        highlightInventoryOption("laserTower");
    }

    private void toggleInventory() {
        if (isInventoryToggled) {
            simpleApp.getFlyByCamera().setEnabled(true);
            simpleApp.getGuiNode().getChild("budgetIcon").removeFromParent();
            simpleApp.getGuiNode().getChild("budgetText").removeFromParent();
            inputManager.setCursorVisible(false);

            for (int i = 0; i < optionNames.length; i++) {
                simpleApp.getGuiNode().getChild(optionNames[i]).removeFromParent();
            }

            setLaserTowerText(false);
            setLightTowerText(false);
            setUnknownTowerText(false);

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
        Picture budgetIcon = new Picture("budgetIcon");
        budgetIcon.setImage(simpleApp.getAssetManager(), "Interface/goldbig.png", true);
        budgetIcon.setWidth(64);
        budgetIcon.setHeight(64);
        budgetIcon.move(15, 40, 0);
        simpleApp.getGuiNode().attachChild(budgetIcon);

        BitmapFont myFont = simpleApp.getAssetManager().loadFont("Interface/Fonts/PoorRichardBig.fnt");
        BitmapText budgetText = new BitmapText(myFont);
        budgetText.setName("budgetText");
        budgetText.setSize(myFont.getCharSet().getRenderedSize());
        budgetText.setText(Integer.toString(gameState.getBudget()));
        budgetText.move(92, 86, 0);
        simpleApp.getGuiNode().attachChild(budgetText);
    }

    public void setSelectedTower(String tower) {
        selectedTower = tower;
    }

    public String getSelectedTower() {
        return selectedTower;
    }
}
