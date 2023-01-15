package org.cis1200.TwentyFortyEight;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class GameTest2048 {

    // Concept: JUnit Testing ================================

    @Test
    public void testMoveLeft() {
        TwentyFortyEight game = new TwentyFortyEight();
        int[][] array = { { 2, 0, 0, 0 },
            { 0, 2, 0, 0 },
            { 0, 0, 4, 0 },
            { 2, 2, 0, 2 } };
        game.board = game.arrayToBoard(array);
        game.moveLeft();
        int[][] afterMove = game.boardToArray(game.board);
        assertEquals(2, afterMove[0][0]);
        assertEquals(2, afterMove[1][0]);
        assertEquals(4, afterMove[2][0]);
        assertEquals(4, afterMove[3][0]);
        assertEquals(2, afterMove[3][1]);
    }

    @Test
    public void testMoveRight() {
        TwentyFortyEight game = new TwentyFortyEight();
        int[][] array = { { 2, 0, 0, 0 },
            { 0, 2, 0, 0 },
            { 0, 0, 4, 0 },
            { 2, 2, 0, 2 } };
        game.board = game.arrayToBoard(array);
        game.moveRight();
        int[][] afterMove = game.boardToArray(game.board);
        assertEquals(2, afterMove[0][3]);
        assertEquals(2, afterMove[1][3]);
        assertEquals(4, afterMove[2][3]);
        assertEquals(4, afterMove[3][3]);
        assertEquals(2, afterMove[3][2]);
    }

    @Test
    public void testMoveUp() {
        TwentyFortyEight game = new TwentyFortyEight();
        int[][] array = { { 2, 0, 0, 0 },
            { 0, 2, 0, 0 },
            { 0, 0, 2, 0 },
            { 0, 0, 0, 2 } };
        game.board = game.arrayToBoard(array);
        game.moveUp();
        int[][] afterMove = game.boardToArray(game.board);
        int[][] expected = { { 2, 2, 2, 2 },
            { 0, 0, 0, 0 },
            { 0, 0, 0, 0 },
            { 0, 0, 0, 0 } };
        // assertTrue(Arrays.deepEquals(afterMove, expected));
        // cannot deepequals the arrays because some tiles are generated after each
        // swipe
        assertEquals(2, afterMove[0][0]);
        assertEquals(2, afterMove[0][1]);
        assertEquals(2, afterMove[0][2]);
        assertEquals(2, afterMove[0][3]);
    }

    @Test
    public void testMoveDown() {
        TwentyFortyEight game = new TwentyFortyEight();
        int[][] array = { { 2, 0, 0, 0 },
            { 0, 64, 0, 0 },
            { 0, 0, 4, 0 },
            { 2, 2, 0, 2 } };
        game.board = game.arrayToBoard(array);
        game.moveDown();
        int[][] afterMove = game.boardToArray(game.board);
        assertEquals(4, afterMove[3][0]);
        assertEquals(2, afterMove[3][1]);
        assertEquals(4, afterMove[3][2]);
        assertEquals(2, afterMove[3][3]);
        assertEquals(64, afterMove[2][1]);
    }

    // edge case
    @Test
    public void testMoveLeftDoubleMerge() {
        TwentyFortyEight game = new TwentyFortyEight();
        int[][] array = { { 4, 4, 4, 4 },
            { 2, 2, 0, 0 },
            { 2, 0, 2, 0 },
            { 2, 2, 2, 2 } };
        game.board = game.arrayToBoard(array);
        game.moveLeft();
        int[][] afterMove = game.boardToArray(game.board);
        assertEquals(8, afterMove[0][0]);
        assertEquals(4, afterMove[1][0]);
        assertEquals(4, afterMove[2][0]);
        assertEquals(4, afterMove[3][0]);
        assertEquals(4, afterMove[3][1]);
        assertEquals(8, afterMove[0][1]);
    }

    @Test
    public void testMoveRightDoubleMerge() {
        TwentyFortyEight game = new TwentyFortyEight();
        int[][] array = { { 4, 4, 4, 4 },
            { 2, 2, 0, 0 },
            { 2, 0, 2, 0 },
            { 2, 2, 2, 2 } };
        game.board = game.arrayToBoard(array);
        game.moveRight();
        int[][] afterMove = game.boardToArray(game.board);
        assertEquals(8, afterMove[0][3]);
        assertEquals(4, afterMove[1][3]);
        assertEquals(4, afterMove[2][3]);
        assertEquals(4, afterMove[3][3]);
        assertEquals(4, afterMove[3][2]);
        assertEquals(8, afterMove[0][2]);
    }

    @Test
    public void testMoveUpDoubleMerge() {
        TwentyFortyEight game = new TwentyFortyEight();
        int[][] array = { { 2, 0, 0, 0 },
            { 2, 0, 0, 0 },
            { 2, 0, 0, 0 },
            { 2, 0, 0, 0 } };
        game.board = game.arrayToBoard(array);
        game.moveUp();
        int[][] afterMove = game.boardToArray(game.board);
        assertEquals(4, afterMove[0][0]);
        assertEquals(4, afterMove[1][0]);
    }

    @Test
    public void testMoveDownDoubleMerge() {
        TwentyFortyEight game = new TwentyFortyEight();
        int[][] array = { { 2, 0, 0, 0 },
            { 2, 0, 0, 0 },
            { 2, 0, 0, 0 },
            { 2, 0, 0, 0 } };
        game.board = game.arrayToBoard(array);
        game.moveDown();
        int[][] afterMove = game.boardToArray(game.board);
        assertEquals(4, afterMove[3][0]);
        assertEquals(4, afterMove[2][0]);
    }

    @Test
    public void testGameEndsWhen2048() {
        TwentyFortyEight game = new TwentyFortyEight();
        int[][] array = { { 1024, 0, 0, 0 },
            { 1024, 0, 0, 0 },
            { 0, 0, 0, 0 },
            { 0, 0, 0, 0 } };
        game.board = game.arrayToBoard(array);
        game.moveUp();
        int[][] afterMove = game.boardToArray(game.board);
        assertEquals(2048, afterMove[0][0]);
        assertEquals(game.status, TwentyFortyEight.GameStatus.gameWon);
    }

    @Test
    public void testUndoOnce() {
        TwentyFortyEight game = new TwentyFortyEight();
        game.moveUp();
        int[][] expected = game.boardToArray(game.board);
        game.moveUp();
        game.undo();
        int[][] undoToExpected = game.boardToArray(game.board);
        assertTrue(Arrays.deepEquals(expected, undoToExpected));
    }

    @Test
    public void testUndoMultiple() {
        TwentyFortyEight game = new TwentyFortyEight();
        int[][] expected = game.boardToArray(game.board);
        game.moveUp();
        game.moveDown();
        game.moveLeft();
        game.moveRight();
        game.undo();
        game.undo();
        game.undo();
        game.undo();
        int[][] undoToExpected = game.boardToArray(game.board);
        assertTrue(Arrays.deepEquals(expected, undoToExpected));
    }

    @Test
    public void testUndoNoMovesYet() {
        TwentyFortyEight game = new TwentyFortyEight();
        int[][] expected = game.boardToArray(game.board);
        game.undo();
        int[][] undoToExpected = game.boardToArray(game.board);
        assertTrue(Arrays.deepEquals(expected, undoToExpected));
    }
}
