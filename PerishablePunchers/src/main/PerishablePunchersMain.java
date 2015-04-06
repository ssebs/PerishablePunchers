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

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
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
	private Runnable haduken;
	private int hardness, gameState = 1, gameMode, player1Tex, map, player2Tex, clickCount, clickCountMaps, fireBallX = 0, fireBallY = 512, fireBall2X = 0, fireBall2Y = 512, combo1WASDNum = 0, combo1ArrowNum = 0, fireBallType = 0, fireBallType2 = 0;
	private long oldTime = 0, newTime = 0, dTime, dTime2, oldTime2 = 0, newTime2 = 0;
	private String gfxType;
	private boolean vSync, renderDot1, renderDot2, renderDot3, renderDot4, beggining, close, isP1Jumping, isP2Jumping, oneDied, oneToFinish, oneToFinish2, playDieSound, playSoundOnce, playSound2Once, playSound3Once, fireBallCollisionOnce,
			p1CanFireBall, p2CanFireBall, p1CanHit, p2CanHit, renderFireBallP1, renderFireBallP2, notDoneP1, notDoneP2, playHaduken1Once, playHaduken2Once;
	private static long lastFrame;
	private Texture diff, easy, medium, hard, parkBG, parkBGHD, maps, roofBG, roofBGHD, officeBG, officeBGHD, sewerBG, sewerBGHD, fireBall, fireBallHD, fireBallFlipped, fireBallHDFlipped, charPick, gfx, gfx8Bit, gfxHD, restart, menu, menuPlay,
			menuQuit, itDied, FinishIt, player1, player2, player1Walk, player2Walk, player1Flipped, player2Flipped, player1Kunch, player2Kunch, player1HealthFull, player1Health85, player1Health70, player1Health55, player1Health40, player1Health25,
			player1Health10, player1HealthFin, player1Health0, player2HealthFull, player2Health85, player2Health70, player2Health55, player2Health40, player2Health25, player2Health10, player2HealthFin, player2Health0, player1HD, player1WalkHD,
			player1FlippedHD, player1KunchHD, player2HD, player2WalkHD, player2FlippedHD, player2KunchHD, player3, player3Walk, player3Flipped, player3Kunch, player4, player4Walk, player4Flipped, player4Kunch, player3HD, player3WalkHD,
			player3FlippedHD, player3KunchHD, player4HD, player4WalkHD, player4FlippedHD, player4KunchHD, explosion, explosionHD, sp, mp, spmp;

	private static long getTime()
	{
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();

	}

	private static double getDelta()
	{
		long currentTime = getTime();
		double delta = (double) currentTime - (double) lastFrame;
		lastFrame = getTime();
		return delta;
	}

	private void renderFinishIt()
	{
		if (oneToFinish)
		{
			renderTex(FinishIt, WIDTH / 2 - 240, 64);

		} else if (oneToFinish2)
		{
			renderTex(FinishIt, WIDTH / 2 - 240, 64);
		}
	}

	private void renderItDied()
	{
		if (oneDied)
		{
			renderTex(itDied, WIDTH / 2 - 260, 64);
		}
	}

	private void renderHealthLeft()
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

	private void renderHealthRight()
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

	private void renderTex(Texture tex, int x, int y)
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

	private void render()
	{
		glClearColor(0, 0, 0, 1);
		// remember, rendered things will go ontop of another
		if (gfxType.equals("8Bit"))
		{
			if (map == 1)
			{
				renderTex(parkBG, 0, 0);
			} else if (map == 2)
			{
				renderTex(roofBG, 0, 0);
			} else if (map == 3)
			{
				renderTex(officeBG, 0, 0);
			} else if (map == 4)
			{
				renderTex(sewerBG, 0, 0);
			}

			renderHealthLeft();
			renderHealthRight();
			renderItDied();
			renderFinishIt();
		} else if (gfxType.equals("HD"))
		{
			if (map == 1)
			{
				renderTex(parkBGHD, 0, 0);
			} else if (map == 2)
			{
				renderTex(roofBGHD, 0, 0);
			} else if (map == 3)
			{
				renderTex(officeBGHD, 0, 0);
			} else if (map == 4)
			{
				renderTex(sewerBGHD, 0, 0);
			}

			renderHealthLeft();
			renderHealthRight();
			renderItDied();
			renderFinishIt();
		}
		// render a square for health bar
		glBegin(GL_QUADS);
		GL11.glColor3f(0, 0, 0);
		glVertex2f(WIDTH / 2 - 5, 34);
		glVertex2f(WIDTH / 2 + 5, 34);
		glVertex2f(WIDTH / 2 + 5, 98);
		glVertex2f(WIDTH / 2 - 5, 98);
		glEnd();

	}

	private void pollInput(Texture player1, Texture p1Walk, Texture player1Kunch, Texture player1Flipped, Texture player2, Texture p2Walk, Texture player2Kunch, Texture player2Flipped)
	{
		long delta = (long) getDelta();
		p1.fall(delta);
		p2.fall(delta);

		while (Keyboard.next())
		{

			combo1WASD();
			combo1Arrow();

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
			if (Keyboard.getEventKey() == Keyboard.KEY_V)
			{
				if (Keyboard.getEventKeyState())
				{
				} else
				{
					if (vSync)
					{
						Display.setVSyncEnabled(false);
						vSync = !vSync;
					} else
					{
						Display.setVSyncEnabled(true);
						vSync = !vSync;
					}
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_R)
			{
				if (Keyboard.getEventKeyState())
				{
				} else
				{
					Display.destroy();
					close = true;
					new PerishablePunchersMain().init();
					System.exit(0);
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
						Sound.play("Punch.wav");
					} else
					{
						Sound.play("Whoosh.wav");
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
						Sound.play("Punch.wav");
					} else
					{
						Sound.play("Whoosh.wav");
					}
				}
			}

			// BELOW WILL CHECK FOR COMBOS

			// keypresses

		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D) && p1CanHit)
		{
			p1.draw(player1);
			p1.moveRight(delta);

		} else if (Keyboard.isKeyDown(Keyboard.KEY_A) && p1CanHit)
		{
			p1.draw(player1Flipped);
			p1.moveLeft(delta);

		} else if (Keyboard.isKeyDown(Keyboard.KEY_S) && p1CanHit)
		{
			p1.draw(player1Kunch);

		} else
		{
			p1.draw(p1Walk);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && p2CanHit)
		{
			p2.draw(player2);
			p2.moveRight(delta);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && p2CanHit)
		{
			p2.draw(player2Flipped);
			p2.moveLeft(delta);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && p2CanHit)
		{
			p2.draw(player2Kunch);

		} else
		{
			p2.draw(p2Walk);

		}

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			gameState = 1;
		}

	}

	private void pollInputSP(Texture player1, Texture p1Walk, Texture player1Kunch, Texture player1Flipped, Texture player2, Texture p2Walk, Texture player2Kunch, Texture player2Flipped)
	{
		long delta = (long) getDelta();
		p1.fall(delta);
		p2.fall(delta);

		while (Keyboard.next())
		{

			combo1WASD();
			combo1Arrow();

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
			if (Keyboard.getEventKey() == Keyboard.KEY_V)
			{
				if (Keyboard.getEventKeyState())
				{
				} else
				{
					if (vSync)
					{
						Display.setVSyncEnabled(false);
						vSync = !vSync;
					} else
					{
						Display.setVSyncEnabled(true);
						vSync = !vSync;
					}
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_R)
			{
				if (Keyboard.getEventKeyState())
				{
				} else
				{
					Display.destroy();
					close = true;
					new PerishablePunchersMain().init();
					System.exit(0);
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

			if (Keyboard.getEventKey() == Keyboard.KEY_S && p1CanHit)
			{

				if (Keyboard.getEventKeyState())
				{
				} else
				{
					if (colliding())
					{
						p2.setHealth(p2.getHealth() - 5);
						Sound.play("Punch.wav");
					} else
					{
						Sound.play("Whoosh.wav");
					}
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_UP && p1CanHit)
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

			if (Keyboard.getEventKey() == Keyboard.KEY_DOWN && p1CanHit)
			{

				if (Keyboard.getEventKeyState())
				{
				} else
				{
					if (colliding())
					{
						p2.setHealth(p2.getHealth() - 5);
						Sound.play("Punch.wav");
					} else
					{
						Sound.play("Whoosh.wav");
					}
				}
			}
			// BELOW WILL CHECK FOR COMBOS

			// keypresses

		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D) && p1CanHit)
		{
			p1.draw(player1);
			p1.moveRight(delta);

		} else if (Keyboard.isKeyDown(Keyboard.KEY_A) && p1CanHit)
		{
			p1.draw(player1Flipped);
			p1.moveLeft(delta);

		} else if (Keyboard.isKeyDown(Keyboard.KEY_S) && p1CanHit)
		{
			p1.draw(player1Kunch);

		} else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && p1CanHit)
		{
			p1.draw(player1);
			p1.moveRight(delta);

		} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && p1CanHit)
		{
			p1.draw(player1Flipped);
			p1.moveLeft(delta);

		} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && p1CanHit)
		{
			p1.draw(player1Kunch);

		} else
		{
			p1.draw(p1Walk);
		}

		p2AI(player2, p2Walk, player2Kunch, player2Flipped);

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			gameState = 1;
		}

	}

	private void combo1WASD()
	{
		if (Keyboard.getEventKey() == Keyboard.KEY_W && p1CanHit)
		{
			if (Keyboard.getEventKeyState())
			{
				oldTime = System.currentTimeMillis();
			} else
			{
				combo1WASDNum++;
			}
		}

		if (Keyboard.getEventKey() == Keyboard.KEY_A && p1CanHit)
		{
			if (Keyboard.getEventKeyState())
			{
				newTime = System.currentTimeMillis();
			} else
			{
				combo1WASDNum++;
			}

		}

		dTime = (newTime - oldTime);

		if (dTime < 400 && dTime > 0)
		{
			if (Keyboard.getEventKey() == Keyboard.KEY_S && p1CanHit && combo1WASDNum == 2)
			{
				if (Keyboard.getEventKeyState())
				{

				} else
				{
					if (colliding())
					{
						p2.setHealth(p2.getHealth() - 5);
						Sound.play("Kombo.wav");
						dTime = 0;
						combo1WASDNum = 0;
					}
				}
			}

		}
	}

	private void combo1Arrow()
	{
		if (Keyboard.getEventKey() == Keyboard.KEY_UP && p2CanHit)
		{
			if (Keyboard.getEventKeyState())
			{
				oldTime2 = System.currentTimeMillis();
			} else
			{
				combo1ArrowNum++;
			}
		}

		if (Keyboard.getEventKey() == Keyboard.KEY_LEFT && p2CanHit)
		{
			if (Keyboard.getEventKeyState())
			{
				newTime2 = System.currentTimeMillis();
			} else
			{
				combo1ArrowNum++;

			}

		}

		dTime2 = (newTime2 - oldTime2);

		if (dTime2 < 400 && dTime2 > 0)
		{
			if (Keyboard.getEventKey() == Keyboard.KEY_DOWN && p2CanHit && combo1ArrowNum == 2)
			{
				if (Keyboard.getEventKeyState())
				{

				} else
				{
					if (colliding())
					{
						p1.setHealth(p1.getHealth() - 5);
						Sound.play("Kombo.wav");
						dTime2 = 0;
						combo1ArrowNum = 0;
					}
				}
			}
		}
	}

	private boolean colliding()
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

	private void finishingMoveArrow()
	{
		if (Keyboard.getEventKey() == Keyboard.KEY_DOWN && p2CanHit)
		{
			if (Keyboard.getEventKeyState())
			{
				if (p2.getY() - 0 <= HEIGHT - 400)// arbitrary
				{
					if (colliding() && playSound3Once)
					{
						Sound.play("TeaBag.wav");
						playSound3Once = false;
					}
				}
			} else
			{
			}
		}
	}

	private void finishingMoveWASD()
	{
		if (Keyboard.getEventKey() == Keyboard.KEY_S && p1CanHit)
		{
			if (Keyboard.getEventKeyState())
			{
				if (p1.getY() - 0 <= HEIGHT - 400)// arbitrary
				{
					if (colliding() && playSound3Once)
					{
						Sound.play("TeaBag.wav");
						playSound3Once = false;
					}
				}
			} else
			{
			}
		}
	}

	private void crash()
	{
		System.out.println("ERROR: GAME CRASHED");
		System.exit(1);
	}

	private int randomInt(int min, int max)
	{
		return (int) (Math.random() * max) + min;
	}

	private void input()
	{
		// 1 n 1
		if (gfxType.equals("8Bit") && player1Tex == 1 && player2Tex == 1)
		{
			pollInput(player1, player1Walk, player1Kunch, player1Flipped, player1, player1Walk, player1Kunch, player1Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 1 && player2Tex == 1)
		{
			pollInput(player1HD, player1WalkHD, player1KunchHD, player1FlippedHD, player1HD, player1WalkHD, player1KunchHD, player1FlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 1 && player2Tex == 1)
		{
			pollInput(player1, player1Walk, player1Kunch, player1Flipped, player1, player1Walk, player1Kunch, player1Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 1 && player2Tex == 1)
		{
			pollInput(player1HD, player1WalkHD, player1KunchHD, player1FlippedHD, player1HD, player1WalkHD, player1KunchHD, player1FlippedHD);
		} else
		// 2 n 2
		if (gfxType.equals("8Bit") && player1Tex == 2 && player2Tex == 2)
		{
			pollInput(player2, player2Walk, player2Kunch, player2Flipped, player2, player2Walk, player2Kunch, player2Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 2 && player2Tex == 2)
		{
			pollInput(player2HD, player2WalkHD, player2KunchHD, player2FlippedHD, player2HD, player2WalkHD, player2KunchHD, player2FlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 2 && player2Tex == 2)
		{
			pollInput(player2, player2Walk, player2Kunch, player2Flipped, player2, player2Walk, player2Kunch, player2Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 2 && player2Tex == 2)
		{
			pollInput(player2HD, player2WalkHD, player2KunchHD, player2FlippedHD, player2HD, player2WalkHD, player2KunchHD, player2FlippedHD);
		} else
		// 3 n 3
		if (gfxType.equals("8Bit") && player1Tex == 3 && player2Tex == 3)
		{
			pollInput(player3, player3Walk, player3Kunch, player3Flipped, player3, player3Walk, player3Kunch, player3Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 3 && player2Tex == 3)
		{
			pollInput(player3HD, player3WalkHD, player3KunchHD, player3FlippedHD, player3HD, player3WalkHD, player3KunchHD, player3FlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 3 && player2Tex == 3)
		{
			pollInput(player3, player3Walk, player3Kunch, player3Flipped, player3, player3Walk, player3Kunch, player3Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 3 && player2Tex == 3)
		{
			pollInput(player3HD, player3WalkHD, player3KunchHD, player3FlippedHD, player3HD, player3WalkHD, player3KunchHD, player3FlippedHD);
		} else
		// 4 n 4
		if (gfxType.equals("8Bit") && player1Tex == 4 && player2Tex == 4)
		{
			pollInput(player4, player4Walk, player4Kunch, player4Flipped, player4, player4Walk, player4Kunch, player4Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 4 && player2Tex == 4)
		{
			pollInput(player4HD, player4WalkHD, player4KunchHD, player4FlippedHD, player4HD, player4WalkHD, player4KunchHD, player4FlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 4 && player2Tex == 4)
		{
			pollInput(player4, player4Walk, player4Kunch, player4Flipped, player4, player4Walk, player4Kunch, player4Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 4 && player2Tex == 4)
		{
			pollInput(player4HD, player4WalkHD, player4KunchHD, player4FlippedHD, player4HD, player4WalkHD, player4KunchHD, player4FlippedHD);
		} else
		// 1 n 2 & 2 n 1
		if (gfxType.equals("8Bit") && player1Tex == 1 && player2Tex == 2)
		{
			pollInput(player1, player1Walk, player1Kunch, player1Flipped, player2, player2Walk, player2Kunch, player2Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 1 && player2Tex == 2)
		{
			pollInput(player1HD, player1WalkHD, player1KunchHD, player1FlippedHD, player2HD, player2WalkHD, player2KunchHD, player2FlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 2 && player2Tex == 1)
		{
			pollInput(player2, player2Walk, player2Kunch, player2Flipped, player1, player1Walk, player1Kunch, player1Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 2 && player2Tex == 1)
		{
			pollInput(player2HD, player2WalkHD, player2KunchHD, player2FlippedHD, player1HD, player1WalkHD, player1KunchHD, player1FlippedHD);
		}
		// 3 n 4 & 4 n 3
		else if (gfxType.equals("8Bit") && player1Tex == 3 && player2Tex == 4)
		{
			pollInput(player3, player3Walk, player3Kunch, player3Flipped, player4, player4Walk, player4Kunch, player4Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 3 && player2Tex == 4)
		{
			pollInput(player3HD, player3WalkHD, player3KunchHD, player3FlippedHD, player4HD, player4WalkHD, player4KunchHD, player4FlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 4 && player2Tex == 3)
		{
			pollInput(player4, player4Walk, player4Kunch, player4Flipped, player3, player3Walk, player3Kunch, player3Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 4 && player2Tex == 3)
		{
			pollInput(player4HD, player4WalkHD, player4KunchHD, player4FlippedHD, player3HD, player3WalkHD, player3KunchHD, player3FlippedHD);
		}
		// 1 n 3 & 3 n 1
		else if (gfxType.equals("8Bit") && player1Tex == 1 && player2Tex == 3)
		{
			pollInput(player1, player1Walk, player1Kunch, player1Flipped, player3, player3Walk, player3Kunch, player3Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 1 && player2Tex == 3)
		{
			pollInput(player1HD, player1WalkHD, player1KunchHD, player1FlippedHD, player3HD, player3WalkHD, player3KunchHD, player3FlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 3 && player2Tex == 1)
		{
			pollInput(player3, player3Walk, player3Kunch, player3Flipped, player1, player1Walk, player1Kunch, player1Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 3 && player2Tex == 1)
		{
			pollInput(player3HD, player3WalkHD, player3KunchHD, player3FlippedHD, player1HD, player1WalkHD, player1KunchHD, player1FlippedHD);
		}
		// 1 n 4 & 4 n 1
		else if (gfxType.equals("8Bit") && player1Tex == 1 && player2Tex == 4)
		{
			pollInput(player1, player1Walk, player1Kunch, player1Flipped, player4, player4Walk, player4Kunch, player4Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 1 && player2Tex == 4)
		{
			pollInput(player1HD, player1WalkHD, player1KunchHD, player1FlippedHD, player4HD, player4WalkHD, player4KunchHD, player4FlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 4 && player2Tex == 1)
		{
			pollInput(player4, player4Walk, player4Kunch, player4Flipped, player1, player1Walk, player1Kunch, player1Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 4 && player2Tex == 1)
		{
			pollInput(player4HD, player4WalkHD, player4KunchHD, player4FlippedHD, player1HD, player1WalkHD, player1KunchHD, player1FlippedHD);
		}
		// 2 n 3 & 3 n 2
		else if (gfxType.equals("8Bit") && player1Tex == 2 && player2Tex == 3)
		{
			pollInput(player2, player2Walk, player2Kunch, player2Flipped, player3, player3Walk, player3Kunch, player3Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 2 && player2Tex == 3)
		{
			pollInput(player2HD, player2WalkHD, player2KunchHD, player2FlippedHD, player3HD, player3WalkHD, player3KunchHD, player3FlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 3 && player2Tex == 2)
		{
			pollInput(player3, player3Walk, player3Kunch, player3Flipped, player2, player2Walk, player2Kunch, player2Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 3 && player2Tex == 2)
		{
			pollInput(player3HD, player3WalkHD, player3KunchHD, player3FlippedHD, player2HD, player2WalkHD, player2KunchHD, player2FlippedHD);
		}
		// 4 n 2 & 2 n 4
		else if (gfxType.equals("8Bit") && player1Tex == 2 && player2Tex == 4)
		{
			pollInput(player2, player2Walk, player2Kunch, player2Flipped, player4, player4Walk, player4Kunch, player4Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 2 && player2Tex == 4)
		{
			pollInput(player2HD, player2WalkHD, player2KunchHD, player2FlippedHD, player4HD, player4WalkHD, player4KunchHD, player4FlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 4 && player2Tex == 2)
		{
			pollInput(player4, player4Walk, player4Kunch, player4Flipped, player2, player2Walk, player2Kunch, player2Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 4 && player2Tex == 2)
		{
			pollInput(player4HD, player4WalkHD, player4KunchHD, player4FlippedHD, player2HD, player2WalkHD, player2KunchHD, player2FlippedHD);
		} else
		{
			crash();
		}
	}

	private void inputSP()
	{
		// 1 n 1
		if (gfxType.equals("8Bit") && player1Tex == 1 && player2Tex == 1)
		{
			pollInputSP(player1, player1Walk, player1Kunch, player1Flipped, player1, player1Walk, player1Kunch, player1Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 1 && player2Tex == 1)
		{
			pollInputSP(player1HD, player1WalkHD, player1KunchHD, player1FlippedHD, player1HD, player1WalkHD, player1KunchHD, player1FlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 1 && player2Tex == 1)
		{
			pollInputSP(player1, player1Walk, player1Kunch, player1Flipped, player1, player1Walk, player1Kunch, player1Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 1 && player2Tex == 1)
		{
			pollInputSP(player1HD, player1WalkHD, player1KunchHD, player1FlippedHD, player1HD, player1WalkHD, player1KunchHD, player1FlippedHD);
		} else
		// 2 n 2
		if (gfxType.equals("8Bit") && player1Tex == 2 && player2Tex == 2)
		{
			pollInputSP(player2, player2Walk, player2Kunch, player2Flipped, player2, player2Walk, player2Kunch, player2Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 2 && player2Tex == 2)
		{
			pollInputSP(player2HD, player2WalkHD, player2KunchHD, player2FlippedHD, player2HD, player2WalkHD, player2KunchHD, player2FlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 2 && player2Tex == 2)
		{
			pollInputSP(player2, player2Walk, player2Kunch, player2Flipped, player2, player2Walk, player2Kunch, player2Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 2 && player2Tex == 2)
		{
			pollInputSP(player2HD, player2WalkHD, player2KunchHD, player2FlippedHD, player2HD, player2WalkHD, player2KunchHD, player2FlippedHD);
		} else
		// 3 n 3
		if (gfxType.equals("8Bit") && player1Tex == 3 && player2Tex == 3)
		{
			pollInputSP(player3, player3Walk, player3Kunch, player3Flipped, player3, player3Walk, player3Kunch, player3Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 3 && player2Tex == 3)
		{
			pollInputSP(player3HD, player3WalkHD, player3KunchHD, player3FlippedHD, player3HD, player3WalkHD, player3KunchHD, player3FlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 3 && player2Tex == 3)
		{
			pollInputSP(player3, player3Walk, player3Kunch, player3Flipped, player3, player3Walk, player3Kunch, player3Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 3 && player2Tex == 3)
		{
			pollInputSP(player3HD, player3WalkHD, player3KunchHD, player3FlippedHD, player3HD, player3WalkHD, player3KunchHD, player3FlippedHD);
		} else
		// 4 n 4
		if (gfxType.equals("8Bit") && player1Tex == 4 && player2Tex == 4)
		{
			pollInputSP(player4, player4Walk, player4Kunch, player4Flipped, player4, player4Walk, player4Kunch, player4Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 4 && player2Tex == 4)
		{
			pollInputSP(player4HD, player4WalkHD, player4KunchHD, player4FlippedHD, player4HD, player4WalkHD, player4KunchHD, player4FlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 4 && player2Tex == 4)
		{
			pollInputSP(player4, player4Walk, player4Kunch, player4Flipped, player4, player4Walk, player4Kunch, player4Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 4 && player2Tex == 4)
		{
			pollInputSP(player4HD, player4WalkHD, player4KunchHD, player4FlippedHD, player4HD, player4WalkHD, player4KunchHD, player4FlippedHD);
		} else
		// 1 n 2 & 2 n 1
		if (gfxType.equals("8Bit") && player1Tex == 1 && player2Tex == 2)
		{
			pollInputSP(player1, player1Walk, player1Kunch, player1Flipped, player2, player2Walk, player2Kunch, player2Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 1 && player2Tex == 2)
		{
			pollInputSP(player1HD, player1WalkHD, player1KunchHD, player1FlippedHD, player2HD, player2WalkHD, player2KunchHD, player2FlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 2 && player2Tex == 1)
		{
			pollInputSP(player2, player2Walk, player2Kunch, player2Flipped, player1, player1Walk, player1Kunch, player1Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 2 && player2Tex == 1)
		{
			pollInputSP(player2HD, player2WalkHD, player2KunchHD, player2FlippedHD, player1HD, player1WalkHD, player1KunchHD, player1FlippedHD);
		}
		// 3 n 4 & 4 n 3
		else if (gfxType.equals("8Bit") && player1Tex == 3 && player2Tex == 4)
		{
			pollInputSP(player3, player3Walk, player3Kunch, player3Flipped, player4, player4Walk, player4Kunch, player4Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 3 && player2Tex == 4)
		{
			pollInputSP(player3HD, player3WalkHD, player3KunchHD, player3FlippedHD, player4HD, player4WalkHD, player4KunchHD, player4FlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 4 && player2Tex == 3)
		{
			pollInputSP(player4, player4Walk, player4Kunch, player4Flipped, player3, player3Walk, player3Kunch, player3Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 4 && player2Tex == 3)
		{
			pollInputSP(player4HD, player4WalkHD, player4KunchHD, player4FlippedHD, player3HD, player3WalkHD, player3KunchHD, player3FlippedHD);
		}
		// 1 n 3 & 3 n 1
		else if (gfxType.equals("8Bit") && player1Tex == 1 && player2Tex == 3)
		{
			pollInputSP(player1, player1Walk, player1Kunch, player1Flipped, player3, player3Walk, player3Kunch, player3Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 1 && player2Tex == 3)
		{
			pollInputSP(player1HD, player1WalkHD, player1KunchHD, player1FlippedHD, player3HD, player3WalkHD, player3KunchHD, player3FlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 3 && player2Tex == 1)
		{
			pollInputSP(player3, player3Walk, player3Kunch, player3Flipped, player1, player1Walk, player1Kunch, player1Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 3 && player2Tex == 1)
		{
			pollInputSP(player3HD, player3WalkHD, player3KunchHD, player3FlippedHD, player1HD, player1WalkHD, player1KunchHD, player1FlippedHD);
		}
		// 1 n 4 & 4 n 1
		else if (gfxType.equals("8Bit") && player1Tex == 1 && player2Tex == 4)
		{
			pollInputSP(player1, player1Walk, player1Kunch, player1Flipped, player4, player4Walk, player4Kunch, player4Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 1 && player2Tex == 4)
		{
			pollInputSP(player1HD, player1WalkHD, player1KunchHD, player1FlippedHD, player4HD, player4WalkHD, player4KunchHD, player4FlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 4 && player2Tex == 1)
		{
			pollInputSP(player4, player4Walk, player4Kunch, player4Flipped, player1, player1Walk, player1Kunch, player1Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 4 && player2Tex == 1)
		{
			pollInputSP(player4HD, player4WalkHD, player4KunchHD, player4FlippedHD, player1HD, player1WalkHD, player1KunchHD, player1FlippedHD);
		}
		// 2 n 3 & 3 n 2
		else if (gfxType.equals("8Bit") && player1Tex == 2 && player2Tex == 3)
		{
			pollInputSP(player2, player2Walk, player2Kunch, player2Flipped, player3, player3Walk, player3Kunch, player3Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 2 && player2Tex == 3)
		{
			pollInputSP(player2HD, player2WalkHD, player2KunchHD, player2FlippedHD, player3HD, player3WalkHD, player3KunchHD, player3FlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 3 && player2Tex == 2)
		{
			pollInputSP(player3, player3Walk, player3Kunch, player3Flipped, player2, player2Walk, player2Kunch, player2Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 3 && player2Tex == 2)
		{
			pollInputSP(player3HD, player3WalkHD, player3KunchHD, player3FlippedHD, player2HD, player2WalkHD, player2KunchHD, player2FlippedHD);
		}
		// 4 n 2 & 2 n 4
		else if (gfxType.equals("8Bit") && player1Tex == 2 && player2Tex == 4)
		{
			pollInputSP(player2, player2Walk, player2Kunch, player2Flipped, player4, player4Walk, player4Kunch, player4Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 2 && player2Tex == 4)
		{
			pollInputSP(player2HD, player2WalkHD, player2KunchHD, player2FlippedHD, player4HD, player4WalkHD, player4KunchHD, player4FlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 4 && player2Tex == 2)
		{
			pollInputSP(player4, player4Walk, player4Kunch, player4Flipped, player2, player2Walk, player2Kunch, player2Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 4 && player2Tex == 2)
		{
			pollInputSP(player4HD, player4WalkHD, player4KunchHD, player4FlippedHD, player2HD, player2WalkHD, player2KunchHD, player2FlippedHD);
		} else
		{
			crash();
		}
	}

	private void p2AI(Texture player2, Texture p2Walk, Texture player2Kunch, Texture player2Flipped)
	{
		int dmg = 0;
		if (hardness == 1)
		{
			dmg = 97;
		} else if (hardness == 2)
		{
			dmg = 95;
		} else if (hardness == 3)
		{
			dmg = 80;
		} else
		{
			crash();
		}
		if (colliding())
		{
			if (p2CanHit && !oneDied)
			{
				int r1 = randomInt(1, 100);
				if (r1 >= dmg)
				{
					p1.setHealth(p1.getHealth() - 7);
					renderTex(player2Kunch, p2.getX(), (int) p2.getY());
					Sound.play("Punch.wav");
				} else if (r1 >= dmg-5 && r1 < dmg)
				{
					renderTex(player2Kunch, p2.getX(), (int) p2.getY());
					Sound.play("Whoosh.wav");
				} else
				{
					renderTex(p2Walk, p2.getX(), (int) p2.getY());
				}
			} else
			{
				renderTex(p2Walk, p2.getX(), (int) p2.getY());
			}
		} else
		{
			if (p2CanHit && !oneDied)
			{
				int r2 = randomInt(0, 600);
				if (r2 >= 590)
				{
					renderFireBallP2 = true;
				} else
				{
					renderFireBallP2 = false;
				}

				moveAI(player2, p2Walk, player2Kunch, player2Flipped);
			} else
			{
				renderTex(p2Walk, p2.getX(), (int) p2.getY());
			}
		}

	}

	private void moveAI(Texture player2, Texture p2Walk, Texture player2Kunch, Texture player2Flipped)
	{

		if (!colliding())
		{
			if (hardness == 1)
			{
				p2.setSpeed(0.005f);
			} else if (hardness == 2)
			{
				p2.setSpeed(0.01f);
			} else if (hardness == 3)
			{
				p2.setSpeed(0.05f);
			} else
			{
				crash();
			}
			if (p2.getX() > p1.getX())
			{
				if (gfxType.equals("8Bit"))
				{
					renderTex(player2Flipped, p2.getX(), (int) p2.getY());
				} else if (gfxType.equals("HD"))
				{
					renderTex(player2Flipped, p2.getX(), (int) p2.getY());
				}
				p2.moveLeft(p2.getX() - ((long) getDelta()));
			} else if (p2.getX() <= p1.getX())
			{
				if (gfxType.equals("8Bit"))
				{
					renderTex(player2, p2.getX(), (int) p2.getY());
				} else if (gfxType.equals("HD"))
				{
					renderTex(player2, p2.getX(), (int) p2.getY());
				}
				p2.moveRight(p2.getX() + ((long) getDelta()));
			}
		}
	}

	private void gamePlay()
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

		// P1 FINISH
		if (p1.getHealth() < 6 && p1.getHealth() > 0)
		{
			oneToFinish = true;
			renderFinishIt();
			finishingMoveArrow();
			if (playSoundOnce)
			{
				Sound.play("FinishIt.wav");
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
			{
				reset();
			}
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
				Sound.play("FinishIt.wav");
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
			{
				reset();
			}
		} else
		{
			oneToFinish2 = false;
		}

		// fieball stuff
		if (p1.getX() > p2.getX())
		{
			fireBallType = 1;
		} else
		{
			fireBallType = 0;
		}

		if (p1.getX() > p2.getX())
		{
			fireBallType2 = 0;
		} else
		{
			fireBallType2 = 1;
		}

		// p1
		if (notDoneP1 && gfxType.equals("8Bit") && p1CanFireBall)
		{
			if (fireBallType == 0)
			{
				renderTex(fireBall, fireBallX, fireBallY);
			} else if (fireBallType == 1)
			{
				renderTex(fireBallFlipped, fireBallX, fireBallY);
			}
		} else if (notDoneP1 && gfxType.equals("HD") && p1CanFireBall)
		{
			if (fireBallType == 0)
			{
				renderTex(fireBallHD, fireBallX, fireBallY);
			} else if (fireBallType == 1)
			{
				renderTex(fireBallHDFlipped, fireBallX, fireBallY);
			}
		} else
		{
			fireBallX = p1.getX();
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && gfxType.equals("8Bit"))
		{
			renderFireBallP1 = true;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && gfxType.equals("HD"))
		{
			renderFireBallP1 = true;
		} else
		{
			renderFireBallP1 = false;
		}

		// p2
		if (notDoneP2 && gfxType.equals("8Bit") && p2CanFireBall)
		{
			if (fireBallType2 == 0)
			{
				renderTex(fireBall, fireBall2X, fireBall2Y);
			} else if (fireBallType2 == 1)
			{
				renderTex(fireBallFlipped, fireBall2X, fireBall2Y);
			}
		} else if (notDoneP2 && gfxType.equals("HD") && p2CanFireBall)
		{
			if (fireBallType2 == 0)
			{
				renderTex(fireBallHD, fireBall2X, fireBall2Y);
			} else if (fireBallType2 == 1)
			{
				renderTex(fireBallHDFlipped, fireBall2X, fireBall2Y);
			}
		} else
		{
			fireBall2X = p2.getX();
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) && gfxType.equals("8Bit"))
		{
			renderFireBallP2 = true;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) && gfxType.equals("HD"))
		{
			renderFireBallP2 = true;
		} else
		{
			renderFireBallP2 = false;
		}

		input();

		if (notDoneP1 && notDoneP2)
		{
			// fireBall explosion thing
			if (fireBallX >= fireBall2X && fireBallX <= fireBall2X + 256)// colliding
			{
				if (fireBallX != 0 && fireBallCollisionOnce)
				{
					if (gfxType.equals("HD"))
					{
						renderTex(explosionHD, fireBallX - 128, fireBallY - 168);
						Sound.play("Explosion.wav");
					} else if (gfxType.equals("8Bit"))
					{
						renderTex(explosion, fireBallX - 128, fireBallY - 168);
						Sound.play("Explosion.wav");
					}
					notDoneP1 = false;
					notDoneP2 = false;

					if (p1.getX() + p1.getW() >= fireBallX - 128 && fireBallX - 128 + 512 >= p1.getX())
					{
						// dmg
						p1.setHealth(p1.getHealth() - randomInt(25, 50));
					}
					if (p2.getX() + p2.getW() >= fireBallX - 128 && fireBallX - 128 + 512 >= p2.getX())
					{
						// dmg
						p2.setHealth(p2.getHealth() - randomInt(25, 50));
					}
					Display.update();
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}

					fireBallCollisionOnce = false;
				}
			}
		}
	}

	private void gamePlaySP()
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

		// P1 FINISH
		if (p1.getHealth() < 6 && p1.getHealth() > 0)
		{
			oneToFinish = true;
			renderFinishIt();
			finishingMoveArrow();
			if (playSoundOnce)
			{
				Sound.play("FinishIt.wav");
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
			{
				reset();
			}
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
				Sound.play("FinishIt.wav");
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
			{
				reset();
			}
		} else
		{
			oneToFinish2 = false;
		}

		// fieball stuff
		if (p1.getX() > p2.getX())
		{
			fireBallType = 1;
		} else
		{
			fireBallType = 0;
		}

		if (p1.getX() > p2.getX())
		{
			fireBallType2 = 0;
		} else
		{
			fireBallType2 = 1;
		}

		// p1
		if (notDoneP1 && gfxType.equals("8Bit") && p1CanFireBall)
		{
			if (fireBallType == 0)
			{
				renderTex(fireBall, fireBallX, fireBallY);
			} else if (fireBallType == 1)
			{
				renderTex(fireBallFlipped, fireBallX, fireBallY);
			}
		} else if (notDoneP1 && gfxType.equals("HD") && p1CanFireBall)
		{
			if (fireBallType == 0)
			{
				renderTex(fireBallHD, fireBallX, fireBallY);
			} else if (fireBallType == 1)
			{
				renderTex(fireBallHDFlipped, fireBallX, fireBallY);
			}
		} else
		{
			fireBallX = p1.getX();
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && gfxType.equals("8Bit"))
		{
			renderFireBallP1 = true;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && gfxType.equals("HD"))
		{
			renderFireBallP1 = true;
		} else
		{
			renderFireBallP1 = false;
		}

		// p2
		if (notDoneP2 && gfxType.equals("8Bit") && p2CanFireBall)
		{
			if (fireBallType2 == 0)
			{
				renderTex(fireBall, fireBall2X, fireBall2Y);
			} else if (fireBallType2 == 1)
			{
				renderTex(fireBallFlipped, fireBall2X, fireBall2Y);
			}
		} else if (notDoneP2 && gfxType.equals("HD") && p2CanFireBall)
		{
			if (fireBallType2 == 0)
			{
				renderTex(fireBallHD, fireBall2X, fireBall2Y);
			} else if (fireBallType2 == 1)
			{
				renderTex(fireBallHDFlipped, fireBall2X, fireBall2Y);
			}
		} else
		{
			fireBall2X = p2.getX();
		}

		// if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) &&
		// gfxType.equals("8Bit"))
		// {
		// renderFireBallP2 = true;
		// } else if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) &&
		// gfxType.equals("HD"))
		// {
		// renderFireBallP2 = true;
		// } else
		// {
		// renderFireBallP2 = false;
		// }

		inputSP();

		if (notDoneP1 && notDoneP2)
		{
			// fireBall explosion thing
			if (fireBallX >= fireBall2X && fireBallX <= fireBall2X + 256)// colliding
			{
				if (fireBallX != 0 && fireBallCollisionOnce)
				{
					System.out.println("FIREBALLS TOUCHING");
					if (gfxType.equals("HD"))
					{
						renderTex(explosionHD, fireBallX - 128, fireBallY - 168);
					} else if (gfxType.equals("8Bit"))
					{
						renderTex(explosion, fireBallX - 128, fireBallY - 168);
					}
					notDoneP1 = false;
					notDoneP2 = false;

					if (p1.getX() + p1.getW() >= fireBallX - 128 && fireBallX - 128 + 512 >= p1.getX())
					{
						// dmg
						System.out.println("DO DMGs P1");
						p1.setHealth(p1.getHealth() - randomInt(25, 50));
					}
					if (p2.getX() + p2.getW() >= fireBallX - 128 && fireBallX - 128 + 512 >= p2.getX())
					{
						// dmg
						System.out.println("DO DMGs P2");
						p2.setHealth(p2.getHealth() - randomInt(25, 50));
					}
					Display.update();
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}

					fireBallCollisionOnce = false;
				}
			}
		}
	}

	private void reset()
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
		p1CanFireBall = true;
		p2CanFireBall = true;
		notDoneP1 = false;
		fireBallCollisionOnce = true;
	}

	private void menu()
	{
		renderTex(menu, 0, 0);
		int x = Mouse.getX(); // will return the X coordinate on the Display.
		int y = Mouse.getY(); // will return the Y coordinate on the Display.
		if (Keyboard.isKeyDown(Keyboard.KEY_RETURN))
		{
			gfxType = "HD";
			beggining = false;
			gameState = 0;
			player1Tex = 1;
			player2Tex = 2;
			map = 1;
			gameMode = 0;
			Sound.play("Gong.wav");
		}

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
						gameState = 5;
						beggining = false;
					} else
					{
						gameState = 0;
					}
				}
			}

			if (x >= 480 && x <= 800)
			{
				if (y >= 225 && y <= 330)
				{
					renderTex(menuQuit, 0, 0);
					Display.update();
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					close = true;
					Display.destroy();
					System.exit(0);
				}
			}
		}
	}

	private void play()
	{
		if (gameMode == 0)
		{
			render();
			gamePlay();
		} else if (gameMode == 1)
		{
			render();
			gamePlaySP();
		} else
		{
			crash();
		}
	}

	private void gfx()
	{
		renderTex(gfx, 0, 0);
		int x = Mouse.getX(); // will return the X coordinate on the Display.
		int y = Mouse.getY(); // will return the Y coordinate on the Display.

		if (Mouse.isButtonDown(0))
		{
			if (x >= 390 && x <= 890)
			{
				if (y <= 540 && y >= 380)
				{
					renderTex(gfx8Bit, 0, 0);
					gfxType = "8Bit";
					Sound.play("Gong.wav");

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
			if (x >= 385 && x <= 890)
			{
				if (y >= 108 && y <= 270)
				{
					renderTex(gfxHD, 0, 0);
					gfxType = "HD";
					Sound.play("Gong.wav");

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

	private void charSelect()
	{
		renderTex(charPick, 0, 0);

		if (renderDot1)
		{
			glBegin(GL_QUADS);
			GL11.glColor3f(0, 0, 0);
			glVertex2f(220, HEIGHT - 370);
			glVertex2f(425, HEIGHT - 370);
			glVertex2f(425, HEIGHT - 620);
			glVertex2f(220, HEIGHT - 620);
			glEnd();
		}

		if (renderDot2)
		{
			glBegin(GL_QUADS);
			GL11.glColor3f(0, 0, 0);
			glVertex2f(220, HEIGHT - 75);
			glVertex2f(425, HEIGHT - 75);
			glVertex2f(425, HEIGHT - 350);
			glVertex2f(220, HEIGHT - 350);
			glEnd();
		}

		if (renderDot3)
		{
			glBegin(GL_QUADS);
			GL11.glColor3f(0, 0, 0);
			glVertex2f(865, HEIGHT - 370);
			glVertex2f(1080, HEIGHT - 370);
			glVertex2f(1080, HEIGHT - 620);
			glVertex2f(865, HEIGHT - 620);
			glEnd();
		}

		if (renderDot4)
		{
			glBegin(GL_QUADS);
			GL11.glColor3f(0, 0, 0);
			glVertex2f(865, HEIGHT - 75);
			glVertex2f(1080, HEIGHT - 75);
			glVertex2f(1080, HEIGHT - 350);
			glVertex2f(865, HEIGHT - 350);
			glEnd();
		}

		int x = Mouse.getX(); // will return the X coordinate on the Display.
		int y = Mouse.getY(); // will return the Y coordinate on the Display.

		if (clickCount >= 2)
		{
			gameState = 2;
		}

		if (Mouse.isButtonDown(0))
		{
			if (x >= 220 && x <= 425)// top left guy
			{
				if (y <= 620 && y >= 370 && clickCount == 0)
				{
					clickCount++;
					player1Tex = 1;
					renderDot1 = true;
					try
					{
						Thread.sleep(100);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				} else if (y <= 620 && y >= 370 && clickCount == 1)
				{
					clickCount++;
					player2Tex = 1;
					renderDot1 = true;
					try
					{
						Thread.sleep(100);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}

			if (x >= 220 && x <= 425)// bottom left guy
			{
				if (y >= 75 && y <= 320 && clickCount == 0)
				{

					clickCount++;
					player1Tex = 2;
					renderDot2 = true;
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				} else if (y >= 75 && y <= 320 && clickCount == 1)
				{
					clickCount++;
					player2Tex = 2;
					renderDot2 = true;
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}

			if (x >= 865 && x <= 1080)// top right guy
			{
				if (y >= 370 && y <= 620 && clickCount == 0)
				{
					clickCount++;
					player1Tex = 3;
					renderDot3 = true;
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				} else if (y >= 370 && y <= 620 && clickCount == 1)
				{
					clickCount++;
					player2Tex = 3;
					renderDot3 = true;
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}

			if (x >= 865 && x <= 1080)// bottom right guy
			{
				if (y >= 75 && y <= 320 && clickCount == 0)
				{
					clickCount++;
					player1Tex = 4;
					renderDot4 = true;
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				} else if (y >= 75 && y <= 320 && clickCount == 1)
				{
					clickCount++;
					player2Tex = 4;
					renderDot4 = true;
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void gameStates()
	{
		switch (gameState)
		{
		case 0:
			play();
			break;
		case 1:
			menu();
			break;
		case 2:
			gfx();
			break;
		case 3:
			charSelect();
			break;
		case 4:
			maps();
			break;
		case 5:
			gameModeChooser();
			break;
		case 6:
			difficulty();
			break;
		}
	}

	private void difficulty()
	{
		renderTex(diff, 0, 0);

		int x = Mouse.getX(); // will return the X coordinate on the Display.
		int y = Mouse.getY(); // will return the Y coordinate on the Display.

		if (Mouse.isButtonDown(0))
		{
			if (x >= 385 && x <= 895)// top
			{
				if (y <= 575 && y >= 415)
				{
					renderTex(easy, 0, 0);
					gameMode = 1;
					hardness = 1;
					Display.update();
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					gameState = 4;
				}
			}

			if (x >= 385 && x <= 895)// middle
			{
				if (y >= 240 && y <= 400)
				{
					renderTex(medium, 0, 0);
					gameMode = 1;
					hardness = 2;
					Display.update();
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					gameState = 4;
				}
			}
			if (x >= 385 && x <= 895)// bottom
			{
				if (y <= 225 && y >= 65)
				{
					renderTex(hard, 0, 0);
					gameMode = 1;
					hardness = 3;
					Display.update();
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					gameState = 4;
				}
			}
		}
	}

	private void gameModeChooser()
	{
		renderTex(spmp, 0, 0);

		int x = Mouse.getX(); // will return the X coordinate on the Display.
		int y = Mouse.getY(); // will return the Y coordinate on the Display.

		if (Mouse.isButtonDown(0))
		{
			if (x >= 400 && x <= 875)
			{
				if (y <= 545 && y >= 390)
				{
					renderTex(sp, 0, 0);
					gameMode = 1;
					Display.update();
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					gameState = 6;
				}
			}

			if (x >= 385 && x <= 890)
			{
				if (y >= 105 && y <= 270)
				{
					renderTex(mp, 0, 0);
					gameMode = 0;
					Display.update();
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					gameState = 4;
				}
			}
		}
	}

	private void maps()
	{
		renderTex(maps, 0, 0);
		int x = Mouse.getX(); // will return the X coordinate on the Display.
		int y = Mouse.getY(); // will return the Y coordinate on the Display.

		if (clickCountMaps >= 1)
		{
			gameState = 3;
		}

		if (Mouse.isButtonDown(0))
		{
			if (x >= 85 && x <= 570)// top left guy
			{
				if (y <= 630 && y >= 360 && clickCount == 0)
				{
					clickCountMaps++;
					map = 1;
					try
					{
						Thread.sleep(100);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}

			if (x >= 85 && x <= 570)// bottom left guy
			{
				if (y >= 65 && y <= 340 && clickCount == 0)
				{
					clickCountMaps++;
					map = 2;
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}

			if (x >= 700 && x <= 1195)// top right guy
			{
				if (y >= 360 && y <= 630 && clickCount == 0)
				{
					clickCountMaps++;
					map = 3;
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}

			if (x >= 700 && x <= 1195)// bottom right guy
			{
				if (y >= 65 && y <= 340 && clickCount == 0)
				{
					clickCountMaps++;
					map = 4;
					try
					{
						Thread.sleep(250);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void loop()
	{
		while (!Display.isCloseRequested())
		{
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

	private void initTexs()
	{
		try
		{
			// general things
			diff = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Diff.png"));

			easy = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Easy.png"));

			medium = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Medium.png"));

			hard = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Hard.png"));

			spmp = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/SPMP.png"));

			mp = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/MP.png"));

			sp = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/SP.png"));

			maps = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Maps.png"));

			parkBG = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/BG.png"));

			parkBGHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/BGHD.png"));

			roofBG = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Roof.png"));

			roofBGHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/RoofHD.png"));

			officeBG = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Office.png"));

			officeBGHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/OfficeHD.png"));

			sewerBG = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Sewer.png"));

			sewerBGHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/SewerHD.png"));

			explosion = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Explosion.png"));

			explosionHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/ExplosionHD.png"));

			fireBall = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/FireBall.png"));

			fireBallHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/FireBallHD.png"));

			fireBallFlipped = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/FireBallFlipped.png"));

			fireBallHDFlipped = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/FireBallHDFlipped.png"));

			gfx = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Gfx.png"));

			gfx8Bit = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Gfx8Bit.png"));

			gfxHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/GfxHD.png"));

			restart = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Restart.png"));

			itDied = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/ItDied.png"));

			FinishIt = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/FinishIt.png"));

			menu = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Menu.png"));

			menuPlay = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/MenuPlay.png"));

			menuQuit = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/MenuQuit.png"));

			charPick = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/CharacterSelection.png"));

			// player 1
			player1HealthFull = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Health/HealthLeftFull.png"));

			player1Health85 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Health/HealthLeft85.png"));

			player1Health70 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Health/HealthLeft70.png"));

			player1Health55 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Health/HealthLeft55.png"));

			player1Health40 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Health/HealthLeft40.png"));

			player1Health25 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Health/HealthLeft25.png"));

			player1Health10 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Health/HealthLeft10.png"));

			player1HealthFin = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Health/HealthLeftFin.png"));

			player1Health0 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Health/HealthLeft0.png"));

			player1 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player1/Player1.png"));

			player1Walk = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player1/Player1Walk.png"));

			player1Flipped = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player1/Player1Flipped.png"));

			player1Kunch = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player1/Player1Kunch.png"));

			// player 2
			player2HealthFull = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Health/HealthRightFull.png"));

			player2Health85 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Health/HealthRight85.png"));

			player2Health70 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Health/HealthRight70.png"));

			player2Health55 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Health/HealthRight55.png"));

			player2Health40 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Health/HealthRight40.png"));

			player2Health25 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Health/HealthRight25.png"));

			player2Health10 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Health/HealthRight10.png"));

			player2HealthFin = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Health/HealthRightFin.png"));

			player2Health0 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Health/HealthRight0.png"));

			player2 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player2/Player2.png"));

			player2Walk = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player2/Player2Walk.png"));

			player2Flipped = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player2/Player2Flipped.png"));

			player2Kunch = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player2/Player2Kunch.png"));

			// player 3 8bit
			player3 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player3/Player3.png"));

			player3Walk = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player3/Player3Walk.png"));

			player3Flipped = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player3/Player3Flipped.png"));

			player3Kunch = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player3/Player3Kunch.png"));

			// player 4 8bit
			player4 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player4/Player4.png"));

			player4Walk = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player4/Player4Walk.png"));

			player4Flipped = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player4/Player4Flipped.png"));

			player4Kunch = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8Bit/Player4/Player4Kunch.png"));

			// player 3 HD
			player3HD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/HD/Player3/Player3HD.png"));

			player3WalkHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/HD/Player3/Player3WalkHD.png"));

			player3FlippedHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/HD/Player3/Player3FlippedHD.png"));

			player3KunchHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/HD/Player3/Player3KunchHD.png"));

			// player 4 HD
			player4HD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/HD/Player4/Player4HD.png"));

			player4WalkHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/HD/Player4/Player4WalkHD.png"));

			player4FlippedHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/HD/Player4/Player4FlippedHD.png"));

			player4KunchHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/HD/Player4/Player4KunchHD.png"));

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

	private void init()
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
		p1 = new Player(120, 100, 1);
		p2 = new Player(WIDTH - 120 - 256, 100, 1);

		// init vars
		lastFrame = getTime();

		oneDied = false;
		oneToFinish = false;
		playSoundOnce = true;
		playSound2Once = true;
		playSound3Once = true;
		beggining = true;
		playDieSound = false;
		fireBallCollisionOnce = true;
		p1CanFireBall = true;
		p2CanFireBall = true;
		p1CanHit = true;
		p2CanHit = true;
		renderFireBallP1 = false;
		renderFireBallP2 = false;
		notDoneP1 = false;
		notDoneP2 = false;
		playHaduken1Once = true;
		playHaduken2Once = true;
		vSync = true;

		clickCount = 0;
		clickCountMaps = 0;
		Thread th1 = new Thread(run1);// multithreading
		th1.start();

		loop();

	}

	public static void main(String[] args)
	{
		PerishablePunchersMain game = new PerishablePunchersMain();
		game.init();
	}

	Runnable run1 = new Runnable()
	{
		@Override
		public void run()
		{

			close = false;
			while (close == false)
			{
				// p1 fireball
				if (renderFireBallP1 && p1CanFireBall)
				{
					notDoneP1 = true;
					if (playHaduken1Once && p1CanFireBall)
					{
						Sound.play("Haduken.wav");
						playHaduken1Once = false;
					}
				}
				if (p1.getX() < p2.getX())
				{
					if (fireBallX < p2.getX() && notDoneP1)
					{
						fireBallX += 35;
					} else if (fireBallX >= p2.getX() && notDoneP1)
					{
						p2.setHealth(p2.getHealth() - randomInt(20, 35));
						Sound.play("Explosion.wav");
						playHaduken1Once = true;
						p1CanFireBall = false;
						notDoneP1 = false;
					} else
					{
						notDoneP1 = false;
					}
				} else if (p1.getX() > p2.getX())
				{

					if (fireBallX > p2.getX() && notDoneP1)
					{
						fireBallX -= 35;
					} else if (fireBallX <= p2.getX() && notDoneP1)
					{
						p2.setHealth(p2.getHealth() - randomInt(20, 35));
						Sound.play("Explosion.wav");
						playHaduken1Once = true;
						p1CanFireBall = false;
						notDoneP1 = false;
					} else
					{
						notDoneP1 = false;
					}
				}

				// p2 fireabll
				if (renderFireBallP2 && p2CanFireBall)
				{
					notDoneP2 = true;
					if (playHaduken2Once && p2CanFireBall)
					{
						new Thread(haduken).start();
						playHaduken2Once = false;
					}
				}

				if (p2.getX() > p1.getX())
				{
					if (fireBall2X > p1.getX() && notDoneP2)
					{
						fireBall2X -= 35;
					} else if (fireBall2X <= p1.getX() && notDoneP2)
					{
						p1.setHealth(p1.getHealth() - randomInt(20, 35));
						Sound.play("Explosion.wav");
						playHaduken2Once = true;
						p2CanFireBall = false;
						notDoneP2 = false;
					} else
					{
						notDoneP2 = false;
					}
				} else if (p2.getX() < p1.getX())
				{
					if (fireBall2X < p1.getX() && notDoneP2)
					{
						fireBall2X += 35;
					} else if (fireBall2X >= p1.getX() && notDoneP2)
					{
						p1.setHealth(p1.getHealth() - randomInt(20, 35));
						Sound.play("Explosion.wav");
						playHaduken2Once = true;
						p2CanFireBall = false;
						notDoneP2 = false;
					} else
					{
						notDoneP2 = false;
					}
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
					Sound.play("ItDied.wav");
					playSound2Once = false;
					playDieSound = false;
				}
			}
		}
	};
}