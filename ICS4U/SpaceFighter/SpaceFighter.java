/*
The SpaceFigther class is an extension of the JFrame class and uses the KeyListener interface. 
It represents SpaceFighter objects, which is a 2-Player game. Each player has 5 lives, and the goal
of the game is to eliminate all 5 lives from the opponent. Bullet damage is directly proportional to the
diameter of the bullet, which can vary depending on PowerUp abilities, which are represented by the PowerUp class. 
*/

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class SpaceFighter extends JFrame implements KeyListener  {

  	private static final Color PURPLE = new Color(114, 0, 197);

	private static final int WINDOW_WIDTH = 1500;
	private static final int WINDOW_HEIGHT = 800;
	private static final int BULLET_DIAMETER = 10;
	private static final int POWERUP_WIDTH = 20; 

	private static final double MAX_SPEED = 5;
	private static final double MIN_SPEED = 2;

	private static final Color P1_COLOR = Color.YELLOW;
	private static final Color P2_COLOR = Color.CYAN;

	private int prevLives1, prevLives2;
	private boolean startMenu = true;
	private Spaceship p1, p2;
	private HashSet<Character> active;
	private ArrayList<PowerUp> pUp;

	/*
	Constructor that makes a game of Space Fighter, super-class is JFrame
	Precondition: no conditions
	Postconditions: a JFrame instance is created, instance variables are intialized, space-ship of two players is created
					Player 1's ship heads horizontally right, Player 2's ship heads horizontally left
	*/
	public SpaceFighter() {
		super("SPACE FIGHTER");
		super.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        super.setVisible(true);
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		super.setLocationRelativeTo(null);
		super.setBackground(PURPLE);
		active = new HashSet<Character>();
		pUp = new ArrayList<PowerUp>();
		addKeyListener(this);
		p1 = new Spaceship(WINDOW_WIDTH/4, WINDOW_HEIGHT/2, 0.0);
		p2 = new Spaceship(3*WINDOW_WIDTH/4, WINDOW_HEIGHT/2, Math.PI);
	}

	/*
	Method that triggers all the active keys in the HashSet so multiple keys can be pressed simultanously
	Precondition: no conditions
	Postconditions: certian methods are called on if the key is active
	*/
	public void triggerActive() {
		if (active.contains('y')) {
            p1.setSpeed(Math.min(MAX_SPEED, p1.getSpeed() + 0.5));
		}
		if (active.contains('g')) {
			p1.rotateCCW();
		}
		if (active.contains('j')) {
			p1.rotateCW();
		}
        if (active.contains('h')) {
            p1.setSpeed(Math.max(MIN_SPEED, p1.getSpeed() - 0.5));
        }
        if (active.contains('7')) {
            p1.addBullet(new Bullet(p1.getX(), p1.getY(), p1.getDirection(), p1.getBulletDiameter()));
        }
		if (active.contains('w')) {
            p2.setSpeed(Math.min(MAX_SPEED, p2.getSpeed() + 0.5));
		}
		if (active.contains('a')) {
			p2.rotateCCW();
		}
		if (active.contains('d')) {
			p2.rotateCW();
		}
        if (active.contains('s')) {
            p2.setSpeed(Math.max(MIN_SPEED, p2.getSpeed() - 0.5));
        }
        if (active.contains('e')) {
            p2.addBullet(new Bullet(p2.getX(), p2.getY(), p2.getDirection(), p2.getBulletDiameter()));
        }
	}

	/*
	Checks if a key is pressed
	Precondition: key is pressed and is represented by the KeyEvent argument
	Postconditions: the key is added to the HashSet and is activated
	@param event e is the key pressed
	*/
	public void keyTyped(KeyEvent e) {
		active.add(e.getKeyChar());
		triggerActive();
	}
	
	/*
	Checks if a key is released
	Precondition: key is released and is represented by the KeyEvent argument
	Postconditions: the key is removed from the HashSet and is de-activated
	@param event e is the key released
	*/
	public void keyReleased(KeyEvent e) {
		active.remove(e.getKeyChar());
		triggerActive();
	}
	
	/*
	Checks if a key is pressed and held
	Precondition: key is pressed and held and is represented by the KeyEvent argument
	Postconditions: no condition
	@param event e is the key pressed and held
	*/
	public void keyPressed(KeyEvent e) {
	}
	
	/*
	Paints the user interface
	Precondition: paramenters are a Graphics object
	Postconditions: circles representing the players' ship and bullets, as well as health bars, hearts, and power-ups are drawn on the screen
	@param Graphics object
	*/
	public void paint(Graphics g) {
		if (p1.getLives() == 0 && p2.getLives() == 0) {
			removeKeyListener(this);
			try {
				BufferedImage img = ImageIO.read(new File("Picture/draw.png"));
				g.drawImage(img, WINDOW_WIDTH/2 - 200, WINDOW_HEIGHT/3, 300, 300, null);
			} 
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return;
		}
		else if (p1.getLives() == 0) {
			removeKeyListener(this);
			try {
				BufferedImage img = ImageIO.read(new File("Picture/win1.png"));
				g.drawImage(img, WINDOW_WIDTH/2 - 200, WINDOW_HEIGHT/3, 300, 300, null);
			} 
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return;
		}
		else if (p2.getLives() == 0) {
			removeKeyListener(this);
			try {
				BufferedImage img = ImageIO.read(new File("Picture/win2.png"));
				g.drawImage(img, WINDOW_WIDTH/2 - 200, WINDOW_HEIGHT/3, 300, 300, null);
			} 
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return;
		}

		g.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

		paintShip(p1, P1_COLOR, P1_COLOR, g);
		paintShip(p2, P2_COLOR, P2_COLOR, g);

		paintHealthBar(p1, g, 100, WINDOW_HEIGHT - 100);
		paintHealthBar(p2, g, WINDOW_WIDTH - 200, WINDOW_HEIGHT - 100);

		for (int i = 0; i < 5; ++i) {
			paintHeart(100 + 30*i, WINDOW_HEIGHT - 50, (i < p1.getLives() ? Color.RED : Color.DARK_GRAY), g);
		}
		for (int i = 4; i >= 0; --i) {
			paintHeart(WINDOW_WIDTH - 200 + 30*i, WINDOW_HEIGHT - 50, (4 - i < p2.getLives() ? Color.RED : Color.DARK_GRAY), g);
		}

		for (int i = 0; i < pUp.size(); ++i) {
			PowerUp p = pUp.get(i);
			if (p.getType() == 0) {
				if (p1.gainPower(p) || p2.gainPower(p)) {
					pUp.remove(i);
					--i;
				}
				else {
					try {
						BufferedImage img = ImageIO.read(new File("Picture/bulletBoost.png"));
						g.drawImage(img, p.getX(), p.getY(), p.getWidth(), p.getWidth(), null);
					} 
					catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
				
			}
			else {
				if (p1.gainPower(p) || p2.gainPower(p)) {
					pUp.remove(i);
					--i;
				}
				else {
					try {
						BufferedImage img = ImageIO.read(new File("Picture/healthBoost.png"));
						g.drawImage(img, p.getX(), p.getY(), p.getWidth(), p.getWidth(), null);
					}
					catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}

		if (startMenu) {
			try {
				BufferedImage img = ImageIO.read(new File("Picture/start.png"));
				g.drawImage(img, WINDOW_WIDTH/2 - 200, WINDOW_HEIGHT/3, 300, 300, null);
			} 
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
			startMenu = false;
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}

		try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            System.out.println(e);
        }

		repaint();
	}

	/*
	Draws a heart with top-left corner at (x, y);
	Precondition: parameters are the (x, y) coordinates, a desired color to fill in the heart, and Graphics object
	Postconditions: a heart is drawn with top-left corner at (x, y) and filled with the correct color
	@param x is the horizontal position of the heart from the top-left corner
	@param y is the vertical position of the heart from the top-left corner
	@param col is the deisred color to fill the polygon/heart
	@param g is the Graphics object 
	*/
	public void paintHeart(int x, int y, Color col, Graphics g) {
		int[] xCoords = {0 + x, 5 + x, 10 + x, 15 + x, 20 + x, 10 + x};
		int[] yCoords = {5 + y, 0 + y, 5 + y, 0 + y, 5 + y, 20 + y};
		g.setColor(col);
		g.fillPolygon(xCoords, yCoords, 6);
	}

	/*
	Draws a health bar with top-left corner (x, y) to represent the health of a Spaceship object
	Precondition: Spaceship and Graphics objects, x and y are passed in as arguments
	Postconditions: a health bar matching the current health status of the Spaceship object is drawn with top-left corner (x, y)
	@param ship is the Spaceship object
	@param g is the Graphics object
	@param x is the horizontal position of the bar from the top-left corner
	@param y is the vertical position of the bar from the top-left corner
	*/
	public void paintHealthBar(Spaceship ship, Graphics g, int x, int y) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(x, y, 100, 10);
		g.setColor(Color.GREEN);
		g.fillRect(x, y, ship.getHealth(), 10);
	}

	/*
	Draws a Spaceship, represented by a circle, with top-left corner (x, y) 
	Precondition: Spaceship, Color, and Graphics objects are passed in as arguments
	Postconditions: a circle filled in with shipColor is drawn representing the spaceship, and bullets are drawn with bulletColor at their respective current locations, stored in the Spaceship and Bullet classes
	@param ship is the Spaceship object
	@praram shipColor is the color of the spaceship
	@param bulletColor is the color of the bullets coming out of the spaceship
	@param g is the Graphics object
	*/
	public void paintShip(Spaceship ship, Color shipColor, Color bulletColor, Graphics g) {
		ship.move();
		prevLives1 = p1.getLives();
		prevLives2 = p2.getLives();
		p1.checkDamage(p2);
		p2.checkDamage(p1);
		if (p1.getLives() != prevLives1) {
			pUp.add(new PowerUp());
			p2.setBulletDiameter(Math.max(10, p2.getBulletDiameter() - 20));
		}
		if (p2.getLives() != prevLives2) {
			pUp.add(new PowerUp());
			p1.setBulletDiameter(Math.max(10, p1.getBulletDiameter() - 20));
		}
        g.setColor(shipColor);
		g.fillOval(ship.getX(), ship.getY(), ship.getDiameter(), ship.getDiameter());
        g.setColor(bulletColor);
		ArrayList<Bullet> bullet_list = ship.getBulletArray();
        for (int i = 0; i < bullet_list.size(); ++i) {
            Bullet b = bullet_list.get(i);
            if (b.getX() > WINDOW_WIDTH || b.getX() < 0 || b.getY() > WINDOW_HEIGHT || b.getY() < 0) {
                bullet_list.remove(i);
                --i;
            }
            else {
                g.fillOval((int)b.getX(), (int)b.getY(), b.getDiameter(), b.getDiameter());
                b.move();
            }
        }
	}

	/*
	Main method
	Precondition: main method
	Postconditions: an object of SpaceFigther is created named game
	*/
	public static void main(String arg[]) {
		SpaceFighter game = new SpaceFighter();
    }	
}
