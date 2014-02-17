package com.infiwork.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.infiwork.game.JuegoUno;
import com.infiwork.loading.LoadingBar;

public class ScreenX extends AbstractScreen {

	private Stage stage;

    private Image logo;
    private Image loadingFrame;
    private Image loadingBarHidden;
    private Image screenBg;
    private Image loadingBg;

    private float startX, endX;
    private float percent;

    private Actor loadingBar;
    
	public ScreenX(JuegoUno game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(float delta) {
		 // Clear the screen
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        if (game.manager.update()) { // Load some, will return true if done loading
                game.setScreen(new ScreenGame(game));
        }

        // Interpolate the percentage to make it more smooth
       // percent = Interpolation.linear.apply(percent, game.manager.getProgress(), 0.1f);

       System.out.println(game.manager.getProgress());
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		
        game.manager.load("juego.png", Texture.class);
        //Assets for DisplayGame
        game.manager.load("button_pause.png", Texture.class);
        game.manager.load("gameover_screen.png", Texture.class);
        game.manager.load("pause_screen.png", Texture.class);
        game.manager.load("complete_screen.png", Texture.class);
        game.manager.load("play-on.png", Texture.class);
        game.manager.load("stop-on.png", Texture.class);
        game.manager.load("menu_screen.png", Texture.class);
        //Assets for Game
        game.manager.load("audio/background_game.ogg",Music.class);
        game.manager.load("audio/button.ogg",Sound.class);
        game.manager.load("audio/robot_jump.ogg",Sound.class);
        game.manager.load("audio/ChargedSonicBoomAttack8-Bit.ogg",Sound.class);
        game.manager.load("escenario.png", Texture.class);
        game.manager.load("robot3.png", Texture.class);
        game.manager.load("circulo.png", Texture.class);
        //game.manager.load("sprite_volando.png", Texture.class);
        game.manager.load("sprite_robot_azul.png", Texture.class);
        game.manager.load("sprite_robot_verde.png", Texture.class);
        game.manager.load("sprite_robot_rosa.png", Texture.class);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

}
