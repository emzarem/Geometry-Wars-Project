/*Eric Murphy-Zaremba
 * Mr.Boss
 * ICS 4U1
 * Jan. 20 / 2015*/

import java.io.*;
import javax.sound.sampled.*;

/*Class to handle the sounds within the game*/
/*PLEASE NOTE: Implementing sound impeded the performance of the game, for some reason causing it to seem to move 
 * at a far faster speed (likely has to do with the computer screen's frame rate, perhaps because of the way
 * it makes the computer run) Therefore I have left the sound portions commented out, if you would like to try them 
 * you can go to the Explosion class and uncomment the 3 lines near the beginning (one Sound variable statement
 * and two in the constructor)and the one line in gamePane in the constructor, perhaps it is only my computer which
 * is experiencing this phenomenon*/

public class Sound{

  /*routine to play exploding sound*/
  public void explode(){
    File sound = new File("explosion.wav");
    try{
      Clip clip = AudioSystem.getClip();
      clip.open(AudioSystem.getAudioInputStream(sound)); //open the specified clip
      clip.start(); //play it
    }
    catch(Exception e){System.out.println(e);}
  }
  
  /*Routine to play theme*/
  public void playTheme(){
    File sound = new File("Death on the Dance Floor_1.wav");
    try{
      Clip clip = AudioSystem.getClip();
      clip.open(AudioSystem.getAudioInputStream(sound)); //open the specified clip
      clip.start(); //start it
      clip.loop(Clip.LOOP_CONTINUOUSLY); //loop it
    }
    catch(Exception e){System.out.println(e);}
  }
  
}