/*Eric Murphy-Zaremba
 * Mr.Boss
 * ICS 4U1
 * Jan. 20 / 2015*/

import java.awt.*;
import java.awt.geom.*;

/*Explosion object class which holds the methods, values and logic needed for each explosion*/
public class Explosion{
  int xCenter;
  int yCenter;
  int r; //radius of explosion
  boolean isDead; //if the explosion is over
  Grid grid; //containing grid
//  Sound sound;
  
  /*Constructor to initialise the explosion with the given center x and y and the containing grid*/
  public Explosion(int xCenter, int yCenter, Grid grid){
    this.xCenter = xCenter;
    this.yCenter = yCenter;
    this.r = 1; //initial radius
    this.isDead = false;
    this.grid = grid;
//    this.sound = new Sound();
//    this.sound.explode();
  }
  
  /*Routine to update the values of the explosion*/
  public void update(){
    this.r += 15; //increase the radius each time
    if(this.r > 200) //if the radius is greater than said size, it is 'dead'
      this.isDead = true;
    checkEnemies(); //check if it has killed any enemies
  }
  
  /*Routine to draw the explosion using given graphics*/
  public void draw(Graphics2D g){
    g.setColor(Color.WHITE);
    g.setStroke(new BasicStroke(5));
    //draws a circle of specified diameter and location
    g.draw(new Ellipse2D.Double(xCenter - r, yCenter - r, 2*r, 2*r)); 
  }
  
  /*Routine to check if enemies have been blown up*/
  public void checkEnemies(){
    //Get which tile the coordinates (upper left corner) of the explosion is on
    int x1 =(this.xCenter - r)/(this.grid.tileWidth);
    int y1 = (this.yCenter - r)/(this.grid.tileHeight);
    //Get which tile the opposite corner of the explosion is on
    int endX = (this.xCenter + (2*r))/(this.grid.tileWidth);
    int endY = (this.yCenter + (2*r))/(this.grid.tileHeight);
    
    //if beyond the range of a tile...
    if(x1 < 0)
      x1 = 0;
    if(y1 < 0)
      y1 = 0;
    if(endX >= this.grid.xCount)
      endX = this.grid.xCount - 1;
    if(endY >= this.grid.yCount)
      endY = this.grid.yCount - 1;
    
    //Loop through the tiles which are affected by the explosions and check if enemies are killed
    for(int x = x1; x <= endX; x++){
      for(int y = y1; y <= endY; y++){
        this.grid.tiles[x][y].checkExplosion(this);
      }
    }
  }
  
  
  
}