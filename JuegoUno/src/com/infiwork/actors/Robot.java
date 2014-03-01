package com.infiwork.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class Robot {
	private float x, y, speedX, speedY, rotation;
	private float speedGlobal = 5f;
	private float spriteWidth = 10, spriteHeight = 10;
	private float countDown = 10;
	private float countTemp = 0;
	private float worldWidthStart=10, worldWidthEnd=70;
	private float worldHeightStart=0, worldHeightEnd=38;
	private float worldWidth=80, worldHeight=38;
	private int color;
	private boolean robotCreated = false;
	private boolean robotTouched = false;
	private boolean robotElected = false;
	private boolean robotDropped = false;
	private boolean robotMove = true;
	private boolean switchDropped= false;
	private boolean robotExplosion = false;
	private boolean robotDestroy = false;
	private boolean robotAlert = false;
	private boolean robotTeleported = false;
	
	private boolean robotPower = false;
	private int power;
	private String powerTextureName;
	private Texture powerTexture;
	
	private float deltaTime;
	private float timeFrames = 0;
	private Vector3 position, origin;
	private String textureName; 
	
	private Animation runAnimation;
	private Texture runTexture;
	private TextureRegion[] runTextureRegion;
	private TextureRegion runFrame;
	private Texture alertTexture;
	
	//private TextureRegion t;
	
	private Sound touchRobot;
	private Sound alertExplosion;
	
	
	private Vector3 touchpoint = new Vector3();
	
	public Robot(float x, float y, float rotation, int color, AssetManager manager){
		//propiedades robot
		
		this.x = x; this.y = y;
		this.rotation = rotation;
		setSpeedX(rotation, speedGlobal);
		setSpeedY(rotation, speedGlobal);
		
		touchRobot = manager.get("audio/robot_jump.ogg");
		alertExplosion = manager.get("audio/button.ogg");
		
		origin = new Vector3(spriteWidth/2,spriteHeight/2,0);
		position = new Vector3(x+origin.x,y+origin.y,0);
		
		this.setPower();
		if(robotPower)
		powerTexture = manager.get(this.getPowerTextureName());
		this.setColor(color);
		createRunAnimation(manager.get(getTextureName(), Texture.class));
		
		alertTexture = manager.get("alert.png");
	}
	
	public void live(Camera camera){
		setRobotTouched(false);
		setRobotDropped(false);
		
		if(Gdx.input.justTouched()){
			camera.unproject(touchpoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			setRobotTouched(justTouch(touchpoint));	
		}
		// <-- Sale a avisar que fue tocado
		if(robotExplosion){
			exploitingRobot();
			this.robotTouched = false;
			this.robotElected = false;
			this.robotCreated = false;
		}
		// --> Regresa si es el unico
		if(getRobotElected()){ // ESTADO 2 - Robot presionado
			setRobotElected(Gdx.input.isTouched());
			camera.unproject(touchpoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			setX(touchpoint.x); 
			setY(touchpoint.y);
			switchDropped = true;
		}
		else{
			if(switchDropped){
				setRobotDropped(true);
				switchDropped=false;
			}
			if(robotCreated){
				run();
			}
			else
				enterToWorld();
		}
		
		x=position.x-origin.x;
		y=position.y-origin.y;
		
	}
	
	public void run(){
		// Choque en eje X
		if((position.x >= worldWidth||position.x < 0)){
			speedX*=-1;
		}
		
		// Choque en eje Y 
		if((position.y >= worldHeight||position.y < 0) ){
			speedY*=-1;
		}
		
		if(getRobotTouched()){
			
		}
		else{ // ESTADO 2 - Caminar
			if(robotMove)
			this.move();
		}
	
	}
	
	private void move(){
		deltaTime = Gdx.graphics.getDeltaTime();
		position.y+=(speedY*deltaTime);
		position.x+=(speedX*deltaTime);
		
		if(!robotExplosion)
		explosionCountDown(deltaTime);
		
		timeFrames += (deltaTime/8);
		runFrame = runAnimation.getKeyFrame(timeFrames,true);
	}
	
	private void enterToWorld(){
		deltaTime = Gdx.graphics.getDeltaTime();
		if(position.y >= worldHeight)
			position.y-=(speedGlobal+2)*deltaTime;
		
		if(position.y <= 0)
			position.y+=(speedGlobal+2)*deltaTime;
		
		if(position.y < worldHeight && position.y > 0 )
			robotCreated = true;
			
			timeFrames += (deltaTime/8);
			runFrame = runAnimation.getKeyFrame(timeFrames,true);
	}
	float timeTemp=0;
	private void exploitingRobot(){
		deltaTime = Gdx.graphics.getDeltaTime();
		timeTemp+=deltaTime;
		if(timeTemp>=1.5){
			this.robotDestroy = true;
			timeTemp=0;
		}
	}
	
	private void createRunAnimation(Texture texture){
		runTexture = texture;
		TextureRegion[][] tmp = TextureRegion.split(runTexture,200, 290);
		runTextureRegion = new TextureRegion[5];
		for (int i = 0; i < 5; i++) {
			runTextureRegion[i] = tmp[0][i];
		}
		runAnimation = new Animation (0.025f, runTextureRegion);
	}
	
	public void collisionX(){
		speedX*=-1;
	}
	
	public void collisionY(){
		speedY*=-1;
	}
	
	public void disposeAssets(){
		runTexture.dispose();
	}
	
	private void explosionCountDown(float delta){
		countDown-=delta;
		countTemp+=delta;
		if(countDown<=5){ // Estado de alerta 
			//System.out.println("if countTemp "+countTemp+" > counDown "+ countDown + " /diez " + (countDown));
			if(countTemp > (countDown/5)){
				soundAlertExplosion();
				this.robotAlert = true;
				countTemp=-.2f;
			}
			if(countTemp >= 0) this.robotAlert = false;
			
			if(countDown <= 0) this.robotExplosion = true;
		}	
	}

	public Texture getAlertTexture(){
		return alertTexture;
	}
	
	public int getColor(){
		return this.color;
	}
	
	public TextureRegion getFrameRun(){
		return runFrame;
	}

	public Vector3 getPosition(){
		return position;
	}
	
	public int getPower(){
		return power;
	}
	
	public Texture getPowerTexture(){
		return powerTexture;
	}
	
	private String getPowerTextureName(){
		return powerTextureName;
	}
	
	public boolean getRobotAlert(){
		return robotAlert;
	}
	
	public boolean getRobotDestroy(){
		return robotDestroy;
	}
	
	public boolean getRobotDropped(){
		return robotDropped;
	}
	
	public boolean getRobotElected(){
		return robotElected;
	}
	
	public boolean getRobotExplosion(){
		return robotExplosion;
	}
	
	public boolean getRobotPower(){
		return robotPower;
	}
	
	public boolean getRobotTouched(){
		return robotTouched;
	}
	
	public float getRobotSpeedGlobal(){
		return speedGlobal;
	}
	
	public float getRotation(){
		return rotation;
	}
	
	private String getTextureName(){
		return textureName;
	}
	
	public float getSpeedX(){
		return speedX;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public boolean justTouch(Vector3 vector){
		if(position.dst(vector)<=5){
			return true;
		}
		else
			return false;
	}
	
	private void setPower(){
		this.power = MathUtils.random(1, 5);
		if(power>3)
			this.robotPower= false;	
		
		else{
			robotPower = true;
			switch (power) {
			case 1:
				powerTextureName="laser.png";
				break;
			case 2:
				powerTextureName="laser.png";
				break;
			case 3:
				powerTextureName="laser.png";
				break;
			default:
				break;
			}
		}
	}
	
	public void setRobotAddSpeedGlobal(float speed){
		//System.out.println("sx "+speedX+" sy "+speedY+"   sp  "+speed);
		speedGlobal+=speed;
		setSpeedX(rotation, speedGlobal);
		setSpeedY(rotation, speedGlobal);
		//System.out.println("sx "+speedX+" sy "+speedY);
	}
	
	public void setRobotAlert(boolean robotAlert){
		this.robotAlert = robotAlert;
	}
	
	public void setRobotDestroy(boolean robotDestroy){
		this.robotDestroy = robotDestroy;
	}
	
	public void setRobotDropped(boolean robotDropped){
		this.robotDropped = robotDropped;
	}
	
	public void setRobotElected(boolean robotElected){
		this.robotElected = robotElected;
	}
	
	public void setRobotExplosion(boolean robotExplosion){
		this.robotExplosion = robotExplosion;
	}
	
	public void setRobotMove(boolean robotMove){
		this.robotMove = robotMove;
	}
	
	public void setRobotTouched(boolean robotTouched){
		this.robotTouched = robotTouched;
	}
	
	
	public void setSpeedGlobal(float speed){
		speedGlobal=speed;
	}
	
	public void setSpeedX(float rotation, float speed){
		speedX = MathUtils.cosDeg(rotation)*speed;
	}
	
	public void setSpeedY(float rotation, float speed){
		speedY = MathUtils.sinDeg(rotation)*speed;
	}

	private void setColor(int color){
		switch(color) {
		case 1:
			this.color = color;
			textureName = "sprite_robot_azul.png";
			break;
		case 2:
			this.color = color;
			textureName = "sprite_robot_verde.png";
			break;
		default:
			this.color = color;
			textureName = "sprite_robot_azul.png";
			break;
		}
	}
	
	public void setX(float x){
		if(x <= worldWidth && x >= 0)
		position.x = x;
	}
	
	public void setY(float y){
		if(y <= worldHeight && y >= 0)
		position.y= y;
	}
	
	public void soundSelectRobot(){
		alertExplosion.play(1.0f);
	}
	
	public void soundAlertExplosion(){
		touchRobot.play(1.0f);
	}
	
	public void soundExplosionRobot(){
		
	}
}
