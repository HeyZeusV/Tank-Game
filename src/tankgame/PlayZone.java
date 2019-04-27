package src.tankgame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;



public class PlayZone extends JPanel implements ActionListener {

    private BufferedImage background;
    private Tank t1;
    private Tank t2;
    private Timer timer;
    private boolean gameOver;
    private final int SPACE = 32;
    private final int DELAY = 15;
    private final int WIDTH = 1920;
    private final int HEIGHT = 896;
    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<BreakableWall> bWalls = new ArrayList<>();
    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    private GameUI ui;

    // learned this way of printing map from here http://zetcode.com/tutorials/javagamestutorial/sokoban/
    private String level = "############################################################\n"
                          +"#              #                               #           #\n"
                          +"#              #             %                 #     %     #\n"
                          +"#              #            $$$$               #           #\n"
                          +"#              #                               $           #\n"
                          +"#              #                               $$$####$$$$$#\n"
                          +"#                                                          #\n"
                          +"#                                                          #\n"
                          +"#                                                          #\n"
                          +"#    $$$$#######                                           #\n"
                          +"#                            ###$$$###$$$                  #\n"
                          +"#                                                          #\n"
                          +"#                                                          #\n"
                          +"#                                                          #\n"
                          +"#                                                          #\n"
                          +"#                             %              ######        #\n"
                          +"#                                                          #\n"
                          +"#                                                          #\n"
                          +"#                  $############                           #\n"
                          +"#                  $                                       #\n"
                          +"#                  $                        #              #\n"
                          +"#                  $                        #              #\n"
                          +"#####$$$####       $                        #              #\n"
                          +"#          $                  %             #              #\n"
                          +"#          $                                #              #\n"
                          +"#     %    $                                #              #\n"
                          +"#          $                                #              #\n"
                          +"############################################################\n";

    public PlayZone() {

        init();
    }

