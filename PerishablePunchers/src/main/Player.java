package main;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

public class Player
{
	private final int WIDTH = 1280, HEIGHT = 720;
	private int x, w = 256, h = 256, health, maxHealth;
	private float y, speed;

	/**
	 * @param ex
	 * @param wy
	 * @param wi
	 * @param hi
	 * @param re
	 * @param ge
	 * @param be
	 * @param speed
	 */
	public Player(int ex, int wy, float speed)
	{
		this.x = ex;
		this.y = wy;
		this.speed = speed;
		maxHealth = 100
				
				
				
				
				;
		setHealth(maxHealth);
	}

	public int getMaxHealth()
	{
		return maxHealth;
	}

	/**
	 * will render character
	 */
	public void draw(Texture tex)
	{
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glLoadIdentity();
		glMatrixMode(GL_MODELVIEW);
		Color.white.bind();
		tex.bind();
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(x, y);// top left coord
		glTexCoord2f(1, 0);
		glVertex2f(x + tex.getTextureWidth(), y);
		glTexCoord2f(1, 1);
		glVertex2f(x + tex.getTextureWidth(), y + tex.getTextureHeight());
		glTexCoord2f(0, 1);
		glVertex2f(x, y + tex.getTextureHeight());
		glEnd();
		glDisable(GL_TEXTURE_2D);
	}

	/**
	 * will make character fall to ground
	 */

	public void fall(long delta)
	{
		if (y < HEIGHT - h)
		{
			// y += 0.63f * delta;
			y += 0.9f * delta;
		} else
		{
			y = HEIGHT - h + 1;
		}
	}

	/**
	 * will make character jump
	 */
	public void jump()
	{
		if (y >= HEIGHT - 280)
		{
			if (y > HEIGHT / 2 - h / 2)
			{
				for (int i = 0; i < 7; i++)
				{
					y -= 2 * speed * 20;
					try
					{
						Thread.sleep(5);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				// Sound.play("Jump.wav");
				AL_Jump.execute();
			}
		}
	}

	/**
	 * will move character left
	 */
	public void moveLeft(long delta)
	{
		if (x > 0)
		{
			x -= delta * speed;
		}
	}

	/**
	 * will move character right
	 */
	public void moveRight(long delta)
	{
		if (x < WIDTH - w)
		{
			x += delta * speed;
		}
	}

	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	/**
	 * @param x
	 */
	public void setX(int x)
	{
		this.x = x;
	}

	/**
	 * @param y
	 */
	public void setY(int y)
	{
		this.y = y;
	}

	/**
	 * @return
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * @return
	 */
	public float getY()
	{
		return y;
	}

	/**
	 * @return
	 */
	public int getW()
	{
		return w;
	}

	/**
	 * @return
	 */
	public int getH()
	{
		return h;
	}

	public int getHealth()
	{
		return health;
	}

	public void setHealth(int health)
	{
		this.health = health;
	}
}