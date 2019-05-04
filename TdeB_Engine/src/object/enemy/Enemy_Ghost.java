package object.enemy;

import static framework.helper.Collection.MOVEMENT_X;
import static framework.helper.Collection.MOVEMENT_Y;
import static framework.helper.Collection.TILE_SIZE;
import static framework.helper.Collection.lights;
import static framework.helper.Graphics.drawAnimation;
import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.loadSpriteSheet;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;

import framework.core.Handler;
import framework.shader.Light;

public class Enemy_Ghost extends Enemy_Basic{
	
	private int eyeX, eyeY;
	private int hpFactor;
	private Light eyeLightLeft, eyeLightRight;

	public Enemy_Ghost(float x, float y, int width, int height, Handler handler) {
		super(x, y, width, height, handler);
		
		moveLeft = new Animation(loadSpriteSheet("enemy/Enemy_Ghost_left", TILE_SIZE, TILE_SIZE), 200);
		moveRight = new Animation(loadSpriteSheet("enemy/Enemy_Ghost_right", TILE_SIZE, TILE_SIZE), 200);
		
		this.hpFactor = 16;
		this.hp *= hpFactor;	
		
		eyeLightLeft = new Light(new Vector2f(0, 0), 0, 2, 20, 15);
		eyeLightRight = new Light(new Vector2f(0, 0), 0, 2, 20, 15);
		lights.add(eyeLightLeft);
		lights.add(eyeLightRight);
	}
	
	public void update(){
		super.update();
		
		// calc eyelight position
		if(direction.equals("left")){
			if(moveLeft.getFrame() == 0){
				eyeX = (int) (x + MOVEMENT_X + 5);
				eyeY = (int) (y + MOVEMENT_Y + 9);
			}else{
				eyeX = (int) (x + MOVEMENT_X + 5);
				eyeY = (int) (y + MOVEMENT_Y + 13);
			}
			eyeLightLeft.setLocation(new Vector2f(eyeX + 1, eyeY));
			eyeLightRight.setLocation(new Vector2f(eyeX + 1 + 12, eyeY));
		}else{
			if(moveRight.getFrame() == 0){
				eyeX = (int) (x + MOVEMENT_X + 25);
				eyeY = (int) (y + MOVEMENT_Y + 9);
			}else{
				eyeX = (int) (x + MOVEMENT_X + 25);
				eyeY = (int) (y + MOVEMENT_Y + 13);
			}	
			eyeLightLeft.setLocation(new Vector2f(eyeX + 1, eyeY));
			eyeLightRight.setLocation(new Vector2f(eyeX + 1 - 12, eyeY));
		}	
	}
	
	public void draw(){
		if(direction.equals("right")){
			drawAnimation(moveRight, x, y, width, height);
		}else{
			drawAnimation(moveLeft, x, y, width, height);
		}
		// hp bar
		drawQuadImage(hpBar, x, y - 6, hp / hpFactor, 4);
	}
	
	@Override
	public Vector2f[] getVertices() {
		
		if(direction.equals("right")){
			return new Vector2f[] {
					new Vector2f(x + MOVEMENT_X + 5, y + MOVEMENT_Y + 13), // left top
					new Vector2f(x + MOVEMENT_X + 5, y + MOVEMENT_Y + 28 ), // left bottom
					new Vector2f(x + MOVEMENT_X + 28, y + MOVEMENT_Y + 28), // right bottom
					new Vector2f(x + MOVEMENT_X + 28, y + MOVEMENT_Y + 5) // right top
			};
		}else{
			return new Vector2f[] {
					new Vector2f(x + MOVEMENT_X + 5, y + MOVEMENT_Y + 5), // left top
					new Vector2f(x + MOVEMENT_X + 5, y + MOVEMENT_Y + 28), // left bottom
					new Vector2f(x + MOVEMENT_X + 28, y + MOVEMENT_Y + 28), // right bottom
					new Vector2f(x + MOVEMENT_X + 28, y + MOVEMENT_Y + 13) // right top
			};
		}
	}
	
	
	@Override
	public void die(){
		lights.remove(eyeLightLeft);
		lights.remove(eyeLightRight);
	}
}