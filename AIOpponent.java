/******************************THE AI*******************************************************/

public class AIOpponent {
    static final int[] COLUMN_ARRAY = new int[]{0, 1, 2, 3, 4, 5, 6};
    int[][] gameBoard;
    IntArrayHashMap transpositionTable;
    int transpositionTableUsedCount; //used to display the AI's thought process to terminal
    String minimaxResultsDepth; //used to display the AI's thought process to terminal

    //constructor
    AIOpponent() {
        gameBoard = new int[6][7];
        transpositionTable = new IntArrayHashMap();
        transpositionTableUsedCount = 0;
        minimaxResultsDepth = "";
    } //AIOpponent

    public int getNextMove(int playerColumnChoice) {
        //first, update gameBoard with playerColumnChoice in the AI's own game board 
        addToColumn(gameBoard, 1, playerColumnChoice);

        int aiChoice = -1;

        //check to see if you (the AI) can win, if so: return the winning column
        for(int col = 0; col < gameBoard[0].length; col++)
            if(isWinningMove(gameBoard, 2, col)) aiChoice = col;
        
        //check for easy winning moves to block
        for(int col = 0; col < gameBoard[0].length && aiChoice == -1; col++)
            if(isWinningMove(gameBoard, 1, col)) aiChoice = col;

        //if there aren't any easy winning moves, use the minimax algorithm to find the next best move
        if(aiChoice == -1) aiChoice = minimax(gameBoard, 10, Integer.MIN_VALUE, Integer.MAX_VALUE, true)[0];

        System.out.println("THE AI CHOSE: " + aiChoice);
        System.out.println("Transposition table size: " + transpositionTable.size() + "   and is used: " + transpositionTableUsedCount + " times.");
        printBoard(gameBoard);
        addToColumn(gameBoard, 2, aiChoice);

        //restart the transposition table
        transpositionTable = new IntArrayHashMap();
        transpositionTableUsedCount = 0;

        return aiChoice;
    } //getNextMove

    public boolean isWinningMove(int[][] board, int chip, int column) { //returns true if placing a chip in that column is a winning move for given chip, false otherwise
        boolean isWinningMove = false;
        if(addToColumn(board, chip, column)) {
            isWinningMove = checkForWinner(board, chip);
            removeFromColumn(board, column);
        } //if

        return isWinningMove;
        //don't remove any of the three functions from this function because all three need to run for it to work :)
    } //isWinningMove


    public boolean addToColumn(int[][] board, int chip, int column) { //adds a chip to given column, return true when added
        for(int i = board.length - 1; i >= 0; i--) {
            if(board[i][column] == 0) {
                board[i][column] = chip;
                return true;
            } //if
        } //for

        return false;
    } //addToColumn

    public boolean removeFromColumn(int[][] board, int column) { //removes the last chip added to the given column, returns true when removed
        for(int i = 0; i < board.length; i++) {
            if(board[i][column] != 0) {
                board[i][column] = 0;
                return true;
            } //if
        } //for

        return false;
    } //removeFromColumn

    public boolean checkForWinner(int[][] board, int chip) { //checks if "chip" has won. (if chip is 1, that means player. if chip is 2, that means AI)
        return (checkForWinnerHelper(board, chip, 0, 1) || checkForWinnerHelper(board, chip, 1, 0) || checkForWinnerHelper(board, chip, 1, 1) || checkForWinnerHelper(board, chip, 1, -1));
    } //checkForWinner

    //r=0 c=1 checks horizontal wins, r=1 c=0 checks vertical wins, r=1 c=1 checks diagonal (\) wins, r=1 c=-1 checks diagonal (/) wins.
    private boolean checkForWinnerHelper(int board[][], int chip, int r, int c) {
        for(int i = 0; i < ((r == 1) ? 3 : board.length); i++) { //iterate through the rows
            for(int j = ((c == -1) ? 3 : 0); j < ((c == 1) ? 4 : board[0].length); j++) { //iterate through the columns
                if(board[i][j] == chip && (board[i][j] == board[i + 1 * r][j + 1 * c]) && (board[i][j] == board[i + 2 * r][j + 2 * c]) && (board[i][j] == board[i + 3 * r][j + 3 * c])) {
                    return true;
                } //if
            } //for
        } //for

        return false;
    } //checkForWinnerHelper

