/*Eric Murphy-Zaremba
 * Mr.Boss
 * ICS 4U1
 * Jan. 20 / 2015*/

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

/*Player object class which holds all logic, routines, and values to make a player for the game*/
public class Player{
  int x;
  int y;
  int speed;
  int maxSpeed;
  double rot; //angle
  int xbound; //bounds 
  int ybound; //bounds
  BufferedImage img; //player image
  //Key Booleans
  boolean up; 
  boolean down;
  boolean left;
  boolean right;
  int w;
  int h;
  int lives;
  int score;
  boolean dead;
  boolean invincible;
  Color shieldColor;
  
  /*Constructor to initialise the player, given location and bounds*/
  public Player(int gx, int gy, int gxbound, int gybound){
    //get image
    try{
      img = ImageIO.read(new File("claw.png"));
    }
    catch(IOException er){}  
    this.x = gx;
    this.y = gy;
    this.xbound = gxbound;
    this.ybound = gybound;
    this.speed = 0;
    this.rot = 0;
    this.up = false;
    this.down = false;
    this.left = false;
    this.right = false;
    this.maxSpeed = 17; //maximum speed which the player can move at
    this.w = img.getWidth();
    this.h = img.getHeight();
    this.lives = 3;
    this.score = 0;
  }
  
  /*Routine to update the player values*/
  public void update(){
    if(dead) //do not update if dead
      return;
    
    //decreases speed (up to zero) when not pressing speed related keys
    if(!up && !down){ 
      if(speed > 0)
        speed--;
      else if(speed < 0)
        speed++;
    }
    
    //check which key(s) are pressed
    if(left) //rotate left
      rot -= 0.18;
    if(right) //rotate right
      rot += 0.18;
    if(up && !(speed >= maxSpeed)) //increase forward speed if not at max    
      speed++;
    if(down && !(speed <= -maxSpeed)) //increase backward speed if not at max
      speed--;
    
    //change coordinates based on current angle
    x += speed* Math.sin(rot);
    y -= speed* Math.cos(rot);
    
    //check if out of bounds and if so , reset to edge
    if(x <= 0)
      x = 0;
    else if(x >= xbound - img.getWidth())
      x = xbound - img.getWidth();
    if(y <= 0)
      y = 0;
    else if(y >= ybound - img.getHeight())
      y = ybound - img.getHeight();      
    
  }
  
  /*Routine to draw the player*/
  public void draw(Graphics2D g2){
    if(dead) //do not draw if dead
      return;
    if(invincible){ //draw the shield
      Color c = g2.getColor();
      Stroke s = g2.getStroke();
      g2.setColor(shieldColor);
      g2.setStroke(new BasicStroke(3));
      g2.draw(new Ellipse2D.Double(x - 15, y - 15, w + 30, h + 30));
      g2.setColor(c);
      g2.setStroke(s);
    }
    //transform image based on rotation and center point and draw it
    AffineTransform at = AffineTransform.getRotateInstance(rot, img.getWidth()/2, img.getHeight()/2);
    AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
    g2.drawImage(op.filter(img,null), x, y, null); //use AffineTransformOp.filter() to adjust the image
  }
  
  /*Routine to run when the player is invincible (immediately after death)*/
  public void invincible(){
    this.invincible = true;    
    shieldColor = Color.WHITE;
    ActionListener aL = new ActionListener(){
      int i = 0;
      public void actionPerformed(ActionEvent e){
        i++;
        if(i == 3){ //after 3 seconds      
        invincible = false;
        ((javax.swing.Timer)e.getSource()).stop();
        }
        else if(i == 2) //after 2 seconds
          shieldColor = Color.RED; //warn that the shield is about to expire
      }
    };
    javax.swing.Timer invincibleTime = new javax.swing.Timer(1000, aL); //timer for life of shield
    invincibleTime.start();
  }
  
}
