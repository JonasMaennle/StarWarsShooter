package data;

import java.util.ArrayList;

import static Gamestate.StateManager.*;
import static helpers.Setup.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Mouse;

import Enity.TileType;
import Gamestate.StateManager;
import object.Goal;
import object.GunganEnemy;
import object.Player;

public class Handler {
	
	public ArrayList<Tile> obstacleList = new ArrayList<>();
	public CopyOnWriteArrayList<GunganEnemy> gunganList = new CopyOnWriteArrayList<>();
	public Player player;
	public Goal levelGoal;
	private long timer1, timer2;
	private TileGrid map;
	private StateManager statemanager;
	
	public Handler(StateManager statemanager)
	{
		this.timer1 = System.currentTimeMillis();
		this.timer2 = timer1;
		this.statemanager = statemanager;
	}
	
	public void update()
	{
		// update Player
		player.update();
		if(player.isOutOfMap())
		{
			statemanager.resetCurrentLevel();
		}
		
		// Get Mouse Coords for weapon angle
		player.getWeapon().calcAngle(Mouse.getX() - MOVEMENT_X, Mouse.getY() - MOVEMENT_Y + 64);
		// Update player direction by mouse movement
		player.updateDirection(Mouse.getX() - MOVEMENT_X, Mouse.getY() - MOVEMENT_Y);

		// update Level Goal
		if(levelGoal != null)
			levelGoal.update();
		
		// update gunganEnemy
		for(GunganEnemy g : gunganList)
		{
			g.update();
		}

		// check player collision with level goal
		if(levelGoal != null && checkCollision(player.getX(), player.getY(), player.getWidth(), player.getHeight(), levelGoal.getX(), levelGoal.getY(), levelGoal.getWidth(), levelGoal.getHeight()))
		{
			//statemanager.loadLevel();
			StateManager.gameState = GameState.LOADING;
		}
		
		// update map
		for(Tile t : obstacleList)
		{
			if(t.getType() == TileType.Grass_Round_Half)
			{
				t.update();
			}
		}
		
		objectInfo();
	}
	
	public void draw()
	{
		// draw tile map
		map.draw();
		
		// draw player
		if(gameState != GameState.DEAD)
			player.draw();
		
		// draw gunganEnemy
		for(GunganEnemy g : gunganList)
		{
			g.draw();
		}
		
		// draw Level Goal
		if(levelGoal != null)
			levelGoal.draw();
	}
	
	private void objectInfo()
	{
		timer1 = System.currentTimeMillis();
		if(timer1 - timer2 > 2000)
		{
			timer2 = timer1;
			// Data output
			System.out.println("Anzahl Tiles: " + obstacleList.size() + "\tAnzahl Enemies: " + gunganList.size() + "\tFPS: " + StateManager.framesInLastSecond);
		}
	}
	
	public void wipe()
	{
		player = null;
		gunganList.clear();
		obstacleList.clear();
		levelGoal = null;
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
}
