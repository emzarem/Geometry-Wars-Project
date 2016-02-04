/*Eric Murphy-Zaremba
 * Mr.Boss
 * ICS 4U1
 * Jan. 20 / 2015*/

import java.awt.geom.AffineTransform;
import java.awt.*;

/*Class for an enemy object, contains all the neccesary logic, methods and values for each enemy*/
public class Enemy{
  int x;
  int y;
  int speed;
  double rot; //rotational angle of the enemy
  int xbound; //bounds of grid
  int ybound; //bounds of grid
  int w;
  int h;
  double r; //radius of the enemy (for collisions)
  Shape shape; //the enemy's shape
  
  /*Constructor to initialise each enemy, accepts its x, y, angle, and bounds*/
  public Enemy(int gx, int gy, double grot, int gxbound, int gybound){
    x = gx;
    y = gy;
    rot = grot;
    xbound = gxbound;
    ybound = gybound;
    speed = 7;
    w = 18;
    h = 18;
    /*Since the enemy is a rectangle, a proper collision circle for them would have a diameter equivalent to a diagonal
     * line from corner to corner of the rectangle, therefore, the radius would be half of this*/
    r = (Math.sqrt(2*(w*w))) /2; //get said radius (using pythagorean theorem)
    shape = new Rectangle(x, y, w ,h);
  }
  
  /*Routine to update the enemy's position based on the given position of the player (pX, pY)*/
  public void update(int px, int py){
    double angle = Math.atan2(px - x, y - py); //angle from enemy to player
    //Rotate towards player
    if(angle > rot)
      rot += 0.2;
    else if(angle < rot)
      rot-= 0.2;
    //Move at the specified speed along the angle at which it is currently facing (using sine and cosine)
    x += speed* Math.sin(rot);
    y -= speed* Math.cos(rot);
    
    //Check if out of bounds: If out of bounds, reset location to edge of boundary
    if(x <= 0)
      x = 0;
    else if(x >= xbound - w)
      x = xbound - w;
    if(y <= 0)
      y = 0;
    else if(y >= ybound - h)
      y = ybound - h;
    
  }
  
  /*Routine to draw the enemy with a given color and graphics*/
  public void draw(Graphics2D g, Color c){
    //Create an affine transform to rotate the shape/enemy with the current angle and the center x and y of the shape
    AffineTransform at = AffineTransform.getRotateInstance(rot, x + (w/2), y + (h/2));
    //use that affineTransform to create a new transformed verson of the rectangle
    shape = at.createTransformedShape(new Rectangle(x, y, w ,h));
    g.setColor(c); // 'c' is unique to each tile
    g.draw(shape); //finally draw the transformed shape
  }
  
  /*Routine to fix collisions between the enemies using a given difference in location (using circle collisions)*/
  public void fixCollision(double xDiff, double yDiff){
    //move the enemy away by the difference with which they collide
    x -= (int)xDiff; 
    y -= (int)yDiff;
  }
  
  
  
}