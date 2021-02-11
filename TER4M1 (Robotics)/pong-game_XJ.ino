// consists of 2 potentiometers and requires 2 players, who use the POTS to guide a rectangle vertically in a game of PONG

const int potPin1 = A4;         //pin for potentiometer of player 1
const int potPin2 = A5;         //pin for potentiometer of player 1

void setup() {
  Serial.begin(9600);            //for debugging purposes, to use the serial monitor 

  pinMode(potPin1,INPUT);        //to analogRead the value of the potentiometer for player 1
  pinMode(potPin2,INPUT);        //to analogRead the value of the potentiometer for player 2
}

void loop() {
  
  int val1=analogRead(potPin1);       //reading value from Player 1's POT
  int val2=analogRead(potPin2);       //reading value from Player 2's POT
  Serial.print(val1, DEC);            //print decimal form of the val of player 1's POT
  Serial.print(",");                  //seperated by comma
  Serial.print(val2, DEC);            //print decimal form of the val of player 2's POT
  Serial.println();                   //print new line
  delay(20);                          //delay 20 milliseconds
}
