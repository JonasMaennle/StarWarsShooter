package data;

import static Gamestate.StateManager.*;
import static helpers.Graphics.drawQuadImageStatic;
import static helpers.Graphics.quickLoaderImage;
import static helpers.Setup.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;

import Enity.Entity;
import Enity.TileType;
import Gamestate.StateManager;
import UI.UI;
import object.AT_ST_Walker;
import object.Enemy;
import object.Goal;
import object.Laser;
import object.MapWeapon;
import object.Player;
import object.Speeder;

public class Handler {
	
	private StateManager statemanager;
	public CopyOnWriteArrayList<Tile> obstacleList;
	private CopyOnWriteArrayList<ParticleEvent> eventList;
	public CopyOnWriteArrayList<Enemy> enemyList;
	public CopyOnWriteArrayList<MapWeapon> weaponList;
	
	public Player player;
	public AT_ST_Walker at_st_walker;
	public Speeder speeder;
	public Goal levelGoal;
	
	private long timer1, timer2;
	private TileGrid map;
	private Entity currentEntity;
	private UI gameUI;
	private Image filter;
	
	public Handler(StateManager statemanager)
	{
		this.statemanager = statemanager;
		this.obstacleList = new CopyOnWriteArrayList<>();
		this.eventList = new CopyOnWriteArrayList<>();
		this.enemyList = new CopyOnWriteArrayList<>();
		this.weaponList = new CopyOnWriteArrayList<>();
		
		this.player = null;
		this.at_st_walker = null;
		this.speeder = null;
		this.levelGoal = null;
		this.filter = quickLoaderImage("background/filter");
		
		this.timer1 = System.currentTimeMillis();
		this.timer2 = timer1;
		this.currentEntity = null;
		this.gameUI = new UI();
		this.gameUI.addButton("Return", "intro/Return", (int)(getLeftBorder() + MOVEMENT_X) + 5, (int)(getTopBorder() + MOVEMENT_Y) + 5, 128, 64);
	}
	
	public void update()
	{
		// update current entity (player || at_st || speeder)
		if(currentEntity != null)
		{
			currentEntity.update();
			if(currentEntity.isOutOfMap())
				statemanager.resetCurrentLevel();
		}
		
		// update enemies
		for(Enemy e : enemyList)
		{
			if(e.getX() >= currentEntity.getX() && e.getX() - currentEntity.getX() < WIDTH)
			{
				e.update();
			}
			if(e.getX() <= currentEntity.getX() && e.getX() - currentEntity.getX() > - WIDTH)
			{
				e.update();
			}
		}

		// check current entity for collision with goal
		if(levelGoal != null && checkCollision(currentEntity.getX(), currentEntity.getY(), currentEntity.getWidth(), currentEntity.getHeight(), levelGoal.getX(), levelGoal.getY(), levelGoal.getWidth(), levelGoal.getHeight()))
		{
			//statemanager.loadLevel();
			if(lastState == GameState.EDITOR)StateManager.gameState = GameState.EDITOR;
			if(lastState != GameState.EDITOR)StateManager.gameState = GameState.LOADING;
		}
		
		// update map if player is near
		for(Tile t : obstacleList)
		{
			if(t.getType() == TileType.Lava_Light)
			{
				if(t.getX() >= currentEntity.getX() && t.getX() - currentEntity.getX() < WIDTH)
					t.update();
				
				if(t.getX() <= currentEntity.getX() && t.getX() - currentEntity.getX() > - WIDTH)
					t.update();
			}
			if(t.getType() == TileType.Grass_Round_Half || t.getType() == TileType.Rock_Half)
				t.update();
		}

		// update particle if tile is dead
		for(ParticleEvent event : eventList)
		{
			if(event.isListEmpty())
			{
				eventList.remove(event);
			}else{
				event.update();
			}
		}
		
		// update "dead" projectiles
		for(Entity e : projectileList)
		{
			e.update();
			
			if(e.getVelX() == 0)
				projectileList.remove(e);
			
			if(e.isOutOfMap())
				projectileList.remove(e);
			
			if(e.getBounds().intersects(currentEntity.getBounds()))
			{
				currentEntity.damage(10);
				projectileList.remove(e);
				if(e.getClass().getSimpleName().equals("Laser"))
				{
					Laser l = (Laser)e;
					l.removeLight();
				}
			}
		}
		
		// Check if "Return" Button was clicked
		if(gameUI.getButton("Return") != null)
		{
			while(Mouse.next())
			{
				if(gameUI.isButtonClicked("Return"))
				{
					if(lastState == GameState.EDITOR)
					{
						StateManager.gameState = GameState.EDITOR;
						statemanager.getEditor().transmitDataFromHandler();
					}else{
						statemanager.getMainMenu().enterMainMenu();
						StateManager.gameState = GameState.MAINMENU;
					}
					lastState = GameState.GAME;
				}
			}
		}
		
		// Game information output in console
		objectInfo();
	}
	
	public void draw()
	{
		// draw tile map
		map.draw();
		

		// Draw alpha FILTER
		GL11.glColor4f(0, 0, 0, 0.2f);
		drawQuadImageStatic(filter, 0, 0, 2048, 2048);
		GL11.glColor4f(1, 1, 1, 1);
		
		// draw player
		if(gameState != GameState.DEAD && player != null && currentEntity.equals(player))
			player.draw();
		
		// draw at st
		if(at_st_walker != null)
			at_st_walker.draw();
		
		// draw speeder
		if(speeder != null)
			speeder.draw();
		
		// draw enemies
		for(Enemy e : enemyList)
		{
			e.draw();
		}
		
		// draw "dead" projectiles
		for(Entity e : projectileList)
		{
			e.draw();
		}
		
		// draw weapons
		for(MapWeapon w : weaponList)
		{
			w.draw();
		}
		
		// draw Level Goal
		if(levelGoal != null)
			levelGoal.draw();
		
		//draw particles if tile dies
		for(ParticleEvent event : eventList)
		{
			if(event.isListEmpty())
			{
				eventList.remove(event);
			}else{
				event.draw();
			}
		}
		
		// draw Buttons
		gameUI.draw();
	}
	
	//@SuppressWarnings("unused")
	private void objectInfo()
	{
		timer1 = System.currentTimeMillis();
		if(timer1 - timer2 > 2000)
		{
			timer2 = timer1;
			// Data output
			System.out.println("Anzahl Tiles: " + obstacleList.size() + "\tAnzahl Enemies: " + enemyList.size() + "\tFPS: " + StateManager.framesInLastSecond + "\t\tLight:" + lights.size());
		}
	}
	
	public void wipe()
	{
		player = null;
		at_st_walker = null;
		speeder = null;
		levelGoal = null;
		enemyList.clear();
		obstacleList.clear();
		eventList.clear();
		weaponList.clear();
		
		// clean lights
		lights.clear();
		
		// clear old projectiles
		projectileList.clear();
	}

	public TileGrid getMap() {
		return map;
	}

	public void setMap(TileGrid map) {
		this.map = map;
	}

	public StateManager getStatemanager() {
		return statemanager;
	}

	public void setStatemanager(StateManager statemanager) {
		this.statemanager = statemanager;
	}

	public Entity getCurrentEntity() {
		return currentEntity;
	}

	public void setCurrentEntity(Entity currentEntity) {
		this.currentEntity = currentEntity;
	}
	
	public void addParticleEvent(ParticleEvent event){
		eventList.add(event);
	}

	public UI getGameUI() {
		return gameUI;
	}

	public void setGameUI(UI gameUI) {
		this.gameUI = gameUI;
	}
}
