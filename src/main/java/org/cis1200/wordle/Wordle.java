package org.cis1200.wordle;

/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import java.io.*;
import java.util.*;

/**
 * This class is a model for TicTacToe.
 * 
 * This game adheres to a Model-View-Controller design framework.
 * This framework is very effective for turn-based games. We
 * STRONGLY recommend you review these lecture slides, starting at
 * slide 8, for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec36.pdf
 * 
 * This model is completely independent of the view and controller.
 * This is in keeping with the concept of modularity! We can play
 * the whole game from start to finish without ever drawing anything
 * on a screen or instantiating a Java Swing object.
 * 
 * Run this file to see the main method play a game of TicTacToe,
 * visualized with Strings printed to the console.
 */
public class Wordle {

    private String[][] board;

    private int[] usedLetters = new int[26];

    private int[][] colors;
    // private int numTurns;
    // private boolean player1;
    private int curRow = 0;
    private List<String> curWordLetters;
    // int curLetter = 0; //same as size of curWordLetters
    private boolean enterPressed;

    private static Set<String> words;
    private static Set<String> targetWords;
    private String targetWord;
    private boolean savedBoard;

    private int winCounter = 0;
    private int gameCounter = 0;
    private String statusMessage;

    private boolean gameOver;
    private boolean gameWon;

