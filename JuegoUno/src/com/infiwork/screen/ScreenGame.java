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

public class ScreenGame extends AbstractScreen{
	Vector3 touchpoint;
	
	Texture textfondo;
	TextureRegion textReg;
	Sprite fondo;
	
	Level level;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private float worldWidth=80, worldHeight=45;
	
	public ScreenGame(JuegoUno game) {
        super(game);
    }
	
	@Override
	public void render(float delta) {
		
		camera.update();
		Gdx.gl.glClearColor( 0.5f, 0.5f, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.enableBlending();
		batch.begin();
		long time_start, time_end;
        time_start = System.currentTimeMillis();
		fondo.setSize(worldWidth, worldHeight);
		
		fondo.draw(batch);
		
		level.render(camera, batch);
		time_end = System.currentTimeMillis();
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
		camera = new OrthographicCamera(worldWidth, worldHeight);
		camera.position.set(worldWidth/2, worldHeight/2, 0);
		batch = new SpriteBatch();
		level = new Level(game.manager);
		textfondo = game.manager.get("escenario.png");
		textReg =  new TextureRegion(textfondo, 1024, 700);
		fondo = new Sprite(textReg);
		touchpoint = new Vector3();
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
