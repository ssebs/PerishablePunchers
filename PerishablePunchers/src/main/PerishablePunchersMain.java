package main;

import static org.lwjgl.opengl.GL11.*;

import java.io.*;

import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.openal.*;
import org.lwjgl.opengl.*;
import org.newdawn.slick.*;
import org.newdawn.slick.opengl.*;
import org.newdawn.slick.util.*;

/**
 * @author ssebs/Charlse
 * @version 1.0
 */
public class PerishablePunchersMain
{
	private final int WIDTH = 1280, HEIGHT = 720;
	private int p1Tiq, p2Tiq, difficultyChoice, gameState, gameMode, player1Tex, map, player2Tex, clickCount, clickCountMaps, fireBallType, fireBallType2, fireBallX, fireBallY, fireBall2X, fireBall2Y, combo1WASDNum, combo1ArrowNum;
	private static long lastFrame;
	private long oldTime, newTime, dTime, dTime2, oldTime2, newTime2;
	private String gfxType;
	private boolean renderP1Shield, renderP2Shield, isP1Blocking, isP2Blocking, playFlawlessSoundOnce, renderDot1, renderDot2, renderDot3, renderDot4, beginning, close, isP1Jumping, isP2Jumping, oneDied, oneToFinish, oneToFinish2, playFlawlessSound,
			playDieSound, playSoundOnce, playSound2Once, playSound3Once, fireBallCollisionOnce, p1CanFireBall, p2CanFireBall, p1CanHit, p2CanHit, renderFireBallP1, renderFireBallP2, notDoneP1, notDoneP2, playHaduken1Once, playHaduken2Once;
	private Player p1, p2;
	private Texture controls, shield, dargonHead, dargon, dargonWalk, dargonFlipped, dargonKunch, dargonHD, dargonWalkHD, dargonFlippedHD, dargonKunchHD, diff, easy, medium, hard, parkBG, parkBGHD, maps, roofBG, roofBGHD, officeBG, officeBGHD,
			sewerBG, sewerBGHD, fireBall, fireBallHD, fireBallFlipped, fireBallHDFlipped, charPick, gfx, gfx8Bit, gfxHD, restart, menu, itDied, FinishIt, player1, player2, player1Walk, player2Walk, player1Flipped, player2Flipped, player1Kunch,
			player2Kunch, player1HealthFull, player1Health85, player1Health70, player1Health55, player1Health40, player1Health25, player1Health10, player1HealthFin, player1Health0, player2HealthFull, player2Health85, player2Health70,
			player2Health55, player2Health40, player2Health25, player2Health10, player2HealthFin, player2Health0, player1HD, player1WalkHD, player1FlippedHD, player1KunchHD, player2HD, player2WalkHD, player2FlippedHD, player2KunchHD, player3,
			player3Walk, player3Flipped, player3Kunch, player4, player4Walk, player4Flipped, player4Kunch, player3HD, player3WalkHD, player3FlippedHD, player3KunchHD, player4HD, player4WalkHD, player4FlippedHD, player4KunchHD, explosion,
			explosionHD, sp, mp, spmp;

