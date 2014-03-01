package com.infiwork.actors;

import java.util.ArrayList;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
	
	//Global
	public int Level = 0;
	public float time = 0;
	public float deltaTime = 0;
	//Estates
	public boolean GameOver = false;
	public boolean LevelComplete = false;
	public boolean gameExplosion = false;
	public boolean gameTeleport = false;
	public boolean gameRobotsCreate = true;
	public boolean gameRobotsRender = true;
	public boolean gameRespawn = true;
	public boolean gameRobotsSetSpeed = true;
	public float gameRobotIncrementSpeed = 0;
	//Powers
	public boolean powerOne = false;
	public float powerOneTime = 0;
	public int powerOneCount = 1;
	public boolean powerTwo = false;
	public float powerTwoTime = 0;
	public int powerTwoCount = 1;
	public boolean powerThree = false;
	public float powerThreeTime = 0;
	public int powerThreeCount = 1;
	//Robots 
	public int robotExploited;
	public int robotTeleport;
	//Respawn
	public int countLevelRobots = 0;
	public int countInternalLevelRobots = 0;
	private float decimalLevelComplete = 0;
	private float dividerRank = 512;
	private float maxRankDelay = 0;
	private float RankDelay = 100;
	private float minRankDelay = 556;
	private int percentLevelComplete = 0;
	private int positionRespawn;
	public int totalLevelRobots = 0;
	private float timeSpanRespawn = 0;
	private float tempRespawnTime = 0;
	//Constantes
	private int COLOR_BLUE = 1;
	private int COLOR_GREEN = 2;
	private int POS_TOP = 1;
	private int POS_BOTTOM = 2;
	
	//Assets
	private AssetManager manager;
	private Music backgroundGame;
	private Sound destroyRobot;
	
	public Level(AssetManager manager){
		robots = new ArrayList<Robot>();
		tele1 = new Teleport(0 , 20, COLOR_GREEN, manager);
		tele2 = new Teleport(70, 20, COLOR_BLUE, manager);
		selectedTemp = new Stack<Integer>();
		robotsExploited = new Stack<Integer>();
		this.manager = manager;
		musicBackgroundGame();
		destroyRobot = manager.get("audio/ChargedSonicBoomAttack8-Bit.ogg");	
	}
	
	public void render(Camera camera, SpriteBatch batch){
		//long time_start, time_end;
        //time_start = System.currentTimeMillis();
        deltaTime = Gdx.graphics.getDeltaTime();
        time += deltaTime;
        tele1.getSprite().draw(batch);
		tele2.getSprite().draw(batch);
		
		if(countLevelRobots==totalLevelRobots) nextLevel();
		if(gameRespawn) respawnCore(deltaTime);
		
		 for (int i = 0; i< robots.size() ; i++){
			 	robots.get(i).live(camera);
			 	batch.draw(robots.get(i).getFrameRun(), robots.get(i).getX(), robots.get(i).getY(), 10, 12);
			 	if(robots.get(i).getRobotPower())
			 	batch.draw(robots.get(i).getPowerTexture(), robots.get(i).getX(), robots.get(i).getY(), 2, 2);
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
				// Acciones de robot al soltarlo cerca del teleporter
				if(robots.get(i).getRobotDropped()){
					if(robots.get(i).getPosition().dst(tele2.getPosition())<6.9){
						if(tele2.getColor()==robots.get(i).getColor()){ // Teletranportar si es del mismo color
							gameTeleport = true;
							robotTeleport = i;
						}
						else robots.get(i).setRobotExplosion(true);	// De lo contrario elminarlo					
						
					}
					if(robots.get(i).getPosition().dst(tele1.getPosition())<6.9){
						if(tele1.getColor()==robots.get(i).getColor()){ // Teletranportar si es del mismo color
							gameTeleport = true;
							robotTeleport = i;
						}
						else robots.get(i).setRobotExplosion(true); // De lo contrario eliminarlo
					}
				}
				// Mostrar alerta de explosion
				if(robots.get(i).getRobotAlert())
					batch.draw(robots.get(i).getAlertTexture(), robots.get(i).getX(), robots.get(i).getY(), 10, 12);
			
				//Explosion si el robot lo indica
				if(robots.get(i).getRobotDestroy()){
					gameExplosion = true;
					robotExploited = robotsExploited.push(i);;
				}
		 }
	
		 //Se ejecuta solo si existe un grupo de robots seleccionados
		if(!selectedTemp.empty())robotSelected();
		
		if(gameTeleport) robotTeleport(robotTeleport);
		if(gameExplosion) robotExplosion();
		if(powerOne) powerOne(deltaTime);
		if(powerTwo) powerTwo(deltaTime);
		if(powerThree) powerThree(deltaTime);
		//time_end = System.currentTimeMillis();
	    //System.out.println("Tiempo total "+ ( time_end - time_start ) +" milliseconds");
	}
	
	
	public void dispose(){
		for (int i = 0; i< robots.size() ; i++){
			robots.get(i).disposeAssets();
		}
		robots.clear();
		selectedTemp.clear();
		backgroundGame.dispose();
	}
	
	public int getCountLevelRobts(){
		return this.countLevelRobots;
	}
	
	public float getDecimalLevelRobots(){
		this.decimalLevelComplete = (float) countLevelRobots/totalLevelRobots;
		return decimalLevelComplete;
	}
	
	public boolean getGameOver(){
		return this.GameOver;
	}
	
	public int getLevel(){
		return this.Level;
	}
	
	public int getTotalLevelRobots(){
		return this.totalLevelRobots;
	}
	
	public int getPercentLevelRobots(){
		this.percentLevelComplete = (100*countLevelRobots)/totalLevelRobots;
		return percentLevelComplete;
	}
	
	private int getPositionRespawnRobots(int value){
		int position = 0;
		switch (value) {
		case 1:
			position = 40;
			break;
		case 2:
			position = -10;
			break;
		default:
			position = 40;
			break;
		}
		return position;
	}
	
	public int getPowerOneCount(){
		return powerOneCount;
	}
	
	public int getPowerTwoCount(){
		return powerTwoCount;
	}
	public int getPowerThreeCount(){
		return powerThreeCount;
	}
	
	public void powerOne(float deltaTime){
		if(powerOneTime == 0){
			for (int i = 0; i< robots.size() ; i++)
				robots.get(i).setRobotMove(false);
			powerOneTime+=1;
			this.powerOneCount--;
		}
		else{
			powerOneTime+=deltaTime;
			if(powerOneTime>=4){
				for (int i = 0; i< robots.size() ; i++)
					robots.get(i).setRobotMove(true);
				this.powerOne = false;
				this.powerOneTime=0;
			}
		}
	}
	
	public void powerTwo(float deltaTime){
		if(powerTwoTime==0){
			this.gameRespawn= false;
			powerTwoTime+=1;
			this.powerTwoCount--;
		}
		else{
			powerTwoTime+= deltaTime;
			if(powerTwoTime>=4){
				this.gameRespawn = true;
				this.powerTwo=false;
			}
		}
	}
	
	public void powerThree(float deltaTime){
		if(powerThreeTime == 0)
			this.powerThreeCount--;
		powerThreeTime+= deltaTime;
		if(powerThreeTime>=3){
			this.powerThree=false;
			this.powerThreeTime = 0;
		}
	}
	
	public void respawnCore(float deltaTime){
		float tempMinRankDelay;
		float tempMaxRankDelay;
		
		this.tempRespawnTime += deltaTime;
		
		if(tempRespawnTime >= timeSpanRespawn){
			countInternalLevelRobots++;
			this.tempRespawnTime = 0;
			this.respawnRobots();
			tempMaxRankDelay = maxRankDelay;
			if(percentLevelComplete>=75)
				tempMaxRankDelay = minRankDelay + 5;
			this.timeSpanRespawn = ((MathUtils.random(minRankDelay, tempMaxRankDelay))*.01f);
			//System.out.println("minRankDelay = "+minRankDelay+"  maxRankDelay = "+tempMaxRankDelay+"   tiem = "+timeSpanRespawn);
		}
		if(countInternalLevelRobots==totalLevelRobots) {
			gameRespawn=false;
		}
	}
	
	public void respawnRobots(){
		int tempPos;
		int tempColor;
		int tempMinAng = 0;
		int tempMaxAng = 0;

		tempPos = MathUtils.random(1, 2);
		positionRespawn = getPositionRespawnRobots(tempPos);
		switch (tempPos) {
		case 1: // Arriba
			tempMinAng = 205;
			tempMaxAng = 335;
			break;
		case 2:
			tempMinAng = 35;
			tempMaxAng = 165;
			break;
		default:
			break;
		}
		tempColor = MathUtils.random(1, 2);
		robot = new Robot(35, positionRespawn ,MathUtils.random(tempMinAng, tempMaxAng), tempColor ,manager);
		if(gameRobotsSetSpeed) 
			robot.setRobotAddSpeedGlobal(gameRobotIncrementSpeed);
		robots.add(robot);
	}
	
	public void robotSelected(){
		int x=-1, temp;
		 while(!selectedTemp.empty()){
			 temp=selectedTemp.pop();
			 if(temp>x){
				 x=temp;
			 }
		 }
		 if(powerThree){
			this.gameTeleport = true;
			this.robotTeleport = x;
		 }
		 else{
			 robots.get(x).setRobotElected(true);
			 robots.get(x).soundSelectRobot();
		 }
	}
	
	public void robotExplosion(){
		while(!robotsExploited.empty()){
			int i = robotsExploited.pop();
			robots.remove(i);
		}
		this.soundExplosionRobot();
		this.gameExplosion = false;
		this.gameRespawn = false;
		if(robots.isEmpty())
			this.GameOver=true;
		else
			robotsExplosion();
	}
	
	public void robotsExplosion(){
		for (int i = 0; i< robots.size() ; i++){
			robots.get(i).setRobotExplosion(true);
		}
	}
	
	public void robotTeleport(int numberRobot){
		powerUp(robots.get(numberRobot).getPower());
		robots.remove(numberRobot);
		countLevelRobots++;
		this.gameTeleport = false;
	}
	
	public void musicBackgroundGame(){
		backgroundGame = manager.get("audio/background_game.ogg");
		backgroundGame.play();
		backgroundGame.setLooping(true);
	}
	
	public void musicBackgroundPauseOn(){
		backgroundGame.setVolume(.25f);
	}
	
	public void musicBackgroundPauseOff(){
		backgroundGame.setVolume(1);
	}
	
	public void nextLevel(){
		Level++;
		totalLevelRobots = 4 + (Level*Level);
		countLevelRobots = 0;
		countInternalLevelRobots = 0;
		timeSpanRespawn = 2.5f;
		if(RankDelay > 40){
			RankDelay -= 10;
		}
		if(dividerRank >= 32){
			dividerRank = dividerRank / 2;
		}
		if(minRankDelay>=30){
			minRankDelay = minRankDelay - dividerRank;
			maxRankDelay = minRankDelay + RankDelay;
		}
		if(gameRobotIncrementSpeed < 3){	
			gameRobotIncrementSpeed += .5f;
			System.out.println(gameRobotIncrementSpeed);
		}
		else{
			gameRobotsSetSpeed = false;
		}
		System.out.println();
		System.out.println("dividerRank = "+dividerRank+"   Level = "+Level);
		System.out.println("minRankDelay = "+minRankDelay+"  maxRankDelay = "+maxRankDelay+"   RankDelay = "+RankDelay);
		gameRespawn = true;
	}
	
	public void powerUp(int var){
		if(var<=3)
		switch (var) {
		case 1:
			System.out.println("1"+var);
			this.powerOneCount++;
			break;
		case 2:
			System.out.println("2"+var);
			this.powerTwoCount++;
			break;
		case 3:
			System.out.println("3"+var);
			this.powerThreeCount++;
			break;
		default:
			break;
		}
	}
	
	public void setPowerOne(boolean var){
		this.powerOne = true;
	}
	
	public void setPowerTwo(boolean var){
		this.powerTwo = true;
	}
	
	public void setPowerThree(boolean var){
		this.powerThree = true;
	}
	
	public void soundExplosionRobot(){
		destroyRobot.play(1.0f);
	}
	
}