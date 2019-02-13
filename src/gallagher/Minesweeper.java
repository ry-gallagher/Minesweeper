package gallagher;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;


//******************************************************************************
//Minesweeper: Constructor methods
//******************************************************************************
public class Minesweeper extends JFrame {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 500;

    JPanel center = new JPanel();
    Container cp;
    MButton[][] buttons; //2D Array of buttons for grid

    JLabel victoryLabel = new JLabel(); //JLabel for displaying victory status
    int totalButtons = 240; //total number of buttons in the grid
    int clickedCount = 0;   //total number of player clicks
    int totalBombCount = 0; //total number of bombs within the grid
    int noBombButtons = 0;  //total number of buttons without bombs

    int time = 0; //incrementing value representing time
    int delay = 60; //delay used for timer
    JLabel timeLabel = new JLabel(); //label represeting total time
    Timer timer = new Timer(delay, new TimerListener( )); //timer utilizing timer listener


    public Minesweeper() {
        super();
        setSize(WIDTH, HEIGHT);
        setTitle("Minesweeper");

        cp = getContentPane();

        center.setLayout(new GridLayout(14,22));

        add(center, BorderLayout.CENTER);
        add(timeLabel, BorderLayout.NORTH);
        add(victoryLabel, BorderLayout.SOUTH);

        ButtonListener button_listener = new ButtonListener();
        buttons = new MButton[14][22];

        createButtons(button_listener);

        createGrid();

        placeBombs();

        counter();

        hideButtons();

        timer.start( );
    }//Minesweeper

    //**************************************************************************
    //createButtons: Utilizes MButton class to create a two dimensional array
    //of buttons to be used in game board
    //**************************************************************************
    public void createButtons(ButtonListener b) {
        for( int i = 0; i < 14; i++ ) {
            for( int j = 0; j < 22; j++ ) {
                buttons[i][j] = new MButton();
                buttons[i][j].addActionListener(b);
                center.add(buttons[i][j], BorderLayout.CENTER);
                buttons[i][j].setVisible(false);
                buttons[i][j].isClicked  = true;
                buttons[i][j].isBomb = false;
                buttons[i][j].bombCount = 0;
            }//for
        }//for
    }//createButtons

    //**************************************************************************
    //createGrid: Creates visbile gameboard grid and sets out block to invisible
    //**************************************************************************
    public void createGrid() {
        for( int i = 1; i < 13; i++ ) {
            for(int j = 1; j < 21; j++) {
                buttons[i][j].setVisible(true);
                buttons[i][j].isClicked = false;
            }//for
        }//for
    }//createGrid

    //**************************************************************************
    //placeBombs: Places bombs within visible grid. 20% chance for boms to spawn
    //**************************************************************************
    public void placeBombs() {
        for( int i = 1; i < 13; i++ ) {
            for( int j = 1; j < 21; j++ ) {
                Random rand = new Random();
                int k = rand.nextInt(100);

                if(k >= 80) {
                    buttons[i][j].isBomb = true;
                    totalBombCount++;
                }//if
            }//for
        }//for
        noBombButtons = totalButtons - totalBombCount;
    }//placeBombs

