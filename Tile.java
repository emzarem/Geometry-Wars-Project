/*Eric Murphy-Zaremba
 * Mr.Boss
 * ICS 4U1
 * Jan. 20 / 2015*/

import java.awt.*;
import java.util.*;
import java.awt.event.*;

/*Tile object class which consists of a tile which has methods to draw, update, and check parameters of the enemies
 * contained within it, used to isolate sections of the screen for efficiency*/
public class Tile{
  
  ArrayList<Enemy> enemies; //all enemies on tile
  int x;
  int y;
  
  int tileWidth; //right side of tile
  int tileHeight; //bottom of tile
  
  //center coordinates of tile (where things will spawn from)
  int xSpawn; 
  int ySpawn;
  
  Color color;
  
  Grid grid;
  
  boolean spawnWarn;
  
  /*Constructor to initialise each tile*/
  public Tile(Grid grid, int x, int y){
    this.grid = grid;
    this.x = x;
    this.y = y;
    
    this.spawnWarn = false; ///SPAWN WARNING STUFF///
    
    //get the right and bottom edge values of tile
    this.tileWidth = x + grid.tileWidth;
    this.tileHeight = y + grid.tileHeight;
    
    //calculate the center of the tile
    xSpawn = x + (grid.tileWidth / 2);
    ySpawn = y + (grid.tileHeight / 2);
    
    enemies = new ArrayList<Enemy>();
    ///create a random color unique to the tile
    float r = (float)(Math.random() * 1.0);
    float g = (float)(Math.random() * 1.0);
    float b = (float)(Math.random() * 1.0);
    Color randomColor = new Color(r, g, b); 
    color = randomColor;
  }
  
  /*Routine to update the elements of the tile, accepts the player x and y*/
  public void update(int px, int py){
    ArrayList<Enemy> toMove = new ArrayList<Enemy>(); //arrayList of enemies which need to move to a new tile
    //iterate through enemies and update them
    for(Enemy e: this.enemies){
      e.update(px, py);
      if(outOfBounds(e)) //if the enemy is out of the tile's bounds...
        toMove.add(e); //add to the move list
    }
    //Iterate though the enemies to move and call move for them
    for(Enemy e: toMove)
      this.move(e);
  }
  
  /*routine to check if the player is contacting a shape(is killed), accepts the player x and y*/
  public boolean checkPlayer(int px, int py){
    Rectangle player = new Rectangle(px, py, 36,36); //to check
    //iterate through enemies and check if they contain or intersect the player's rectangle
    for(Enemy en: this.enemies){
      if(en.shape.contains(player) || en.shape.intersects(player))
        return true; //player killed
    }
    return false;//player alive
  }
  
  /*routine to draw elements of the tile*/
  public void draw(Graphics2D g){
    //iterate through enemies and call .draw()
    for(Enemy e: this.enemies)
      e.draw(g, color);
    
    ///SPAWN WARNING STUFF///
    //Draw a semi transparent square to warn of a coming spawn point
    if(spawnWarn){ 
      Color c = g.getColor();
      g.setColor(new Color(1,0,0, (float)0.2)); //semi transparent red
      g.fillRect(this.x, this.y, 50, 50);
      g.setColor(c);
    }
  }
  
  /*Routine to spawn enemies of a given amount*/
  public void spawnEnemies(int amt){
    //add the amount of enemies (at center of tile and random angles) specified to the enemy arrayList
    for(int i = 0; i < amt; i++)
          enemies.add(new Enemy(xSpawn, ySpawn, Math.random()*6.28, this.grid.gridWidth, this.grid.gridHeight));

    this.spawnWarn = false; //after spawning, the spawn warning is over
  }
  
  /*Routine to warn of a future spawn point of the given amount (amt)*/
  public void spawnWarn(final int amt){
    this.spawnWarn = true;   
    //action listener to spawn enemies after a delay
    ActionListener actionListener = new ActionListener(){
      public void actionPerformed(ActionEvent e){
        spawnEnemies(amt);
        ((javax.swing.Timer)e.getSource()).stop();
      }
    };
    javax.swing.Timer time = new javax.swing.Timer(1000, actionListener); //timer to spawn enemies after 1s
    time.start();
  }
  
  /*Routine to return number of enemies contained by the tile*/
  public int getEnemyCount(){
    return this.enemies.size();
  }
  
  /*Routine to check if the given enemy is out of bounds*/
  public boolean outOfBounds(Enemy en){
    if(en.x > this.tileWidth)
      return true;
    else if(en.x < this.x)
      return true;
    else if(en.y > this.tileHeight)
      return true;
    else if(en.y  < this.y)
      return true;
    return false;    
  }
  
