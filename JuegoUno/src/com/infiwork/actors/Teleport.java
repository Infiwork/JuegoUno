package com.infiwork.actors;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

public class Teleport {
	private float x, y;
	private float width=10, height=10;
	
	private Texture texture;
	private Sprite sprite;
	
	private Vector3 position;
	
	
	public Teleport(AssetManager manager){
		x=10; y=10;
		
		position = new Vector3(x+(width/2),y+(height/2),0);
		texture = manager.get("circulo.png");
		sprite = new Sprite(texture);
		
		//Sprite
		sprite.setSize(width, height);
		sprite.setPosition(x, y);
	}
	
	public Sprite getSprite(){
		return sprite;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public Vector3 getPosition(){
		return position;
	}
}