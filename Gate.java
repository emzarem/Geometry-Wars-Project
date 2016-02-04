/*Eric Murphy-Zaremba
 * Mr.Boss
 * ICS 4U1
 * Jan. 20 / 2015*/

import java.awt.*;
import java.awt.geom.*;

/*This class is a Gate object which holds all the parameters and methods neccesary for each gate that is created 
 * within the game world*/
public class Gate{
  //Two ends of gate (p1, p2)
  Point p1; 
  Point p2;
  //Center x and y of gate
  int centerX;
  int centerY;
  //Movement speed in each axis
  int xSpeed;
  int ySpeed;
  //Rotational values
  double angle;
  double rotationSpeed;
  
  double length;
  //The grid which contains it
  Grid grid;
  
  /*Constructor which accepts its center coordinates and its containing grid, intialises all values of the gate*/
  public Gate(int centerX, int centerY, Grid grid){
    this.angle = Math.random()*6.28; //create a random initial angle
    this.rotationSpeed = 0.05;
    this.length = 150;
    this.centerX = centerX;
    this.centerY = centerY;
    this.xSpeed = 1;
    this.ySpeed = 1;
    this.grid = grid;
    
    /*Determine the x and y of each end point based on the length and angle (using cosine and sine to calculate its
     * position relative to the center*/
    int x1 = centerX + (int)((length / 2)*(Math.cos(angle)));
    int y1 = centerY + (int)((length / 2)*(Math.sin(angle)));
    int x2 = centerX - (int)((length / 2)*(Math.cos(angle)));
    int y2 = centerY - (int)((length / 2)*(Math.sin(angle)));
    
    //Give the end points those x and y coordinates
    this.p1 = new Point(x1, y1);
    this.p2 = new Point(x2, y2);
  }
  
  /*Routine to draw the gate, accepting the graphics from the panel it is on*/
  public void draw(Graphics2D g){
      g.setColor(Color.WHITE);
      g.setStroke(new BasicStroke(1));
      g.draw(new Line2D.Float(p1, p2));
  }
  
  /*Routine to update the position and rotation of the gate*/
  public void update(){
    //Move and rotate the gate at the specified speeds
    this.angle += this.rotationSpeed; 
    this.centerX += this.xSpeed;
    this.centerY += this.ySpeed;
    
    //Calculate the new endpoints based on the updated values
    p1.x = centerX + (int)((length / 2)*(Math.cos(angle)));
    p1.y = centerY + (int)((length / 2)*(Math.sin(angle)));
    p2.x = centerX - (int)((length / 2)*(Math.cos(angle)));
    p2.y = centerY - (int)((length / 2)*(Math.sin(angle)));  
    
    //Check if either end is out of bounds
    checkOutOfBounds(p1);
    checkOutOfBounds(p2); 
  }
  
  /*Routine to check if either end point is out of the bounds of the grid, accepts a point*/
  public void checkOutOfBounds(Point p){
    /*If the point is out of bounds, the rotation will be reversed (*(-1)) and the speed in the axis in which
     * it is out of bounds is also reversed (*(-1))*/
    //If out of bounds on the left
    if(p.x <= 0){
      p.x = 0;
      this.rotationSpeed = this.rotationSpeed * (-1); 
      this.xSpeed = this.xSpeed * (-1); 
    }
    //If out of bounds on the right
    else if(p.x >= this.grid.gridWidth){
      p.x = this.grid.gridWidth; 
      this.rotationSpeed = this.rotationSpeed * (-1); 
     this.xSpeed = this.xSpeed * (-1); 
    }
    //If out of bounds on the top
    if(p.y <= 0){
      p.y = 0;
      this.rotationSpeed = this.rotationSpeed * (-1);
      this.ySpeed = this.ySpeed * (-1);
    }
    //If out of bounds on the bottom
    else if(p.y >= grid.gridHeight){
      p.y = grid.gridHeight;
      this.rotationSpeed = this.rotationSpeed * (-1);
      this.ySpeed = this.ySpeed * (-1);
    }
  }

  /*Routine to check if the player has triggered the gate, accepts the player x,y,width,height*/
  public boolean playerThrough(int px, int py, int pw, int ph){
    Line2D.Double line = new Line2D.Double(p1,p2); //the gate line
     //check if line intersects the player's rectangle of space
    if(line.intersects(px, py, pw, ph)){
      return true; //gate triggered
    }
    return false; //gate not triggered
  }  
  
}