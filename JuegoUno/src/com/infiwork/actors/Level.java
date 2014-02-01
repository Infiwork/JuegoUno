package com.infiwork.actors;

import java.util.ArrayList;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Level {
	
	// Objetos
	Robot robot;
	Teleport tele;
	
	//Datos
	Stack<Integer> selectedTemp;
	ArrayList<Robot> robots;
	
	//Variables
	boolean GameOver=false;
	float time = 0;
	
	//Assets
	AssetManager manager;
	Music backgroundGame;
	
	public Level(AssetManager manager){
		long time_start, time_end;
        time_start = System.currentTimeMillis();
		robots = new ArrayList<Robot>();
		time_end = System.currentTimeMillis();
        System.out.println("Tiempo total "+ ( time_end - time_start ) +" milliseconds");
		tele = new Teleport(manager);
		selectedTemp = new Stack<Integer>();
		this.manager = manager;
		musicBackgroundGame();
		
	}
	
	public void render(Camera camera, SpriteBatch batch){
		time += Gdx.graphics.getDeltaTime();
		tele.getSprite().draw(batch);
		
		if(((int) time)%5==0)
			if(robots.size()<5) respawnRobots();
		
		// Codigo para tocar solo un robot
		 for (int i = 0; i< robots.size() ; i++){
			 	robots.get(i).live(camera);
			 	batch.draw(robots.get(i).getFrameRun(), robots.get(i).getX(), robots.get(i).getY(), 10, 12);
				//Codigo de colisiones para los paneles
				if(robots.get(i).getPosition().dst(tele.getPosition())<=7){
					robots.get(i).collisionX();
					robots.get(i).collisionY();
				}
				//	Guardar robots presionados temporalmente
				if(robots.get(i).getRobotTouched()){
					selectedTemp.push(i);
				}
				// Eliminar robot al solarlo cerca del teleporter
				if(robots.get(i).getRobotDropped())
				if(robots.get(i).getPosition().dst(tele.getPosition())<6.5){
					robots.remove(i);
					break;
				}
				
				// Mostrar alerta de explosion
				if(robots.get(i).getRobotAlert()){
					batch.draw(robots.get(i).getAlertTexture(), robots.get(i).getX()+4, robots.get(i).getY()+4, 2, 2);
					robots.get(i).setRobotAlert(false);
				}
				
				if(robots.get(i).getRobotExplosion()){
					robots.get(i).soundExplsionRobot();
					robots.remove(i);
				}
		 }
		 //Se ejecuta solo si existe un grupo de robots seleccionados
		if(!selectedTemp.empty())
			robotSelected();
		
		
	}
	
	public void respawnRobots(){
        robot = new Robot(40,0,MathUtils.random(35, 165), manager);
		robots.add(robot);
	}
	
	public void robotSelected(){
		int x=-1, temp;
		 while(!selectedTemp.empty()){
			 temp=selectedTemp.pop();
			 System.out.println(temp);
			 if(temp>x){
				 x=temp;
			 }
		 }
		 robots.get(x).setRobotElected(true);
		 robots.get(x).soundSelectRobot();
	}
	
	public void musicBackgroundGame(){
		backgroundGame = manager.get("audio/background_game.ogg");
		backgroundGame.play();
		backgroundGame.setLooping(true);
	}
	
	public void dispose(){
		for (int i = 0; i< robots.size() ; i++){
			robots.get(i).disposeAssets();
		}
		robots.clear();
		selectedTemp.clear();
	}
}
