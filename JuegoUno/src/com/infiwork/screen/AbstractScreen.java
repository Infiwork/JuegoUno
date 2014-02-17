package com.infiwork.screen;

import com.badlogic.gdx.Screen;
import com.infiwork.game.JuegoUno;

	/**
	 * @author Uriel
	 */
	public abstract class AbstractScreen implements Screen {

	    protected JuegoUno game;

	    public AbstractScreen(JuegoUno game) {
	        this.game = game;
	    }

	    @Override
	    public void pause() {
	    }

	    @Override
	    public void resume() {
	    }

	    @Override
	    public void dispose() {
	    }

	}