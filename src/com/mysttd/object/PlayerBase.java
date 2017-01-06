package com.mysttd.object;

import com.jme3.scene.Node;

public class PlayerBase {

    private Node base;

    public PlayerBase(int hp, Node rootNode) {

        base = (Node) rootNode.getChild("Home");
        base.setUserData("health", hp);

    }

    public Node getBase() {
        return base;
    }

    public int getBaseHp() {
        return base.getUserData("health");
    }

    public void removeHealth(int hp) {

        if (hp < 0) {
            throw new IllegalStateException("Should be a positive int.");
        }

        base.setUserData("health", getBaseHp() - hp);
    }
}
