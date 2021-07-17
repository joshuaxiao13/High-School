/*
The Bullet class makes the bullet move knowing the direction the bullet is moving it, its current location, and its speed. 
It also controls the size of the bullet of the diameter 
*/

public class Bullet {

    private static final double BULLET_SPEED = 8;   // constant, speed that bullet moves at cannot be changeed
    private double x, y, direction;                 // private variables to prevent other classes from accessing, for the x and y coordinates of the bullet, and direction it moves in
    private int diameter;                           // for the size of the bullet

    /*
    Constructor takes in x and y coordinates, direction, size of the bullet
    pre: x, y diameter must be within the size of the frame
    post: set the default for x, y, direction, diameter to what was passed in
    @param x is the x coordinate of the bullet
    @param y is the y coordinate of the bullet
    @param direction is the direction of the bullet
    @param diameter is the size of the bullet
    */
    public Bullet(double x, double y, double direction, int diameter) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.diameter = diameter;
    }

    /*
    public method that is used to find x coordinate of the bullet 
    pre: no condition
    post: return x coordinate
    */
    public double getX() {
        return x;
    }

    /*
    public method that is used to find y coordinate of the bullet 
    pre: no condition
    post: return y coordinate
    */
    public double getY() {
        return y;
    }

    /*
    public method that is used to find diameter of the bullet 
    pre: no condition
    post: return diameter
    */
    public int getDiameter() {
        return diameter;
    }

    /*
    public method that is used to make the bullet move
    pre: no condition
    post: make the bullet move based on the speed constant and the direction is it moving in 
    */
    public void move() {
        x += Math.cos(direction) * BULLET_SPEED;
        y += Math.sin(direction) * BULLET_SPEED;
    }
}
