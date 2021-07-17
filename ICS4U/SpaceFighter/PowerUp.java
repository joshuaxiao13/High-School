/*
PowerUp class is used to give the powerup image icons a postion, as well as to know what type of powerup it is. 
There are 2 types of power ups, health boost and bullet boost. Bullet boost makes the the bullets bigger in size, 
making them more destructive. Health Boost fills the health bar back to the full and gives an extra life if the
number of lives is less than the maximum allowed - which is 5. PowerUps are located randomly throughout the graphics
window, and appears when a player loses a life.
*/

public class PowerUp {

    //frame dimension that match the frame in the main, and the width of powerup icon that is a square
    private static final int WINDOW_WIDTH = 1500;
	private static final int WINDOW_HEIGHT = 800;
    private static final int POWERUP_WIDTH = 20; 

    private double x, y;    // private variables to prevent other classes from accessing location of the powerup icon
    private int type;       // 2 types of PowerUps: 0 = health boost, 1 = bullet boost

    /*
    constructor that gives the x and y coordinates of the powerup icon
    pre: no condition
    post: the x and y coordinated of the powerup icon will be randomly generated, 
    but they will not be at the edge of the screen or off the screen
    */
    public PowerUp() {
        x = (int) (Math.random() * (WINDOW_WIDTH - 100) + 50);
        y = (int) (Math.random() * (WINDOW_HEIGHT - 250) + 50);
        this.type = (int)(Math.random() * 2);
    }

    /*
    public method to find the x coordinate of the powerup icon
    pre: no condition
    post: returns the integer of the x coordinate
    */
    public int getX() {
        return (int) x;
    }

    /*
    pre: no condition
    post: returns the integer of the y coordinate
    */
    public int getY() {
        return (int) y;
    }

    /*
    public method to find the y coordinate of the powerup icon
    pre: no condition
    post: return the type of the powerup, type is represented with an integer
    */
    public int getType() {
        return type;
    }

    /*
    public static method to find the size of the powerup icon
    pre: no condition
    post: returns the width of the powerup icon that is a square
    */
    public static int getWidth() {
        return POWERUP_WIDTH;
    }
}
