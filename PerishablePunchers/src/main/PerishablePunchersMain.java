package main;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
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

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * @author ssebs/Charlse
 * @version 1.0
 */
public class PerishablePunchersMain
{
	private final int WIDTH = 1280, HEIGHT = 720;
	private Player p1, p2;
	private int gameState = 1;
	private long oldTime = 0, newTime = 0, dTime;
	private String gfxType;
	private boolean beggining, stopper, close, isP1Jumping, isP2Jumping, paused, oneDied, oneToFinish, oneToFinish2, playDieSound, playSoundOnce, playSound2Once, playSound3Once, p1CanHit = true, p2CanHit = true;
	private Texture backgroundHD, gfx, gfx8Bit, gfxHD, background, restart, menu, menuPlay, menuQuit, itDied, FinishIt, player1, player2, player1Walk, player2Walk, player1Flipped, player2Flipped, player1Kunch, player2Kunch, player1HealthFull,
			player1Health85, player1Health70, player1Health55, player1Health40, player1Health25, player1Health10, player1HealthFin, player1Health0, player2HealthFull, player2Health85, player2Health70, player2Health55, player2Health40,
			player2Health25, player2Health10, player2HealthFin, player2Health0, player1Kombo1, player2Kombo1, player1HD, player1WalkHD, player1FlippedHD, player1KunchHD, player2HD, player2WalkHD, player2FlippedHD, player2KunchHD;

	// TODO MAKE TEABAGGING FINISHING MOVE, JUMP AND HIT SHOULD TRIGGER IT

	public void renderFinishIt()

	{
		if (oneToFinish)
		{
			renderTex(FinishIt, WIDTH / 2 - 240, 64);

		} else if (oneToFinish2)
		{
			renderTex(FinishIt, WIDTH / 2 - 240, 64);
		}
	}

	public void renderItDied()
	{
		if (oneDied)
		{
			renderTex(itDied, WIDTH / 2 - 260, 64);
		}
	}

	public void renderHealthLeft()
	{
		int health = p1.getHealth();

		if (health > 85)
		{
			renderTex(player1HealthFull, 0, 0);
		} else if (health > 70)
		{
			renderTex(player1Health85, 0, 0);
		} else if (health > 55)
		{
			renderTex(player1Health70, 0, 0);
		} else if (health > 40)
		{
			renderTex(player1Health55, 0, 0);
		} else if (health > 25)
		{
			renderTex(player1Health40, 0, 0);
		} else if (health > 10)
		{
			renderTex(player1Health25, 0, 0);
		} else if (health > 5)
		{
			renderTex(player1Health10, 0, 0);
		} else if (health < 6 && health > 2)
		{
			renderTex(player1HealthFin, 0, 0);
		} else
		{
			renderTex(player1Health0, 0, 0);
		}
	}

	public void renderHealthRight()
	{
		int health = p2.getHealth();
		// System.out.println("P2 Health: " + health);
		if (health > 85)
		{
			renderTex(player2HealthFull, 640, 0);
		} else if (health > 70)
		{
			renderTex(player2Health85, 640, 0);
		} else if (health > 55)
		{
			renderTex(player2Health70, 640, 0);
		} else if (health > 40)
		{
			renderTex(player2Health55, 640, 0);
		} else if (health > 25)
		{
			renderTex(player2Health40, 640, 0);
		} else if (health > 10)
		{
			renderTex(player2Health25, 640, 0);
		} else if (health > 5)
		{
			renderTex(player2Health10, 640, 0);
		} else if (health < 6 && health > 2)
		{
			renderTex(player2HealthFin, 640, 0);
		} else if (health < 3)
		{
			renderTex(player2Health0, 640, 0);
		}

	}

	public void renderTex(Texture tex, int x, int y)
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

	public void render()
	{
		// remember, rendered things will go ontop of another
		if (gfxType.equals("8Bit"))
		{
			renderTex(background, 0, 0);
			renderHealthLeft();
			renderHealthRight();
			renderItDied();
			renderFinishIt();
		} else if (gfxType.equals("HD"))
		{
			renderTex(backgroundHD, 0, 0);
			renderHealthLeft();
			renderHealthRight();
			renderItDied();
			renderFinishIt();
		}

	}

