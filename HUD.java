/*Eric Murphy-Zaremba
 * Mr.Boss
 * ICS 4U1
 * Jan. 20 / 2015*/

import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.*;

/*A HUD object class which deals with the 'heads up display' in the game: the diplay of points, time, and lives*/
public class HUD{
  int score;
  int lives;
  String level; //time
  int x;
  int y;
  int w;
  int h;
  int timeX; //time x coordinate (changes as it is aligned to right)
  BufferedImage img; //player image for lives
  
  /*Constructor to initialise the hud with th given score, lives, time, location, and size*/
  public HUD(int score, int lives, int level, int x, int y, int w, int h){
    this.score = score;
    this.lives = lives;
    
    //converts the 'tick' count to a seconds value using the delay of the timer which adds to it (17ms)
    this.level = String.format("%.2f", level*(0.017)); 
    
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.timeX = 0;
    
    //Get the lives image
    try{this.img = ImageIO.read(new File("claw.png"));}
    catch(IOException er){}  
  }
  
  /*Routine to update the values of the HUD*/
  public void update(int score, int lives, int level){
    this.score = score;
    this.lives = lives;
    this.level = String.format("%.2f", level*(0.017)); //again reformat into seconds
  }
  
  /*Routine to draw the HUD*/
  public void draw(Graphics2D g){
    Font temp = g.getFont(); //original font
    
    /*Draw the score*/
    Font font = new Font("Impact", Font.PLAIN, 36);
    g.setFont(font);
    g.setColor(Color.GREEN);
    g.drawString("Score: " + this.score, this.x + 10, this.y + 35);
    
    /*Draw the time*/
    String time = "Time: " + this.level;
    //Calculate the string bounds (as a rectangle)
    FontMetrics fm = g.getFontMetrics(font);
    java.awt.geom.Rectangle2D stringBounds = fm.getStringBounds(time, g);    
    //If the end of the time string is out of bounds (as it is ever-increasing), then adjust it by the overlap + 5
    if((this.w - this.timeX) + stringBounds.getWidth() >= this.w)
      this.timeX += (this.w - this.timeX) + stringBounds.getWidth() - this.w + 5;
    g.drawString(time, (int)(this.w - this.timeX), this.y + 35);
    
    /*Draw the lives*/
    //draw a ship for each live, with a calculated position relative to the center of the screen
    for(int i = -1; i < this.lives - 1; i++) 
      g.drawImage(img, (w/2) + (i*this.img.getWidth()) + 5, y + 10, null);
    
    g.setFont(temp); //reset font
    
  }
  
}