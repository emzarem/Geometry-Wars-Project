/*Eric Murphy-Zaremba
 * Mr.Boss
 * ICS 4U1
 * Jan. 20 / 2015*/

import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

/*Jpanel class which contains the entire gameplay itself (separate from the menu)*/
public class GamePane extends JPanel implements KeyListener, MouseListener, MouseMotionListener{
  BufferedImage bg; //background
  Player player;
  Grid gameGrid;
  int level; //number of 'ticks'/number of times updated (used to check time)
  boolean gameOver; 
  boolean pause;
  Timer frameRate; //timer to update panel
  HUD hud; //'heads up display'
  ActionListener a; //actionlistener to switch over to menu
  MenuButton menu; 
  
  /*Constructor to initialise the game panel with the given actionListener, width and height*/
  public GamePane(ActionListener a, int w, int h){
    setSize(w,h);
    setBackground(Color.BLACK);
    reset(); //initialise values
    this.a = a;
    addMouseListener(this);
    addMouseMotionListener(this);
    addKeyListener(this);
    this.pause = false;
//    (new Sound()).playTheme();
    //get background image
    try{ bg = ImageIO.read(new File("space.jpg")); }
    catch(IOException er){}
    
    //action listener to be fired by timer to update at a set speed
    ActionListener refresh = new ActionListener(){
      public void actionPerformed(ActionEvent e){
        update();
      }
    };    
    frameRate = new Timer(17, refresh); //update every 17 milliseconds (should average the equivalent of 60fps)
    
    addComponentListener(new ResizeListen());
    
    //create the button to return to menu 
    this.menu = new MenuButton(this.getWidth()/2, this.getHeight()/2 + 50, 200, 75, "Menu", Color.ORANGE); 
  }
  
  /*Draws all elements of jpanel*/
  public void paintComponent(Graphics g){
    super.paintComponent(g);
    g.drawImage(bg, 0,0, this);   
    //Call .draw() for the grid, hud, and player
    player.draw((Graphics2D)g);
    gameGrid.draw((Graphics2D)g);
    hud.draw((Graphics2D)g);
    
    //if paused or gameOver, call unique routines
    if(pause)
      pauseDraw(g);    
    else if(gameOver)
      gameOverDraw(g);
  }
  
  /*Routine to draw the gameOver state of the game*/
  public void gameOverDraw(Graphics g){
    //semi-transparent background over top the rest of the game
    g.setColor(new Color(0,0,0, (float)0.2)); 
    g.fillRect(0,0,this.getWidth(), this.getHeight());
    
    //Draw Game Over
    g.setColor(Color.RED);
    Font temp = g.getFont(); //original font
    Font font = new Font("Impact", Font.PLAIN, 72);
    //Determine the bounds of the text (in the form of a rectangle) and calculate its location accordingly
    FontMetrics fm = g.getFontMetrics(font);
    java.awt.geom.Rectangle2D stringBounds = fm.getStringBounds("Game Over", g);
    g.setFont(font);
    g.drawString("Game Over", (int)((this.getWidth()/2) - (stringBounds.getWidth()/2)), 
                 (int)((this.getHeight()/2) - (stringBounds.getHeight()/2)));
    
    //Draw Your final score and time
    Font font2 = new Font("Impact", Font.PLAIN, 36);
    //Determine the bounds of the text (in the form of a rectangle) and calculate its location accordingly
    FontMetrics fm2 = g.getFontMetrics(font2);
    java.awt.geom.Rectangle2D stringBounds2 = fm2.getStringBounds("Your Score: " + String.valueOf(hud.score) + 
                                                                  " in " + String.valueOf(hud.level) + " seconds", g);
    g.setFont(font2);
    g.setColor(Color.ORANGE);
    g.drawString("Your Score: " + String.valueOf(hud.score) + " in " + String.valueOf(hud.level) + " seconds", 
                 (int)((this.getWidth()/2) - (stringBounds2.getWidth()/2)),
                 (int)((this.getHeight()/2) - (stringBounds2.getHeight()/2) - 150));
    
    //Draw a prompt to restart the game
    Font font3 = new Font("Impact", Font.PLAIN, 18);
    //Determine the bounds of the text (in the form of a rectangle) and calculate its location accordingly
    FontMetrics fm3 = g.getFontMetrics(font3);
    java.awt.geom.Rectangle2D stringBounds3 = fm3.getStringBounds("Press SPACE to Restart", g);
    g.setFont(font3);
    g.drawString("Press SPACE to Restart", (int)((this.getWidth()/2) - (stringBounds3.getWidth()/2)),  
                 (int)((this.getHeight()/2) - (stringBounds3.getHeight()/2) + 130));
    
    g.setFont(temp); //reset the font
    this.menu.draw((Graphics2D)g); //draw the menu button
  }
  
  /*Routine to draw the paused state of the game*/
  public void pauseDraw(Graphics g){
    //semi-transparent background over top the rest of the game
    g.setColor(new Color(0,0,0, (float)0.4));
    g.fillRect(0,0,this.getWidth(), this.getHeight());
    g.setColor(Color.WHITE);
    Font temp = g.getFont(); //original font
    Font font = new Font("Impact", Font.PLAIN, 72);
    //Determine the bounds of the text (in the form of a rectangle) and calculate its location accordingly
    FontMetrics fm = g.getFontMetrics(font);
    java.awt.geom.Rectangle2D stringBounds = fm.getStringBounds("PAUSE", g);
    g.setFont(font);
    g.drawString("PAUSE", (int)((this.getWidth()/2) - (stringBounds.getWidth()/2)), 
                 (int)((this.getHeight()/2) - (stringBounds.getHeight()/2)));
    g.setFont(temp); //reset font
    this.menu.draw((Graphics2D)g); //draw menu button
  }
  
