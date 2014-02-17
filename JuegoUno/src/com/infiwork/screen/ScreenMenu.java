package com.infiwork.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.infiwork.actors.Level;
import com.infiwork.game.JuegoUno;

public class ScreenMenu extends AbstractScreen{
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private TextureRegion region;
	private float worldWidth=80, worldHeight=45;
	private Vector3 touchpoint;
	private BoundingBox boxStart;
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
		}
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		batch.draw(region, 0, 0, worldWidth, worldHeight);
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
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
		
	}

}
