/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;
/**
 *
 * @author Jeffrey
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class GUI extends JPanel implements ItemListener{
    private static final int WIDTH = 200;
    private static final int HEIGHT = 100;

    private JLabel MoodL, ArtistL;
    private JCheckBox mood0, mood1, mood2, mood3, mood4, mood5, mood6, mood7;
    private JTextField lyricsTF, artistTF, areaTF;
    private JButton find;

    //Button handlers:
    private FindButtonHandler fHandler;
    private ExitButtonHandler ebHandler;
    
    boolean[] moods = new boolean[8];

    public GUI()
    {
        MoodL = new JLabel("Mood:",SwingConstants.CENTER);
        ArtistL = new JLabel("Artist:",SwingConstants.CENTER);
        
        artistTF = new JTextField(20);
        
        mood0 = new JCheckBox("Love");
        mood1 = new JCheckBox("Joyfulness");
        mood2 = new JCheckBox("Satisfied");
        mood3 = new JCheckBox("Calm");
        mood4 = new JCheckBox("Sadness");
        mood5 = new JCheckBox("Contempt");
        mood6 = new JCheckBox("Disgust");
        mood7 = new JCheckBox("Astonished");
        
        mood0.addItemListener(this);
        mood1.addItemListener(this);
        mood2.addItemListener(this);
        mood3.addItemListener(this);
        mood4.addItemListener(this);
        mood5.addItemListener(this);
        mood6.addItemListener(this);
        mood7.addItemListener(this);
        
        JPanel checkPanel = new JPanel(new GridLayout(2, 4));
        checkPanel.add(mood0);
        checkPanel.add(mood1);
        checkPanel.add(mood2);
        checkPanel.add(mood3);
        checkPanel.add(mood4);
        checkPanel.add(mood5);
        checkPanel.add(mood6);
        checkPanel.add(mood7);  
        
        find = new JButton("Search");
        fHandler = new FindButtonHandler();
        find.addActionListener(fHandler);

        
        add(MoodL);
        add(checkPanel);
        add(ArtistL);
        add(artistTF);
        add(find);
        setSize(WIDTH,HEIGHT);
    }
    
     public void itemStateChanged(ItemEvent e) {
        int index = 0;
        Object source = e.getItemSelectable();
 
        if (source == mood0) {
            index = 0;
        } else if (source == mood1) {
            index = 1;
        } else if (source == mood2) {
            index = 2;            
        } else if (source == mood3) {
            index = 3;           
        } else if (source == mood4) {
            index = 4;         
        } else if (source == mood5) {
            index = 5;          
        } else if (source == mood6) {
            index = 6;           
        } else if (source == mood7) {
            index = 7;
        }
        moods[index] = true;
        //Now that we know which button was pushed, find out
        //whether it was selected or deselected.
        if (e.getStateChange() == ItemEvent.DESELECTED) {
            moods[index] = false;
        }

    }
    
    private class FindButtonHandler implements ActionListener
    {
        private Component frame;
        public void actionPerformed(ActionEvent e)
        {
            boolean check = false;
            for(int i = 0; i<moods.length; i++){
                check = moods[i]||check;
            }
            if(check){
                
            }
            else{
                JOptionPane.showMessageDialog(frame, "No Moods Selected!");
            }
        }
    }

    public class ExitButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
                System.exit(0);
        }
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("CheckBoxDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        JComponent newContentPane = new GUI();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}
