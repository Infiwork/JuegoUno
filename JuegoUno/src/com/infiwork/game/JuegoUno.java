package com.infiwork.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.infiwork.screen.ScreenLoading;
import com.infiwork.screen.ScreenX;

public class JuegoUno extends Game {

    /**
     * Holds all our assets
     */
    public AssetManager manager = new AssetManager();

    @Override
    public void create() {
        setScreen(new ScreenLoading(this));
    }
}