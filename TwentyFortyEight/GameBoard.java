package org.cis1200.TwentyFortyEight;

/*
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This class instantiates a 2048 object, which is the model for the game.
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

    private TwentyFortyEight tfe; // model for the game
    private JLabel status; // current status text

    // Game constants
    public static final int BOARD_WIDTH = 700;
    public static final int BOARD_HEIGHT = 900;

    static Color color2 = new Color(240, 230, 220);
    static Color color4 = new Color(240, 220, 200);
    static Color color8 = new Color(240, 180, 120);
    static Color color16 = new Color(250, 150, 100);
    static Color color32 = new Color(250, 120, 100);
    static Color color64 = new Color(250, 90, 60);
    static Color color128 = new Color(240, 210, 110);
    static Color color256 = new Color(240, 210, 100);
    static Color color512 = new Color(240, 200, 80);
    static Color color1024 = new Color(240, 200, 60);
    static Color color2048 = new Color(240, 190, 50);

    public void instructions() {
        JFrame frame = new JFrame();
        JDialog dialogue = new JDialog(frame);
        dialogue.setLayout(new FlowLayout());
        dialogue.setBounds(300, 100, 500, 475);

        String s = "<html><body width='%1s'><h1>Welcome to 2048!</h1>\n" +
                "<h2>Game description:</h2>\n" +
                "<p>2048 is a game where you try to combine tiles to reach 2048.</p>\n" +
                "<h2>Instructions: </h2>\n" +
                "<p>Use your arrow keys to move tiles in the " +
                "corresponding direction. " +
                "Tiles with the same value merge when they are " +
                "adjacent " +
                "and move in the same direction. " +
                "Continuously add up tiles until you reach 2048, " +
                "or until you have no more moves available.</p>\n" +
                "<p><br></p>" +
                "<p>Press &quot;save game&quot; to save your current game. " +
                "Press &quot;load game&quot; to load your save " +
                "and continue where you left off. " +
                "Note that there is no point in saving a lost game!" +
                "Press &quot;undo&quot; to undo your last move as " +
                "long as you have not already lost.</p>\n" +
                "<p><br></p>" +
                "<p>Enjoy the game, and good luck getting 2048!</p>\n" +
                "<p><br></p>";

        JLabel jl = new JLabel(String.format(s, 300));
        dialogue.add(jl);
        dialogue.setVisible(true);
        repaint();
        requestFocusInWindow();
    }

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        tfe = new TwentyFortyEight(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        /*
         * Listens for keyboard presses. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        tfe.moveLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        tfe.moveRight();
                        break;
                    case KeyEvent.VK_UP:
                        tfe.moveUp();
                        break;
                    case KeyEvent.VK_DOWN:
                        tfe.moveDown();
                        break;
                    default:
                        throw new IllegalArgumentException("funky");
                }
                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        tfe.start2048Game();
        status.setText("Start game!");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Undoes one move.
     */
    public void undo() {
        if (tfe.status == TwentyFortyEight.GameStatus.inProgress) {
            tfe.undo();
            status.setText("Undid move");
            repaint();
            requestFocusInWindow();
        } else {
            status.setText("Can't undo lost game. Try again!");
        }
    }

    /**
     * Saves the game.
     */
    public void save() {
        if (tfe.status == TwentyFortyEight.GameStatus.inProgress) {
            tfe.save();
            status.setText("Game saved to SavedGame.txt.");
            repaint();
            requestFocusInWindow();
        } else {
            status.setText("No point in saving a lost game. Try again!");
        }
    }

    /**
     * Resumes game from save file.
     */
    public void resume() {
        tfe.resume();
        status.setText("Game loaded from SavedGame.txt.");
        repaint();
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        switch (tfe.getStatus()) {
            case gameWon:
                status.setText("You got 2048!");
                break;
            case gameOver:
                status.setText("You lost! Try again?");
                break;
            case inProgress:
                status.setText("Game in progress...");
                break;
            default:
                throw new IllegalArgumentException("hi");
        }
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
        g.setFont(new Font("Times New Roman", Font.BOLD, 30));
        g.drawString("2048!", 325, 90);
        g.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        g.drawString("Current Score: " + tfe.score, 155, 540);
        g.drawString("High Score: " + tfe.highScore, 155, 570);

        g.setColor(new Color(0xBBADA0));
        g.drawRoundRect(155, 110, 400, 400, 20, 20);
        // g.setColor(new Color(0xBBADA0));
        g.fillRoundRect(155, 110, 400, 400, 20, 20);
        g.setColor(Color.black);
        g.drawRoundRect(155, 110, 400, 400, 20, 20);
        g.drawRoundRect(155, 110, 401, 401, 20, 20);
        g.drawRoundRect(154, 109, 401, 401, 20, 20);
        g.drawRoundRect(153, 108, 402, 402, 20, 20);

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                // if there is no square there, then color it nothing
                if (tfe.board[row][col] == null) {
                    // draw outline
                    g.setColor(Color.black);
                    g.drawRoundRect(
                            154 + (col * 100), 109 + (row * 100), 102, 102,
                            20, 20
                    );
                    // fill tile
                    g.setColor(new Color(0xCDC1B4));
                    g.fillRoundRect(
                            155 + (col * 100), 110 + (row * 100), 100, 100,
                            20, 20
                    );
                } else {
                    switch (tfe.board[row][col].getTileValue()) {
                        case 2:
                            g.setColor(color2);
                            break;
                        case 4:
                            g.setColor(color4);
                            break;
                        case 8:
                            g.setColor(color8);
                            break;
                        case 16:
                            g.setColor(color16);
                            break;
                        case 32:
                            g.setColor(color32);
                            break;
                        case 64:
                            g.setColor(color64);
                            break;
                        case 128:
                            g.setColor(color128);
                            break;
                        case 256:
                            g.setColor(color256);
                            break;
                        case 512:
                            g.setColor(color512);
                            break;
                        case 1024:
                            g.setColor(color1024);
                            break;
                        case 2048:
                            g.setColor(color2048);
                            break;
                        default:
                            throw new IllegalArgumentException("funky");
                    }
                    g.fillRoundRect(
                            155 + (col * 100), 110 + (row * 100), 100, 100,
                            20, 20
                    );
                    g.setColor(Color.black);
                    g.drawRoundRect(
                            154 + (col * 100), 109 + (row * 100), 102, 102,
                            20, 20
                    );

                    String number = String.valueOf(tfe.board[row][col].getTileValue());
                    // font metrics to get the correct dimensions for where to draw string
                    FontMetrics fm = g.getFontMetrics();
                    int xPos = 155 + (col * 100) + (100 - fm.stringWidth(number)) / 2;
                    int yPos = 110 + (row * 100) + (fm.getAscent() +
                            (100 - (fm.getAscent() + fm.getDescent())) / 2);
                    g.drawString(number, xPos, yPos);
                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
