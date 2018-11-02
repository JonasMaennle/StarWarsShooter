package object.obstacle;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import static helpers.Artist.*;

import data.Entity;

public class Obstacle implements Entity{

	private float x, y;
	private int width, height;
	private Image img;

	public Obstacle(Image img, float x, float y, int width, int height)
	{
		this.img = img;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		///// !!! NOT USED !!!!
	}
	
	@Override
	public void update() 
	{
		draw();
	}

	@Override
	public void draw() 
	{
		drawQuadImage(img, x, y, width, height);
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
	public Vector2f[] getVertices() 
	{
		return new Vector2f[] {
				new Vector2f(x, y), // left top
				new Vector2f(x, y + width), // left bottom
				new Vector2f(x + width, y + height), // right bottom
				new Vector2f(x + width, y) // right top
		};
	}
}
