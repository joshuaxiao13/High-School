//try this if you just want to watch ball bounce back and forth

float val1=height/2, val2=height/2;           //these will be the values controlled by the potentiometers
float x,y,speedX,speedY;    //x and y coordinates of ball, as well as the horizontal and vertical velocities of the ball
final int d=50;             //diameter of ball and is set as a constant

void setup() {
  fullScreen();     //essentially makes the interface?(not sure if that's the correct term) fit to the screen
  val1=height/2;    //set the beginning positions of the rectangles
  val2=height/2; 
  x=width/2;        //ball begins from the middle of screen
  y=height/2;
  speedX = random(4,7);    //set random vertical and horizontal speeds 
  speedY = random(4,7);
}

void draw() {
    
    background(0);       //background color to black
    textSize(80);        //text size of score
    
    circle(x, y, d);    //display the ball cenetered at (x,y)
    
    x+=speedX;      //move the ball in the x direction
    y+=speedY;      //move the ball in the y direction
    
    
    //if the ball hits the top or bottom boundaries, invert Y direction
    if(y>height || y<0) speedY*=-1;
    
    //if the ball hitsd the left boudary. invert X direction
    else if (x<30)  speedX*=-1.001;

    
    //if the ball huts the right boudary, invert X directon
    else if (x>width-30) speedX*=-1.001;
  
    rect(0,0,30,height);          //left pad
    rect(width-30,0,30,height);   //right pad
    
    println(speedX + " " +speedY);    //print in console
}

void mousePressed() {      //mouse press to begin new game
  x=width/2;
  y=height/2;
  speedX = random(4,7);
  speedY = random(4,7);
}
