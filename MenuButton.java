/*Eric Murphy-Zaremba
 * Mr.Boss
 * ICS 4U1
 * Jan. 20 / 2015*/

import java.awt.*;

/*MenuButton Object class which creates a rectangular button of specified size, text, and color and can check if a
 * click is within it (.contains())*/
public class MenuButton{
  Rectangle rec;
  String message;
  Color c;
  int centerX;
  int centerY;
  
  /*constructor to initialise the button with the given values*/
  public MenuButton(int centerX, int centerY, int width, int height, String message, Color c){
    int x = centerX - (width/2);
    int y = centerY - (height/2);
    this.centerX = centerX;
    this.centerY = centerY;
    this.rec = new Rectangle(x, y, width, height); //create a rectangle with specified values
    this.message = message;
    this.c = c;
  }
  
  /*Routine to draw the button*/
  public void draw(Graphics g){
    g.setColor(c);
    //Draw the containing rectangle
    g.drawRect((int)rec.getX(), (int)rec.getY(), (int)rec.getWidth(), (int)rec.getHeight());
    
    Font font = new Font("Impact", Font.PLAIN, 36);
    g.setFont(font);
    //Get the string's bounds in the form of a rectangle
    FontMetrics fm = g.getFontMetrics(font);
    java.awt.geom.Rectangle2D stringBounds = fm.getStringBounds(message, g);
    //Use those bounds to determine its x and y locations to be relative to its given center coordinates
    int stringX = this.centerX - ((int)stringBounds.getWidth() / 2);
    int stringY = this.centerY ;//- ((int)stringBounds.getHeight() / 2);
    g.drawString(message, stringX, stringY);
  }
  
  /*Routine to check if the button contains a given set of coordinates (used to check mouse clicks)*/
  public boolean contains(int x, int y){
    if(rec.contains(x,y))
      return true;
    return false;
  }
}