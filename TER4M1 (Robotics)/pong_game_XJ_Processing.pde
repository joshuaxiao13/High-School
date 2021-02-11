import processing.serial.*;  //import the Serial library

int end = 10;                  // The number 10 is the ASCII code for linefeed (end of serial.println), used to break up individual messages.
String receivedString=null;    // this will be the string from the aurdiono serial monitor, assinged null in the start
Serial myPort;                 // new instance of the Serial class (an Object)

float val1=height/2, val2=height/2;           //these will be the values controlled by the potentiometers
float x,y,speedX,speedY;    //x and y coordinates of ball, as well as the horizontal and vertical velocities of the ball
final int d=50;             //diameter of ball and is set as a constant

int counter1=0, counter2=0;    //counters for the number of wins in a match
boolean gameEnded=false;       //boolean to check if there is a winner

void setup() {
  fullScreen();     //essentially makes the interface?(not sure if that's the correct term) fit to the screen
  myPort= new Serial(this, Serial.list()[0], 9600); // initializing the object by assigning a port to COM1
  myPort.clear();  // function from serial library that throws out the first reading, in case we started reading in the middle of a string from Arduino
  receivedString = myPort.readStringUntil(end); // function reads the string from serial port until a new line occurs, then initiates the String receivedString to the reading
  val1=height/2;    //set the beginning positions of the rectangles
  val2=height/2; 
  x=width/2;        //ball begins from the middle of screen
  y=height/2;
  speedX = random(4,7);    //set random vertical and horizontal speeds 
  speedY = random(4,7);
}

void draw() {
  if(!gameEnded) {    //if there isnt a winner (first to five)
    
    background(0);       //background color to black
    textSize(80);        //text size of score
    text(counter1+"    :    "+counter2, 800, 200);      //display the score
  
   while (myPort.available () > 0) { //it there is data coming from serial port, read and store it 
    receivedString = myPort.readStringUntil(end);    //read until a new line and assign the string
    }
  if (receivedString != null) {  //if the string is not empty

    //split function is useful for parsing (separating) messages when reading from multiple inputs in Arduino (got this split function from the web)

    String[] a = split(receivedString, ',');  // a new array (called 'a') that stores values into separate cells (separated by commas specified in your Arduino program)
    //println(a[0]); //print the first string value of the array
    //println(a[1]); //print to the console the second string value
    val1 = Integer.parseInt(a[0].trim());     // (got from the web)
    val2 = Integer.parseInt(a[1].trim());     // (got from the web)
    val1= map(val1, 0, 1023,0,height-200);    //map the value so that the rectangles don't "clear" the screen
    val2= map(val2, 0, 1023,0,height-200);    //map the value so that the rectangles don't "clear" the screen
  }
    
    circle(x, y, d);    //display the ball cenetered at (x,y)
    
    x+=speedX;      //move the ball in the x direction
    y+=speedY;      //move the ball in the y direction
    
    // if ball hits right pad, invert X direction, increase horizontal speed of ball 
    if (Math.abs(width-x)<=30 && Math.abs(val2+100-y)<=110) {
      speedX = speedX * -1.2;
    } 
    
    //if the ball hits the left pad, invert X direction, increase horizontal speed of ball 
    else if (x<=30 && Math.abs(val1+100-y)<=110) {
      speedX = speedX * -1.2;
    }
    
    //if the ball hits the top or bottom boundaries, increase speed of ball and invert Y direction
    else if(y>height || y<0) speedY*=-1.1;
    
    //if the ball does not hit the left pad and goes beyond the left boudary, increase score of player 2
    else if (x<30) {
      ++counter2;
      
      if(counter2==5) {    //if player 2 has a score of 5, the they win
        textSize(150);     //change font
        text("PLAYER 2 WINS!!!", width/5.5, height/2);    //display winning message
        textSize(50);      //change font
        text("click mouse to play again",width/3,height/1.4);    //display play again message
        gameEnded=true;    //  game ended and there is a winner
      }
      else
        replay2();    //play the ball to player 2 in the next round at the beginning
      }
    
    //if the ball does not hit the right pad and goes beyond the right boudary, increase score of player 1
    else if (x>width-30) {
      ++counter1;
      
      if(counter1==5) {    //if player 1 has a score of 5, the they win
        textSize(150);     //change font
        text("PLAYER 1 WINS!!!", width/5.5, height/2);    //display winning message
        textSize(50);     //change font
        text("click mouse to play again",width/3,height/1.4);    //display play again message
        gameEnded=true;    //  game ended and there is a winner
    }
      else
        replay1();    //play the ball to player 1 in the next round at the beginning
    }
    
    rect(0,val1,30,200);          //draw player 1 and 2's pads/rectangles
    rect(width-30,val2,30,200);
    
    println(speedX + " " +speedY);
  }
  
}

void replay1() {          //when player 1 wins a round, this will be executed, and the ball will be played to player 1 first
  x=width/2;
  y=height/2;
  speedX = random(4,7);
  speedX*=-1;
  speedY = random(4,7); 
}

void replay2() {        //when player 2 wins a round, this will be executed, and the ball will be played to player 2 first
  x=width/2;
  y=height/2;
  speedX = random(4,7);
  speedY = random(4,7); 
}

void mousePressed() {      //mouse press to begin new game
  gameEnded=false;
  counter1=0;              //counters set to zero
  counter2=0;
  x=width/2;
  y=height/2;
  speedX = random(4,7);
  speedY = random(4,7);
}

  
  
