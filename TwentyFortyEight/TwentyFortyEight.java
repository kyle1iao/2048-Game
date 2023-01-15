package org.cis1200.TwentyFortyEight;

/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

/**
 * This class is a model for 2048.
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
public class TwentyFortyEight {

    // Defining all fields needed to implement 2048
    enum GameStatus {
        gameStart, inProgress, gameOver, gameWon
    }

    private LinkedList<Integer> scoreList = new LinkedList<>();
    private LinkedList<int[][]> boardList = new LinkedList<>();
    static int score;
    static int highScore;
    static int highestValueTile;
    private boolean checkingForMoves;

    GameTile[][] board;
    GameStatus status = GameStatus.gameStart;

    /**
     * Constructor sets up game state.
     */
    public TwentyFortyEight() {
        start2048Game();
    }

    // Concepts: 2D Arrays and Collections ================================
    public void start2048Game() {
        this.score = 0;
        scoreList = new LinkedList<>();
        scoreList.add(score);

        this.highScore = 0;
        this.highestValueTile = 0;
        this.status = GameStatus.inProgress;

        board = new GameTile[4][4];

        // drop two tiles for start of the game
        addNewTile();
        addNewTile();

        // initialize board list and add starting board
        boardList = new LinkedList<>();
        boardList.add(boardToArray(board));
    }

    public GameStatus getStatus() {
        return this.status;
    }

    public void undo() {
        if (scoreList.size() != 1) {
            scoreList.removeLast();
            boardList.removeLast();
            // tile board = new last elem
            this.score = scoreList.peekLast();
            this.board = arrayToBoard(boardList.peekLast());
        }
    }

    private void addNewTile() {
        // while loop to pick a random tile to generate a new value at
        Random randomNumGen = new Random();
        int row = randomNumGen.nextInt(4);
        int col = randomNumGen.nextInt(4);
        while (board[row][col] != null) {
            row = randomNumGen.nextInt(4);
            col = randomNumGen.nextInt(4);
        }

        // generating random tile value
        int[] possibleValueArray = { 2, 4 };
        int randomIndex = new Random().nextInt(possibleValueArray.length);
        int newValue = possibleValueArray[randomIndex];
        board[row][col] = new GameTile(newValue);
    }

    /**
     * Game movement logic section
     */

    /**
     * Logic behind if a left key press is inputted.
     * 
     * @return true if the board has changed at all, false otherwise.
     */
    boolean moveLeft() {
        boolean gameChanged = false;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (board[row][col] != null) {
                    int nextRow = row;
                    int nextCol = col - 1;

                    while (nextRow >= 0 && nextRow < 4 && nextCol >= 0 && nextCol < 4) {
                        GameTile nextGameTile = board[nextRow][nextCol];
                        GameTile currentGameTile = board[row][col];

                        // case where the left tile is empty
                        if (nextGameTile == null) {
                            // uses boolean to continuously move tiles until there are no more moves
                            // available in a certain direction
                            if (checkingForMoves) {
                                return true;
                            }
                            board[nextRow][nextCol] = currentGameTile;
                            board[row][col] = null;

                            row = nextRow;
                            col = nextCol;
                            nextCol -= 1;
                            gameChanged = true;

                        } else if (nextGameTile.canCombineWith(currentGameTile)) {
                            if (checkingForMoves) {
                                return true;
                            }

                            int newValue = nextGameTile.combineWith(currentGameTile);
                            score += newValue;
                            if (score > highScore) {
                                highScore = score;
                            }
                            if (newValue > highestValueTile) {
                                highestValueTile = newValue;
                            }
                            board[row][col] = null;
                            gameChanged = true;
                            break;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return gameChangeReset(gameChanged);
    }

    /**
     * Logic behind if a right key press is inputted.
     * 
     * @return true if the board has changed at all, false otherwise.
     */
    boolean moveRight() {
        boolean gameChanged = false;

        for (int row = 3; row >= 0; row--) {
            for (int col = 3; col >= 0; col--) {
                if (board[row][col] != null) {

                    int nextRow = row;
                    int nextCol = col + 1;

                    while (nextRow >= 0 && nextRow < 4 && nextCol >= 0 && nextCol < 4) {
                        GameTile nextGameTile = board[nextRow][nextCol];
                        GameTile currentGameTile = board[row][col];

                        // case where the left tile is empty
                        if (nextGameTile == null) {
                            if (checkingForMoves) {
                                return true;
                            }
                            board[nextRow][nextCol] = currentGameTile;
                            board[row][col] = null;

                            row = nextRow;
                            col = nextCol;
                            nextCol += 1;
                            gameChanged = true;

                        } else if (nextGameTile.canCombineWith(currentGameTile)) {
                            if (checkingForMoves) {
                                return true;
                            }

                            int newValue = nextGameTile.combineWith(currentGameTile);
                            score += newValue;
                            if (score > highScore) {
                                highScore = score;
                            }
                            if (newValue > highestValueTile) {
                                highestValueTile = newValue;
                            }
                            board[row][col] = null;
                            gameChanged = true;
                            break;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return gameChangeReset(gameChanged);
    }

    /**
     * Logic behind if an up key press is inputted.
     * 
     * @return true if the board has changed at all, false otherwise.
     */
    boolean moveUp() {
        boolean gameChanged = false;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (board[row][col] != null) {
                    int nextRow = row - 1;
                    int nextCol = col;

                    while (nextRow >= 0 && nextRow < 4 && nextCol < 4) {
                        GameTile nextGameTile = board[nextRow][nextCol];
                        GameTile currentGameTile = board[row][col];

                        // case where the left tile is empty
                        if (nextGameTile == null) {
                            if (checkingForMoves) {
                                return true;
                            }
                            board[nextRow][nextCol] = currentGameTile;
                            board[row][col] = null;

                            row = nextRow;
                            col = nextCol;
                            nextRow -= 1;
                            gameChanged = true;

                        } else if (nextGameTile.canCombineWith(currentGameTile)) {
                            if (checkingForMoves) {
                                return true;
                            }

                            int newValue = nextGameTile.combineWith(currentGameTile);
                            score += newValue;
                            if (score > highScore) {
                                highScore = score;
                            }
                            if (newValue > highestValueTile) {
                                highestValueTile = newValue;
                            }
                            board[row][col] = null;
                            gameChanged = true;
                            break;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return gameChangeReset(gameChanged);
    }

    /**
     * Logic behind if a down key press is inputted.
     * 
     * @return true if the board has changed at all, false otherwise.
     */
    boolean moveDown() {
        boolean gameChanged = false;

        for (int row = 3; row >= 0; row--) {
            for (int col = 3; col >= 0; col--) {
                if (board[row][col] != null) {
                    int nextRow = row + 1;
                    int nextCol = col;

                    while (nextRow >= 0 && nextRow < 4 && nextCol < 4) {
                        GameTile nextGameTile = board[nextRow][nextCol];
                        GameTile currentGameTile = board[row][col];

                        // case where the left tile is empty
                        if (nextGameTile == null) {
                            if (checkingForMoves) {
                                return true;
                            }
                            board[nextRow][nextCol] = currentGameTile;
                            board[row][col] = null;

                            row = nextRow;
                            col = nextCol;
                            nextRow += 1;
                            gameChanged = true;

                        } else if (nextGameTile.canCombineWith(currentGameTile)) {
                            if (checkingForMoves) {
                                return true;
                            }

                            int newValue = nextGameTile.combineWith(currentGameTile);
                            score += newValue;
                            if (score > highScore) {
                                highScore = score;
                            }
                            if (newValue > highestValueTile) {
                                highestValueTile = newValue;
                            }
                            board[row][col] = null;
                            gameChanged = true;
                            break;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return gameChangeReset(gameChanged);
    }

    /**
     * Resets the booleans for each tile so that game can progress
     * and player can continue swiping. Also adds board to list of boards and
     * score to list of scores.
     * If the player reaches 2048, then the game ends and they have won.
     * 
     * @return true if the board has changed at all, false otherwise.
     */
    private boolean gameChangeReset(boolean gameChanged) {
        if (gameChanged) {
            if (highestValueTile == 2048) {
                status = GameStatus.gameWon;
            } else {
                // reset combined boolean for each tile changed
                for (GameTile[] row : board) {
                    for (GameTile t : row) {
                        if (t != null) {
                            t.setCombined(false);
                        }
                    }
                }
                addNewTile();
                boardList.add(boardToArray(board));
                scoreList.add(score);
                if (!canMove()) {
                    status = GameStatus.gameOver;
                }
            }
        }
        return gameChanged;
    }

    boolean canMove() {
        checkingForMoves = true;
        boolean hasMoves = moveLeft() || moveRight() || moveUp() || moveDown();
        checkingForMoves = false;
        return hasMoves;
    }

    // Concept: FILE I/O ================================

    public void save() {
        try {
            // clear file for each save
            PrintWriter writer = new PrintWriter("SavedGame.txt");
            writer.print("");
            writer.close();

            // write score and high score into file first
            FileWriter fw = new FileWriter("SavedGame.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(this.score);
            bw.write(this.highScore);

            // write 2D array into save file
            int[][] storedArray = boardToArray(board);
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    bw.write(storedArray[row][col]);
                }
            }
            // close file once done with it
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        try {
            FileReader fr = new FileReader("SavedGame.txt");
            BufferedReader br = new BufferedReader(fr);
            // read score and high score first
            score = br.read();
            highScore = br.read();
            int[][] storedArray = new int[4][4];

            // read in values for each tile based on how they were originally wrote into
            // file
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    storedArray[row][col] = br.read();
                }
            }
            br.close();
            status = GameStatus.inProgress;
            board = arrayToBoard(storedArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ==================================================

    // helper methods for implementation and testing
    int[][] boardToArray(GameTile[][] gameBoard) {
        int[][] storedArray = new int[4][4];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                GameTile t = gameBoard[row][col];
                if (t != null) {
                    storedArray[row][col] = t.getTileValue();
                } else {
                    storedArray[row][col] = 0;
                }
            }
        }
        return storedArray;
    }

    GameTile[][] arrayToBoard(int[][] storedArray) {
        GameTile[][] gameBoard = new GameTile[4][4];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                GameTile t;
                int v = storedArray[row][col];
                if (v != 0) {
                    t = new GameTile(v);
                } else {
                    t = null;
                }
                gameBoard[row][col] = t;
            }
        }
        return gameBoard;
    }

    public static void main(String[] args) {
        TwentyFortyEight newGame = new TwentyFortyEight();
        int[][] board = newGame.boardToArray(newGame.board);
        System.out.println(Arrays.deepToString(board));

        newGame.moveLeft();
        board = newGame.boardToArray(newGame.board);
        System.out.println(Arrays.deepToString(board));
        System.out.println(newGame.score);

        newGame.moveDown();
        board = newGame.boardToArray(newGame.board);
        System.out.println(Arrays.deepToString(board));
        System.out.println(newGame.score);

        newGame.moveRight();
        board = newGame.boardToArray(newGame.board);
        System.out.println(Arrays.deepToString(board));
        System.out.println(newGame.score);

        newGame.moveUp();
        board = newGame.boardToArray(newGame.board);
        System.out.println(Arrays.deepToString(board));
        System.out.println(newGame.score);

        newGame.moveUp();
        board = newGame.boardToArray(newGame.board);
        System.out.println(Arrays.deepToString(board));
        System.out.println(newGame.score);
    }
}