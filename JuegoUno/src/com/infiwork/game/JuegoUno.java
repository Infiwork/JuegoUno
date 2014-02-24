package com.infiwork.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.infiwork.screen.ScreenLoading;
import com.infiwork.screen.ScreenX;

public class JuegoUno extends Game {

    /**
     * Holds all our assets
     */
    public AssetManager manager = new AssetManager();
    //public Preferences prefs = Gdx.app.getPreferences("my-preferences");

    @Override
    public void create() {
        setScreen(new ScreenLoading(this));
    }
}