    //**************************************************************************
    //counter: Loops through grid and determines number of bombs surrounding
    //each button
    //**************************************************************************
    public void counter() {
        for( int i = 1; i < 13; i++ ) {
            for(int j = 1; j < 21; j++) {
                if(buttons[i][j].isBomb == true) {
                    buttons[i][j].bombCount = -1;
                }//if

                if(buttons[i][j].isBomb == false) {
                    if(buttons[i - 1][j - 1].isBomb == true) {
                        buttons[i][j].bombCount++;
                    }//if
                    if(buttons[i - 1][j].isBomb == true) {
                        buttons[i][j].bombCount++;
                    }//if
                    if(buttons[i - 1][j + 1].isBomb == true) {
                        buttons[i][j].bombCount++;
                    }//if
                    if(buttons[i][j - 1].isBomb == true) {
                        buttons[i][j].bombCount++;
                    }//if
                    if(buttons[i][j + 1].isBomb == true) {
                        buttons[i][j].bombCount++;
                    }//if
                    if(buttons[i + 1][j - 1].isBomb == true) {
                        buttons[i][j].bombCount++;
                    }//if
                    if(buttons[i + 1][j].isBomb == true) {
                        buttons[i][j].bombCount++;
                    }//if
                    if(buttons[i + 1][j + 1].isBomb == true) {
                        buttons[i][j].bombCount++;
                    }//if

                    buttons[i][j].setText(Integer.toString(buttons[i][j].bombCount));
                }//if
            }//for
        }//for
    }//counter

    //**************************************************************************
    //hideButtons: Hides button values
    //**************************************************************************
    public void hideButtons() {
        for( int i = 1; i < 13; i++ ) {
            for(int j = 1; j < 21; j++) {
                buttons[i][j].setText("");
            }//for
        }//for
    }//hideButtons

    //**************************************************************************
    //click: Perfomed from button listener, determines button value and changes
    //game board accordingly
    //**************************************************************************
    public void click(int i, int j) {
        if(buttons[i][j].isClicked == false) {

            if(buttons[i][j].isBomb == true) {
                timer.stop();
                victoryLabel.setText("You lose!");
                victoryLabel.setForeground(Color.red);

                for(int x = 1; x < 13; x++) {
                    for(int y = 1; y < 21; y++) {
                        if(buttons[x][y].isBomb == true) {
                            buttons[x][y].setText("X");
                            buttons[x][y].setBackground(Color.RED);
                        }//if
                    }//for
                }//for
            }//if

            if(buttons[i][j].bombCount > 0) {
                buttons[i][j].isClicked = true;
                clickedCount++;
                buttons[i][j].setText(Integer.toString(buttons[i][j].bombCount));
            }//if

            if(buttons[i][j].bombCount == 0) {
                if (i < 1 || i > 13 || j < 1 || j > 21) { return; }//bounds checking

                buttons[i][j].isClicked = true;
                clickedCount++;

                buttons[i][j].setText(Integer.toString(buttons[i][j].bombCount));

                click(i-1, j-1);
                click(i-1, j);
                click(i-1, j+1);
                click(i, j-1);
                click(i, j+1);
                click(i+1, j-1);
                click(i+1, j);
                click(i+1, j+1);

            }//if

            if(boardRevealed() == true) {
                timer.stop();
                victoryLabel.setText("You win!");
                victoryLabel.setForeground(Color.green);

                for(int x = 1; x < 13; x++) {
                    for(int y = 1; y < 21; y++) {
                        if(buttons[x][y].isBomb == true) {
                            buttons[x][y].setText("X");
                            buttons[x][y].setBackground(Color.GREEN);
                        }//if
                    }//for
                }//for
            }//if
        }//if
    }//click

    //**************************************************************************
    //boardRevealed: Determines if player has revealed the entire board
    //**************************************************************************
    public boolean boardRevealed() {
        return clickedCount  == noBombButtons;
    }//boardRevealed

    //**************************************************************************
    //Listeners: Used for buttons and timer
    //**************************************************************************
    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for(int i = 1; i < 13; i++) {
                for(int j = 1; j < 21; j++) {
                    if(buttons[i][j].equals(e.getSource())) {
                        click(i, j);
                    }//if
                }//for
            }//for
        }//actionPerformed
    }//ButtonListener

    private class TimerListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){
            time++;
            timeLabel.setText("Elapsed Time = " + ((time * delay) / 1000) + "s");
        }//actionPerformed
    }//TimerListener

    public static void main(String[] args) {
        Minesweeper sweeper = new Minesweeper();
        sweeper.setVisible(true);
    }//main
}//Minesweeper
