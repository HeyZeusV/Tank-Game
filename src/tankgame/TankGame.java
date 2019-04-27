package src.tankgame;

import javax.swing.*;


public class TankGame extends JFrame {

    private PlayZone gameArea;

    public static void main(String[] args) {

        TankGame game = new TankGame();
        game.init();
    }

    public TankGame() {

        init();
    }

    private void init() {

        setTitle("Tank Game");
        gameArea = new PlayZone();
        add(gameArea);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