    /**
     * Constructor sets up game state.
     */
    public Wordle() {
        words = new HashSet<String>();
        targetWords = new HashSet<String>();

        // String filelocation = "../../../../../files/";
        try {
            Scanner scanner = new Scanner(new File("files/words.txt"));
            while (scanner.hasNext()) {
                // add scanner.nextLine() words to array
                String hi = scanner.next();
                if (!(hi.equals("")) && !(hi.equals(null))) {
                    words.add(hi);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("making set of words: word file not found");
        }

        Scanner scanner2;
        try {
            scanner2 = new Scanner(new File("files/targetwords.txt"));
        } catch (Exception e) {
            throw new RuntimeException("making set of target words: targetwords file not found");
        }

        int count = 0;
        try {
            while (scanner2.hasNext()) {
                // System.out.println("hello");
                // add scanner.nextLine() words to array
                String hi = scanner2.next();
                if (!(hi.equals("")) && !(hi.equals(null))) {
                    // System.out.println("aa " + hi);
                    targetWords.add(hi);
                }
                count++;
            }
        } catch (Exception e) {
            System.out.println("failed\n\n\n\n\n\n\n");
        }

        // try {
        // targetWord = generateTargetWord();
        // } catch (Exception e) {
        // throw new RuntimeException("generate target word: word file not found");
        // }

        board = new String[6][5];
        savedBoard = false;
        try {
            readSavedBoard(); // updates savedBoard

        } catch (Exception e) {
            targetWord = "";
            reset(false);
        }

        // System.out.println("saved board? " + savedBoard);

        if (!savedBoard) {
            targetWord = "";
            reset(false);
        } else {
            // for (int i = 0; i < 5; i++) {
            // if (board[0][i] != null) {
            // System.out.print(board[0][i] + " ");
            // }
            // }
            reset(true);
        }

        for (int i = 0; i < usedLetters.length; i++) {
            usedLetters[i] = 0;
        }

    }

    public int getCurLetterIndex() {
        if (curWordLetters == null) {
            return 0;
        }
        return curWordLetters.size();
    }

    public List<String> getCurWord() {
        return curWordLetters;
    }

    public int getCurRow() {
        return curRow;
    }

    public int[] getCurRowColors() {
        return colors[curRow];
    }

    public int[][] getColors() {
        return colors;
    }

    public LinkedList<String> listOfTargetString() {
        LinkedList<String> target = new LinkedList<String>();
        for (int i = 0; i < targetWord.length(); i++) {
            target.add(Character.toString(targetWord.charAt(i)));
        }
        return target;
    }

    public LinkedList<String> listOfString(String s) {
        LinkedList<String> target = new LinkedList<String>();
        for (int i = 0; i < s.length(); i++) {
            target.add(Character.toString(s.charAt(i)));
        }
        return target;
    }

    /**
     * playTurn allows players to play a turn. Returns true if the move is
     * successful and false if a player tries to play in a location that is
     * taken or after the game has ended. If the turn is successful and the game
     * has not ended, the player is changed. If the turn is unsuccessful or the
     * game has ended, the player is not changed.
     *
     * @param c column to play in
     * @param r row to play in
     * @return whether the turn was successful
     */
    public boolean playTurn(String letter) {
        // if (board[curRow][getCurLetterIndex()] != 0 || gameOver) {
        // return false;
        // }

        // System.out.println("\n\n\nHELLO\n\n\n");

        if (gameOver) {
            return false;
        }
        enterPressed = false;
        if (getCurLetterIndex() < 5) {
            board[curRow][getCurLetterIndex()] = letter;
            // System.out.println("letter at " + curRow + ", " + getCurLetterIndex());
            curWordLetters.add(letter); // updates getcurletterindex
        } // else don't do anything
          // System.out.println("curwordletters: " + curWordLetters);
          // System.out.println("in playTurn, " +
          // curWordLetters.get(getCurLetterIndex()-1));

        return true;
    }

    public boolean deleteLetter() {
        enterPressed = false;
        if (getCurLetterIndex() > 0) {
            // delete last letter
            curWordLetters.remove(curWordLetters.size() - 1);
            if (board[curRow][getCurLetterIndex()] != null) {
                // System.out.println("removing letter from board");
                board[curRow][getCurLetterIndex()] = null;
            }
        } else {
            return false;
        }
        return true;
    }

    // execute the following function when the enter key is pressed
    public boolean checkWord() { // enter is pressed
        // System.out.println("\n\n\n\nENTER WAS PRESSED!!!");
        // System.out.println(curWordLetters);
        if (gameOver) {
            return false;
        }

        if (getCurLetterIndex() == 5) {
            // System.out.println("at last letter inside checkWord (enter pressed)\n");
            enterPressed = true;
            String curWord = "";
            for (int i = 0; i < curWordLetters.size(); i++) {
                curWord += curWordLetters.get(i);
            }
            // System.out.println("curWord: " + curWord);

            if (!(words.contains(curWord.toLowerCase()))) {
                statusMessage = "Invalid word";
                System.out.println("OOPS INVALID WORD!");
                // throw new IllegalArgumentException("word not valid");
            } else if ((targetWord.toLowerCase()).equals(curWord.toLowerCase())) {
                // you win
                gameOver = true;
                gameWon = true;
                changeLetterColors(); // all green
                // TODO CALL SOME GAME END FUNCTION HERE
            } else {
                statusMessage = " ";
                // System.out.println("valid word, about to change letter colors");
                if (curRow >= 5) {
                    // System.out.println("last row and its not the target!!!!!\n\n\n\n\n");
                    // last row and target word didn't equal curWord
                    gameOver = true;
                    gameWon = false;
                    changeLetterColors();
                } else {
                    changeLetterColors(); // change letter colors for this row
                    if (curRow >= 6) {
                        gameOver = true;
                        gameWon = false;
                        // TODO run some game over function screen
                    } else {
                        gameOver = false;
                        gameWon = false;
                        // System.out.println("printing letters so far in checkWord()");
                        // for (int i = 0; i <= curRow; i++) {
                        // for (int j = 0; j < 5; j++) {
                        // System.out.println(board[i][j]);
                        // }
                        // System.out.println();
                        // }

                        curRow++;
                        curWordLetters = new LinkedList<String>();
                    }
                }
            }
        } else if (getCurLetterIndex() < 6) {
            // TODO print to the screen not enough letters
            statusMessage = "Not enough letters";
            enterPressed = false;
        }

        return true;
    }

    // called when enter is pressed and word is valid
    // updates the colors 2d array
    public void changeLetterColors() {
        LinkedList<String> target = listOfTargetString();

        String[] letters = { "a", "b", "c", "d", "e", "f", "g", "h", "i",
            "j", "k", "l", "m", "n", "o", "p", "q", "r",
            "s", "t", "u", "v", "w", "x", "y", "z" };

        // System.out.println("used letters A: " + usedLetters[0]);
        // Change colors function (called from enter pressed function)

        List<String> indivLetters = new ArrayList<>();
        List<Integer> numTimesEachLetter = new ArrayList<>();
        for (int a = 0; a < curWordLetters.size(); a++) {
            if (!(indivLetters.contains(curWordLetters.get(a)))) {
                indivLetters.add(curWordLetters.get(a));
                numTimesEachLetter.add(1);
            } else {
                // already in list; add 1 to that element's count
                int ind = indivLetters.indexOf(curWordLetters.get(a));
                numTimesEachLetter.set(ind, numTimesEachLetter.get(ind) + 1);
            }
        }

        System.out.println("guessed word: " + curWordLetters);
        System.out.println(
                "indiv letters & num times each letter: " + indivLetters + " --> "
                        + numTimesEachLetter
        );

        // DUPE LETTER CHECK
        // boolean dupeInRightPlace = false;

        // for (int i = 0; i < curWordLetters.size(); i++) {
        // int letterInd =
        // Arrays.asList(letters).indexOf(curWordLetters.get(i).toLowerCase());
        // int ind = indivLetters.indexOf(curWordLetters.get(i));
        // if (numTimesEachLetter.get(ind) > 1) {
        // // 2 or more of this letter in the guess
        // if (curWordLetters.get(i).equals(target.get(i).toLowerCase())) {
        // dupeInRightPlace = true;
        // // CHANGE COLOR TO GREEN
        // System.out.println("DUPE entry, found right place");
        // colors[curRow][i] = 3;
        // updateUsedLetters(letterInd, 3); // keyboard shows green
        // } else {
        //
        // if (target.contains(curWordLetters.get(i))) {
        // System.out.println("DUPE entry but this one is wrong place");
        // // letter is in word in the right place once; other spots should be gray, not
        // yellow
        // colors[curRow][i] = 2; // gray
        // } else {
        // System.out.println("DUPE entry but not in word");
        // // letter not in word at all
        // colors[curRow][i] = 2; // gray
        // if (!(usedLetters[letterInd] == 3)) {
        // // don't update if it was green
        // updateUsedLetters(letterInd, 2);
        // }
        //
        // }
        // }
        // }
        // }

        for (int i = 0; i < curWordLetters.size(); i++) {
            int letterInd = Arrays.asList(letters).indexOf(curWordLetters.get(i).toLowerCase());
            System.out.println(
                    "current letter index: " + letterInd + ", curLetter "
                            + curWordLetters.get(i).toLowerCase()
            );

            // check duplicates if all should be yellow
            // int ind = indivLetters.indexOf(curWordLetters.get(i));
            // if(numTimesEachLetter.get(ind) > 1) {
            // if (!dupeInRightPlace) {
            // System.out.println("DUPE letter in word but never in right place");
            // // all guesses in the wrong place but letter is in the word
            // colors[curRow][i] = 1;
            // if (!(usedLetters[letterInd] == 3)) {
            // // don't update if it was green
            // updateUsedLetters(letterInd, 1);
            // }
            // }
            // } else {
            // System.out.println("in changelettercolors");
            if (curWordLetters.get(i).equals(target.get(i).toLowerCase())) {
                // CHANGE COLOR TO GREEN
                // System.out.println("changing colors to green at " + i);
                colors[curRow][i] = 3;
                updateUsedLetters(letterInd, 3);
            } else if (targetWord.contains(curWordLetters.get(i).toLowerCase())) {
                // CHANGE COLOR TO YELLOW
                // System.out.println("changing colors to yellow at " + i);
                colors[curRow][i] = 1;
                if (!(usedLetters[letterInd] == 3)) {
                    // TODO FIX WHY IS THIS NOT WORKING
                    // don't update if it was green
                    updateUsedLetters(letterInd, 1);
                }
            } else {
                // CHANGE COLOR TO GRAY
                // System.out.println("changing colors to gray at " + i);
                colors[curRow][i] = 2;
                if (!(usedLetters[letterInd] == 3)) {
                    // TODO FIX WHY IS THIS NOT WORKING
                    // don't update if it was green
                    updateUsedLetters(letterInd, 2);
                }
            }
            // }

        }
    }

    public void updateUsedLetters(int letterInd, int colorInd) {
        // System.out.println("updating keyboard");
        // System.out.println("used letters before: " + usedLetters);
        if (letterInd < 0) {
            // System.out.println("in update used letters, negative letter index lol");
            System.out.print("");
            // } else if (!(usedLetters[letterInd] == 3)) {
            // // if already green, don't make it something different
            // System.out.println("usedLetters " + letterInd + " before: " +
            // usedLetters[letterInd]);
            // usedLetters[letterInd] = colorInd;
        } else {
            usedLetters[letterInd] = colorInd;
        }
    }

    public int[] getUsedLetters() {
        return usedLetters;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * checkWinner checks whether the game has reached a win condition.
     * checkWinner only looks for horizontal wins.
     *
     * @return 0 if you haven't won yet, 1 if you win, 2 if you lose
     */
    public int checkWinner() {
        FileWriter writer;

        if (gameOver) {
            gameCounter++;
            if (gameWon) {
                winCounter++;
            }
            try {
                writer = new FileWriter("files/savedCounter.txt", false);
                // System.out.println("game over; gamecounter:" + gameCounter);
                // System.out.println("wincounter:" + winCounter);
                writer.write(Integer.toString(gameCounter));// write new line
                writer.write(Integer.toString(winCounter));// write new line
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (gameWon) {
                return 1;
            } else {
                return 2;
            }
        } else {
            return 0;
        }
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        if (!(curRow == 0 & getCurLetterIndex() == 0)) {
            System.out.println("\n\nTarget word: " + targetWord);
            System.out.println("Word " + (curRow + 1));
            System.out.println("Letter " + (getCurLetterIndex() + 1) + " ");
            for (int k = 0; k < curWordLetters.size(); k++) {
                System.out.print(" " + curWordLetters.get(k) + " ");
            }
        }

    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset(boolean saved) {
        if (!saved) {
            board = new String[6][5];

            curRow = 0;
            curWordLetters = new LinkedList<String>();
            enterPressed = false;

            for (int i = 0; i < usedLetters.length; i++) {
                usedLetters[i] = 0;
            }

            targetWord = generateTargetWord();

            colors = new int[6][5];

            gameOver = false;
            // winCounter = 0;
            // gameCounter = 0;

            try {
                FileWriter writer = new FileWriter("files/savedWordleBoard.txt", false);
                writer.write("");// write new line
                writer.close();
                writer = new FileWriter("files/savedColors.txt", false);
                writer.write("");// write new line
                writer.close();
                writer = new FileWriter("files/savedTarget.txt", false);
                writer.write("");// write new line
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (targetWord == null) {
                targetWord = generateTargetWord();
            }
            if (colors == null) {
                colors = new int[6][5];
                // changeLetterColors();
            }
            // System.out.println("curRow: " + curRow);
            enterPressed = false;
            gameOver = false;
        }

    }

    public void saveExit() {

        // if(gameOver) {
        // reset(false);
        // }

        // use fileIO to save
        try {
            FileWriter writer = new FileWriter("files/savedWordleBoard.txt", false);
            // writer.write("Hello World");

            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++) {
                    if (j > 0) {
                        writer.write(" "); // separation
                    }
                    if (board[i][j] == null) {
                        writer.write("0");
                        // System.out.println("writing 0");
                    } else {
                        writer.write(board[i][j]);
                    }
                }
                // writer.write("!\r\n");// write new line
                writer.write("!");// write new line
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("COULD NOT SAVE BOARD!\n\n\n\n");
        }

        try {
            FileWriter writer = new FileWriter("files/savedTarget.txt", false);
            writer.write(targetWord);
            writer.close();
        } catch (IOException e) {
            System.out.println("COULD NOT SAVE TARGET WORD!\n\n\n\n");
        }

        try {
            FileWriter writer = new FileWriter("files/savedColors.txt", false);
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++) {
                    if (j > 0) {
                        writer.write(" "); // separation
                    }

                    writer.write(Integer.toString((colors[i][j])));
                }
                // writer.write("!\r\n");// write new line
                writer.write("!");// write new line
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("could not save colors");
            // TODO close game board
        }
    }

    // reads in saved file
    public void readSavedBoard() {
        try {
            FileReader reader = new FileReader("files/savedWordleBoard.txt");
            int c;
            int rowCount = 0;
            int letterCount = 0;
            while (((c = reader.read()) != -1) && rowCount < 6) {
                savedBoard = true;
                // System.out.println("C: " + (char) c + ", letterCount " + letterCount);
                String curLetter = Character.toString(((char) c));
                if (curLetter.equals("!")) {
                    // System.out.println("NEWLINENEWLINE\n\n\n");
                    rowCount++;
                    letterCount = 0;
                } else if (!(curLetter.equals(" ") || curLetter.equals(""))) {
                    if (curLetter.equals("0")) {
                        // System.out.println("letter: " + curLetter);
                        board[rowCount][letterCount] = null;
                        letterCount++;
                    } else {
                        // System.out.println("letter: " + curLetter);
                        board[rowCount][letterCount] = curLetter;
                        letterCount++;
                    }
                }
            }

            reader.close();

        } catch (IOException e) {
            System.out.println("COULD NOT READ IN SAVED BOARD!\n\n\n\n");
        }

        try {
            FileReader reader2 = new FileReader("files/savedTarget.txt");
            String t = "";
            int c;
            while (((c = reader2.read()) != -1) && t.length() < 5) {
                t += Character.toString(((char) c));
            }
            targetWord = t;
            reader2.close();
        } catch (Exception e) {
            System.out.println("COULD NOT READ IN SAVED TARGET WORD!\n\n\n\n");
        }

        // colors
        try {
            FileReader reader3 = new FileReader("files/savedColors.txt");
            int c;
            int rowCount = 0;
            int colorCount = 0;
            colors = new int[6][5];
            while (((c = reader3.read()) != -1) && rowCount < 6) {
                savedBoard = true;
                // System.out.println("C: " + c + ", colorCount " + colorCount);
                String curColor = Character.toString(((char) c));
                // System.out.println("CURCOLOR: " + curColor + " vs. c: " + c);

                if (curColor.equals("!")) {
                    // System.out.println("NEWLINENEWLINE\n\n\n");
                    rowCount++;
                    colorCount = 0;
                } else if (!(curColor.equals(" ") || curColor.equals(""))) {
                    if (curColor.equals("0")) {
                        // System.out.println("color 0");
                        colorCount++;
                    } else {
                        // System.out.println("color: " + curColor);
                        colors[rowCount][colorCount] = Integer.valueOf(curColor);
                        colorCount++;
                    }
                }
            }

            try {
                FileReader reader4 = new FileReader("files/savedCounter.txt");
                int count = 0;
                while (((c = reader4.read()) != -1)) {
                    // System.out.println("counter reading in: " + ((char) c));
                    if (count == 0) {
                        gameCounter = c;
                    } else {
                        winCounter = c;
                    }
                    // System.out.println("gameCounter: " + gameCounter);
                    // System.out.println("winCounter: " + winCounter);
                }
                reader4.close();
            } catch (Exception e) {
                System.out.println("COULD NOT READ IN SAVED COUNTERS!\n\n\n\n");
            }

            curRow = 0;
            for (int i = 0; i < 6; i++) {
                if ((board[i][0] != null) && (colors[i][0] != 0)) {
                    // need to check both letters and colors to tell if word was finished or not
                    // row is not empty
                    curRow++;
                }
            }
            // System.out.println("last row index with text: " + curRow);

            curWordLetters = new LinkedList<String>();
            String[] s = board[curRow];
            for (int i = 0; i < s.length; i++) {
                if (!(s[i] == null)) {
                    // System.out.println("cur row " + curRow + " letters: " + curWordLetters);
                    curWordLetters.add(s[i]);
                }
            }

            reader3.close();

        } catch (IOException e) {
            System.out.println("COULD NOT READ IN SAVED BOARD!\n\n\n\n");
        }

    }

    // generates a target word for the new game from the list of txt files
    public String generateTargetWord() {
        try {
            Random rand = new Random();
            int randInt = rand.nextInt(targetWords.size() - 1);

            List<String> wordsList = new ArrayList<>(targetWords);
            // or just store words as an arraylist
            return wordsList.get(randInt).toLowerCase();

        } catch (Exception e) {
            throw new RuntimeException(
                    "generate target word:" +
                            "error picking target word random number"
            );
        }
    }

    /**
     * getCurrentPlayer is a getter for the player
     * whose turn it is in the game.
     * 
     * @return true if it's Player 1's turn,
     *         false if it's Player 2's turn.
     */
    // public boolean getCurrentPlayer() {
    // return player1;
    // }

    /**
     * getCell is a getter for the contents of the cell specified by the method
     * arguments.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return an integer denoting the contents of the corresponding cell on the
     *         game board. 0 = empty, 1 = Player 1, 2 = Player 2
     */
    public String getCell(int r, int c) {
        if (!(r <= curRow)) {
            throw new IllegalArgumentException("row too large");
        }
        if (r == curRow) {
            if (c >= curWordLetters.size()) {
                throw new IllegalArgumentException("column " + c + " too large for row " + r);
            }
        }
        if (r >= 6) {
            throw new IllegalArgumentException("row " + r + " too large");
        }

        return board[r][c];
    }

    // get if enter was just pressed
    public boolean getEnterPressed() {
        return enterPressed;
    }

    public String getTargetWord(boolean testing) {
        if (gameOver || testing) {
            return targetWord;
        } else {
            return ("You are not allowed to see the target word while the game is still going!");
        }
    }

    public void setTargetWord(String t) {
        // for testing purposes
        targetWord = t;
    }

    public boolean getWon() {
        return gameWon;
    }

    public boolean getOver() {
        return gameOver;
    }

    /**
     * This main method illustrates how the model is completely independent of
     * the view and controller. We can play the game from start to finish
     * without ever creating a Java Swing object.
     *
     * This is modularity in action, and modularity is the bedrock of the
     * Model-View-Controller design framework.
     *
     * Run this file to see the output of this method in your console.
     */
    public static void main(String[] args) {
        Wordle t = new Wordle();

        t.playTurn("S");
        t.printGameState();

        t.playTurn("T");
        t.printGameState();

        t.playTurn("A");
        t.printGameState();

        t.playTurn("R");
        t.printGameState();

        t.playTurn("E");
        t.printGameState();

        t.playTurn("\n");
        t.printGameState();

        // should go to next line now
        // and change colors of first line

        // try enter before line is filled
        // try invalid word
        // try typing once line is filled but before pressing enter
        // try deleting when no letters typed yet
        // check colors update correctly

        t.printGameState();
        System.out.println();
        System.out.println();
        System.out.println("Winner is: " + t.checkWinner());
    }
}
