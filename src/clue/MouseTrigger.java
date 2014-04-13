package clue;

import clue.ClueGame;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Marissa on 3/26/14.
 */
public class MouseTrigger implements MouseListener {



    //Internal memory management
    ClueGame game;
   // Graphics myGraphics;
    //constructor
    MouseTrigger(ClueGame g){
        game = g;
       // this.myGraphics = graphics;
    }

    //THe mouse clicked is awesome
    @Override
    public void mouseClicked(MouseEvent e) {

        game.checksValidTarget(new Point (e.getX(), e.getY()));

        return;
    }

    //this is the mousePressed
    @Override
    public void mousePressed(MouseEvent e) {

    }

    //The mouse is released
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    //The mouse is entered!!!
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    //The mouse is exited :)
    @Override
    public void mouseExited(MouseEvent e) {

    }
}