  /*Key Listener routines*/
  /*When a movement key (UP, DOWN, LEFT, RIGHT) is pressed, its respective boolean (in the player object) is made true
   * When it is released, it is made false.  The escape key pauses the game, the spacebar restarts it at the end*/
  public void keyTyped(KeyEvent e){}
  public void keyReleased(KeyEvent e){
    int key = e.getKeyCode();
    if(key == KeyEvent.VK_KP_LEFT || key == KeyEvent.VK_LEFT)
      player.left = false;
    if(key == KeyEvent.VK_KP_RIGHT || key == KeyEvent.VK_RIGHT)
      player.right = false;
    if(key == KeyEvent.VK_KP_DOWN || key == KeyEvent.VK_DOWN)
      player.down = false;
    if(key == KeyEvent.VK_KP_UP || key == KeyEvent.VK_UP)
      player.up = false;    
  }
  
  public void keyPressed(KeyEvent e){ 
    int key = e.getKeyCode();
    if(key == KeyEvent.VK_KP_LEFT || key == KeyEvent.VK_LEFT)
      player.left = true;
    if(key == KeyEvent.VK_KP_RIGHT || key == KeyEvent.VK_RIGHT)
      player.right = true;
    if(key == KeyEvent.VK_KP_DOWN || key == KeyEvent.VK_DOWN)
      player.down = true;
    if(key == KeyEvent.VK_KP_UP || key == KeyEvent.VK_UP)
      player.up = true;
    if(key == KeyEvent.VK_SPACE && gameOver) //restart
      reset();
    if(key == KeyEvent.VK_ESCAPE && !gameOver){ //pause
      if(pause)
        pause = false;
      else
        pause = true;
    }
  }
  
  /*Routine to initialise all values (reset the game)*/
  public void reset(){
    player = new Player(this.getWidth() / 2, this.getHeight() / 2, this.getWidth(), this.getHeight());
    gameGrid = new Grid(getWidth(),getHeight(),50,50);
    gameOver = false;
    player.dead = false;
    level = 0;
    //new HUD(int score, int lives, int level, int x, int y, int w, int h)
    hud = new HUD(player.score, player.lives, level, 0,0, getWidth(), getHeight());
  }
  
  /*Routine to update all elements of the game*/
  public void update(){     
      
    if(pause){ //do not update the game elements if paused
      repaint();
      return;
    }
    player.update(); 
    int enemies = gameGrid.getEnemyCount(); //number of enemies  
    gameGrid.update(player.x, player.y);
    
    //player score is calculated by determining the difference in the number of enemies before and after updating
    player.score += (enemies - gameGrid.getEnemyCount())*10; 
    
    //Check all elements of the grid
    gameGrid.checkCollisions();
    gameGrid.checkGates(player.x, player.y, player.w, player.h);
    gameGrid.checkExplosions();
    //Check if the player has died (if they are not already dead nor invincible)
    if(!player.dead && !player.invincible && gameGrid.checkPlayer(player.x, player.y))
      death(); //if so then run the death() routine
    
    level++; //add to the number of ticks
    
    //if the player is still alive, update the HUD values
    if(!player.dead) 
      hud.update(player.score, player.lives, level);
    
    int gates = gameGrid.getGateCount(); //amount of gates
    
    /*Spawn enemies in gates at intervals of 200 and 210 'ticks' respectively (enemies limited to 4000, gates 
     * limited to 10)*/
    if(level%200 == 0 && enemies < 4000)
      gameGrid.spawnEnemies(level/50, player.x, player.y);
    if(level%210 == 0 && gates < 10)
      gameGrid.spawnGate();
    repaint();
  }
  
  private class ResizeListen implements ComponentListener{
    public void componentResized(ComponentEvent e){
      player.xbound = getWidth();
      player.ybound = getHeight();
      System.out.println(getWidth() + ", " + getHeight());
    }
    public void componentHidden(ComponentEvent e){}
    public void componentMoved(ComponentEvent e){}
    public void componentShown(ComponentEvent e){}
  }
  
  /*Routine to do neccesary actions when the player dies*/
  public void death(){
    player.dead = true;
    ActionListener gameListen = new ActionListener(){ //action listener to fire after a delay following death
      public void actionPerformed(ActionEvent e){
        if(player.lives > 0){ //if there are more lives...
          player.lives--; //reduce the lives
          player.rot = 0; //reset the player's movement
          player.speed = 0;
          player.dead = false;
          player.invincible(); //give the temporary shield
        }
        else
          gameOver = true;
        ((Timer)e.getSource()).stop();
      }
    };
    Timer gameWait = new Timer(1000, gameListen); //delay after dieing before restarting or having game over
    gameWait.start();
  }
  
  /*MouseListener and MouseMotionListener routines*/
  public void mouseMoved(MouseEvent e){ 
    //Highlights the buttons as passed over by mouse
    if(menu.contains(e.getX(), e.getY()))
      menu.c = Color.WHITE;
    else
      menu.c = Color.ORANGE;
  }
  public void mouseDragged(MouseEvent e){}
  public void mousePressed(MouseEvent e){
    if(!gameOver && !pause)
      return;
    /*If the game has ended or paused, check if the menu button has been pressed, in which case fire the respective 
     * listener*/
    int x = e.getX();
    int y = e.getY();
    if(menu.contains(x,y)){
      if(pause)
        pause = false;
      a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
    }
  }
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e){}
  public void mouseReleased(MouseEvent e){}
  public void mouseClicked(MouseEvent e){}
}
