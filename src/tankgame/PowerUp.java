package src.tankgame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;

public class PowerUp extends JComponent implements Drawable {

    private BufferedImage live;
    private BufferedImage damage;
    private BufferedImage fireRate;
    private int random;
    private int x;
    private int y;
    private final int WIDTH = 28;
    private final int HEIGHT = 28;
    private final int DELAY = 6000;
    private boolean visible;
    private Rectangle bounds;
    Timer timer;

    public PowerUp(int x, int y) {

        this.x = x;
        this.y = y;
        this.visible = true;
        init();
        bounds = new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void init() {

        // used random number generator to generate powerups
        Random rand = new Random();
        try {
            live = ImageIO.read(getClass().getResource("resources/images/1Up.png"));
            damage = ImageIO.read(getClass().getResource("resources/images/DamageBoost.png"));
            fireRate = ImageIO.read(getClass().getResource("resources/images/FireRate.png"));
        } catch (IOException e) {

            e.printStackTrace();
        }
        random = rand.nextInt(3);
    }

    public void drawImage(Graphics g) {

        paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        if (random == 0) {

            g2d.drawImage(live, x, y, this);
        } else if (random == 1) {

            g2d.drawImage(damage, x, y, this);
        } else if (random == 2) {

            g2d.drawImage(fireRate, x, y, this);
        }
    }
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
    }


    // extra life
    public void ExtraLife(Tank t1) {

        t1.setLives(t1.getLives() + 1);
    }

    // quad damage
    public void DamageBoost(Tank t1) {

        int oldDamage = t1.getDamage();
        t1.setDamage(-24);
        timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {

                t1.setDamage(oldDamage);
            }
        }, DELAY);
    }

    // about 3x faster fire rate
    public void FireRate(Tank t1) {

        int oldROF = t1.getRateOfFire();
        t1.setRateOfFire(5);
        timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {

                t1.setRateOfFire(oldROF);
            }
        }, DELAY);
    }

    public Rectangle getBounds() {

        return bounds;
    }

    public int getRandom() {

        return random;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }
}
