/*Eric Murphy-Zaremba
 * Mr.Boss
 * ICS 4U1
 * Jan. 20 / 2015*/

import java.awt.*;
import java.util.ArrayList;

/*A grid object class which divides the screen/map into a collection of tiles.  This grid manages all of the elements
 * within the workings of this game, is used to draw, update, and create new elements within the game environment*/
public class Grid{
  int gridWidth;
  int gridHeight;
  
  int tileHeight;
  int tileWidth;
  
  int xCount; //Number of tiles across the screen horizontally (x axis) 
  int yCount; //Number of tiles across the screen vertically (y axis)
  
  Tile[][] tiles; //array of tiles
  ArrayList<Gate> gates; //array list of all gates currently in game
  ArrayList<Explosion> explosions; //array list of all explosions currently in game
  
  //Constructor routine accepts the specified sizes of the grid and tiles and intialises the grid accordingly
  public Grid(int gridWidth, int gridHeight, int tileWidth, int tileHeight){
    this.gridWidth = gridWidth;
    this.gridHeight = gridHeight;
    this.tileWidth = tileWidth;
    this.tileHeight = tileHeight;
    this.gates  = new ArrayList<Gate>();
    this.explosions = new ArrayList<Explosion>();
        
    this.xCount = gridWidth/tileWidth; //determine amount of tiles which will fit in the width of the screen
    this.yCount = gridHeight/tileHeight;//determine amount of tiles which will fit in the height of the screen
    
    tiles = new Tile[xCount][yCount]; //create a new array with the amount of tiles which will fit in each direction
    
    /*Fill the array with tiles at the specified coordinate for each (determined by multiplying the tile's 
     * x - number (in the array) by the tile width and its y - number by the tile height*/
    for(int x = 0; x < xCount; x++){
      for(int y = 0; y < yCount; y++){
        this.tiles[x][y] = new Tile(this, x*tileWidth, y*tileHeight);
      }
    }
    
  }
  
  /*Routine to update all the elements of the grid; all tiles, gates, and explosions (accepts the player's x and y 
   * coordinates)*/
  public void update(int px, int py){
    //iterate through tiles and update them with the player position (for enemy updates)
    for(Tile[] tr: this.tiles){
      for(Tile t: tr)
        t.update(px,py);
    }
    
    //iterate through the gates and explosions and call update for them
    for(Gate ga: this.gates)
      ga.update();
    for(Explosion ex: this.explosions){
      ex.update();
    }
      
  }
  
  /*Routine to draw all the components of the grid including tiles, gates, and explosions (accepts the graphics 
   * object from the panel it is on*/
  public void draw(Graphics2D g){
    //Iterate through the tiles and call draw
    for(Tile[] tr: this.tiles){
      for(Tile t: tr)
        t.draw(g);
    }
    
    //iterate through the gates and explosions and call draw for them
    for(Gate ga: this.gates)
      ga.draw(g);
    for(Explosion ex: this.explosions)
      ex.draw(g);
  }
  
  /*Routine to spawn enemies, accepts a specified amount (amt) as well as the player's x and y coordinates*/
  public void spawnEnemies(int amt, int pX, int pY){
    //Get the x and y of the tile the player is on
    int playerX = pX/this.tileWidth;
    int playerY = pY/this.tileHeight;
    //X and y of tile to spawn from
    int x;
    int y;
    
    /*Randomly choose a tile to spawn until one is chosen that is not within a radius of 1 square in any direction
     * from the player*/
    do{
    x = (int)(Math.random()*xCount);
    y = (int)(Math.random()*yCount);
    }while(x == playerX || x == playerX + 1 || x == playerX - 1 || 
           y == playerY || y == playerY + 1 || y == playerY - 1);
    
    //creates a spawn warning (which is followed by a spawn) on the specified tile with the specified amount
    this.tiles[x][y].spawnWarn(amt); 
  }
  
  /*Routine to return the number of enemies currently in the game*/
  public int getEnemyCount(){
    int enemyCount = 0;
    //Iterate through tiles and add the enemy count of each to the total enemy count (enemyCount)
    for(Tile[] tr: this.tiles){
      for(Tile t: tr){
        enemyCount += t.getEnemyCount();
      }
    }
    return enemyCount;
  }
  
  /*Routine to move enemies between tiles (as each tile is in charge of drawing only the enemies within its borders)
   * Accepts an enemy object (the one which needs to be moved)*/  
  public void move(Enemy en){
    //Try to add the given enemy to whichever tile it now rests on
    try{
      //determing which tile
      int x = (int)(en.x / this.tileWidth);
      int y = (int)(en.y / this.tileHeight);
      //add enemy to that tile
      this.tiles[x][y].addEnemy(en);
    }
    catch(ArrayIndexOutOfBoundsException e){}; //in case something goes awry (for instance near the borders)
  }
  
  /*Routine to check the collisions in all the tiles*/
  public void checkCollisions(){
    //iterate through tiles and check the collisions within each
    for(Tile[] tr: this.tiles){
      for(Tile t: tr){
        t.checkCollisions();
      }
    }
  }
  
  /*Routine to check if player is in contact with enemies (is killed), accepts the player x and y*/
  public boolean checkPlayer(int pX, int pY){
    /*iterate through tiles and check the player (goes through all tiles as some may overlap b/w tiles so checking
     * only the player's will not always work)*/
    for(Tile[] tr: this.tiles){
      for(Tile t: tr){
        if(t.checkPlayer(pX, pY)){
          this.explosions.add(new Explosion(pX, pY, this)); //if the player is killed, create an explosion at the site
          return true; //return that the player has died
        }
      }
    }
    return false; //return that the player is alive
  }
  
  /*Routine to spawn a new gate at a random tile*/
  public void spawnGate(){
    //randomly choose a tile a specified distance away from the edge (+3, -3)
    int x = (int)(Math.random()*(xCount - 3) + 3);
    int y = (int)(Math.random()*(yCount - 3) + 3);
    //add a new gate at the center of the chosen tile
    gates.add(new Gate(tiles[x][y].xSpawn, tiles[x][y].ySpawn, this));
  }
  
  /*Routine to return the number of gates currently in the game*/
  public int getGateCount(){
    return gates.size();
  }
  
  /*Routine to check if the player has triggered any gates (accepts player x,y,width,height)*/
  public void checkGates(int pX, int pY, int pW, int pH){
    ArrayList<Gate> toMove = new ArrayList<Gate>(); //arraylist of gates which must be removed and exploded
    //iterate through gatees and check each
    for(Gate ga: this.gates){
      if(ga.playerThrough(pX, pY, pW, pH))
        toMove.add(ga); //if triggered, add to list to remove
    }
    
    //iterate through gates to remove and explode them
    for(Gate gA: toMove)
      explode(gA);
  }
  
  /*Routine to add an explosion at the site of a triggered gate and remove the gate from the grid's arrayList
   * accepts a gate to remove*/
  public void explode(Gate ga){
    this.explosions.add(new Explosion(ga.centerX, ga.centerY, this));
    this.gates.remove(ga);
  }
  
  /*Routine to check all explosions and determine whether they are still ongoing*/
  public void checkExplosions(){
    ArrayList<Explosion> toRemove = new ArrayList<Explosion>(); //explosions to remove
    //iterate through explosions and check if they are dead (boolean isDead)
    for(Explosion ex: this.explosions){
      if(ex.isDead)
        toRemove.add(ex); //if dead, add to arraylist to remove
    }
    //iterate through explosions to remove and remove them from the grid's arrayList
    for(Explosion ex: toRemove)
      this.explosions.remove(ex);
  }
  
  
}