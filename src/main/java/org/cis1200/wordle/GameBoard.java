package org.cis1200.wordle;

/*
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

/**
 * This class instantiates a TicTacToe object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Wordle wordle; // model for the game
    private JLabel status; // current status text
    private String statusMessage;

    // private int curLetterInd;

    // Game constants
    public static final int BOARD_WIDTH = 620;
    public static final int BOARD_HEIGHT = 1200;

    public static final String[] LETTERS = {
        "a", "b", "c", "d", "e", "f", "g", "h", "i",
        "j", "k", "l", "m", "n", "o", "p", "q", "r",
        "s", "t", "u", "v", "w", "x", "y", "z"
    };

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        wordle = new Wordle(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        wordle.printGameState();

        // listens for key presses

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() >= 65 && e.getKeyCode() <= 90) {
                    int i = e.getKeyCode() - 65;
                    String letter = LETTERS[i];
                    wordle.playTurn(letter);
                    // curLetterInd = i;
                    // updateStatus();
                    // repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    wordle.checkWord();
                    // curLetterInd = -1;
                    // updateStatus();
                    // repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    wordle.deleteLetter();
                    // curLetterInd = -1;
                    // updateStatus();
                    // repaint();
                }

                repaint();
                updateStatus();

            }

            // public void keyReleased(KeyEvent e) {
            // square.setVx(0);
            // square.setVy(0);
            // }

        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset(boolean saved) {
        wordle.reset(saved);
        status.setText("");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void saveExit() {
        wordle.saveExit();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        // if not enough letters, update message
        // if invalid word, update message
        wordle.printGameState();

        int winner = wordle.checkWinner();

        if (winner == 1) { // won
            statusMessage = "You win!";
            status.setText("You win!");
            endPopup(new JFrame(), "You win!!!");
        } else if (winner == 2) { // lost
            statusMessage = "You lose! \nThe word was: " + wordle.getTargetWord(true).toUpperCase();
            status.setText("You lose!\nThe word was: " + wordle.getTargetWord(false).toUpperCase());
            endPopup(new JFrame(), "You lost :(");
        } else { // still going
            statusMessage = wordle.getStatusMessage();
            status.setText(statusMessage);
        }

    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void popUp(JFrame frame) {

        // JOptionPane pane = new JOptionPane( "information",
        // JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(
                frame,
                "Welcome to Wordle! " +
                        "\n\nIn this game, you have 6 tries to guess a 5-letter goal word." +
                        "\n\nTo enter your guess, type in letters using your keyboard " +
                        "and press enter to submit." +
                        "\nIf your guess is a valid 5-letter word, each letter " +
                        "in the word will change " +
                        "colors after you press enter based on similarity to the goal word. " +
                        "\nIf a letter is in the correct location for the goal word," +
                        " it will turn green." +
                        "\nIf a letter is in the goal word but in the incorrect location," +
                        " it will turn yellow." +
                        "If a letter is not in the goal word, it will turn gray." +
                        "\nThe keyboard at the bottom will help show you which letters you " +
                        "have used that are in the goal word in the correct place, in " +
                        "the goal word in the incorrect place, or not " +
                        "in the goal word." +
                        "\n\nIf you would like to save your progress and return later, " +
                        "press the Save button above." +
                        "\nIf you would like to reset the game, press the Reset button." +
                        "\n\nGood luck, and hope you enjoy!"
        );

    }

    public void endPopup(JFrame frame, String m) {
        // frame.setDefaultCloseOperation();
        JOptionPane.showMessageDialog(frame, m);

    }

    public void drawSquare(Graphics g, int i, int j, int vOff, int hOff, int size) {
        g.drawLine(hOff + size * i, vOff + size * j, hOff + size * i, vOff + size * (j + 1));
        // 0, 0 --> 0, 100
        g.drawLine(
                hOff + size * (i + 1), vOff + size * j, hOff + size * (i + 1), vOff + size * (j + 1)
        );
        // 100, 0 --> 100, 100
        g.drawLine(hOff + size * i, vOff + size * j, hOff + size * (i + 1), vOff + size * j);
        // 0, 0 --> 100, 0
        g.drawLine(
                hOff + size * i, vOff + size * (j + 1), hOff + size * (i + 1), vOff + size * (j + 1)
        );
        // 0, 100 --> 100, 100
    }

    public void drawKeyboard(Graphics g) {
        int hOff = 140;
        int vOff = 555;
        int width = 30;
        int height = 45;
        int fontSize = 15;

        g.setFont(new Font("Helvetica", Font.BOLD, fontSize));

        String[][] keyboardLetters = { { "q", "w", "e", "r", "t", "y", "u", "i", "o", "p" },
            { "a", "s", "d", "f", "g", "h", "j", "k", "l" },
            { "z", "x", "c", "v", "b", "n", "m" }
        };

        int imax = 10;
        for (int j = 0; j < 3; j++) { // row
            if (j == 0) {
                imax = 10;
            } else if (j == 1) {
                imax = 9;
                hOff -= 10;
            } else if (j == 2) {

                imax = 7;
            }
            for (int i = 0; i < imax; i++) { // col
                g.setColor(Color.gray);
                g.drawRect(hOff + (width * i), vOff + (height * j), width, height);
                g.setColor(Color.black);
                if (keyboardLetters[j][i].toUpperCase().equals("I")) {
                    g.drawString(
                            keyboardLetters[j][i].toUpperCase(), hOff + width * i + 10 + 3,
                            vOff + height * j + 25
                    );
                } else if (keyboardLetters[j][i].toUpperCase().equals("W")) {
                    g.drawString(
                            keyboardLetters[j][i].toUpperCase(), hOff + width * i + 10 - 1,
                            vOff + height * j + 25
                    );
                } else {
                    g.drawString(
                            keyboardLetters[j][i].toUpperCase(), hOff + width * i + 10,
                            vOff + height * j + 25
                    );
                }

                hOff += 5;
            }
            hOff -= 15;
            vOff += 5;
            if (j == 1) {
                hOff += 10;
            }

        }
    }

    public void fillKeyboard(Graphics g, int letterNumber, int color) {
        int hOff = 140;
        int vOff = 555;
        int width = 30;
        int height = 45;
        int fontSize = 15;

        // System.out.println("keyboard: letterNumber " + letterNumber);

        String[][] keyboardLetters = { { "q", "w", "e", "r", "t", "y", "u", "i", "o", "p" },
            { "a", "s", "d", "f", "g", "h", "j", "k", "l" },
            { "z", "x", "c", "v", "b", "n", "m" }
        };
        int[][] keyboardInts = { { 16, 22, 4, 17, 19, 24, 20, 8, 14, 15 },
            { -1, 0, 18, 3, 5, 6, 7, 9, 10, 11 },
            { -1, -1, 25, 23, 2, 21, 1, 13, 12, -1 }
        };

        int row = 0;
        int col = 0;
        boolean letterFound = false;
        int imax = 10;
        for (int j = 0; j < 3; j++) { // row

            for (int i = 0; i < imax; i++) { // col
                if (letterFound) {
                    break;
                }
                // System.out.println("j, i" + j + ", " + i);

                if (keyboardInts[j][i] == letterNumber) {
                    row = j;
                    col = i;
                    // System.out.println("letter to fill: " + keyboardLetters[j][i]);
                    letterFound = true;
                    break;
                }
            }

        }

        Color myYellow = new Color(198, 178, 81);
        Color myGreen = new Color(91, 174, 93);

        if (row == 2) {
            hOff -= 5;
        } else if (row == 1) {
            hOff -= 10;
        }
        int y = (vOff + (height * row) + (row * 5));
        int x = (hOff + (5 * col) + (width * col));
        if (color == 3) {
            // System.out.println("green at letter " + keyboardLetters[row][col] + ", pos "
            // + x + ", " + y);
            g.setColor(myGreen);
            g.fillRect(x, y, width, height);
        } else if (color == 1) {
            // System.out.println("yellow at letter " + keyboardLetters[row][col] + ", pos "
            // + x + ", " + y);
            g.setColor(myYellow);
            g.fillRect(x, y, width, height);
        } else if (color == 2) {
            // System.out.println("gray at letter " + keyboardLetters[row][col] + ", pos "
            // + x + ", " + y);
            g.setColor(Color.gray);
            g.fillRect(x, y, width, height);
        }
        drawKeyboard(g);
        // if (row == 2) {
        // hOff -= 20;
        // }

    }

    /**
     * Draws the game board.
     * 
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // System.out.println("\n\nin paint");

        Color myYellow = new Color(198, 178, 81);
        Color myGreen = new Color(91, 174, 93);
        Color myGray = new Color(91, 174, 93);

        g.setColor(Color.black);

        int fontSize = 50;
        int xPos = BOARD_WIDTH - (fontSize * 9) + 23;
        g.setFont(new Font("Helvetica", Font.BOLD, 40));
        g.drawString("W O R D L E", xPos, 50);

        // draw board grid
        // for (int i = 100; i <= 650; i += 105) {
        // g.drawLine(i, 100, i, 730);
        // if (i <= 550) {
        // g.drawLine(i+100, 100, i+100, 730);
        // }
        //
        // }
        // for (int j = 100; j <= 750; j += 105) {
        // g.drawLine(100, j, 625, j);
        //
        // if (j <= 650) {
        // g.drawLine(100, j+100, 625, j+100);
        // }
        // }

        int vertOffset = 0;
        int horOffset = 50;
        for (int i = 1; i < 6; i++) {
            vertOffset = 0;
            for (int j = 1; j < 7; j++) {

                if (j < (wordle.getCurRow() + 1)) {
                    g.setColor(Color.darkGray);
                } else if ((j == (wordle.getCurRow() + 1)) && (i <= (wordle.getCurLetterIndex()))) {
                    g.setColor(Color.darkGray);
                } else {
                    g.setColor(Color.lightGray);
                }
                drawSquare(g, i, j, vertOffset, horOffset, 70);
                vertOffset += 7;
            }
            horOffset += 7;
        }

        // keyboard
        drawKeyboard(g);

        if (!((wordle.getCurLetterIndex() == 0) && wordle.getCurRow() == 0)) {
            // System.out.println("curRow " + wordle.getCurRow() + ", letter " +
            // wordle.getCurLetterIndex());
            // Draws letter
            int curRow = wordle.getCurRow();
            // int[] curRowColors = wordle.getCurRowColors();
            int[][] colors = wordle.getColors();

            for (int i = 0; i < 6; i++) { // rows
                for (int j = 0; j < 5; j++) { // columns
                    // if (i == curRow) {
                    // System.out.println("in row " + i + ", printing " + wordle.getCell(i, j));
                    // }
                    // System.out.println("COLORS AT " + i + ", " + j + ": " + colors[i][j]);

                    String state;
                    try {
                        state = wordle.getCell(i, j);
                        if (state == null) {
                            state = "";
                        }
                        // System.out.println("got letter " + state + ", row " + i + ", col " + j);
                    } catch (Exception e) {
                        // empty
                        // System.out.println("empty cell at " + i + ", " + j);
                        state = "";
                    }

                    int letterFillSize = 72;

                    if (i <= curRow) { // && wordle.getCurLetterIndex() > j) {
                        // write out letters
                        // board x start goes from 100 to 500
                        // board y start goes from 200 to 700
                        int cellX = 120 + (77 * j);
                        int cellY = 70 + (77 * i);

                        // cell colors change once word is entered
                        if (wordle.getEnterPressed()) {
                            // System.out.println("time to change colors!");

                            // System.out.println("target word: " + wordle.getTargetWord(true));

                            if (colors[i][j] == 3) {
                                // System.out.println("GREEN");
                                g.setColor(myGreen);
                                g.fillRect(cellX - 1, cellY - 1, letterFillSize, letterFillSize);
                            } else if (colors[i][j] == 1) {
                                // System.out.println("YELLOW");
                                g.setColor(myYellow);
                                g.fillRect(cellX - 1, cellY - 1, letterFillSize, letterFillSize);
                            } else if (colors[i][j] == 2) {
                                // System.out.println("GRAY");
                                g.setColor(Color.gray);
                                g.fillRect(cellX - 1, cellY - 1, letterFillSize, letterFillSize);
                            }

                            if (state != "" && state != null) {
                                // System.out.println(Arrays.asList(letters));
                                // System.out.println("state: " + state.toLowerCase());
                                fillKeyboard(
                                        g, Arrays.asList(LETTERS).indexOf(state.toLowerCase()),
                                        colors[i][j]
                                );

                            }

                        } else {
                            // System.out.println("change colors ONLY PREVIOUS");
                            // System.out.println(colors[i][j] + " at " + i + ", " + j);

                            if (i < curRow) {
                                if (colors[i][j] == 3) {
                                    // System.out.println("GREEN");
                                    g.setColor(myGreen);
                                    g.fillRect(
                                            cellX - 1, cellY - 1, letterFillSize, letterFillSize
                                    );
                                } else if (colors[i][j] == 1) {
                                    // System.out.println("YELLOW");
                                    g.setColor(myYellow);
                                    g.fillRect(
                                            cellX - 1, cellY - 1, letterFillSize, letterFillSize
                                    );
                                } else if (colors[i][j] == 2) {
                                    // System.out.println("GRAY");
                                    g.setColor(Color.gray);
                                    g.fillRect(
                                            cellX - 1, cellY - 1, letterFillSize, letterFillSize
                                    );
                                }
                                fillKeyboard(
                                        g, Arrays.asList(LETTERS).indexOf(state.toLowerCase()),
                                        colors[i][j]
                                );
                            }
                        }

                        // draw letter
                        // System.out.println("ready to print letter " + state + " at "
                        // + (20 + cellX) + ", " + (80 + cellY));
                        if (i < curRow) {
                            g.setColor(Color.white);
                        } else {
                            g.setColor(Color.black);
                        }

                        g.setFont(new Font("Helvetica", Font.BOLD, 40));
                        if (state.toUpperCase().equals("I")) {
                            g.drawString(state.toUpperCase(), (22 + cellX + 8), (49 + cellY));
                        } else if (state.toUpperCase().equals("W")) {
                            g.drawString(state.toUpperCase(), (22 + cellX - 4), (49 + cellY));
                        } else {
                                g.drawString(state.toUpperCase(), (22 + cellX), (49 + cellY));
                        }


                    }
                    g.setColor(Color.lightGray);
                }
            }
        } // else do nothing there are no letters yet

    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