    public int[] minimax(int[][] board, int depth, int alpha, int beta, boolean isAITurn) {
        if(boardFull(board)) return new int[]{-1, 0};
        if(checkForWinner(board, 2)) return new int[]{-1, 2000000000};
        if(checkForWinner(board, 1)) return new int[]{-1, -1000000000};
        if(depth == 0) return new int[]{-1, findBoardScore(board, 2)};

        if(isAITurn) {
            int score = Integer.MIN_VALUE;
            int aiColumnChoice = 0;
            
            if(depth == 10) minimaxResultsDepth += "[Here's how the AI currently weighs the value of each non-full column: ";
            for(int column : COLUMN_ARRAY) {
                int[][] boardCopy = copyBoard(board);
                if(!addToColumn(boardCopy, 2, column)) continue; //don't want to do minimax for columns that are full

                //first check the transposition table
                int minimaxScore;
                if(transpositionTable.containsKey(boardCopy)) {
                    minimaxScore = transpositionTable.get(boardCopy);
                    transpositionTableUsedCount++;
                }
                else {
                    //if the transposition table doesn't contain the score, calculate it yourself
                    minimaxScore = minimax(boardCopy, depth - 1, alpha, beta, false)[1];
                    transpositionTable.put(boardCopy, minimaxScore);
                } //else

                if(depth == 10) minimaxResultsDepth += "" + minimaxScore + ", ";
                if(minimaxScore > score) {
                    score = minimaxScore;
                    aiColumnChoice = column;
                } //if
                alpha = Math.max(alpha, score);
                if(alpha >= beta) break;
            } //for

            if(depth == 10) minimaxResultsDepth += "]";

            if(depth == 10){
                System.out.println("\n\n\n");
                System.out.println(minimaxResultsDepth);

                minimaxResultsDepth = "";
            } //if

            return new int[]{aiColumnChoice, score};
        } //if
        else {
            int score = Integer.MAX_VALUE;
            int aiColumnChoice = 0;

            for(int column : COLUMN_ARRAY) {
                int[][] boardCopy = copyBoard(board);
                addToColumn(boardCopy, 1, column);

                int minimaxScore;

                if(transpositionTable.containsKey(boardCopy)) {
                    minimaxScore = transpositionTable.get(boardCopy);
                    transpositionTableUsedCount++;
                }
                else {
                    minimaxScore = minimax(boardCopy, depth - 1, alpha, beta, true)[1];
                    transpositionTable.put(boardCopy, minimaxScore);
                } //else

                if(minimaxScore < score) {
                    score = minimaxScore;
                    aiColumnChoice = column;
                } //if
                beta = Math.min(beta, score);
                if(alpha >= beta) break;
            } //for

            return new int[]{aiColumnChoice, score};
        } //else
    } //minimax

    public int[][] copyBoard(int[][] board) {
        final int[][] copiedBoard = new int[board.length][];
        for(int i = 0; i < board.length; i++) copiedBoard[i] = java.util.Arrays.copyOf(board[i], board[i].length);
        return copiedBoard;
    } //copyBoard

    public boolean boardFull(int[][] board) {
        for(int[] row : board) {
            for(int col : row) if(col == 0) return false;
        } //for

        return true;
    } //boardFull

    public int countInt(String string, int chip) {
        return string.length() - string.replaceAll("" + chip, "").length();
    } //intCountOfString

    public int findBoardScore(int[][] board, int chip) {
        int score = 0;

        //analyze the middle most column
        String middleColumn = "";
        for(int i = 0; i < 6; i++) middleColumn += board[i][3];
        score += countInt(middleColumn, chip) * 30000000;

        for(int i = 1; i <= 4; i++)
            score += findBoardScoreHelper(board, chip, i != 1, i % 2 != 0, i == 4);

        return score;
    } //findBoardScore