	public void pollInput()
	{

		if (Keyboard.isKeyDown(Keyboard.KEY_D) && p1CanHit)
		{
			p1.draw(player1);
			p1.moveRight();

		} else if (Keyboard.isKeyDown(Keyboard.KEY_A) && p1CanHit)
		{
			p1.draw(player1Flipped);
			p1.moveLeft();

		} else if (Keyboard.isKeyDown(Keyboard.KEY_S) && p1CanHit)
		{
			p1.draw(player1Kunch);

		} else
		{
			p1.draw(player1Walk);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && p2CanHit)
		{
			p2.draw(player2);
			p2.moveRight();
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && p2CanHit)
		{
			p2.draw(player2Flipped);
			p2.moveLeft();
		} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && p2CanHit)
		{
			p2.draw(player2Kunch);

		} else
		{
			p2.draw(player2Walk);

		}

		while (Keyboard.next())
		{

			if (colliding())
			{
				combo1WASD();
				combo1Arrow();
			}

			if (Keyboard.getEventKey() == Keyboard.KEY_G)
			{
				if (Keyboard.getEventKeyState())
				{
				} else
				{
					if (gfxType.equals("HD"))
					{
						gfxType = "8Bit";
					} else if (gfxType.equals("8Bit"))
					{
						gfxType = "HD";
					}
				}
			}

			if (Keyboard.getEventKey() == Keyboard.KEY_W && p1CanHit)
			{
				p1.draw(player1);
				if (Keyboard.getEventKeyState())
				{
					isP1Jumping = true;
				} else
				{
					isP1Jumping = false;
				}
			}

			if (Keyboard.getEventKey() == Keyboard.KEY_UP && p2CanHit)
			{
				p2.draw(player2);
				if (Keyboard.getEventKeyState())
				{
					isP2Jumping = true;
				} else
				{
					isP2Jumping = false;
				}
			}

			if (Keyboard.getEventKey() == Keyboard.KEY_DOWN && p2CanHit)
			{

				if (Keyboard.getEventKeyState())
				{
				} else
				{
					if (colliding())
					{

						p1.setHealth(p1.getHealth() - 5);
						Sound.play("res/Sounds/Punch.wav");
					} else
					{
						Sound.play("res/Sounds/Whoosh.wav");
					}

				}
			}

			if (Keyboard.getEventKey() == Keyboard.KEY_S && p1CanHit)
			{

				if (Keyboard.getEventKeyState())
				{
				} else
				{
					if (colliding())
					{
						p2.setHealth(p2.getHealth() - 5);
						Sound.play("res/Sounds/Punch.wav");
					} else
					{
						Sound.play("res/Sounds/Whoosh.wav");
					}
				}
			}

			// BELOW WILL CHECK FOR COMBOS

			// keypresses

		}

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			gameState = 1;
		}

	}

	public void pollInputHD()
	{

		if (Keyboard.isKeyDown(Keyboard.KEY_D) && p1CanHit)
		{
			p1.draw(player1HD);
			p1.moveRight();

		} else if (Keyboard.isKeyDown(Keyboard.KEY_A) && p1CanHit)
		{
			p1.draw(player1FlippedHD);
			p1.moveLeft();

		} else if (Keyboard.isKeyDown(Keyboard.KEY_S) && p1CanHit)
		{
			p1.draw(player1KunchHD);

		} else
		{
			p1.draw(player1WalkHD);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && p2CanHit)
		{
			p2.draw(player2HD);
			p2.moveRight();
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && p2CanHit)
		{
			p2.draw(player2FlippedHD);
			p2.moveLeft();
		} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && p2CanHit)
		{
			p2.draw(player2KunchHD);

		} else
		{
			p2.draw(player2WalkHD);

		}

		while (Keyboard.next())
		{
			finishingMoveWASD();
			if (colliding())
			{
				combo1WASD();
				combo1Arrow();
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_G)
			{
				if (Keyboard.getEventKeyState())
				{
				} else
				{
					if (gfxType.equals("HD"))
					{
						gfxType = "8Bit";
					} else if (gfxType.equals("8Bit"))
					{
						gfxType = "HD";
					}
				}
			}

			if (Keyboard.getEventKey() == Keyboard.KEY_W && p1CanHit)
			{
				p1.draw(player1HD);
				if (Keyboard.getEventKeyState())
				{
					isP1Jumping = true;
				} else
				{
					isP1Jumping = false;
				}
			}

			if (Keyboard.getEventKey() == Keyboard.KEY_UP && p2CanHit)
			{
				p2.draw(player2HD);
				if (Keyboard.getEventKeyState())
				{
					isP2Jumping = true;
				} else
				{
					isP2Jumping = false;
				}
			}

			if (Keyboard.getEventKey() == Keyboard.KEY_DOWN && p2CanHit)
			{

				if (Keyboard.getEventKeyState())
				{
				} else
				{
					if (colliding())
					{

						p1.setHealth(p1.getHealth() - 5);
						Sound.play("res/Sounds/Punch.wav");
					} else
					{
						Sound.play("res/Sounds/Whoosh.wav");
					}

				}
			}

			if (Keyboard.getEventKey() == Keyboard.KEY_S && p1CanHit)
			{

				if (Keyboard.getEventKeyState())
				{
				} else
				{
					if (colliding())
					{
						p2.setHealth(p2.getHealth() - 5);
						Sound.play("res/Sounds/Punch.wav");
					} else
					{
						Sound.play("res/Sounds/Whoosh.wav");
					}
				}
			}

		}

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			gameState = 1;
		}

	}

	public void finishingMoveWASD()
	{
		if (Keyboard.getEventKey() == Keyboard.KEY_S && p1CanHit)
		{
			if (Keyboard.getEventKeyState())
			{
				if (p1.getY() - 0 <= HEIGHT - 400)// arbitrary
				{
					if (colliding() && playSound3Once)
					{
						System.out.println("FINISHING MOVE");
						Sound.play("res/Sounds/TeaBag.wav");
						playSound3Once = false;
					}
				}

			} else
			{
			}
		}

	}

	public void finishingMoveArrow()
	{
		if (Keyboard.getEventKey() == Keyboard.KEY_DOWN && p2CanHit)
		{
			if (Keyboard.getEventKeyState())
			{
				if (p2.getY() - 0 <= HEIGHT - 400)// arbitrary
				{
					if (colliding() && playSound3Once) // need to make &&
														// oneToFinish work
					{
						System.out.println("FINISHING MOVE");
						Sound.play("res/Sounds/TeaBag.wav");
						playSound3Once = false;
					}
				}

			} else
			{
			}
		}

	}

	public void combo1WASD()
	{
		if (Keyboard.getEventKey() == Keyboard.KEY_W && p1CanHit)
		{
			if (Keyboard.getEventKeyState())
			{
				oldTime = System.currentTimeMillis();
			} else
			{
			}
		}

		if (Keyboard.getEventKey() == Keyboard.KEY_A && p1CanHit)
		{
			if (Keyboard.getEventKeyState())
			{
				newTime = System.currentTimeMillis();
			} else
			{
			}

		}

		dTime = (newTime - oldTime);

		if (dTime < 200 && dTime > 0)
		{
			if (Keyboard.getEventKey() == Keyboard.KEY_S && p1CanHit)
			{
				if (Keyboard.getEventKeyState())
				{
					p2.setHealth(p2.getHealth() - 5);
					p1.draw(player1Kombo1);
					Sound.play("res/Sounds/Kombo.wav");
					dTime = 0;
				} else
				{
				}
			}

		}
	}

	public void combo1Arrow()
	{
		if (Keyboard.getEventKey() == Keyboard.KEY_UP && p2CanHit)
		{
			if (Keyboard.getEventKeyState())
			{
				oldTime = System.currentTimeMillis();
			} else
			{
			}
		}

		if (Keyboard.getEventKey() == Keyboard.KEY_LEFT && p2CanHit)
		{
			if (Keyboard.getEventKeyState())
			{
				newTime = System.currentTimeMillis();
			} else
			{
			}

		}

		dTime = (newTime - oldTime);

		if (dTime < 200 && dTime > 0)
		{
			if (Keyboard.getEventKey() == Keyboard.KEY_DOWN && p2CanHit)
			{
				if (Keyboard.getEventKeyState())
				{
					p1.setHealth(p1.getHealth() - 5);
					p2.draw(player2Kombo1);
					Sound.play("res/Sounds/Kombo.wav");
					dTime = 0;
				} else
				{
				}
			}

		}
	}

	public boolean colliding()
	{
		boolean ret = false;
		int x1 = p1.getX();
		int x2 = p2.getX();

		if (x1 + 210 > x2 && x1 < x2 + 210)
		{
			ret = true;
		} else
		{
			ret = false;
		}

		return ret;
	}

	public void gamePlay()
	{
		if (p1.getHealth() < 6)
		{
			p1CanHit = false;
		} else
		{
			p1CanHit = true;
		}
		if (p2.getHealth() < 6)
		{
			p2CanHit = false;
		} else
		{
			p2CanHit = true;
		}

		// graphics settings
		if (gfxType.equals("8Bit"))
		{
			pollInput();
		} else if (gfxType.equals("HD"))
		{
			pollInputHD();
		}

		// P1 FINISH
		if (p1.getHealth() < 6 && p1.getHealth() > 0)
		{
			oneToFinish = true;
			renderFinishIt();
			finishingMoveArrow();
			if (playSoundOnce)
			{
				Sound.play("res/Sounds/FinishIt.wav");
				playSoundOnce = false;
			}
			// P1 DIED
		} else if (p1.getHealth() < 1)
		{
			oneToFinish = false;
			oneDied = true;
			renderItDied();

			renderTex(restart, WIDTH / 2 - 256, 256);
			playDieSound = true;
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
				reset();
		} else
		{
			oneToFinish = false;
		}
		// P2 FINISH
		if (p2.getHealth() < 6 && p2.getHealth() > 0)
		{
			oneToFinish2 = true;
			renderFinishIt();
			finishingMoveWASD();
			if (playSoundOnce)
			{
				Sound.play("res/Sounds/FinishIt.wav");
				playSoundOnce = false;
			}
			// P2 DIED
		} else if (p2.getHealth() < 1)
		{
			oneToFinish2 = false;
			oneDied = true;
			renderItDied();

			renderTex(restart, WIDTH / 2 - 256, 256);
			playDieSound = true;
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
				reset();

		} else
		{
			oneToFinish2 = false;
		}
	}

	public void menu()
	{
		paused = true;
		renderTex(menu, 0, 0);
		int x = Mouse.getX(); // will return the X coordinate on the Display.
		int y = Mouse.getY(); // will return the Y coordinate on the Display.
		// System.out.println("X: " + x + "\nY: " + y);
		if (Mouse.isButtonDown(0))
		{
			if (x >= 400 && x <= 875)
			{
				if (y <= 545 && y >= 390)
				{
					renderTex(menuPlay, 0, 0);
					Display.update();
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}

					if (beggining)
					{
						gameState = 2;
						beggining = false;
					} else
					{
						gameState = 0;
					}

					// System.out.println("PLAY BUTTON CLICKED");
				}
			}

			if (x >= 480 && x <= 800)
			{
				if (y >= 225 && y <= 330)
				{
					// System.out.println("QUIT BUTTON CLICKED");
					renderTex(menuQuit, 0, 0);
					Display.update();
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					stopper = true;
				}
			}
		}
	}

	public void play()
	{
		paused = false;
		render();
		gamePlay();
	}

	public void gfx()
	{
		paused = true;
		renderTex(gfx, 0, 0);
		int x = Mouse.getX(); // will return the X coordinate on the Display.
		int y = Mouse.getY(); // will return the Y coordinate on the Display.
		// System.out.println("X: " + x + "\nY: " + y);
		if (Mouse.isButtonDown(0))
		{
			if (x >= 390 && x <= 890)
			{
				if (y <= 540 && y >= 380)
				{
					renderTex(gfx8Bit, 0, 0);
					gfxType = "8Bit";
					Display.update();
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					gameState = 0;
					// System.out.println("PLAY BUTTON CLICKED");
				}
			}
			if (x >= 385 && x <= 890)
			{
				if (y >= 108 && y <= 270)
				{
					// System.out.println("QUIT BUTTON CLICKED");
					renderTex(gfxHD, 0, 0);
					gfxType = "HD";
					Display.update();
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					gameState = 0;
				}
			}
		}
	}

	public void gameStates()
	{
		switch (gameState)
		{
		case 0:
			play();
			return;
		case 1:
			menu();
			return;
		case 2:
			gfx();
		}
	}

	public void reset()
	{
		p1.setHealth(100);
		p1.setX(120);
		p1.setY(100);

		p2.setHealth(100);
		p2.setX(WIDTH - 120 - 256);
		p2.setY(100);

		oneDied = false;
		playSoundOnce = true;
		playSound2Once = true;
		playSound3Once = true;
		playDieSound = false;
	}

	public void start()
	{
		init();
		Thread th1 = new Thread(run1);// multithreading
		th1.start();
		while (stopper == false)
		{
			if (Display.isCloseRequested())
			{
				stopper = true;
			}
			// init openGl here
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
			glMatrixMode(GL_MODELVIEW);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			Display.setTitle("Perishable Punchers");
			// game rules

			gameStates();
			Display.update();// updates screen
		}
		Display.destroy();
		close = true;
	}

	public void init()
	{
		try
		{
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("Loading...");
			Display.setInitialBackground(0, 0, 0);
			Display.setVSyncEnabled(true);

			Display.create();
		} catch (LWJGLException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		initTexs();

		// create objects here
		p1 = new Player(120, 100, 10);
		p2 = new Player(WIDTH - 120 - 256, 100, 10);
		// init vars
		stopper = false;
		paused = true;
		oneDied = false;
		oneToFinish = false;
		playSoundOnce = true;
		playSound2Once = true;
		playSound3Once = true;
		beggining = true;
		playDieSound = false;
		// end init vars

	}

	public void initTexs()
	{
		try
		{
			// general things
			gfx = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Gfx.png"));

			gfx8Bit = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Gfx8Bit.png"));

			gfxHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/GfxHD.png"));

			restart = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Restart.png"));

			itDied = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/ItDied.png"));

			FinishIt = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/FinishIt.png"));

			background = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/BG.png"));

			backgroundHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/BGHD.png"));

			menu = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Menu.png"));

			menuPlay = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/MenuPlay.png"));

			menuQuit = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/MenuQuit.png"));

			// komboz
			player1Kombo1 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player1/Player1Kombo1.png"));

			player2Kombo1 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player2/Player2Kombo1.png"));

			// player 1
			player1HealthFull = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player1/HealthLeftFull.png"));

			player1Health85 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player1/HealthLeft85.png"));

			player1Health70 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player1/HealthLeft70.png"));

			player1Health55 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player1/HealthLeft55.png"));

			player1Health40 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player1/HealthLeft40.png"));

			player1Health25 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player1/HealthLeft25.png"));

			player1Health10 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player1/HealthLeft10.png"));

			player1HealthFin = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player1/HealthLeftFin.png"));

			player1Health0 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player1/HealthLeft0.png"));

			player1 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player1/Player1.png"));

			player1Walk = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player1/Player1Walk.png"));

			player1Flipped = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player1/Player1Flipped.png"));

			player1Kunch = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player1/Player1Kunch.png"));

			// player 2
			player2HealthFull = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player2/HealthRightFull.png"));

			player2Health85 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player2/HealthRight85.png"));

			player2Health70 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player2/HealthRight70.png"));

			player2Health55 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player2/HealthRight55.png"));

			player2Health40 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player2/HealthRight40.png"));

			player2Health25 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player2/HealthRight25.png"));

			player2Health10 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player2/HealthRight10.png"));

			player2HealthFin = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player2/HealthRightFin.png"));

			player2Health0 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player2/HealthRight0.png"));

			player2 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player2/Player2.png"));

			player2Walk = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player2/Player2Walk.png"));

			player2Flipped = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player2/Player2Flipped.png"));

			player2Kunch = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player2/Player2Kunch.png"));

			// hd textures for player 1
			player1HD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/HD/Player1/Player1HD.png"));

			player1WalkHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/HD/Player1/Player1WalkHD.png"));

			player1FlippedHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/HD/Player1/Player1FlippedHD.png"));

			player1KunchHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/HD/Player1/Player1KunchHD.png"));

			// hd textures for player 2
			player2HD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/HD/Player2/Player2HD.png"));

			player2WalkHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/HD/Player2/Player2WalkHD.png"));

			player2FlippedHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/HD/Player2/Player2FlippedHD.png"));

			player2KunchHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/HD/Player2/Player2KunchHD.png"));

		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public static void main(String[] args)
	{
		PerishablePunchersMain game = new PerishablePunchersMain();
		game.start();
	}

	Runnable run1 = new Runnable()
	{
		public void run()
		{
			close = false;
			while (close == false)
			{
				if (!paused)
				{
					p1.fall();
					p2.fall();
				}
				if (isP1Jumping)
				{
					p1.jump();
				}
				if (isP2Jumping)
				{
					p2.jump();
				}
				try
				{
					Thread.sleep(20);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				if (playSound2Once && playDieSound)
				{
					try
					{
						Thread.sleep(1500);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					Sound.play("res/Sounds/ItDied.wav");
					playSound2Once = false;
					playDieSound = false;
				}

			}
		}
	};

}