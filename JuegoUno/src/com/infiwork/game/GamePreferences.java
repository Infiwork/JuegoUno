package com.infiwork.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GamePreferences {

	private static final String PREF_RECORD = "record";
    private static final String PREF_MUSIC_ENABLED = "musicenabled";
    private static final String PREF_SOUND_ENABLED = "soundenabled";
    private static final String PREFS_NAME = "com.infiwork.pickingrobots";
    private Preferences preferences;
    
	public GamePreferences(){
		preferences = Gdx.app.getPreferences(PREFS_NAME);
	}

    public boolean isSoundEffectsEnabled() {
        return preferences.getBoolean(PREF_SOUND_ENABLED, true);
    }

    public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
    	preferences.putBoolean(PREF_SOUND_ENABLED, soundEffectsEnabled);
    	preferences.flush();
    }

    public boolean isMusicEnabled() {
        return preferences.getBoolean(PREF_MUSIC_ENABLED, true);
    }

    public void setMusicEnabled(boolean musicEnabled) {  
    	preferences.putBoolean(PREF_MUSIC_ENABLED, musicEnabled);
    	preferences.flush();
    }

    public int isRecordEnabled() {
        return preferences.getInteger(PREF_RECORD, 1);
    }

    public void setVibroEnabled(int record) {
    	preferences.putInteger(PREF_RECORD, record);
    	preferences.flush();
    }
}
