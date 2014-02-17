package com.infiwork.screen;

import com.infiwork.actors.Level;
import com.infiwork.game.JuegoUno;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class ScreenGame extends AbstractScreen{
	Vector3 touchpoint;
	Texture textfondo;
	TextureRegion textReg;
	Texture texturePause;
	TextureRegion screenPause;
	Texture textureOver;
	TextureRegion screenOver;
	Texture textureComplete;
	TextureRegion screenComplete;
	Texture buttonPlay;
	BoundingBox boxPlay;
	Texture buttonRestar;
	BoundingBox boxRestar;
	Texture buttonPause;
	BoundingBox boxPause;
	Sprite fondo;
	
	Level level;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private float worldWidth=80, worldHeight=45;
	
	private int GAME_STATE;
	private int GAME_PLAY = 1;
	private int GAME_OVER = 2;
	private int GAME_PAUSE = 3;
	
	public ScreenGame(JuegoUno game) {
        super(game);
        
    }
	
	@Override
	public void render(float delta) {
		
		camera.update();
		Gdx.gl.glClearColor( 0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.enableBlending();
		batch.begin();
		
		//long time_start, time_end;
        //time_start = System.currentTimeMillis();
		switch (GAME_STATE) {
		case 1: //GAME_PLAY
			fondo.setSize(worldWidth, worldHeight);
			fondo.draw(batch);
			level.render(camera, batch);
			batch.draw(buttonPause, 75, 40, 5, 5);
			if(level.getGameOver()){
				level.musicBackgroundPauseOn();
				GAME_STATE = GAME_OVER;
			}
			if(Gdx.input.justTouched()){ // Controles Play
				camera.unproject(touchpoint.set(Gdx.input.getX(),Gdx.input.getY(),0));
				if(boxPause.contains(touchpoint)) {
					level.musicBackgroundPauseOn();
					GAME_STATE = GAME_PAUSE;
				}
			}
			break;
		case 2: //GAME_OVER
			batch.draw(screenOver, 0, 0, 80, 45);
			batch.draw(buttonRestar, 37, 20, 5, 5);
			if(Gdx.input.justTouched()){ // Controles Pause
				camera.unproject(touchpoint.set(Gdx.input.getX(),Gdx.input.getY(),0));
				if(boxRestar.contains(touchpoint)) {
					level = null;
					level = new Level(game.manager);
					GAME_STATE = GAME_PLAY;
					level.musicBackgroundPauseOff();
				}
			}
			break;
		case 3: //GAME_PAUSE
			batch.draw(screenPause, 0, 0, 80, 45);
			batch.draw(buttonRestar, 37, 20, 5, 5);
			batch.draw(buttonPlay, 30, 20, 5, 5);
			if(Gdx.input.justTouched()){ // Controles Pause
				camera.unproject(touchpoint.set(Gdx.input.getX(),Gdx.input.getY(),0));
				if(boxPlay.contains(touchpoint)){
					GAME_STATE = GAME_PLAY;
					level.musicBackgroundPauseOff();
				}
				if(boxRestar.contains(touchpoint)) {
					level = null;
					level = new Level(game.manager);
					GAME_STATE = GAME_PLAY;
					level.musicBackgroundPauseOff();
				}
			}
			break;
		case 4:
			break;
		default:
			break;
		}
		//time_end = System.currentTimeMillis();
        //System.out.println("the task has taken "+ ( time_end - time_start ) +" milliseconds");
		batch.end();	
	}
		
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		GAME_STATE = 1;
		camera = new OrthographicCamera(worldWidth, worldHeight);
		camera.position.set(worldWidth/2, worldHeight/2, 0);
		batch = new SpriteBatch();
		
		level = new Level(game.manager);
		System.out.println("hola1");
		textfondo = game.manager.get("escenario.png");
		textReg =  new TextureRegion(textfondo, 1024, 700);
		texturePause = game.manager.get("pause_screen.png");
		screenPause = new TextureRegion(texturePause, 800, 480);
		textureOver = game.manager.get("gameover_screen.png");
		screenOver = new TextureRegion(textureOver, 800, 480);
		buttonPlay = game.manager.get("play-on.png");
		boxPlay = new BoundingBox(new Vector3(30,20,0), new Vector3(35, 25,0));
		buttonRestar = game.manager.get("stop-on.png");
		boxRestar = new BoundingBox(new Vector3(37,20,0), new Vector3(43, 25,0));
		buttonPause = game.manager.get("button_pause.png");
		boxPause = new BoundingBox(new Vector3(75,40,0), new Vector3(80, 45,0));
		fondo = new Sprite(textReg);
		touchpoint = new Vector3();
		System.out.println("hola2");
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		batch.dispose();
		textfondo.dispose();
		game.manager.unload("audio/background_game.ogg");
		game.manager.unload("audio/robot_jump.ogg");
		game.manager.unload("audio/button.ogg");
		game.manager.unload("audio/ChargedSonicBoomAttack8-Bit.ogg");
		game.manager.unload("escenario.png");
        game.manager.unload("robot3.png");
        game.manager.unload("circulo.png");
        game.manager.unload("sprite_robot_azul.png");
        game.manager.unload("sprite_robot_rosa.png");
        game.manager.unload("sprite_robot_verde.png");
        game.manager.dispose();
        level.dispose();
	}

}
