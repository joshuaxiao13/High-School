/*
Joshua Xiao, Ivy Sun
2021-06-23

The Spaceship class represents a Space-ship object, and stores the current position, direction, speed, health remaining,
lives remaining, and bullet diameter, which varies depending on the presence of power ups. It handles the collision detection with
the opponent's bullets and power ups as well, using methods with boolean return type;
*/

import java.util.ArrayList;

public class Spaceship {

    private static final int WINDOW_WIDTH = 1500;
	private static final int WINDOW_HEIGHT = 800;
	private static final int SHIP_DIAMETER = 30;
    private static final double TURN_RADIANS = 0.15;

    private double x, y, direction, speed;
    private int health, lives, bulletDiameter;

    private ArrayList<Bullet> bullet;

    /*
	Constructor that makes an object of Spaceship, a spaceship
	Precondition: x and y coordinates representing the current location, and the direction in radians are passed as arguments
	Postconditions: a Spaceship object is created after call to constructor, health is set to full, lives is initialized to 5, speed starts off at 0
	@param x is the inital x-coordinate of the spaceship
    @param y is the inital y-coordinate of the spaceship
    @param direction is the direction angle of the spaceship, in radians
	*/
    public Spaceship(double x, double y, double direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = 0;
        this.health = 100;
        this.lives = 5;
        this.bulletDiameter = 10;
        bullet = new ArrayList<Bullet>(); 
    }

    /*
	Getter method to return x-coordinate
	Precondition: no arguements are passed
	Postconditions: returns an integer, denoting the x-coordinate
	*/
    public int getX() {
        return (int) x;
    }

    /*
	Getter method to return y-coordinate
	Precondition: no arguements are passed
	Postconditions: returns an integer, denoting the y-coordinate
	*/
    public int getY() {
        return (int) y;
    }

    /*
	Getter method to return direction angle, in radians
	Precondition: no arguements are passed
	Postconditions: returns a floating-point, the direction angle in radians
	*/
    public double getDirection() {
        return direction;
    }

    /*
	Getter method to return health status, full health is 100
	Precondition: no arguements are passed
	Postconditions: returns an int, the health status
	*/
    public int getHealth() {
        return health;
    }

    /*
	Getter method to return number of lives remaining
	Precondition: no arguements are passed
	Postconditions: returns an int, number of lives remaining
	*/
    public int getLives() {
        return lives;
    }

    /*
	Getter method to return current speed
	Precondition: no arguements are passed
	Postconditions: returns a floating-point, the current speed
	*/
    public double getSpeed() {
        return speed;
    }

    /*
	Getter method to return ArrayList representing the ship's bullets
	Precondition: no arguements are passed
	Postconditions: returns ArrayList representing the ship's bullets
	*/
    public ArrayList<Bullet> getBulletArray() {
        return bullet;
    }

    /*
	Getter method to return diameter of ship's bullets
	Precondition: no arguements are passed
	Postconditions: returns an integer, the current diameter of the ship's bullets
	*/
    public int getBulletDiameter() {
        return bulletDiameter;
    }

    /*
	Getter method to return diameter of the ship
	Precondition: no arguements are passed
	Postconditions: returns an integer, the diameter of the ship
	*/
    public static int getDiameter() {
        return SHIP_DIAMETER;
    }

    /*
	Setter method to set speed of the ship to argument passed
	Precondition: the speed is passed as an argument
	Postconditions: the speed of the ship is chnaged to match speed, the argument passed
    @param speed is the new speed of the space ship
	*/
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /*
	Setter method to set the diameter of bullets of the ship to argument passed
	Precondition: the bulletDiameter is passed as an argument
	Postconditions: bulletDiamter changed to the desired size
    @param bulletDimeter is the new diameter of bullets from this ship
	*/
    public void setBulletDiameter(int bulletDiameter) {
        this.bulletDiameter = bulletDiameter;
    }

    /*
	Moves the spaceship directly proportional to the direction angle and speed of the ship
	Precondition: no condition
	Postconditions: x- and y-coordinates are changed according to the direction angle and speed
	*/
    public void move() {
        x += Math.cos(direction) * speed;
        y += Math.sin(direction) * speed;
        x %= WINDOW_WIDTH;
        y %= WINDOW_HEIGHT;
        if (x < 0) x += WINDOW_WIDTH;
        if (y < 0) y += WINDOW_HEIGHT;
    }

