package src.tankgame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Tank extends JComponent implements Drawable {

    private int x;
    private int y;
    private final int r = 2;
    private int vx;
    private int vy;
    private int bx;
    private int by;
    private short angle;
    private BufferedImage image;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean FirePressed;
    private final int up;
    private final int down;
    private final int right;
    private final int left;
    private final int fire;
    private ArrayList<Bullet> bullets;
    private int rateOfFire;
    private int repaintCount = 0;
    private int health = 60;
    private int lives = 3;
    private int damage;
    private int oldX;
    private int oldY;
    private Rectangle bounds;

    public Tank(int x, int y, int vx, int vy, short angle, int up, int down, int left, int right, int fire) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;
        this.fire = fire;
        this.rateOfFire = 17;
        this.damage = -6;
        init();
        bounds = new Rectangle(x, y, image.getWidth(), image.getHeight());
        bullets = new ArrayList<>();

    }

    private void init() {

        try {
            image = ImageIO.read(getClass().getResource("resources/images/Tank1.png"));
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    // didn't change
    public void drawImage(Graphics g) {

        paintComponent(g);
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), image.getWidth() / 2, image.getHeight() / 2);
        Graphics2D graphic2D = (Graphics2D) g;
        graphic2D.drawImage(image, rotation, null);
    }
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
    }
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public BufferedImage getImage() {

        return image;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    public void setAngle(short angle) {
        this.angle = angle;
    }

    public void setHealth(int health) {

        this.health += health;
    }

    public void setLives(int lives) {

        this.lives = lives;
    }


    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setRateOfFire(int rateOfFire) {
        this.rateOfFire = rateOfFire;
    }

    public ArrayList<Bullet> getBullets() {

        return bullets;
    }

    public int getRateOfFire() {
        return rateOfFire;
    }

    @Override
    public int getX() {
        return x;
    }

    public int getOldX() {
        return oldX;
    }

    @Override
    public int getY() {
        return y;
    }

    public int getOldY() {
        return oldY;
    }

    public short getAngle() {
        return angle;
    }

    public int getHealth() {

        return health;
    }

    public int getLives() {

        return lives;
    }


    public int getDamage() {
        return damage;
    }

    public void toggleUpPressed() {
        this.UpPressed = true;
    }

    public void toggleDownPressed() {
        this.DownPressed = true;
    }

    public void toggleRightPressed() {
        this.RightPressed = true;
    }

    public void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    public void toggleFirePressed() { this.FirePressed = true;}

    public void unToggleUpPressed() {
        this.UpPressed = false;
    }

    public void unToggleDownPressed() {
        this.DownPressed = false;
    }

    public void unToggleRightPressed() {
        this.RightPressed = false;
    }

    public void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    public void unToogleFirePressed() { this.FirePressed = false;}

    public boolean isUpPressed() {
        return UpPressed;
    }


    // decided to modify the way input was read
    // i didn't understand observable to much so i switched to actionListener
    public void keyPressed(KeyEvent ke) {

        int keyPressed = ke.getKeyCode();
        if (keyPressed == up) {
            toggleUpPressed();
        }
        if (keyPressed == down) {
            toggleDownPressed();
        }
        if (keyPressed == left) {
            toggleLeftPressed();
        }
        if (keyPressed == right) {
            toggleRightPressed();
        }
        if (keyPressed == fire) {
            toggleFirePressed();
        }
        update();
    }

    public void keyReleased(KeyEvent ke) {

        int keyReleased = ke.getKeyCode();
        if (keyReleased  == up) {
            unToggleUpPressed();
            vx = 0;

        }
        if (keyReleased == down) {
            unToggleDownPressed();
            vy = 0;

        }
        if (keyReleased  == left) {
            unToggleLeftPressed();

        }
        if (keyReleased  == right) {
            unToggleRightPressed();
        }
        if (keyReleased == fire) {

            unToogleFirePressed();
        }
        update();
    }

    public void update() {

        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }
        if (this.FirePressed) {

            this.fire();
        }
        bounds.setLocation(x, y);
        this.repaint();
        repaintCount += 1;
    }

    // tweaked movement method in order to get bullet to fire at correct angle
    private void fire() {

        if (repaintCount % rateOfFire == 0) {
            bx = (int) Math.round(r * Math.cos(Math.toRadians(angle)));
            by = (int) Math.round(r * Math.sin(Math.toRadians(angle)));
            bullets.add(new Bullet(x + 10 + 8 * bx,y + 10 + 8 * by, angle));
        }
    }

    // made it 0 -> 360 to help with testing
    private void rotateLeft() {

        if (this.angle - 3 == 0) {

            this.angle = 360;
            this.angle -= 3;
        } else {
            this.angle -= 3;
        }
    }

    private void rotateRight() {

        if (this.angle + 3 == 360) {

            this.angle = 0;
            this.angle += 3;
        } else {
            this.angle += 3;
        }
    }

    private void moveBackwards() {
        vx = (int) Math.round(r * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(r * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
    }

    private void moveForwards() {
        vx = (int) Math.round(r * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(r * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }

    private void checkBorder() {
        if (x < 32) {
            x = 32;
        }
        if (x >= 1834) {
            x = 1834;
        }
        if (y < 32) {
            y = 32;
        }
        if (y >= 819) {
            y = 819;
        }
    }

    public Rectangle getBounds() {

        return bounds;
    }

    // pushes you away from wall depending on which direction you coming from
    // have to push a bit away or else tank goes through wall
    public void pushFromWall() {

        if (DownPressed){

            vx = (int) Math.round(r * Math.cos(Math.toRadians(angle)));
            vy = (int) Math.round(r * Math.sin(Math.toRadians(angle)));
            x += 4 * vx;
            y += 4 * vy;
        } else if (UpPressed) {

            vx = (int) Math.round(r * Math.cos(Math.toRadians(angle)));
            vy = (int) Math.round(r * Math.sin(Math.toRadians(angle)));
            x -= 4 * vx;
            y -= 4 * vy;
        }

    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

}

