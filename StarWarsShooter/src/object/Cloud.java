package object;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import Enity.Entity;

import static helpers.Graphics.*;

import java.awt.Rectangle;

public class Cloud implements Entity{
	
	private float x, y;
	private int width, height;
	private Image image;

	public Cloud(float x, float y)
	{
		this.x = x;
		this.y = y;
		this.width = 800;
		this.height = 800;
		this.image = quickLoaderImage("background/Cloud");
	}
	
	public void update() 
	{
		if(x < Display.getX() - image.getWidth())
		{
			x = Display.getX() + Display.getWidth();
		}else{
			x -= 0.3;
		}
	}

	public void draw() 
	{
		drawQuadImageStatic(image, x, y, width, height);
	}
	
	@Override
	public float getX() 
	{
		return x;
	}

	@Override
	public float getY() 
	{
		return y;
	}

	@Override
	public int getWidth() 
	{
		return width;
	}

	@Override
	public int getHeight() 
	{
		return height;
	}

	@Override
	public void setWidth(int width) 
	{
		this.width = width;
	}

	@Override
	public void setHeight(int height) 
	{
		this.height = height;
	}

	@Override
	public void setX(float x) 
	{
		this.x = x;
	}

	@Override
	public void setY(float y) 
	{
		this.y = y;
	}

	@Override
	public Vector2f[] getVertices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getVelX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getVelY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void damage(float amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isOutOfMap() {
		return false;
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}
}
