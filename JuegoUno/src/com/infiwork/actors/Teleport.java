package com.infiwork.actors;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

public class Teleport {
	private float x, y;
	private int color;
	private float width=10, height=10;
	
	private Texture texture;
	private Sprite sprite;
	
	private Vector3 position;
	
	
	public Teleport(float x, float y, int color, AssetManager manager){
		this.x=x; this.y=y;
		
		position = new Vector3(x+(width/2),y+(height/2),0);
		texture = manager.get("circulo.png");
		sprite = new Sprite(texture);
		setColor(color);
		
		//Sprite
		sprite.setSize(width, height);
		sprite.setPosition(x, y);
	}
	
	public float getColor(){
		return color;
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
	
	public void setColor(int color){
		switch(color) {
		case 1:
			this.color = color;
			break;
		case 2:
			this.color = color;
			break;
		default:
			this.color = color;
			break;
		}
	}
}