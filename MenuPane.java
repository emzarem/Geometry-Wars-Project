/*Eric Murphy-Zaremba
 * Mr.Boss
 * ICS 4U1
 * Jan. 20 / 2015*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.*;

/*JPanel class which makes up the main menu of the game*/
public class MenuPane extends JPanel implements MouseListener, MouseMotionListener{
  MenuButton play;
  MenuButton help;
  MenuButton quit;
  MenuButton back;
  boolean helpClicked; //if help button is clicked
  ActionListener a; //action listener to transition into play mode
  BufferedImage title; //title image
  BufferedImage bg; //background image
  BufferedImage instructions; //instructions image
  Grid grid;
  Timer refresh; //timer to update
  int mX; //mouse coordinates (to control the enemy movement)
  int mY;
  
  /*Constructor to initialise the menu with given heigh and width (and actionListener)*/
  public MenuPane(int width, int height, ActionListener a){
    this.setSize(width, height);  
    this.setBackground(Color.BLACK);
    this.a = a;
    //get images
    try{ 
      this.title = ImageIO.read(new File("geoTitle.png"));
      this.bg = ImageIO.read(new File("space.jpg"));
      this.instructions = ImageIO.read(new File("Instructions.png"));
    }
    catch(IOException er){}
    //Create the buttons
    //MenuButton(int centerX, int centerY, int width, int height, String message, Color c)
    this.play = new MenuButton(width/2, (height/2) - 80, 200, 70, "Play", Color.GREEN);
    this.help = new MenuButton(width/2, (height/2) +50, 200, 70, "Help", Color.GREEN);
    this.back = new MenuButton((width/2), (height/2) + 250, 175, 70, "Back", Color.CYAN);
    this.quit = new MenuButton(width/2, (height/2) + 180, 200, 70, "Quit", Color.GREEN);
    this.addMouseListener(this);
    this.addMouseMotionListener(this);
    this.grid = new Grid(width, height, 50,50);
    //Listener to update the menu
    ActionListener al = new ActionListener(){
      public void actionPerformed(ActionEvent e){
        update();
      }
    };
    this.refresh = new Timer(17, al); //timer to update with a delay of 17ms (approx 60fps)
    this.refresh.start();
    this.mX = 100;
    this.mY = 100;
    this.grid.spawnEnemies(50, mX,mY);
  }
  
  /*Draws all elements of the menu*/
  public void paintComponent(Graphics g){
    super.paintComponent(g);   
    g.drawImage(this.bg, 0,0,null); //draw background
    
    //draw help section
    if(helpClicked){
      g.drawImage(this.instructions, 0,0,null);
      this.back.draw(g);
      return;
    }
    //draw grid, title, and buttons
    this.grid.draw((Graphics2D)g);
    g.drawImage(this.title, (this.getWidth() / 2) -((this.title.getWidth() / 2)) + 30,
                (this.getHeight() / 2) - 300, null); 
    this.play.draw(g);
    this.help.draw(g);
    this.quit.draw(g);
  }
  
  /*Routine to update the elements of the menu*/
  public void update(){
    grid.update(mX, mY); //enemies chase the mouse coordinates
    grid.checkCollisions();
    repaint();
  }
  
  /*MouseListener and MouseMotionListener routines*/
  public void mousePressed(MouseEvent e){
    //Check which buttons (if any) contains the mouse and act appropriately
    int x = e.getX();
    int y = e.getY();
    if(helpClicked && this.back.contains(x,y))
      helpClicked = false; //go back to main menu
    else if(this.play.contains(x,y))
      play(); //switch to play
    else if(this.help.contains(x,y))
      this.helpClicked = true; //go to help screen
    else if(this.quit.contains(x,y))
      System.exit(0);  //exit program
  }
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e){}
  public void mouseReleased(MouseEvent e){}
  public void mouseClicked(MouseEvent e){}
  
  public void mouseMoved(MouseEvent e){
    //Highlights the buttons as they are hovered over and changes the values of mX and mY which the enemies chase
    this.mX = e.getX();
    this.mY = e.getY();  
    //Standard colors
    this.play.c = Color.GREEN;
    this.help.c = Color.GREEN;
    this.quit.c = Color.GREEN;
    this.back.c = Color.CYAN;
    
    //if hovered over, switch to these new colors
    if(this.helpClicked && this.back.contains(mX, mY))
      this.back.c = Color.WHITE;
    else if(this.play.contains(mX, mY))
      this.play.c = Color.RED;
    else if(this.help.contains(mX,mY))
      this.help.c = Color.RED;
    else if(this.quit.contains(mX,mY))
      this.quit.c = Color.RED;
  }
  public void mouseDragged(MouseEvent e){}
  
  public void play(){
    //fire the action listener to switch to play mode
    a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
  }
  
}