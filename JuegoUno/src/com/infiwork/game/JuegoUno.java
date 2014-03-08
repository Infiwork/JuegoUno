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
    public float volGlobalMusic = 1;
    public float volGlobalSound = 1;
    public Preferences preferences;
   

    @Override
    public void create() {
        setScreen(new ScreenLoading(this));
        preferences = Gdx.app.getPreferences("pickingRobotsPreferences");
     
        
        
    }
    
    public float getVolGobalMusic(){
    	return volGlobalMusic;
    }
    
    public float getVolGobalSound(){
    	return volGlobalSound;
    }
    
    public void setVolGobalMusic(float vol){
    	volGlobalMusic = vol;
    }
    
    public void setVolGobalSound(float vol){
    	volGlobalSound = vol;
    }
}