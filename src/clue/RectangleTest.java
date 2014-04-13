package clue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Marissa on 4/8/14.
 */
public class RectangleTest {
    public static  void main (String [] args){
        ClueGame game = new ClueGame("CluePlayers.txt", "ClueLayout.csv", "ClueLegend.txt", "ClueWeapons.txt");
        Board board = new Board("ClueLayout.csv", "ClueLegend.txt", game);
        //game.setVisible(true);
        JFrame myFrame = new JFrame();
        myFrame.setTitle("Clue");
        myFrame.setSize(1000, 1000);
        final Rectangle rectangle = new Rectangle(10,10,20, 20);
        //JButton kjjl = new JButton("agafds");
        ///myFrame.add(kjjl);
        myFrame.setVisible(true);
        MouseListener mdf = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
               if (rectangle.contains(new Point ( e.getX(), e.getY())))
                System.out.println("adgasf");
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
        myFrame.addMouseListener(mdf);

    }
}
