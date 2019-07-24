package framework.core;

import object.LightSpot;
import object.Obstacle;
import object.enemy.Enemy_Basic;
import object.player.Player;

import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;

import framework.entity.GameEntity;
import network.NetworkPlayer;

public class Handler implements Serializable{
	
	private static final long serialVersionUID = 7764178927884827307L;
	public CopyOnWriteArrayList<GameEntity> obstacleList;
	public CopyOnWriteArrayList<Enemy_Basic> enemyList;
	public CopyOnWriteArrayList<LightSpot> lightSpotList;
	public CopyOnWriteArrayList<NetworkPlayer> otherPlayers;
	public CopyOnWriteArrayList<Obstacle> obstacleServerList;
	
	private TileGrid map;
	private GameEntity currentEntity;
	private Player player;
	
	private float brightness;
	private transient Image filter, path;
	private long time1, time2;
	
	public Handler(){
		this.currentEntity = null;
		this.player = null;
		this.brightness = 0.8f;
		
		this.obstacleList = new CopyOnWriteArrayList<>();
		this.enemyList = new CopyOnWriteArrayList<>();
		this.lightSpotList = new CopyOnWriteArrayList<>();
		this.otherPlayers = new CopyOnWriteArrayList<NetworkPlayer>();
		this.obstacleServerList = new CopyOnWriteArrayList<>();
		
		this.filter = quickLoaderImage("background/Filter");
		this.path = quickLoaderImage("tiles/path");
		this.time1 = System.currentTimeMillis();
		this.time2 = time1;
	}
	
	public void update(){
		// update current entity 
		if(currentEntity != null){
			currentEntity.update();
		}

		// update SpotLights
		for(LightSpot spot : lightSpotList){
			spot.update();
		}
		
		// update enemy
		for(Enemy_Basic e : enemyList){
			e.update();
			if(e.getHp() <= 0){
				enemyList.remove(e);
				shadowObstacleList.remove(e);
			}
		}
		
		// kick inactive players
		for(NetworkPlayer net : otherPlayers) {
			long timestamp = System.currentTimeMillis();
			if(timestamp - net.getTimeout() > 3000) {
				otherPlayers.remove(net);
			}	
			net.update();
		}
		
		//objectInfo();
	}
	
	public void draw(){
		if(filter == null)
			this.filter = quickLoaderImage("background/Filter");
		
		MOVEMENT_X = (int)MOVEMENT_X;
		MOVEMENT_Y = (int)MOVEMENT_Y;
		
		// draw tile map
		map.draw();
		
		
		// draw server obstacles
		for(Obstacle o : obstacleServerList) {
			o.draw();
		}
		
		for(NetworkPlayer netPlayer : otherPlayers) {
			netPlayer.draw();
		}
		
		// draw player
		if(StateManager.gameState != StateManager.GameState.DEATHSCREEN && player != null && currentEntity.equals(player))
			player.draw();
		
		// draw enemy
		for(Enemy_Basic e : enemyList){
			e.draw();
		}
		// draw enemy path to player
		//drawPath();
		
		// draw filter to darken the map
		GL11.glColor4f(0, 0, 0, brightness);
		drawQuadImageStatic(filter, 0, 0, 2048, 2048);
		GL11.glColor4f(1, 1, 1, 1);
	}
	
	public void wipe(){
		player = null;
		currentEntity = null;
		map = null;
		obstacleList.clear();
		enemyList.clear();
		lightSpotList.clear();
	}
	
	@SuppressWarnings("unused")
	private void objectInfo(){
		time1 = System.currentTimeMillis();
		if(time1 - time2 > 2000)
		{
			time2 = time1;
			// Data output
			System.out.println("Anzahl Tiles: " + map.getSetTileCounter() + "\tAnzahl Enemies: " + enemyList.size() + "\tFPS: " + StateManager.framesInLastSecond + "\t\tLight: " + lights.size() + "\tMovementX: " + MOVEMENT_X);
		}
	}
	
	@SuppressWarnings("unused")
	private void drawPath(){
		for(Enemy_Basic e : enemyList){
			for(int i = 0; i < e.getPath().size(); i++){
				drawQuadImage(path, e.getPath().get(i).getX() * TILE_SIZE, e.getPath().get(i).getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
			}
		}
	}

	public GameEntity getCurrentEntity() {
		return currentEntity;
	}

	public void setCurrentEntity(GameEntity currentEntity) {
		this.currentEntity = currentEntity;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public TileGrid getMap() {
		return map;
	}

	public void setMap(TileGrid map) {
		this.map = map;
	}
	
	public void addNetworkPlayer(NetworkPlayer p) {
		otherPlayers.add(p);
	}
	
	public void removeNetworkPlayer(NetworkPlayer p) {
		otherPlayers.remove(p);
	}

	public CopyOnWriteArrayList<NetworkPlayer> getOtherPlayers() {
		return otherPlayers;
	}
}