    private void init() {

        setOpaque(false);
        addKeyListener(new TAdapter());
        setFocusable(true);
        setDoubleBuffered(true);

        Wall wall;
        BreakableWall bwall;
        PowerUp pUp;
        int x = 0;
        int y = 0;
        for (int i = 0; i < level.length(); i++) {

            char item = level.charAt(i);

            if (item == '\n') {

                y += SPACE;
                x = 0;
            } else if (item == '#') {

                wall = new Wall(x, y);
                walls.add(wall);
                x += SPACE;
            } else if (item == '$') {

                bwall = new BreakableWall(x,y);
                bWalls.add(bwall);
                x += SPACE;
            } else if (item == ' ') {

                x += SPACE;
            } else if (item == '%') {

                pUp = new PowerUp(x, y);
                powerUps.add(pUp);
                x += SPACE;
            }
        }
        // creates tank
        t1 = new Tank(32, 32, 0, 0, (short)0, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE);
        t2 = new Tank(1834, 819,0,0,(short)180, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_F);
        try {
            background = ImageIO.read(getClass().getResource("resources/images/Background.jpg"));
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        // creates UI
        ui = new GameUI(t1, t2);
        gameOver = false;

        // timer to know when to update everything
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public Dimension getPreferredSize() {

        return new Dimension(1920,896);
    }


    // I couldn't get the black boxes around my sprites to go away (Linux Mint), even tho my resources where png and I made sure they
    // were transparent.
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.drawImage(background,0,0,this);

        // draws everything
        if (!gameOver) {

            ArrayList<Bullet> bulletsT1 = t1.getBullets();
            ArrayList<Bullet> bulletsT2 = t2.getBullets();
            for (Bullet bullet: bulletsT1) {

                bullet.drawImage(g);
            }
            for (Bullet bullet: bulletsT2) {

                bullet.drawImage(g);
            }
            for (Wall wall: walls) {

                wall.drawImage(g);
            }
            for (BreakableWall bwall: bWalls) {

                bwall.drawImage(g);
            }
            for (PowerUp pUp: powerUps) {

                pUp.drawImage(g);
            }
            t1.drawImage(g);
            t2.drawImage(g);
            ui.drawImage(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        updateTanks();
        updateBullets();
        updateWalls();
        updatePowerUps();
        collision();
        repaint();
    }

    // updates position/health/lives
    private void updateTanks() {

        t1.update();
        t2.update();
    }

    // checks if bullets should still be drawn or discarded
    private void updateBullets() {

        ArrayList<Bullet> bulletsT1 = t1.getBullets();
        ArrayList<Bullet> bulletsT2 = t2.getBullets();

        for (int i = 0; i < bulletsT1.size(); i++) {

            Bullet b = bulletsT1.get(i);
            if (b.isVisible()) {

                b.move();
            } else {

                bulletsT1.remove(i);
            }
        }

        for (int i = 0; i < bulletsT2.size(); i++) {

            Bullet b = bulletsT2.get(i);
            if (b.isVisible()) {

                b.move();
            } else {

                bulletsT2.remove(i);
            }
        }
    }

    // only worries about breakable walls
    // removes them completely so rectangle bounds won't cause collisions
    private void updateWalls() {

        for (int i = 0; i < bWalls.size(); i++) {

            BreakableWall bwall = bWalls.get(i);
            if (!bwall.isVisible()) {

                bWalls.remove(i);
            }
        }
    }

    // checks if they have been picked up or not
    private void updatePowerUps() {

        for (int i = 0; i < powerUps.size(); i++) {

            PowerUp p = powerUps.get(i);
            if (!p.isVisible()) {

                powerUps.remove(i);
            }
        }
    }

    // ugly but it works...
    public void collision() {

        Rectangle r1 = t1.getBounds();
        Rectangle r2 = t2.getBounds();
        ArrayList<Bullet> b1 = t1.getBullets();
        ArrayList<Bullet> b2 = t2.getBullets();

        // checks all the bullets from tank1
        for (Bullet b: b1) {

            // deals with collisions with other tank
            Rectangle r3 = b.getBounds();
            if (r3.intersects(r2)) {

                b.setVisible(false);
                t2.setHealth(t1.getDamage());
                if (t2.getHealth() <= 0) {

                    t2.setLives(t2.getLives() - 1);
                    if (t2.getLives() == 0) {

                        gameOver = true;
                    } else {

                        t2.setHealth(60);
                    }
                }
            }
            // collisions with breakable walls
            for (BreakableWall w: bWalls) {

                if (w.isVisible()) {
                    Rectangle r4 = w.getBounds();
                    if (r4.intersects(r3)) {

                        b.setVisible(false);
                        w.setHealth(-10);
                    }
                }
            }
            // collisions with non breakable walls
            for (Wall w: walls) {

                if (w.isVisible()) {
                    Rectangle r4 = w.getBounds();
                    if (r4.intersects(r3)) {

                        b.setVisible(false);
                    }
                }
            }

        }
        // same as above but for bullets from tank 2
        for (Bullet b: b2) {

            Rectangle r3 = b.getBounds();
            if (r3.intersects(r1)) {

                b.setVisible(false);
                t1.setHealth(t2.getDamage());
                if (t1.getHealth() <= 0) {

                    t1.setLives(t1.getLives() - 1);
                    if (t1.getLives() == 0) {

                        gameOver = true;
                    } else {

                        t1.setHealth(60);
                    }
                }
            }
            for (BreakableWall w: bWalls) {

                if (w.isVisible()) {
                    Rectangle r4 = w.getBounds();
                    if (r4.intersects(r3)) {

                        b.setVisible(false);
                        w.setHealth(-10);
                    }
                }
            }
            for (Wall w: walls) {

                if (w.isVisible()) {
                    Rectangle r4 = w.getBounds();
                    if (r4.intersects(r3)) {

                        b.setVisible(false);
                    }
                }
            }
        }
        // this checks for collisions between tanks and walls, pushes tanks back
        for (Wall w: walls) {

            Rectangle r4 = w.getBounds();
            if (r4.intersects(r1)) {

                t1.pushFromWall();
            } else if (r4.intersects(r2)) {

                t2.pushFromWall();
            }
        }

        for (BreakableWall w: bWalls) {

            if (w.isVisible()) {
                Rectangle r4 = w.getBounds();
                if (r4.intersects(r1)) {

                    t1.pushFromWall();
                } else if (r4.intersects(r2)) {

                    t2.pushFromWall();
                }
            }
        }
        // collisions between power ups and tanks
        for (PowerUp p: powerUps) {

            Rectangle r5 = p.getBounds();
            if (r5.intersects(r1)) {

                if (p.getRandom() == 0) {

                    p.ExtraLife(t1);
                    p.setVisible(false);
                } else if (p.getRandom() == 1) {

                    p.DamageBoost(t1);
                    p.setVisible(false);
                } else {

                    p.FireRate(t1);
                    p.setVisible(false);
                }
            }
            if (r5.intersects(r2)) {

                if (p.getRandom() == 0) {

                    p.ExtraLife(t2);
                    p.setVisible(false);
                } else if (p.getRandom() == 1) {

                    p.DamageBoost(t2);
                    p.setVisible(false);
                } else {

                    p.FireRate(t2);
                    p.setVisible(false);
                }
            }
        }
    }

    // checks for user inputs
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            t1.keyReleased(e);
            t2.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            t1.keyPressed(e);
            t2.keyPressed(e);
        }
    }

}