	/**
	 * @return Time
	 */
	private static long getTime()
	{
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * @return Delta time between updates(redraw calls)
	 */
	private static double getDelta()
	{
		long currentTime = getTime();
		double delta = (double) currentTime - (double) lastFrame;
		lastFrame = getTime();
		return delta;
	}

	/**
	 * Render "Finish it" text
	 */
	private void renderFinishIt()
	{
		if (oneToFinish)
		{
			texRender(FinishIt, WIDTH / 2 - 240, 64);
		} else if (oneToFinish2)
		{
			texRender(FinishIt, WIDTH / 2 - 240, 64);
		}
	}

	/**
	 * Render "It died" text
	 */
	private void renderItDied()
	{
		if (oneDied)
		{
			texRender(itDied, WIDTH / 2 - 260, 64);
		}
	}

	/**
	 * Render Left players health
	 */
	private void renderHealthLeft()
	{
		int health = p1.getHealth();

		if (health > 85)
		{
			texRender(player1HealthFull, 0, 0);
		} else if (health > 70)
		{
			texRender(player1Health85, 0, 0);
		} else if (health > 55)
		{
			texRender(player1Health70, 0, 0);
		} else if (health > 40)
		{
			texRender(player1Health55, 0, 0);
		} else if (health > 25)
		{
			texRender(player1Health40, 0, 0);
		} else if (health > 10)
		{
			texRender(player1Health25, 0, 0);
		} else if (health > 5)
		{
			texRender(player1Health10, 0, 0);
		} else if (health < 6 && health > 2)
		{
			texRender(player1HealthFin, 0, 0);
		} else
		{
			texRender(player1Health0, 0, 0);
		}
	}

	/**
	 * Render Right playyers Health
	 */
	private void renderHealthRight()
	{
		int health = p2.getHealth();
		if (health > 85)
		{
			texRender(player2HealthFull, 640, 0);
		} else if (health > 70)
		{
			texRender(player2Health85, 640, 0);
		} else if (health > 55)
		{
			texRender(player2Health70, 640, 0);
		} else if (health > 40)
		{
			texRender(player2Health55, 640, 0);
		} else if (health > 25)
		{
			texRender(player2Health40, 640, 0);
		} else if (health > 10)
		{
			texRender(player2Health25, 640, 0);
		} else if (health > 5)
		{
			texRender(player2Health10, 640, 0);
		} else if (health < 6 && health > 2)
		{
			texRender(player2HealthFin, 640, 0);
		} else if (health < 3)
		{
			texRender(player2Health0, 640, 0);
		}

	}

	/**
	 * Will render a QUAD with a Texture
	 * 
	 * @param tex
	 * @param x
	 * @param y
	 */
	private void texRender(Texture tex, int x, int y)
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
	 * Rendering method.
	 */
	private void render()
	{
		glClearColor(0, 0, 0, 1);
		// remember, rendered things will go ontop of another
		if (gfxType.equals("8Bit"))
		{
			if (map == 1)
			{
				texRender(parkBG, 0, 0);
			} else if (map == 2)
			{
				texRender(roofBG, 0, 0);
			} else if (map == 3)
			{
				texRender(officeBG, 0, 0);
			} else if (map == 4)
			{
				texRender(sewerBG, 0, 0);
			}

			renderHealthLeft();
			renderHealthRight();
			renderItDied();
			renderFinishIt();
		} else if (gfxType.equals("HD"))
		{
			if (map == 1)
			{
				texRender(parkBGHD, 0, 0);
			} else if (map == 2)
			{
				texRender(roofBGHD, 0, 0);
			} else if (map == 3)
			{
				texRender(officeBGHD, 0, 0);
			} else if (map == 4)
			{
				texRender(sewerBGHD, 0, 0);
			}

			renderHealthLeft();
			renderHealthRight();
			renderItDied();
			renderFinishIt();
		}
		// render a square to divide the health bar
		glBegin(GL_QUADS);
		GL11.glColor3f(0, 0, 0);
		glVertex2f(WIDTH / 2 - 5, 34);
		glVertex2f(WIDTH / 2 + 5, 34);
		glVertex2f(WIDTH / 2 + 5, 98);
		glVertex2f(WIDTH / 2 - 5, 98);
		glEnd();

	}

	/**
	 * Multiplayer Input Polling
	 * 
	 * @param player1
	 * @param p1Walk
	 * @param player1Kunch
	 * @param player1Flipped
	 * @param player2
	 * @param p2Walk
	 * @param player2Kunch
	 * @param player2Flipped
	 */
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
			// if (Keyboard.getEventKey() == Keyboard.KEY_R)
			// {
			// if (Keyboard.getEventKeyState())
			// {
			// } else
			// {
			// Display.destroy();
			// AL.destroy();
			// close = true;
			// new PerishablePunchersMain().init();
			// System.exit(0);
			// }
			// }

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
						if (!isP1Blocking)
						{
							p1.setHealth(p1.getHealth() - 5);
						} else
						{
							p1.setHealth(p1.getHealth() - 2);
							isP1Blocking = false;
						}
						AL_Punch.execute();
					} else
					{
						AL_Whoosh.execute();
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
						if (!isP2Blocking)
						{
							p2.setHealth(p2.getHealth() - 5);
						} else
						{
							p2.setHealth(p2.getHealth() - 2);
							isP2Blocking = false;
						}
						AL_Punch.execute();
					} else
					{
						AL_Whoosh.execute();
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

		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && !Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			isP1Blocking = true;
			renderP1Shield = true;
		}
		if (p1Tiq < 75 && renderP1Shield)
		{
			texRender(shield, p1.getX(), (int) p1.getY());
			isP1Blocking = true;
		} else
		{
			p1Tiq = 0;
			renderP1Shield = false;
			isP1Blocking = false;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) && !Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			isP2Blocking = true;
			renderP2Shield = true;
		}
		if (p2Tiq < 75 && renderP2Shield)
		{
			texRender(shield, p2.getX(), (int) p2.getY());
			isP2Blocking = true;
		} else
		{
			p2Tiq = 0;
			renderP2Shield = false;
			isP2Blocking = false;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			gameState = 1;
		}

	}

	/**
	 * Singleplayer Input Polling
	 * 
	 * @param player1
	 * @param p1Walk
	 * @param player1Kunch
	 * @param player1Flipped
	 * @param player2
	 * @param p2Walk
	 * @param player2Kunch
	 * @param player2Flipped
	 */
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
			// if (Keyboard.getEventKey() == Keyboard.KEY_R)
			// {
			// if (Keyboard.getEventKeyState())
			// {
			// } else
			// {
			// Display.destroy();
			// AL.destroy();
			// close = true;
			// new PerishablePunchersMain().init();
			// System.exit(0);
			// }
			// }

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
						if (!isP2Blocking)
						{
							p2.setHealth(p2.getHealth() - 5);
						} else
						{
							p2.setHealth(p2.getHealth() - 2);
							isP2Blocking = false;
						}
						AL_Punch.execute();
					} else
					{
						AL_Whoosh.execute();
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
						if (!isP2Blocking)
						{
							p2.setHealth(p2.getHealth() - 5);
						} else
						{
							p2.setHealth(p2.getHealth() - 2);
							isP2Blocking = false;
						}
						AL_Punch.execute();
					} else
					{
						AL_Whoosh.execute();
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

		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && !Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			isP1Blocking = true;
			renderP1Shield = true;
		}
		if (p1Tiq < 75 && renderP1Shield)
		{
			texRender(shield, p1.getX(), (int) p1.getY());
			isP1Blocking = true;
		} else
		{
			p1Tiq = 0;
			renderP1Shield = false;
			isP1Blocking = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			gameState = 1;
		}

	}

	/**
	 * First Combo for Player 1
	 */
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
						AL_Kombo.execute();
						dTime = 0;
						combo1WASDNum = 0;
					}
				}
			}

		}
	}

	/**
	 * First Combo for Player 2
	 */
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
						AL_Kombo.execute();
						dTime2 = 0;
						combo1ArrowNum = 0;
					}
				}
			}
		}
	}

	/**
	 * @return true if Players are colliding
	 */
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

	/**
	 * Finishing Move for Player 2
	 */
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
						AL_Teabag.execute();
						playSound3Once = false;
					}
				}
			} else
			{
			}
		}
	}

	/**
	 * Finishing Move for Player 1
	 */
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
						AL_Teabag.execute();
						playSound3Once = false;
					}
				}
			} else
			{
			}
		}
	}

	/**
	 * Something happened when it wasn't supposed to, so this will stop the game
	 */
	private void crash()
	{
		System.out.println("ERROR: GAME CRASHED");
		System.exit(1);
	}

	/**
	 * @param min
	 * @param max
	 * @return random integer between the range specified
	 */
	private int randomInt(int min, int max)
	{
		return (int) (Math.random() * max) + min;
	}

	/**
	 * Will call different pollInputs based on the players' Textures
	 */
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
		// 5 n 5
		if (gfxType.equals("8Bit") && player1Tex == 5 && player2Tex == 5)
		{
			pollInput(dargon, dargonWalk, dargonKunch, dargonFlipped, dargon, dargonWalk, dargonKunch, dargonFlipped);
		} else if (gfxType.equals("HD") && player1Tex == 5 && player2Tex == 5)
		{
			pollInput(dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD, dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 5 && player2Tex == 5)
		{
			pollInput(dargon, dargonWalk, dargonKunch, dargonFlipped, dargon, dargonWalk, dargonKunch, dargonFlipped);
		} else if (gfxType.equals("HD") && player1Tex == 5 && player2Tex == 5)
		{
			pollInput(dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD, dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD);
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

		// 1 n 5
		if (gfxType.equals("8Bit") && player1Tex == 1 && player2Tex == 5)
		{
			pollInput(player1, player1Walk, player1Kunch, player1Flipped, dargon, dargonWalk, dargonKunch, dargonFlipped);
		} else if (gfxType.equals("HD") && player1Tex == 1 && player2Tex == 5)
		{
			pollInput(player1HD, player1WalkHD, player1KunchHD, player1FlippedHD, dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 5 && player2Tex == 1)
		{
			pollInput(dargon, dargonWalk, dargonKunch, dargonFlipped, player1, player1Walk, player1Kunch, player1Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 5 && player2Tex == 1)
		{
			pollInput(dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD, player1HD, player1WalkHD, player1KunchHD, player1FlippedHD);
		} else
		// 2 n 5
		if (gfxType.equals("8Bit") && player1Tex == 2 && player2Tex == 5)
		{
			pollInput(player2, player2Walk, player2Kunch, player2Flipped, dargon, dargonWalk, dargonKunch, dargonFlipped);
		} else if (gfxType.equals("HD") && player1Tex == 2 && player2Tex == 5)
		{
			pollInput(player2HD, player2WalkHD, player2KunchHD, player2FlippedHD, dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 5 && player2Tex == 2)
		{
			pollInput(dargon, dargonWalk, dargonKunch, dargonFlipped, player2, player2Walk, player2Kunch, player2Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 5 && player2Tex == 2)
		{
			pollInput(player2HD, player2WalkHD, player2KunchHD, player2FlippedHD, player2HD, player2WalkHD, player2KunchHD, player2FlippedHD);
		} else
		// 3 n 5
		if (gfxType.equals("8Bit") && player1Tex == 3 && player2Tex == 5)
		{
			pollInput(player3, player3Walk, player3Kunch, player3Flipped, dargon, dargonWalk, dargonKunch, dargonFlipped);
		} else if (gfxType.equals("HD") && player1Tex == 3 && player2Tex == 5)
		{
			pollInput(player3HD, player3WalkHD, player3KunchHD, player3FlippedHD, dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 5 && player2Tex == 3)
		{
			pollInput(dargon, dargonWalk, dargonKunch, dargonFlipped, player3, player3Walk, player3Kunch, player3Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 5 && player2Tex == 3)
		{
			pollInput(dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD, player3HD, player3WalkHD, player3KunchHD, player3FlippedHD);
		} else
		// 4 n 5
		if (gfxType.equals("8Bit") && player1Tex == 4 && player2Tex == 5)
		{
			pollInput(player4, player4Walk, player4Kunch, player4Flipped, dargon, dargonWalk, dargonKunch, dargonFlipped);
		} else if (gfxType.equals("HD") && player1Tex == 4 && player2Tex == 5)
		{
			pollInput(player4HD, player4WalkHD, player4KunchHD, player4FlippedHD, dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 5 && player2Tex == 4)
		{
			pollInput(dargon, dargonWalk, dargonKunch, dargonFlipped, player4, player4Walk, player4Kunch, player4Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 5 && player2Tex == 4)
		{
			pollInput(dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD, player4HD, player4WalkHD, player4KunchHD, player4FlippedHD);
		} else
		{
			crash();
		}
	}

	/**
	 * Will call different pollInputs based on the players' Textures, but for
	 * Singleplayer
	 */
	private void inputSP()
	{
		// 1 n 1
		if (gfxType.equals("8Bit") && player1Tex == 1 && player2Tex == 1)
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
		} else
		// 3 n 3
		if (gfxType.equals("8Bit") && player1Tex == 3 && player2Tex == 3)
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
		} else
		// 5 n 5
		if (gfxType.equals("8Bit") && player1Tex == 5 && player2Tex == 5)
		{
			pollInputSP(dargon, dargonWalk, dargonKunch, dargonFlipped, dargon, dargonWalk, dargonKunch, dargonFlipped);
			// System.out.println("5 n 5, 8 bit");
		} else if (gfxType.equals("HD") && player1Tex == 5 && player2Tex == 5)
		{
			pollInputSP(dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD, dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD);
			// System.out.println("5 n 5, HD");
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
		// 1 n 5
		if (gfxType.equals("8Bit") && player1Tex == 1 && player2Tex == 5)
		{
			pollInputSP(player1, player1Walk, player1Kunch, player1Flipped, dargon, dargonWalk, dargonKunch, dargonFlipped);
		} else if (gfxType.equals("HD") && player1Tex == 1 && player2Tex == 5)
		{
			pollInputSP(player1HD, player1WalkHD, player1KunchHD, player1FlippedHD, dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 5 && player2Tex == 1)
		{
			pollInputSP(dargon, dargonWalk, dargonKunch, dargonFlipped, player1, player1Walk, player1Kunch, player1Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 5 && player2Tex == 1)
		{
			pollInputSP(dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD, player1HD, player1WalkHD, player1KunchHD, player1FlippedHD);
		} else
		// 2 n 5
		if (gfxType.equals("8Bit") && player1Tex == 2 && player2Tex == 5)
		{
			pollInputSP(player2, player2Walk, player2Kunch, player2Flipped, dargon, dargonWalk, dargonKunch, dargonFlipped);
		} else if (gfxType.equals("HD") && player1Tex == 2 && player2Tex == 5)
		{
			pollInputSP(player2HD, player2WalkHD, player2KunchHD, player2FlippedHD, dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 5 && player2Tex == 2)
		{
			pollInputSP(dargon, dargonWalk, dargonKunch, dargonFlipped, player2, player2Walk, player2Kunch, player2Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 5 && player2Tex == 2)
		{
			pollInputSP(player2HD, player2WalkHD, player2KunchHD, player2FlippedHD, player2HD, player2WalkHD, player2KunchHD, player2FlippedHD);
		} else
		// 3 n 5
		if (gfxType.equals("8Bit") && player1Tex == 3 && player2Tex == 5)
		{
			pollInputSP(player3, player3Walk, player3Kunch, player3Flipped, dargon, dargonWalk, dargonKunch, dargonFlipped);
		} else if (gfxType.equals("HD") && player1Tex == 3 && player2Tex == 5)
		{
			pollInputSP(player3HD, player3WalkHD, player3KunchHD, player3FlippedHD, dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 5 && player2Tex == 3)
		{
			pollInputSP(dargon, dargonWalk, dargonKunch, dargonFlipped, player3, player3Walk, player3Kunch, player3Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 5 && player2Tex == 3)
		{
			pollInputSP(dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD, player3HD, player3WalkHD, player3KunchHD, player3FlippedHD);
		} else
		// 4 n 5
		if (gfxType.equals("8Bit") && player1Tex == 4 && player2Tex == 5)
		{
			pollInputSP(player4, player4Walk, player4Kunch, player4Flipped, dargon, dargonWalk, dargonKunch, dargonFlipped);
		} else if (gfxType.equals("HD") && player1Tex == 4 && player2Tex == 5)
		{
			pollInputSP(player4HD, player4WalkHD, player4KunchHD, player4FlippedHD, dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD);
		} else if (gfxType.equals("8Bit") && player1Tex == 5 && player2Tex == 4)
		{
			pollInputSP(dargon, dargonWalk, dargonKunch, dargonFlipped, player4, player4Walk, player4Kunch, player4Flipped);
		} else if (gfxType.equals("HD") && player1Tex == 5 && player2Tex == 4)
		{
			pollInputSP(dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD, player4HD, player4WalkHD, player4KunchHD, player4FlippedHD);
		} else
		{
			System.out.println("P1: " + player1Tex + "\nP2: " + player2Tex);
			crash();
		}
	}

	/**
	 * Generic AI Algorithm
	 * 
	 * @param player2
	 * @param p2Walk
	 * @param player2Kunch
	 * @param player2Flipped
	 */
	private void p2AI(Texture player2, Texture p2Walk, Texture player2Kunch, Texture player2Flipped)
	{
		int dmg = 0;
		if (difficultyChoice == 1)
		{
			dmg = 97;
		} else if (difficultyChoice == 2)
		{
			dmg = 95;
		} else if (difficultyChoice == 3)
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
					texRender(player2Kunch, p2.getX(), (int) p2.getY());
					AL_Punch.execute();
				} else if (r1 >= dmg - 5 && r1 < dmg)
				{
					texRender(player2Kunch, p2.getX(), (int) p2.getY());
					AL_Whoosh.execute();
				} else
				{
					texRender(p2Walk, p2.getX(), (int) p2.getY());
				}
			} else
			{
				texRender(p2Walk, p2.getX(), (int) p2.getY());
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

				if (player2Tex == 5)
				{
					if (gfxType.equals("8Bit"))
					{
						moveAI(dargon, dargonWalk, dargonKunch, dargonFlipped);
					} else
					{
						moveAI(dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD);
					}
				}

				if (player1Tex == 5 && player2Tex == 5)
				{
					moveAI(dargonHD, dargonWalkHD, dargonKunchHD, dargonFlippedHD);
				} else
				{
					moveAI(player2, p2Walk, player2Kunch, player2Flipped);
				}

			} else
			{
				texRender(p2Walk, p2.getX(), (int) p2.getY());
			}
		}

	}

	/**
	 * AI Algorithm that moves the AI
	 * 
	 * @param player2
	 * @param p2Walk
	 * @param player2Kunch
	 * @param player2Flipped
	 */
	private void moveAI(Texture player2, Texture p2Walk, Texture player2Kunch, Texture player2Flipped)
	{

		if (!colliding())
		{
			if (difficultyChoice == 1)
			{
				p2.setSpeed(0.005f);
			} else if (difficultyChoice == 2)
			{
				p2.setSpeed(0.01f);
			} else if (difficultyChoice == 3)
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
					texRender(player2Flipped, p2.getX(), (int) p2.getY());
				} else if (gfxType.equals("HD"))
				{
					texRender(player2Flipped, p2.getX(), (int) p2.getY());
				}
				p2.moveLeft(p2.getX() - ((long) getDelta()));
			} else if (p2.getX() <= p1.getX())
			{
				if (gfxType.equals("8Bit"))
				{
					texRender(player2, p2.getX(), (int) p2.getY());
				} else if (gfxType.equals("HD"))
				{
					texRender(player2, p2.getX(), (int) p2.getY());
				}
				p2.moveRight(p2.getX() + ((long) getDelta()));
			}
		}
	}

	/**
	 * Generic Gameplay method(Multiplayer)
	 */
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
				AL_FinishIt.execute();
				playSoundOnce = false;
			}
			// P1 DIED
		} else if (p1.getHealth() < 1)
		{
			oneToFinish = false;
			oneDied = true;
			renderItDied();
			texRender(restart, WIDTH / 2 - 256, 256);
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
				AL_FinishIt.execute();
				playSoundOnce = false;
			}
			// P2 DIED
		} else if (p2.getHealth() < 1)
		{
			oneToFinish2 = false;
			oneDied = true;
			renderItDied();
			texRender(restart, WIDTH / 2 - 256, 256);
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
				texRender(fireBall, fireBallX, fireBallY);
			} else if (fireBallType == 1)
			{
				texRender(fireBallFlipped, fireBallX, fireBallY);
			}
		} else if (notDoneP1 && gfxType.equals("HD") && p1CanFireBall)
		{
			if (fireBallType == 0)
			{
				texRender(fireBallHD, fireBallX, fireBallY);
			} else if (fireBallType == 1)
			{
				texRender(fireBallHDFlipped, fireBallX, fireBallY);
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
				texRender(fireBall, fireBall2X, fireBall2Y);
			} else if (fireBallType2 == 1)
			{
				texRender(fireBallFlipped, fireBall2X, fireBall2Y);
			}
		} else if (notDoneP2 && gfxType.equals("HD") && p2CanFireBall)
		{
			if (fireBallType2 == 0)
			{
				texRender(fireBallHD, fireBall2X, fireBall2Y);
			} else if (fireBallType2 == 1)
			{
				texRender(fireBallHDFlipped, fireBall2X, fireBall2Y);
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
				if (fireBallX != 0 && fireBallCollisionOnce && p1CanFireBall && p2CanFireBall)
				{
					p1CanFireBall = false;
					p2CanFireBall = false;
					if (gfxType.equals("HD"))
					{
						texRender(explosionHD, fireBallX - 128, fireBallY - 168);
						AL_Explosion.execute();
					} else if (gfxType.equals("8Bit"))
					{
						texRender(explosion, fireBallX - 128, fireBallY - 168);
						AL_Explosion.execute();
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

	/**
	 * Generic Gameplay method(Singleplayer)
	 */
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
				AL_FinishIt.execute();
				playSoundOnce = false;
			}
			// P1 DIED
		} else if (p1.getHealth() < 1)
		{
			oneToFinish = false;
			oneDied = true;
			renderItDied();
			texRender(restart, WIDTH / 2 - 256, 256);
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
				AL_FinishIt.execute();
				playSoundOnce = false;
			}
			// P2 DIED
		} else if (p2.getHealth() < 1)
		{
			oneToFinish2 = false;
			oneDied = true;
			renderItDied();
			texRender(restart, WIDTH / 2 - 256, 256);
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
				texRender(fireBall, fireBallX, fireBallY);
			} else if (fireBallType == 1)
			{
				texRender(fireBallFlipped, fireBallX, fireBallY);
			}
		} else if (notDoneP1 && gfxType.equals("HD") && p1CanFireBall)
		{
			if (fireBallType == 0)
			{
				texRender(fireBallHD, fireBallX, fireBallY);
			} else if (fireBallType == 1)
			{
				texRender(fireBallHDFlipped, fireBallX, fireBallY);
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
				texRender(fireBall, fireBall2X, fireBall2Y);
			} else if (fireBallType2 == 1)
			{
				texRender(fireBallFlipped, fireBall2X, fireBall2Y);
			}
		} else if (notDoneP2 && gfxType.equals("HD") && p2CanFireBall)
		{
			if (fireBallType2 == 0)
			{
				texRender(fireBallHD, fireBall2X, fireBall2Y);
			} else if (fireBallType2 == 1)
			{
				texRender(fireBallHDFlipped, fireBall2X, fireBall2Y);
			}
		} else
		{
			fireBall2X = p2.getX();
		}
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
						texRender(explosionHD, fireBallX - 128, fireBallY - 168);
					} else if (gfxType.equals("8Bit"))
					{
						texRender(explosion, fireBallX - 128, fireBallY - 168);
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

	/**
	 * Will reset the variables needed to play so that you can have a rematch
	 */
	private void reset()
	{
		p1.setHealth(p1.getMaxHealth());
		p1.setX(120);
		p1.setY(100);

		p2.setHealth(p2.getMaxHealth());
		p2.setX(WIDTH - 120 - 256);
		p2.setY(100);

		oneDied = false;
		playSoundOnce = true;
		playSound2Once = true;
		playSound3Once = true;
		playDieSound = false;
		p1CanFireBall = true;
		p2CanFireBall = true;
		playHaduken1Once = true;
		playHaduken2Once = true;
		notDoneP1 = false;
		fireBallCollisionOnce = true;
		playFlawlessSound = true;
		playFlawlessSoundOnce = true;
	}

	/**
	 * Will render the Menu and check to see if a button was pressed
	 */
	private void menu()
	{
		texRender(menu, 0, 0);
		int x = Mouse.getX(); // will return the X coordinate on the Display.
		int y = Mouse.getY(); // will return the Y coordinate on the Display.
		if (Keyboard.isKeyDown(Keyboard.KEY_RETURN))
		{
			gfxType = "HD";
			beginning = false;
			gameState = 0;
			player1Tex = 1;
			player2Tex = 5;
			map = 1;
			gameMode = 0;
			AL_Gong.execute();

		}
		if (Mouse.isButtonDown(0))
		{
			if (x >= 400 && x <= 875)
			{
				if (y <= 545 && y >= 390)
				{
					// texRender(menuPlay, 0, 0);
					// Display.update();
					try
					{
						Thread.sleep(100);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}

					if (beginning)
					{
						gameState = 5;
						beginning = false;
					} else
					{
						gameState = 0;
					}
				}
			}

			if (x >= 480 && x <= 800)// conrols
			{
				if (y >= 225 && y <= 330)
				{
					try
					{
						Thread.sleep(100);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					gameState = 7;
				}
			}

			if (x >= 480 && x <= 800)
			{
				if (y >= 70 && y <= 170)
				{
					try
					{
						Thread.sleep(100);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					close = true;
					AL.destroy();
					Display.destroy();
					System.exit(0);
				}
			}
		}
	}

	/**
	 * First method being called when playing. This checks what game mode your
	 * in and calls the appropriate methods
	 */
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

	/**
	 * Graphics Page
	 */
	private void graphicsSelector()
	{
		texRender(gfx, 0, 0);
		int x = Mouse.getX(); // will return the X coordinate on the Display.
		int y = Mouse.getY(); // will return the Y coordinate on the Display.

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			clickCount = 0;
			gameState = 3;
			renderDot1 = false;
			renderDot2 = false;
			renderDot3 = false;
			renderDot4 = false;
			Display.update();
			try
			{
				Thread.sleep(250);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		if (Mouse.isButtonDown(0))
		{
			if (x >= 390 && x <= 890)
			{
				if (y <= 540 && y >= 380)
				{
					texRender(gfx8Bit, 0, 0);
					gfxType = "8Bit";
					AL_Gong.execute();

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
					texRender(gfxHD, 0, 0);
					gfxType = "HD";
					AL_Gong.execute();
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

	/**
	 * Character selection page
	 */
	private void charSelect()
	{
		texRender(charPick, 0, 0);

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			if ((clickCountMaps == 0))
			{
				clickCountMaps = 0;
				gameState = 4;
				Display.update();
				try
				{
					Thread.sleep(250);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}

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
		// easter egg

		if (Mouse.isButtonDown(0))
		{
			// easter egg
			if (x <= 85)
			{
				if (y >= 720 - 185 && clickCount == 0)
				{
					clickCount++;
					player1Tex = 5;
					try
					{
						Thread.sleep(100);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				} else if (y >= 720 - 185 && clickCount == 1)
				{
					clickCount++;
					player2Tex = 5;
					try
					{
						Thread.sleep(100);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}

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
		if (x <= 85)
		{
			if (y >= 720 - 185)
			{
				texRender(dargonHead, 0, 0);
			}
		}
	}

	/**
	 * Will check gamestates. Useful for having a menu/pausing/etc
	 */
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
			graphicsSelector();
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
		case 7:
			controlViewer();
			break;
		}
	}

	/**
	 * Will render Controls page
	 */
	private void controlViewer()
	{
		texRender(controls, 0, 0);
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			gameState = 1;
		}
	}

	/**
	 * Choose difficulty after singleplayer is clicked
	 */
	private void difficulty()
	{
		texRender(diff, 0, 0);

		int x = Mouse.getX(); // will return the X coordinate on the Display.
		int y = Mouse.getY(); // will return the Y coordinate on the Display.

		if (Mouse.isButtonDown(0))
		{
			if (x >= 385 && x <= 895)// top
			{
				if (y <= 575 && y >= 415)
				{
					texRender(easy, 0, 0);
					gameMode = 1;
					difficultyChoice = 1;
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
					texRender(medium, 0, 0);
					gameMode = 1;
					difficultyChoice = 2;
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
					texRender(hard, 0, 0);
					gameMode = 1;
					difficultyChoice = 3;
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

	/**
	 * Will let you choose Singleplayer or Multiplayer
	 */
	private void gameModeChooser()
	{
		texRender(spmp, 0, 0);

		int x = Mouse.getX(); // will return the X coordinate on the Display.
		int y = Mouse.getY(); // will return the Y coordinate on the Display.

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			beginning = true;
			gameState = 1;
		}

		if (Mouse.isButtonDown(0))
		{
			if (x >= 400 && x <= 875)
			{
				if (y <= 545 && y >= 390)
				{
					texRender(sp, 0, 0);
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
					texRender(mp, 0, 0);
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

	/**
	 * Will let you choose the map you want to play on
	 */
	private void maps()
	{
		texRender(maps, 0, 0);
		int x = Mouse.getX(); // will return the X coordinate on the Display.
		int y = Mouse.getY(); // will return the Y coordinate on the Display.

		if (clickCountMaps == 1)
		{
			gameState = 3;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			gameState = 5;
			try
			{
				Thread.sleep(250);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
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

	/**
	 * The main game loop
	 */
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
			Display.sync(60);
			Display.update();// updates screen
		}
		AL.destroy();
		Display.destroy();
		close = true;
	}

	/**
	 * Will load all Textures
	 * 
	 * @return if all textures are loaded.
	 */
	private boolean initTexs()
	{
		try
		{
			// general things

			// controls = TextureLoader.getTexture("PNG",
			// ResourceLoader.getResourceAsStream("res/Other/Controls.png"));
			// This gets loaded during first init ^^^

			shield = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Shield.png"));

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

			// dargon textures
			dargonHead = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Dargon/DargonHead.png"));

			// 8bit
			dargon = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Dargon/Dargon.png"));

			dargonWalk = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Dargon/DargonWalk.png"));

			dargonFlipped = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Dargon/DargonFlipped.png"));

			dargonKunch = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Dargon/DargonKunch.png"));

			// hd
			dargonHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Dargon/DargonHD.png"));

			dargonWalkHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Dargon/DargonWalkHD.png"));

			dargonFlippedHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Dargon/DargonFlippedHD.png"));

			dargonKunchHD = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Dargon/DargonKunchHD.png"));

		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * Will Initialize the game
	 */
	private void init()
	{
		try
		{
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("Loading...");
			Display.setInitialBackground(0, 0, 0);
			Display.create();
			try
			{
				controls = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Other/Controls.png"));
			} catch (IOException e)
			{
				e.printStackTrace();
			}

		} catch (LWJGLException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		do
		{
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
			glMatrixMode(GL_MODELVIEW);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			texRender(controls, 0, 0);

			Display.sync(60);
			Display.update();// updates screen
		} while (!initTexs());

		// create objects here
		int speed = 1;
		int health = 100;
		p1 = new Player(120, 100, speed, health);
		p2 = new Player(WIDTH - 120 - 256, 100, speed, health);

		// init vars
		lastFrame = getTime();

		oneDied = false;
		oneToFinish = false;
		playSoundOnce = true;
		playSound2Once = true;
		playSound3Once = true;
		beginning = true;
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
		playFlawlessSound = true;
		playFlawlessSoundOnce = true;
		isP1Blocking = false;
		isP2Blocking = false;

		clickCount = 0;
		clickCountMaps = 0;
		p1Tiq = 0;
		p2Tiq = 0;
		gameState = 1;
		fireBallX = 0;
		fireBallY = 512;
		fireBall2X = 0;
		fireBall2Y = 512;
		combo1WASDNum = 0;
		combo1ArrowNum = 0;
		fireBallType = 0;
		fireBallType2 = 0;

		oldTime = 0;
		newTime = 0;
		oldTime2 = 0;
		newTime2 = 0;

		try
		{
			AL.create();
		} catch (LWJGLException le)
		{
			le.printStackTrace();
			return;
		}

		Thread th1 = new Thread(run1);// multithreading
		th1.start();
		loop();

	}

	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		PerishablePunchersMain game = new PerishablePunchersMain();
		game.init();
	}

	/**
	 * Second thread
	 */
	Runnable run1 = new Runnable()
	{
		@Override
		public void run()
		{

			close = false;
			while (close == false)
			{
				if (isP1Blocking)
				{
					p1Tiq++;
				} else
				{
					p1Tiq = 0;
				}
				if (isP2Blocking)
				{
					p2Tiq++;
				} else
				{
					p2Tiq = 0;
				}
				// p1 fireball
				if (renderFireBallP1 && p1CanFireBall)
				{
					notDoneP1 = true;
					if (playHaduken1Once && p1CanFireBall)
					{
						if (player1Tex != 5)
						{
							AL_Haduken.execute();
						} else
						{
							AL_Scream.execute();
						}
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
						if (!isP2Blocking)
						{
							p2.setHealth(p2.getHealth() - randomInt(20, 35));
						} else
						{
							p2.setHealth(p2.getHealth() - randomInt(5, 20));
							isP2Blocking = false;
						}
						AL_Explosion.execute();
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
						if (!isP2Blocking)
						{
							p2.setHealth(p2.getHealth() - randomInt(20, 35));
						} else
						{
							p2.setHealth(p2.getHealth() - randomInt(5, 20));
							isP2Blocking = false;
						}
						AL_Explosion.execute();
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
						if (player2Tex != 5)
						{
							AL_Haduken.execute();
						} else
						{
							AL_Scream.execute();
						}
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
						if (!isP1Blocking)
						{
							p1.setHealth(p1.getHealth() - randomInt(20, 35));
						} else
						{
							p1.setHealth(p1.getHealth() - randomInt(5, 20));
							isP1Blocking = false;
						}
						AL_Explosion.execute();
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
						if (!isP1Blocking)
						{
							p1.setHealth(p1.getHealth() - randomInt(20, 35));
						} else
						{
							p1.setHealth(p1.getHealth() - randomInt(5, 20));
							isP1Blocking = false;
						}
						AL_Explosion.execute();
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
					AL_ItDied.execute();
					playSound2Once = false;
					playDieSound = false;
				}
				if (p1.getHealth() == p1.getMaxHealth() || p2.getHealth() == p2.getMaxHealth())
				{
					playFlawlessSound = true;
				}

				if (oneDied && p1.getHealth() == p1.getMaxHealth() && playFlawlessSound && playFlawlessSoundOnce)
				{
					try
					{
						Thread.sleep(1200);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					AL_UC.execute();
					playFlawlessSound = false;
					playFlawlessSoundOnce = false;
				}
				if (oneDied && p2.getHealth() == p2.getMaxHealth() && playFlawlessSound && playFlawlessSoundOnce)
				{
					try
					{
						Thread.sleep(1200);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					AL_UC.execute();
					playFlawlessSound = false;
					playFlawlessSoundOnce = false;
				}
			}
		}
	};
}