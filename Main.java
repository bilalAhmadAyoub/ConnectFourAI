import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Main {
    public static void main(String[] args) {
        //make the game buttons, their functionality, and the AI
        GameDisplay game = new GameDisplay(true);    

        //make the frame
        JFrame frame = new JFrame("Connect Four");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /***************************CREATING THE GAME PAGE***************************************/

        //create the game page--this holds the game buttons
        JPanel gamePage = new JPanel();
        gamePage.setLayout(new GridLayout(7, 1));

        //create the rows (1 top bar row + 6 rows for the actual game = 7)
        JPanel[] gameRows = new JPanel[7];

        /***************************CREATING THE TOP BAR***************************************/

        //make the top bar row (this contains a restart button and a button to switch between an AI or human opponent)
        JButton restartButton = new JButton("RESTART");
        JButton opponentSwitchButton = new JButton("PLAY AGAINST HUMAN");

        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.restart();
            } //actionPerformed
        }); //addActionListener

        opponentSwitchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                opponentSwitchButton.setText(opponentSwitchButton.getText().equals("PLAY AGAINST HUMAN") ? "PLAY AGAINST AI" : "PLAY AGAINST HUMAN");

                game.switchOpponent();
            } //actionPerformed
        }); //addActionListener

        //add the top row to the UI
        gameRows[0] = new JPanel();
        gameRows[0].setLayout(new GridLayout(1,2));
        gameRows[0].add(restartButton);
        gameRows[0].add(opponentSwitchButton);
        gamePage.add(gameRows[0]);

        /***************************CREATING THE GAME ROWS UI***************************************/

        //create the Connect Four game rows
        for(int i = 0; i < game.buttons.length; i++) {
            gameRows[i + 1] = new JPanel();
            gameRows[i + 1].setLayout(new GridLayout(1,7)); 
            for(int j = 0; j < game.buttons[0].length; j++) gameRows[i + 1].add(game.buttons[i][j]);
            gamePage.add(gameRows[i + 1]);
        } //for

        //add the gamePage to the frame
        frame.add(gamePage);
        frame.setVisible(true);
    } //main
} //Main


