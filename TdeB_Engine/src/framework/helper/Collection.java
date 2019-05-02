package framework.helper;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import framework.entity.GameEntity;
import framework.shader.Light;

public class Collection {
	
	// START SETTINGS
	public static int WIDTH = 960;//(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static int HEIGHT = 640;//(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public static boolean SET_FULL_SCREEN = false;
	public static final int TILE_SIZE = 32;
	
	public static ArrayList<Light> lights = new ArrayList<Light>();
	public static CopyOnWriteArrayList<GameEntity> shadowObstacleList = new CopyOnWriteArrayList<>();
	public static float MOVEMENT_X, MOVEMENT_Y;

	
	public static int TILES_WIDTH;
	public static int TILES_HEIGHT;
	
	public static float getLeftBorder(){
		return MOVEMENT_X * -1;
	}
	
	public static float getRightBorder(){
		return (MOVEMENT_X * -1) + WIDTH;
	}
	
	public static float getTopBorder(){
		return (MOVEMENT_Y * -1);
	}
	
	public static float getBottomBorder(){
		return (MOVEMENT_Y * -1) + HEIGHT;
	}
	
	public static Vector2f[] getImageVertices(int x, int y, Image image){
		
		int w = image.getWidth();
		int h = image.getHeight();
		//
		int[][] alpha = new int[w][h];
		int alphaCounter = 0;
		
		for(int xx = 0; xx < w; xx++){
			for(int yy = 0; yy < h; yy++){
				Color c = image.getColor(xx, yy);
				if(c.getAlpha() != 0){
					alpha[xx][yy] = 1;
					alphaCounter++;
				}
			}
		}
		Vector2f[] vertices = new Vector2f[alphaCounter];
		int count = 0;
		// fill vertices array
		for(int xx = 0; xx < w; xx++){
			for(int yy = 0; yy < h; yy++){
				if(alpha[xx][yy] == 1 && count < alphaCounter){
					vertices[count] = new Vector2f(x + MOVEMENT_X + xx, y + MOVEMENT_Y + yy);
					count++;
				}
			}
		}
		// System.out.println(vertices.length);
		return vertices;
	}
}
