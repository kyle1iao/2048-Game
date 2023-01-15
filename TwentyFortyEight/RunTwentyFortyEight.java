package org.cis1200.TwentyFortyEight;

/*
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import javax.swing.*;
import java.awt.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a TicTacToe object to serve as the game's model.
 */
public class RunTwentyFortyEight implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("2048");
        frame.setLocation(300, 10);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton restart = new JButton("Restart Game");
        restart.addActionListener(e -> board.reset());

        // add undo button
        final JButton undo = new JButton("Undo Move");
        // create anonymous function to call undo function if undo button pressed
        undo.addActionListener(e -> board.undo());

        // add save button
        final JButton save = new JButton("Save Progress");
        // add anonymous function
        save.addActionListener(e -> board.save());

        // add resume/load button
        final JButton load = new JButton("Load Save");
        // add anonymous function
        load.addActionListener(e -> board.resume());

        // add instructions button
        final JButton instructions = new JButton("Instructions");
        // add anonymous function
        instructions.addActionListener(e -> board.instructions());

        // add buttons to panel
        control_panel.add(restart);
        control_panel.add(undo);
        control_panel.add(save);
        control_panel.add(load);
        control_panel.add(instructions);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}