/*Eric Murphy-Zaremba
 * Mr.Boss
 * ICS 4U1
 * Jan. 20 / 2015*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*Main routine which controls the switching between the MENU and PLAY states of the game, inserting the appropriate
 * panel into the containing JFrame accordingly*/

public class GeoMain{
  
  static JPanel content; //content pane
  static GamePane gamePane; //game panel
  static MenuPane menuPane; //menu panel
  static boolean play; //checks if game is already being played (avoids having double start ups of play)
  
  public static void main(String[]args){
    JFrame window = new JFrame();
    content = new JPanel(new BorderLayout());    
    content.setLayout(new OverlayLayout(content));
    window.setContentPane(content);
    
    //ActionListener to listen for a neccesary change in game state to PLAY (triggered when 'play' is pressed)
    ActionListener menuListen = new ActionListener(){
      public void actionPerformed(ActionEvent e){
        changeState("PLAY");
      }      
    };
    
    //ActionListener to listen for a neccesary change in game state to MENU (triggered when 'menu' is pressed)
    ActionListener playListen = new ActionListener(){
      public void actionPerformed(ActionEvent e){        
        changeState("MENU");
      }      
    };
    
    //Initialise panels and variables
    //1100, 750
    int w = 1100;
    int h = 750;
    play = false;
    menuPane = new MenuPane(w, h, menuListen);
    gamePane = new GamePane(playListen, w,h);
    
    //WindowListener to end the program when the window is closed
    window.addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e){
        System.exit(0);
      } 
    });
    
    //Set up the window
    changeState("MENU"); //initialise by setting the state as MENU
    window.setSize(w + 16,h + 38); //+16 and +38 to compensate for frame size
    window.setResizable(false);
    window.setLocationRelativeTo(null);
    window.setVisible(true);
  }
  
  /*Routine to perform a change in the state of the game between MENU and PLAY, accepts a string to define the
   * game state which it needs to switch to*/
  public static void changeState(String gameState){    
    //Clear the panel and add the menu (and set it up)
    if(gameState.equals("MENU")){
      content.removeAll(); 
      content.add(menuPane, BorderLayout.CENTER);
      play = false;
      menuPane.refresh.start(); //begins menu running
    }
    
    //Clear the panel and add the gamePane to it, reinitialising it each time for consecutive plays
    else if(gameState.equals("PLAY") && !play){
      play = true;
      content.removeAll();        
      content.add(gamePane, BorderLayout.CENTER);
      gamePane.reset();
      gamePane.frameRate.start(); //begins game running
      gamePane.setFocusable(true);
      gamePane.requestFocusInWindow();
      menuPane.refresh.stop(); //stops menu from running in background
    }
    
    //Finally repaint the frame with the new components
    content.repaint();
  }   
  
  
  
}