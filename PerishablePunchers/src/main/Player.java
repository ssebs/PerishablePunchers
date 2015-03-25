package main;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glViewport;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

public class Player
{
	private final int WIDTH = 1280, HEIGHT = 720;
	private int x, y, w = 256, h = 256, speed, health;

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
	public Player(int ex, int wy, int speed)
	{
		this.x = ex;
		this.y = wy;
		this.speed = speed;
		setHealth(100);
	}

	/**
	 * @param speed
	 */
	public void setSpeed(int speed)
	{
		this.speed = speed;
	}
	
	/**
	 * will render character
	 */
	public void draw(Texture tex)
	{
		glEnable(GL_TEXTURE_2D);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glViewport(0, 0, WIDTH, HEIGHT);
		glMatrixMode(GL_MODELVIEW);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
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
	 * will make character fall, is in 2nd thread
	 */
	public void fall()
	{
		if (y < HEIGHT - h)
		{
			y += speed * 2;
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
					y -= speed * 4;
					try
					{
						Thread.sleep(15);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				Sound.play("res/Sounds/Jump.wav");
			}
		}
	}

	/**
	 * will move character left
	 */
	public void moveLeft()
	{
		if (x > 0)
		{
			x -= speed;
		}
	}

	/**
	 * will move character right
	 */
	public void moveRight()
	{
		if (x < WIDTH - w)
		{
			x += speed;
		}
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
	 * @param w
	 */
	public void setW(int w)
	{
		this.w = w;
	}

	/**
	 * @param h
	 */
	public void setH(int h)
	{
		this.h = h;
	}

	/**
	 * @return
	 */
	public int getWIDTH()
	{
		return WIDTH;
	}

	/**
	 * @return
	 */
	public int getHEIGHT()
	{
		return HEIGHT;
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
	public int getY()
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