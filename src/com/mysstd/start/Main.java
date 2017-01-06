package com.mysstd.start;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.mysttd.intro.IntroMenu;

public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();

        AppSettings screenSettings = new AppSettings(true);
        screenSettings.setTitle("Myst Td");
        screenSettings.setResolution(1280, 600);
        screenSettings.setFullscreen(false);

        app.setSettings(screenSettings);
        app.setShowSettings(false);
        app.setPauseOnLostFocus(true);

        app.start();

    }

    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);
        flyCam.setDragToRotate(false);

        stateManager.attach(new IntroMenu());


    }
}
