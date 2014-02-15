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
	Teleport tele1;
	Teleport tele2;
	
	//Datos
	Stack<Integer> selectedTemp;
	Stack<Integer> robotsExploited;
	ArrayList<Robot> robots;
	//ArrayList<Teleport> tele;
	
	//Variables
	public boolean GameOver = false;
	public boolean gameExplosion = false;
	public boolean gameTeleport = false;
	public boolean gameRobotsExplosion = false;
	public boolean gameRobotsRender = true;
	public float time = 0;
	public float deltaTime = 0;
	public int robotExploited;
	public int robotTeleport;
	
	private int COLOR_BLUE = 1;
	private int COLOR_GREEN = 2;
	
	//Assets
	AssetManager manager;
	Music backgroundGame;
	
	public Level(AssetManager manager){
		robots = new ArrayList<Robot>();
		tele1 = new Teleport(0 , 20, COLOR_GREEN, manager);
		tele2 = new Teleport(70, 20, COLOR_BLUE, manager);
		selectedTemp = new Stack<Integer>();
		robotsExploited = new Stack<Integer>();
		this.manager = manager;
		musicBackgroundGame();
	}
	
	public void render(Camera camera, SpriteBatch batch){
		long time_start, time_end;
        time_start = System.currentTimeMillis();
        deltaTime = Gdx.graphics.getDeltaTime();
        time += deltaTime;
        tele1.getSprite().draw(batch);
		tele2.getSprite().draw(batch);
		
		if(robots.size()<10) respawnRobots(deltaTime);
		
		
		 for (int i = 0; i< robots.size() ; i++){
			 	robots.get(i).live(camera);
			 	batch.draw(robots.get(i).getFrameRun(), robots.get(i).getX(), robots.get(i).getY(), 10, 12);
			 	//Codigo de colisiones para los panel2
				if(robots.get(i).getPosition().dst(tele1.getPosition())<=7){
					robots.get(i).collisionX();
					robots.get(i).collisionY();
				}
			 	//Codigo de colisiones para los panel2
				if(robots.get(i).getPosition().dst(tele2.getPosition())<=7){
					robots.get(i).collisionX();
					robots.get(i).collisionY();
				}
				//	Guardar robots presionados temporalmente
				if(robots.get(i).getRobotTouched()){
					selectedTemp.push(i);
				}
				// Eliminar robot al soltarlo cerca del teleporter
				if(robots.get(i).getRobotDropped()){
					if(robots.get(i).getPosition().dst(tele2.getPosition())<6.9){
						gameTeleport = true;
						robotTeleport = i;
					}
					if(robots.get(i).getPosition().dst(tele1.getPosition())<6.9){
						gameTeleport = true;
						robotTeleport = i;
					}
				}
				// Mostrar alerta de explosion
				if(robots.get(i).getRobotAlert())
					batch.draw(robots.get(i).getAlertTexture(), robots.get(i).getX()+4, robots.get(i).getY()+6, 2, 2);
			
				//Explosion por tiempo de robot
				if(robots.get(i).getRobotDestroy()){
					gameExplosion = true;
					robotExploited = robotsExploited.push(i);;
				}
		 }
	
		 //Se ejecuta solo si existe un grupo de robots seleccionados
		if(!selectedTemp.empty())robotSelected();
		
		if(gameTeleport) robotTeleport(robotTeleport);
		if(gameExplosion) robotExplosion();
		time_end = System.currentTimeMillis();
	      // System.out.println("Tiempo total "+ ( time_end - time_start ) +" milliseconds");
	}
	float tempTime = 0;
	public void respawnRobots(float deltaTime){
		int tempPlace = 1;
		tempPlace = MathUtils.random(1, 2);
		tempTime += deltaTime;
		if(tempTime >= 2){
			if(tempPlace==1)
			robot = new Robot(35,40,MathUtils.random(205, 335), COLOR_BLUE , manager);
			if(tempPlace==2)
			robot = new Robot(35,-10,MathUtils.random(35, 165), COLOR_GREEN ,manager);
			robots.add(robot);
			tempTime=0;
		}
        
	}
	
	public void robotSelected(){
		int x=-1, temp;
		 while(!selectedTemp.empty()){
			 temp=selectedTemp.pop();
			 if(temp>x){
				 x=temp;
			 }
		 }
		 robots.get(x).setRobotElected(true);
		 robots.get(x).soundSelectRobot();
	}
	
	public void robotExplosion(){
		while(!robotsExploited.empty()){
			int i = robotsExploited.pop();
			robots.remove(i);
			robots.get(i).soundExplosionRobot();
		}
		this.gameExplosion = false;
		robotsExplosion();
	}
	
	public void robotsExplosion(){
		for (int i = 0; i< robots.size() ; i++){
			robots.get(i).setRobotExplosion(true);
		}
		this.gameRobotsExplosion = false;
	}
	
	public void robotTeleport(int numberRobot){
		robots.remove(numberRobot);
		this.gameTeleport = false;
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
		backgroundGame.dispose();
	}
}