  /*Routine to add an enemy to the tile (used in conjunction with the move function in the grid)*/
  public void addEnemy(Enemy en){
    this.enemies.add(en); //add the given enemy to the list
  }
  
  /*Routine to move a given enemy into a new tile*/
  public void move(Enemy en){
    this.enemies.remove(en); //get rid of the enemy from this tile
    this.grid.move(en); //move it to a new one
  }
  
  /*Routine to check the collisions between enemies within the tile*/
  public void checkCollisions(){
    //check which tile it is on
    int gX = x/grid.tileWidth;
    int gY = y/grid.tileHeight;
    
    //Loop through the arrayList of enemies and check each one against the others
    for(int i = 0; i < enemies.size(); i++){
      Enemy e1 = enemies.get(i);
      for(int g = i + 1; g < enemies.size(); g++){
        Enemy e2 = enemies.get(g);
        collision(e1, e2); //check if the two collide
      }
    }
    
    //Also check the collisions within the surrounding tiles
    for(int tileX = gX - 1; tileX <= gX + 1; tileX++){
      for(int tileY = gY - 1; tileY <= gY + 1; tileY++){
        checkTile(tileX, tileY);
      }
    }
    
  }
  
  /*Routine to check if two given enemies are colliding*/
  public void collision(Enemy e1, Enemy e2){
    /*Implements circle collision: creates a 'circle' around each enemy and checks if the difference between the 
     * centers of them is greater than (not colliding) or less than (colliding) the sum of their radii*/
    double xDiff = (e1.x + e1.r ) - (e2.x + e2.r); //difference in x b/w centers
    double yDiff = (e1.y + e1.r) - (e2.y + e2.r); //difference in y b/w centers
    double diff = (int)Math.sqrt(xDiff*xDiff + yDiff*yDiff); /*difference between centers (calculated using 
                                                              * pythagorean theorem)*/
    
    if(diff < (e1.r + e2.r)){ //if the difference is less than the sum of their radii...
      //There is a collision
      double nX; //overlap in x direction
      double nY; //overlap in y direction
      
      //e1 is on left
      if(e1.x < e2.x) 
        nX = e1.x + e1.w - e2.x; //overlap = right side of e1 - left side of e2
      //e2 is on left
      else 
        nX = e1.x - e2.x - e2.w; //overlap = right side of e2 - left side of e1
      //e1 is below e2
      if(e1.y > e2.y)
        nY = e2.y + e2.w - e1.y; //overlap = bottom of e2 - top of e1
      //e1 is above e2
      else 
        nY = e1.y + e1.w - e2.y; //overlap = bottom of e1 - top of e2
      //Fix the collision by half for each enemy
      e1.fixCollision(nX/2, nY/2);
      e2.fixCollision(-(nX/2), -(nY/2));
    }
    return;
  }
  
  /*Routine to check the collisions of enemies in the surrounding tiles*/
  public void checkTile(int tileX, int tileY){
    //check if the tile is out of bounds
    if(tileX > grid.xCount - 1 || tileX < 0) 
      return;
    if(tileY > grid.yCount - 1 || tileY < 0)
      return;
    
    /*iterate through the enemies and through the enmies in the tile being checked against and check them all 
     * against each other*/    
    for(Enemy e1: this.enemies){
      for(Enemy en: grid.tiles[tileX][tileY].enemies){
        collision(e1, en);
      }
    }
  }
  
  /*Routine to check if any enemies have been exploded by a given explosion*/
  public void checkExplosion(Explosion ex){
    ArrayList<Enemy> toMove = new ArrayList<Enemy>(); //the enemies to remove
    //iterate through the enemies and check if they are exploded
    for(Enemy en: this.enemies){
      if(exploded(en, ex)){
        toMove.add(en); //if they are exploded, que them to be removed
      }
    }
    //iterate through enemies to remove and remove them from the tile's list
    for(Enemy en: toMove)
      this.enemies.remove(en);                             
  }
  
  /*Routine to check if the given enemy has been exploded by the given explosion*/
  public boolean exploded(Enemy en, Explosion ex){
    //Again using circle collision *see .collision()
    double xDiff = (en.x + en.r ) - (ex.xCenter); //x difference
    double yDiff = (en.y + en.r) - (ex.yCenter); //y difference
    double diff = (int)Math.sqrt(xDiff*xDiff + yDiff*yDiff); //distance between centers    
    
    if(diff < (en.r + ex.r)) //if distance is less than sum of radii..
      return true; //there is a collision and so it is exploded (true)
    return false;
  }
}