    public int findBoardScoreHelper(int[][] board, int chip, boolean top, boolean left, boolean negDiag) {
        int score = 0;

        for(int i = 0; i < board.length - (top ? 3 : 0); i++) {
            for(int j = (negDiag ? 3 : 0); j < board[0].length - (left ? 3 : 0); j++) {
                int rowMultiplier = top ? 1 : 0; 
                int colMultiplier = left ? 1 : (negDiag ? -1 : 0);
                String window = "" + board[i][j] + board[i + 1 * rowMultiplier][j + 1 * colMultiplier] + board[i + 2 * rowMultiplier][j + 2 * colMultiplier] + board[i + 3 * rowMultiplier][j + 3 * colMultiplier];
                score += evaluateWindow(window, chip);
            } //for
        } //for

        return score;
    } //findBoardScoreHelper

    public int evaluateWindow(String window, int chip) {
        int score = 0;
        int opponentChip = (chip == 2) ? 1 : 2;

        if(countInt(window, chip) == 4) score += 10000; //four in a row == 100
        //else if(countInt(window, chip) == 3 && countInt(window, 0) == 1) score += 50000000; //three in a row == 5
        //else if(countInt(window, chip) == 2 && countInt(window, 0) == 2) score += 10; //two in a row == 2
        
        //if(countInt(window, opponentChip) == 2 && countInt(window, 0) == 2) score -= 5; //opponent two in a row == -6
        if(countInt(window, opponentChip) == 3 && countInt(window, 0) == 1) score -= 40000000; //opponent three in a row == -4000
        
        return score;
    } //evaluateWindow
    
    public void printBoard(int[][] board) {
        for(int[] row : board) {
            for(int column : row) {
                System.out.print(" " + column);
            }
            System.out.println("");
        }
    } //printBoard
} //AIOpponent




/* (from the board score function)
        score += findBoardScoreHelper(board, chip, false, true, false); //analyze horizontal
        score += findBoardScoreHelper(board, chip, true, false, false); //analyze vertical
        score += findBoardScoreHelper(board, chip, true, true, false); //analyze positive diagonals
        score += findBoardScoreHelper(board, chip, true, false, true); //analyze negative diagonals
        */


/*
    public int findBoardScore(int[][] board, int chip) {
        int score = 0;

        //analyze the middle most column
        String tempoString = "";
        for(int i = 0; i < 6; i++) tempoString += board[i][3];
        score += countInt(tempoString, chip) * 30000000;

        //analyze horizontal
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[0].length - 3; j++) {
                String window = "" + board[i][j] + board[i][j + 1] + board[i][j + 2] + board[i][j + 3];
                score += evaluateWindow(window, chip);
            } //for
        } //for

        //analyze vertical
        for(int i = 0; i < board.length - 3; i++) {
            for(int j = 0; j < board[0].length; j++) {
                String window = "" + board[i][j] + board[i + 1][j] + board[i + 2][j] + board[i + 3][j];
                score += evaluateWindow(window, chip);
            } //for
        } //for

        //positive diagonal
        for(int i = 0; i < board.length - 3; i++) {
            for(int j = 0; j < board[0].length - 3; j++) {
                String window = "" + board[i][j] + board[i + 1][j + 1] + board[i + 2][j + 2] + board[i + 3][j + 3];
                score += evaluateWindow(window, chip);
            } //for
        } //for

        //negative diagonal
        for(int i = 0; i < board.length - 3; i++) {
            for(int j = 3; j < board[0].length; j++) {
                String window = "" + board[i][j] + board[i + 1][j - 1] + board[i + 2][j - 2] + board[i + 3][j - 3];
                score += evaluateWindow(window, chip);
            } //for
        } //for

        return score;
    } //findBoardScore */