    /*
	Rotates the spaceship's trajectory clockwise by TURN_RADIANS
	Precondition: no condition
	Postconditions: direction angle increases by TURN_RADIANS
	*/
    public void rotateCW() {
        direction += TURN_RADIANS;
    }

    /*
	Rotates the spaceship's trajectory counter-clockwise by TURN_RADIANS
	Precondition: no condition
	Postconditions: direction angle decreases by TURN_RADIANS
	*/

    public void rotateCCW() {
        direction -= TURN_RADIANS;
    }

    /*
	A new bullet is added to the current ship
	Precondition: a Bullet object is passed as an argument
	Postconditions: a Bullet object is added to the list of bullets belonging to this ship
    @param b is the Bullet object representing the bullet
	*/
    public void addBullet(Bullet b) {
        bullet.add(b);
    }

    /*
	Checks if the ship has collided with the opponent's bullets, and does the appropriate changes to the instance variables
	Precondition: a Spaceship object is passed representing the opponent's spaceship
	Postconditions: if a collision is detected, the health decreases proportonally to the opponent's bullet size, if health dips below
                    zero, a life is taken away, the health is decreased modulo 100, for a carry-on effect
    @param ship is the opponent's spaceship object
	*/
    public void checkDamage(Spaceship ship) {
        ArrayList<Bullet> bullet_list = ship.getBulletArray();
        for (int i = 0; i < bullet_list.size() && lives > 0; ++i) {
            if (collision(bullet_list.get(i))) {
                health -= bullet_list.get(i).getDiameter();
                if (health <= 0) {
                    --lives;
                    if (lives == 0) health = 0;
                    else health += 100;
                }
                bullet_list.remove(i);
                --i;
            }
        }
    }

    /*
	Checks if the ship has gain a Power Up and does the necessary changes to the instance variabless
	Precondition: a PowerUp object is passed, which is the powerUp being checked with this Spaceship
	Postconditions: returns true if a collision is detected with the PowerUp, then depending on the type of PowerUp,
                    the bullet size increases by 20, or the number of lives increases if not already at the
                    maximum of 5 lives, and health bar is reset
    @param p is the PowerUp being checked
	*/
    public boolean gainPower(PowerUp p) {
        if (!collision(p)) return false;
        if (p.getType() == 0) {
            bulletDiameter += 20;
        }
        else {
            lives = Math.min(5, lives + 1);
            health = 100;
        }
        return true;
    }

    /*
	Returns if this spaceship collided with a bullet
	Precondition: a Bullet object is passed, the bullet being checked
	Postconditions: using pythagorean theorem to find the distance between the centers of ship and bullet, 
                    return true if distance is less than or equal to the the maximum allowed distance between
                    two circles that don't intersect, false otherwise
    @param b is the Bullet being checked
	*/
    private boolean collision(Bullet b) {
        double distanceSquared = Math.pow(x + SHIP_DIAMETER/2.0 - b.getX() - b.getDiameter()/2.0, 2) + Math.pow(y + SHIP_DIAMETER/2.0 - b.getY() - b.getDiameter()/2.0, 2);
        double maxDistance = Math.pow(SHIP_DIAMETER/2.0 + b.getDiameter()/2.0, 2);
        return Math.abs(Math.sqrt(maxDistance) - Math.sqrt(distanceSquared)) < 5;
    }

     /*
	Returns if this spaceship collided with a powerUp
	Precondition: a PowerUp object is passed, the powerUp being checked
	Postconditions: using pythagorean theorem to find the distance between the centers of ship and PowerUp, 
                    return true if distance is less than or equal to the the maximum allowed distance between
                    two circles that don't intersect, false otherwise. Note, it approximates the square shape of the
                    powerUp as a circle due to the relative small size of the PowerUp in the graphic-interface
    @param p is the PowerUp being checked
	*/
    private boolean collision(PowerUp p) {
        double distanceSquared = Math.pow(x + SHIP_DIAMETER/2.0 - p.getX() - p.getWidth()/2.0, 2) + Math.pow(y + SHIP_DIAMETER/2.0 - p.getY() - p.getWidth()/2.0, 2);
        double maxDistance = Math.pow(SHIP_DIAMETER/2.0 + p.getWidth()/2.0, 2);
        return Math.abs(Math.sqrt(maxDistance) - Math.sqrt(distanceSquared)) < 5;
    }
}