package com.infiwork.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.infiwork.actors.Level;
import com.infiwork.game.GamePreferences;
import com.infiwork.game.JuegoUno;

public class ScreenMenu extends AbstractScreen{
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private TextureRegion region;
	
	private float worldWidth=80, worldHeight=45;
	public boolean volGlobalMusic;
    public boolean volGlobalSound;
	private Vector3 touchpoint;
	private BoundingBox boxStart;
	
	private Texture  buttonPlay, buttonRestar;
	private BoundingBox boxPlay, boxRestar;
	private Music backgroundMenu;
	private GamePreferences preferences;
	
	public ScreenMenu(JuegoUno game) {
        super(game);
    }
	
	@Override
	public void render(float delta) {
		camera.update();
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		
		if(Gdx.input.justTouched()){ // Controles Pause
			camera.unproject(touchpoint.set(Gdx.input.getX(),Gdx.input.getY(),0));
			if(boxStart.contains(touchpoint)) {
				game.setScreen(new ScreenGame(game));
			}
			if(boxPlay.contains(touchpoint)){
				if(volGlobalMusic){
					volGlobalMusic = false;
					System.out.println(volGlobalMusic);
					backgroundMenu.stop();
				}
				else{
					volGlobalMusic = true;
					System.out.println(volGlobalMusic);
					backgroundMenu.play();
					backgroundMenu.setLooping(true);
				}
			}
			if(boxRestar.contains(touchpoint)) {
				if(volGlobalSound){
					volGlobalSound = false;
				}
				else{
					volGlobalSound = false;
				}
			}
		}
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		batch.draw(region, 0, 0, worldWidth, worldHeight);
		batch.draw(buttonRestar, 0, 10, 5, 5);
		batch.draw(buttonPlay, 0, 20, 5, 5);
		batch.end();	
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
		camera = new OrthographicCamera(worldWidth, worldHeight);
		camera.position.set(worldWidth/2, worldHeight/2, 0);
		batch = new SpriteBatch();
		
		texture = game.manager.get("menu_screen.png");
		region = new TextureRegion(texture, 0, 0, 800, 445);
		
		boxStart = new BoundingBox(new Vector3(35,25,0), new Vector3(45, 32,0));
		touchpoint = new Vector3();
		
		buttonPlay = game.manager.get("play-on.png");
		boxPlay = new BoundingBox(new Vector3(0,10,0), new Vector3(5, 15,0));
		buttonRestar = game.manager.get("stop-on.png");
		boxRestar = new BoundingBox(new Vector3(0,20,0), new Vector3(5, 25,0));
		
		backgroundMenu = game.manager.get("audio/background_menu.ogg");
		preferences = new GamePreferences();
		volGlobalSound = preferences.isSoundEffectsEnabled();
		volGlobalMusic = preferences.isMusicEnabled();
		if(volGlobalMusic)
		backgroundMenu.play();
		backgroundMenu.setLooping(true);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		backgroundMenu.stop();
		backgroundMenu.dispose();
		preferences.setMusicEnabled(volGlobalMusic);
		preferences.setSoundEffectsEnabled(volGlobalSound);
	}

}
