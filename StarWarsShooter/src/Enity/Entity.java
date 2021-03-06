package Enity;

import java.awt.Rectangle;

import org.lwjgl.util.vector.Vector2f;

public interface Entity {
	
	public void update();
	public void draw();
	public float getX();
	public float getY();
	public int getWidth();
	public int getHeight();
	public void setWidth(int width);
	public void setHeight(int height);
	public void setX(float x);
	public void setY(float y);
	public float getVelX();
	public float getVelY();
	public void damage(float amount);
	public boolean isOutOfMap();
	public Rectangle getBounds();
	public Vector2f[] getVertices();
}
