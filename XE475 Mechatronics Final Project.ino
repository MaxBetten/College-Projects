
#include <Servo.h>
Servo motor; 
Servo steer;

#define irleftSensorPin 7             
int centersensor = 0;
#define irrightSensorPin 6 // our sensor pins 
int leftIR; 
int rightIR; 
int centerIR; //values to store our sensor data
int distance = 0; 
int preverror = 0;
float velocity;
float steering = 91;
int counter = 0;
int error = 0; 
float mySteers [10] = {90,90,90,90,90,90,90,90,90,90}; //delays the steering to help avoid immediately turning when lead vehicle turns
int steerpos = 9; 
int speedup; 



void setup(){
  Serial.begin(9600); 
motor.attach(5);  
steer.attach(4);
}
void loop(){
  leftIR = digitalRead(irleftSensorPin);
 rightIR = digitalRead(irrightSensorPin); //read if either IR sensor detects the cone of LEDs
  distance = analogRead(centersensor); //read how far away an object is 
  error = 200-distance; //calculate how far away it is from our preferred following distance
  speedup = preverror+error; //calculate the deviation 
  if(error <205&& error>195) //if within the goal distance, there is no deviation 
  {speedup=0;}
 velocity = 90 - (0.12*(error))-.08*speedup; //speed control equation 
 //Serial.println(velocity); 
if(error-preverror > 150||error-preverror<-150) //our fail safe if the vehicle suddenly detects something way too close or far away from what it was tracking
{
  velocity = 92;
  while(1); 
}
 motor.write(velocity);
  if (leftIR==0&&rightIR==0)//both IR sensors detect the cone of LEDs, go straight 
  {
    steering = 92;
  }
  else if (leftIR==0&&rightIR==1) //if only left IR sensor detects, the vehicle must begin turning to the right 
  {
    steering = steering + 1;
     if(steering>180)
    {
      steering = 180;
    }
   
  }
  else if (leftIR==1&&rightIR==0) //if only right IR sensor detects, the vehicle begins turning to the left 
  {
    steering = steering - 1;
    if(steering<0)
    {
      steering = 0;
    } 
  }
   Serial.println(steering); 
  steer.write(mySteers[counter]); 
   mySteers [steerpos] = steering; 
   counter = counter + 1;
   steerpos = steerpos + 1; 
   if(steerpos >9)
   {steerpos = 0;}
   if(counter>9)
   {counter = 0;} // an alogirthm to store steering values in an array and help delay steering so it does not immediately turn when the lead vehicle turns
  preverror = error; //store the error in distance 
}



