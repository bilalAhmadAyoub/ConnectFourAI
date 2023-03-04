/*******************************GameDisplay**************************************************************************/

import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameDisplay {
    //variables
    JButton[][] buttons;
    int[][] gameBoard;
    int[] chipsInColumn;
    boolean playersTurn;
    boolean playAgainstAI;
    boolean gameOver;
    AIOpponent ai;
    
    //constructor
    GameDisplay(boolean playAgainstAI) {
        this.playAgainstAI = playAgainstAI;
        gameOver = false;
        buttons = new JButton[6][7]; //six rows, with seven columns each
        gameBoard = new int[6][7];
        chipsInColumn = new int[7];
        playersTurn = true;
        ai = new AIOpponent();

        //initialize the number of chips in each column (fix this later)
        for(int i = 0; i < chipsInColumn.length; i++) chipsInColumn[i] = 5;

        createButtons();
    } //GameDisplay constructor

    public void switchOpponent() {
        restart();
        playAgainstAI = !playAgainstAI;
    } //switchOpponent

    public void restart() {
        gameOver = false;
        gameBoard = new int[6][7];
        chipsInColumn = new int[7];
        playersTurn = true;
        ai = new AIOpponent();

        //initialize the number of chips in each column (fix this later)
        for(int i = 0; i < chipsInColumn.length; i++) chipsInColumn[i] = 5;

        int counter = 1;
        for(int i = 0; i < buttons.length; i++) {
            for(int j = 0; j < buttons[0].length; j++) {
                buttons[i][j].setText("" + counter++);
            } //for
        } //for
    } //restart

    public void createButtons() {
        //initialize the buttons
        int counter = 1;

        for(int i = 0; i < buttons.length; i++) {
            for(int j = 0; j < buttons[0].length; j++) {
                //create the button
                buttons[i][j] = new JButton("" + counter++);
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 40)); //40
                
                //add the functionality for when the button is clicked
                final int column = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addChipToColumn(column); //the player makes a move!

                        Timer timer = new Timer(1000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (playAgainstAI) addChipToColumn(column); //the AI makes a move (after one second!)
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    } //actionPerformed
                }); //addActionListener
            } //for 
        } //for

    } //createButtons

    public void addChipToColumn(int column) {
        if(!playersTurn && playAgainstAI) column = ai.getNextMove(column); //get the ai's move (if it is it's turn!)
        if(chipsInColumn[column] < 0 || gameOver) return; //if the column is filled up, or the game is over, we can't add a chip!
        buttons[chipsInColumn[column]][column].setText(playersTurn ? "🟡" : playAgainstAI ? "🤖" : "🔴"); //update game display
        gameBoard[chipsInColumn[column]][column] = playersTurn ? 1 : 2; //update game board
        chipsInColumn[column]--; //one less empty slot in the column        
        gameOver = checkForWinner() || gameBoardFilled(); //check to see if the game is over
        playersTurn = !playersTurn; //other player's turn!
    } //addChipToColumn

    public boolean checkForWinner() { //maybe put this function in the ai class and then call it?
        return (checkForWinnerHelper(0, 1) || checkForWinnerHelper(1, 0) || checkForWinnerHelper(1, 1) || checkForWinnerHelper(1, -1));
    } //checkForWinner

    //r=0 c=1 checks horizontal wins, r=1 c=0 checks vertical wins, r=1 c=1 checks diagonal (\) wins, r=1 c=-1 checks diagonal (/) wins.
    private boolean checkForWinnerHelper(int r, int c) {
        for(int i = 0; i < ((r == 1) ? 3 : gameBoard.length); i++) { //iterate through the rows
            for(int j = ((c == -1) ? 3 : 0); j < ((c == 1) ? 4 : gameBoard[0].length); j++) { //iterate through the columns
                if(gameBoard[i][j] != 0 && (gameBoard[i][j] == gameBoard[i + 1 * r][j + 1 * c]) && (gameBoard[i][j] == gameBoard[i + 2 * r][j + 2 * c]) && (gameBoard[i][j] == gameBoard[i + 3 * r][j + 3 * c])) {
                    //we want to make them green and return here
                    buttons[i][j].setBackground(Color.GREEN);
                    buttons[i][j].setVisible(true);

                    for(int k : new int[]{0, 1, 2, 3}) buttons[i + k * r][j + k * c].setText(playersTurn ? "🟢" : "🟩");

                    System.out.println("WINNER");

                    return true;
                } //if
            } //for
        } //for

        return false;
    } //checkForWinnerHelper

    public boolean gameBoardFilled() {
        for(int[] row : gameBoard) {
            for(int cell : row) if(cell == 0) return false;
        } //for
        return true;
    } //gameBoardFilled
} //GameDisplay


