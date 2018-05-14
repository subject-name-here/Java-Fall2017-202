package ru.spbau.mit.java.paradov.logic;

import org.junit.Test;
import ru.spbau.mit.java.paradov.data.GameType;

import static org.junit.Assert.*;

/** Tests game logic of work with field. */
public class GameLogicTest {

    /** Tests if empty field isn't full and isn't complete. */
    @Test
    public void testFieldCheck1() {
        Field f = new Field(3, 3);
        assertFalse(GameLogic.checkIfWin(f));
        assertFalse(f.isFull());
    }

    /** Tests if field with only crosses is full and complete. */
    @Test
    public void testFieldCheck2() {
        Field f = new Field(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                f.setCell(i, j, true);
            }
        }
        assertTrue(GameLogic.checkIfWin(f));
        assertTrue(f.isFull());
    }

    /** Tests if diagonal crosses is winning combination and if not full field is completed. */
    @Test
    public void testFieldCheck3() {
        Field f = new Field(3, 3);
        f.setCell(0, 0, true);
        f.setCell(1, 1, true);
        f.setCell(2, 2, true);
        f.setCell(1, 0, false);
        f.setCell(2, 0, false);
        assertTrue(GameLogic.checkIfWin(f));
        assertFalse(f.isFull());
    }

    /** Tests if horizontal crosses is winning combination and if not full field is completed. */
    @Test
    public void testFieldCheck4() {
        Field f = new Field(3, 3);
        f.setCell(0, 0, true);
        f.setCell(0, 1, true);
        f.setCell(0, 2, true);
        f.setCell(1, 0, false);
        f.setCell(2, 0, false);
        assertTrue(GameLogic.checkIfWin(f));
        assertFalse(f.isFull());
    }

    /** Tests if vertical crosses is winning combination and if not full field is completed. */
    @Test
    public void testFieldCheck5() {
        Field f = new Field(3, 3);
        f.setCell(0, 0, true);
        f.setCell(1, 0, true);
        f.setCell(2, 0, true);
        f.setCell(1, 2, false);
        f.setCell(2, 1, false);
        assertTrue(GameLogic.checkIfWin(f));
        assertFalse(f.isFull());
    }

    /** Tests if full field is full, and if it's draw field. */
    @Test
    public void testFieldCheck6() {
        Field f = new Field(3, 3);
        f.setCell(0, 0, true);
        f.setCell(0, 1, true);
        f.setCell(0, 2, false);
        f.setCell(1, 0, false);
        f.setCell(1, 1, false);
        f.setCell(1, 2, true);
        f.setCell(2, 0, true);
        f.setCell(2, 1, true);
        f.setCell(2, 2, false);
        assertFalse(GameLogic.checkIfWin(f));
        assertTrue(f.isFull());
    }

    /** Tests if zeroes can also win on non-full field. */
    @Test
    public void testFieldCheck7() {
        Field f = new Field(3, 3);
        f.setCell(2, 0, false);
        f.setCell(1, 1, false);
        f.setCell(0, 2, false);
        f.setCell(0, 0, true);

        assertTrue(GameLogic.checkIfWin(f));
        assertFalse(f.isFull());
    }

    /** Tests if full field can be back non-full. */
    @Test
    public void testFieldCheck8() {
        Field f = new Field(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                f.setCell(i, j, true);
            }
        }
        assertTrue(f.isFull());
        f.freeCell(0, 0);
        assertFalse(f.isFull());
    }

    /** Tests game types are returning correctly. */
    @Test
    public void testGameType() {
        GameLogic gl1 = new GameLogic("012345_Human", "012345_Human");
        assertEquals(GameType.PVP, gl1.getGameType());

        GameLogic gl2 = new GameLogic("012345_EasyBot", "012345_Human");
        assertEquals(GameType.BVP, gl2.getGameType());

        GameLogic gl3 = new GameLogic("012345_HardBot", "012345_Human");
        assertEquals(GameType.BVP, gl3.getGameType());

        GameLogic gl4 = new GameLogic("012345_EasyBot", "012345_HardBot");
        assertEquals(GameType.BVB, gl4.getGameType());
    }
}