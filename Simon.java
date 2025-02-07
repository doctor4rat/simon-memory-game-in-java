package simon;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class Simon extends JPanel {
    private JButton green;
    private JButton red;
    private JButton yellow;
    private JButton blue;
    private List<Integer> sequence;
    private int currentStep;
    private Random random;
    private boolean playing;

    public Simon() {
        //construct components
        green = new JButton("green");
        red = new JButton("red");
        yellow = new JButton("yellow");
        blue = new JButton("blue");

        // add font
        green.setFont(new Font("Dialog", Font.BOLD, 16));
        red.setFont(new Font("Dialog", Font.BOLD, 16));
        yellow.setFont(new Font("Dialog", Font.BOLD, 16));
        blue.setFont(new Font("Dialog", Font.BOLD, 16));

        // add b g color
        green.setBackground(Color.decode("#32CD32"));
        red.setBackground(Color.decode("#FF4500"));
        yellow.setBackground(Color.decode("#FFD700"));
        blue.setBackground(Color.decode("#1E90FF"));

        //adjust size and set layout
        setPreferredSize(new Dimension(944, 566));
        setLayout(new GridLayout(2, 2, 0, 0)); // Corrected: 2 rows, 2 columns for 4 buttons

        //add components
        add(green);
        add(red);
        add(yellow);
        add(blue);

        sequence = new ArrayList<>();
        random = new Random();
        currentStep = 0;
        playing = false;

        // Add action listeners to buttons
        green.addActionListener(new ColorListener(0));
        red.addActionListener(new ColorListener(1));
        yellow.addActionListener(new ColorListener(2));
        blue.addActionListener(new ColorListener(3));

        startGame();
    }

    private void startGame() {
        sequence.clear();
        currentStep = 0;
        playing = true;
        addStep();
        playSequence();
    }

    private void addStep() {
        sequence.add(random.nextInt(4));
    }

    private void playSequence() {
        if (currentStep < sequence.size()) {
            int colorIndex = sequence.get(currentStep);
            highlightButton(colorIndex);
            Timer timer = new Timer(500, e -> {
                unhighlightButton(colorIndex);
                currentStep++;
                playSequence();
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            currentStep = 0; // Reset for player input
            playing = false;
        }
    }

    private void highlightButton(int colorIndex) {
        JButton button = getButton(colorIndex);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5)); // Highlight with white border
    }

    private void unhighlightButton(int colorIndex) {
        JButton button = getButton(colorIndex);
        button.setBorder(null); // Remove highlight
    }


    private JButton getButton(int colorIndex) {
        switch (colorIndex) {
            case 0: return green;
            case 1: return red;
            case 2: return yellow;
            case 3: return blue;
            default: return null; // Should not happen
        }
    }

    private class ColorListener implements ActionListener {
        private int colorIndex;

        public ColorListener(int colorIndex) {
            this.colorIndex = colorIndex;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!playing) {
                if (colorIndex == sequence.get(currentStep)) {
                    highlightButton(colorIndex);
                    Timer timer = new Timer(200, ev -> unhighlightButton(colorIndex)); // Shorter highlight
                    timer.setRepeats(false);
                    timer.start();
                    currentStep++;
                    if (currentStep == sequence.size()) {
                        currentStep = 0;
                        addStep();
                        playSequence();
                    }
                } else {
                    JOptionPane.showMessageDialog(Simon.this, "Game Over!");
                    startGame();
                }
            }
        }
    }



    public static void main(String[] args) {
        JFrame frame = new JFrame("Simon Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new Simon());
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
