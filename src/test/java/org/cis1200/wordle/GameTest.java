package org.cis1200.wordle;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class GameTest {

    Wordle w = new Wordle();

    @Test
    public void test() {
        assertNotEquals("CIS 120", "CIS 160");
    }

    // @Test
    // public void testSaveOneWord() {
    // w.reset(false);
    // w.playTurn("S");
    // w.playTurn("T");
    // w.playTurn("A");
    // w.playTurn("R");
    // w.playTurn("E");
    // w.playTurn("L"); // shouldn't do anything
    //
    // w.saveExit();
    // // manually checking savedWordleBoard has correct S T A R E
    // // manually checking savedTarget
    // // manually checking savedColors is correct (nothing)
    // }
    //
    // @Test
    // public void testSaveOneEnterOneNot() {
    // w.reset(false);
    // w.playTurn("S");
    // w.playTurn("T");
    // w.playTurn("A");
    // w.playTurn("R");
    // w.playTurn("E");
    // w.playTurn("\n");
    // w.playTurn("P");
    // w.playTurn("I");
    // w.playTurn("N");
    // w.playTurn("C");
    // w.playTurn("H");
    //
    // w.saveExit();
    // // w.reset(true);
    // w.readSavedBoard();
    // // manually checking savedWordleBoard has correct S T A R E ! P I N C H
    // // manually checking savedTarget
    //
    //
    // assertEquals("S", w.getCell(0, 0));
    // assertEquals("H", w.getCell(1, 4));
    // assertEquals(4, w.getCurLetterIndex());
    // // int[][] colors = w.getColors();
    // // manually checking savedColors is correct (only 1 word)
    // // assertEquals("H", w.getCell(1, 4));
    //
    //
    // }
    //
    // @Test
    // public void testSaveTwoWordsEnter() {
    // w.reset(false);
    // w.playTurn("S");
    // w.playTurn("T");
    // w.playTurn("A");
    // w.playTurn("R");
    // w.playTurn("E");
    // w.playTurn("\n");
    // w.playTurn("P");
    // w.playTurn("I");
    // w.playTurn("N");
    // w.playTurn("C");
    // w.playTurn("H");
    // w.playTurn("\n");
    //
    // w.saveExit();
    // // manually checking savedWordleBoard has correct S T A R E ! P I N C H
    // // manually checking savedTarget
    // // manually checking savedColors is correct (only 1 word)
    // }
    //
    //
    // @Test
    // public void testSaveMidWord() {
    // w.reset(false);
    // w.playTurn("S");
    // w.playTurn("T");
    // w.playTurn("A");
    // w.playTurn("R");
    // w.playTurn("E");
    // w.playTurn("\n");
    // w.playTurn("P");
    // w.playTurn("I");
    // w.playTurn("N");
    //
    // w.saveExit();
    // // manually checking savedWordleBoard has correct S T A R E ! P I N
    // // manually checking savedTarget
    // // manually checking savedColors is correct (only 1st word)
    // }

    @Test
    public void testAddLetter() {
        w.reset(false);
        w.playTurn("s");
        assertEquals(1, w.getCurLetterIndex());
        assertEquals("s", w.getCurWord().get(0));
        assertEquals(0, w.getCurRow());
    }

    @Test
    public void testAddWord() {
        w.reset(false);
        w.playTurn("s");
        w.playTurn("t");
        w.playTurn("a");
        w.playTurn("r");
        w.playTurn("e");
        assertEquals(5, w.getCurLetterIndex());
        assertEquals("r", w.getCurWord().get(3));
        assertEquals(0, w.getCurRow());
    }

    @Test
    public void testAddWord6Letter() {
        w.reset(false);
        w.playTurn("s");
        w.playTurn("t");
        w.playTurn("a");
        w.playTurn("r");
        w.playTurn("e");
        w.playTurn("p"); // should do nothing
        assertEquals(5, w.getCurLetterIndex());
        assertEquals("e", w.getCurWord().get(4));
        assertEquals(0, w.getCurRow());
    }

    @Test
    public void testAddNewLine() {
        w.reset(false);
        w.playTurn("s");
        w.playTurn("t");
        w.playTurn("a");
        w.playTurn("r");
        w.playTurn("e");
        // w.playTurn("\n"); // enter
        w.checkWord();
        assertEquals(0, w.getCurLetterIndex());
        // assertEquals("E", w.getCurWord().get(4));
        assertEquals(new ArrayList<String>(), w.getCurWord());
        assertEquals(1, w.getCurRow());
    }

    @Test
    public void testWin() {
        w.reset(false);
        assertFalse(w.getWon());
        assertFalse(w.getOver());
        // String t = w.getTargetWord(true);
        LinkedList<String> tList = w.listOfTargetString();
        for (int i = 0; i < tList.size(); i++) {
            w.playTurn(tList.get(i));
        }
        // w.playTurn("\n"); // enter
        w.checkWord(); // makeshift enter pressed
        // should have won now!
        assertTrue(w.getWon());
        assertTrue(w.getOver());
    }

    @Test
    public void testLoss() {
        w.reset(false);
        assertFalse(w.getWon());
        assertFalse(w.getOver());
        // String t = w.getTargetWord(true);
        LinkedList<String> tList = w.listOfTargetString();
        for (int i = 0; i < 6; i++) {
            w.playTurn("p");
            w.playTurn("l");
            w.playTurn("a");
            if (tList.get(3).equals("n")) {
                w.playTurn("c");
            } else {
                w.playTurn("n");
            }
            w.playTurn("e");

            // w.playTurn("\n"); // enter
            w.checkWord(); // makeshift enter pressed

        }
        // should have lost now! 6 times wrong word
        assertFalse(w.getWon());
        System.out.println(tList);
        assertTrue(w.getOver());
    }

    @Test
    public void testInvalidWordEnterDoesNotWork() {
        w.reset(false);
        // String t = w.getTargetWord(true);
        w.playTurn("a");
        w.playTurn("a");
        w.playTurn("a");
        w.playTurn("a");
        w.playTurn("a");
        w.checkWord(); // makeshift enter pressed; invalid word, should stay on curRow is 0
        assertEquals(0, w.getCurRow());
        assertEquals(5, w.getCurLetterIndex());
        assertEquals("a", w.getCurWord().get(0));
        assertEquals("a", w.getCurWord().get(4));

        w.deleteLetter();
        w.deleteLetter();
        w.deleteLetter();
        w.deleteLetter();
        // should just be A now
        assertEquals(0, w.getCurRow());
        assertEquals(1, w.getCurLetterIndex());
        assertEquals("a", w.getCurWord().get(0));
        w.playTurn("f");
        w.playTurn("t");
        w.playTurn("e");
        w.playTurn("r");
        w.checkWord(); // enter
        System.out.println(w.getCurWord());
        assertEquals(0, w.getCurLetterIndex());
        assertEquals(1, w.getCurRow());
    }

    @Test
    public void testEnterMidWordDoesNotChange() {
        w.reset(false);
        // String t = w.getTargetWord(true);
        w.playTurn("a");
        w.playTurn("a");
        w.playTurn("a");
        w.checkWord(); // makeshift enter pressed; invalid word, should stay on curRow is 0
        assertEquals(0, w.getCurRow());
        assertEquals(3, w.getCurLetterIndex());
        assertEquals("a", w.getCurWord().get(0));
        assertEquals("a", w.getCurWord().get(2));

    }

    @Test
    public void testReset() {
        w.reset(false);
        w.playTurn("s");
        w.playTurn("t");
        w.playTurn("a");
        w.playTurn("r");
        w.playTurn("e");
        assertEquals("r", w.getCurWord().get(3));
        w.checkWord();
        w.playTurn("l");
        assertEquals(1, w.getCurLetterIndex());
        assertEquals(1, w.getCurRow());
        w.reset(false);
        assertEquals(0, w.getCurLetterIndex());
        assertEquals(0, w.getCurRow());
        assertTrue(w.getCurWord().isEmpty());

    }

    @Test
    public void testColorsUpdate() {
        w.reset(false);
        w.playTurn("s");
        w.playTurn("t");
        w.playTurn("a");
        w.playTurn("r");
        w.playTurn("e");
        assertEquals("r", w.getCurWord().get(3));
        int[][] c = w.getColors();

        LinkedList<String> t = w.listOfTargetString();
        ArrayList<Integer> colorsExp = new ArrayList<>();
        for (int i = 0; i < t.size(); i++) {
            if (t.get(i).equals(w.getCurWord().get(i))) {
                colorsExp.add(3); // green
            } else if (t.contains(w.getCurWord().get(i))) {
                colorsExp.add(1); // yellow
            } else {
                colorsExp.add(2); // gray
            }
        }

        w.checkWord();
        assertEquals(c[0][0], colorsExp.get(0));
        assertEquals(c[0][1], colorsExp.get(1));
        assertEquals(c[0][4], colorsExp.get(4));
    }

    @Test
    public void testColorsCorrectPlace() {
        w.reset(false);


        LinkedList<String> t = w.listOfTargetString();
        ArrayList<Integer> colorsExp = new ArrayList<>();
        for (int i = 0; i < t.size(); i++) {
            w.playTurn(t.get(i));
            colorsExp.add(3);
        }

        w.checkWord();
        int[][] c = w.getColors();
        assertEquals(c[0][0], colorsExp.get(0));
        assertEquals(c[0][1], colorsExp.get(1));
        assertEquals(c[0][2], colorsExp.get(2));
        assertEquals(c[0][3], colorsExp.get(3));
        assertEquals(c[0][4], colorsExp.get(4));
    }

    @Test
    public void testColorsSomeCorrectPlace() {
        w.reset(false);
        int[][] c = w.getColors();

        w.setTargetWord("apple");
        ArrayList<Integer> colorsExp = new ArrayList<>();
        w.playTurn("p"); // yellow
        w.playTurn("l"); // yellow
        w.playTurn("a"); // yellow
        w.playTurn("c"); // gray
        w.playTurn("e"); // green
        w.checkWord(); // makeshift enter pressed
        assertEquals(c[0][0], 1);
        assertEquals(c[0][1], 1);
        assertEquals(c[0][2], 1);
        assertEquals(c[0][3], 2);
        assertEquals(c[0][4], 3);

        w.playTurn("a"); // green
        w.playTurn("n"); // gray
        w.playTurn("k"); // gray
        w.playTurn("l"); // green
        w.playTurn("e"); // green
        w.checkWord(); // makeshift enter pressed

        assertEquals(c[1][0], 3);
        assertEquals(c[1][1], 2);
        assertEquals(c[1][2], 2);
        assertEquals(c[1][3], 3);
        assertEquals(c[1][4], 3);
    }

}